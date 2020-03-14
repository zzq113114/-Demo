/**
 * @(#)PublicService.java Created on 2012-1-5 上午11:40:38
 * 
 * 版权：版权所有 bsoft 保留所有权力。
 */
package phis.application.pub.source;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import phis.source.BSPHISEntryNames;
import phis.source.BaseDAO;
import phis.source.ModelDataOperationException;
import phis.source.PersistentDataOperationException;
import phis.source.service.AbstractActionService;
import phis.source.service.DAOSupportable;
import phis.source.utils.BCLUtil;
import phis.source.utils.CNDHelper;
import phis.source.utils.OpLogUtil;
import phis.source.utils.ParameterUtil;
import ctd.account.UserRoleToken;
import ctd.dictionary.DictionaryController;
import ctd.service.core.ServiceException;
import ctd.util.annotation.RpcService;
import ctd.util.context.Context;

/**
 * @description
 * 
 * @author <a href="mailto:huangpf@bsoft.com.cn">huangpf</a>
 */
public class PublicService extends AbstractActionService implements
		DAOSupportable {

	static Logger logger = LoggerFactory.getLogger(PublicService.class);

	/**
	 * 保存业务操作日志
	 */
	@SuppressWarnings("unchecked")
	public void doSaveOpLog(Map<String, Object> req, Map<String, Object> res,
			BaseDAO dao, Context ctx) throws ServiceException {
		Map<String, Object> body = (Map<String, Object>) req.get("body");
		// String moduleId = (String) body.get("moduleId");
		String desc = (String) body.get("desc");
		String snapshot = (String) body.get("logInfo");
		OpLogUtil.writeLog(desc, snapshot);
	}

	/**
	 * 病历锁功能,支持单业务或者数组方式
	 * 
	 * @param req
	 * @param res
	 * @param dao
	 * @param ctx
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public void doBclLock(Map<String, Object> req, Map<String, Object> res,
			BaseDAO dao, Context ctx) throws ServiceException {
		Map<String, Object> lockKeys = new HashMap<String, Object>();
		Object body = req.get("body");
		if (body == null)
			return;
		Session session = (Session) ctx.get(Context.DB_SESSION);
		try {
			session.beginTransaction();
			if (body instanceof List) {
				for (Map<String, Object> m : (List<Map<String, Object>>) body) {
					Map<String, Object> r = BCLUtil.lock(m, dao);
					if (r != null) {
						lockKeys.put(m.get("YWXH").toString(), r.get("JLXH"));
					}
				}
			} else {
				Map<String, Object> r = BCLUtil.lock(
						(Map<String, Object>) body, dao);
				if (r != null) {
					lockKeys.put(((Map<String, Object>) body).get("YWXH")
							.toString(), r.get("JLXH"));
				}
			}
			session.getTransaction().commit();
			res.put("body", lockKeys);
		} catch (ModelDataOperationException e) {
			session.getTransaction().rollback();
			throw new ServiceException(e);
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void doBclUnlock(Map<String, Object> req, Map<String, Object> res,
			BaseDAO dao, Context ctx) throws ServiceException {
		Object body = req.get("body");
		if (body == null)
			return;
		try {
			if (body instanceof List) {
				for (Map<String, Object> m : (List<Map<String, Object>>) body) {
					BCLUtil.unlock(m, dao);
				}
			} else {
				BCLUtil.unlock((Map<String, Object>) body, dao);
			}
		} catch (ModelDataOperationException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void doCheckBclLock(Map<String, Object> req,
			Map<String, Object> res, BaseDAO dao, Context ctx)
			throws ServiceException {
		Map<String, Object> resBody = new HashMap<String, Object>();
		Object body = req.get("body");
		if (body == null)
			return;
		try {
			if (body instanceof List) {
				for (Map<String, Object> m : (List<Map<String, Object>>) body) {
					resBody.put(m.get("YWXH").toString(),
							BCLUtil.checkLock(m, dao));
				}
			} else {
				resBody.put(
						((Map<String, Object>) body).get("YWXH").toString(),
						BCLUtil.checkLock((Map<String, Object>) body, dao));

			}
			res.put("body", resBody);
		} catch (ModelDataOperationException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 获取系统参数
	 * 
	 * @param req
	 * @param res
	 * @param dao
	 * @param ctx
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public void doLoadSystemParams(Map<String, Object> req,
			Map<String, Object> res, BaseDAO dao, Context ctx)
			throws ServiceException {
		Map<String, Object> body = (Map<String, Object>) req.get("body");
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<String> commons = body.containsKey("commons") ? (List<String>) body
				.get("commons") : null;
		List<String> privates = body.containsKey("privates") ? (List<String>) body
				.get("privates") : null;
		List<String> personals = body.containsKey("personals") ? (List<String>) body
				.get("personals") : null;
		UserRoleToken user = UserRoleToken.getCurrent();
		String manageUnit = user.getManageUnit().getId();
		if (commons != null) {
			for (String _CSMC : commons) {
				retMap.put(_CSMC, ParameterUtil.getParameter(
						ParameterUtil.getTopUnitId(), _CSMC, ctx));
			}
		}
		if (privates != null) {
			for (String _CSMC : privates) {
				retMap.put(_CSMC,
						ParameterUtil.getParameter(manageUnit, _CSMC, ctx));
			}
		}
		if (personals != null) {
			List<Object> cnd = CNDHelper.createSimpleCnd("eq", "YHBH", "s",
					user.getUserId());
			body = null;
			try {
				Map<String, Object> ysxg = dao.doLoad(cnd,
						BSPHISEntryNames.EMR_YSXG_GRCS);
				if (ysxg != null) {
					retMap.putAll(ysxg);
				}
			} catch (PersistentDataOperationException e) {
				throw new ServiceException("加载医生个人习惯失败!", e);
			}
		}
		res.put("body", retMap);
	}

	/**
	 * 保存系统参数分类信息
	 * 
	 * @param req
	 * @param res
	 * @param dao
	 * @param ctx
	 * @throws ServiceException
	 */
	public void doSaveSystemParamsType(Map<String, Object> req,
			Map<String, Object> res, BaseDAO dao, Context ctx)
			throws ServiceException {
		// Map<String, Object> body = (Map<String, Object>) req.get("body");
		// PublicModel pm = new PublicModel();

		// pm.doSaveSystemParamsType(body,ctx);
	}

	/**
	 * 
	 */
	public List<String> getNoDBActions() {
		List<String> list = new ArrayList<String>();
		list.add("getCurrentDate");
		list.add("calculateAge");
		list.add("personAge");
		list.add("getServerDate");
		list.add("getServerDateTime");
		list.add("reloadDictionarys");
		return list;
	}

	/**
	 * 重载字典功能
	 * 
	 * @param req
	 * @param res
	 * @param ctx
	 */
	@SuppressWarnings("unchecked")
	public void doReloadDictionarys(Map<String, Object> req,
			Map<String, Object> res, Context ctx) {
		List<String> body = (List<String>) req.get("body");
		if (body == null || body.size() <= 0) {
			DictionaryController.instance().reloadAll();
		} else {
			for (String dicId : body) {
				DictionaryController.instance().reload(dicId);
			}
		}
	}

	// 小于3*12个月而又大于等于1*12个月的，用岁月表示；

	/**
	 * 
	 * @param req
	 * @param res
	 * @param dao
	 * @param ctx
	 */
	@RpcService
	public void doGetServerDate(Map<String, Object> req,
			Map<String, Object> res, Context ctx) {
		res.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}

	/**
	 * 
	 * @param req
	 * @param res
	 * @param dao
	 * @param ctx
	 */
	@RpcService
	public void doGetServerDateTime(Map<String, Object> req,
			Map<String, Object> res, Context ctx) {
		res.put("dateTime",
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

}
