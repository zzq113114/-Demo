package phis.source.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import phis.source.BaseDAO;
import ctd.account.UserRoleToken;
import ctd.util.AppContextHolder;
import ctd.util.S;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

public class OpLogUtil {
	protected static Logger logger = LoggerFactory.getLogger(OpLogUtil.class);

	public static void writeLog(String desc) {
		writeLog(desc, null);
	}

	/**
	 * 日志记录 Sys_log4j
	 * 
	 * @param desc
	 */
	public static void writeLog(String desc, String view) {
		Context ctx = ContextUtils.getContext();
		UserRoleToken user = UserRoleToken.getCurrent();
		// 此IP若为RPC调用，可能无法获取到准确的客户端IP
		String clientIp = (String) ctx.get(Context.CLIENT_IP_ADDRESS);
		SessionFactory sf = AppContextHolder.getBean(
				AppContextHolder.DEFAULT_SESSION_FACTORY, SessionFactory.class);
		Session ss = sf.openSession();
		Map<String, Object> log4j = new HashMap<String, Object>();
		log4j.put("logDate", new Date());
		log4j.put("logLevel", "INFO");
		if (!S.isEmpty(desc)) {
			log4j.put(
					"logMessage",
					desc.substring(0,
							(desc.length() > 1000 ? 1000 : desc.length())));
		}
		log4j.put("logType", "1");
		log4j.put("logUser", user.getUserName());
		log4j.put("logClass", clientIp);
		if (!S.isEmpty(view)) {
			log4j.put("logInfo", ss.getLobHelper().createBlob(view.getBytes()));
			log4j.put("infoType", 1);
		}
		ss.beginTransaction();
		BaseDAO dao = new BaseDAO(ctx, ss);
		try {
			dao.doSave("create","platform.security.schemas.SYS_Log4j", log4j, false);
			ss.getTransaction().commit();
		} catch (Exception e) {
			ss.getTransaction().rollback();
			logger.error(e.getMessage());
		} finally {
			if (ss != null && ss.isOpen()) {
				ss.close();
			}
		}
	}

	public static byte[] compress(String paramString) {
		if (paramString == null)
			return null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		byte[] arrayOfByte;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			zipOutputStream.putNextEntry(new ZipEntry("0"));
			zipOutputStream.write(paramString.getBytes());
			zipOutputStream.closeEntry();
			arrayOfByte = byteArrayOutputStream.toByteArray();
		} catch (IOException localIOException5) {
			arrayOfByte = null;
		} finally {
			if (zipOutputStream != null)
				try {
					zipOutputStream.close();
				} catch (IOException localIOException6) {
				}
			if (byteArrayOutputStream != null)
				try {
					byteArrayOutputStream.close();
				} catch (IOException localIOException7) {
				}
		}
		return arrayOfByte;
	}

	@SuppressWarnings("unused")
	public static String decompress(byte[] paramArrayOfByte) {
		if (paramArrayOfByte == null)
			return null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		ByteArrayInputStream byteArrayInputStream = null;
		ZipInputStream zipInputStream = null;
		String str;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
			zipInputStream = new ZipInputStream(byteArrayInputStream);
			ZipEntry localZipEntry = zipInputStream.getNextEntry();
			byte[] arrayOfByte = new byte[1024];
			int i = -1;
			while ((i = zipInputStream.read(arrayOfByte)) != -1)
				byteArrayOutputStream.write(arrayOfByte, 0, i);
			str = byteArrayOutputStream.toString();
		} catch (IOException localIOException7) {
			str = null;
		} finally {
			if (zipInputStream != null)
				try {
					zipInputStream.close();
				} catch (IOException localIOException8) {
				}
			if (byteArrayInputStream != null)
				try {
					byteArrayInputStream.close();
				} catch (IOException localIOException9) {
				}
			if (byteArrayOutputStream != null)
				try {
					byteArrayOutputStream.close();
				} catch (IOException localIOException10) {
				}
		}
		return str;
	}

}
