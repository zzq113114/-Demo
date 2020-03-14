package phis.application.eg.source;

import ctd.service.core.ServiceException;
import ctd.util.context.Context;
import phis.source.BaseDAO;
import phis.source.ModelDataOperationException;
import phis.source.service.AbstractActionService;
import phis.source.service.DAOSupportable;

import java.util.List;
import java.util.Map;

public class CxService extends AbstractActionService implements
        DAOSupportable {

    public void doCxcf01(Map<String, Object> req, Map<String, Object> res, BaseDAO dao, Context ctx)
            throws ServiceException {
        CxModel model=new CxModel(dao);
        List<Map<String,Object>> body= null;
        try {
            body = model.cxcf01();
        } catch (ModelDataOperationException e) {
            res.put(RES_CODE, e.getCode());
            res.put(RES_MESSAGE, e.getMessage());
            throw new ServiceException(e);
        }
        res.put("body",body);
    }
}
