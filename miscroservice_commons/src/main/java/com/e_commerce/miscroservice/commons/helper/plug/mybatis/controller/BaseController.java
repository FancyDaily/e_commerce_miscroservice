package com.e_commerce.miscroservice.commons.helper.plug.mybatis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	public HttpServletRequest request;
	@Autowired
	public HttpServletResponse response;
	@Autowired
	public HttpSession session;

}
