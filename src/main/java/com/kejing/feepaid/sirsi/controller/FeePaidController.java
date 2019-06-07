package com.kejing.feepaid.sirsi.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kejing.feepaid.sirsi.controller.base.BaseController;
import com.kejing.feepaid.sirsi.exception.AuditException;
import com.kejing.feepaid.sirsi.utils.DownLoadResourceFile;
import com.kejing.shuyang.kjsip34j.messages.KJFeePaid;
import com.kejing.shuyang.kjsip34j.messages.KJFeePaidResponse;

@RestController
public class FeePaidController extends BaseController {
	@Resource
	private ResourceLoader resourceLoader;

	@RequestMapping(value = "api/circulation/feepaid", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public KJFeePaidResponse doFeePaid(@RequestBody KJFeePaid input) {
		try {
			return feePaidManager.doFeePaid(input);
		} catch (AuditException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	// 用户下载账单
	@RequestMapping(value = "kjplatform/api/{dz}/{date}", method = RequestMethod.GET)
	public void download(HttpServletResponse response, @PathVariable String dz, @PathVariable String date)
			throws Exception {
		DownLoadResourceFile.downloadFile(response, date, dz);
		// DownLoad.setPath(response, dz, date);
	}

}
