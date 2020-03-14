package phis.source.service.base;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import phis.application.pub.source.PermissionsVerifyService;
import phis.source.BaseDAO;
import phis.source.PersistentDataOperationException;
import phis.source.utils.ParameterUtil;
import ctd.account.UserRoleToken;
import ctd.domain.DomainUtil;
import ctd.print.PrintUtil;
import ctd.service.core.ServiceException;
import ctd.service.logon.LogonInfoLoader;
import ctd.util.annotation.RpcService;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

public class PhisCommonLogonInfo implements LogonInfoLoader {
	private static final Logger logger = LoggerFactory
			.getLogger(PhisCommonLogonInfo.class);

	// @RpcService
	@SuppressWarnings("unchecked")
	@RpcService(executor = "HibernateSupportExecutor")
	// @DBSupport(transaction=true)
	@Override
	public Map<String, Object> afterLogon() {
		HttpSession httpSession = ((HttpServletRequest) ContextUtils
				.get(Context.HTTP_REQUEST)).getSession(false);
		HashMap<String, Object> prop = null;
		if (httpSession != null) {
			prop = (HashMap<String, Object>) httpSession
					.getAttribute("properties");
		}
		if (prop == null) {
			prop = new HashMap<String, Object>();
		}

		HashMap<String, Object> res = new HashMap<String, Object>();
		// super.afterLogon(res);
		PrintUtil.setLOAD("phis.simpleLoad");
		PrintUtil.setQUERY("phis.simpleQuery");
		return res;
	}

}
