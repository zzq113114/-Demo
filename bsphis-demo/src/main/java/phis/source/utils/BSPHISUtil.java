package phis.source.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.SessionFactory;
import org.hibernate.impl.SessionFactoryImpl;

import phis.source.BSPHISEntryNames;
import phis.source.BaseDAO;
import phis.source.ModelDataOperationException;
import phis.source.PersistentDataOperationException;
import phis.source.service.ServiceCode;
import ctd.account.UserRoleToken;
import ctd.service.core.ServiceException;
import ctd.util.AppContextHolder;
import ctd.util.context.Context;
import ctd.validator.ValidateException;

public class BSPHISUtil {
	private static Map<String, Long> mzlbCache = new ConcurrentHashMap<String, Long>();

	
	/**
	 * 数字金额转大写中文金额
	 * @param amount
	 * @return
	 */
	public static String changeMoneyUpper(Object amount) {
		if (amount == null || amount.toString().length() == 0)
			return null;
		String sNumber = amount.toString();
		String[] oneUnit = { "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾",
				"佰", "仟", "兆", "拾", "佰", "仟" };
		String[] twoUnit = { "分", "角" };
		String[] sChinese = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String sign = ""; // 符号位 add by Zhangxw
		if (sNumber.startsWith("-")) {
			sNumber = sNumber.substring(1);
			sign = "负";
		}
		int pointPos = sNumber.indexOf("."); // 小数点的位置
		String sInteger;// 记录整数部分
		String sDecimal;// 记录小数部分
		String value = "";// 记录返回结果
		if (pointPos != -1) {
			// 分解并记录整数部分，注意substring()的用法
			sInteger = sNumber.substring(0, pointPos); // 分解并记录小数部分
			sDecimal = sNumber.substring(pointPos + 1, pointPos + 3 < sNumber
					.length() ? pointPos + 3 : sNumber.length());
		} else { // 没有小数部分的情况
			sInteger = sNumber;
			sDecimal = "";
		} // 格式化整数部分，并记录到返回结果
		for (int i = 0; i < sInteger.length(); i++) {
			int temp = Integer.parseInt(sInteger.substring(i, i + 1));
			value += sChinese[temp] + oneUnit[sInteger.length() - i - 1];
		} // 格式化小数部分，并记录到返回结果
		for (int i = 0; i < sDecimal.length(); i++) {
			int temp = Integer.parseInt(sDecimal.substring(i, i + 1));
			// value += sChinese[temp] + twoUnit[sDecimal.length() - i - 1];
			value += sChinese[temp] + twoUnit[2 - i - 1];
		}
		return sign + value;
	}

	/**
	 * 获取MZLB的方法
	 * 
	 * @param manaUnitId
	 * @param dao
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static long getMZLB(String manaUnitId, BaseDAO dao)
			throws ModelDataOperationException {
		if (mzlbCache.containsKey(manaUnitId)) {
			return mzlbCache.get(manaUnitId);
		}
		Map<String, Object> MS_MZLB = new HashMap<String, Object>();
		MS_MZLB.put("JGID", manaUnitId);
		MS_MZLB.put("MZMC", "门诊");
		List<Map<String, Object>> MZLB_List;
		Long mzlb = 0l;
		try {
			MZLB_List = dao
					.doQuery(
							"select MZLB as MZLB from MS_MZLB where JGID=:JGID and MZMC=:MZMC",
							MS_MZLB);
			if (MZLB_List.size() > 0) {
				mzlb = Long.parseLong(MZLB_List.get(0).get("MZLB") + "");// 门诊类别
			} else {
				Map<String, Object> MZLB = dao.doSave("create",
						BSPHISEntryNames.MS_MZLB, MS_MZLB, false);
				mzlb = Long.parseLong(MZLB.get("MZLB").toString() + "");
			}
			mzlbCache.put(manaUnitId, mzlb);
			return mzlb;
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException("获取门诊类别失败!", e);
		} catch (ValidateException e) {
			throw new ModelDataOperationException("获取门诊类别失败!", e);
		}
	}

	/**
	 * tochar函数转化
	 * 
	 * @param prop
	 * @param format
	 * @return
	 */
	public static String toChar(String prop, String format) {
		// 由于使用的是sql查询，无法使用hql中的str自动转化，增加判断收工更改，只适用oracle与DB2
		String tochar = "";
		// WebApplicationContext wac = AppContextHolder.get();
		// SessionFactory sf = (SessionFactory) wac.getBean("mySessionFactory");
		SessionFactory sf = AppContextHolder.getBean(
				AppContextHolder.DEFAULT_SESSION_FACTORY, SessionFactory.class);
		String dialectName = ((SessionFactoryImpl) sf).getDialect().getClass()
				.getName();
		if (dialectName.contains("MyDB2")) {
			tochar = "char(" + prop + ")";
		} else {
			tochar += "to_char(" + prop + ",'" + format + "')";
		}
		return tochar;
	}

	/**
	 * tochar函数转化
	 * 
	 * @param prop
	 * @param format
	 * @return
	 */
	public static String toDate(String value, String format) {
		// 由于使用的是sql查询，无法使用hql中的str自动转化，增加判断收工更改，只适用oracle与DB2
		String todate = "";
		// WebApplicationContext wac = AppContextHolder.get();
		// SessionFactory sf = (SessionFactory) wac.getBean("mySessionFactory");
		SessionFactory sf = AppContextHolder.getBean(
				AppContextHolder.DEFAULT_SESSION_FACTORY, SessionFactory.class);
		String dialectName = ((SessionFactoryImpl) sf).getDialect().getClass()
				.getName();
		if (dialectName.contains("MyDB2")) {
			todate = "date(" + value + ")";
		} else {
			todate += "to_date('" + value + "','" + format + "')";
		}
		return todate;
	}

	/**
	 * 性质转换
	 * 
	 * @param oldBrxz
	 * @param newBrxz
	 * @param list_ZY_FYMX
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static List<Map<String, Object>> change(long oldBrxz, long newBrxz,
			List<Map<String, Object>> list_ZY_FYMX, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		for (int i = 0; i < list_ZY_FYMX.size(); i++) {
			Map<String, Object> ZY_FYMX = list_ZY_FYMX.get(i);
			int YPLX = (Integer) ZY_FYMX.get("YPLX");
			long FYXH = Long.parseLong(ZY_FYMX.get("FYXH") + "");
			long FYGB = Long.parseLong(ZY_FYMX.get("FYXM") + "");
			double FYDJ = Double.parseDouble(ZY_FYMX.get("FYDJ") + "");
			double FYSL = Double.parseDouble(ZY_FYMX.get("FYSL") + "");
			Map<String, Object> je = getje(YPLX, newBrxz, FYXH, FYGB, FYDJ,
					FYSL, dao, ctx);
			ZY_FYMX.put("ZFBL", je.get("ZFBL"));
			ZY_FYMX.put("ZFJE", je.get("ZFJE"));
			ZY_FYMX.put("ZLJE", je.get("ZLJE"));
			ZY_FYMX.put("ZJJE", je.get("ZJJE"));
			list_ZY_FYMX.set(i, ZY_FYMX);
		}
		return list_ZY_FYMX;
	}

	/**
	 * 获取金额
	 * 
	 * @param al_yplx
	 * @param al_brxz
	 * @param al_fyxh
	 * @param al_fygb
	 * @param ad_fydj
	 * @param ad_fysl
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static Map<String, Object> getje(int al_yplx, long al_brxz,
			long al_fyxh, long al_fygb, double ad_fydj, double ad_fysl,
			BaseDAO dao, Context ctx) throws ModelDataOperationException {
		double ld_zfbl = 1;
		boolean adc_zfbl = false;
		double ld_cxbl = 1;
		double ld_zfje = 0;
		double ld_zlje = 0;
		double ld_fyxe = 0;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("BRXZ", al_brxz);

		try {
			if (al_yplx == 0) {
				parameters.put("FYXH", al_fyxh);
				Map<String, Object> GY_FYJY = dao
						.doLoad("Select ZFBL as ZFBL,FYXE as FYXE,CXBL as CXBL From GY_FYJY Where BRXZ= :BRXZ And FYXH= :FYXH",
								parameters);
				if (GY_FYJY != null) {
					if (GY_FYJY.get("FYXE") != null) {
						ld_fyxe = Double.parseDouble(GY_FYJY.get("FYXE") + "");
					}
					if (GY_FYJY.get("CXBL") != null) {
						ld_cxbl = Double.parseDouble(GY_FYJY.get("CXBL") + "");
					}
					if (GY_FYJY.get("ZFBL") != null) {
						ld_zfbl = Double.parseDouble(GY_FYJY.get("ZFBL") + "");
						adc_zfbl = true;
					}
				}
				if (ld_fyxe > 0 && ad_fydj > ld_fyxe) {
					ld_zfje = ad_fysl * ld_fyxe * ld_zfbl;
					ld_zlje = ad_fysl * (ad_fydj - ld_fyxe) * ld_cxbl;
					ld_zfje = ld_zfje + ld_zlje;
				} else {
					ld_zfje = ad_fysl * ad_fydj * ld_zfbl;
					ld_zlje = 0;
				}
			} else if (al_yplx == 1 || al_yplx == 2 || al_yplx == 3) {
				parameters.put("YPXH", al_fyxh);
				Map<String, Object> GY_YPJY = dao
						.doLoad("Select ZFBL as ZFBL From GY_YPJY Where BRXZ= :BRXZ And YPXH= :YPXH",
								parameters);
				if (GY_YPJY != null) {
					if (GY_YPJY.get("ZFBL") != null) {
						ld_zfbl = Double.parseDouble(GY_YPJY.get("ZFBL") + "");
						adc_zfbl = true;
					}
				}
				ld_zfje = ad_fysl * ad_fydj * ld_zfbl;
				ld_zlje = 0;
			}
			if (!adc_zfbl) {
				al_fygb = getfygb(al_yplx, al_fyxh, dao, ctx);
				parameters.remove("YPXH");
				parameters.remove("FYXH");
				parameters.put("SFXM", al_fygb);
				Map<String, Object> GY_ZFBL = dao
						.doLoad("Select ZFBL as ZFBL From GY_ZFBL Where BRXZ= :BRXZ And SFXM= :SFXM",
								parameters);
				if (GY_ZFBL != null) {
					if (GY_ZFBL.get("ZFBL") != null) {
						ld_zfbl = Double.parseDouble(GY_ZFBL.get("ZFBL") + "");
					}
				}
				ld_zfje = ad_fysl * ad_fydj * ld_zfbl;
				ld_zlje = 0;
			}
			double ld_zjje = ad_fydj * ad_fysl;
			Map<String, Object> Getje = new HashMap<String, Object>();
			Getje.put("ZFBL", ld_zfbl);
			Getje.put("ZFJE", ld_zfje);
			Getje.put("ZLJE", ld_zlje);
			Getje.put("ZJJE", ld_zjje);
			Getje.put("FYGB", al_fygb);
			return Getje;
		} catch (PersistentDataOperationException e) {
			e.printStackTrace();
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "金额信息查询失败");
		}
	}

	
	/**
	 * 获取费用归并
	 * 
	 * @param al_yplx
	 * @param al_fyxh
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static long getfygb(long al_yplx, long al_fyxh, BaseDAO dao,
			Context ctx) throws ModelDataOperationException {
		long al_fygb = 0;
		if (al_yplx == 1 || al_yplx == 2 || al_yplx == 3) {// 如果不是费用,先查询有吴账簿类别,没有账簿类别则按药品类型分
			StringBuffer hql_zblb = new StringBuffer();
			hql_zblb.append("select ZBLB as ZBLB from YK_TYPK where YPXH=:ypxh");
			Map<String, Object> map_par = new HashMap<String, Object>();
			map_par.put("ypxh", al_fyxh);
			try {
				Map<String, Object> map_zblb = dao.doLoad(hql_zblb.toString(),
						map_par);
				if (map_zblb != null && map_zblb.size() != 0
						&& map_zblb.get("ZBLB") != null
						&& Long.parseLong(map_zblb.get("ZBLB") + "") != 0) {
					al_fygb = Long.parseLong(map_zblb.get("ZBLB") + "");
				} else {
					throw new ModelDataOperationException(
							ServiceCode.CODE_DATABASE_ERROR, "药品账簿类别查询失败");
				}
			} catch (PersistentDataOperationException e) {
				throw new ModelDataOperationException(
						ServiceCode.CODE_DATABASE_ERROR, "药品账簿类别查询失败");
			}
		} else {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("FYXH", al_fyxh);
			try {
				Map<String, Object> GY_YLSF = dao.doLoad(
						"SELECT FYGB as FYGB FROM GY_YLSF WHERE FYXH = :FYXH",
						parameters);
				al_fygb = Long.parseLong(GY_YLSF.get("FYGB") + "");
			} catch (PersistentDataOperationException e) {
				throw new ModelDataOperationException(
						ServiceCode.CODE_DATABASE_ERROR, "费用归并查询失败");
			}
		}
		return al_fygb;
	}

	/**
	 * double四舍五入保留小数
	 * 
	 * @param d
	 * @param i
	 * @return
	 */
	public static double getDouble(Object d, int i) {
		if (d == null) {
			d = 0d;
		}
		String rStr = String.format("%." + i + "f", Double.parseDouble(d + ""));
		double rd = Double.parseDouble(rStr);
		return rd;
	}

