package com.kejing.feepaid.sirsi.threads;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.kejing.feepaid.sirsi.config.SirsiFeepaidRestConf;
import com.kejing.feepaid.sirsi.dao.ISirsiFeepaidClient;
import com.kejing.feepaid.sirsi.dao.ItfeepaidDAO;
import com.kejing.feepaid.sirsi.dao.module.Tfeepaid;

import feign.Response;

@Component
public class FeePaidScheduledTasks implements SchedulingConfigurer {
	@Autowired
	protected SirsiFeepaidRestConf conf;
	@Autowired
	protected ItfeepaidDAO tfeepaidDao;
	@Autowired
	protected ISirsiFeepaidClient sirsiFeepaidClient;

	private static String cron = "0 0 0 1 * ?";
	// private static String cron = "0 29/1 12 * * ?";

	private Runnable doTask() {
		return new Runnable() {
			@Override
			public void run() {
				List<Tfeepaid> list = tfeepaidDao.queryTfeepaidNotSucc(conf.getCasherid());
				for (int i = 0; i < list.size(); i++) {
					Tfeepaid feepaid = list.get(i);
					HashMap<String, String> reqParams = new HashMap<>();
					reqParams.put("login", conf.getUsername());
					reqParams.put("password", conf.getPassword());
					Response resp = sirsiFeepaidClient.getSession(reqParams);
					String token = null;
					if (resp.status() == 200)
						token = resp.body().toString();
					if (token != null) {
						reqParams.clear();
						reqParams.put("userID", feepaid.getFcertid());
						reqParams.put("userPin", feepaid.getFresv1());
						reqParams.put("billAmount", feepaid.getFfeeamount().toString());
						reqParams.put("sessionToken", token);
						resp = sirsiFeepaidClient.doFeepaid(reqParams);
						String respBody = resp.body().toString();
						if (resp.status() == 200) {
							long payStatus = 3;
							if ("NO:用户没有需要支付的账单".equals(respBody)) {
								payStatus = 2;
							}
							if ("OK".equals(respBody.toUpperCase())) {
								payStatus = 1;
							}
							feepaid.setFlastcommit(new Timestamp(new Date().getTime()));
							feepaid.setFcommittimes(feepaid.getFcommittimes() + 1);
							feepaid.setFstatus(payStatus);
							tfeepaidDao.save(feepaid);
						} else {
							feepaid.setFlastcommit(new Timestamp(new Date().getTime()));
							feepaid.setFcommittimes(feepaid.getFcommittimes() + 1);
							feepaid.setFstatus((long) 93);
							tfeepaidDao.save(feepaid);
						}

					} else {
						feepaid.setFlastcommit(new Timestamp(new Date().getTime()));
						feepaid.setFcommittimes(feepaid.getFcommittimes() + 1);
						feepaid.setFstatus((long) 92);
						tfeepaidDao.save(feepaid);
					}
				}
			}
		};
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		// TODO Auto-generated method stub
		taskRegistrar.addTriggerTask(doTask(), getTrigger());
	}

	private Trigger getTrigger() {
		return new Trigger() {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				if (conf.getCron() != null && !conf.getCron().equals(""))
					cron = conf.getCron();
				CronTrigger trigger = new CronTrigger(cron);
				return trigger.nextExecutionTime(triggerContext);
			}
		};
	}

}