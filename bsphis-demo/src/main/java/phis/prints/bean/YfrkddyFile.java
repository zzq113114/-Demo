package phis.prints.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import phis.application.mds.source.MedicineUtils;
import phis.source.BaseDAO;
import phis.source.PersistentDataOperationException;

import ctd.account.UserRoleToken;
import ctd.print.IHandler;
import ctd.print.PrintException;
import ctd.util.context.Context;

public class YfrkddyFile implements IHandler{

	@Override
	public void getParameters(Map<String, Object> request,
			Map<String, Object> response, Context ctx) throws PrintException {
		String jgname = UserRoleToken.getCurrent().getManageUnit().getName();//当前机构名称
		BaseDAO dao = new BaseDAO(ctx);//通过ctx创建获取dao
		long yfsb=MedicineUtils.parseLong(request.get("yfsb"));//药房识别
		int rkfs=MedicineUtils.parseInt(request.get("rkfs"));//入库方式
		int rkdh=MedicineUtils.parseInt(request.get("rkdh"));//入库单号
		StringBuffer hql=new StringBuffer();//查询报表所需字段的sql
		hql.append("select b.FSMC as RKFS,a.RKRQ as RKRQ,a.RKBZ as BZ from YF_RK01 a,YF_RKFS b where a.YFSB=b.YFSB ");
		hql.append("and a.RKFS=b.RKFS and a.YFSB=:yfsb and a.RKDH=:rkdh and a.RKFS=:rkfs");
		Map<String,Object> map_par=new HashMap<String,Object>();//参数集合
		map_par.put("yfsb", yfsb);
		map_par.put("rkfs", rkfs);
		map_par.put("rkdh", rkdh);
		try {
			Map<String,Object> map_rk01=dao.doLoad(hql.toString(), map_par);
			if(map_rk01==null||map_rk01.size()==0){
				throw new  PrintException(9000,"未找到入库记录:");
			}
			//response中存放的数据会填充报表中的 $paramter
			response.put("RKFS", MedicineUtils.parseString(map_rk01.get("RKFS")));
			response.put("RKRQ", MedicineUtils.parseString(map_rk01.get("RKRQ")));
			response.put("BZ", MedicineUtils.parseString(map_rk01.get("BZ")));
			response.put("JGMC", jgname);
			response.put("DH", rkdh);
		} catch (PersistentDataOperationException e) {
			throw new  PrintException(9000,"报表数据查询失败:"+e.getMessage());
		}
	}

	@Override
	public void getFields(Map<String, Object> request,
			List<Map<String, Object>> records, Context ctx)
			throws PrintException {
		BaseDAO dao = new BaseDAO(ctx);//通过ctx创建获取dao
		long yfsb=MedicineUtils.parseLong(request.get("yfsb"));//药房识别
		int rkfs=MedicineUtils.parseInt(request.get("rkfs"));//入库方式
		int rkdh=MedicineUtils.parseInt(request.get("rkdh"));//入库单号
		StringBuffer hql=new StringBuffer();//查询报表所需字段的sql
		hql.append("select b.YPMC as YPMC,a.YPGG as YFGG,a.YFDW as YFDW,a.RKSL as YPSL,a.JHJG as JHJG");
		hql.append(",a.JHJE as JHJE,a.LSJG as LSJG,a.LSJE as LSJE,a.YPPH as YPPH,a.YPXQ as YPXQ,c.CDMC as CDMC  ");
		hql.append("from YF_RK02 a,YK_TYPK b,YK_CDDZ c where a.YPXH=b.YPXH and a.YPCD=c.YPCD ");
		hql.append("and a.YFSB=:yfsb and a.RKDH=:rkdh and a.RKFS=:rkfs");
		Map<String,Object> map_par=new HashMap<String,Object>();//参数集合
		map_par.put("yfsb", yfsb);
		map_par.put("rkfs", rkfs);
		map_par.put("rkdh", rkdh);
		try {
			List<Map<String,Object>> list_rk02=dao.doQuery(hql.toString(), map_par);
			if(list_rk02==null||list_rk02.size()==0){
				throw new  PrintException(9000,"未找到入库明细记录:");
			}
			//records中存放的数据会填充报表中的 $field
			records.addAll(list_rk02);
		} catch (PersistentDataOperationException e) {
			throw new  PrintException(9000,"报表数据查询失败:"+e.getMessage());
		}
	}

}