	/**
	 * add by zhangyq 获取合计金额
	 * 
	 * @param BRXX
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static Map<String, Object> gf_zy_gxmk_getjkhj(
			Map<String, Object> BRXX, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		BRXX.put("JKHJ", 0);
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(
					"ZYH",
					Long.parseLong(BRXX.get("ZYH") == null ? "0" : BRXX.get(
							"ZYH").toString()));
			if (Integer.parseInt(BRXX.get("JSLX") + "") == 0
					|| Integer.parseInt(BRXX.get("JSLX") + "") == 5) {
				Map<String, Object> ZY_TBKK = dao
						.doLoad("SELECT sum(JKJE) as JKHJ FROM ZY_TBKK WHERE ZYH  = :ZYH AND ZFPB = 0 AND JSCS = 0",
								parameters);
				if (ZY_TBKK.get("JKHJ") != null) {
					BRXX.put("JKHJ", ZY_TBKK.get("JKHJ"));
				}
			} else if (Integer.parseInt(BRXX.get("JSLX") + "") < 0
					|| Integer.parseInt(BRXX.get("JSLX") + "") == 10) {
				parameters.put("JSCS", BRXX.get("JSCS"));
				Map<String, Object> ZY_ZYJS = dao
						.doLoad("SELECT sum(JKHJ) as JKHJ FROM ZY_ZYJS WHERE ZYH  = :ZYH AND JSCS = :JSCS",
								parameters);
				if (ZY_ZYJS.get("JKHJ") != null) {
					BRXX.put("JKHJ", ZY_ZYJS.get("JKHJ"));
				}
			}
			return BRXX;
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "获取合计金额失败");
		}
	}

	/**
	 * 获取合计金额
	 * 
	 * @param BRXX
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static Map<String, Object> gf_jc_gxmk_getjkhj(
			Map<String, Object> BRXX, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		BRXX.put("JKHJ", 0);
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(
					"ZYH",
					Long.parseLong(BRXX.get("ZYH") == null ? "0" : BRXX.get(
							"ZYH").toString()));
			if (Integer.parseInt(BRXX.get("JSLX") + "") == 0
					|| Integer.parseInt(BRXX.get("JSLX") + "") == 5) {
				Map<String, Object> JC_TBKK = dao
						.doLoad("SELECT sum(JKJE) as JKHJ FROM JC_TBKK WHERE ZYH  = :ZYH AND ZFPB = 0 AND JSCS = 0",
								parameters);
				if (JC_TBKK.get("JKHJ") != null) {
					BRXX.put("JKHJ", JC_TBKK.get("JKHJ"));
				}
			} else if (Integer.parseInt(BRXX.get("JSLX") + "") < 0
					|| Integer.parseInt(BRXX.get("JSLX") + "") == 10) {
				parameters.put("JSCS", BRXX.get("JSCS"));
				Map<String, Object> JC_JCJS = dao
						.doLoad("SELECT sum(JKHJ) as JKHJ FROM JC_JCJS WHERE ZYH  = :ZYH AND JSCS = :JSCS",
								parameters);
				if (JC_JCJS.get("JKHJ") != null) {
					BRXX.put("JKHJ", JC_JCJS.get("JKHJ"));
				}
			}
			return BRXX;
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "获取合计金额失败");
		}
	}

	/**
	 * add by zhangyq 获取自负金额
	 * 
	 * @param BRXX
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static Map<String, Object> gf_zy_gxmk_getzfhj(
			Map<String, Object> BRXX, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		BRXX.put("ZFHJ", 0);
		BRXX.put("YL_ZFHJ", 0);
		BRXX.put("YP_ZFHJ", 0);
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(
					"ZYH",
					Long.parseLong(BRXX.get("ZYH") == null ? "0" : BRXX.get(
							"ZYH").toString()));
			if (Integer.parseInt(BRXX.get("JSLX") + "") == 0
					|| Integer.parseInt(BRXX.get("JSLX") + "") == 5) {
				Map<String, Object> ZY_FYMX = dao
						.doLoad("SELECT nvl(sum(ZFJE),0) as ZFHJ FROM ZY_FYMX WHERE ZYH=:ZYH AND JSCS=0",
								parameters);
				if (ZY_FYMX.get("ZFHJ") != null) {
					BRXX.put("ZFHJ", ZY_FYMX.get("ZFHJ"));
				}
				Map<String, Object> YL_ZY_FYMX = dao
						.doLoad("SELECT nvl(sum(ZFJE),0) as ZFHJ FROM ZY_FYMX WHERE ZYH  = :ZYH AND JSCS = 0 and YPLX = 0",
								parameters);
				if (YL_ZY_FYMX.get("ZFHJ") != null) {
					BRXX.put("YL_ZFHJ", YL_ZY_FYMX.get("ZFHJ"));
				}
				Map<String, Object> YP_ZY_FYMX = dao
						.doLoad("SELECT nvl(sum(ZFJE),0) as ZFHJ FROM ZY_FYMX WHERE ZYH  = :ZYH AND JSCS = 0 and YPLX <> 0",
								parameters);
				if (YP_ZY_FYMX.get("ZFHJ") != null) {
					BRXX.put("YP_ZFHJ", YP_ZY_FYMX.get("ZFHJ"));
				}
			} else if (Integer.parseInt(BRXX.get("JSLX") + "") < 0
					|| Integer.parseInt(BRXX.get("JSLX") + "") == 10) {
				parameters.put("JSCS", BRXX.get("JSCS"));
				Map<String, Object> ZY_ZYJS = dao
						.doLoad("SELECT sum(ZFHJ) as ZFHJ FROM ZY_ZYJS WHERE ZYH  = :ZYH AND JSCS = :JSCS",
								parameters);
				if (ZY_ZYJS.get("ZFHJ") != null) {
					BRXX.put("ZFHJ", ZY_ZYJS.get("ZFHJ"));
				}
				Map<String, Object> YL_ZY_FYMX = dao
						.doLoad("SELECT sum(ZFJE) as ZFHJ FROM ZY_FYMX WHERE ZYH  = :ZYH AND JSCS = :JSCS and YPLX = 0",
								parameters);
				if (YL_ZY_FYMX.get("ZFHJ") != null) {
					BRXX.put("YL_ZFHJ", YL_ZY_FYMX.get("ZFHJ"));
				}
				Map<String, Object> YP_ZY_FYMX = dao
						.doLoad("SELECT sum(ZFJE) as ZFHJ FROM ZY_FYMX WHERE ZYH  = :ZYH AND JSCS = :JSCS and YPLX <> 0",
								parameters);
				if (YP_ZY_FYMX.get("ZFHJ") != null) {
					BRXX.put("YP_ZFHJ", YP_ZY_FYMX.get("ZFHJ"));
				}
			}
			return BRXX;
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "获取自负金额失败");
		}
	}

	/**
	 * 
	 * @param BRXX
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static Map<String, Object> gf_jc_gxmk_getzfhj(
			Map<String, Object> BRXX, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		BRXX.put("ZFHJ", 0);
		BRXX.put("YL_ZFHJ", 0);
		BRXX.put("YP_ZFHJ", 0);
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(
					"ZYH",
					Long.parseLong(BRXX.get("ZYH") == null ? "0" : BRXX.get(
							"ZYH").toString()));
			if (Integer.parseInt(BRXX.get("JSLX") + "") == 0
					|| Integer.parseInt(BRXX.get("JSLX") + "") == 5) {
				Map<String, Object> JC_FYMX = dao
						.doLoad("SELECT nvl(sum(ZFJE),0) as ZFHJ FROM JC_FYMX WHERE ZYH=:ZYH AND JSCS=0",
								parameters);
				if (JC_FYMX.get("ZFHJ") != null) {
					BRXX.put("ZFHJ", JC_FYMX.get("ZFHJ"));
				}
				Map<String, Object> YL_JC_FYMX = dao
						.doLoad("SELECT nvl(sum(ZFJE),0) as ZFHJ FROM JC_FYMX WHERE ZYH  = :ZYH AND JSCS = 0 and YPLX = 0",
								parameters);
				if (YL_JC_FYMX.get("ZFHJ") != null) {
					BRXX.put("YL_ZFHJ", YL_JC_FYMX.get("ZFHJ"));
				}
				Map<String, Object> YP_JC_FYMX = dao
						.doLoad("SELECT nvl(sum(ZFJE),0) as ZFHJ FROM JC_FYMX WHERE ZYH  = :ZYH AND JSCS = 0 and YPLX <> 0",
								parameters);
				if (YP_JC_FYMX.get("ZFHJ") != null) {
					BRXX.put("YP_ZFHJ", YP_JC_FYMX.get("ZFHJ"));
				}
			} else if (Integer.parseInt(BRXX.get("JSLX") + "") < 0
					|| Integer.parseInt(BRXX.get("JSLX") + "") == 10) {
				parameters.put("JSCS", BRXX.get("JSCS"));
				Map<String, Object> JC_JCJS = dao
						.doLoad("SELECT sum(ZFHJ) as ZFHJ FROM JC_JCJS WHERE ZYH  = :ZYH AND JSCS = :JSCS",
								parameters);
				if (JC_JCJS.get("ZFHJ") != null) {
					BRXX.put("ZFHJ", JC_JCJS.get("ZFHJ"));
				}
				Map<String, Object> YL_JC_FYMX = dao
						.doLoad("SELECT sum(ZFJE) as ZFHJ FROM JC_FYMX WHERE ZYH  = :ZYH AND JSCS = :JSCS and YPLX = 0",
								parameters);
				if (YL_JC_FYMX.get("ZFHJ") != null) {
					BRXX.put("YL_ZFHJ", YL_JC_FYMX.get("ZFHJ"));
				}
				Map<String, Object> YP_JC_FYMX = dao
						.doLoad("SELECT sum(ZFJE) as ZFHJ FROM JC_FYMX WHERE ZYH  = :ZYH AND JSCS = :JSCS and YPLX <> 0",
								parameters);
				if (YP_JC_FYMX.get("ZFHJ") != null) {
					BRXX.put("YP_ZFHJ", YP_JC_FYMX.get("ZFHJ"));
				}
			}
			return BRXX;
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "获取自负金额失败");
		}
	}

	/**
	 * add by zhangyq 获取结算金额
	 * 
	 * @param BRXX
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static Map<String, Object> gf_zy_gxmk_getjsje(
			Map<String, Object> BRXX, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		BRXX.put("FYHJ", 0);
		BRXX.put("ZFHJ", 0);
		BRXX.put("JKHJ", 0);
		BRXX.put("JSJE", 0);
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ZYH", BRXX.get("ZYH"));
			parameters.put("JSCS", BRXX.get("JSCS"));
			Map<String, Object> ZY_ZYJS = dao
					.doLoad("SELECT sum(FYHJ) as FYHJ,sum(ZFHJ) as ZFHJ,sum(JKHJ) as JKHJ,sum(ZFHJ - JKHJ) as JSJE FROM ZY_ZYJS WHERE ZYH  = :ZYH AND JSCS = :JSCS",
							parameters);
			if (ZY_ZYJS.get("FYHJ") != null) {
				BRXX.put("FYHJ", ZY_ZYJS.get("FYHJ"));
			}
			if (ZY_ZYJS.get("ZFHJ") != null) {
				BRXX.put("ZFHJ", ZY_ZYJS.get("ZFHJ"));
			}
			if (ZY_ZYJS.get("JKHJ") != null) {
				BRXX.put("JKHJ", ZY_ZYJS.get("JKHJ"));
			}
			if (ZY_ZYJS.get("JSJE") != null) {
				BRXX.put("JSJE", ZY_ZYJS.get("JSJE"));
			}
			return BRXX;
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "获取结算金额失败");
		}
	}

	/**
	 * add by zhangyq 获取结算金额
	 * 
	 * @param BRXX
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static Map<String, Object> gf_jc_gxmk_getjsje(
			Map<String, Object> BRXX, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		BRXX.put("FYHJ", 0);
		BRXX.put("ZFHJ", 0);
		BRXX.put("JKHJ", 0);
		BRXX.put("JSJE", 0);
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ZYH", BRXX.get("ZYH"));
			parameters.put("JSCS", BRXX.get("JSCS"));
			Map<String, Object> JC_JCJS = dao
					.doLoad("SELECT sum(FYHJ) as FYHJ,sum(ZFHJ) as ZFHJ,sum(JKHJ) as JKHJ,sum(ZFHJ - JKHJ) as JSJE FROM JC_JCJS WHERE ZYH  = :ZYH AND JSCS = :JSCS",
							parameters);
			if (JC_JCJS.get("FYHJ") != null) {
				BRXX.put("FYHJ", JC_JCJS.get("FYHJ"));
			}
			if (JC_JCJS.get("ZFHJ") != null) {
				BRXX.put("ZFHJ", JC_JCJS.get("ZFHJ"));
			}
			if (JC_JCJS.get("JKHJ") != null) {
				BRXX.put("JKHJ", JC_JCJS.get("JKHJ"));
			}
			if (JC_JCJS.get("JSJE") != null) {
				BRXX.put("JSJE", JC_JCJS.get("JSJE"));
			}
			return BRXX;
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "获取结算金额失败");
		}
	}

	/**
	 * 医嘱执行——确认
	 * 
	 * @param list_FYMX
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public static boolean uf_insert_fymx(List<Map<String, Object>> list_FYMX,
			List<Map<String, Object>> listForputFYMX, BaseDAO dao, Context ctx)
			throws ModelDataOperationException, ServiceException,
			ParseException {
		// User user = (User) ctx.get("user.instance");
		// String JGID = user.get("manageUnit.id");
		UserRoleToken user = UserRoleToken.getCurrent();
		String JGID = user.getManageUnit().getId();// 用户的机构ID
		long BQ = 0;
		if (user.getProperty("wardId") != null) {
			// BQ = Long.parseLong(user.getProperty("wardId"));// 当前病区
			BQ = Long.parseLong(user.getProperty("wardId") + "");// 当前病区
		} else {
			if (list_FYMX != null && list_FYMX.size() > 0)
				BQ = Long.parseLong(String
						.valueOf(list_FYMX.get(0).get("FYBQ")));
		}
		String YGGH = user.getUserId();// 当前操作员工号
		// try {
		for (int i = 0; i < list_FYMX.size(); i++) {
			long ZXKS = 0;
			double ZFBL = 0;
			double ZFJE = 0;
			double ZLJE = 0;
			double DZBL = 0;
			double ZJJE = 0;
			if (list_FYMX.get(i).get("ZFBL") != null) {
				ZFBL = Double.parseDouble(list_FYMX.get(i).get("ZFBL") + "");
			}
			if (list_FYMX.get(i).get("ZFJE") != null) {
				ZFJE = Double.parseDouble(list_FYMX.get(i).get("ZFJE") + "");
			}
			if (list_FYMX.get(i).get("ZLJE") != null) {
				ZLJE = Double.parseDouble(list_FYMX.get(i).get("ZLJE") + "");
			}
			if (list_FYMX.get(i).get("DZBL") != null) {
				DZBL = Double.parseDouble(list_FYMX.get(i).get("DZBL") + "");
			}
			if (list_FYMX.get(i).get("ZJJE") != null) {
				ZJJE = Double.parseDouble(list_FYMX.get(i).get("ZJJE") + "");
			}
			long ZYH = Long.parseLong(list_FYMX.get(i).get("ZYH") + "");
			long FYKS = Long.parseLong(list_FYMX.get(i).get("FYKS") + "");
			int YPLX = Integer.parseInt(list_FYMX.get(i).get("YPLX") + "");
			long BRXZ = Long.parseLong(list_FYMX.get(i).get("BRXZ") + "");
			double FYDJ = Double.parseDouble(list_FYMX.get(i).get("FYDJ") + "");
			double FYSL = Double.parseDouble(list_FYMX.get(i).get("FYSL") + "");
			String YSGH = list_FYMX.get(i).get("YSGH") + "";
			long YPCD = Long.parseLong(list_FYMX.get(i).get("YPCD") + "");
			// 执行科室为空时 默认为费用科室
			if (list_FYMX.get(i).get("ZXKS") == null
					|| list_FYMX.get(i).get("ZXKS") == ""
					|| list_FYMX.get(i).get("ZXKS").equals("null")) {
				ZXKS = FYKS;
			} else {
				ZXKS = Long.parseLong(list_FYMX.get(i).get("ZXKS") + "");
			}
			// 判断主治医生是否为空
			// Map<String, Object> parameters = new HashMap<String, Object>();
			// parameters.put("ZYH", ZYH);
			// parameters.put("JGID", JGID);
			// Map<String, Object> map_ZSYS = dao
			// .doLoad("SELECT ZSYS as YSGH From ZY_BRRY where JGID = :JGID and ZYH = :ZYH",
			// parameters);
			// if (map_ZSYS.get("YSGH") != null) {
			// YSGH = map_ZSYS.get("YSGH") + "";
			// }
			// 住院费用明细表的用于插入的Map
			Map<String, Object> zyfymx_map = (Map<String, Object>) list_FYMX
					.get(i);
			// 费用性质 YPLX_c 参数药品类型。
			long YPLX_c = Long.parseLong(list_FYMX.get(i).get("YPLX") + "");
			long FYXH = Long.parseLong(list_FYMX.get(i).get("FYXH") + "");
			long FYXM = getfygb(YPLX_c, FYXH, dao, ctx);
			zyfymx_map.put("FYXM", FYXM);
			// YPLX 为0表示费用
			if (YPLX == 0) {
				if (FYSL < 0) {
					zyfymx_map.put("ZFBL", ZFBL);
					zyfymx_map.put("ZJJE", ZJJE);
					zyfymx_map.put("ZFJE", ZFJE);
					zyfymx_map.put("ZLJE", ZLJE);
				} else {
					if (ZFJE > 0) {
						zyfymx_map.put("ZFBL", ZFBL);
						zyfymx_map.put("ZJJE", ZJJE);
						zyfymx_map.put("ZFJE", ZFJE);
						zyfymx_map.put("ZLJE", ZLJE);
					} else {
						Map<String, Object> FYXX = getje(YPLX, BRXZ, FYXH,
								FYXM, FYDJ, FYSL, dao, ctx);
						zyfymx_map.put("ZFBL", FYXX.get("ZFBL"));
						zyfymx_map.put("ZJJE", FYXX.get("ZJJE"));
						zyfymx_map.put("ZFJE", FYXX.get("ZFJE"));
						zyfymx_map.put("ZLJE", FYXX.get("ZLJE"));
					}
				}
			} else {
				// 否则就是药品
				if (FYSL < 0) {
					zyfymx_map.put("ZFBL", ZFBL);
					zyfymx_map.put("ZJJE", ZJJE);
					zyfymx_map.put("ZFJE", ZFJE);
					zyfymx_map.put("ZLJE", ZLJE);
				} else {
					if (ZFJE > 0) {
						zyfymx_map.put("ZFBL", ZFBL);
						zyfymx_map.put("ZJJE", ZJJE);
						zyfymx_map.put("ZFJE", ZFJE);
						zyfymx_map.put("ZLJE", ZLJE);
						zyfymx_map.put("FYXM", FYXM);
					} else {
						Map<String, Object> FYXX = getje(YPLX, BRXZ, FYXH,
								FYXM, FYDJ, FYSL, dao, ctx);
						zyfymx_map.put("ZFBL", FYXX.get("ZFBL"));
						zyfymx_map.put("ZJJE", FYXX.get("ZJJE"));
						zyfymx_map.put("ZFJE", FYXX.get("ZFJE"));
						zyfymx_map.put("ZLJE", FYXX.get("ZLJE"));
						zyfymx_map.put("FYXM", FYXX.get("FYGB"));
					}
				}
			}
			// 判断发药日期是否为空
			if (list_FYMX.get(i).get("FYRQ") == null
					|| list_FYMX.get(i).get("FYRQ") == "") {
				zyfymx_map.put("FYRQ", new Date());
			} else {
				String FYRQ = list_FYMX.get(i).get("FYRQ") + "";
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = list_FYMX.get(i).get("FYRQ") instanceof Date ? (Date) list_FYMX
						.get(i).get("FYRQ") : sdf.parse(FYRQ);
				zyfymx_map.put("FYRQ", date);
			}
			// 判断计费日期是否为空
			if (list_FYMX.get(i).get("JFRQ") == null
					|| list_FYMX.get(i).get("JFRQ") == "") {
				zyfymx_map.put("JFRQ", new Date());
			} else {
				String JFRQ = list_FYMX.get(i).get("JFRQ") + "";
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = list_FYMX.get(i).get("JFRQ") instanceof Date ? (Date) list_FYMX
						.get(i).get("JFRQ") : sdf.parse(JFRQ);
				zyfymx_map.put("JFRQ", date);
			}
			// 判断药品类型
			if (YPLX == 0) {
				zyfymx_map.put("YPCD", 0);
			} else {
				zyfymx_map.put("YPCD", YPCD);
			}
			if (list_FYMX.get(i).get("JLXH") != null) {
				zyfymx_map.put("YZXH",
						Long.parseLong(list_FYMX.get(i).get("JLXH") + ""));// 医嘱序号
			}
			zyfymx_map.put("ZYH", ZYH);// 住院号
			zyfymx_map.put("FYXH", FYXH);// 发药序号
			zyfymx_map.put("FYMC", list_FYMX.get(i).get("FYMC"));// 费用名称
			zyfymx_map.put("FYSL", FYSL);// 发药数量
			zyfymx_map.put("FYDJ", FYDJ);// 发药单价
			zyfymx_map.put("QRGH", YGGH);// 当前操作员工号
			zyfymx_map.put("FYKS", FYKS);// 费用科室 long
			zyfymx_map.put("ZXKS", ZXKS);// 执行科室 long
			zyfymx_map.put("XMLX", 1);// 项目类型// int
			zyfymx_map.put("YPLX", YPLX);// 药品类型
			zyfymx_map.put("YSGH", YSGH);// 医生工号
			zyfymx_map.put("FYBQ", BQ);// 费用病区 long
			zyfymx_map.put("DZBL", DZBL);
			zyfymx_map.put("JSCS", 0);
			zyfymx_map.put("YEPB", 0);
			zyfymx_map.put("JGID", JGID);
			zyfymx_map.put("JFRQ", new Date());
			listForputFYMX.add(i, zyfymx_map);
			/*
			 * Map<String, Object> parameters_update = new HashMap<String,
			 * Object>(); for (int j = 0; j < inofList.size(); j++) { long JLXH
			 * = Long .parseLong(inofList.get(j).get("JLXH") + ""); long JLXH_l
			 * = Long.parseLong(list_FYMX.get(i).get("JLXH") + ""); String QRSJ
			 * = inofList.get(j).get("QRSJ") + ""; double YCSL =
			 * Double.parseDouble(inofList.get(j) .get("YCSL") + ""); int LSBZ =
			 * Integer.parseInt(inofList.get(j).get("LSBZ") + "");
			 * 
			 * parameters_update.put("JLXH", JLXH);
			 * parameters_update.put("QRSJ", BSHISUtil.toDate(QRSJ));
			 * parameters_update.put("LSBZ", LSBZ); if (JLXH == JLXH_l) {
			 * dao.doUpdate(
			 * "update ZY_BQYZ set QRSJ=:QRSJ,LSBZ=:LSBZ where JLXH =:JLXH",
			 * parameters_update); } } dao.doSave("create", "ZY_FYMX",
			 * zyfymx_map, false);
			 */
		}
		// } catch (PersistentDataOperationException e) {
		// e.printStackTrace();
		// throw new RuntimeException("确认失败！");
		// }
		return true;
	}

	/**
	 * 医嘱执行——确认
	 * 
	 * @param list_FYMX
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public static boolean uf_insert_jc_fymx(
			List<Map<String, Object>> list_FYMX,
			List<Map<String, Object>> listForputFYMX, BaseDAO dao, Context ctx)
			throws ModelDataOperationException, ServiceException,
			ParseException {
		// User user = (User) ctx.get("user.instance");
		// String JGID = user.get("manageUnit.id");
		UserRoleToken user = UserRoleToken.getCurrent();
		String JGID = user.getManageUnit().getId();// 用户的机构ID
		long BQ = 0;
		if (user.getProperty("wardId") != null) {
			// BQ = Long.parseLong(user.getProperty("wardId"));// 当前病区
			BQ = Long.parseLong(user.getProperty("wardId") + "");// 当前病区
		} else {
			if (list_FYMX != null && list_FYMX.size() > 0)
				BQ = Long.parseLong(String
						.valueOf(list_FYMX.get(0).get("FYBQ")));
		}
		String YGGH = user.getUserId();// 当前操作员工号
		try {
			for (int i = 0; i < list_FYMX.size(); i++) {
				long ZXKS = 0;
				double ZFBL = 0;
				double ZFJE = 0;
				double ZLJE = 0;
				double DZBL = 0;
				double ZJJE = 0;
				if (list_FYMX.get(i).get("ZFBL") != null) {
					ZFBL = Double
							.parseDouble(list_FYMX.get(i).get("ZFBL") + "");
				}
				if (list_FYMX.get(i).get("ZFJE") != null) {
					ZFJE = Double
							.parseDouble(list_FYMX.get(i).get("ZFJE") + "");
				}
				if (list_FYMX.get(i).get("ZLJE") != null) {
					ZLJE = Double
							.parseDouble(list_FYMX.get(i).get("ZLJE") + "");
				}
				if (list_FYMX.get(i).get("DZBL") != null) {
					DZBL = Double
							.parseDouble(list_FYMX.get(i).get("DZBL") + "");
				}
				if (list_FYMX.get(i).get("ZJJE") != null) {
					ZJJE = Double
							.parseDouble(list_FYMX.get(i).get("ZJJE") + "");
				}
				long ZYH = Long.parseLong(list_FYMX.get(i).get("ZYH") + "");
				long FYKS = Long.parseLong(list_FYMX.get(i).get("FYKS") + "");
				int YPLX = Integer.parseInt(list_FYMX.get(i).get("YPLX") + "");
				long BRXZ = Long.parseLong(list_FYMX.get(i).get("BRXZ") + "");
				double FYDJ = Double.parseDouble(list_FYMX.get(i).get("FYDJ")
						+ "");
				double FYSL = Double.parseDouble(list_FYMX.get(i).get("FYSL")
						+ "");
				String YSGH = list_FYMX.get(i).get("YSGH") + "";
				long YPCD = Long.parseLong(list_FYMX.get(i).get("YPCD") + "");
				// 执行科室为空时 默认为费用科室
				if (list_FYMX.get(i).get("ZXKS") == null
						|| list_FYMX.get(i).get("ZXKS") == ""
						|| list_FYMX.get(i).get("ZXKS").equals("null")) {
					ZXKS = FYKS;
				} else {
					ZXKS = Long.parseLong(list_FYMX.get(i).get("ZXKS") + "");
				}
				// 判断主治医生是否为空
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("ZYH", ZYH);
				parameters.put("JGID", JGID);
				Map<String, Object> map_ZSYS = dao
						.doLoad("SELECT ZRYS as YSGH From JC_BRRY where JGID = :JGID and ZYH = :ZYH",
								parameters);
				if (map_ZSYS.get("YSGH") != null) {
					YSGH = map_ZSYS.get("YSGH") + "";
				}
				// 住院费用明细表的用于插入的Map
				Map<String, Object> zyfymx_map = (Map<String, Object>) list_FYMX
						.get(i);
				// 费用性质 YPLX_c 参数药品类型。
				long YPLX_c = Long.parseLong(list_FYMX.get(i).get("YPLX") + "");
				long FYXH = Long.parseLong(list_FYMX.get(i).get("FYXH") + "");
				long FYXM = getfygb(YPLX_c, FYXH, dao, ctx);
				zyfymx_map.put("FYXM", FYXM);
				// YPLX 为0表示费用
				if (YPLX == 0) {
					if (FYSL < 0) {
						zyfymx_map.put("ZFBL", ZFBL);
						zyfymx_map.put("ZJJE", ZJJE);
						zyfymx_map.put("ZFJE", ZFJE);
						zyfymx_map.put("ZLJE", ZLJE);
					} else {
						if (ZFJE > 0) {
							zyfymx_map.put("ZFBL", ZFBL);
							zyfymx_map.put("ZJJE", ZJJE);
							zyfymx_map.put("ZFJE", ZFJE);
							zyfymx_map.put("ZLJE", ZLJE);
						} else {
							Map<String, Object> FYXX = getje(YPLX, BRXZ, FYXH,
									FYXM, FYDJ, FYSL, dao, ctx);
							zyfymx_map.put("ZFBL", FYXX.get("ZFBL"));
							zyfymx_map.put("ZJJE", FYXX.get("ZJJE"));
							zyfymx_map.put("ZFJE", FYXX.get("ZFJE"));
							zyfymx_map.put("ZLJE", FYXX.get("ZLJE"));
						}
					}
				} else {
					// 否则就是药品
					if (FYSL < 0) {
						zyfymx_map.put("ZFBL", ZFBL);
						zyfymx_map.put("ZJJE", ZJJE);
						zyfymx_map.put("ZFJE", ZFJE);
						zyfymx_map.put("ZLJE", ZLJE);
					} else {
						if (ZFJE > 0) {
							zyfymx_map.put("ZFBL", ZFBL);
							zyfymx_map.put("ZJJE", ZJJE);
							zyfymx_map.put("ZFJE", ZFJE);
							zyfymx_map.put("ZLJE", ZLJE);
							zyfymx_map.put("FYXM", FYXM);
						} else {
							Map<String, Object> FYXX = getje(YPLX, BRXZ, FYXH,
									FYXM, FYDJ, FYSL, dao, ctx);
							zyfymx_map.put("ZFBL", FYXX.get("ZFBL"));
							zyfymx_map.put("ZJJE", FYXX.get("ZJJE"));
							zyfymx_map.put("ZFJE", FYXX.get("ZFJE"));
							zyfymx_map.put("ZLJE", FYXX.get("ZLJE"));
							zyfymx_map.put("FYXM", FYXX.get("FYGB"));
						}
					}
				}
				// 判断发药日期是否为空
				if (list_FYMX.get(i).get("FYRQ") == null
						|| list_FYMX.get(i).get("FYRQ") == "") {
					zyfymx_map.put("FYRQ", new Date());
				} else {
					String FYRQ = list_FYMX.get(i).get("FYRQ") + "";
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date date = list_FYMX.get(i).get("FYRQ") instanceof Date ? (Date) list_FYMX
							.get(i).get("FYRQ") : sdf.parse(FYRQ);
					zyfymx_map.put("FYRQ", date);
				}
				// 判断计费日期是否为空
				if (list_FYMX.get(i).get("JFRQ") == null
						|| list_FYMX.get(i).get("JFRQ") == "") {
					zyfymx_map.put("JFRQ", new Date());
				} else {
					String JFRQ = list_FYMX.get(i).get("JFRQ") + "";
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date date = list_FYMX.get(i).get("JFRQ") instanceof Date ? (Date) list_FYMX
							.get(i).get("JFRQ") : sdf.parse(JFRQ);
					zyfymx_map.put("JFRQ", date);
				}
				// 判断药品类型
				if (YPLX == 0) {
					zyfymx_map.put("YPCD", 0);
				} else {
					zyfymx_map.put("YPCD", YPCD);
				}
				if (list_FYMX.get(i).get("JLXH") != null) {
					zyfymx_map.put("YZXH",
							Long.parseLong(list_FYMX.get(i).get("JLXH") + ""));// 医嘱序号
				}
				zyfymx_map.put("ZYH", ZYH);// 住院号
				zyfymx_map.put("FYXH", FYXH);// 发药序号
				zyfymx_map.put("FYMC", list_FYMX.get(i).get("FYMC"));// 费用名称
				zyfymx_map.put("FYSL", FYSL);// 发药数量
				zyfymx_map.put("FYDJ", FYDJ);// 发药单价
				zyfymx_map.put("QRGH", YGGH);// 当前操作员工号
				zyfymx_map.put("FYKS", FYKS);// 费用科室 long
				zyfymx_map.put("ZXKS", ZXKS);// 执行科室 long
				zyfymx_map.put("XMLX", 1);// 项目类型// int
				zyfymx_map.put("YPLX", YPLX);// 药品类型
				zyfymx_map.put("YSGH", YSGH);// 医生工号
				zyfymx_map.put("FYBQ", BQ);// 费用病区 long
				zyfymx_map.put("DZBL", DZBL);
				zyfymx_map.put("JSCS", 0);
				zyfymx_map.put("YEPB", 0);
				zyfymx_map.put("JGID", JGID);
				zyfymx_map.put("JFRQ", new Date());
				listForputFYMX.add(i, zyfymx_map);
				/*
				 * Map<String, Object> parameters_update = new HashMap<String,
				 * Object>(); for (int j = 0; j < inofList.size(); j++) { long
				 * JLXH = Long .parseLong(inofList.get(j).get("JLXH") + "");
				 * long JLXH_l = Long.parseLong(list_FYMX.get(i).get("JLXH") +
				 * ""); String QRSJ = inofList.get(j).get("QRSJ") + ""; double
				 * YCSL = Double.parseDouble(inofList.get(j) .get("YCSL") + "");
				 * int LSBZ = Integer.parseInt(inofList.get(j).get("LSBZ") +
				 * "");
				 * 
				 * parameters_update.put("JLXH", JLXH);
				 * parameters_update.put("QRSJ", BSHISUtil.toDate(QRSJ));
				 * parameters_update.put("LSBZ", LSBZ); if (JLXH == JLXH_l) {
				 * dao.doUpdate(
				 * "update ZY_BQYZ set QRSJ=:QRSJ,LSBZ=:LSBZ where JLXH =:JLXH",
				 * parameters_update); } } dao.doSave("create", "ZY_FYMX",
				 * zyfymx_map, false);
				 */
			}
		} catch (PersistentDataOperationException e) {
			e.printStackTrace();
			throw new RuntimeException("确认失败！");
		}
		return true;
	}

	// 新模式:
	// 年龄大于等于3*12个月的，用岁表示；
	// 小于3*12个月而又大于等于1*12个月的，用岁月表示；
	// 小于12个月而又大于等于6个月的，用月表示；
	// 小于6个月而大于等于29天的，用月天表示；
	// 大于72小时小于29天的，用天表示；
	// 小于72小时的，用小时表示。

	public static Map<String, Object> getPersonAge(Date birthday, Date nowDate) {
		Calendar now = Calendar.getInstance();
		Calendar birth = Calendar.getInstance();
		birth.setTime(birthday);
		if (nowDate != null) {
			// nowDate = new Date();
			now.setTime(nowDate);
		}
		// Calendar now = Calendar.getInstance();
		// now.setTime(nowDate);
		// Calendar birth = Calendar.getInstance();
		// birth.setTime(birthday);
		int age = BSHISUtil.calculateAge(birthday, nowDate);
		String reAge = age + "岁";
		if (age < 3 && age >= 1) {
			int month = BSHISUtil.getMonths(birthday, now.getTime());
			reAge = age + "岁";
			if ((month - 12 * age) > 0) {
				reAge = age + "岁" + (month - 12 * age) + "月";
			}
		} else if (age < 1) {
			int month = BSHISUtil.getMonths(birthday, now.getTime());
			if (month < 12 && month >= 6) {
				reAge = month + "月";
			} else {
				int day = BSHISUtil.getPeriod(birthday, null);
				if (day >= 29 && month > 0) {
					if (now.get(Calendar.DAY_OF_MONTH) >= birth
							.get(Calendar.DAY_OF_MONTH)) {
						day = now.get(Calendar.DAY_OF_MONTH)
								- birth.get(Calendar.DAY_OF_MONTH);
					} else {
						now.set(Calendar.MONTH, birth.get(Calendar.MONTH) + 1);
						day = now.get(Calendar.DAY_OF_YEAR)
								- birth.get(Calendar.DAY_OF_YEAR);
					}
					reAge = month + "月";
					if (day > 0) {
						reAge = month + "月" + day + "天";
					}
				} else {
					if (day >= 4) {
						if ((now.get(Calendar.DAY_OF_YEAR) - birth
								.get(Calendar.DAY_OF_YEAR)) > 0) {
							day = now.get(Calendar.DAY_OF_YEAR)
									- birth.get(Calendar.DAY_OF_YEAR);
						}
						reAge = day - 1 + "天";
					} else {
						int hour = now.get(Calendar.HOUR_OF_DAY)
								- birth.get(Calendar.HOUR_OF_DAY);
						reAge = hour + 24 * (day) + "小时";
					}
				}
			}
		}
		HashMap<String, Object> resBody = new HashMap<String, Object>();
		resBody.put("age", age);
		resBody.put("ages", reAge);
		return resBody;
	}

	// 医嘱提交
	/**
	 * collardrugdetailslist 里是用于提交的药品数据集合 parameters 领药日期 modify by yangl
	 * 医嘱执行时间根据医嘱序号返回到Map对象中
	 */
	public static void uf_yztj(List<Map<String, Object>> collardrugdetailslist,
			Map<Long, List<Date>> qrsjMap, Map<String, Object> parameters,
			BaseDAO dao, Context ctx) throws ModelDataOperationException {
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int ll_fycs_total = 0;
		try {
			Date adt_ylrq = sdfdate.parse(sdfdate.format(parameters
					.get("ldt_server")));
			List<Map<String, Object>> listGY_SYPC = u_his_share_yzzxsj(
					parameters, dao, ctx);
			for (int ll_row = 0; ll_row < collardrugdetailslist.size(); ll_row++) {
				List<Date> ldt_qrsj_list = new ArrayList<Date>();
				Long ll_yzxh = Long.parseLong(collardrugdetailslist.get(ll_row)
						.get("JLXH") + ""); // 医嘱序号
				String ldt_kssj = null;
				if (collardrugdetailslist.get(ll_row).get("KSSJ") != null) {
					ldt_kssj = collardrugdetailslist.get(ll_row).get("KSSJ")
							+ ""; // 开始时间
				}
				String ldt_tzsj = null;
				if (collardrugdetailslist.get(ll_row).get("TZSJ") != null) {
					ldt_tzsj = collardrugdetailslist.get(ll_row).get("TZSJ")
							+ "";// 停止时间
				}
				String ldt_qrsj = null;
				if (collardrugdetailslist.get(ll_row).get("QRSJ") != null) {
					ldt_qrsj = collardrugdetailslist.get(ll_row).get("QRSJ")
							+ "";
				}// 确认时间
				String ls_sypc = collardrugdetailslist.get(ll_row).get("SYPC")
						+ "";// 使用频次

				int ll_lsyz = Integer.parseInt(collardrugdetailslist
						.get(ll_row).get("LSYZ") + "");// 临时医嘱标志

				String ls_yzzxsj_str = collardrugdetailslist.get(ll_row).get(
						"YZZXSJ")
						+ "";// 医嘱执行时间字符串
				int ll_yfbz = Integer.parseInt(collardrugdetailslist
						.get(ll_row).get("YFBZ") + "");// 药房包装
				int ll_srcs = Integer.parseInt(collardrugdetailslist
						.get(ll_row).get("SRCS") + ""); // 首日次数
				int ll_xmlx = Integer.parseInt(collardrugdetailslist
						.get(ll_row).get("XMLX") + "");// 项目类型
				Double ycsl = Double.parseDouble(collardrugdetailslist.get(
						ll_row).get("YCSL")
						+ ""); // 首日次数
				Map<String, Object> sjparameters = new HashMap<String, Object>();
				sjparameters.put("ldt_kssj", ldt_kssj);
				sjparameters.put("ldt_qrsj", ldt_qrsj);
				sjparameters.put("ldt_tzsj", ldt_tzsj);
				sjparameters.put("ls_sypc", ls_sypc);
				sjparameters.put("ls_yzzxsj_str", ls_yzzxsj_str);
				sjparameters.put("ll_lsyz", ll_lsyz);
				sjparameters.put("al_ypbz", 1);
				if (ll_yfbz + "" == "" || ll_yfbz == 0) {
					ll_yfbz = 1;
				}
				int ll_lsbz = uf_cacl_lsbz(listGY_SYPC, sjparameters, dao, ctx);
				if (ll_lsbz == 1) {
					parameters.put("ll_yzxh", ll_yzxh);
					uf_update_lsbz(parameters, dao, ctx);
					collardrugdetailslist.get(ll_row).put("FYCS", 0);
					continue;
				}
				parameters.put("ldt_kssj", ldt_kssj);
				parameters.put("ldt_qrsj", ldt_qrsj);
				parameters.put("ldt_tzsj", ldt_tzsj);
				parameters.put("adt_ylrq", adt_ylrq);
				parameters.put("ll_srcs", ll_srcs);
				parameters.put("ls_sypc", ls_sypc);
				parameters.put("ls_yzzxsj_str", ls_yzzxsj_str);
				parameters.put("al_fybz", 1);
				// 得到发药次数
				// 长期医嘱
				if (ll_lsyz == 0) {
					ll_fycs_total = uf_cacl_fycs_cq(listGY_SYPC, parameters,
							ldt_qrsj_list, dao, ctx);
				} else {// 临时医嘱
					if (ll_xmlx != 3) {
						ll_fycs_total = uf_cacl_fycs_ls(listGY_SYPC,
								parameters, ldt_qrsj_list, dao, ctx);
					}
				}
				if (ll_xmlx == 3) {
					collardrugdetailslist.get(ll_row).put("FYCS", 1);
					collardrugdetailslist
							.get(ll_row)
							.put("JE",
									String.format(
											"%1$.2f",
											ycsl
													* 1
													* Double.parseDouble(collardrugdetailslist
															.get(ll_row).get(
																	"YPDJ")
															+ "")));
				} else {
					collardrugdetailslist.get(ll_row)
							.put("FYCS", ll_fycs_total);
					collardrugdetailslist
							.get(ll_row)
							.put("JE",
									String.format(
											"%1$.2f",
											ycsl
													* ll_fycs_total
													* Double.parseDouble(collardrugdetailslist
															.get(ll_row).get(
																	"YPDJ")
															+ "")));
				}
				qrsjMap.put(ll_yzxh, ldt_qrsj_list);
			}
		} catch (ParseException e) {
			throw new ModelDataOperationException("时间转换失败!", e);
		}
	}

	// 计算历史标志
	/**
	 * 入参： parameters里的参数 datetime adt_kssj 开嘱时间，adt_qrsj 确认时间，adt_tzsj
	 * 停嘱时间，string as_sypc 频次编码，as_yzzxsj_str执行时间字符串，long al_lsyz 临时医嘱标志 出参：int
	 * lsbz 历史标志 1:历史医嘱 0:正常医嘱
	 */
	public static int uf_cacl_lsbz(List<Map<String, Object>> listGY_SYPC,
			Map<String, Object> parameters, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		SimpleDateFormat sdfdatetime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date adt_kssj = null;
			if (parameters.get("ldt_kssj") != null) {
				adt_kssj = sdfdatetime.parse(parameters.get("ldt_kssj") + "");
			}
			Date adt_qrsj = null;
			if (parameters.get("ldt_qrsj") != null) {
				adt_qrsj = sdfdatetime.parse(parameters.get("ldt_qrsj") + "");
			}
			Date adt_tzsj = null;
			if (parameters.get("ldt_tzsj") != null) {
				adt_tzsj = sdfdatetime.parse(parameters.get("ldt_tzsj") + "");
			}
			String as_sypc = parameters.get("ls_sypc") + "";
			String as_yzzxsj_str = parameters.get("ls_yzzxsj_str") + "";
			Long al_lsyz = null;
			if (parameters.get("ll_lsyz") != null) {
				al_lsyz = Long.parseLong(parameters.get("ll_lsyz") + "");
			}
			// int al_ypbz = 0;
			// if (parameters.get("al_ypbz") != null) {
			// al_ypbz = Integer.parseInt(parameters.get("al_ypbz") + "");
			// }
			if (al_lsyz == 1) {
				if (adt_qrsj == null) {
					return 0;
				} else {
					return 1;
				}
			}
			if (adt_tzsj == null) {
				return 0;
			}
			if (adt_qrsj == null) {
				adt_qrsj = adt_kssj;
			}
			if (adt_qrsj.getTime() >= adt_tzsj.getTime()) {
				return 1;
			}
			for (int i = 0; i < listGY_SYPC.size(); i++) {
				if ((listGY_SYPC.get(i).get("PCBM") + "").equals(as_sypc)) {
					if (as_yzzxsj_str == null || as_yzzxsj_str == ""
							|| "0".equals(as_yzzxsj_str)) {
						as_yzzxsj_str = listGY_SYPC.get(i).get("ZXSJ") + "";
					}
					List<String> ls_zxsjlist = new ArrayList<String>();
					// 将执行时间字符串转换成执行时间列表
					parameters.put("as_yzzxsj_str", as_yzzxsj_str);
					uf_get_zxsj_list(parameters, ls_zxsjlist, dao, ctx);
					// 获取每日次数
					int ll_mrcs = Integer.parseInt(listGY_SYPC.get(i).get(
							"MRCS")
							+ "");
					// 计算总的需要计算的天数
					int ll_total_ts = BSHISUtil.getPeriod(adt_qrsj, adt_tzsj) + 1;
					// 计算每一天的执行次数 for表示遍历
					for (int ll_ts = 0; ll_ts < ll_total_ts; ll_ts++) {
						// 获取日期
						Date ldt_current = BSHISUtil.getDateAfter(adt_qrsj,
								ll_ts);
						parameters.put("as_sypc", as_sypc);
						parameters.put("adt_kssj", adt_kssj);
						parameters.put("ldt_current", ldt_current);
						int bol = uf_check_zx(listGY_SYPC, parameters, dao, ctx);
						if (bol <= 0) {
							continue;
						}
						for (int ll_zxcs = 0; ll_zxcs < ll_mrcs; ll_zxcs++) {
							Date ldt_zxsj = sdfdatetime.parse(sdfdate
									.format(ldt_current)
									+ " "
									+ ls_zxsjlist.get(ll_zxcs));
							if (ldt_zxsj.getTime() > adt_qrsj.getTime()
									&& ldt_zxsj.getTime() < adt_tzsj.getTime()) {
								return 0;
							}
						}
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			throw new ModelDataOperationException("计算历史标志失败!", e);
		}
		return 1;
	}

	/**
	 * 将医嘱转为历史
	 * 
	 * @param params
	 *            {ZYH}
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static int uf_lsyz(List<Map<String, Object>> listGY_SYPC,
			Map<String, Object> params, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		Long zyh = Long.parseLong(params.get("ZYH").toString());
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ZYH", zyh);
		try {
			List<Map<String, Object>> bryzs = dao
					.doQuery(
							"select JLXH as JLXH,YPLX as al_ypbz,KSSJ as ldt_kssj,QRSJ as ldt_qrsj,TZSJ as ldt_tzsj,LSYZ as ll_lsyz,SYPC as ls_sypc,YZZXSJ as ls_yzzxsj_str from "
									+ "ZY_BQYZ where ZYH=:ZYH and LSBZ<>1",// yangl历史医嘱转化时过滤YZPB<>4
							// + " where ZYH=:ZYH and LSBZ<>1 and YZPB<>4",//
							// yangl历史医嘱转化时过滤YZPB<>4
							parameters);

			for (Map<String, Object> bryz : bryzs) {
				int lsbz = uf_cacl_lsbz(listGY_SYPC, bryz, dao, ctx);
				if (lsbz == 1) {
					Map<String, Object> p = new HashMap<String, Object>();
					p.put("JLXH", bryz.get("JLXH"));
					dao.doUpdate("update ZY_BQYZ set LSBZ=1 where JLXH=:JLXH",
							p);
				}
			}

		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException("设置病人医嘱历史失败!", e);
		}
		return 1;
	}

	// 使用频次数据集
	public static List<Map<String, Object>> u_his_share_yzzxsj(
			Map<String, Object> parameters, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		Map<String, Object> ZY_YGPJparameters = new HashMap<String, Object>();
		List<Map<String, Object>> listGY_SYPC = new ArrayList<Map<String, Object>>();
		try {
			listGY_SYPC = dao
					.doQuery(
							"select PCBM as PCBM,PCMC as PCMC,MRCS as MRCS,ZXZQ as ZXZQ,RLZ as RLZ,ZXSJ as ZXSJ,RZXZQ as RZXZQ from GY_SYPC",
							ZY_YGPJparameters);
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException("获取使用频次数据集失败!", e);
		}
		return listGY_SYPC;
	}

	// 将执行时间字符串转换成执行时间列表
	/**
	 * 入参： parameters里的参数 string as_yzzxsj_str执行时间字符串，ref string
	 * ls_zxsjlist[]用于返回的执行时间列表数组（返回）
	 */
	public static void uf_get_zxsj_list(Map<String, Object> parameters,
			List<String> ls_zxsjlist, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		String as_zxsj_str = parameters.get("as_yzzxsj_str") + "";
		String ls_zxsj = as_zxsj_str.trim();
		if (ls_zxsj != "" || ls_zxsj != null) {
			String[] ll_pos = ls_zxsj.split("-");
			for (int i = 0; i < ll_pos.length; i++) {
				if (ll_pos[i].indexOf(":") < 0) {
					ls_zxsjlist.add(ll_pos[i] + ":00:00");
				} else {
					ls_zxsjlist.add(ll_pos[i] + ":00");
				}
			}
		}
	}

	// 判断当前日期是否执行
	/**
	 * 入参：parameters里的参数 string as_sypc 使用频次datetime adt_kssj 开始时间(开嘱时间)
	 * datetime adt_dqrq 当前日期 出参： int ll_zxbz 1:需执行 0:不需执行 -1:有错误发生
	 */
	public static int uf_check_zx(List<Map<String, Object>> listGY_SYPC,
			Map<String, Object> parameters, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfdatetime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		int ll_zxbz = 0;
		for (int i = 0; i < listGY_SYPC.size(); i++) {
			if (listGY_SYPC.get(i).get("PCBM").toString()
					.equals(parameters.get("as_sypc").toString())) {
				if (Integer.parseInt(listGY_SYPC.get(i).get("ZXZQ") + "") == 1) {
					ll_zxbz = 1;
					break;
				} else {
					// 取日执行周期
					int ll_zxzq = Integer.parseInt(listGY_SYPC.get(i).get(
							"ZXZQ")
							+ "");
					String ls_rzxzq = listGY_SYPC.get(i).get("RZXZQ") + "";
					if (ll_zxzq != ls_rzxzq.length()) {
						ll_zxbz = -1;
						break;
					} else {
						try {
							// 取执行标志
							if (Integer.parseInt(listGY_SYPC.get(i).get("RLZ")
									+ "") == 1) {
								int weknum = BSHISUtil
										.getWeekOfDate(sdfdatetime.parse(sdfdatetime
												.format(parameters
														.get("ldt_current"))));
								if (ls_rzxzq.length() != weknum) {
									ll_zxbz = Integer.parseInt(ls_rzxzq
											.substring(weknum - 1, weknum));
								} else {
									ll_zxbz = Integer.parseInt(ls_rzxzq
											.substring(weknum - 1));
								}
							} else {
								int ll_days = BSHISUtil.getPeriod(sdfdate
										.parse(sdfdatetime.format(parameters
												.get("adt_kssj"))), sdfdate
										.parse(sdfdatetime.format(parameters
												.get("ldt_current")))) + 1;
								ll_days = ll_days % ll_zxzq;
								if (ll_days == 0) {
									ll_days = ls_rzxzq.length() - 1;
								} else {
									ll_days = ll_days - 1;
								}
								if (ls_rzxzq.length() != ll_days) {
									ll_zxbz = Integer.parseInt(ls_rzxzq
											.substring(ll_days, ll_days + 1)); // 获取该天的执行标志
								} else {
									ll_zxbz = Integer.parseInt(ls_rzxzq
											.substring(ll_days)); // 获取该天的执行标志
								}
							}
						} catch (ParseException e) {
							throw new ModelDataOperationException(
									"当前日期是否可以执行失败!", e);
						}
						break;
					}
				}
			} else {
				ll_zxbz = -1;
			}
		}
		return ll_zxbz;
	}

	// 将指定医嘱转为历史医嘱
	/**
	 * 入参：parameters里的参数 long al_yzxh 医嘱序号
	 */
	public static void uf_update_lsbz(Map<String, Object> parameters,
			BaseDAO dao, Context ctx) throws ModelDataOperationException {
		// User user = (User) ctx.get("user.instance");
		// String manageUnit = user.get("manageUnit.id");
		UserRoleToken user = UserRoleToken.getCurrent();
		String manageUnit = user.getManageUnit().getId();// 用户的机构ID
		try {
			Map<String, Object> parametersjlxh = new HashMap<String, Object>();
			if (parameters.get("ll_yzxh") != null) {
				parametersjlxh.put("JLXH",
						Long.parseLong(parameters.get("ll_yzxh") + ""));
			} else {
				parametersjlxh.put("JLXH", 0L);
			}
			parametersjlxh.put("JGID", manageUnit);
			dao.doUpdate(
					"update ZY_BQYZ set LSBZ=1 where JLXH=:JLXH and JGID =:JGID and YZPB<>4",
					parametersjlxh);
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException("将指定医嘱转为历史医嘱失败!", e);
		}

	}

	/**
	 * 入参：parameters里的参数 datetime adt_kssj 开嘱时间，adt_qrsj 确认时间，adt_tzsj
	 * 停嘱时间，adt_ylrq 预领日期，long 首日次数，string as_sypc
	 * 频次编码，as_yzzxsj_str执行时间字符串，long 发药方式， ref datetime adt_qrsj_list[]确认时间列表,
	 * long al_fybz (1:发药 2:退药) 出参：long ll_count 总的需执行的次数
	 */
	public static int uf_cacl_fycs_cq(List<Map<String, Object>> listGY_SYPC,
			Map<String, Object> parameters, List<Date> adt_qrsj_list,
			BaseDAO dao, Context ctx) throws ModelDataOperationException {
		SimpleDateFormat sdfdatetime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
		int ll_count = 0;
		try {
			Date adt_kssj = null;
			if (parameters.get("ldt_kssj") != null) {
				adt_kssj = sdfdatetime.parse(parameters.get("ldt_kssj") + "");
			}
			Date adt_qrsj = null;
			if (parameters.get("ldt_qrsj") != null) {
				adt_qrsj = sdfdatetime.parse(parameters.get("ldt_qrsj") + "");
			}
			Date adt_tzsj = null;
			if (parameters.get("ldt_tzsj") != null) {
				adt_tzsj = sdfdatetime.parse(parameters.get("ldt_tzsj") + "");
			}
			Date adt_ylrq = null;
			if (parameters.get("adt_ylrq") != null) {
				adt_ylrq = (Date) parameters.get("adt_ylrq");// sdfdate.parse(sdfdate.format(parameters
				// .get("adt_ylrq")));
			}
			int adt_srcs = 0;
			if (parameters.get("ll_srcs") != null) {
				adt_srcs = Integer.parseInt(parameters.get("ll_srcs") + "");
			}
			String as_sypc = parameters.get("ls_sypc") + "";
			String as_yzzxsj_str = parameters.get("ls_yzzxsj_str") + "";
			int al_fybz = 0;
			if (parameters.get("al_fybz") != null) {
				al_fybz = Integer.parseInt(parameters.get("al_fybz") + "");
			}
			for (int i = 0; i < listGY_SYPC.size(); i++) {
				List<String> ls_zxsjlist = new ArrayList<String>();
				if (listGY_SYPC.get(i).get("PCBM").toString().equals(as_sypc)) {
					if (as_yzzxsj_str == null || as_yzzxsj_str == ""
							|| "0".equals(as_yzzxsj_str)) {
						as_yzzxsj_str = listGY_SYPC.get(i).get("ZXSJ") + "";
					}
					// 将执行时间字符串转换成执行时间列表
					parameters.put("as_yzzxsj_str", as_yzzxsj_str);
					uf_get_zxsj_list(parameters, ls_zxsjlist, dao, ctx);
					// 获取每日次数
					int ll_mrcs = Integer.parseInt(listGY_SYPC.get(i).get(
							"MRCS")
							+ "");
					// 如果确认时间为空，则起始时间 = 开嘱时间，否则起始时间 = 确认时间
					Date ldt_qssj = new Date();
					if (adt_qrsj != null) {
						ldt_qssj = adt_qrsj;
					} else if (adt_kssj != null) {
						ldt_qssj = adt_kssj;
					}
					// 计算预领截止时间
					Date ldt_yljzsj = adt_ylrq;//sdfdatetime
							//.parse((sdfdate.format(BSHISUtil.getDateAfter(
							//		adt_ylrq, 1)) + " 00:00:00"));
					// 取预领截止时间和停嘱时间的较小者作为本次提交的终止时间
					Date ldt_zzsj = new Date();
					if (adt_tzsj == null
							|| ldt_yljzsj.getTime() < adt_tzsj.getTime()) {
						ldt_zzsj = ldt_yljzsj;
					} else {
						ldt_zzsj = adt_tzsj;
					}
					// 计算总的需要计算的天数 daysafter获取两个日期之前的天数
					int ll_total_ts = BSHISUtil.getPeriod(
							sdfdate.parse(sdfdatetime.format(ldt_qssj)),
							sdfdate.parse(sdfdatetime.format(ldt_zzsj))) + 1;
					Date ldt_zxsj = new Date();
					for (int ll_ts = 0; ll_ts < ll_total_ts; ll_ts++) {
						// 获取计算日期
						Date ldt_current = BSHISUtil.getDateAfter(ldt_qssj,
								ll_ts);
						// 该天不需要执行,有错误发生,也认为是不能执行
						parameters.put("as_sypc", as_sypc);
						parameters.put("adt_kssj", adt_kssj);
						parameters.put("ldt_current", ldt_current);
						int bol = uf_check_zx(listGY_SYPC, parameters, dao, ctx);
						if (bol <= 0)
							continue;
						// 首日（确认时间为空情况下的第一天）
						if (ll_ts == 0 && adt_qrsj == null) {
							// for表示遍历
							for (int ll_zxcs = (ll_mrcs - adt_srcs); ll_zxcs < ll_mrcs; ll_zxcs++) {
								ldt_zxsj = sdfdatetime.parse(sdfdate
										.format(ldt_current)
										+ " " + ls_zxsjlist.get(ll_zxcs));
								adt_qrsj_list.add(ldt_zxsj);
								ll_count++;
							}
							// 预领时间 1:领药日期当天23:59:59 2:领药日期第二天08:30:00
//							if ("2".equals(ypylsj)) {
//								adt_qrsj_list.add(sdfdatetime.parse(sdfdate
//										.format(ldt_current)
//										+ " "
//										+ ls_zxsjlist.get(0)));
//							}
						} else {
							// 非首日
							// for表示遍历
							for (int ll_zxcs = 0; ll_zxcs < ll_mrcs; ll_zxcs++) {
								ldt_zxsj = sdfdatetime.parse(sdfdate
										.format(ldt_current)
										+ " "
										+ ls_zxsjlist.get(ll_zxcs));
								// 发药时是否执行的判断
								if (al_fybz == 1
										&& ldt_zxsj.getTime() > ldt_qssj
												.getTime()
										&& ldt_zxsj.getTime() < ldt_zzsj
												.getTime()) {
									adt_qrsj_list.add(ldt_zxsj);
									ll_count++;
								}

								// 退药时是否执行的判断
								if (al_fybz == 2
										&& ldt_zxsj.getTime() > ldt_qssj
												.getTime()
										&& ldt_zxsj.getTime() <= ldt_zzsj
												.getTime()) {
									adt_qrsj_list.add(ldt_zxsj);
									ll_count++;
								}
							}
						}
					}
				}
			}
		} catch (ParseException e) {
			throw new ModelDataOperationException("长期医嘱得到发药次数失败!", e);
		}
		return ll_count;
	}

	/**
	 * 入参：parameters里的参数 datetime adt_kssj 开嘱时间，string as_sypc
	 * 频次编码，as_yzzxsj_str执行时间字符串，ref datetime adt_qrsj[]确认时间列表 出参：long ll_mrcs
	 * 总的需执行的次数
	 */
	public static int uf_cacl_fycs_ls(List<Map<String, Object>> listGY_SYPC,
			Map<String, Object> parameters, List<Date> adt_qrsj_list,
			BaseDAO dao, Context ctx) throws ModelDataOperationException {
		SimpleDateFormat sdfdatetime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
		int ll_mrcs = 0;
		try {
			Date adt_kssj = null;
			if (parameters.get("ldt_kssj") != null) {
				adt_kssj = sdfdatetime.parse(parameters.get("ldt_kssj") + "");
			}
			String as_sypc = parameters.get("ls_sypc") + "";
			String as_yzzxsj_str = parameters.get("ls_yzzxsj_str") + "";
			for (int i = 0; i < listGY_SYPC.size(); i++) {
				List<String> ls_zxsjlist = new ArrayList<String>();
				if (listGY_SYPC.get(i).get("PCBM").toString().equals(as_sypc)) {
					if (as_yzzxsj_str == null || as_yzzxsj_str == ""
							|| "0".equals(as_yzzxsj_str)) {
						as_yzzxsj_str = listGY_SYPC.get(i).get("ZXSJ") + "";
					}
					// 将执行时间字符串转换成执行时间列表
					parameters.put("as_yzzxsj_str", as_yzzxsj_str);
					uf_get_zxsj_list(parameters, ls_zxsjlist, dao, ctx);
					// 获取每日次数
					ll_mrcs = Integer.parseInt(listGY_SYPC.get(i).get("MRCS")
							+ "");
					// 遍历每日次数，将开嘱时间和执行时间字符数组中的执行时间组成一个时间加入adt_qrsj确认时间中
					// for表示遍历
					Date ldt_zxsj = new Date();
					for (int ll_zxcs = 0; ll_zxcs < ll_mrcs; ll_zxcs++) {
						ldt_zxsj = sdfdatetime.parse(sdfdate.format(adt_kssj)
								+ " " + ls_zxsjlist.get(ll_zxcs));
						adt_qrsj_list.add(ldt_zxsj);
					}
				}
			}
		} catch (ParseException e) {
			throw new ModelDataOperationException("临时医嘱得到发药次数失败!", e);
		}
		return ll_mrcs;
	}

	public static Map<String, Object> getzfbl(Map<String, Object> body,
			Context ctx, BaseDAO dao) throws ModelDataOperationException {
		long al_yplx = Long.parseLong(body.get("TYPE") + "");// 药品传药品类型1,2,3,费用传0
		long al_brxz = Long.parseLong(body.get("BRXZ") + "");// 病人性质
		long al_fyxh = Long.parseLong(body.get("FYXH") + "");// 费用序号
		long al_fygb = Long.parseLong(body.get("FYGB") + "");// 费用归并
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("BRXZ", al_brxz);
		params.put("FYXH", al_fyxh);
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("FYGB", al_fygb);
		reMap.put("ZFBL", 1);
		String hql = "";
		if (al_yplx == 0) {
			// 项目
			hql = "Select ZFBL as ZFBL From GY_FYJY Where BRXZ= :BRXZ And FYXH= :FYXH";
		} else {
			hql = "Select ZFBL as ZFBL From GY_YPJY Where BRXZ= :BRXZ And YPXH= :FYXH";
		}
		Map<String, Object> map;
		try {
			map = dao.doLoad(hql, params);
			if (map == null) {
				if (al_fygb == 0) {
					al_fygb = getfygb(al_yplx, al_fyxh, dao, ctx);
					reMap.put("FYGB", al_fygb);
				}
				params.remove("FYXH");
				params.put("SFXM", al_fygb);
				Map<String, Object> map1 = dao
						.doLoad("Select ZFBL as ZFBL From GY_ZFBL Where BRXZ= :BRXZ And SFXM= :SFXM",
								params);
				if (map1 != null) {
					reMap.put("ZFBL", map1.get("ZFBL"));
				}
			} else {
				reMap.put("ZFBL", map.get("ZFBL"));
			}
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException("获取自负比例失败!", e);
		}
		return reMap;
	}

	/**
	 * 长期医嘱项目执行次数
	 * 
	 * @param parameters
	 * @param adt_qrsj_list
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */

	public static int uf_cacl_zxcs_cq(List<Map<String, Object>> listGY_SYPC,
			Map<String, Object> parameters, Map<String, Object> parameters_cq,
			BaseDAO dao, Context ctx) throws ModelDataOperationException {
		SimpleDateFormat sdfdatetime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
		int ll_count = 0;
		try {
			Date adt_kssj = null;
			if (parameters.get("ldt_kssj") != null) {
				adt_kssj = sdfdatetime.parse(parameters.get("ldt_kssj") + "");
			}
			Date adt_qrsj = null;
			if (parameters.get("ldt_qrsj") != null) {
				adt_qrsj = sdfdatetime.parse(parameters.get("ldt_qrsj") + "");
			}
			Date adt_tzsj = null;
			if (parameters.get("ldt_tzsj") != null) {
				adt_tzsj = sdfdatetime.parse(parameters.get("ldt_tzsj") + "");
			}
			int SRCS = 0; // 首日次数
			if (parameters.get("SRCS") != null) {
				SRCS = Integer.parseInt(parameters.get("SRCS") + "");
			}

			String as_sypc = parameters.get("ls_sypc") + "";
			String as_yzzxsj_str = parameters.get("ls_yzzxsj_str") + "";

			for (int i = 0; i < listGY_SYPC.size(); i++) {
				List<String> ls_zxsjlist = new ArrayList<String>();
				int mrcs = Integer
						.parseInt(listGY_SYPC.get(i).get("MRCS") + "");

				if (listGY_SYPC.get(i).get("PCBM").toString().equals(as_sypc)) {
					if (as_yzzxsj_str == null || as_yzzxsj_str == ""
							|| "0".equals(as_yzzxsj_str)) {
						as_yzzxsj_str = listGY_SYPC.get(i).get("ZXSJ") + "";
					}
					// 将执行时间字符串转换成执行时间列表
					parameters.put("as_yzzxsj_str", as_yzzxsj_str);
					uf_get_zxsj_list(parameters, ls_zxsjlist, dao, ctx);

					// 如果确认时间为空，则起始时间 = 开嘱时间，否则起始时间 = 确认时间
					Date ldt_qssj = new Date();
					if (adt_qrsj != null) {
						ldt_qssj = adt_qrsj;
					} else if (adt_kssj != null) {
						ldt_qssj = adt_kssj;
					}
					// 截止时间为当前时间
					Date ldt_yljzsj = sdfdatetime
							.parse((sdfdate.format(BSHISUtil.getDateAfter(
									new Date(), 1)) + " 00:00:00"));
					// 截止时间和停嘱时间的较小者作为本次提交的终止时间
					Date ldt_zzsj = new Date();
					if (adt_tzsj == null
							|| ldt_yljzsj.getTime() < adt_tzsj.getTime()) {
						ldt_zzsj = ldt_yljzsj;
					} else {
						ldt_zzsj = adt_tzsj;
					}
					// 计算总的需要计算的天数 daysafter获取两个日期之前的天数
					int ll_total_ts = BSHISUtil.getPeriod(
							sdfdate.parse(sdfdatetime.format(ldt_qssj)),
							sdfdate.parse(sdfdatetime.format(ldt_zzsj))) + 1;

					// 计算每一天的执行次数
					int ll_zxcs_day = 0;
					Date ldt_zxsj = new Date();
					Date zxsj = new Date();

					// 计算每一天的执行次数
					for (int ll_ts = 0; ll_ts < ll_total_ts; ll_ts++) {
						// 获取计算日期
						Date ldt_current = BSHISUtil.getDateAfter(ldt_qssj,
								ll_ts);
						// 该天不需要执行,有错误发生,也认为是不能执行
						parameters.put("as_sypc", as_sypc);
						parameters.put("adt_kssj", adt_kssj);
						parameters.put("ldt_current", ldt_current);
						int bol = uf_check_zx(listGY_SYPC, parameters, dao, ctx);
						if (bol <= 0)
							continue;
						// 计算该天的执行次数
						if (ll_ts == 0 && adt_qrsj == null && SRCS != 0) {
							for (int j = mrcs - SRCS; j < mrcs; j++) {
								ll_zxcs_day++;
								ldt_zxsj = sdfdatetime.parse(sdfdate
										.format(ldt_current)
										+ " "
										+ ls_zxsjlist.get(j) + ":00");
								Date current = BSHISUtil.getDateAfter(ldt_zxsj,
										1);
								zxsj = sdfdatetime.parse(sdfdate
										.format(current) + " " + "00:00:00");
							}
						} else {
							for (int j = 0; j < mrcs; j++) {
								ls_zxsjlist.get(j);
								ldt_zxsj = sdfdatetime.parse(sdfdate
										.format(ldt_current)
										+ " "
										+ ls_zxsjlist.get(j) + ":00");
								if (ldt_zxsj.getTime() > ldt_qssj.getTime()) {
									if (ldt_zxsj.getTime() <= ldt_zzsj
											.getTime()) {
										ll_zxcs_day++;
										// zxsj = ldt_zxsj;
										Date current = BSHISUtil.getDateAfter(
												ldt_zxsj, 1);
										zxsj = sdfdatetime.parse(sdfdate
												.format(current)
												+ " "
												+ "00:00:00");
									}
								}
							}
						}
						if (ll_zxcs_day > 0) {
							ll_count++;
							parameters_cq.put("al_zxcs", ll_zxcs_day);
							parameters_cq.put("currentTime", zxsj);// 确认时间取医嘱的最后执行时间。
						}
					}
				}
			}
		} catch (ParseException e) {
			throw new ModelDataOperationException("长期医嘱得到项目执行次数失败!", e);
		}
		return ll_count;
	}

	/**
	 * 计算医嘱执行
	 * 
	 * @param projectList
	 * @param parameters
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static boolean uf_comp_yzzx(List<Map<String, Object>> listGY_SYPC,
			List<Map<String, Object>> projectList, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		try {
			SimpleDateFormat sdfdatetime = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			long ll_yzzh_old = 0;
			int fysx = 0;
			int ll_jfbz = 0;
			Date currentDate = null;
			/**
			 * 2013-08-20 modify by gejj
			 * 在病区项目执行中的附加计价显示时，计费标识为9时未进行过滤，通过张伟确认现修改如下代码 1、增加ll_yjxh变量
			 * 2、在sqlString中增加, min(YJXH) as LL_YJXH 3、增加ll_yjxh =
			 * Long.parseLong(list_fysx.get(0).get("LL_YJXH") + "");代码 4、将原有的if
			 * (ll_jfbz == 1) { 修改为if (ll_jfbz == 1 || (ll_yjxh > 0 && ll_jfbz
			 * == 9)) { 5、将原有的LL_JFBZ小写改成大小(共三处)
			 * **/
			long ll_yjxh = 0;
			for (int i = 0; i < projectList.size(); i++) {
				Map<String, Object> parameters_lsbz = new HashMap<String, Object>();
				Map<String, Object> parameters_up_lsbz = new HashMap<String, Object>();
				Map<String, Object> parameters_cq = new HashMap<String, Object>(); // 作为uf_cacl_zxcs_cq
																					// 的返回值

				String QRSJ = null;
				if (projectList.get(i).get("QRSJ") != null
						&& projectList.get(i).get("QRSJ") != "") {
					QRSJ = projectList.get(i).get("QRSJ") + "";// 确认时间
				}
				String TZSJ = null;
				if (projectList.get(i).get("TZSJ") != null
						&& projectList.get(i).get("TZSJ") != "") {
					TZSJ = projectList.get(i).get("TZSJ") + "";// 停医嘱时间
				}
				String KSSJ = null;
				if (projectList.get(i).get("KSSJ") != null
						&& projectList.get(i).get("KSSJ") != "") {
					KSSJ = projectList.get(i).get("KSSJ") + "";// 开始时间
				}
				String YZZXSJ = projectList.get(i).get("YZZXSJ") + "";// 医嘱执行时间
				String SYPC = projectList.get(i).get("SYPC") + "";// 使用频次
				long YZXH = Long.parseLong(projectList.get(i).get("JLXH") + "");// 医嘱序号
				int LSYZ = Integer
						.parseInt(projectList.get(i).get("LSYZ") + "");// 临时医嘱//
																		// 1,长期医嘱
																		// 0

				int SRCS = 0;
				if (projectList.get(i).get("SRCS") != null) {
					SRCS = Integer
							.parseInt(projectList.get(i).get("SRCS") + "");// 首日次数
				}
				int FJBZ = 0; // 附加计价标志
				if (projectList.get(0).get("FJBZ") != null) {
					FJBZ = Integer
							.parseInt(projectList.get(0).get("FJBZ") + "");// 首日次数
				}

				int ZXCS_TOTAL = 0;// 执行次数

				parameters_lsbz.put("ldt_kssj", KSSJ);
				parameters_lsbz.put("ldt_qrsj", QRSJ);
				parameters_lsbz.put("ldt_tzsj", TZSJ);
				parameters_lsbz.put("ls_sypc", SYPC);
				parameters_lsbz.put("ls_yzzxsj_str", YZZXSJ);
				parameters_lsbz.put("ll_lsyz", LSYZ);
				parameters_lsbz.put("al_ypbz", 0);
				parameters_lsbz.put("SRCS", SRCS);
				// 得到历史标志
				int ll_lsbz = uf_cacl_lsbz(listGY_SYPC, parameters_lsbz, dao,
						ctx);

				if (ll_lsbz == 1) {// 在执行前已经不再需要执行,即可置为历史医嘱
					parameters_up_lsbz.put("ll_yzxh", YZXH);
					uf_update_lsbz(parameters_up_lsbz, dao, ctx); // 更新历史标志
					projectList.get(i).put("FYCS", 0);
					continue;
				}
				// 计算附加计价执行的次数

				if (FJBZ == 1) {
					long ll_yzzh = Long.parseLong(projectList.get(i)
							.get("YZZH") + "");
					if (ll_yzzh_old != ll_yzzh) {
						ll_yjxh = 0;
						Map<String, Object> map_FY = new HashMap<String, Object>();
						map_FY.put(
								"YZZH",
								Long.parseLong(projectList.get(i).get("YZZH")
										+ ""));
						// 药品的附加项目必须在药品发药以后才可以执行
						StringBuffer sqlString = new StringBuffer(
								"SELECT to_char(MAX(QRSJ),'YYYY-MM-DD HH24:MI:SS') as LDT_QRSJ,MIN(FYSX) as LL_FYSX ,min(JFBZ) as LL_JFBZ, min(YJXH) as LL_YJXH FROM ZY_BQYZ WHERE YZZH=:YZZH and YZPB=0");
						List<Map<String, Object>> list_fysx = dao.doSqlQuery(
								sqlString.toString(), map_FY);
						fysx = Integer.parseInt(list_fysx.get(0).get("LL_FYSX")
								+ "");
						ll_yjxh = Long.parseLong(list_fysx.get(0)
								.get("LL_YJXH") + "");
						if (list_fysx.get(0).get("LL_JFBZ") != null) {
							ll_jfbz = Integer.parseInt(list_fysx.get(0).get(
									"LL_JFBZ")
									+ "");
						}
						if (list_fysx.get(0).get("LDT_QRSJ") != null) {
							currentDate = sdfdatetime.parse(list_fysx.get(0)
									.get("LDT_QRSJ") + "");
						} else {
							currentDate = null;
						}
					}
					ll_yzzh_old = Long.parseLong(projectList.get(i).get("YZZH")
							+ "");

					if (fysx == 2) {
						projectList.get(i).put("FYCS", 0);
						continue;
					} else {
						if (ll_jfbz == 1 || (ll_yjxh > 0 && ll_jfbz == 9)) {
							if (currentDate != null) {
								if (TZSJ != null
										&& BSHISUtil.toDate(TZSJ).getTime() > currentDate
												.getTime()) {
									TZSJ = sdfdatetime.format(currentDate);
								}
								parameters_lsbz.put("ldt_tzsj", TZSJ);
							} else {
								projectList.get(i).put("FYCS", 0);
								continue;
							}
						}
					}
				}
				if (LSYZ == 0) {// 长期医嘱
					ZXCS_TOTAL = uf_cacl_zxcs_cq(listGY_SYPC, parameters_lsbz,
							parameters_cq, dao, ctx);

					double al_zxcs = 0;
					if (parameters_cq.get("al_zxcs") != null) {
						al_zxcs = Double.parseDouble(parameters_cq
								.get("al_zxcs") + "");
					}
					projectList.get(i).put("FYCS", al_zxcs);
					if (ZXCS_TOTAL > 0) {
						String currentTime = null;
						if (parameters_cq.get("currentTime") != null) {
							currentTime = sdfdatetime.format(parameters_cq
									.get("currentTime"));
						}
						// 当前最大时间放入表中
						projectList.get(i).put("QRSJ", currentTime);
						// 把最大时间当做QRSJ传入 重新获取 ll_lsbz(历史标志)
						parameters_lsbz.put("ldt_qrsj", currentTime);
						ll_lsbz = uf_cacl_lsbz(listGY_SYPC, parameters_lsbz,
								dao, ctx);
						if (ll_lsbz == 1) {
							projectList.get(i).put("LSBZ", 1);
						}
					}
				} else { // 临时医嘱
					// 得到频次的每日次数
					int count_MRCS = 0;
					for (int j = 0; j < listGY_SYPC.size(); j++) {
						if (SYPC.equals(listGY_SYPC.get(j).get("PCBM"))) {
							count_MRCS = Integer.parseInt(listGY_SYPC.get(j)
									.get("MRCS") + "");
						}
					}
					if (count_MRCS > 0) {
						projectList.get(i).put("QRSJ", KSSJ);
						projectList.get(i).put("LSBZ", 1);
					}
					projectList.get(i).put("FYCS", count_MRCS);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ModelDataOperationException("时间转换失败!", e);
		}

		return true;
	}

	public static String get_public_fillleft(String format, String leftStr,
			int length) throws ModelDataOperationException {
		try {
			if (format.length() > Integer.parseInt(leftStr + length)) {
				return format;
			}
			long inNum = Long.parseLong(format);
			return String.format("%" + leftStr + length + "d", inNum);
		} catch (NumberFormatException e) {
			return format;
		}

	}


	/**
	 * 
	 * @param adt_hzrq
	 *            汇总日期
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static void wf_Create_jzhz(Date adt_hzrq, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		SimpleDateFormat sdfdatetime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
		// User user = (User) ctx.get("user.instance");
		// String gl_jgid = user.get("manageUnit.id");
		UserRoleToken user = UserRoleToken.getCurrent();
		String gl_jgid = user.getManageUnit().getId();// 用户的机构ID
		Map<String, Object> parametershzrq = new HashMap<String, Object>();
		Date ldt_sqhzrq = null; // 上期汇总日期
		parametershzrq.put("gl_jgid", gl_jgid);
		try {
			adt_hzrq = sdfdatetime
					.parse(sdfdate.format(adt_hzrq) + " 00:00:00");
			Map<String, Object> hzrqmap = dao
					.doLoad("SELECT str(MAX(HZRQ),'YYYY-MM-DD HH24:MI:SS') as HZRQ FROM ZY_JZHZ where JGID=:gl_jgid",
							parametershzrq);
			if (hzrqmap.get("HZRQ") != null) {
				ldt_sqhzrq = sdfdatetime.parse(hzrqmap.get("HZRQ") + "");
			}
			// １、取上期结存(sqjc)
			List<Map<String, Object>> lws_hzxxsqjc = new ArrayList<Map<String, Object>>();
			if (ldt_sqhzrq != null) {
				Map<String, Object> parametersbqye = new HashMap<String, Object>();
				parametersbqye.put("ldt_sqhzrq", ldt_sqhzrq);
				parametersbqye.put("gl_jgid", gl_jgid);
				Map<String, Object> mapbqye1 = dao
						.doLoad("SELECT BQYE as SQJC FROM ZY_JZHZ WHERE HZRQ=:ldt_sqhzrq AND XMBH=1 and JGID=:gl_jgid",
								parametersbqye);
				if (mapbqye1.get("SQJC") != null) {
					lws_hzxxsqjc.add(0, mapbqye1);
				} else {
					mapbqye1.put("SQJC", 0);
					lws_hzxxsqjc.add(0, mapbqye1);
				}
				Map<String, Object> mapbqye2 = dao
						.doLoad("SELECT BQYE as SQJC FROM ZY_JZHZ WHERE HZRQ=:ldt_sqhzrq AND XMBH=2 and JGID=:gl_jgid",
								parametersbqye);
				if (mapbqye2.get("SQJC") != null) {
					lws_hzxxsqjc.add(1, mapbqye2);
				} else {
					mapbqye2.put("SQJC", 0);
					lws_hzxxsqjc.add(1, mapbqye2);
				}
				Map<String, Object> mapbqye3 = dao
						.doLoad("SELECT BQYE as SQJC FROM ZY_JZHZ WHERE HZRQ=:ldt_sqhzrq AND XMBH=3 and JGID=:gl_jgid",
								parametersbqye);
				if (mapbqye3 != null && mapbqye3.get("SQJC") != null) {
					lws_hzxxsqjc.add(2, mapbqye3);
				} else {
					mapbqye3 = new HashMap<String, Object>();
					mapbqye3.put("SQJC", 0);
					lws_hzxxsqjc.add(2, mapbqye3);
				}
				// Map<String, Object> mapbqye3 = dao
				// .doLoad("SELECT BQYE as SQJC FROM ZY_JZHZ WHERE HZRQ=:ldt_sqhzrq AND XMBH=3 and JGID=:gl_jgid",
				// parametersbqye);
				// if (mapbqye3.get("SQJC") != null) {
				// lws_hzxxsqjc.add(2, mapbqye3);
				// } else {
				// mapbqye3.put("SQJC", 0);
				// lws_hzxxsqjc.add(2, mapbqye3);
				// }
			} else {
				Map<String, Object> mapbqye4 = new HashMap<String, Object>();
				mapbqye4.put("SQJC", 0);
				lws_hzxxsqjc.add(0, mapbqye4);
				lws_hzxxsqjc.add(1, mapbqye4);
				lws_hzxxsqjc.add(2, mapbqye4);
				// lws_hzxxsqjc.add(2, mapbqye4);
			}

			// ２、取本期发生(bqfs)
			List<Map<String, Object>> lws_hzxxbqfs = new ArrayList<Map<String, Object>>();
			Map<String, Object> parametersje = new HashMap<String, Object>();
			parametersje.put("adt_hzrq", adt_hzrq);
			parametersje.put("gl_jgid", gl_jgid);
			Map<String, Object> mapzjje1 = dao
					.doLoad("SELECT sum(ZJJE) as BQFS FROM ZY_FYMX WHERE HZRQ=:adt_hzrq and JGID=:gl_jgid",
							parametersje);
			if (mapzjje1.get("BQFS") != null) {
				lws_hzxxbqfs.add(0, mapzjje1);
			} else {
				mapzjje1.put("BQFS", 0);
				lws_hzxxbqfs.add(0, mapzjje1);
			}
			Map<String, Object> mapjkje2 = dao
					.doLoad("SELECT sum(JKJE) as BQFS FROM ZY_TBKK WHERE HZRQ=:adt_hzrq and JGID=:gl_jgid",
							parametersje);
			if (mapjkje2.get("BQFS") != null) {
				lws_hzxxbqfs.add(1, mapjkje2);
			} else {
				mapjkje2.put("BQFS", 0);
				lws_hzxxbqfs.add(1, mapjkje2);
			}
			Map<String, Object> mapjkje3 = new HashMap<String, Object>();
			mapjkje3.put("BQFS", 0);
			lws_hzxxbqfs.add(2, mapjkje3);
			// Map<String, Object> defaultmap = new HashMap<String, Object>();
			// defaultmap.put("BQFS", 0);
			// lws_hzxxbqfs.add(2, defaultmap);

			double lws_Temp1 = 0.00;
			Map<String, Object> calculatemap = dao
					.doLoad("SELECT sum(ZY_TBKK.JKJE) as BQFS FROM ZY_TBKK ZY_TBKK,ZY_JKZF ZY_JKZF WHERE ZY_TBKK.JKXH = ZY_JKZF.JKXH AND ZY_TBKK.JGID = ZY_JKZF.JGID AND ZY_JKZF.HZRQ=:adt_hzrq and ZY_TBKK.JGID=:gl_jgid",
							parametersje);
			if (calculatemap.get("BQFS") != null) {
				lws_Temp1 = Double.parseDouble(calculatemap.get("BQFS") + "");
			}
			lws_hzxxbqfs.get(1).put(
					"BQFS",
					Double.parseDouble(lws_hzxxbqfs.get(1).get("BQFS") + "")
							- lws_Temp1);

			// ３、取本期结算(bqjs)
			List<Map<String, Object>> lws_hzxxbqjs = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapfyhj = dao
					.doLoad("SELECT sum(FYHJ) as BQJS FROM ZY_ZYJS WHERE HZRQ=:adt_hzrq AND JSLX in(5,1) and JGID=:gl_jgid",
							parametersje);
			if (mapfyhj.get("BQJS") != null) {
				lws_hzxxbqjs.add(0, mapfyhj);
			} else {
				mapfyhj.put("BQJS", 0);
				lws_hzxxbqjs.add(0, mapfyhj);
			}
			Map<String, Object> mapjkhj = dao
					.doLoad("SELECT sum(JKHJ) as BQJS FROM ZY_ZYJS WHERE HZRQ=:adt_hzrq AND JSLX in(5,1) and JGID=:gl_jgid",
							parametersje);
			if (mapjkhj.get("BQJS") != null) {
				lws_hzxxbqjs.add(1, mapjkhj);
			} else {
				mapjkhj.put("BQJS", 0);
				lws_hzxxbqjs.add(1, mapjkhj);
			}
			Map<String, Object> mapbqjs3 = new HashMap<String, Object>();
			mapbqjs3.put("BQJS", 0);
			lws_hzxxbqjs.add(2, mapbqjs3);
			// Map<String, Object> defaultmap1 = new HashMap<String, Object>();
			// defaultmap1.put("BQJS", 0);
			// lws_hzxxbqjs.add(2, defaultmap1);

			double lws_Temp2 = 0.00;
			Map<String, Object> calculatemap1 = dao
					.doLoad("SELECT sum(ZY_ZYJS.FYHJ) as BQJS FROM ZY_ZYJS ZY_ZYJS,ZY_JSZF ZY_JSZF WHERE ZY_ZYJS.ZYH = ZY_JSZF.ZYH AND ZY_ZYJS.JSCS = ZY_JSZF.JSCS AND ZY_ZYJS.JGID = ZY_JSZF.JGID AND ZY_JSZF.HZRQ=:adt_hzrq AND ZY_ZYJS.JSLX in(5,1) and ZY_JSZF.JGID=:gl_jgid",
							parametersje);
			if (calculatemap1.get("BQJS") != null) {
				lws_Temp2 = Double.parseDouble(calculatemap1.get("BQJS") + "");
			}
			lws_hzxxbqjs.get(0).put(
					"BQJS",
					Double.parseDouble(lws_hzxxbqjs.get(0).get("BQJS") + "")
							- lws_Temp2);
			double lws_Temp6 = 0.00;
			Map<String, Object> calculatemap6 = dao
					.doLoad("SELECT sum(ZY_ZYJS.JKHJ) as BQJS FROM ZY_ZYJS ZY_ZYJS,ZY_JSZF ZY_JSZF WHERE ZY_ZYJS.ZYH = ZY_JSZF.ZYH AND ZY_ZYJS.JSCS = ZY_JSZF.JSCS AND ZY_ZYJS.JGID = ZY_JSZF.JGID AND ZY_JSZF.HZRQ=:adt_hzrq AND ZY_ZYJS.JSLX in(5,1) and ZY_JSZF.JGID=:gl_jgid",
							parametersje);
			if (calculatemap6.get("BQJS") != null) {
				lws_Temp6 = Double.parseDouble(calculatemap6.get("BQJS") + "");
			}
			lws_hzxxbqjs.get(1).put(
					"BQJS",
					Double.parseDouble(lws_hzxxbqjs.get(1).get("BQJS") + "")
							- lws_Temp6);

			// ４、取现金支票(xjzp)
			List<Map<String, Object>> lws_hzxxxjzp = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapzfhj = dao
					.doLoad("SELECT sum(ZFHJ) as XJZP FROM ZY_ZYJS WHERE HZRQ=:adt_hzrq AND JSLX in(5,1) and JGID=:gl_jgid",
							parametersje);
			if (mapzfhj.get("XJZP") != null) {
				lws_hzxxxjzp.add(0, mapzfhj);
			} else {
				mapzfhj.put("XJZP", 0);
				lws_hzxxxjzp.add(0, mapzfhj);
			}
			Map<String, Object> mapjkhj1 = dao
					.doLoad("SELECT sum(JKHJ) as XJZP FROM ZY_ZYJS WHERE HZRQ=:adt_hzrq AND JSLX in(5,1) and JGID=:gl_jgid",
							parametersje);
			if (mapjkhj1.get("XJZP") != null) {
				lws_hzxxxjzp.add(1, mapjkhj1);
			} else {
				mapjkhj1.put("XJZP", 0);
				lws_hzxxxjzp.add(1, mapjkhj1);
			}
			Map<String, Object> mapxjzp3 = new HashMap<String, Object>();
			mapxjzp3.put("XJZP", 0);
			lws_hzxxxjzp.add(2, mapxjzp3);
			// Map<String, Object> defaultmap2 = new HashMap<String, Object>();
			// defaultmap2.put("XJZP", 0);
			// lws_hzxxxjzp.add(2, defaultmap2);

			double lws_Temp3 = 0.00;
			Map<String, Object> calculatemap2 = dao
					.doLoad("SELECT sum(ZY_ZYJS.ZFHJ) as XJZP FROM ZY_ZYJS ZY_ZYJS,ZY_JSZF ZY_JSZF WHERE ZY_ZYJS.ZYH = ZY_JSZF.ZYH AND ZY_ZYJS.JSCS = ZY_JSZF.JSCS AND ZY_ZYJS.JGID = ZY_JSZF.JGID AND ZY_JSZF.HZRQ=:adt_hzrq  AND ZY_ZYJS.JSLX in(5,1) and ZY_ZYJS.JGID=:gl_jgid",
							parametersje);
			if (calculatemap2.get("XJZP") != null) {
				lws_Temp3 = Double.parseDouble(calculatemap2.get("XJZP") + "");
			}
			double lws_Temp4 = 0.00;
			Map<String, Object> calculatemap3 = dao
					.doLoad("SELECT sum(ZY_ZYJS.JKHJ) as XJZP FROM ZY_ZYJS ZY_ZYJS,ZY_JSZF ZY_JSZF WHERE ZY_ZYJS.ZYH = ZY_JSZF.ZYH AND ZY_ZYJS.JSCS = ZY_JSZF.JSCS AND ZY_ZYJS.JGID = ZY_JSZF.JGID AND ZY_JSZF.HZRQ=:adt_hzrq AND ZY_ZYJS.JSLX in(5,1) and ZY_ZYJS.JGID=:gl_jgid",
							parametersje);
			if (calculatemap3.get("XJZP") != null) {
				lws_Temp4 = Double.parseDouble(calculatemap3.get("XJZP") + "");
			}
			double yqfje = 0.00;
			double dqfje = 0.00;
			Map<String, Object> qfjemap3 = dao
					.doLoad("select sum(a.ZFHJ) as ZFHJ,sum(a.JKHJ) as JKHJ from ZY_ZYJS a WHERE a.HZRQ=:adt_hzrq and a.JSLX = 4 and a.JGID=:gl_jgid",
							parametersje);
			if (qfjemap3.get("ZFHJ") != null) {
				yqfje = Double.parseDouble(qfjemap3.get("JKHJ") + "");
				dqfje = Double.parseDouble(qfjemap3.get("ZFHJ") + "");
				lws_hzxxxjzp.get(0).put("QFJE", 0);
				lws_hzxxxjzp.get(1).put("QFJE", yqfje);
				lws_hzxxxjzp.get(2).put("QFJE", dqfje);
			} else {
				lws_hzxxxjzp.get(0).put("QFJE", 0);
				lws_hzxxxjzp.get(1).put("QFJE", 0);
				lws_hzxxxjzp.get(2).put("QFJE", 0);
			}
			lws_hzxxxjzp.get(0).put(
					"XJZP",
					Double.parseDouble(lws_hzxxxjzp.get(0).get("XJZP") + "")
							- lws_Temp3);
			lws_hzxxxjzp.get(1).put(
					"XJZP",
					Double.parseDouble(lws_hzxxxjzp.get(1).get("XJZP") + "")
							- lws_Temp4);
			// 5、取其它应收(qtys)
			List<Map<String, Object>> lws_hzxxqtys = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapzfhjzfhj = dao
					.doLoad("SELECT sum(FYHJ - ZFHJ) as QTYS FROM ZY_ZYJS ZY_ZYJS WHERE ZY_ZYJS.HZRQ=:adt_hzrq AND ZY_ZYJS.JSLX in(5,1) AND ZY_ZYJS.BRXZ IN (SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB=0) and JGID=:gl_jgid",
							parametersje);
			if (mapzfhjzfhj.get("QTYS") != null) {
				lws_hzxxqtys.add(0, mapzfhjzfhj);
			} else {
				mapzfhjzfhj.put("QTYS", 0);
				lws_hzxxqtys.add(0, mapzfhjzfhj);
			}
			Map<String, Object> defaultmap3 = new HashMap<String, Object>();
			defaultmap3.put("QTYS", 0);
			lws_hzxxqtys.add(1, defaultmap3);
			lws_hzxxqtys.add(2, defaultmap3);
			// Map<String, Object> mapzfhjzfhj1 = dao
			// .doLoad("SELECT sum(FYHJ - ZFHJ) as QTYS FROM ZY_ZYJS ZY_ZYJS WHERE ZY_ZYJS.HZRQ=:adt_hzrq AND (ZY_ZYJS.JSLX=3) AND ZY_ZYJS.BRXZ IN (SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB=0) and JGID=:gl_jgid",
			// parametersje);
			// if (mapzfhjzfhj1.get("QTYS") != null) {
			// lws_hzxxqtys.add(2, mapzfhjzfhj1);
			// } else {
			// mapzfhjzfhj1.put("QTYS", 0);
			// lws_hzxxqtys.add(2, mapzfhjzfhj1);
			// }
			double lws_Temp5 = 0.00;
			Map<String, Object> calculatemap4 = dao
					.doLoad("SELECT sum(ZY_ZYJS.FYHJ - ZY_ZYJS.ZFHJ) as QTYS FROM ZY_ZYJS ZY_ZYJS,ZY_JSZF ZY_JSZF WHERE ZY_ZYJS.ZYH=ZY_JSZF.ZYH AND ZY_ZYJS.JSCS=ZY_JSZF.JSCS AND ZY_ZYJS.JGID=ZY_JSZF.JGID AND ZY_JSZF.HZRQ=:adt_hzrq AND ZY_ZYJS.JSLX in(5,1) AND ZY_ZYJS.BRXZ IN (SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB=0) and ZY_ZYJS.JGID=:gl_jgid",
							parametersje);
			if (calculatemap4.get("QTYS") != null) {
				lws_Temp5 = Double.parseDouble(calculatemap4.get("QTYS") + "");
			}
			lws_hzxxqtys.get(0).put(
					"QTYS",
					Double.parseDouble(lws_hzxxqtys.get(0).get("QTYS") + "")
							- lws_Temp5);
			// 6、取参保应收(cbys)
			List<Map<String, Object>> lws_hzxxcbys = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapfyhjfyhj = dao
					.doLoad("SELECT sum(FYHJ - ZFHJ) as CBYS FROM ZY_ZYJS ZY_ZYJS WHERE ZY_ZYJS.HZRQ=:adt_hzrq AND ZY_ZYJS.JSLX in(5,1) AND ZY_ZYJS.BRXZ IN ( SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB>0) and JGID=:gl_jgid",
							parametersje);
			if (mapfyhjfyhj.get("CBYS") != null) {
				lws_hzxxcbys.add(0, mapfyhjfyhj);
			} else {
				mapfyhjfyhj.put("CBYS", 0);
				lws_hzxxcbys.add(0, mapfyhjfyhj);
			}
			Map<String, Object> defaultmap4 = new HashMap<String, Object>();
			defaultmap4.put("CBYS", 0);
			lws_hzxxcbys.add(1, defaultmap4);
			lws_hzxxcbys.add(2, defaultmap4);
			// Map<String, Object> mapfyhjfyhj1 = dao
			// .doLoad("SELECT sum(FYHJ - ZFHJ) as CBYS FROM ZY_ZYJS ZY_ZYJS WHERE ZY_ZYJS.HZRQ=:adt_hzrq AND (ZY_ZYJS.JSLX=3) AND ZY_ZYJS.BRXZ IN ( SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB>0) and JGID=:gl_jgid",
			// parametersje);
			// if (mapfyhjfyhj1.get("CBYS") != null) {
			// lws_hzxxcbys.add(2, mapfyhjfyhj1);
			// } else {
			// mapfyhjfyhj1.put("CBYS", 0);
			// lws_hzxxcbys.add(2, mapfyhjfyhj1);
			// }
			double lws_Temp7 = 0.00;
			Map<String, Object> calculatemap5 = dao
					.doLoad("SELECT sum(ZY_ZYJS.FYHJ - ZY_ZYJS.ZFHJ) as CBYS FROM ZY_ZYJS ZY_ZYJS,ZY_JSZF ZY_JSZF WHERE ZY_ZYJS.ZYH  = ZY_JSZF.ZYH AND ZY_ZYJS.JSCS = ZY_JSZF.JSCS AND ZY_ZYJS.JGID = ZY_JSZF.JGID AND ZY_JSZF.HZRQ=:adt_hzrq AND ZY_ZYJS.JSLX in(5,1) AND ZY_ZYJS.BRXZ IN (SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB>0) and ZY_ZYJS.JGID=:gl_jgid",
							parametersje);
			if (calculatemap5.get("CBYS") != null) {
				lws_Temp7 = Double.parseDouble(calculatemap5.get("CBYS") + "");
			}
			lws_hzxxcbys.get(0).put(
					"CBYS",
					Double.parseDouble(lws_hzxxcbys.get(0).get("CBYS") + "")
							- lws_Temp7);

			// 7、计算本期余额(bqye) = 上期结存 + 本期发生 - 本期结算
			List<Double> lws_hzxxbqye = new ArrayList<Double>();
			lws_hzxxbqye.add(
					0,
					Double.parseDouble(lws_hzxxsqjc.get(0).get("SQJC") + "")
							+ Double.parseDouble(lws_hzxxbqfs.get(0)
									.get("BQFS") + "")
							- Double.parseDouble(lws_hzxxbqjs.get(0)
									.get("BQJS") + ""));
			lws_hzxxbqye.add(
					1,
					Double.parseDouble(lws_hzxxsqjc.get(1).get("SQJC") + "")
							+ Double.parseDouble(lws_hzxxbqfs.get(1)
									.get("BQFS") + "")
							- Double.parseDouble(lws_hzxxbqjs.get(1)
									.get("BQJS") + ""));
			lws_hzxxbqye.add(
					2,
					Double.parseDouble(lws_hzxxsqjc.get(2).get("SQJC") + "")
							+ Double.parseDouble(lws_hzxxbqfs.get(2)
									.get("BQFS") + "")
							- Double.parseDouble(lws_hzxxbqjs.get(2)
									.get("BQJS") + ""));
			// lws_hzxxbqye.add(
			// 2,
			// Double.parseDouble(lws_hzxxsqjc.get(2).get("SQJC") + "")
			// + Double.parseDouble(lws_hzxxbqfs.get(2)
			// .get("BQFS") + "")
			// - Double.parseDouble(lws_hzxxbqjs.get(2)
			// .get("BQJS") + ""));
			// 7. 将汇总信息写入ZY_JZHZ
			for (int ll_row = 0; ll_row < 3; ll_row++) {
				Map<String, Object> zy_jzhzmap = new HashMap<String, Object>();
				zy_jzhzmap.put("HZRQ", adt_hzrq);
				zy_jzhzmap.put("XMBH", ll_row + 1);
				zy_jzhzmap.put("SQJC", lws_hzxxsqjc.get(ll_row).get("SQJC"));
				zy_jzhzmap.put("BQFS", lws_hzxxbqfs.get(ll_row).get("BQFS"));
				zy_jzhzmap.put(
						"BQJS",
						Double.parseDouble(lws_hzxxbqjs.get(ll_row).get("BQJS")
								+ "")
								+ Double.parseDouble(lws_hzxxxjzp.get(ll_row)
										.get("QFJE") + ""));
				zy_jzhzmap.put("XJZP", lws_hzxxxjzp.get(ll_row).get("XJZP"));
				zy_jzhzmap.put("CYDJ", 0);
				zy_jzhzmap.put("QFJE", lws_hzxxxjzp.get(ll_row).get("QFJE"));
				zy_jzhzmap.put("CBJE", lws_hzxxcbys.get(ll_row).get("CBYS"));
				zy_jzhzmap.put("QTJE", lws_hzxxqtys.get(ll_row).get("QTYS"));
				zy_jzhzmap.put(
						"BQYE",
						Double.parseDouble(lws_hzxxbqye.get(ll_row) + "")
								- Double.parseDouble(lws_hzxxxjzp.get(ll_row)
										.get("QFJE") + ""));
				zy_jzhzmap.put("YHJE", 0);
				zy_jzhzmap.put("JGID", gl_jgid);
				dao.doSave("create", BSPHISEntryNames.ZY_JZHZ, zy_jzhzmap,
						false);
			}
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		} catch (ParseException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		} catch (ValidateException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		}
	}

	/**
	 * 
	 * @param adt_hzrq
	 *            汇总日期
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static void fsb_wf_Create_jzhz(Date adt_hzrq, BaseDAO dao,
			Context ctx) throws ModelDataOperationException {
		SimpleDateFormat sdfdatetime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
		// User user = (User) ctx.get("user.instance");
		// String gl_jgid = user.get("manageUnit.id");
		UserRoleToken user = UserRoleToken.getCurrent();
		String gl_jgid = user.getManageUnit().getId();// 用户的机构ID
		Map<String, Object> parametershzrq = new HashMap<String, Object>();
		Date ldt_sqhzrq = null; // 上期汇总日期
		parametershzrq.put("gl_jgid", gl_jgid);
		try {
			adt_hzrq = sdfdatetime
					.parse(sdfdate.format(adt_hzrq) + " 00:00:00");
			Map<String, Object> hzrqmap = dao
					.doLoad("SELECT str(MAX(HZRQ),'YYYY-MM-DD HH24:MI:SS') as HZRQ FROM JC_JZHZ where JGID=:gl_jgid",
							parametershzrq);
			if (hzrqmap.get("HZRQ") != null) {
				ldt_sqhzrq = sdfdatetime.parse(hzrqmap.get("HZRQ") + "");
			}
			// １、取上期结存(sqjc)
			List<Map<String, Object>> lws_hzxxsqjc = new ArrayList<Map<String, Object>>();
			if (ldt_sqhzrq != null) {
				Map<String, Object> parametersbqye = new HashMap<String, Object>();
				parametersbqye.put("ldt_sqhzrq", ldt_sqhzrq);
				parametersbqye.put("gl_jgid", gl_jgid);
				Map<String, Object> mapbqye1 = dao
						.doLoad("SELECT BQYE as SQJC FROM JC_JZHZ WHERE HZRQ=:ldt_sqhzrq AND XMBH=1 and JGID=:gl_jgid",
								parametersbqye);
				if (mapbqye1.get("SQJC") != null) {
					lws_hzxxsqjc.add(0, mapbqye1);
				} else {
					mapbqye1.put("SQJC", 0);
					lws_hzxxsqjc.add(0, mapbqye1);
				}
				Map<String, Object> mapbqye2 = dao
						.doLoad("SELECT BQYE as SQJC FROM JC_JZHZ WHERE HZRQ=:ldt_sqhzrq AND XMBH=2 and JGID=:gl_jgid",
								parametersbqye);
				if (mapbqye2.get("SQJC") != null) {
					lws_hzxxsqjc.add(1, mapbqye2);
				} else {
					mapbqye2.put("SQJC", 0);
					lws_hzxxsqjc.add(1, mapbqye2);
				}
				Map<String, Object> mapbqye3 = dao
						.doLoad("SELECT BQYE as SQJC FROM JC_JZHZ WHERE HZRQ=:ldt_sqhzrq AND XMBH=3 and JGID=:gl_jgid",
								parametersbqye);
				if (mapbqye3 != null && mapbqye3.get("SQJC") != null) {
					lws_hzxxsqjc.add(2, mapbqye3);
				} else {
					mapbqye3 = new HashMap<String, Object>();
					mapbqye3.put("SQJC", 0);
					lws_hzxxsqjc.add(2, mapbqye3);
				}
				// Map<String, Object> mapbqye3 = dao
				// .doLoad("SELECT BQYE as SQJC FROM JC_JZHZ WHERE HZRQ=:ldt_sqhzrq AND XMBH=3 and JGID=:gl_jgid",
				// parametersbqye);
				// if (mapbqye3.get("SQJC") != null) {
				// lws_hzxxsqjc.add(2, mapbqye3);
				// } else {
				// mapbqye3.put("SQJC", 0);
				// lws_hzxxsqjc.add(2, mapbqye3);
				// }
			} else {
				Map<String, Object> mapbqye4 = new HashMap<String, Object>();
				mapbqye4.put("SQJC", 0);
				lws_hzxxsqjc.add(0, mapbqye4);
				lws_hzxxsqjc.add(1, mapbqye4);
				lws_hzxxsqjc.add(2, mapbqye4);
				// lws_hzxxsqjc.add(2, mapbqye4);
			}

			// ２、取本期发生(bqfs)
			List<Map<String, Object>> lws_hzxxbqfs = new ArrayList<Map<String, Object>>();
			Map<String, Object> parametersje = new HashMap<String, Object>();
			parametersje.put("adt_hzrq", adt_hzrq);
			parametersje.put("gl_jgid", gl_jgid);
			Map<String, Object> mapzjje1 = dao
					.doLoad("SELECT sum(ZJJE) as BQFS FROM JC_FYMX WHERE HZRQ=:adt_hzrq and JGID=:gl_jgid",
							parametersje);
			if (mapzjje1.get("BQFS") != null) {
				lws_hzxxbqfs.add(0, mapzjje1);
			} else {
				mapzjje1.put("BQFS", 0);
				lws_hzxxbqfs.add(0, mapzjje1);
			}
			Map<String, Object> mapjkje2 = dao
					.doLoad("SELECT sum(JKJE) as BQFS FROM JC_TBKK WHERE HZRQ=:adt_hzrq and JGID=:gl_jgid",
							parametersje);
			if (mapjkje2.get("BQFS") != null) {
				lws_hzxxbqfs.add(1, mapjkje2);
			} else {
				mapjkje2.put("BQFS", 0);
				lws_hzxxbqfs.add(1, mapjkje2);
			}
			Map<String, Object> mapjkje3 = new HashMap<String, Object>();
			mapjkje3.put("BQFS", 0);
			lws_hzxxbqfs.add(2, mapjkje3);
			// Map<String, Object> defaultmap = new HashMap<String, Object>();
			// defaultmap.put("BQFS", 0);
			// lws_hzxxbqfs.add(2, defaultmap);

			double lws_Temp1 = 0.00;
			Map<String, Object> calculatemap = dao
					.doLoad("SELECT sum(JC_TBKK.JKJE) as BQFS FROM JC_TBKK JC_TBKK,JC_JKZF JC_JKZF WHERE JC_TBKK.JKXH = JC_JKZF.JKXH AND JC_TBKK.JGID = JC_JKZF.JGID AND JC_JKZF.HZRQ=:adt_hzrq and JC_TBKK.JGID=:gl_jgid",
							parametersje);
			if (calculatemap.get("BQFS") != null) {
				lws_Temp1 = Double.parseDouble(calculatemap.get("BQFS") + "");
			}
			lws_hzxxbqfs.get(1).put(
					"BQFS",
					Double.parseDouble(lws_hzxxbqfs.get(1).get("BQFS") + "")
							- lws_Temp1);

			// ３、取本期结算(bqjs)
			List<Map<String, Object>> lws_hzxxbqjs = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapfyhj = dao
					.doLoad("SELECT sum(FYHJ) as BQJS FROM JC_JCJS WHERE HZRQ=:adt_hzrq AND JSLX in(5,1) and JGID=:gl_jgid",
							parametersje);
			if (mapfyhj.get("BQJS") != null) {
				lws_hzxxbqjs.add(0, mapfyhj);
			} else {
				mapfyhj.put("BQJS", 0);
				lws_hzxxbqjs.add(0, mapfyhj);
			}
			Map<String, Object> mapjkhj = dao
					.doLoad("SELECT sum(JKHJ) as BQJS FROM JC_JCJS WHERE HZRQ=:adt_hzrq AND JSLX in(5,1) and JGID=:gl_jgid",
							parametersje);
			if (mapjkhj.get("BQJS") != null) {
				lws_hzxxbqjs.add(1, mapjkhj);
			} else {
				mapjkhj.put("BQJS", 0);
				lws_hzxxbqjs.add(1, mapjkhj);
			}
			Map<String, Object> mapcczj = dao
					.doLoad("SELECT sum(FYHJ) as BQJS FROM JC_JCJS WHERE HZRQ=:adt_hzrq AND JSLX=4 and JGID=:gl_jgid",
							parametersje);
			if (mapcczj.get("BQJS") != null) {
				lws_hzxxbqjs.add(2, mapcczj);
			} else {
				mapcczj.put("BQJS", 0);
				lws_hzxxbqjs.add(2, mapcczj);
			}
			// Map<String, Object> defaultmap1 = new HashMap<String, Object>();
			// defaultmap1.put("BQJS", 0);
			// lws_hzxxbqjs.add(2, defaultmap1);

			double lws_Temp2 = 0.00;
			Map<String, Object> calculatemap1 = dao
					.doLoad("SELECT sum(JC_JCJS.FYHJ) as BQJS FROM JC_JCJS JC_JCJS,JC_JSZF JC_JSZF WHERE JC_JCJS.ZYH = JC_JSZF.ZYH AND JC_JCJS.JSCS = JC_JSZF.JSCS AND JC_JCJS.JGID = JC_JSZF.JGID AND JC_JSZF.HZRQ=:adt_hzrq AND JC_JCJS.JSLX in(5,1) and JC_JSZF.JGID=:gl_jgid",
							parametersje);
			if (calculatemap1.get("BQJS") != null) {
				lws_Temp2 = Double.parseDouble(calculatemap1.get("BQJS") + "");
			}
			lws_hzxxbqjs.get(0).put(
					"BQJS",
					Double.parseDouble(lws_hzxxbqjs.get(0).get("BQJS") + "")
							- lws_Temp2);
			double lws_Temp6 = 0.00;
			Map<String, Object> calculatemap6 = dao
					.doLoad("SELECT sum(JC_JCJS.JKHJ) as BQJS FROM JC_JCJS JC_JCJS,JC_JSZF JC_JSZF WHERE JC_JCJS.ZYH = JC_JSZF.ZYH AND JC_JCJS.JSCS = JC_JSZF.JSCS AND JC_JCJS.JGID = JC_JSZF.JGID AND JC_JSZF.HZRQ=:adt_hzrq AND JC_JCJS.JSLX in(5,1) and JC_JSZF.JGID=:gl_jgid",
							parametersje);
			if (calculatemap6.get("BQJS") != null) {
				lws_Temp6 = Double.parseDouble(calculatemap6.get("BQJS") + "");
			}
			lws_hzxxbqjs.get(1).put(
					"BQJS",
					Double.parseDouble(lws_hzxxbqjs.get(1).get("BQJS") + "")
							- lws_Temp6);

			// ４、取现金支票(xjzp)
			List<Map<String, Object>> lws_hzxxxjzp = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapzfhj = dao
					.doLoad("SELECT sum(ZFHJ) as XJZP FROM JC_JCJS WHERE HZRQ=:adt_hzrq AND JSLX in(5,1) and JGID=:gl_jgid",
							parametersje);
			if (mapzfhj.get("XJZP") != null) {
				lws_hzxxxjzp.add(0, mapzfhj);
			} else {
				mapzfhj.put("XJZP", 0);
				lws_hzxxxjzp.add(0, mapzfhj);
			}
			Map<String, Object> mapjkhj1 = dao
					.doLoad("SELECT sum(JKHJ) as XJZP FROM JC_JCJS WHERE HZRQ=:adt_hzrq AND JSLX in(5,1) and JGID=:gl_jgid",
							parametersje);
			if (mapjkhj1.get("XJZP") != null) {
				lws_hzxxxjzp.add(1, mapjkhj1);
			} else {
				mapjkhj1.put("XJZP", 0);
				lws_hzxxxjzp.add(1, mapjkhj1);
			}
			Map<String, Object> mapxjzp3 = new HashMap<String, Object>();
			mapxjzp3.put("XJZP", 0);
			lws_hzxxxjzp.add(2, mapxjzp3);
			// Map<String, Object> defaultmap2 = new HashMap<String, Object>();
			// defaultmap2.put("XJZP", 0);
			// lws_hzxxxjzp.add(2, defaultmap2);

			double lws_Temp3 = 0.00;
			Map<String, Object> calculatemap2 = dao
					.doLoad("SELECT sum(JC_JCJS.ZFHJ) as XJZP FROM JC_JCJS JC_JCJS,JC_JSZF JC_JSZF WHERE JC_JCJS.ZYH = JC_JSZF.ZYH AND JC_JCJS.JSCS = JC_JSZF.JSCS AND JC_JCJS.JGID = JC_JSZF.JGID AND JC_JSZF.HZRQ=:adt_hzrq  AND JC_JCJS.JSLX in(5,1) and JC_JCJS.JGID=:gl_jgid",
							parametersje);
			if (calculatemap2.get("XJZP") != null) {
				lws_Temp3 = Double.parseDouble(calculatemap2.get("XJZP") + "");
			}
			double lws_Temp4 = 0.00;
			Map<String, Object> calculatemap3 = dao
					.doLoad("SELECT sum(JC_JCJS.JKHJ) as XJZP FROM JC_JCJS JC_JCJS,JC_JSZF JC_JSZF WHERE JC_JCJS.ZYH = JC_JSZF.ZYH AND JC_JCJS.JSCS = JC_JSZF.JSCS AND JC_JCJS.JGID = JC_JSZF.JGID AND JC_JSZF.HZRQ=:adt_hzrq AND JC_JCJS.JSLX in(5,1) and JC_JCJS.JGID=:gl_jgid",
							parametersje);
			if (calculatemap3.get("XJZP") != null) {
				lws_Temp4 = Double.parseDouble(calculatemap3.get("XJZP") + "");
			}
			double yqfje = 0.00;
			double dqfje = 0.00;
			Map<String, Object> qfjemap3 = dao
					.doLoad("select sum(a.ZFHJ) as ZFHJ,sum(a.JKHJ) as JKHJ from JC_JCJS a WHERE a.HZRQ=:adt_hzrq and a.JSLX = 4 and a.JGID=:gl_jgid",
							parametersje);
			if (qfjemap3.get("ZFHJ") != null) {
				yqfje = Double.parseDouble(qfjemap3.get("JKHJ") + "");
				dqfje = Double.parseDouble(qfjemap3.get("ZFHJ") + "");
				lws_hzxxxjzp.get(0).put("QFJE", 0);
				lws_hzxxxjzp.get(1).put("QFJE", yqfje);
				lws_hzxxxjzp.get(2).put("QFJE", dqfje);
			} else {
				lws_hzxxxjzp.get(0).put("QFJE", 0);
				lws_hzxxxjzp.get(1).put("QFJE", 0);
				lws_hzxxxjzp.get(2).put("QFJE", 0);
			}
			lws_hzxxxjzp.get(0).put(
					"XJZP",
					Double.parseDouble(lws_hzxxxjzp.get(0).get("XJZP") + "")
							- lws_Temp3);
			lws_hzxxxjzp.get(1).put(
					"XJZP",
					Double.parseDouble(lws_hzxxxjzp.get(1).get("XJZP") + "")
							- lws_Temp4);
			// 5、取其它应收(qtys)
			List<Map<String, Object>> lws_hzxxqtys = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapzfhjzfhj = dao
					.doLoad("SELECT sum(FYHJ - ZFHJ) as QTYS FROM JC_JCJS JC_JCJS WHERE JC_JCJS.HZRQ=:adt_hzrq AND JC_JCJS.JSLX in(5,1) AND JC_JCJS.BRXZ IN (SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB=0) and JGID=:gl_jgid",
							parametersje);
			if (mapzfhjzfhj.get("QTYS") != null) {
				lws_hzxxqtys.add(0, mapzfhjzfhj);
			} else {
				mapzfhjzfhj.put("QTYS", 0);
				lws_hzxxqtys.add(0, mapzfhjzfhj);
			}
			Map<String, Object> defaultmap3 = new HashMap<String, Object>();
			defaultmap3.put("QTYS", 0);
			lws_hzxxqtys.add(1, defaultmap3);
			lws_hzxxqtys.add(2, defaultmap3);
			// Map<String, Object> mapzfhjzfhj1 = dao
			// .doLoad("SELECT sum(FYHJ - ZFHJ) as QTYS FROM JC_JCJS JC_JCJS WHERE JC_JCJS.HZRQ=:adt_hzrq AND (JC_JCJS.JSLX=3) AND JC_JCJS.BRXZ IN (SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB=0) and JGID=:gl_jgid",
			// parametersje);
			// if (mapzfhjzfhj1.get("QTYS") != null) {
			// lws_hzxxqtys.add(2, mapzfhjzfhj1);
			// } else {
			// mapzfhjzfhj1.put("QTYS", 0);
			// lws_hzxxqtys.add(2, mapzfhjzfhj1);
			// }
			double lws_Temp5 = 0.00;
			Map<String, Object> calculatemap4 = dao
					.doLoad("SELECT sum(JC_JCJS.FYHJ - JC_JCJS.ZFHJ) as QTYS FROM JC_JCJS JC_JCJS,JC_JSZF JC_JSZF WHERE JC_JCJS.ZYH=JC_JSZF.ZYH AND JC_JCJS.JSCS=JC_JSZF.JSCS AND JC_JCJS.JGID=JC_JSZF.JGID AND JC_JSZF.HZRQ=:adt_hzrq AND JC_JCJS.JSLX in(5,1) AND JC_JCJS.BRXZ IN (SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB=0) and JC_JCJS.JGID=:gl_jgid",
							parametersje);
			if (calculatemap4.get("QTYS") != null) {
				lws_Temp5 = Double.parseDouble(calculatemap4.get("QTYS") + "");
			}
			lws_hzxxqtys.get(0).put(
					"QTYS",
					Double.parseDouble(lws_hzxxqtys.get(0).get("QTYS") + "")
							- lws_Temp5);
			// 6、取参保应收(cbys)
			List<Map<String, Object>> lws_hzxxcbys = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapfyhjfyhj = dao
					.doLoad("SELECT sum(FYHJ - ZFHJ) as CBYS FROM JC_JCJS JC_JCJS WHERE JC_JCJS.HZRQ=:adt_hzrq AND JC_JCJS.JSLX in(5,1) AND JC_JCJS.BRXZ IN ( SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB>0) and JGID=:gl_jgid",
							parametersje);
			if (mapfyhjfyhj.get("CBYS") != null) {
				lws_hzxxcbys.add(0, mapfyhjfyhj);
			} else {
				mapfyhjfyhj.put("CBYS", 0);
				lws_hzxxcbys.add(0, mapfyhjfyhj);
			}
			Map<String, Object> defaultmap4 = new HashMap<String, Object>();
			defaultmap4.put("CBYS", 0);
			lws_hzxxcbys.add(1, defaultmap4);
			lws_hzxxcbys.add(2, defaultmap4);
			// Map<String, Object> mapfyhjfyhj1 = dao
			// .doLoad("SELECT sum(FYHJ - ZFHJ) as CBYS FROM JC_JCJS JC_JCJS WHERE JC_JCJS.HZRQ=:adt_hzrq AND (JC_JCJS.JSLX=3) AND JC_JCJS.BRXZ IN ( SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB>0) and JGID=:gl_jgid",
			// parametersje);
			// if (mapfyhjfyhj1.get("CBYS") != null) {
			// lws_hzxxcbys.add(2, mapfyhjfyhj1);
			// } else {
			// mapfyhjfyhj1.put("CBYS", 0);
			// lws_hzxxcbys.add(2, mapfyhjfyhj1);
			// }
			double lws_Temp7 = 0.00;
			Map<String, Object> calculatemap5 = dao
					.doLoad("SELECT sum(JC_JCJS.FYHJ - JC_JCJS.ZFHJ) as CBYS FROM JC_JCJS JC_JCJS,JC_JSZF JC_JSZF WHERE JC_JCJS.ZYH  = JC_JSZF.ZYH AND JC_JCJS.JSCS = JC_JSZF.JSCS AND JC_JCJS.JGID = JC_JSZF.JGID AND JC_JSZF.HZRQ=:adt_hzrq AND JC_JCJS.JSLX in(5,1) AND JC_JCJS.BRXZ IN (SELECT GY_BRXZ.BRXZ FROM GY_BRXZ GY_BRXZ WHERE GY_BRXZ.DBPB>0) and JC_JCJS.JGID=:gl_jgid",
							parametersje);
			if (calculatemap5.get("CBYS") != null) {
				lws_Temp7 = Double.parseDouble(calculatemap5.get("CBYS") + "");
			}
			lws_hzxxcbys.get(0).put(
					"CBYS",
					Double.parseDouble(lws_hzxxcbys.get(0).get("CBYS") + "")
							- lws_Temp7);

			// 7、计算本期余额(bqye) = 上期结存 + 本期发生 - 本期结算
			List<Double> lws_hzxxbqye = new ArrayList<Double>();
			lws_hzxxbqye.add(
					0,
					Double.parseDouble(lws_hzxxsqjc.get(0).get("SQJC") + "")
							+ Double.parseDouble(lws_hzxxbqfs.get(0)
									.get("BQFS") + "")
							- Double.parseDouble(lws_hzxxbqjs.get(0)
									.get("BQJS") + ""));
			lws_hzxxbqye.add(
					1,
					Double.parseDouble(lws_hzxxsqjc.get(1).get("SQJC") + "")
							+ Double.parseDouble(lws_hzxxbqfs.get(1)
									.get("BQFS") + "")
							- Double.parseDouble(lws_hzxxbqjs.get(1)
									.get("BQJS") + ""));
			lws_hzxxbqye.add(
					2,
					Double.parseDouble(lws_hzxxsqjc.get(2).get("SQJC") + "")
							+ Double.parseDouble(lws_hzxxbqfs.get(2)
									.get("BQFS") + "")
							- Double.parseDouble(lws_hzxxbqjs.get(2)
									.get("BQJS") + ""));
			// lws_hzxxbqye.add(
			// 2,
			// Double.parseDouble(lws_hzxxsqjc.get(2).get("SQJC") + "")
			// + Double.parseDouble(lws_hzxxbqfs.get(2)
			// .get("BQFS") + "")
			// - Double.parseDouble(lws_hzxxbqjs.get(2)
			// .get("BQJS") + ""));
			// 7. 将汇总信息写入JC_JZHZ
			for (int ll_row = 0; ll_row < 3; ll_row++) {
				Map<String, Object> JC_jzhzmap = new HashMap<String, Object>();
				JC_jzhzmap.put("HZRQ", adt_hzrq);
				JC_jzhzmap.put("XMBH", ll_row + 1);
				JC_jzhzmap.put("SQJC", lws_hzxxsqjc.get(ll_row).get("SQJC"));
				JC_jzhzmap.put("BQFS", lws_hzxxbqfs.get(ll_row).get("BQFS"));
				JC_jzhzmap.put(
						"BQJS",
						Double.parseDouble(lws_hzxxbqjs.get(ll_row).get("BQJS")
								+ ""));
				JC_jzhzmap.put("XJZP", lws_hzxxxjzp.get(ll_row).get("XJZP"));
				JC_jzhzmap.put("CYDJ", 0);
				JC_jzhzmap.put("QFJE", lws_hzxxxjzp.get(ll_row).get("QFJE"));
				JC_jzhzmap.put("CBJE", lws_hzxxcbys.get(ll_row).get("CBYS"));
				JC_jzhzmap.put("QTJE", lws_hzxxqtys.get(ll_row).get("QTYS"));
				JC_jzhzmap.put("BQYE",
						Double.parseDouble(lws_hzxxbqye.get(ll_row) + ""));
				JC_jzhzmap.put("YHJE", 0);
				JC_jzhzmap.put("JGID", gl_jgid);
				dao.doSave("create", BSPHISEntryNames.JC_JZHZ, JC_jzhzmap,
						false);
			}
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		} catch (ParseException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		} catch (ValidateException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		}
	}

	/**
	 * 
	 * @param adt_hzrq
	 *            汇总日期
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static void wf_create_fyhz(Date adt_hzrq, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		SimpleDateFormat sdfdatetime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
		// User user = (User) ctx.get("user.instance");
		// String gl_jgid = user.get("manageUnit.id");
		UserRoleToken user = UserRoleToken.getCurrent();
		String gl_jgid = user.getManageUnit().getId();// 用户的机构ID
		List<Map<String, Object>> zy_fyhzlist = new ArrayList<Map<String, Object>>();
		try {
			adt_hzrq = sdfdatetime
					.parse(sdfdate.format(adt_hzrq) + " 00:00:00");
			Map<String, Object> parametershzrq = new HashMap<String, Object>();
			Date ldt_sqhzrq = null; // 上期汇总日期
			parametershzrq.put("gl_jgid", gl_jgid);
			Map<String, Object> hzrqmap = dao
					.doLoad("SELECT max(HZRQ) as HZRQ FROM ZY_FYHZ where JGID=:gl_jgid",
							parametershzrq);
			if (hzrqmap.get("HZRQ") != null) {
				ldt_sqhzrq = sdfdatetime.parse(hzrqmap.get("HZRQ") + "");
			}
			// １、取上期结存(sqjc)
			Map<String, Object> parameterslds_sqjc = new HashMap<String, Object>();
			parameterslds_sqjc.put("adt_hzrq", ldt_sqhzrq);
			parameterslds_sqjc.put("al_jgid", gl_jgid);
			List<Map<String, Object>> lds_sqjc = dao
					.doQuery(
							"SELECT ZY_FYHZ.FYXM as FYXM,sum(ZY_FYHZ.BQJC) AS FYJE FROM ZY_FYHZ ZY_FYHZ WHERE ZY_FYHZ.HZRQ=:adt_hzrq and ZY_FYHZ.JGID=:al_jgid GROUP BY ZY_FYHZ.FYXM",
							parameterslds_sqjc);
			for (int ll_Row = 0; ll_Row < lds_sqjc.size(); ll_Row++) {
				Map<String, Object> zy_fyhzmap = new HashMap<String, Object>();
				zy_fyhzmap.put("HZRQ", adt_hzrq);
				zy_fyhzmap.put("FYXM", lds_sqjc.get(ll_Row).get("FYXM"));
				zy_fyhzmap.put("SQJC", lds_sqjc.get(ll_Row).get("FYJE"));
				zy_fyhzmap.put("JGID", gl_jgid);
				zy_fyhzmap.put("BQFS", 0.00);
				zy_fyhzmap.put("BQJS", 0.00);
				zy_fyhzmap.put("SJJC", 0.00);
				zy_fyhzmap.put("BQJC", 0.00);
				zy_fyhzlist.add(zy_fyhzmap);
			}
			// ２、取本期发生(bqfs)
			Map<String, Object> parametersfyje = new HashMap<String, Object>();
			parametersfyje.put("adt_hzrq", adt_hzrq);
			parametersfyje.put("al_jgid", gl_jgid);
			List<Map<String, Object>> lds_bqfs = dao
					.doQuery(
							"SELECT ZY_FYMX.FYXM as FYXM,sum(ZY_FYMX.ZJJE) AS FYJE FROM ZY_FYMX ZY_FYMX	WHERE ZY_FYMX.HZRQ=:adt_hzrq and ZY_FYMX.JGID=:al_jgid GROUP BY ZY_FYMX.FYXM",
							parametersfyje);
			List<Map<String, Object>> lds_bqfslist = new ArrayList<Map<String, Object>>();
			int sign1 = 0;
			for (int ll_Row = 0; ll_Row < lds_bqfs.size(); ll_Row++) {
				if (zy_fyhzlist.size() > 0) {
					for (int j = 0; j < zy_fyhzlist.size(); j++) {
						if (lds_bqfs
								.get(ll_Row)
								.get("FYXM")
								.toString()
								.equals(zy_fyhzlist.get(j).get("FYXM")
										.toString())) {
							zy_fyhzlist.get(j).put(
									"BQFS",
									Double.parseDouble(zy_fyhzlist.get(j).get(
											"BQFS")
											+ "")
											+ Double.parseDouble(lds_bqfs.get(
													ll_Row).get("FYJE")
													+ ""));
							sign1 = 1;
							break;
						}
					}
					if (sign1 == 0) {
						Map<String, Object> zy_fyhzmap = new HashMap<String, Object>();
						zy_fyhzmap.put("HZRQ", adt_hzrq);
						zy_fyhzmap
								.put("FYXM", lds_bqfs.get(ll_Row).get("FYXM"));
						zy_fyhzmap.put("SQJC", 0.00);
						zy_fyhzmap.put("JGID", gl_jgid);
						zy_fyhzmap.put(
								"BQFS",
								Double.parseDouble(lds_bqfs.get(ll_Row).get(
										"FYJE")
										+ ""));
						zy_fyhzmap.put("BQJS", 0.00);
						zy_fyhzmap.put("SJJC", 0.00);
						zy_fyhzmap.put("BQJC", 0.00);
						lds_bqfslist.add(zy_fyhzmap);
					}
				} else {
					Map<String, Object> zy_fyhzmap = new HashMap<String, Object>();
					zy_fyhzmap.put("HZRQ", adt_hzrq);
					zy_fyhzmap.put("FYXM", lds_bqfs.get(ll_Row).get("FYXM"));
					zy_fyhzmap.put("SQJC", 0.00);
					zy_fyhzmap.put("JGID", gl_jgid);
					zy_fyhzmap.put(
							"BQFS",
							Double.parseDouble(lds_bqfs.get(ll_Row).get("FYJE")
									+ ""));
					zy_fyhzmap.put("BQJS", 0.00);
					zy_fyhzmap.put("SJJC", 0.00);
					zy_fyhzmap.put("BQJC", 0.00);
					lds_bqfslist.add(zy_fyhzmap);
				}
			}
			if (lds_bqfslist.size() > 0) {
				zy_fyhzlist.addAll(lds_bqfslist);
			}
			// ３、取本期结算(bqjs)
			List<Map<String, Object>> lds_bqjs = dao
					.doQuery(
							"SELECT ZY_JSMX.FYXM as FYXM,sum(ZY_JSMX.ZJJE) AS FYJE FROM ZY_ZYJS ZY_ZYJS,ZY_JSMX ZY_JSMX WHERE ZY_ZYJS.ZYH=ZY_JSMX.ZYH AND ZY_ZYJS.JSCS=ZY_JSMX.JSCS AND	ZY_ZYJS.HZRQ=:adt_hzrq and ZY_ZYJS.JGID=:al_jgid and ZY_ZYJS.JSLX in(5,1) GROUP BY ZY_JSMX.FYXM UNION ALL SELECT ZY_JSMX.FYXM,- sum(ZY_JSMX.ZJJE) AS FYJE FROM ZY_ZYJS,ZY_JSMX,ZY_JSZF WHERE ZY_ZYJS.JSLX in(5,1) and ZY_ZYJS.ZYH=ZY_JSMX.ZYH AND ZY_ZYJS.JSCS=ZY_JSMX.JSCS AND ZY_ZYJS.ZYH=ZY_JSZF.ZYH AND ZY_ZYJS.JSCS=ZY_JSZF.JSCS AND ZY_JSZF.HZRQ=:adt_hzrq and ZY_ZYJS.JGID=:al_jgid GROUP BY ZY_JSMX.FYXM",
							parametersfyje);
			List<Map<String, Object>> lds_bqjslist = new ArrayList<Map<String, Object>>();
			int sign2 = 0;
			for (int ll_Row = 0; ll_Row < lds_bqjs.size(); ll_Row++) {
				if (zy_fyhzlist.size() > 0) {
					for (int j = 0; j < zy_fyhzlist.size(); j++) {
						if (lds_bqjs
								.get(ll_Row)
								.get("FYXM")
								.toString()
								.equals(zy_fyhzlist.get(j).get("FYXM")
										.toString())) {
							zy_fyhzlist.get(j).put(
									"BQJS",
									Double.parseDouble(zy_fyhzlist.get(j).get(
											"BQJS")
											+ "")
											+ Double.parseDouble(lds_bqjs.get(
													ll_Row).get("FYJE")
													+ ""));
							sign2 = 1;
							break;
						}
					}
					if (sign2 == 0) {
						Map<String, Object> zy_fyhzmap = new HashMap<String, Object>();
						zy_fyhzmap.put("HZRQ", adt_hzrq);
						zy_fyhzmap
								.put("FYXM", lds_bqjs.get(ll_Row).get("FYXM"));
						zy_fyhzmap.put("SQJC", 0.00);
						zy_fyhzmap.put("JGID", gl_jgid);
						zy_fyhzmap.put("BQFS", 0.00);
						zy_fyhzmap.put(
								"BQJS",
								Double.parseDouble(lds_bqjs.get(ll_Row).get(
										"FYJE")
										+ ""));
						zy_fyhzmap.put("SJJC", 0.00);
						zy_fyhzmap.put("BQJC", 0.00);
						lds_bqjslist.add(zy_fyhzmap);
					}

				} else {
					Map<String, Object> zy_fyhzmap = new HashMap<String, Object>();
					zy_fyhzmap.put("HZRQ", adt_hzrq);
					zy_fyhzmap.put("FYXM", lds_bqjs.get(ll_Row).get("FYXM"));
					zy_fyhzmap.put("SQJC", 0.00);
					zy_fyhzmap.put("JGID", gl_jgid);
					zy_fyhzmap.put("BQFS", 0.00);
					zy_fyhzmap.put(
							"BQJS",
							Double.parseDouble(lds_bqjs.get(ll_Row).get("FYJE")
									+ ""));
					zy_fyhzmap.put("SJJC", 0.00);
					zy_fyhzmap.put("BQJC", 0.00);
					lds_bqjslist.add(zy_fyhzmap);
				}
			}
			if (lds_bqjslist.size() > 0) {
				zy_fyhzlist.addAll(lds_bqjslist);
			}
			// ４、取实际结存(sjjc)
			List<Map<String, Object>> lds_sjjc = dao
					.doQuery(
							"SELECT ZY_FYMX.FYXM as FYXM,sum(ZY_FYMX.ZJJE) AS FYJE FROM ZY_FYMX ZY_FYMX WHERE ZY_FYMX.HZRQ<=:adt_hzrq AND ZY_FYMX.JSCS=0 and ZY_FYMX.JGID=:al_jgid GROUP BY ZY_FYMX.FYXM",
							parametersfyje);
			List<Map<String, Object>> lds_sjjclist = new ArrayList<Map<String, Object>>();
			int sign3 = 0;
			for (int ll_Row = 0; ll_Row < lds_sjjc.size(); ll_Row++) {
				if (zy_fyhzlist.size() > 0) {
					for (int j = 0; j < zy_fyhzlist.size(); j++) {
						if (lds_sjjc
								.get(ll_Row)
								.get("FYXM")
								.toString()
								.equals(zy_fyhzlist.get(j).get("FYXM")
										.toString())) {
							zy_fyhzlist.get(j).put(
									"SJJC",
									Double.parseDouble(zy_fyhzlist.get(j).get(
											"SJJC")
											+ "")
											+ Double.parseDouble(lds_sjjc.get(
													ll_Row).get("FYJE")
													+ ""));
							sign3 = 1;
							break;
						}
					}
					if (sign3 == 0) {
						Map<String, Object> zy_fyhzmap = new HashMap<String, Object>();
						zy_fyhzmap.put("HZRQ", adt_hzrq);
						zy_fyhzmap
								.put("FYXM", lds_sjjc.get(ll_Row).get("FYXM"));
						zy_fyhzmap.put("SQJC", 0);
						zy_fyhzmap.put("JGID", gl_jgid);
						zy_fyhzmap.put("BQFS", 0.00);
						zy_fyhzmap.put("BQJS", 0.00);
						zy_fyhzmap.put(
								"SJJC",
								Double.parseDouble(lds_sjjc.get(ll_Row).get(
										"FYJE")
										+ ""));
						zy_fyhzmap.put("BQJC", 0.00);
						lds_sjjclist.add(zy_fyhzmap);
					}

				} else {
					Map<String, Object> zy_fyhzmap = new HashMap<String, Object>();
					zy_fyhzmap.put("HZRQ", adt_hzrq);
					zy_fyhzmap.put("FYXM", lds_sjjc.get(ll_Row).get("FYXM"));
					zy_fyhzmap.put("SQJC", 0);
					zy_fyhzmap.put("JGID", gl_jgid);
					zy_fyhzmap.put("BQFS", 0.00);
					zy_fyhzmap.put("BQJS", 0.00);
					zy_fyhzmap.put(
							"SJJC",
							Double.parseDouble(lds_sjjc.get(ll_Row).get("FYJE")
									+ ""));
					zy_fyhzmap.put("BQJC", 0.00);
					lds_sjjclist.add(zy_fyhzmap);
				}
			}
			if (lds_sjjclist.size() > 0) {
				zy_fyhzlist.addAll(lds_sjjclist);
			}
			// ５、计算本期结存(bqjc) = 上期结存 + 本期发生 - 本期结算
			for (int ll_Row = 0; ll_Row < zy_fyhzlist.size(); ll_Row++) {
				zy_fyhzlist.get(ll_Row).put(
						"BQJC",
						Double.parseDouble(zy_fyhzlist.get(ll_Row).get("SQJC")
								+ "")
								+ Double.parseDouble(zy_fyhzlist.get(ll_Row)
										.get("BQFS") + "")
								- Double.parseDouble(zy_fyhzlist.get(ll_Row)
										.get("BQJS") + ""));
				// 删除全部项目为零的记录
				if (Double
						.parseDouble(zy_fyhzlist.get(ll_Row).get("SQJC") + "")
						+ Double.parseDouble(zy_fyhzlist.get(ll_Row)
								.get("BQFS") + "")
						+ Double.parseDouble(zy_fyhzlist.get(ll_Row)
								.get("BQJS") + "")
						+ Double.parseDouble(zy_fyhzlist.get(ll_Row)
								.get("BQJC") + "")
						+ Double.parseDouble(zy_fyhzlist.get(ll_Row)
								.get("SJJC") + "") == 0.0) {
					zy_fyhzlist.remove(ll_Row);
					ll_Row--;
				}
			}
			// ５、计算本期结存(bqjc) = 上期结存 + 本期发生 - 本期结算
			for (int ll_Row = 0; ll_Row < zy_fyhzlist.size(); ll_Row++) {
				Map<String, Object> zy_fyhzmap = new HashMap<String, Object>();
				zy_fyhzmap.put("HZRQ", zy_fyhzlist.get(ll_Row).get("HZRQ"));
				zy_fyhzmap.put("FYXM", zy_fyhzlist.get(ll_Row).get("FYXM"));
				zy_fyhzmap.put("SQJC", zy_fyhzlist.get(ll_Row).get("SQJC"));
				zy_fyhzmap.put("JGID", zy_fyhzlist.get(ll_Row).get("JGID"));
				zy_fyhzmap.put("BQFS", zy_fyhzlist.get(ll_Row).get("BQFS"));
				zy_fyhzmap.put("BQJS", zy_fyhzlist.get(ll_Row).get("BQJS"));
				zy_fyhzmap.put("SJJC", zy_fyhzlist.get(ll_Row).get("SJJC"));
				zy_fyhzmap.put("BQJC", zy_fyhzlist.get(ll_Row).get("BQJC"));
				dao.doSave("create", BSPHISEntryNames.ZY_FYHZ, zy_fyhzmap,
						false);
			}
		} catch (ParseException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		} catch (ValidateException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		}

	}

	/**
	 * 
	 * @param adt_hzrq
	 *            汇总日期
	 * @param dao
	 * @param ctx
	 * @return
	 * @throws ModelDataOperationException
	 */
	public static void fsb_wf_create_fyhz(Date adt_hzrq, BaseDAO dao,
			Context ctx) throws ModelDataOperationException {
		SimpleDateFormat sdfdatetime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
		// User user = (User) ctx.get("user.instance");
		// String gl_jgid = user.get("manageUnit.id");
		UserRoleToken user = UserRoleToken.getCurrent();
		String gl_jgid = user.getManageUnit().getId();// 用户的机构ID
		List<Map<String, Object>> JC_fyhzlist = new ArrayList<Map<String, Object>>();
		try {
			adt_hzrq = sdfdatetime
					.parse(sdfdate.format(adt_hzrq) + " 00:00:00");
			Map<String, Object> parametershzrq = new HashMap<String, Object>();
			Date ldt_sqhzrq = null; // 上期汇总日期
			parametershzrq.put("gl_jgid", gl_jgid);
			Map<String, Object> hzrqmap = dao
					.doLoad("SELECT max(HZRQ) as HZRQ FROM JC_FYHZ where JGID=:gl_jgid",
							parametershzrq);
			if (hzrqmap.get("HZRQ") != null) {
				ldt_sqhzrq = sdfdatetime.parse(hzrqmap.get("HZRQ") + "");
			}
			// １、取上期结存(sqjc)
			Map<String, Object> parameterslds_sqjc = new HashMap<String, Object>();
			parameterslds_sqjc.put("adt_hzrq", ldt_sqhzrq);
			parameterslds_sqjc.put("al_jgid", gl_jgid);
			List<Map<String, Object>> lds_sqjc = dao
					.doQuery(
							"SELECT JC_FYHZ.FYXM as FYXM,sum(JC_FYHZ.BQJC) AS FYJE FROM JC_FYHZ JC_FYHZ WHERE JC_FYHZ.HZRQ=:adt_hzrq and JC_FYHZ.JGID=:al_jgid GROUP BY JC_FYHZ.FYXM",
							parameterslds_sqjc);
			for (int ll_Row = 0; ll_Row < lds_sqjc.size(); ll_Row++) {
				Map<String, Object> JC_fyhzmap = new HashMap<String, Object>();
				JC_fyhzmap.put("HZRQ", adt_hzrq);
				JC_fyhzmap.put("FYXM", lds_sqjc.get(ll_Row).get("FYXM"));
				JC_fyhzmap.put("SQJC", lds_sqjc.get(ll_Row).get("FYJE"));
				JC_fyhzmap.put("JGID", gl_jgid);
				JC_fyhzmap.put("BQFS", 0.00);
				JC_fyhzmap.put("BQJS", 0.00);
				JC_fyhzmap.put("SJJC", 0.00);
				JC_fyhzmap.put("BQJC", 0.00);
				JC_fyhzlist.add(JC_fyhzmap);
			}
			// ２、取本期发生(bqfs)
			Map<String, Object> parametersfyje = new HashMap<String, Object>();
			parametersfyje.put("adt_hzrq", adt_hzrq);
			parametersfyje.put("al_jgid", gl_jgid);
			List<Map<String, Object>> lds_bqfs = dao
					.doQuery(
							"SELECT JC_FYMX.FYXM as FYXM,sum(JC_FYMX.ZJJE) AS FYJE FROM JC_FYMX JC_FYMX	WHERE JC_FYMX.HZRQ=:adt_hzrq and JC_FYMX.JGID=:al_jgid GROUP BY JC_FYMX.FYXM",
							parametersfyje);
			List<Map<String, Object>> lds_bqfslist = new ArrayList<Map<String, Object>>();
			int sign1 = 0;
			for (int ll_Row = 0; ll_Row < lds_bqfs.size(); ll_Row++) {
				if (JC_fyhzlist.size() > 0) {
					for (int j = 0; j < JC_fyhzlist.size(); j++) {
						if (lds_bqfs
								.get(ll_Row)
								.get("FYXM")
								.toString()
								.equals(JC_fyhzlist.get(j).get("FYXM")
										.toString())) {
							JC_fyhzlist.get(j).put(
									"BQFS",
									Double.parseDouble(JC_fyhzlist.get(j).get(
											"BQFS")
											+ "")
											+ Double.parseDouble(lds_bqfs.get(
													ll_Row).get("FYJE")
													+ ""));
							sign1 = 1;
							break;
						}
					}
					if (sign1 == 0) {
						Map<String, Object> JC_fyhzmap = new HashMap<String, Object>();
						JC_fyhzmap.put("HZRQ", adt_hzrq);
						JC_fyhzmap
								.put("FYXM", lds_bqfs.get(ll_Row).get("FYXM"));
						JC_fyhzmap.put("SQJC", 0.00);
						JC_fyhzmap.put("JGID", gl_jgid);
						JC_fyhzmap.put(
								"BQFS",
								Double.parseDouble(lds_bqfs.get(ll_Row).get(
										"FYJE")
										+ ""));
						JC_fyhzmap.put("BQJS", 0.00);
						JC_fyhzmap.put("SJJC", 0.00);
						JC_fyhzmap.put("BQJC", 0.00);
						lds_bqfslist.add(JC_fyhzmap);
					}
				} else {
					Map<String, Object> JC_fyhzmap = new HashMap<String, Object>();
					JC_fyhzmap.put("HZRQ", adt_hzrq);
					JC_fyhzmap.put("FYXM", lds_bqfs.get(ll_Row).get("FYXM"));
					JC_fyhzmap.put("SQJC", 0.00);
					JC_fyhzmap.put("JGID", gl_jgid);
					JC_fyhzmap.put(
							"BQFS",
							Double.parseDouble(lds_bqfs.get(ll_Row).get("FYJE")
									+ ""));
					JC_fyhzmap.put("BQJS", 0.00);
					JC_fyhzmap.put("SJJC", 0.00);
					JC_fyhzmap.put("BQJC", 0.00);
					lds_bqfslist.add(JC_fyhzmap);
				}
			}
			if (lds_bqfslist.size() > 0) {
				JC_fyhzlist.addAll(lds_bqfslist);
			}
			// ３、取本期结算(bqjs)
			List<Map<String, Object>> lds_bqjs = dao
					.doQuery(
							"SELECT JC_JSMX.FYXM as FYXM,sum(JC_JSMX.ZJJE) AS FYJE FROM JC_JCJS JC_JCJS,JC_JSMX JC_JSMX WHERE JC_JCJS.ZYH=JC_JSMX.ZYH AND JC_JCJS.JSCS=JC_JSMX.JSCS AND	JC_JCJS.HZRQ=:adt_hzrq and JC_JCJS.JGID=:al_jgid and JC_JCJS.JSLX in(5,1) GROUP BY JC_JSMX.FYXM UNION ALL SELECT JC_JSMX.FYXM,- sum(JC_JSMX.ZJJE) AS FYJE FROM JC_JCJS,JC_JSMX,JC_JSZF WHERE JC_JCJS.JSLX in(5,1) and JC_JCJS.ZYH=JC_JSMX.ZYH AND JC_JCJS.JSCS=JC_JSMX.JSCS AND JC_JCJS.ZYH=JC_JSZF.ZYH AND JC_JCJS.JSCS=JC_JSZF.JSCS AND JC_JSZF.HZRQ=:adt_hzrq and JC_JCJS.JGID=:al_jgid GROUP BY JC_JSMX.FYXM",
							parametersfyje);
			List<Map<String, Object>> lds_bqjslist = new ArrayList<Map<String, Object>>();
			int sign2 = 0;
			for (int ll_Row = 0; ll_Row < lds_bqjs.size(); ll_Row++) {
				if (JC_fyhzlist.size() > 0) {
					for (int j = 0; j < JC_fyhzlist.size(); j++) {
						if (lds_bqjs
								.get(ll_Row)
								.get("FYXM")
								.toString()
								.equals(JC_fyhzlist.get(j).get("FYXM")
										.toString())) {
							JC_fyhzlist.get(j).put(
									"BQJS",
									Double.parseDouble(JC_fyhzlist.get(j).get(
											"BQJS")
											+ "")
											+ Double.parseDouble(lds_bqjs.get(
													ll_Row).get("FYJE")
													+ ""));
							sign2 = 1;
							break;
						}
					}
					if (sign2 == 0) {
						Map<String, Object> JC_fyhzmap = new HashMap<String, Object>();
						JC_fyhzmap.put("HZRQ", adt_hzrq);
						JC_fyhzmap
								.put("FYXM", lds_bqjs.get(ll_Row).get("FYXM"));
						JC_fyhzmap.put("SQJC", 0.00);
						JC_fyhzmap.put("JGID", gl_jgid);
						JC_fyhzmap.put("BQFS", 0.00);
						JC_fyhzmap.put(
								"BQJS",
								Double.parseDouble(lds_bqjs.get(ll_Row).get(
										"FYJE")
										+ ""));
						JC_fyhzmap.put("SJJC", 0.00);
						JC_fyhzmap.put("BQJC", 0.00);
						lds_bqjslist.add(JC_fyhzmap);
					}

				} else {
					Map<String, Object> JC_fyhzmap = new HashMap<String, Object>();
					JC_fyhzmap.put("HZRQ", adt_hzrq);
					JC_fyhzmap.put("FYXM", lds_bqjs.get(ll_Row).get("FYXM"));
					JC_fyhzmap.put("SQJC", 0.00);
					JC_fyhzmap.put("JGID", gl_jgid);
					JC_fyhzmap.put("BQFS", 0.00);
					JC_fyhzmap.put(
							"BQJS",
							Double.parseDouble(lds_bqjs.get(ll_Row).get("FYJE")
									+ ""));
					JC_fyhzmap.put("SJJC", 0.00);
					JC_fyhzmap.put("BQJC", 0.00);
					lds_bqjslist.add(JC_fyhzmap);
				}
			}
			if (lds_bqjslist.size() > 0) {
				JC_fyhzlist.addAll(lds_bqjslist);
			}
			// ４、取实际结存(sjjc)
			List<Map<String, Object>> lds_sjjc = dao
					.doQuery(
							"SELECT JC_FYMX.FYXM as FYXM,sum(JC_FYMX.ZJJE) AS FYJE FROM JC_FYMX JC_FYMX WHERE JC_FYMX.HZRQ<=:adt_hzrq AND JC_FYMX.JSCS=0 and JC_FYMX.JGID=:al_jgid GROUP BY JC_FYMX.FYXM",
							parametersfyje);
			List<Map<String, Object>> lds_sjjclist = new ArrayList<Map<String, Object>>();
			int sign3 = 0;
			for (int ll_Row = 0; ll_Row < lds_sjjc.size(); ll_Row++) {
				if (JC_fyhzlist.size() > 0) {
					for (int j = 0; j < JC_fyhzlist.size(); j++) {
						if (lds_sjjc
								.get(ll_Row)
								.get("FYXM")
								.toString()
								.equals(JC_fyhzlist.get(j).get("FYXM")
										.toString())) {
							JC_fyhzlist.get(j).put(
									"SJJC",
									Double.parseDouble(JC_fyhzlist.get(j).get(
											"SJJC")
											+ "")
											+ Double.parseDouble(lds_sjjc.get(
													ll_Row).get("FYJE")
													+ ""));
							sign3 = 1;
							break;
						}
					}
					if (sign3 == 0) {
						Map<String, Object> JC_fyhzmap = new HashMap<String, Object>();
						JC_fyhzmap.put("HZRQ", adt_hzrq);
						JC_fyhzmap
								.put("FYXM", lds_sjjc.get(ll_Row).get("FYXM"));
						JC_fyhzmap.put("SQJC", 0);
						JC_fyhzmap.put("JGID", gl_jgid);
						JC_fyhzmap.put("BQFS", 0.00);
						JC_fyhzmap.put("BQJS", 0.00);
						JC_fyhzmap.put(
								"SJJC",
								Double.parseDouble(lds_sjjc.get(ll_Row).get(
										"FYJE")
										+ ""));
						JC_fyhzmap.put("BQJC", 0.00);
						lds_sjjclist.add(JC_fyhzmap);
					}

				} else {
					Map<String, Object> JC_fyhzmap = new HashMap<String, Object>();
					JC_fyhzmap.put("HZRQ", adt_hzrq);
					JC_fyhzmap.put("FYXM", lds_sjjc.get(ll_Row).get("FYXM"));
					JC_fyhzmap.put("SQJC", 0);
					JC_fyhzmap.put("JGID", gl_jgid);
					JC_fyhzmap.put("BQFS", 0.00);
					JC_fyhzmap.put("BQJS", 0.00);
					JC_fyhzmap.put(
							"SJJC",
							Double.parseDouble(lds_sjjc.get(ll_Row).get("FYJE")
									+ ""));
					JC_fyhzmap.put("BQJC", 0.00);
					lds_sjjclist.add(JC_fyhzmap);
				}
			}
			if (lds_sjjclist.size() > 0) {
				JC_fyhzlist.addAll(lds_sjjclist);
			}
			// ５、计算本期结存(bqjc) = 上期结存 + 本期发生 - 本期结算
			for (int ll_Row = 0; ll_Row < JC_fyhzlist.size(); ll_Row++) {
				JC_fyhzlist.get(ll_Row).put(
						"BQJC",
						Double.parseDouble(JC_fyhzlist.get(ll_Row).get("SQJC")
								+ "")
								+ Double.parseDouble(JC_fyhzlist.get(ll_Row)
										.get("BQFS") + "")
								- Double.parseDouble(JC_fyhzlist.get(ll_Row)
										.get("BQJS") + ""));
				// 删除全部项目为零的记录
				if (Double
						.parseDouble(JC_fyhzlist.get(ll_Row).get("SQJC") + "")
						+ Double.parseDouble(JC_fyhzlist.get(ll_Row)
								.get("BQFS") + "")
						+ Double.parseDouble(JC_fyhzlist.get(ll_Row)
								.get("BQJS") + "")
						+ Double.parseDouble(JC_fyhzlist.get(ll_Row)
								.get("BQJC") + "")
						+ Double.parseDouble(JC_fyhzlist.get(ll_Row)
								.get("SJJC") + "") == 0.0) {
					JC_fyhzlist.remove(ll_Row);
					ll_Row--;
				}
			}
			// ５、计算本期结存(bqjc) = 上期结存 + 本期发生 - 本期结算
			for (int ll_Row = 0; ll_Row < JC_fyhzlist.size(); ll_Row++) {
				Map<String, Object> JC_fyhzmap = new HashMap<String, Object>();
				JC_fyhzmap.put("HZRQ", JC_fyhzlist.get(ll_Row).get("HZRQ"));
				JC_fyhzmap.put("FYXM", JC_fyhzlist.get(ll_Row).get("FYXM"));
				JC_fyhzmap.put("SQJC", JC_fyhzlist.get(ll_Row).get("SQJC"));
				JC_fyhzmap.put("JGID", JC_fyhzlist.get(ll_Row).get("JGID"));
				JC_fyhzmap.put("BQFS", JC_fyhzlist.get(ll_Row).get("BQFS"));
				JC_fyhzmap.put("BQJS", JC_fyhzlist.get(ll_Row).get("BQJS"));
				JC_fyhzmap.put("SJJC", JC_fyhzlist.get(ll_Row).get("SJJC"));
				JC_fyhzmap.put("BQJC", JC_fyhzlist.get(ll_Row).get("BQJC"));
				dao.doSave("create", BSPHISEntryNames.JC_FYHZ, JC_fyhzmap,
						false);
			}
		} catch (ParseException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		} catch (PersistentDataOperationException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		} catch (ValidateException e) {
			throw new ModelDataOperationException(
					ServiceCode.CODE_DATABASE_ERROR, "汇总失败,数据库处理异常!", e);
		}

	}

	/**
	 * 
	 * @author caijy
	 * @createDate 2012-11-21
	 * @description 数据转换成double
	 * @updateInfo
	 * @param o
	 * @return
	 */
	public static double parseDouble(Object o) {
		if (o == null || "null".equals(o)) {
			return new Double(0);
		}
		return Double.parseDouble(o + "");
	}

	/**
	 * 
	 * @param adt_hzrq_start
	 *            汇总开始日期
	 * @param adt_hzrq_end
	 *            汇总结束日期
	 * @param dao
	 * @param ctx
	 * @throws ModelDataOperationException
	 */
	public static void wf_Query(Map<String, Object> request,
			Map<String, Object> response, List<Map<String, Object>> resultLi,
			BaseDAO dao, Context ctx) throws ModelDataOperationException {
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// User user = (User) ctx.get("user.instance");
		// String jgname = user.get("manageUnit.name");
		// String manaUnitId = user.get("manageUnit.id");// 用户的机构ID
		UserRoleToken user = UserRoleToken.getCurrent();
		String jgname = user.getManageUnit().getName();
		String manaUnitId = user.getManageUnit().getId();// 用户的机构ID
		response.put("title", jgname);
		Date adt_hzrq_start = null;
		Date adt_hzrq_end = null;
		try {
			if (request.get("beginDate") != null) {
				adt_hzrq_start = sdfdate.parse(request.get("beginDate") + "");
			}
			if (request.get("endDate") != null) {
				adt_hzrq_end = sdfdate.parse(request.get("endDate") + "");
			}
			String[] lS_ksrq = (request.get("beginDate") + "").split("-| ");
			String ksrq = lS_ksrq[0] + "年" + lS_ksrq[1] + "月" + lS_ksrq[2]
					+ "日";
			response.put("HZRQ", "汇总日期:" + ksrq);
			Map<String, Object> parametershzbd = new HashMap<String, Object>();
			parametershzbd.put("adt_hzrq_b", adt_hzrq_start);
			parametershzbd.put("adt_hzrq_e", adt_hzrq_end);
			parametershzbd.put("al_jgid", manaUnitId);
			// 日结汇总汇总表单
			// List<Map<String, Object>> resultList2 = dao
			// .doQuery(
			// "SELECT ZY_JZXX.QTYSFB as QTYSFB FROM ZY_JZXX ZY_JZXX WHERE ZY_JZXX.HZRQ>=:adt_hzrq_b AND ZY_JZXX.HZRQ<=:adt_hzrq_e AND ZY_JZXX.JGID=:al_jgid ",
			// parametershzbd);
			// if(resultList2.size()>0){
			// response.put("qtysFb",resultList2.get(0).get("QTYSFB")+"");
			// }
			List<Map<String, Object>> resultList = dao
					.doQuery(
							"SELECT ZY_JZXX.CZGH as CZGH,sum(ZY_JZXX.CYSR) as CYSR,sum(ZY_JZXX.YJJE) as YJJE,sum(ZY_JZXX.TPJE) as TPJE,sum(ZY_JZXX.FPZS) as FPZS,sum(ZY_JZXX.SJZS) as SJZS,sum(ZY_JZXX.YSJE) as YSJE,sum(ZY_JZXX.YSXJ) as YSXJ,sum(ZY_JZXX.ZPZS) as ZPZS,sum(ZY_JZXX.TYJJ) as TYJJ,sum(ZY_JZXX.YSQT) as YSQT,sum(ZY_JZXX.QTZS) as QTZS,sum(ZY_JZXX.SRJE) as SRJE,sum(ZY_JZXX.TCZC) as TCZC,sum(ZY_JZXX.DBZC) as DBZC,sum(ZY_JZXX.ZXJZFY) as ZXJZFY,sum(ZY_JZXX.GRXJZF) as GRXJZF,sum(ZY_JZXX.BCZHZF) as BCZHZF,sum(ZY_JZXX.AZQGFY) as AZQGFY FROM ZY_JZXX ZY_JZXX WHERE ZY_JZXX.HZRQ>=:adt_hzrq_b AND ZY_JZXX.HZRQ<=:adt_hzrq_e AND ZY_JZXX.JGID=:al_jgid group by ZY_JZXX.CZGH order by ZY_JZXX.CZGH",
							parametershzbd);
			for (int i = 0; i < resultList.size(); i++) {
				// for(int j=0;j<resultList2.size();j++){
				// if(i==j){
				// resultList.get(i).put("QTYSFB",
				// resultList2.get(j).get("QTYSFB"));
				// }
				// }
				String ids_fkfs_hql = "select d.FKFS as FKFS,sum(d.FKJE) as FKJE,e.FKMC as FKMC from ("
						+ "select a.FKFS as FKFS, (-1*a.FKJE) as FKJE from ZY_FKXX a, ZY_JSZF b where a.ZYH = b.ZYH and a.JSCS = b.JSCS and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid and b.ZFGH = :czgh"
						+ " union all "
						+ "select a.FKFS as FKFS, a.FKJE as FKJE from ZY_FKXX a, ZY_ZYJS b where b.JSLX<>4 and a.ZYH = b.ZYH and a.JSCS = b.JSCS and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid and b.CZGH = :czgh"
						+ " union all "
						+ "SELECT a.JKFS as FKFS,a.JKJE as FKJE FROM ZY_TBKK a WHERE a.HZRQ>=:adt_hzrq_b AND a.HZRQ<=:adt_hzrq_e AND a.JGID=:al_jgid and a.CZGH = :czgh"
						+ " union all "
						+ "SELECT a.JKFS as FKFS,(-1*a.JKJE) as FKJE FROM ZY_TBKK a ,ZY_JKZF b WHERE b.JKXH = a.JKXH and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid and b.ZFGH = :czgh"
						+ ") d left outer join GY_FKFS e on d.FKFS = e.FKFS group by d.FKFS,e.FKMC";
				String ids_brxz_hql = "select sum(c.FYHJ) as FYHJ,sum(c.ZFHJ) as ZFHJ,c.BRXZ as BRXZ,d.XZMC as XZMC,d.DBPB as DBPB from ("
						+ "SELECT a.FYHJ as FYHJ,a.ZFHJ as ZFHJ,a.BRXZ as BRXZ FROM ZY_ZYJS a WHERE a.JSLX<>4 and a.FYHJ<>a.ZFHJ and a.HZRQ>=:adt_hzrq_b AND a.HZRQ<=:adt_hzrq_e AND a.JGID=:al_jgid and a.CZGH = :czgh"
						+ " union all "
						+ "SELECT (-1*a.FYHJ) as FYHJ,(-1*a.ZFHJ) as ZFHJ,a.BRXZ as BRXZ FROM ZY_ZYJS a ,ZY_JSZF b WHERE a.JSLX<>4 and a.ZYH = b.ZYH AND a.JSCS = b.JSCS and a.FYHJ<>a.ZFHJ and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid and b.ZFGH = :czgh"
						+ ") c left outer join GY_BRXZ d on c.BRXZ = d.BRXZ group by c.BRXZ,d.XZMC,d.DBPB";
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("adt_hzrq_b", adt_hzrq_start);
				parameters.put("adt_hzrq_e", adt_hzrq_end);
				parameters.put("al_jgid", manaUnitId);
				parameters.put("czgh", resultList.get(i).get("CZGH") + "");
				List<Map<String, Object>> ids_brxz = dao.doSqlQuery(
						ids_brxz_hql, parameters);
				List<Map<String, Object>> ids_fkfs = dao.doSqlQuery(
						ids_fkfs_hql, parameters);
				String qtysFb = "";
				String jzjeSt = "0.00";
				if (ids_fkfs != null && ids_fkfs.size() != 0) {
					for (int n = 0; n < ids_fkfs.size(); n++) {
						qtysFb = qtysFb
								+ ids_fkfs.get(n).get("FKMC")
								+ ":"
								+ String.format("%1$.2f",
										ids_fkfs.get(n).get("FKJE")) + " ";
					}
				}
				if (ids_brxz != null && ids_brxz.size() != 0) {
					for (int n = 0; n < ids_brxz.size(); n++) {
						if (Integer.parseInt(ids_brxz.get(n).get("DBPB") + "") == 0) {
							jzjeSt = String
									.format("%1$.2f",
											parseDouble(jzjeSt)
													+ (parseDouble(ids_brxz
															.get(n).get("FYHJ")
															+ "") - parseDouble(ids_brxz
															.get(n).get("ZFHJ")
															+ "")));
						} else {
							qtysFb = qtysFb
									+ ids_brxz.get(n).get("XZMC")
									+ ":"
									+ String.format(
											"%1$.2f",
											(parseDouble(ids_brxz.get(n).get(
													"FYHJ")
													+ "") - parseDouble(ids_brxz
													.get(n).get("ZFHJ") + "")))
									+ " ";
						}
					}
					qtysFb = qtysFb + " " + "记账 :" + jzjeSt + " ";
				}
				resultList.get(i).put("QTYSFB", qtysFb);
				resultList.get(i).put("CYSR",
						String.format("%1$.2f", resultList.get(i).get("CYSR")));
				resultList.get(i).put("YJJE",
						String.format("%1$.2f", resultList.get(i).get("YJJE")));
				resultList.get(i).put("TPJE",
						String.format("%1$.2f", resultList.get(i).get("TPJE")));
				resultList.get(i).put("YSJE",
						String.format("%1$.2f", resultList.get(i).get("YSJE")));
				resultList.get(i).put("YSXJ",
						String.format("%1$.2f", resultList.get(i).get("YSXJ")));
				resultList.get(i).put("TYJJ",
						String.format("%1$.2f", resultList.get(i).get("TYJJ")));
				resultList.get(i).put("YSQT",
						String.format("%1$.2f", resultList.get(i).get("YSQT")));
				resultList.get(i).put("SRJE",
						String.format("%1$.2f", resultList.get(i).get("SRJE")));
				// if (resultList.get(i).get("SZYB") != null) {
				// resultList.get(i).put(
				// "SZYB",
				// String.format("%1$.2f",
				// resultList.get(i).get("SZYB")));
				// }
				// if (resultList.get(i).get("SYB") != null) {
				// resultList.get(i).put(
				// "SYB",
				// String.format("%1$.2f", resultList.get(i)
				// .get("SYB")));
				// }
				// if (resultList.get(i).get("YHYB") != null) {
				// resultList.get(i).put(
				// "YHYB",
				// String.format("%1$.2f",
				// resultList.get(i).get("YHYB")));
				// }
				// if (resultList.get(i).get("SMK") != null) {
				// resultList.get(i).put(
				// "SMK",
				// String.format("%1$.2f", resultList.get(i)
				// .get("SMK")));
				// }
				if (resultList.get(i).get("TCZC") != null) {
					resultList.get(i).put(
							"TCZC",
							String.format("%1$.2f",
									resultList.get(i).get("TCZC")));
				}
				if (resultList.get(i).get("DBZC") != null) {
					resultList.get(i).put(
							"DBZC",
							String.format("%1$.2f",
									resultList.get(i).get("DBZC")));
				}
				if (resultList.get(i).get("ZXJZFY") != null) {
					resultList.get(i).put(
							"ZXJZFY",
							String.format("%1$.2f",
									resultList.get(i).get("ZXJZFY")));
				}
				if (resultList.get(i).get("GRXJZF") != null) {
					resultList.get(i).put(
							"GRXJZF",
							String.format("%1$.2f",
									resultList.get(i).get("GRXJZF")));
				}
				if (resultList.get(i).get("BCZHZF") != null) {
					resultList.get(i).put(
							"BCZHZF",
							String.format("%1$.2f",
									resultList.get(i).get("BCZHZF")));
				}
				if (resultList.get(i).get("AZQGFY") != null) {
					resultList.get(i).put(
							"AZQGFY",
							String.format("%1$.2f",
									resultList.get(i).get("AZQGFY")));
				}
			}
			Map<String, Object> parametershj = dao
					.doLoad("SELECT sum(ZY_JZXX.CYSR) as ZCYSR,sum(ZY_JZXX.YJJE) as ZYJJE,sum(ZY_JZXX.TPJE) as ZTPJE,sum(ZY_JZXX.FPZS) as ZFPZS,sum(ZY_JZXX.SJZS) as ZSJZS,sum(ZY_JZXX.YSJE) as ZYSJE,sum(ZY_JZXX.YSXJ) as ZYSXJ,sum(ZY_JZXX.ZPZS) as ZZPZS,sum(ZY_JZXX.TYJJ) as ZTYJJ,sum(ZY_JZXX.YSQT) as ZYSQT,sum(ZY_JZXX.QTZS) as ZQTZS,sum(ZY_JZXX.SRJE) as ZSRJE,sum(ZY_JZXX.TCZC) as TCZC,sum(ZY_JZXX.DBZC) as DBZC,sum(ZY_JZXX.ZXJZFY) as ZXJZFY,sum(ZY_JZXX.GRXJZF) as GRXJZF,sum(ZY_JZXX.BCZHZF) as BCZHZF,sum(ZY_JZXX.AZQGFY) as AZQGFY FROM ZY_JZXX ZY_JZXX WHERE ZY_JZXX.HZRQ>=:adt_hzrq_b AND ZY_JZXX.HZRQ<=:adt_hzrq_e AND ZY_JZXX.JGID=:al_jgid",
							parametershzbd);
			String ids_fkfs_hql = "select d.FKFS as FKFS,sum(d.FKJE) as FKJE,e.FKMC as FKMC from ("
					+ "select a.FKFS as FKFS, (-1*a.FKJE) as FKJE from ZY_FKXX a, ZY_JSZF b where a.ZYH = b.ZYH and a.JSCS = b.JSCS and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid"
					+ " union all "
					+ "select a.FKFS as FKFS, a.FKJE as FKJE from ZY_FKXX a, ZY_ZYJS b where b.JSLX<>4 and a.ZYH = b.ZYH and a.JSCS = b.JSCS and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid"
					+ " union all "
					+ "SELECT a.JKFS as FKFS,a.JKJE as FKJE FROM ZY_TBKK a WHERE a.HZRQ>=:adt_hzrq_b AND a.HZRQ<=:adt_hzrq_e AND a.JGID=:al_jgid"
					+ " union all "
					+ "SELECT a.JKFS as FKFS,(-1*a.JKJE) as FKJE FROM ZY_TBKK a ,ZY_JKZF b WHERE b.JKXH = a.JKXH and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid"
					+ ") d left outer join GY_FKFS e on d.FKFS = e.FKFS group by d.FKFS,e.FKMC";
			String ids_brxz_hql = "select sum(c.FYHJ) as FYHJ,sum(c.ZFHJ) as ZFHJ,c.BRXZ as BRXZ,d.XZMC as XZMC,d.DBPB as DBPB from ("
					+ "SELECT a.FYHJ as FYHJ,a.ZFHJ as ZFHJ,a.BRXZ as BRXZ FROM ZY_ZYJS a WHERE a.JSLX<>4 and a.FYHJ<>a.ZFHJ and a.HZRQ>=:adt_hzrq_b AND a.HZRQ<=:adt_hzrq_e AND a.JGID=:al_jgid"
					+ " union all "
					+ "SELECT (-1*a.FYHJ) as FYHJ,(-1*a.ZFHJ) as ZFHJ,a.BRXZ as BRXZ FROM ZY_ZYJS a ,ZY_JSZF b WHERE a.JSLX<>4 and a.ZYH = b.ZYH AND a.JSCS = b.JSCS and a.FYHJ<>a.ZFHJ and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid"
					+ ") c left outer join GY_BRXZ d on c.BRXZ = d.BRXZ group by c.BRXZ,d.XZMC,d.DBPB";
			List<Map<String, Object>> ids_brxz = dao.doSqlQuery(ids_brxz_hql,
					parametershzbd);
			List<Map<String, Object>> ids_fkfs = dao.doSqlQuery(ids_fkfs_hql,
					parametershzbd);
			String qtysFb = "";
			String jzjeSt = "0.00";
			if (ids_fkfs != null && ids_fkfs.size() != 0) {
				for (int i = 0; i < ids_fkfs.size(); i++) {
					qtysFb = qtysFb
							+ ids_fkfs.get(i).get("FKMC")
							+ ":"
							+ String.format("%1$.2f",
									ids_fkfs.get(i).get("FKJE")) + " ";
				}
			}
			if (ids_brxz != null && ids_brxz.size() != 0) {
				for (int i = 0; i < ids_brxz.size(); i++) {
					if (Integer.parseInt(ids_brxz.get(i).get("DBPB") + "") == 0) {
						jzjeSt = String.format(
								"%1$.2f",
								parseDouble(jzjeSt)
										+ (parseDouble(ids_brxz.get(i).get(
												"FYHJ")
												+ "") - parseDouble(ids_brxz
												.get(i).get("ZFHJ") + "")));
					} else {
						qtysFb = qtysFb
								+ ids_brxz.get(i).get("XZMC")
								+ ":"
								+ String.format(
										"%1$.2f",
										(parseDouble(ids_brxz.get(i)
												.get("FYHJ") + "") - parseDouble(ids_brxz
												.get(i).get("ZFHJ") + "")))
								+ " ";
					}
				}
				qtysFb = qtysFb + " " + "记账 :" + jzjeSt + " ";
			}
			response.put("qtysFb", qtysFb);
			if (parametershj.get("ZCYSR") != null) {
				response.put("ZCYSR",
						String.format("%1$.2f", parametershj.get("ZCYSR")));
			} else {
				response.put("ZCYSR", "");
			}
			if (parametershj.get("ZYJJE") != null) {
				response.put("ZYJJE",
						String.format("%1$.2f", parametershj.get("ZYJJE")));
			} else {
				response.put("ZYJJE", "");
			}
			if (parametershj.get("ZTPJE") != null) {
				response.put("ZTPJE",
						String.format("%1$.2f", parametershj.get("ZTPJE")));
			} else {
				response.put("ZTPJE", "");
			}
			if (parametershj.get("ZFPZS") != null) {
				response.put("ZFPZS", parametershj.get("ZFPZS") + "");
			} else {
				response.put("ZFPZS", "");
			}
			if (parametershj.get("ZSJZS") != null) {
				response.put("ZSJZS", parametershj.get("ZSJZS") + "");
			} else {
				response.put("ZSJZS", "");
			}
			if (parametershj.get("ZTYJJ") != null) {
				response.put("ZTYJJ",
						String.format("%1$.2f", parametershj.get("ZTYJJ")));
			} else {
				response.put("ZTYJJ", "");
			}
			if (parametershj.get("ZYSJE") != null) {
				response.put("ZYSJE",
						String.format("%1$.2f", parametershj.get("ZYSJE")));
			} else {
				response.put("ZYSJE", "");
			}
			if (parametershj.get("ZYSXJ") != null) {
				response.put("ZYSXJ",
						String.format("%1$.2f", parametershj.get("ZYSXJ")));
			} else {
				response.put("ZYSXJ", "");

			}
			if (parametershj.get("ZYSQT") != null) {
				response.put("ZYSQT",
						String.format("%1$.2f", parametershj.get("ZYSQT")));
			} else {
				response.put("ZYSQT", "");
			}
			if (parametershj.get("ZQTZS") != null) {
				response.put("ZQTZS", parametershj.get("ZQTZS") + "");
			} else {
				response.put("ZQTZS", "");
			}
			if (parametershj.get("ZSRJE") != null) {
				response.put("ZSRJE",
						String.format("%1$.2f", parametershj.get("ZSRJE")));
			} else {
				response.put("ZSRJE", "");
			}
			if (parametershj.get("TCZC") != null) {
				response.put("TCZCHJ",
						String.format("%1$.2f", parametershj.get("TCZC")));
			} else {
				response.put("TCZCHJ", "");
			}
			if (parametershj.get("DBZC") != null) {
				response.put("DBZCHJ",
						String.format("%1$.2f", parametershj.get("DBZC")));
			} else {
				response.put("DBZCHJ", "");
			}
			if (parametershj.get("ZXJZFY") != null) {
				response.put("ZXJZFYHJ",
						String.format("%1$.2f", parametershj.get("ZXJZFY")));
			} else {
				response.put("ZXJZFYHJ", "");
			}
			if (parametershj.get("GRXJZF") != null) {
				response.put("GRXJZFHJ",
						String.format("%1$.2f", parametershj.get("GRXJZF")));
			} else {
				response.put("GRXJZFHJ", "");
			}
			if (parametershj.get("BCZHZF") != null) {
				response.put("BCZHZFHJ",
						String.format("%1$.2f", parametershj.get("BCZHZF")));
			} else {
				response.put("BCZHZFHJ", "");
			}
			if (parametershj.get("AZQGFY") != null) {
				response.put("AZQGFYHJ",
						String.format("%1$.2f", parametershj.get("AZQGFY")));
			} else {
				response.put("AZQGFYHJ", "");
			}
			// if (parametershj.get("SZYB") != null) {
			// response.put("SZYBHJ",
			// String.format("%1$.2f", parametershj.get("SZYB")));
			// }
			// if (parametershj.get("SYB") != null) {
			// response.put("SYBHJ",
			// String.format("%1$.2f", parametershj.get("SYB")));
			// }
			// if (parametershj.get("YHYB") != null) {
			// response.put("YHYBHJ",
			// String.format("%1$.2f", parametershj.get("YHYB")));
			// }
			// if (parametershj.get("SMK") != null) {
			// response.put("SMKHJ",
			// String.format("%1$.2f", parametershj.get("SMK")));
			// }
			List<Map<String, Object>> zfzslist = new ArrayList<Map<String, Object>>();
			StringBuffer sbfp = new StringBuffer();
			StringBuffer sbsj = new StringBuffer();
			zfzslist = dao
					.doQuery(
							"SELECT ZY_ZFPJ.PJLB as PJLB,ZY_ZFPJ.PJHM as PJHM FROM ZY_ZFPJ ZY_ZFPJ,ZY_JZXX ZY_JZXX WHERE ( ZY_ZFPJ.JZRQ = ZY_JZXX.JZRQ ) AND ( ZY_ZFPJ.CZGH = ZY_JZXX.CZGH ) AND ZY_JZXX.HZRQ>=:adt_hzrq_b AND ZY_JZXX.HZRQ<=:adt_hzrq_e AND ZY_JZXX.JGID=:al_jgid ORDER BY ZY_ZFPJ.PJLB,ZY_ZFPJ.PJHM",
							parametershzbd);
			for (int i = 0; i < zfzslist.size(); i++) {
				if (Integer.parseInt(zfzslist.get(i).get("PJLB") + "") == 1) {
					if (zfzslist.get(i).get("PJHM") != null) {
						sbfp.append(zfzslist.get(i).get("PJHM") + " ");
					}
				} else {
					if (zfzslist.get(i).get("PJHM") != null) {
						sbsj.append(zfzslist.get(i).get("PJHM") + " ");
					}
				}
			}
			if (sbfp.toString() != null && sbfp.length() > 0) {
				response.put(
						"ZFFPHM",
						sbfp.toString().substring(0,
								sbfp.toString().length() - 1));
			} else {
				response.put("ZFFPHM", "");
			}
			if (sbsj.toString() != null && sbsj.length() > 0) {
				response.put(
						"ZFSJHM",
						sbsj.toString().substring(0,
								sbsj.toString().length() - 1));
			} else {
				response.put("ZFSJHM", "");
			}
			List<Map<String, Object>> zfpjlist = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> parametersczgh = new HashMap<String, Object>();
				parametersczgh.put("adt_hzrq_b", adt_hzrq_start);
				parametersczgh.put("adt_hzrq_e", adt_hzrq_end);
				parametersczgh.put("as_czgh", resultList.get(i).get("CZGH"));
				parametersczgh.put("gl_jgid", manaUnitId);
				StringBuffer ls_jsfp_all = new StringBuffer();
				StringBuffer ls_jssj_all = new StringBuffer();
				zfpjlist = dao
						.doQuery(
								"SELECT QZPJ as QZPJ,QZSJ as QZSJ From ZY_JZXX Where CZGH=:as_czgh And HZRQ>=:adt_hzrq_b And HZRQ<=:adt_hzrq_e And JGID=:gl_jgid",
								parametersczgh);
				for (int j = 0; j < zfpjlist.size(); j++) {
					if (zfpjlist.get(j).get("QZPJ") != null) {
						ls_jsfp_all.append(zfpjlist.get(j).get("QZPJ") + "");
					}
					if (zfpjlist.get(j).get("QZSJ") != null) {
						ls_jssj_all.append(zfpjlist.get(j).get("QZSJ") + "");
					}
				}
				if (ls_jsfp_all.toString() != null && ls_jsfp_all.length() > 0) {
					resultList.get(i).put("QZPJ", ls_jsfp_all.toString());
				} else {
					resultList.get(i).put("QZPJ", "");
				}
				if (ls_jssj_all.toString() != null && ls_jssj_all.length() > 0) {
					resultList.get(i).put("QZSJ", ls_jssj_all.toString());
				} else {
					resultList.get(i).put("QZSJ", "");
				}
			}
			resultLi.addAll(resultList);
		} catch (PersistentDataOperationException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param adt_hzrq_start
	 *            家床汇总开始日期
	 * @param adt_hzrq_end
	 *            汇总结束日期
	 * @param dao
	 * @param ctx
	 * @throws ModelDataOperationException
	 */
	public static void fsb_wf_Query(Map<String, Object> request,
			Map<String, Object> response, List<Map<String, Object>> resultLi,
			BaseDAO dao, Context ctx) throws ModelDataOperationException {
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// User user = (User) ctx.get("user.instance");
		// String jgname = user.get("manageUnit.name");
		// String manaUnitId = user.get("manageUnit.id");// 用户的机构ID
		UserRoleToken user = UserRoleToken.getCurrent();
		String jgname = user.getManageUnit().getName();
		String manaUnitId = user.getManageUnit().getId();// 用户的机构ID
		response.put("title", jgname);
		Date adt_hzrq_start = null;
		Date adt_hzrq_end = null;
		try {
			if (request.get("beginDate") != null) {
				adt_hzrq_start = sdfdate.parse(request.get("beginDate") + "");
			}
			if (request.get("endDate") != null) {
				adt_hzrq_end = sdfdate.parse(request.get("endDate") + "");
			}
			String[] lS_ksrq = (request.get("beginDate") + "").split("-| ");
			String ksrq = lS_ksrq[0] + "年" + lS_ksrq[1] + "月" + lS_ksrq[2]
					+ "日";
			String[] lS_jsrq = (request.get("endDate") + "").split("-| ");
			String jsrq = lS_jsrq[0] + "年" + lS_jsrq[1] + "月" + lS_jsrq[2]
					+ "日";
			response.put("HZRQ", "汇总日期:" + ksrq + " 至 " + jsrq);
			Map<String, Object> parametershzbd = new HashMap<String, Object>();
			parametershzbd.put("adt_hzrq_b", adt_hzrq_start);
			parametershzbd.put("adt_hzrq_e", adt_hzrq_end);
			parametershzbd.put("al_jgid", manaUnitId);
			// 日结汇总汇总表单
			// List<Map<String, Object>> resultList2 = dao
			// .doQuery(
			// "SELECT JC_JZXX.QTYSFB as QTYSFB FROM JC_JZXX JC_JZXX WHERE JC_JZXX.HZRQ>=:adt_hzrq_b AND JC_JZXX.HZRQ<=:adt_hzrq_e AND JC_JZXX.JGID=:al_jgid ",
			// parametershzbd);
			// if(resultList2.size()>0){
			// response.put("qtysFb",resultList2.get(0).get("QTYSFB")+"");
			// }
			List<Map<String, Object>> resultList = dao
					.doQuery(
							"SELECT JC_JZXX.CZGH as CZGH,sum(JC_JZXX.CYSR) as CYSR,sum(JC_JZXX.YJJE) as YJJE,sum(JC_JZXX.TPJE) as TPJE,sum(JC_JZXX.FPZS) as FPZS,sum(JC_JZXX.SJZS) as SJZS,sum(JC_JZXX.YSJE) as YSJE,sum(JC_JZXX.YSXJ) as YSXJ,sum(JC_JZXX.ZPZS) as ZPZS,sum(JC_JZXX.TYJJ) as TYJJ,sum(JC_JZXX.YSQT) as YSQT,sum(JC_JZXX.QTZS) as QTZS,sum(JC_JZXX.SRJE) as SRJE,sum(JC_JZXX.TCZC) as TCZC,sum(JC_JZXX.DBZC) as DBZC,sum(JC_JZXX.ZXJZFY) as ZXJZFY,sum(JC_JZXX.GRXJZF) as GRXJZF,sum(JC_JZXX.BCZHZF) as BCZHZF,sum(JC_JZXX.AZQGFY) as AZQGFY FROM JC_JZXX JC_JZXX WHERE JC_JZXX.HZRQ>=:adt_hzrq_b AND JC_JZXX.HZRQ<=:adt_hzrq_e AND JC_JZXX.JGID=:al_jgid group by JC_JZXX.CZGH order by JC_JZXX.CZGH",
							parametershzbd);
			for (int i = 0; i < resultList.size(); i++) {
				// for(int j=0;j<resultList2.size();j++){
				// if(i==j){
				// resultList.get(i).put("QTYSFB",
				// resultList2.get(j).get("QTYSFB"));
				// }
				// }
				String ids_fkfs_hql = "select d.FKFS as FKFS,sum(d.FKJE) as FKJE,e.FKMC as FKMC from ("
						+ "select a.FKFS as FKFS, (-1*a.FKJE) as FKJE from JC_FKXX a, JC_JSZF b where a.ZYH = b.ZYH and a.JSCS = b.JSCS and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid and b.ZFGH = :czgh"
						+ " union all "
						+ "select a.FKFS as FKFS, a.FKJE as FKJE from JC_FKXX a, JC_JCJS b where b.JSLX<>4 and a.ZYH = b.ZYH and a.JSCS = b.JSCS and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid and b.CZGH = :czgh"
						+ " union all "
						+ "SELECT a.JKFS as FKFS,a.JKJE as FKJE FROM JC_TBKK a WHERE a.HZRQ>=:adt_hzrq_b AND a.HZRQ<=:adt_hzrq_e AND a.JGID=:al_jgid and a.CZGH = :czgh"
						+ " union all "
						+ "SELECT a.JKFS as FKFS,(-1*a.JKJE) as FKJE FROM JC_TBKK a ,JC_JKZF b WHERE b.JKXH = a.JKXH and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid and b.ZFGH = :czgh"
						+ ") d left outer join GY_FKFS e on d.FKFS = e.FKFS group by d.FKFS,e.FKMC";
				String ids_brxz_hql = "select sum(c.FYHJ) as FYHJ,sum(c.ZFHJ) as ZFHJ,c.BRXZ as BRXZ,d.XZMC as XZMC,d.DBPB as DBPB from ("
						+ "SELECT a.FYHJ as FYHJ,a.ZFHJ as ZFHJ,a.BRXZ as BRXZ FROM JC_JCJS a WHERE a.JSLX<>4 and a.FYHJ<>a.ZFHJ and a.HZRQ>=:adt_hzrq_b AND a.HZRQ<=:adt_hzrq_e AND a.JGID=:al_jgid and a.CZGH = :czgh"
						+ " union all "
						+ "SELECT (-1*a.FYHJ) as FYHJ,(-1*a.ZFHJ) as ZFHJ,a.BRXZ as BRXZ FROM JC_JCJS a ,JC_JSZF b WHERE a.JSLX<>4 and a.ZYH = b.ZYH AND a.JSCS = b.JSCS and a.FYHJ<>a.ZFHJ and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid and b.ZFGH = :czgh"
						+ ") c left outer join GY_BRXZ d on c.BRXZ = d.BRXZ group by c.BRXZ,d.XZMC,d.DBPB";
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("adt_hzrq_b", adt_hzrq_start);
				parameters.put("adt_hzrq_e", adt_hzrq_end);
				parameters.put("al_jgid", manaUnitId);
				parameters.put("czgh", resultList.get(i).get("CZGH") + "");
				List<Map<String, Object>> ids_brxz = dao.doSqlQuery(
						ids_brxz_hql, parameters);
				List<Map<String, Object>> ids_fkfs = dao.doSqlQuery(
						ids_fkfs_hql, parameters);
				String qtysFb = "";
				String jzjeSt = "0.00";
				if (ids_fkfs != null && ids_fkfs.size() != 0) {
					for (int n = 0; n < ids_fkfs.size(); n++) {
						qtysFb = qtysFb
								+ ids_fkfs.get(n).get("FKMC")
								+ ":"
								+ String.format("%1$.2f",
										ids_fkfs.get(n).get("FKJE")) + " ";
					}
				}
				if (ids_brxz != null && ids_brxz.size() != 0) {
					for (int n = 0; n < ids_brxz.size(); n++) {
						if (Integer.parseInt(ids_brxz.get(n).get("DBPB") + "") == 0) {
							jzjeSt = String
									.format("%1$.2f",
											parseDouble(jzjeSt)
													+ (parseDouble(ids_brxz
															.get(n).get("FYHJ")
															+ "") - parseDouble(ids_brxz
															.get(n).get("ZFHJ")
															+ "")));
						} else {
							qtysFb = qtysFb
									+ ids_brxz.get(n).get("XZMC")
									+ ":"
									+ String.format(
											"%1$.2f",
											(parseDouble(ids_brxz.get(n).get(
													"FYHJ")
													+ "") - parseDouble(ids_brxz
													.get(n).get("ZFHJ") + "")))
									+ " ";
						}
					}
					qtysFb = qtysFb + " " + "记账 :" + jzjeSt + " ";
				}
				resultList.get(i).put("QTYSFB", qtysFb);
				resultList.get(i).put("CYSR",
						String.format("%1$.2f", resultList.get(i).get("CYSR")));
				resultList.get(i).put("YJJE",
						String.format("%1$.2f", resultList.get(i).get("YJJE")));
				resultList.get(i).put("TPJE",
						String.format("%1$.2f", resultList.get(i).get("TPJE")));
				resultList.get(i).put("YSJE",
						String.format("%1$.2f", resultList.get(i).get("YSJE")));
				resultList.get(i).put("YSXJ",
						String.format("%1$.2f", resultList.get(i).get("YSXJ")));
				resultList.get(i).put("TYJJ",
						String.format("%1$.2f", resultList.get(i).get("TYJJ")));
				resultList.get(i).put("YSQT",
						String.format("%1$.2f", resultList.get(i).get("YSQT")));
				resultList.get(i).put("SRJE",
						String.format("%1$.2f", resultList.get(i).get("SRJE")));
				// if (resultList.get(i).get("SZYB") != null) {
				// resultList.get(i).put(
				// "SZYB",
				// String.format("%1$.2f",
				// resultList.get(i).get("SZYB")));
				// }
				// if (resultList.get(i).get("SYB") != null) {
				// resultList.get(i).put(
				// "SYB",
				// String.format("%1$.2f", resultList.get(i)
				// .get("SYB")));
				// }
				// if (resultList.get(i).get("YHYB") != null) {
				// resultList.get(i).put(
				// "YHYB",
				// String.format("%1$.2f",
				// resultList.get(i).get("YHYB")));
				// }
				// if (resultList.get(i).get("SMK") != null) {
				// resultList.get(i).put(
				// "SMK",
				// String.format("%1$.2f", resultList.get(i)
				// .get("SMK")));
				// }
				if (resultList.get(i).get("TCZC") != null) {
					resultList.get(i).put(
							"TCZC",
							String.format("%1$.2f",
									resultList.get(i).get("TCZC")));
				}
				if (resultList.get(i).get("DBZC") != null) {
					resultList.get(i).put(
							"DBZC",
							String.format("%1$.2f",
									resultList.get(i).get("DBZC")));
				}
				if (resultList.get(i).get("ZXJZFY") != null) {
					resultList.get(i).put(
							"ZXJZFY",
							String.format("%1$.2f",
									resultList.get(i).get("ZXJZFY")));
				}
				if (resultList.get(i).get("GRXJZF") != null) {
					resultList.get(i).put(
							"GRXJZF",
							String.format("%1$.2f",
									resultList.get(i).get("GRXJZF")));
				}
				if (resultList.get(i).get("BCZHZF") != null) {
					resultList.get(i).put(
							"BCZHZF",
							String.format("%1$.2f",
									resultList.get(i).get("BCZHZF")));
				}
				if (resultList.get(i).get("AZQGFY") != null) {
					resultList.get(i).put(
							"AZQGFY",
							String.format("%1$.2f",
									resultList.get(i).get("AZQGFY")));
				}
			}
			Map<String, Object> parametershj = dao
					.doLoad("SELECT sum(JC_JZXX.CYSR) as ZCYSR,sum(JC_JZXX.YJJE) as ZYJJE,sum(JC_JZXX.TPJE) as ZTPJE,sum(JC_JZXX.FPZS) as ZFPZS,sum(JC_JZXX.SJZS) as ZSJZS,sum(JC_JZXX.YSJE) as ZYSJE,sum(JC_JZXX.YSXJ) as ZYSXJ,sum(JC_JZXX.ZPZS) as ZZPZS,sum(JC_JZXX.TYJJ) as ZTYJJ,sum(JC_JZXX.YSQT) as ZYSQT,sum(JC_JZXX.QTZS) as ZQTZS,sum(JC_JZXX.SRJE) as ZSRJE,sum(JC_JZXX.TCZC) as TCZC,sum(JC_JZXX.DBZC) as DBZC,sum(JC_JZXX.ZXJZFY) as ZXJZFY,sum(JC_JZXX.GRXJZF) as GRXJZF,sum(JC_JZXX.BCZHZF) as BCZHZF,sum(JC_JZXX.AZQGFY) as AZQGFY FROM JC_JZXX JC_JZXX WHERE JC_JZXX.HZRQ>=:adt_hzrq_b AND JC_JZXX.HZRQ<=:adt_hzrq_e AND JC_JZXX.JGID=:al_jgid",
							parametershzbd);
			String ids_fkfs_hql = "select d.FKFS as FKFS,sum(d.FKJE) as FKJE,e.FKMC as FKMC from ("
					+ "select a.FKFS as FKFS, (-1*a.FKJE) as FKJE from JC_FKXX a, JC_JSZF b where a.ZYH = b.ZYH and a.JSCS = b.JSCS and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid"
					+ " union all "
					+ "select a.FKFS as FKFS, a.FKJE as FKJE from JC_FKXX a, JC_JCJS b where b.JSLX<>4 and a.ZYH = b.ZYH and a.JSCS = b.JSCS and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid"
					+ " union all "
					+ "SELECT a.JKFS as FKFS,a.JKJE as FKJE FROM JC_TBKK a WHERE a.HZRQ>=:adt_hzrq_b AND a.HZRQ<=:adt_hzrq_e AND a.JGID=:al_jgid"
					+ " union all "
					+ "SELECT a.JKFS as FKFS,(-1*a.JKJE) as FKJE FROM JC_TBKK a ,JC_JKZF b WHERE b.JKXH = a.JKXH and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid"
					+ ") d left outer join GY_FKFS e on d.FKFS = e.FKFS group by d.FKFS,e.FKMC";
			String ids_brxz_hql = "select sum(c.FYHJ) as FYHJ,sum(c.ZFHJ) as ZFHJ,c.BRXZ as BRXZ,d.XZMC as XZMC,d.DBPB as DBPB from ("
					+ "SELECT a.FYHJ as FYHJ,a.ZFHJ as ZFHJ,a.BRXZ as BRXZ FROM JC_JCJS a WHERE a.JSLX<>4 and a.FYHJ<>a.ZFHJ and a.HZRQ>=:adt_hzrq_b AND a.HZRQ<=:adt_hzrq_e AND a.JGID=:al_jgid"
					+ " union all "
					+ "SELECT (-1*a.FYHJ) as FYHJ,(-1*a.ZFHJ) as ZFHJ,a.BRXZ as BRXZ FROM JC_JCJS a ,JC_JSZF b WHERE a.JSLX<>4 and a.ZYH = b.ZYH AND a.JSCS = b.JSCS and a.FYHJ<>a.ZFHJ and b.HZRQ>=:adt_hzrq_b AND b.HZRQ<=:adt_hzrq_e AND b.JGID=:al_jgid"
					+ ") c left outer join GY_BRXZ d on c.BRXZ = d.BRXZ group by c.BRXZ,d.XZMC,d.DBPB";
			List<Map<String, Object>> ids_brxz = dao.doSqlQuery(ids_brxz_hql,
					parametershzbd);
			List<Map<String, Object>> ids_fkfs = dao.doSqlQuery(ids_fkfs_hql,
					parametershzbd);
			String qtysFb = "";
			String jzjeSt = "0.00";
			if (ids_fkfs != null && ids_fkfs.size() != 0) {
				for (int i = 0; i < ids_fkfs.size(); i++) {
					qtysFb = qtysFb
							+ ids_fkfs.get(i).get("FKMC")
							+ ":"
							+ String.format("%1$.2f",
									ids_fkfs.get(i).get("FKJE")) + " ";
				}
			}
			if (ids_brxz != null && ids_brxz.size() != 0) {
				for (int i = 0; i < ids_brxz.size(); i++) {
					if (Integer.parseInt(ids_brxz.get(i).get("DBPB") + "") == 0) {
						jzjeSt = String.format(
								"%1$.2f",
								parseDouble(jzjeSt)
										+ (parseDouble(ids_brxz.get(i).get(
												"FYHJ")
												+ "") - parseDouble(ids_brxz
												.get(i).get("ZFHJ") + "")));
					} else {
						qtysFb = qtysFb
								+ ids_brxz.get(i).get("XZMC")
								+ ":"
								+ String.format(
										"%1$.2f",
										(parseDouble(ids_brxz.get(i)
												.get("FYHJ") + "") - parseDouble(ids_brxz
												.get(i).get("ZFHJ") + "")))
								+ " ";
					}
				}
				qtysFb = qtysFb + " " + "记账 :" + jzjeSt + " ";
			}
			response.put("qtysFb", qtysFb);
			if (parametershj.get("ZCYSR") != null) {
				response.put("ZCYSR",
						String.format("%1$.2f", parametershj.get("ZCYSR")));
			} else {
				response.put("ZCYSR", "");
			}
			if (parametershj.get("ZYJJE") != null) {
				response.put("ZYJJE",
						String.format("%1$.2f", parametershj.get("ZYJJE")));
			} else {
				response.put("ZYJJE", "");
			}
			if (parametershj.get("ZTPJE") != null) {
				response.put("ZTPJE",
						String.format("%1$.2f", parametershj.get("ZTPJE")));
			} else {
				response.put("ZTPJE", "");
			}
			if (parametershj.get("ZFPZS") != null) {
				response.put("ZFPZS", parametershj.get("ZFPZS") + "");
			} else {
				response.put("ZFPZS", "");
			}
			if (parametershj.get("ZSJZS") != null) {
				response.put("ZSJZS", parametershj.get("ZSJZS") + "");
			} else {
				response.put("ZSJZS", "");
			}
			if (parametershj.get("ZTYJJ") != null) {
				response.put("ZTYJJ",
						String.format("%1$.2f", parametershj.get("ZTYJJ")));
			} else {
				response.put("ZTYJJ", "");
			}
			if (parametershj.get("ZYSJE") != null) {
				response.put("ZYSJE",
						String.format("%1$.2f", parametershj.get("ZYSJE")));
			} else {
				response.put("ZYSJE", "");
			}
			if (parametershj.get("ZYSXJ") != null) {
				response.put("ZYSXJ",
						String.format("%1$.2f", parametershj.get("ZYSXJ")));
			} else {
				response.put("ZYSXJ", "");

			}
			if (parametershj.get("ZYSQT") != null) {
				response.put("ZYSQT",
						String.format("%1$.2f", parametershj.get("ZYSQT")));
			} else {
				response.put("ZYSQT", "");
			}
			if (parametershj.get("ZQTZS") != null) {
				response.put("ZQTZS", parametershj.get("ZQTZS") + "");
			} else {
				response.put("ZQTZS", "");
			}
			if (parametershj.get("ZSRJE") != null) {
				response.put("ZSRJE",
						String.format("%1$.2f", parametershj.get("ZSRJE")));
			} else {
				response.put("ZSRJE", "");
			}
			if (parametershj.get("TCZC") != null) {
				response.put("TCZCHJ",
						String.format("%1$.2f", parametershj.get("TCZC")));
			} else {
				response.put("TCZCHJ", "");
			}
			if (parametershj.get("DBZC") != null) {
				response.put("DBZCHJ",
						String.format("%1$.2f", parametershj.get("DBZC")));
			} else {
				response.put("DBZCHJ", "");
			}
			if (parametershj.get("ZXJZFY") != null) {
				response.put("ZXJZFYHJ",
						String.format("%1$.2f", parametershj.get("ZXJZFY")));
			} else {
				response.put("ZXJZFYHJ", "");
			}
			if (parametershj.get("GRXJZF") != null) {
				response.put("GRXJZFHJ",
						String.format("%1$.2f", parametershj.get("GRXJZF")));
			} else {
				response.put("GRXJZFHJ", "");
			}
			if (parametershj.get("BCZHZF") != null) {
				response.put("BCZHZFHJ",
						String.format("%1$.2f", parametershj.get("BCZHZF")));
			} else {
				response.put("BCZHZFHJ", "");
			}
			if (parametershj.get("AZQGFY") != null) {
				response.put("AZQGFYHJ",
						String.format("%1$.2f", parametershj.get("AZQGFY")));
			} else {
				response.put("AZQGFYHJ", "");
			}
			// if (parametershj.get("SZYB") != null) {
			// response.put("SZYBHJ",
			// String.format("%1$.2f", parametershj.get("SZYB")));
			// }
			// if (parametershj.get("SYB") != null) {
			// response.put("SYBHJ",
			// String.format("%1$.2f", parametershj.get("SYB")));
			// }
			// if (parametershj.get("YHYB") != null) {
			// response.put("YHYBHJ",
			// String.format("%1$.2f", parametershj.get("YHYB")));
			// }
			// if (parametershj.get("SMK") != null) {
			// response.put("SMKHJ",
			// String.format("%1$.2f", parametershj.get("SMK")));
			// }
			List<Map<String, Object>> zfzslist = new ArrayList<Map<String, Object>>();
			StringBuffer sbfp = new StringBuffer();
			StringBuffer sbsj = new StringBuffer();
			zfzslist = dao
					.doQuery(
							"SELECT JC_ZFPJ.PJLB as PJLB,JC_ZFPJ.PJHM as PJHM FROM JC_ZFPJ JC_ZFPJ,JC_JZXX JC_JZXX WHERE ( JC_ZFPJ.JZRQ = JC_JZXX.JZRQ ) AND ( JC_ZFPJ.CZGH = JC_JZXX.CZGH ) AND JC_JZXX.HZRQ>=:adt_hzrq_b AND JC_JZXX.HZRQ<=:adt_hzrq_e AND JC_JZXX.JGID=:al_jgid ORDER BY JC_ZFPJ.PJLB,JC_ZFPJ.PJHM",
							parametershzbd);
			for (int i = 0; i < zfzslist.size(); i++) {
				if (Integer.parseInt(zfzslist.get(i).get("PJLB") + "") == 1) {
					if (zfzslist.get(i).get("PJHM") != null) {
						sbfp.append(zfzslist.get(i).get("PJHM") + " ");
					}
				} else {
					if (zfzslist.get(i).get("PJHM") != null) {
						sbsj.append(zfzslist.get(i).get("PJHM") + " ");
					}
				}
			}
			if (sbfp.toString() != null && sbfp.length() > 0) {
				response.put(
						"ZFFPHM",
						sbfp.toString().substring(0,
								sbfp.toString().length() - 1));
			} else {
				response.put("ZFFPHM", "");
			}
			if (sbsj.toString() != null && sbsj.length() > 0) {
				response.put(
						"ZFSJHM",
						sbsj.toString().substring(0,
								sbsj.toString().length() - 1));
			} else {
				response.put("ZFSJHM", "");
			}
			List<Map<String, Object>> zfpjlist = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> parametersczgh = new HashMap<String, Object>();
				parametersczgh.put("adt_hzrq_b", adt_hzrq_start);
				parametersczgh.put("adt_hzrq_e", adt_hzrq_end);
				parametersczgh.put("as_czgh", resultList.get(i).get("CZGH"));
				parametersczgh.put("gl_jgid", manaUnitId);
				StringBuffer ls_jsfp_all = new StringBuffer();
				StringBuffer ls_jssj_all = new StringBuffer();
				zfpjlist = dao
						.doQuery(
								"SELECT QZPJ as QZPJ,QZSJ as QZSJ From JC_JZXX Where CZGH=:as_czgh And HZRQ>=:adt_hzrq_b And HZRQ<=:adt_hzrq_e And JGID=:gl_jgid",
								parametersczgh);
				for (int j = 0; j < zfpjlist.size(); j++) {
					if (zfpjlist.get(j).get("QZPJ") != null) {
						ls_jsfp_all.append(zfpjlist.get(j).get("QZPJ") + "");
					}
					if (zfpjlist.get(j).get("QZSJ") != null) {
						ls_jssj_all.append(zfpjlist.get(j).get("QZSJ") + "");
					}
				}
				if (ls_jsfp_all.toString() != null && ls_jsfp_all.length() > 0) {
					resultList.get(i).put("QZPJ", ls_jsfp_all.toString());
				} else {
					resultList.get(i).put("QZPJ", "");
				}
				if (ls_jssj_all.toString() != null && ls_jssj_all.length() > 0) {
					resultList.get(i).put("QZSJ", ls_jssj_all.toString());
				} else {
					resultList.get(i).put("QZSJ", "");
				}
			}
			resultLi.addAll(resultList);
		} catch (PersistentDataOperationException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param parameters里参数分别是ldt_begin
	 *            (开始时间)、ldt_end(结束时间)、gl_jgid(机构id)、ii_tjfs(提交方式)
	 * @param dao
	 * @param ctx
	 * @throws ModelDataOperationException
	 */
	public static List<Map<String, Object>> wf_tj_mzhs(
			Map<String, Object> parameters, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		List<Map<String, Object>> listMS_MZHS = new ArrayList<Map<String, Object>>();
		try {
			SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfdatetime = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			int ii_tjfs = Integer.parseInt(parameters.get("ii_tjfs") + "");
			parameters.remove("ii_tjfs");
			String sql = "";
			if (ii_tjfs == 1) {
				sql = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_CF01.CFLX as CFLX,count(MS_CF01.CFSB) AS CFZS,SUM(MS_CF02.HJJE) AS HJJE FROM MS_CF01 MS_CF01, MS_MZXX MS_MZXX, MS_CF02 MS_CF02 WHERE (MS_CF02.CFSB = MS_CF01.CFSB) AND (MS_MZXX.MZXH = MS_CF01.MZXH) AND (MS_MZXX.SFRQ >= :ldt_begin) AND (MS_MZXX.SFRQ <= :ldt_end) AND (MS_MZXX.JGID = :gl_jgid) GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF01.CFLX ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF01.CFLX";
			} else if (ii_tjfs == 2) {
				sql = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_CF01.CFLX as CFLX,count(MS_CF01.CFSB) AS CFZS,SUM(MS_CF02.HJJE) AS HJJE FROM MS_CF01 MS_CF01, MS_MZXX MS_MZXX, MS_CF02 MS_CF02 WHERE (MS_CF02.CFSB = MS_CF01.CFSB) AND (MS_MZXX.MZXH = MS_CF01.MZXH) AND (MS_MZXX.JZRQ >= :ldt_begin) AND (MS_MZXX.JZRQ <= :ldt_end) AND (MS_MZXX.JGID = :gl_jgid) GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF01.CFLX ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF01.CFLX";
			} else if (ii_tjfs == 3) {
				sql = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_CF01.CFLX as CFLX,count(MS_CF01.CFSB) AS CFZS,SUM(MS_CF02.HJJE) AS HJJE FROM MS_CF01 MS_CF01, MS_MZXX MS_MZXX, MS_CF02 MS_CF02 WHERE (MS_CF02.CFSB = MS_CF01.CFSB) AND (MS_MZXX.MZXH = MS_CF01.MZXH) AND (MS_MZXX.HZRQ >= :ldt_begin) AND (MS_MZXX.HZRQ <= :ldt_end) AND (MS_MZXX.JGID = :gl_jgid) GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF01.CFLX ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF01.CFLX";
			}
			// 处方张数临时表单
			List<Map<String, Object>> cf01andmzxxList = dao.doQuery(sql,
					parameters);
			for (int i = 0; i < cf01andmzxxList.size(); i++) {
				int sign = 0;
				Long li_ksdm = 0L;
				if (cf01andmzxxList.get(i).get("KSDM") != null) {
					li_ksdm = Long.parseLong(cf01andmzxxList.get(i).get("KSDM")
							+ "");
				}
				String ls_ysdm = null;
				if (cf01andmzxxList.get(i).get("YSDM") != null) {
					ls_ysdm = cf01andmzxxList.get(i).get("YSDM") + "";
				}
				int li_cflx = 0;
				if (cf01andmzxxList.get(i).get("CFLX") != null) {
					li_cflx = Integer.parseInt(cf01andmzxxList.get(i).get(
							"CFLX")
							+ "");
				}

				int li_cfzs = 0;
				if (cf01andmzxxList.get(i).get("CFZS") != null) {
					li_cfzs = Integer.parseInt(cf01andmzxxList.get(i).get(
							"CFZS")
							+ "");
				}
				double ld_hjje = 0.00;
				if (cf01andmzxxList.get(i).get("HJJE") != null) {
					ld_hjje = Double.parseDouble(String.format("%1$.2f",
							cf01andmzxxList.get(i).get("HJJE")));
				}
				Date ldt_sfrq = null;
				if (ii_tjfs == 1) {
					if (cf01andmzxxList.get(i).get("SFRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(cf01andmzxxList.get(i)
								.get("SFRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 2) {
					if (cf01andmzxxList.get(i).get("JZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(cf01andmzxxList.get(i)
								.get("JZRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 3) {
					if (cf01andmzxxList.get(i).get("HZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(cf01andmzxxList.get(i)
								.get("HZRQ") + "");
					} else {
						continue;
					}
				}
				ldt_sfrq = sdfdatetime.parse(sdfdate.format(ldt_sfrq)
						+ " 00:00:00");
				if (listMS_MZHS.size() <= 0) {
					Map<String, Object> mapMS_MZHS = new HashMap<String, Object>();
					mapMS_MZHS.put("KSDM", li_ksdm);
					mapMS_MZHS.put("YSDM", ls_ysdm);
					mapMS_MZHS.put("GZRQ", ldt_sfrq);
					mapMS_MZHS.put("TJFS", ii_tjfs);
					mapMS_MZHS.put("HJJE", ld_hjje);
					mapMS_MZHS.put("JCD", 0);
					if (li_cflx == 1) {
						mapMS_MZHS.put("XYF", li_cfzs);
						mapMS_MZHS.put("ZYF", 0);
						mapMS_MZHS.put("CYF", 0);
					} else if (li_cflx == 2) {
						mapMS_MZHS.put("XYF", 0);
						mapMS_MZHS.put("ZYF", li_cfzs);
						mapMS_MZHS.put("CYF", 0);

					} else if (li_cflx == 3) {
						mapMS_MZHS.put("XYF", 0);
						mapMS_MZHS.put("ZYF", 0);
						mapMS_MZHS.put("CYF", li_cfzs);
					}
					mapMS_MZHS.put("MZRC", 0);
					listMS_MZHS.add(mapMS_MZHS);
				} else {
					for (int j = 0; j < listMS_MZHS.size(); j++) {
						if (Long.parseLong(listMS_MZHS.get(j).get("KSDM") + "") == li_ksdm
								&& listMS_MZHS.get(j).get("YSDM").toString()
										.equals(ls_ysdm)
								&& sdfdatetime.parse(
										sdfdatetime.format(listMS_MZHS.get(j)
												.get("GZRQ"))).getTime() == ldt_sfrq
										.getTime()
								&& Integer.parseInt(listMS_MZHS.get(j).get(
										"TJFS")
										+ "") == ii_tjfs) {
							if (li_cflx == 1) {
								int li_zs = 0;
								if (listMS_MZHS.get(j).get("XYF") != null) {
									li_zs = Integer.parseInt(listMS_MZHS.get(j)
											.get("XYF") + "");
									listMS_MZHS.get(j).put("XYF",
											li_zs + li_cfzs);
								}

							} else if (li_cflx == 2) {
								int li_zs = 0;
								if (listMS_MZHS.get(j).get("ZYF") != null) {
									li_zs = Integer.parseInt(listMS_MZHS.get(j)
											.get("ZYF") + "");
									listMS_MZHS.get(j).put("ZYF",
											li_zs + li_cfzs);
								}
							} else if (li_cflx == 3) {
								int li_zs = 0;
								if (listMS_MZHS.get(j).get("CYF") != null) {
									li_zs = Integer.parseInt(listMS_MZHS.get(j)
											.get("CYF") + "");
									listMS_MZHS.get(j).put("CYF",
											li_zs + li_cfzs);
								}
							}
							listMS_MZHS.get(j).put(
									"HJJE",
									Double.parseDouble(String.format(
											"%1$.2f",
											Double.parseDouble(listMS_MZHS.get(
													j).get("HJJE")
													+ "")
													+ ld_hjje)));
							sign = 1;
						}
					}
					if (sign == 0) {
						Map<String, Object> mapMS_MZHS = new HashMap<String, Object>();
						mapMS_MZHS.put("KSDM", li_ksdm);
						mapMS_MZHS.put("YSDM", ls_ysdm);
						mapMS_MZHS.put("GZRQ", ldt_sfrq);
						mapMS_MZHS.put("TJFS", ii_tjfs);
						mapMS_MZHS.put("HJJE", ld_hjje);
						mapMS_MZHS.put("JCD", 0);
						if (li_cflx == 1) {
							mapMS_MZHS.put("XYF", li_cfzs);
							mapMS_MZHS.put("ZYF", 0);
							mapMS_MZHS.put("CYF", 0);
						} else if (li_cflx == 2) {
							mapMS_MZHS.put("XYF", 0);
							mapMS_MZHS.put("ZYF", li_cfzs);
							mapMS_MZHS.put("CYF", 0);

						} else if (li_cflx == 3) {
							mapMS_MZHS.put("XYF", 0);
							mapMS_MZHS.put("ZYF", 0);
							mapMS_MZHS.put("CYF", li_cfzs);
						}
						mapMS_MZHS.put("MZRC", 0);
						listMS_MZHS.add(mapMS_MZHS);
					}
				}
			}
			// 处方作废张数临时表单
			String sql2 = "";
			if (ii_tjfs == 1) {
				sql2 = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_CF01.CFLX as CFLX,count(MS_CF01.CFSB) AS CFZS,SUM(MS_CF02.HJJE) AS HJJE FROM MS_CF01 MS_CF01,MS_ZFFP MS_ZFFP,MS_MZXX MS_MZXX,MS_CF02 MS_CF02 WHERE (MS_CF02.CFSB = MS_CF01.CFSB) AND (MS_ZFFP.MZXH = MS_CF01.MZXH) AND (MS_ZFFP.MZXH = MS_MZXX.MZXH) AND MS_ZFFP.ZFRQ >= :ldt_begin And MS_ZFFP.ZFRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF01.CFLX ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF01.CFLX";
			} else if (ii_tjfs == 2) {
				sql2 = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_CF01.CFLX as CFLX,count(MS_CF01.CFSB) AS CFZS,SUM(MS_CF02.HJJE) AS HJJE FROM MS_CF01 MS_CF01,MS_ZFFP MS_ZFFP,MS_MZXX MS_MZXX,MS_CF02 MS_CF02 WHERE (MS_CF02.CFSB = MS_CF01.CFSB) AND (MS_ZFFP.MZXH = MS_CF01.MZXH) AND (MS_ZFFP.MZXH = MS_MZXX.MZXH) AND MS_ZFFP.JZRQ >= :ldt_begin And MS_ZFFP.JZRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF01.CFLX ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF01.CFLX";
			} else if (ii_tjfs == 3) {
				sql2 = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_CF01.CFLX as CFLX,count(MS_CF01.CFSB) AS CFZS,SUM(MS_CF02.HJJE) AS HJJE FROM MS_CF01 MS_CF01,MS_ZFFP MS_ZFFP,MS_MZXX MS_MZXX,MS_CF02 MS_CF02 WHERE (MS_CF02.CFSB = MS_CF01.CFSB) AND (MS_ZFFP.MZXH = MS_CF01.MZXH) AND (MS_ZFFP.MZXH = MS_MZXX.MZXH) AND MS_ZFFP.HZRQ >= :ldt_begin And MS_ZFFP.HZRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF01.CFLX ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF01.CFLX";
			}
			List<Map<String, Object>> cf01andmzxxzfList = dao.doQuery(sql2,
					parameters);
			for (int i = 0; i < cf01andmzxxzfList.size(); i++) {
				int signzf = 0;
				Long li_ksdm = 0L;
				if (cf01andmzxxzfList.get(i).get("KSDM") != null) {
					li_ksdm = Long.parseLong(cf01andmzxxzfList.get(i).get(
							"KSDM")
							+ "");
				}
				String ls_ysdm = null;
				if (cf01andmzxxzfList.get(i).get("YSDM") != null) {
					ls_ysdm = cf01andmzxxzfList.get(i).get("YSDM") + "";
				}
				int li_cflx = 0;
				if (cf01andmzxxzfList.get(i).get("CFLX") != null) {
					li_cflx = Integer.parseInt(cf01andmzxxzfList.get(i).get(
							"CFLX")
							+ "");
				}

				int li_cfzs = 0;
				if (cf01andmzxxzfList.get(i).get("CFZS") != null) {
					li_cfzs = Integer.parseInt(cf01andmzxxzfList.get(i).get(
							"CFZS")
							+ "");
				}
				double ld_hjje = 0.00;
				if (cf01andmzxxzfList.get(i).get("HJJE") != null) {
					ld_hjje = Double.parseDouble(String.format("%1$.2f",
							cf01andmzxxzfList.get(i).get("HJJE")));
				}
				Date ldt_sfrq = null;
				if (ii_tjfs == 1) {
					if (cf01andmzxxzfList.get(i).get("SFRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(cf01andmzxxzfList.get(i)
								.get("SFRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 2) {
					if (cf01andmzxxzfList.get(i).get("JZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(cf01andmzxxzfList.get(i)
								.get("JZRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 3) {
					if (cf01andmzxxzfList.get(i).get("HZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(cf01andmzxxzfList.get(i)
								.get("HZRQ") + "");
					} else {
						continue;
					}
				}
				ldt_sfrq = sdfdatetime.parse(sdfdate.format(ldt_sfrq)
						+ " 00:00:00");
				if (listMS_MZHS.size() <= 0) {
					Map<String, Object> mapzfMS_MZHS = new HashMap<String, Object>();
					mapzfMS_MZHS.put("KSDM", li_ksdm);
					mapzfMS_MZHS.put("YSDM", ls_ysdm);
					mapzfMS_MZHS.put("GZRQ", ldt_sfrq);
					mapzfMS_MZHS.put("TJFS", ii_tjfs);
					mapzfMS_MZHS.put("HJJE", ld_hjje);
					mapzfMS_MZHS.put("JCD", 0);
					if (li_cflx == 1) {
						mapzfMS_MZHS.put("XYF", 0 - li_cfzs);
						mapzfMS_MZHS.put("ZYF", 0);
						mapzfMS_MZHS.put("CYF", 0);
					} else if (li_cflx == 2) {
						mapzfMS_MZHS.put("XYF", 0);
						mapzfMS_MZHS.put("ZYF", 0 - li_cfzs);
						mapzfMS_MZHS.put("CYF", 0);
					} else if (li_cflx == 3) {
						mapzfMS_MZHS.put("XYF", 0);
						mapzfMS_MZHS.put("ZYF", 0);
						mapzfMS_MZHS.put("CYF", 0 - li_cfzs);
					}
					mapzfMS_MZHS.put("MZRC", 0);
					listMS_MZHS.add(mapzfMS_MZHS);
				} else {
					for (int j = 0; j < listMS_MZHS.size(); j++) {
						if (Long.parseLong(listMS_MZHS.get(j).get("KSDM") + "") == li_ksdm
								&& listMS_MZHS.get(j).get("YSDM").toString()
										.equals(ls_ysdm)
								&& sdfdatetime.parse(
										sdfdatetime.format(listMS_MZHS.get(j)
												.get("GZRQ"))).getTime() == ldt_sfrq
										.getTime()
								&& Integer.parseInt(listMS_MZHS.get(j).get(
										"TJFS")
										+ "") == ii_tjfs) {
							if (li_cflx == 1) {
								int li_zs = 0;
								if (listMS_MZHS.get(j).get("XYF") != null) {
									li_zs = Integer.parseInt(listMS_MZHS.get(j)
											.get("XYF") + "");
									listMS_MZHS.get(j).put("XYF",
											li_zs - li_cfzs);
								}

							} else if (li_cflx == 2) {
								int li_zs = 0;
								if (listMS_MZHS.get(j).get("ZYF") != null) {
									li_zs = Integer.parseInt(listMS_MZHS.get(j)
											.get("ZYF") + "");
									listMS_MZHS.get(j).put("ZYF",
											li_zs - li_cfzs);
								}
							} else if (li_cflx == 3) {
								int li_zs = 0;
								if (listMS_MZHS.get(j).get("CYF") != null) {
									li_zs = Integer.parseInt(listMS_MZHS.get(j)
											.get("CYF") + "");
									listMS_MZHS.get(j).put("CYF",
											li_zs - li_cfzs);
								}
							}
							listMS_MZHS.get(j).put(
									"HJJE",
									Double.parseDouble(String.format(
											"%1$.2f",
											Double.parseDouble(listMS_MZHS.get(
													j).get("HJJE")
													+ "")
													- ld_hjje)));
							signzf = 1;
						}
					}
					if (signzf == 0) {
						Map<String, Object> mapzfMS_MZHS = new HashMap<String, Object>();
						mapzfMS_MZHS.put("KSDM", li_ksdm);
						mapzfMS_MZHS.put("YSDM", ls_ysdm);
						mapzfMS_MZHS.put("GZRQ", ldt_sfrq);
						mapzfMS_MZHS.put("TJFS", ii_tjfs);
						mapzfMS_MZHS.put("HJJE", ld_hjje);
						mapzfMS_MZHS.put("JCD", 0);
						if (li_cflx == 1) {
							mapzfMS_MZHS.put("XYF", 0 - li_cfzs);
							mapzfMS_MZHS.put("ZYF", 0);
							mapzfMS_MZHS.put("CYF", 0);
						} else if (li_cflx == 2) {
							mapzfMS_MZHS.put("XYF", 0);
							mapzfMS_MZHS.put("ZYF", 0 - li_cfzs);
							mapzfMS_MZHS.put("CYF", 0);
						} else if (li_cflx == 3) {
							mapzfMS_MZHS.put("XYF", 0);
							mapzfMS_MZHS.put("ZYF", 0);
							mapzfMS_MZHS.put("CYF", 0 - li_cfzs);
						}
						mapzfMS_MZHS.put("MZRC", 0);
						listMS_MZHS.add(mapzfMS_MZHS);
					}
				}
			}
			String sql_yj = "";
			if (ii_tjfs == 1) {
				sql_yj = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,count(MS_YJ01.YJXH) AS JCZS,sum(MS_YJ02.HJJE) AS SFJE FROM MS_MZXX MS_MZXX,MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02 WHERE (MS_YJ01.MZXH = MS_MZXX.MZXH) and (MS_YJ02.YJXH = MS_YJ01.YJXH) AND MS_MZXX.SFRQ >= :ldt_begin And MS_MZXX.SFRQ <= :ldt_end and MS_MZXX.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ";
			} else if (ii_tjfs == 2) {
				sql_yj = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,count(MS_YJ01.YJXH) AS JCZS,sum(MS_YJ02.HJJE) AS SFJE FROM MS_MZXX MS_MZXX,MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02 WHERE (MS_YJ01.MZXH = MS_MZXX.MZXH) and (MS_YJ02.YJXH = MS_YJ01.YJXH) AND MS_MZXX.JZRQ >= :ldt_begin And MS_MZXX.JZRQ <= :ldt_end and MS_MZXX.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ";
			} else if (ii_tjfs == 3) {
				sql_yj = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,count(MS_YJ01.YJXH) AS JCZS,sum(MS_YJ02.HJJE) AS SFJE FROM MS_MZXX MS_MZXX,MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02 WHERE (MS_YJ01.MZXH = MS_MZXX.MZXH) and (MS_YJ02.YJXH = MS_YJ01.YJXH) AND MS_MZXX.HZRQ >= :ldt_begin And MS_MZXX.HZRQ <= :ldt_end and MS_MZXX.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ";
			}
			// 检查张数临时表单
			List<Map<String, Object>> yj01andmzxxList = dao.doQuery(sql_yj,
					parameters);
			for (int i = 0; i < yj01andmzxxList.size(); i++) {
				int sign = 0;
				Long li_ksdm = 0L;
				if (yj01andmzxxList.get(i).get("KSDM") != null) {
					li_ksdm = Long.parseLong(yj01andmzxxList.get(i).get("KSDM")
							+ "");
				}
				String ls_ysdm = null;
				if (yj01andmzxxList.get(i).get("YSDM") != null) {
					ls_ysdm = yj01andmzxxList.get(i).get("YSDM") + "";
				}

				int li_jczs = 0;
				if (yj01andmzxxList.get(i).get("JCZS") != null) {
					li_jczs = Integer.parseInt(yj01andmzxxList.get(i).get(
							"JCZS")
							+ "");
				}
				double ld_hjje = 0.00;
				if (yj01andmzxxList.get(i).get("SFJE") != null) {
					ld_hjje = Double.parseDouble(String.format("%1$.2f",
							yj01andmzxxList.get(i).get("SFJE")));
				}
				Date ldt_sfrq = null;
				if (ii_tjfs == 1) {
					if (yj01andmzxxList.get(i).get("SFRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(yj01andmzxxList.get(i)
								.get("SFRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 2) {
					if (yj01andmzxxList.get(i).get("JZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(yj01andmzxxList.get(i)
								.get("JZRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 3) {
					if (yj01andmzxxList.get(i).get("HZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(yj01andmzxxList.get(i)
								.get("HZRQ") + "");
					} else {
						continue;
					}
				}
				ldt_sfrq = sdfdatetime.parse(sdfdate.format(ldt_sfrq)
						+ " 00:00:00");
				if (listMS_MZHS.size() <= 0) {
					Map<String, Object> mapMS_MZHS = new HashMap<String, Object>();
					mapMS_MZHS.put("KSDM", li_ksdm);
					mapMS_MZHS.put("YSDM", ls_ysdm);
					mapMS_MZHS.put("GZRQ", ldt_sfrq);
					mapMS_MZHS.put("TJFS", ii_tjfs);
					mapMS_MZHS.put("HJJE", ld_hjje);
					mapMS_MZHS.put("JCD", li_jczs);
					mapMS_MZHS.put("XYF", 0);
					mapMS_MZHS.put("ZYF", 0);
					mapMS_MZHS.put("CYF", 0);
					mapMS_MZHS.put("MZRC", 0);
					listMS_MZHS.add(mapMS_MZHS);
				} else {
					for (int j = 0; j < listMS_MZHS.size(); j++) {
						if (listMS_MZHS.get(j).get("YSDM") != null) {
							if (Long.parseLong(listMS_MZHS.get(j).get("KSDM")
									+ "") == li_ksdm
									&& listMS_MZHS.get(j).get("YSDM")
											.toString().equals(ls_ysdm)
									&& sdfdatetime.parse(
											sdfdatetime.format(listMS_MZHS.get(
													j).get("GZRQ"))).getTime() == ldt_sfrq
											.getTime()
									&& Integer.parseInt(listMS_MZHS.get(j).get(
											"TJFS")
											+ "") == ii_tjfs) {

								listMS_MZHS.get(j).put(
										"JCD",
										Integer.parseInt(listMS_MZHS.get(j)
												.get("JCD") + "")
												+ li_jczs);

								listMS_MZHS.get(j).put(
										"HJJE",
										Double.parseDouble(String.format(
												"%1$.2f",
												Double.parseDouble(listMS_MZHS
														.get(j).get("HJJE")
														+ "") + ld_hjje)));
								sign = 1;
							}
						}
					}
					if (sign == 0) {
						Map<String, Object> mapMS_MZHS = new HashMap<String, Object>();
						mapMS_MZHS.put("KSDM", li_ksdm);
						mapMS_MZHS.put("YSDM", ls_ysdm);
						mapMS_MZHS.put("GZRQ", ldt_sfrq);
						mapMS_MZHS.put("TJFS", ii_tjfs);
						mapMS_MZHS.put("HJJE", ld_hjje);
						mapMS_MZHS.put("JCD", li_jczs);
						mapMS_MZHS.put("XYF", 0);
						mapMS_MZHS.put("ZYF", 0);
						mapMS_MZHS.put("CYF", 0);
						mapMS_MZHS.put("MZRC", 0);
						listMS_MZHS.add(mapMS_MZHS);
					}
				}
			}

			String sql2_yj = "";
			if (ii_tjfs == 1) {
				sql2_yj = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as ZFRQ,count(MS_YJ01.YJXH) AS JCZS,sum(MS_YJ02.HJJE) AS SFJE FROM MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02,MS_ZFFP MS_ZFFP WHERE (MS_YJ02.YJXH = MS_YJ01.YJXH) and (MS_ZFFP.MZXH = MS_YJ01.MZXH) AND MS_ZFFP.ZFRQ >= :ldt_begin And MS_ZFFP.ZFRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_YJ02.FYGB ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ";
			} else if (ii_tjfs == 2) {
				sql2_yj = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as ZFRQ,count(MS_YJ01.YJXH) AS JCZS,sum(MS_YJ02.HJJE) AS SFJE FROM MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02,MS_ZFFP MS_ZFFP WHERE (MS_YJ02.YJXH = MS_YJ01.YJXH) and (MS_ZFFP.MZXH = MS_YJ01.MZXH) AND MS_ZFFP.JZRQ >= :ldt_begin And MS_ZFFP.JZRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_YJ02.FYGB ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ";
			} else if (ii_tjfs == 3) {
				sql2_yj = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as ZFRQ,count(MS_YJ01.YJXH) AS JCZS,sum(MS_YJ02.HJJE) AS SFJE FROM MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02,MS_ZFFP MS_ZFFP WHERE (MS_YJ02.YJXH = MS_YJ01.YJXH) and (MS_ZFFP.MZXH = MS_YJ01.MZXH) AND MS_ZFFP.HZRQ >= :ldt_begin And MS_ZFFP.HZRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_YJ02.FYGB ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ";
			}
			// 检查作废张数临时表单
			List<Map<String, Object>> yj01andmzxxzfList = dao.doQuery(sql2_yj,
					parameters);
			for (int i = 0; i < yj01andmzxxzfList.size(); i++) {
				int sign = 0;
				Long li_ksdm = 0L;
				if (yj01andmzxxzfList.get(i).get("KSDM") != null) {
					li_ksdm = Long.parseLong(yj01andmzxxzfList.get(i).get(
							"KSDM")
							+ "");
				}
				String ls_ysdm = null;
				if (yj01andmzxxzfList.get(i).get("YSDM") != null) {
					ls_ysdm = yj01andmzxxzfList.get(i).get("YSDM") + "";
				}

				int li_jczs = 0;
				if (yj01andmzxxzfList.get(i).get("JCZS") != null) {
					li_jczs = Integer.parseInt(yj01andmzxxzfList.get(i).get(
							"JCZS")
							+ "");
				}
				double ld_hjje = 0.00;
				if (yj01andmzxxzfList.get(i).get("SFJE") != null) {
					ld_hjje = Double.parseDouble(String.format("%1$.2f",
							yj01andmzxxzfList.get(i).get("SFJE")));
				}
				Date ldt_sfrq = null;
				if (ii_tjfs == 1) {
					if (yj01andmzxxzfList.get(i).get("SFRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(yj01andmzxxzfList.get(i)
								.get("SFRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 2) {
					if (yj01andmzxxzfList.get(i).get("JZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(yj01andmzxxzfList.get(i)
								.get("JZRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 3) {
					if (yj01andmzxxzfList.get(i).get("HZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(yj01andmzxxzfList.get(i)
								.get("HZRQ") + "");
					} else {
						continue;
					}
				}
				ldt_sfrq = sdfdatetime.parse(sdfdate.format(ldt_sfrq)
						+ " 00:00:00");
				if (listMS_MZHS.size() <= 0) {
					Map<String, Object> mapMS_MZHS = new HashMap<String, Object>();
					mapMS_MZHS.put("KSDM", li_ksdm);
					mapMS_MZHS.put("YSDM", ls_ysdm);
					mapMS_MZHS.put("GZRQ", ldt_sfrq);
					mapMS_MZHS.put("TJFS", ii_tjfs);
					mapMS_MZHS.put("HJJE", ld_hjje);
					mapMS_MZHS.put("JCD", li_jczs);
					mapMS_MZHS.put("XYF", 0);
					mapMS_MZHS.put("ZYF", 0);
					mapMS_MZHS.put("CYF", 0);
					mapMS_MZHS.put("MZRC", 0);
					listMS_MZHS.add(mapMS_MZHS);
				} else {
					for (int j = 0; j < listMS_MZHS.size(); j++) {
						if (Long.parseLong(listMS_MZHS.get(j).get("KSDM") + "") == li_ksdm
								&& listMS_MZHS.get(j).get("YSDM").toString()
										.equals(ls_ysdm)
								&& sdfdatetime.parse(
										sdfdatetime.format(listMS_MZHS.get(j)
												.get("GZRQ"))).getTime() == ldt_sfrq
										.getTime()
								&& Integer.parseInt(listMS_MZHS.get(j).get(
										"TJFS")
										+ "") == ii_tjfs) {
							listMS_MZHS.get(j).put(
									"JCD",
									Integer.parseInt(listMS_MZHS.get(j).get(
											"JCD")
											+ "")
											- li_jczs);
							listMS_MZHS.get(j).put(
									"HJJE",
									Double.parseDouble(String.format(
											"%1$.2f",
											Double.parseDouble(listMS_MZHS.get(
													j).get("HJJE")
													+ "")
													- ld_hjje)));
							sign = 1;
						}
					}
					if (sign == 0) {
						Map<String, Object> mapMS_MZHS = new HashMap<String, Object>();
						mapMS_MZHS.put("KSDM", li_ksdm);
						mapMS_MZHS.put("YSDM", ls_ysdm);
						mapMS_MZHS.put("GZRQ", ldt_sfrq);
						mapMS_MZHS.put("TJFS", ii_tjfs);
						mapMS_MZHS.put("HJJE", 0 - ld_hjje);
						mapMS_MZHS.put("JCD", 0 - li_jczs);
						mapMS_MZHS.put("XYF", 0);
						mapMS_MZHS.put("ZYF", 0);
						mapMS_MZHS.put("CYF", 0);
						mapMS_MZHS.put("MZRC", 0);
						listMS_MZHS.add(mapMS_MZHS);
					}
				}
			}
		} catch (PersistentDataOperationException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return listMS_MZHS;
	}

	public static void wf_tj_je(List<Map<String, Object>> adw_dw,
			List<Map<String, Object>> as_data, BaseDAO dao, Context ctx,
			int ii_tjfs, int ai_zfpb) throws ModelDataOperationException {
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfdatetime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		try {
			for (int ll_row = 0; ll_row < as_data.size(); ll_row++) {
				int sign = 0;
				Long li_ksdm = 0L;
				if (as_data.get(ll_row).get("KSDM") != null) {
					li_ksdm = Long.parseLong(as_data.get(ll_row).get("KSDM")
							+ "");
				}
				String ls_ysdm = null;
				if (as_data.get(ll_row).get("YSDM") != null) {
					ls_ysdm = as_data.get(ll_row).get("YSDM") + "";
				}
				Date ldt_sfrq = null;
				if (ii_tjfs == 1) {
					if (ai_zfpb == 1) {
						ldt_sfrq = sdfdatetime.parse(as_data.get(ll_row).get(
								"ZFRQ")
								+ "");// 作废日期
					} else {
						ldt_sfrq = sdfdatetime.parse(as_data.get(ll_row).get(
								"SFRQ")
								+ ""); // 收费日期
					}
				} else if (ii_tjfs == 2) {
					ldt_sfrq = sdfdatetime.parse(as_data.get(ll_row)
							.get("JZRQ") + ""); // 结帐日期
				} else if (ii_tjfs == 3) {
					ldt_sfrq = sdfdatetime.parse(as_data.get(ll_row)
							.get("HZRQ") + ""); // 汇总日期
				}
				ldt_sfrq = sdfdatetime.parse(sdfdate.format(ldt_sfrq)
						+ " 00:00:00");
				Long li_sfxm = 0L;
				if (as_data.get(ll_row).get("SFXM") != null) {
					li_sfxm = Long.parseLong(as_data.get(ll_row).get("SFXM")
							+ "");
				}
				double ld_sfje = 0.00;
				if (as_data.get(ll_row).get("SFJE") != null) {
					ld_sfje = Double.parseDouble(String.format("%1$.2f",
							as_data.get(ll_row).get("SFJE")));
				}
				double ld_zfje = 0.00;
				if (as_data.get(ll_row).get("ZFJE") != null) {
					ld_sfje = Double.parseDouble(String.format("%1$.2f",
							as_data.get(ll_row).get("ZFJE")));
				}
				if (adw_dw.size() <= 0) {
					Map<String, Object> mapadw_dw = new HashMap<String, Object>();
					mapadw_dw.put("KSDM", li_ksdm);
					mapadw_dw.put("YSDM", ls_ysdm);
					mapadw_dw.put("GZRQ", ldt_sfrq);
					mapadw_dw.put("TJFS", ii_tjfs);
					mapadw_dw.put("SFXM", li_sfxm);
					mapadw_dw.put("SFJE", ld_sfje);
					mapadw_dw.put("ZFJE", ld_zfje);
					adw_dw.add(mapadw_dw);
				} else {
					for (int ll_sf_row = 0; ll_sf_row < adw_dw.size(); ll_sf_row++) {
						if (Long.parseLong(adw_dw.get(ll_sf_row).get("KSDM")
								+ "") == li_ksdm
								&& (adw_dw.get(ll_sf_row).get("YSDM") + "")
										.equals(ls_ysdm)
								&& sdfdatetime.parse(
										sdfdatetime.format(adw_dw
												.get(ll_sf_row).get("GZRQ")))
										.getTime() == ldt_sfrq.getTime()
								&& Long.parseLong(adw_dw.get(ll_sf_row).get(
										"SFXM")
										+ "") == li_sfxm
								&& Integer.parseInt(adw_dw.get(ll_sf_row).get(
										"TJFS")
										+ "") == ii_tjfs) {
							adw_dw.get(ll_sf_row).put(
									"SFJE",
									Double.parseDouble(String.format(
											"%1$.2f",
											Double.parseDouble(adw_dw.get(
													ll_sf_row).get("SFJE")
													+ "")
													+ ld_sfje)));
							adw_dw.get(ll_sf_row).put(
									"ZFJE",
									Double.parseDouble(String.format(
											"%1$.2f",
											Double.parseDouble(adw_dw.get(
													ll_sf_row).get("ZFJE")
													+ "")
													+ ld_zfje)));
							sign = 1;
						}
					}
					if (sign == 0) {
						Map<String, Object> mapadw_dw = new HashMap<String, Object>();
						mapadw_dw.put("KSDM", li_ksdm);
						mapadw_dw.put("YSDM", ls_ysdm);
						mapadw_dw.put("GZRQ", ldt_sfrq);
						mapadw_dw.put("TJFS", ii_tjfs);
						mapadw_dw.put("SFXM", li_sfxm);
						mapadw_dw.put("SFJE", ld_sfje);
						mapadw_dw.put("ZFJE", ld_zfje);
						adw_dw.add(mapadw_dw);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static List<Map<String, Object>> wf_tj_mzmx(
			Map<String, Object> parameters, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		int ii_tjfs = Integer.parseInt(parameters.get("ii_tjfs") + "");
		parameters.remove("ii_tjfs");
		List<Map<String, Object>> ms_mzmxlist = new ArrayList<Map<String, Object>>();
		try {
			String sql = "";
			String sql2 = "";
			String sql3 = "";
			String sql4 = "";
			if (ii_tjfs == 1) {
				sql = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_CF02.FYGB as SFXM,SUM(MS_CF02.HJJE) AS SFJE,SUM(MS_CF02.HJJE * MS_CF02.ZFBL) AS ZFJE FROM MS_CF01 MS_CF01,MS_CF02 MS_CF02,MS_MZXX MS_MZXX WHERE (MS_CF02.CFSB = MS_CF01.CFSB) and (MS_MZXX.MZXH = MS_CF01.MZXH) AND MS_MZXX.SFRQ >= :ldt_begin And MS_MZXX.SFRQ <= :ldt_end and MS_MZXX.JGID = :gl_jgid GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF02.FYGB ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF02.FYGB";
				sql2 = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as ZFRQ,MS_CF02.FYGB as SFXM,0 - sum(MS_CF02.HJJE) AS SFJE,0 - SUM(MS_CF02.HJJE * MS_CF02.ZFBL) AS ZFJE FROM MS_CF01 MS_CF01,MS_CF02 MS_CF02,MS_ZFFP MS_ZFFP WHERE (MS_CF02.CFSB = MS_CF01.CFSB) and (MS_ZFFP.MZXH = MS_CF01.MZXH) AND MS_ZFFP.ZFRQ >= :ldt_begin And MS_ZFFP.ZFRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF02.FYGB ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF02.FYGB";
				sql3 = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_YJ02.FYGB AS SFXM,sum(MS_YJ02.HJJE) AS SFJE,SUM(MS_YJ02.HJJE * MS_YJ02.ZFBL) AS ZFJE FROM MS_MZXX MS_MZXX,MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02 WHERE (MS_YJ01.MZXH = MS_MZXX.MZXH) and (MS_YJ02.YJXH = MS_YJ01.YJXH) AND MS_MZXX.SFRQ >= :ldt_begin And MS_MZXX.SFRQ <= :ldt_end and MS_MZXX.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_YJ02.FYGB ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_YJ02.FYGB";
				sql4 = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as ZFRQ,MS_YJ02.FYGB as SFXM,0 - sum(MS_YJ02.HJJE) AS SFJE,0 - SUM(MS_YJ02.HJJE * MS_YJ02.ZFBL) AS ZFJE FROM MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02,MS_ZFFP MS_ZFFP WHERE (MS_YJ02.YJXH = MS_YJ01.YJXH) and (MS_ZFFP.MZXH = MS_YJ01.MZXH) AND MS_ZFFP.ZFRQ >= :ldt_begin And MS_ZFFP.ZFRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_YJ02.FYGB ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_YJ02.FYGB";
			} else if (ii_tjfs == 2) {
				sql = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_CF02.FYGB as SFXM,SUM(MS_CF02.HJJE) AS SFJE,SUM(MS_CF02.HJJE * MS_CF02.ZFBL) AS ZFJE FROM MS_CF01 MS_CF01,MS_CF02 MS_CF02,MS_MZXX MS_MZXX WHERE (MS_CF02.CFSB = MS_CF01.CFSB) and (MS_MZXX.MZXH = MS_CF01.MZXH) AND MS_MZXX.JZRQ >= :ldt_begin And MS_MZXX.JZRQ <= :ldt_end and MS_MZXX.JGID = :gl_jgid GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF02.FYGB ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF02.FYGB";
				sql2 = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as ZFRQ,MS_CF02.FYGB as SFXM,0 - sum(MS_CF02.HJJE) AS SFJE,0 - SUM(MS_CF02.HJJE * MS_CF02.ZFBL) AS ZFJE FROM MS_CF01 MS_CF01,MS_CF02 MS_CF02,MS_ZFFP MS_ZFFP WHERE (MS_CF02.CFSB = MS_CF01.CFSB) and (MS_ZFFP.MZXH = MS_CF01.MZXH) AND MS_ZFFP.JZRQ >= :ldt_begin And MS_ZFFP.JZRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF02.FYGB ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF02.FYGB";
				sql3 = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_YJ02.FYGB AS SFXM,sum(MS_YJ02.HJJE) AS SFJE,SUM(MS_YJ02.HJJE * MS_YJ02.ZFBL) AS ZFJE FROM MS_MZXX MS_MZXX,MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02 WHERE (MS_YJ01.MZXH = MS_MZXX.MZXH) and (MS_YJ02.YJXH = MS_YJ01.YJXH) AND MS_MZXX.JZRQ >= :ldt_begin And MS_MZXX.JZRQ <= :ldt_end and MS_MZXX.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_YJ02.FYGB ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_YJ02.FYGB";
				sql4 = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as ZFRQ,MS_YJ02.FYGB as SFXM,0 - sum(MS_YJ02.HJJE) AS SFJE,0 - SUM(MS_YJ02.HJJE * MS_YJ02.ZFBL) AS ZFJE FROM MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02,MS_ZFFP MS_ZFFP WHERE (MS_YJ02.YJXH = MS_YJ01.YJXH) and (MS_ZFFP.MZXH = MS_YJ01.MZXH) AND MS_ZFFP.JZRQ >= :ldt_begin And MS_ZFFP.JZRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_YJ02.FYGB ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_YJ02.FYGB";
			} else if (ii_tjfs == 3) {
				sql = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_CF02.FYGB as SFXM,SUM(MS_CF02.HJJE) AS SFJE,SUM(MS_CF02.HJJE * MS_CF02.ZFBL) AS ZFJE FROM MS_CF01 MS_CF01,MS_CF02 MS_CF02,MS_MZXX MS_MZXX WHERE (MS_CF02.CFSB = MS_CF01.CFSB) and (MS_MZXX.MZXH = MS_CF01.MZXH) AND MS_MZXX.HZRQ >= :ldt_begin And MS_MZXX.HZRQ <= :ldt_end and MS_MZXX.JGID = :gl_jgid GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF02.FYGB ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_CF02.FYGB";
				sql2 = "SELECT MS_CF01.KSDM as KSDM,MS_CF01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as ZFRQ,MS_CF02.FYGB as SFXM,0 - sum(MS_CF02.HJJE) AS SFJE,0 - SUM(MS_CF02.HJJE * MS_CF02.ZFBL) AS ZFJE FROM MS_CF01 MS_CF01,MS_CF02 MS_CF02,MS_ZFFP MS_ZFFP WHERE (MS_CF02.CFSB = MS_CF01.CFSB) and (MS_ZFFP.MZXH = MS_CF01.MZXH) AND MS_ZFFP.HZRQ >= :ldt_begin And MS_ZFFP.HZRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF02.FYGB ORDER BY MS_CF01.KSDM,MS_CF01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_CF02.FYGB";
				sql3 = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_YJ02.FYGB AS SFXM,sum(MS_YJ02.HJJE) AS SFJE,SUM(MS_YJ02.HJJE * MS_YJ02.ZFBL) AS ZFJE FROM MS_MZXX MS_MZXX,MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02 WHERE (MS_YJ01.MZXH = MS_MZXX.MZXH) and (MS_YJ02.YJXH = MS_YJ01.YJXH) AND MS_MZXX.HZRQ >= :ldt_begin And MS_MZXX.HZRQ <= :ldt_end and MS_MZXX.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_YJ02.FYGB ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_YJ02.FYGB";
				sql4 = "SELECT MS_YJ01.KSDM as KSDM,MS_YJ01.YSDM as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as ZFRQ,MS_YJ02.FYGB as SFXM,0 - sum(MS_YJ02.HJJE) AS SFJE,0 - SUM(MS_YJ02.HJJE * MS_YJ02.ZFBL) AS ZFJE FROM MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02,MS_ZFFP MS_ZFFP WHERE (MS_YJ02.YJXH = MS_YJ01.YJXH) and (MS_ZFFP.MZXH = MS_YJ01.MZXH) AND MS_ZFFP.HZRQ >= :ldt_begin And MS_ZFFP.HZRQ <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_YJ02.FYGB ORDER BY MS_YJ01.KSDM,MS_YJ01.YSDM,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_YJ02.FYGB";
			}

			// 开单金额临时表单
			List<Map<String, Object>> cf01cf02andmzxxList = dao.doQuery(sql,
					parameters);
			wf_tj_je(ms_mzmxlist, cf01cf02andmzxxList, dao, ctx, ii_tjfs, 0);

			// 开单作废金额临时表单
			List<Map<String, Object>> cf01cf02andzffplist = dao.doQuery(sql2,
					parameters);
			wf_tj_je(ms_mzmxlist, cf01cf02andzffplist, dao, ctx, ii_tjfs, 1);
			// 医技开单金额临时表单
			List<Map<String, Object>> yj01yj02andmzxxlist = dao.doQuery(sql3,
					parameters);
			wf_tj_je(ms_mzmxlist, yj01yj02andmzxxlist, dao, ctx, ii_tjfs, 0);
			// 医技开单作废金额临时表单
			List<Map<String, Object>> yj01yj02andzffplist = dao.doQuery(sql4,
					parameters);
			wf_tj_je(ms_mzmxlist, yj01yj02andzffplist, dao, ctx, ii_tjfs, 1);
		} catch (PersistentDataOperationException e) {
			e.printStackTrace();
		}
		return ms_mzmxlist;
	}

	/**
	 * 
	 * @param parameters里参数分别是ldt_begin
	 *            (开始时间)、ldt_end(结束时间)、gl_jgid(机构id)、ii_tjfs(提交方式)
	 * @param dao
	 * @param ctx
	 * @throws ModelDataOperationException
	 */
	public static List<Map<String, Object>> wf_tj_yjhs(
			Map<String, Object> parameters, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		List<Map<String, Object>> listMS_YJHS = new ArrayList<Map<String, Object>>();
		try {
			SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfdatetime = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			int ii_tjfs = Integer.parseInt(parameters.get("ii_tjfs") + "");
			parameters.remove("ii_tjfs");
			String ms_mzxx = "";
			String ms_zffp = "";
			if (ii_tjfs == 1) {
				ms_mzxx = "MS_MZXX.SFRQ";
				ms_zffp = "MS_ZFFP.ZFRQ";
			} else if (ii_tjfs == 2) {
				ms_mzxx = "MS_MZXX.JZRQ";
				ms_zffp = "MS_ZFFP.JZRQ";
			} else if (ii_tjfs == 3) {
				ms_mzxx = "MS_MZXX.HZRQ";
				ms_zffp = "MS_ZFFP.HZRQ";
			}
			// 检查张数临时表单
			List<Map<String, Object>> mzxxandyj01List = dao
					.doQuery(
							"SELECT MS_YJ01.ZXKS as KSDM,MS_YJ01.ZXYS as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,count(MS_YJ01.YJXH) AS YJZS FROM MS_MZXX MS_MZXX,MS_YJ01 MS_YJ01 WHERE (MS_YJ01.MZXH = MS_MZXX.MZXH) and (MS_YJ01.ZXPB = 1) AND "
									+ ms_mzxx
									+ " >= :ldt_begin And "
									+ ms_mzxx
									+ " <= :ldt_end and MS_MZXX.JGID = :gl_jgid GROUP BY MS_YJ01.ZXKS,MS_YJ01.ZXYS,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ ORDER BY MS_YJ01.ZXKS,MS_YJ01.ZXYS,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ",
							parameters);
			for (int i = 0; i < mzxxandyj01List.size(); i++) {
				int sign = 0;
				Long li_ksdm = 0L;
				if (mzxxandyj01List.get(i).get("KSDM") != null) {
					li_ksdm = Long.parseLong(mzxxandyj01List.get(i).get("KSDM")
							+ "");
				}
				String ls_ysdm = null;
				if (mzxxandyj01List.get(i).get("YSDM") != null) {
					ls_ysdm = mzxxandyj01List.get(i).get("YSDM") + "";
				}

				int li_yjzs = 0;
				if (mzxxandyj01List.get(i).get("YJZS") != null) {
					li_yjzs = Integer.parseInt(mzxxandyj01List.get(i).get(
							"YJZS")
							+ "");
				}
				Date ldt_sfrq = null;
				if (ii_tjfs == 1) {
					if (mzxxandyj01List.get(i).get("SFRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(mzxxandyj01List.get(i)
								.get("SFRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 2) {
					if (mzxxandyj01List.get(i).get("JZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(mzxxandyj01List.get(i)
								.get("JZRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 3) {
					if (mzxxandyj01List.get(i).get("HZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(mzxxandyj01List.get(i)
								.get("HZRQ") + "");
					} else {
						continue;
					}
				}
				ldt_sfrq = sdfdatetime.parse(sdfdate.format(ldt_sfrq)
						+ " 00:00:00");
				if (listMS_YJHS.size() <= 0) {
					Map<String, Object> mapMS_YJHS = new HashMap<String, Object>();
					mapMS_YJHS.put("KSDM", li_ksdm);
					mapMS_YJHS.put("YSDM", ls_ysdm);
					mapMS_YJHS.put("GZRQ", ldt_sfrq);
					mapMS_YJHS.put("TJFS", ii_tjfs);
					mapMS_YJHS.put("JCD", li_yjzs);
					mapMS_YJHS.put("XYF", 0);
					mapMS_YJHS.put("ZYF", 0);
					mapMS_YJHS.put("CYF", 0);
					mapMS_YJHS.put("HJJE", 0);
					mapMS_YJHS.put("MZRC", 0);
					listMS_YJHS.add(mapMS_YJHS);
				} else {
					for (int j = 0; j < listMS_YJHS.size(); j++) {
						if (Long.parseLong(listMS_YJHS.get(j).get("KSDM") + "") == li_ksdm
								&& listMS_YJHS.get(j).get("YSDM").toString()
										.equals(ls_ysdm)
								&& sdfdatetime.parse(
										sdfdatetime.format(listMS_YJHS.get(j)
												.get("GZRQ"))).getTime() == ldt_sfrq
										.getTime()
								&& Integer.parseInt(listMS_YJHS.get(j).get(
										"TJFS")
										+ "") == ii_tjfs) {

							listMS_YJHS.get(j).put(
									"JCD",
									Integer.parseInt(listMS_YJHS.get(j).get(
											"JCD")
											+ "")
											+ li_yjzs);
							sign = 1;
						}

					}
					if (sign == 0) {
						Map<String, Object> mapMS_YJHS = new HashMap<String, Object>();
						mapMS_YJHS.put("KSDM", li_ksdm);
						mapMS_YJHS.put("YSDM", ls_ysdm);
						mapMS_YJHS.put("GZRQ", ldt_sfrq);
						mapMS_YJHS.put("TJFS", ii_tjfs);
						mapMS_YJHS.put("JCD", li_yjzs);
						mapMS_YJHS.put("XYF", 0);
						mapMS_YJHS.put("ZYF", 0);
						mapMS_YJHS.put("CYF", 0);
						mapMS_YJHS.put("HJJE", 0);
						mapMS_YJHS.put("MZRC", 0);
						listMS_YJHS.add(mapMS_YJHS);
					}
				}
			}
			// 检查作废张数临时表单
			List<Map<String, Object>> yj01andzffpList = dao
					.doQuery(
							"SELECT MS_YJ01.ZXKS as KSDM,MS_YJ01.ZXYS as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,count(MS_YJ01.YJXH) AS YJZS FROM MS_YJ01 MS_YJ01,MS_ZFFP MS_ZFFP WHERE (MS_ZFFP.MZXH = MS_YJ01.MZXH) AND (MS_YJ01.ZXPB = 1) AND "
									+ ms_zffp
									+ " >= :ldt_begin And "
									+ ms_zffp
									+ " <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_YJ01.ZXKS,MS_YJ01.ZXYS,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ ORDER BY MS_YJ01.ZXKS,MS_YJ01.ZXYS,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ",
							parameters);
			for (int i = 0; i < yj01andzffpList.size(); i++) {
				int signzf = 0;
				Long li_ksdm = 0L;
				if (yj01andzffpList.get(i).get("KSDM") != null) {
					li_ksdm = Long.parseLong(yj01andzffpList.get(i).get("KSDM")
							+ "");
				}
				String ls_ysdm = null;
				if (yj01andzffpList.get(i).get("YSDM") != null) {
					ls_ysdm = yj01andzffpList.get(i).get("YSDM") + "";
				}
				int li_yjzs = 0;
				if (yj01andzffpList.get(i).get("YJZS") != null) {
					li_yjzs = Integer.parseInt(yj01andzffpList.get(i).get(
							"YJZS")
							+ "");
				}
				Date ldt_sfrq = null;
				if (ii_tjfs == 1) {
					if (yj01andzffpList.get(i).get("SFRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(yj01andzffpList.get(i)
								.get("SFRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 2) {
					if (yj01andzffpList.get(i).get("JZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(yj01andzffpList.get(i)
								.get("JZRQ") + "");
					} else {
						continue;
					}
				} else if (ii_tjfs == 3) {
					if (yj01andzffpList.get(i).get("HZRQ") != null) {
						ldt_sfrq = sdfdatetime.parse(yj01andzffpList.get(i)
								.get("HZRQ") + "");
					} else {
						continue;
					}
				}
				ldt_sfrq = sdfdatetime.parse(sdfdate.format(ldt_sfrq)
						+ " 00:00:00");
				if (listMS_YJHS.size() <= 0) {
					Map<String, Object> mapzfMS_YJHS = new HashMap<String, Object>();
					mapzfMS_YJHS.put("KSDM", li_ksdm);
					mapzfMS_YJHS.put("YSDM", ls_ysdm);
					mapzfMS_YJHS.put("GZRQ", ldt_sfrq);
					mapzfMS_YJHS.put("TJFS", ii_tjfs);
					mapzfMS_YJHS.put("JCD", -li_yjzs);
					mapzfMS_YJHS.put("XYF", 0);
					mapzfMS_YJHS.put("ZYF", 0);
					mapzfMS_YJHS.put("CYF", 0);
					mapzfMS_YJHS.put("HJJE", 0);
					mapzfMS_YJHS.put("MZRC", 0);
					listMS_YJHS.add(mapzfMS_YJHS);
				} else {
					for (int j = 0; j < listMS_YJHS.size(); j++) {
						if (Long.parseLong(listMS_YJHS.get(j).get("KSDM") + "") == li_ksdm
								&& listMS_YJHS.get(j).get("YSDM").toString()
										.equals(ls_ysdm)
								&& sdfdatetime.parse(
										sdfdatetime.format(listMS_YJHS.get(j)
												.get("GZRQ"))).getTime() == ldt_sfrq
										.getTime()
								&& Integer.parseInt(listMS_YJHS.get(j).get(
										"TJFS")
										+ "") == ii_tjfs) {
							int li_zs = 0;
							li_zs = Integer.parseInt(listMS_YJHS.get(j).get(
									"HJJE")
									+ "");
							listMS_YJHS.get(j).put("JCD", li_zs - li_yjzs);
							signzf = 1;
						}
					}
					if (signzf == 0) {
						Map<String, Object> mapzfMS_YJHS = new HashMap<String, Object>();
						mapzfMS_YJHS.put("KSDM", li_ksdm);
						mapzfMS_YJHS.put("YSDM", ls_ysdm);
						mapzfMS_YJHS.put("GZRQ", ldt_sfrq);
						mapzfMS_YJHS.put("TJFS", ii_tjfs);
						mapzfMS_YJHS.put("JCD", -li_yjzs);
						mapzfMS_YJHS.put("XYF", 0);
						mapzfMS_YJHS.put("ZYF", 0);
						mapzfMS_YJHS.put("CYF", 0);
						mapzfMS_YJHS.put("HJJE", 0);
						mapzfMS_YJHS.put("MZRC", 0);
						listMS_YJHS.add(mapzfMS_YJHS);
					}
				}
			}
		} catch (PersistentDataOperationException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return listMS_YJHS;
	}

	public static List<Map<String, Object>> wf_tj_yjmx(
			Map<String, Object> parameters, BaseDAO dao, Context ctx)
			throws ModelDataOperationException {
		int ii_tjfs = Integer.parseInt(parameters.get("ii_tjfs") + "");
		parameters.remove("ii_tjfs");
		List<Map<String, Object>> ms_yjmxlist = new ArrayList<Map<String, Object>>();
		try {
			String ms_mzxx = "";
			String ms_zffp = "";
			if (ii_tjfs == 1) {
				ms_mzxx = "MS_MZXX.SFRQ";
				ms_zffp = "MS_ZFFP.ZFRQ";
			} else if (ii_tjfs == 2) {
				ms_mzxx = "MS_MZXX.JZRQ";
				ms_zffp = "MS_ZFFP.JZRQ";
			} else if (ii_tjfs == 3) {
				ms_mzxx = "MS_MZXX.HZRQ";
				ms_zffp = "MS_ZFFP.HZRQ";
			}
			// 执行金额临时表单
			List<Map<String, Object>> yj01yj02andmzxxList = dao
					.doQuery(
							"SELECT MS_YJ01.ZXKS as KSDM,MS_YJ01.ZXYS as YSDM,str(MS_MZXX.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_MZXX.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_MZXX.SFRQ,'YYYY-MM-DD HH24:MI:SS') as SFRQ,MS_YJ02.FYGB as SFXM,sum(MS_YJ02.HJJE) AS SFJE,SUM(MS_YJ02.HJJE * MS_YJ02.ZFBL) AS ZFJE FROM MS_MZXX MS_MZXX,MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02 WHERE (MS_YJ01.MZXH = MS_MZXX.MZXH) and (MS_YJ02.YJXH = MS_YJ01.YJXH) and (MS_YJ01.ZXPB = 1) AND "
									+ ms_mzxx
									+ " >= :ldt_begin And "
									+ ms_mzxx
									+ " <= :ldt_end and MS_MZXX.JGID = :gl_jgid GROUP BY MS_YJ01.ZXKS,MS_YJ01.ZXYS,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_YJ02.FYGB ORDER BY MS_YJ01.ZXKS,MS_YJ01.ZXYS,MS_MZXX.HZRQ,MS_MZXX.JZRQ,MS_MZXX.SFRQ,MS_YJ02.FYGB",
							parameters);
			wf_tj_je(ms_yjmxlist, yj01yj02andmzxxList, dao, ctx, ii_tjfs, 0);
			// 执行作废金额临时表单
			List<Map<String, Object>> yj01yj02andzffplist = dao
					.doQuery(
							"SELECT MS_YJ01.ZXKS as KSDM,MS_YJ01.ZXYS as YSDM,str(MS_ZFFP.HZRQ,'YYYY-MM-DD HH24:MI:SS') as HZRQ,str(MS_ZFFP.JZRQ,'YYYY-MM-DD HH24:MI:SS') as JZRQ,str(MS_ZFFP.ZFRQ,'YYYY-MM-DD HH24:MI:SS') as ZFRQ,MS_YJ02.FYGB as SFXM,0-sum(MS_YJ02.HJJE) AS SFJE,0 - SUM(MS_YJ02.HJJE * MS_YJ02.ZFBL) AS ZFJE FROM MS_YJ01 MS_YJ01,MS_YJ02 MS_YJ02,MS_ZFFP MS_ZFFP WHERE (MS_YJ02.YJXH = MS_YJ01.YJXH) and (MS_ZFFP.MZXH = MS_YJ01.MZXH) AND (MS_YJ01.ZXPB = 1) AND "
									+ ms_zffp
									+ " >= :ldt_begin And "
									+ ms_zffp
									+ " <= :ldt_end and MS_ZFFP.JGID = :gl_jgid GROUP BY MS_YJ01.ZXKS,MS_YJ01.ZXYS,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_YJ02.FYGB ORDER BY MS_YJ01.ZXKS,MS_YJ01.ZXYS,MS_ZFFP.HZRQ,MS_ZFFP.JZRQ,MS_ZFFP.ZFRQ,MS_YJ02.FYGB",
							parameters);
			wf_tj_je(ms_yjmxlist, yj01yj02andzffplist, dao, ctx, ii_tjfs, 1);

		} catch (PersistentDataOperationException e) {
			e.printStackTrace();
		}
		return ms_yjmxlist;
	}

	public static String daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = Math.abs(time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days)) + "";
	}
}
