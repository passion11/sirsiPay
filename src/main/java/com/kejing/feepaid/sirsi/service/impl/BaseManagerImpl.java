package com.kejing.feepaid.sirsi.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.kejing.feepaid.sirsi.config.SirsiFeepaidRestConf;
import com.kejing.feepaid.sirsi.dao.ISirsiFeepaidClient;
import com.kejing.feepaid.sirsi.dao.ItfeepaidDAO;
import com.kejing.feepaid.sirsi.dao.module.Tfeepaid;

@Service
public class BaseManagerImpl {
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	protected SimpleDateFormat sdfSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	protected DecimalFormat decimalFormat = new DecimalFormat();
	/// ------------------------------------TABLES--------------------------------------///
	@Autowired
	protected ItfeepaidDAO tfeepaidDao;

	/// ------------------------------------UTILS------------------------------------------///
	@Autowired
	protected ISirsiFeepaidClient sirsiFeepaidClient;

	@Autowired
	protected SirsiFeepaidRestConf conf;

	public List<Tfeepaid> getTfeepaidByTime() {
		Date date1 = new Date();
		// 测试昨天的
		long times = new Date().getTime() - 1000 * 24 * 60 * 60;
		date1.setTime(times);
		Date date2 = new Date();
		String startTime = sdf.format(date1);
		String endsTime = sdf.format(date2);
		List<Tfeepaid> list = tfeepaidDao.queryTfeepaidSucc(startTime, endsTime);
		return list;
	}

	// 获得项目根路径
	protected static String getResourceBasePath() {
		File path = null;
		try {
			path = new File(ResourceUtils.getURL("classpath:").getPath());
			System.out.println("path:" + path);
		} catch (FileNotFoundException e) {
		}
		if (path == null || !path.exists()) {
			path = new File("");
		}
		String pathStr = path.getAbsolutePath();
		System.out.println("替换前的pathStr" + pathStr);
		// pathStr = pathStr.replace("\\classes", "\\src\\main\\resource");
		System.out.println("pathStr" + pathStr);
		return pathStr;
	}

	protected String getOneDayForDown() {
		Date date = new Date();
		long time = date.getTime() - 1000 * 60 * 60 * 23;
		date.setTime(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
}
