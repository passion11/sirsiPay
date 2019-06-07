package com.kejing.feepaid.sirsi.threads;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.kejing.feepaid.sirsi.config.SirsiFeepaidRestConf;
import com.kejing.feepaid.sirsi.dao.ItfeepaidDAO;
import com.kejing.feepaid.sirsi.dao.module.Tfeepaid;
import com.kejing.feepaid.sirsi.service.impl.BaseManagerImpl;

@Component
public class GenfileScheduledledTask extends BaseManagerImpl implements SchedulingConfigurer {
	@Autowired
	protected SirsiFeepaidRestConf conf;
	@Autowired
	protected ItfeepaidDAO tfeepaidDao;

	private static String cron = "0 0 0 * * ?";
	// private static String cron = "0 29/1 12 * * ?";

	private Runnable dodownload() throws IOException {
		return new Runnable() {
			public void run() {
				// 根目录
				String basepath = getResourceBasePath();
				// 测试前八天的
				List<Tfeepaid> tfeepaids = getTfeepaidByTime();
				// 昨天日期
				String timeName = getOneDayForDown();
				String KjDownResourcePath = "";
				Map<String, List<Tfeepaid>> map = new HashMap<String, List<Tfeepaid>>();
				for (Tfeepaid tfeepaid : tfeepaids) {
					List<Tfeepaid> list1 = map.get(tfeepaid.getFcertid());
					if (list1 == null) {
						list1 = new ArrayList<Tfeepaid>();
					}
					list1.add(tfeepaid);
					map.put(tfeepaid.getFcertid(), list1);
				}
				Set<String> set = map.keySet();
				for (String key : set) {
					List<Tfeepaid> list2 = map.get(key);
					KjDownResourcePath = new File(basepath, "bill/" + key + "." + timeName + ".txt").getAbsolutePath();
					ensureDirectory(KjDownResourcePath);
					// 昨天每个用户的支付金额总计
					String allPayMoney = getAllPay(list2);
					StringBuffer str = new StringBuffer();
					str.append("总成功笔数:" + list2.size()).append("|").append("总成功金额:" + allPayMoney).append("\r\n");
					str.append("\t").append("用户").append("\t|");
					str.append("金额").append("\t|");
					str.append("状态").append("\t|");
					str.append("发起时间").append("\t|");
					str.append("平账次数").append("\t|");
					str.append("最后更新时间").append("\r\n");
					for (Tfeepaid tfeepaid : list2) {
						str.append(tfeepaid.getFcertid()).append("\t|");
						str.append(tfeepaid.getFfeeamount()).append("\t|");
						str.append(tfeepaid.getFstatus()).append("\t|");
						str.append(sdf.format(tfeepaid.getFtimestamp())).append("\t|");
						str.append(tfeepaid.getFcommittimes()).append("\t\t|");
						str.append(sdf.format(tfeepaid.getFlastcommit())).append("\r\n");
					}
					try {
						BufferedWriter bw = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(KjDownResourcePath)));
						bw.write(str.toString());
						bw.flush();
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		};
	}

	private String getAllPay(List<Tfeepaid> list) {
		double allPayMoney = 0.00;
		for (Tfeepaid tfeepaid : list) {
			allPayMoney += tfeepaid.getFfeeamount();
		}
		// 昨天某用户总计成功平账金额四舍五入,保留两位小数
		decimalFormat.setMaximumFractionDigits(2);
		return decimalFormat.format(allPayMoney);
	}

	// 确保文件存在,否则在对应路径创建文件夹
	public static void ensureDirectory(String filePath) {
		if (StringUtils.isBlank(filePath)) {
			return;
		}
		filePath = replaceSeparator(filePath);
		if (filePath.indexOf("/") != -1) {
			filePath = filePath.substring(0, filePath.lastIndexOf("/"));
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	// 避免同一个路径出现两个或三种不同的分隔符
	public static String replaceSeparator(String str) {
		return str.replace("\\", "/").replace("\\\\", "/");
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		try {
			taskRegistrar.addTriggerTask(dodownload(), getTrigger());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Trigger getTrigger() {
		return new Trigger() {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				// 触发器
				if (conf.getCron() != null && !conf.getCron().equals(""))
					cron = conf.getCron();
				CronTrigger trigger = new CronTrigger(cron);
				return trigger.nextExecutionTime(triggerContext);
			}
		};
	}

}
