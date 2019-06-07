package com.kejing.feepaid.sirsi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "sirsi.feepaid", ignoreUnknownFields = true)
@Component
public class SirsiFeepaidRestConf {
	private String url;
	private String username;
	private String password;
	private String casherid;
	private int badlimit;
	private String cron;
	
	
	
	public int getBadlimit() {
		return badlimit;
	}
	public void setBadlimit(int badlimit) {
		this.badlimit = badlimit;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCasherid() {
		return casherid;
	}
	public void setCasherid(String casherid) {
		this.casherid = casherid;
	}
	
}
