package phis.application.eg.source;

import phis.source.BaseDAO;
import phis.source.ModelDataOperationException;
import phis.source.PersistentDataOperationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CxModel {
    protected BaseDAO dao;
    protected Logger logger = LoggerFactory
            .getLogger(CxModel.class);

    public   CxModel(BaseDAO dao){
        this.dao=dao;
    }

    public List<Map<String,Object>> cxcf01() throws ModelDataOperationException {
        StringBuffer hql=new StringBuffer();
        hql.append("select CFSB as CFSB,JGID as JGID,CFHM as CFHM,FPHM as FPHM,KFRQ as KFRQ,CFLX as CFLX,YSDM as YSDM from MS_CF01");
       Map<String,Object> map_par=new HashMap<String,Object>();
//       map_par.put("cfsb",123);
        try {
            return dao.doQuery(hql.toString(),map_par);
        } catch (PersistentDataOperationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
