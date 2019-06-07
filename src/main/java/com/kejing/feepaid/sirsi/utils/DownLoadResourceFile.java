package com.kejing.feepaid.sirsi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kejing.feepaid.sirsi.service.impl.BaseManagerImpl;

/**
 * 浏览器附件下载
 * 
 * @author apple
 *
 */

public class DownLoadResourceFile extends BaseManagerImpl {
	public static final String ATTACHMENT = "attachment"; // 附件下载
	public static final Logger logger = LoggerFactory.getLogger(DownLoadResourceFile.class);

	public static void downloadFile(HttpServletResponse response, String date, String dz) throws Exception {
		String fileName = "bill\\" + dz + "." + date + ".txt";
		// 获取项目根目录
		String basePath = getResourceBasePath();
		File file = new File(basePath, fileName);
		download(response, file.getAbsolutePath(), file.getName(), ATTACHMENT);
	}

	private static void download(HttpServletResponse response, String filePath, String fileName, String downloadWay)
			throws Exception {
		try {
			// 浏览器受到这个响应头的响应对象,创建文件,待读写
			response.addHeader("Content-Disposition", downloadWay + "; filename=" + fileName);
			InputStream is = new FileInputStream(filePath);
			byte[] b = new byte[8 * 1024];
			int length = -1;
			OutputStream out = response.getOutputStream();
			while ((length = is.read(b)) != -1) {
				out.write(b, 0, length);
			}
			out.close();
			is.close();
		} catch (Exception e) {
			// error等级,记录！
			if (logger.isErrorEnabled()) {
				logger.error(ExceptionUtils.getMessage(e));
			}
			throw e;
		}

	}

}
