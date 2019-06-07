package com.kejing.feepaid.sirsi.controller.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.kejing.feepaid.sirsi.service.IFeePaidManager;

public class BaseController {
	@Autowired
	protected IFeePaidManager feePaidManager;

}
