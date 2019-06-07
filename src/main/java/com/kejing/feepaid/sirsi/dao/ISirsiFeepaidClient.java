package com.kejing.feepaid.sirsi.dao;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kejing.feepaid.sirsi.config.FromUrlEncodedClientConfiguration;

import feign.Response;

@FeignClient(url = "${sirsi.feepaid.url}",name="sirsi.feepaidserver",configuration = FromUrlEncodedClientConfiguration.class)
public interface ISirsiFeepaidClient {
	@RequestMapping(value="/login",method= RequestMethod.POST,consumes = "multipart/form-data")
	public Response  getSession(Map<String,?> request);
	@RequestMapping(value="/bill",method= RequestMethod.POST,consumes = "multipart/form-data")
	public Response doFeepaid(Map<String,?> request);
}
