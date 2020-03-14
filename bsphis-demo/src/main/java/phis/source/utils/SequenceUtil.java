package phis.source.utils;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.impl.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ctd.dao.SimpleDAO;
import ctd.dao.exception.DataAccessException;
import ctd.util.AppContextHolder;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

/**
 * 序列工具类
 * 
 * @author yangl
 * 
 */
public class SequenceUtil {
	/**
	 * 缓存数量
	 */
	private static final int CACHE_COUNT = 20;
	private static final Logger logger = LoggerFactory
			.getLogger(SimpleDAO.class);

	/**
	 * 实现内存高速缓存
	 */
	private static Map<String, Object> cacheKeys = new HashMap<String, Object>();

	/**
	 * 
	 * @param entryName
	 *            获取entryName对应表当前最大值，用于自动创建序列
	 * @param rec
	 *            主键生成策略，主要获取seqName参数(seqName：序列名称)
	 * @param pkey
	 *            entryName需要序列的字段
	 * @param obj
	 *            暂时不启用，后期扩展
	 * @return
	 * @throws DataAccessException
	 */
	public static String getKey(String entryName, HashMap<String, String> rec,
			String pkey, Object obj) throws DataAccessException {
		Context ctx = ContextUtils.getContext();
		Session ss = null;
		Object key = null;
		if (ctx.has(Context.DB_SESSION)) {
			ss = (Session) ctx.get(Context.DB_SESSION);
		} else {
			throw new DataAccessException("MissingDatebaseConnection");
		}
		// 判断entryName是否为全路径形式，如果是，截取最后
		if (entryName.contains(".")) {
			entryName = entryName.substring(entryName.lastIndexOf(".") + 1);
		}
		// 判断是否有存在序列名称
		String seqName = "SEQ_" + entryName; // 默认序列名称
		if (rec.containsKey("seqName")) {
			seqName = rec.get("seqName");
		}
		SessionFactory sf = AppContextHolder.getBean(
				AppContextHolder.DEFAULT_SESSION_FACTORY, SessionFactory.class);
		String dialectName = ((SessionFactoryImpl) sf).getDialect().getClass()
				.getName();
		Long startValue = 1l;
		if (rec.containsKey("startPos")) {
			startValue = Long.parseLong(rec.get("startPos").toString());
		}
		// oracle 序列实现
		if (dialectName.contains("MyOracle")) {
			StringBuilder sql = new StringBuilder("select ");
			sql.append(seqName);
			sql.append(".nextval from dual");
			try {
				key = ss.createSQLQuery(sql.toString()).uniqueResult();
			} catch (HibernateException e) {
				logger.error("can't find sequence [" + seqName
						+ "],attempt to create it and try again");
				// 尝试创建序列并重试 oracle
				Object maxValue = ss.createQuery(
						"select max(" + pkey + ") from " + entryName)
						.uniqueResult();
				if (maxValue != null
						&& Long.parseLong(maxValue.toString()) > startValue) {
					startValue = Long.parseLong(maxValue.toString()) + 1;
				}
				StringBuilder seqSql = new StringBuilder("create sequence ");
				seqSql.append(seqName);
				seqSql.append(" minvalue ");
				seqSql.append(startValue);
				seqSql.append(" maxvalue 9999999999 start with ");
				seqSql.append(startValue);
				seqSql.append(" increment by 1 cache 20");
				ss.createSQLQuery(seqSql.toString()).executeUpdate();
				ss.flush();
				key = ss.createSQLQuery(sql.toString()).uniqueResult();
			}
		} else { // 不支持序列的情况
			key = getCacheKey(entryName);
			if (key == null) {
				// 内存缓存 + 数据库序列 --缺点:集群环境下不连续
				// 建立序列表，模拟序列
				// 自己控制事务
				SessionFactory seq_sf = AppContextHolder.getBean(
						AppContextHolder.DEFAULT_SESSION_FACTORY,
						SessionFactory.class);
				Session seq_ss = seq_sf.openSession();
				try {
					seq_ss.beginTransaction();
					String sql = "select seq_currval from sys_seq where seq_name=:seqname for update";
					key = seq_ss.createSQLQuery(sql)
							.setParameter("seqname", seqName).uniqueResult();
					if (key == null) {
						// 没有对应的序列，创建
						Object maxValue = seq_ss.createQuery(
								"select max(" + pkey + ") from " + entryName)
								.uniqueResult();
						if (maxValue != null
								&& Long.parseLong(maxValue.toString()) > startValue) {
							startValue = Long.parseLong(maxValue.toString()) + 1;
						}
						String createSql = "insert into sys_seq values(:seqname,1,9999999999,:seq_currval)";
						int i = seq_ss.createSQLQuery(createSql)
								.setParameter("seqname", seqName)
								.setParameter("seq_currval", startValue)
								.executeUpdate();
						if (i > 0) {
							key = seq_ss.createSQLQuery(sql)
									.setParameter("seqname", seqName)
									.uniqueResult();
						}
					}
					if (key != null) {
						seq_ss.createSQLQuery(
								"update sys_seq set seq_currval = seq_currval + :seq_cache where seq_name=:seqname")
								.setParameter("seqname", seqName)
								.setParameter("seq_cache", CACHE_COUNT + 1)
								.executeUpdate();
					}
					seq_ss.getTransaction().commit();

					setCacheKey(entryName, key);
				} catch (HibernateException e) {
					if (seq_ss.getTransaction().isActive()) {
						seq_ss.getTransaction().rollback();
					}
					throw new DataAccessException("MissingDatebaseSequence");
				} finally {
					seq_ss.close();
				}
			}
		}
		return key == null ? null : key.toString();
	}

	private synchronized static void setCacheKey(String entryName, Object key) {
		System.out.println("set cache:" + key);
		Long[] cur_max = new Long[2];
		cur_max[0] = Long.parseLong(key.toString()) + 1;
		cur_max[1] = Long.parseLong(cur_max[0].toString()) + CACHE_COUNT - 1;
		cacheKeys.put(entryName, cur_max);

	}

	/**
	 * 获取缓存key
	 * 
	 * @param entryName
	 * @return
	 */
	private static String getCacheKey(String entryName) {
		synchronized (entryName) {
			if (cacheKeys.containsKey(entryName)
					&& cacheKeys.get(entryName) != null) {
				Long[] cur_max = ((Long[]) cacheKeys.get(entryName));
				Long curval = cur_max[0];
				if (cur_max[0].longValue() == cur_max[1].longValue()) {
					cacheKeys.put(entryName, null);
				} else {
					cur_max[0]++;
				}
				return curval.toString();
			}
		}
		return null;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 99; i++) {
			String key = getCacheKey("abc");
			if (key == null) {
				setCacheKey("abc", 1 + i);
				key = getCacheKey("abc");
			}
			System.out.println(key);
		}
	}
}
