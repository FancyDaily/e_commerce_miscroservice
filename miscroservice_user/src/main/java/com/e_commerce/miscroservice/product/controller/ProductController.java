package com.e_commerce.miscroservice.product.controller;

import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 求助服务
 * 求助服务controller层
 */
@RestController
@RequestMapping("/api/v1/seekHelp")
public class ProductController  {

	@Autowired
	private ProductService productService;

	private RedisUtil redisUtil;
	/**
	 * 功能描述:发布求助
	 * @author 马晓晨
	 * @date 2019/3/4 10:29
	 * @params 
	 * @return 
	 */

	@PostMapping("/submit")
	public Object submitSeekHelp(String token, HttpServletRequest request, Integer type, Integer source, Long serviceTypeId, String serviceName, Integer servicePlace, String labels, Integer servicePersonnel,
								 Long startTime, Long endTime, Integer timeType, String dateWeek, String addressName, Double longitude, Double latitude, Double radius,
								 Integer collectType, Long collectTime, String nameAudioUrl, Long companyId, Integer openAuth, String serviceDescJson) {
		AjaxResult result = new AjaxResult();
		//从拦截器中获取参数的String
//		String paramString = (String)request.getAttribute("paramString");
//		request.removeAttribute("paramString");
//		ServiceParamView param = JsonUtil.parseFromJson(paramString, ServiceParamView.class);

//		TUser user = (TUser) redisUtil.get(token);
		try {
//			seekHelpService.submitSeekHelp(user, param, token);
			result.setSuccess(true);
			result.setMsg("发布求助成功");
			return result;
		} catch (MessageException e) {
//			logger.error("发布求助失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg("发布求助失败," + e.getMessage());
			return result;
		} catch (Exception e) {
//			logger.error("发布求助失败" + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("发布求助失败");
			return result;
		}
	}

}
