package phis.prints.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import phis.application.mds.source.MedicineUtils;
import phis.source.BaseDAO;
import phis.source.PersistentDataOperationException;

import ctd.print.IHandler;
import ctd.print.PrintException;
import ctd.print.PrintUtil;
import ctd.util.context.Context;

public class YfsyrkddyFile implements IHandler{

	@Override
	public void getParameters(Map<String, Object> request,
			Map<String, Object> response, Context ctx) throws PrintException {
				// 获得项目的绝对路径 并将路径值传进SUBREPORT_DIR中,
				// 因子报表SUBREPORT_DIR参数值在不同服务器上路径不同,所以用动态路径传值
				String realPath = YfsyrkddyFile.class.getResource("/").getPath();
				realPath = realPath.substring(1, realPath.indexOf("WEB-INF"));
				String[] path = realPath.split("/");
				String usePath = "";
				String xg = System.getProperty("file.separator");
				for (int i = 0; i < path.length; i++) {
					if (i == 0) {
						usePath = path[i];
					} else {
						usePath += xg + path[i];
					}
				}
				usePath = usePath.replace("%20", " ");
				response.put("SUBREPORT_DIR", xg + usePath + xg + "WEB-INF" + xg
						+ "classes" + xg + "phis" + xg + "prints" + xg + "jrxml" + xg);
	}

	@Override
	public void getFields(Map<String, Object> request,
			List<Map<String, Object>> records, Context ctx)
			throws PrintException {
		BaseDAO dao = new BaseDAO(ctx);//通过ctx创建获取dao
		StringBuffer hql_rk01=new StringBuffer();//查询YFSB是365的药房所有入库单
		hql_rk01.append("select b.FSMC as FSMC,a.RKRQ as RKRQ,a.RKBZ as BZ,a.RKFS as RKFS,a.YFSB as YFSB,a.RKDH as DH ");
		hql_rk01.append("from YF_RK01 a,YF_RKFS b where a.YFSB=b.YFSB and a.RKFS=b.RKFS and a.YFSB=365");
		StringBuffer hql_rk02=new StringBuffer();//查询入库单对应的明细记录
		hql_rk02.append("select b.YPMC as YPMC,a.YPGG as YFGG,a.YFDW as YFDW,a.RKSL as YPSL,a.JHJG as JHJG");
		hql_rk02.append(",a.JHJE as JHJE,a.LSJG as LSJG,a.LSJE as LSJE,a.YPPH as YPPH,a.YPXQ as YPXQ,c.CDMC as CDMC  ");
		hql_rk02.append("from YF_RK02 a,YK_TYPK b,YK_CDDZ c where a.YPXH=b.YPXH and a.YPCD=c.YPCD ");
		hql_rk02.append("and a.YFSB=:yfsb and a.RKDH=:rkdh and a.RKFS=:rkfs");
		try {
			List<Map<String,Object>> list_rk01=dao.doSqlQuery(hql_rk01.toString(), null);
			if(list_rk01!=null&&list_rk01.size()>0){
				for(Map<String,Object> map_rk01:list_rk01){//循环入库单,查询明细记录,每条入库单是一个子报表的数据
					int rkfs=MedicineUtils.parseInt(map_rk01.get("RKFS"));
					Map<String,Object> map_data=new HashMap<String,Object>();//存放子报表数据的集合
					Map<String,Object> map_par=new HashMap<String,Object>();
					map_par.put("yfsb", MedicineUtils.parseLong(map_rk01.get("YFSB")));
					map_par.put("rkfs", rkfs);
					map_par.put("rkdh", MedicineUtils.parseInt(map_rk01.get("DH")));
					//子报表的明细记录.这里需要通过PrintUtil.ds方法将list数据转成
					//net.sf.jasperreports.engine.data.JRMapCollectionDataSource数据
					map_data.put("DetailRecords", PrintUtil.ds(dao.doQuery(hql_rk02.toString(), map_par)));
					map_rk01.put("RKFS", map_rk01.get("FSMC"));
					map_data.put("MapRecord", map_rk01);//子报表的parameters数据
					records.add(map_data);
				}
			}
		} catch (PersistentDataOperationException e) {
			throw new  PrintException(9000,"报表数据查询失败:"+e.getMessage());
		}
		
		
	}

}
