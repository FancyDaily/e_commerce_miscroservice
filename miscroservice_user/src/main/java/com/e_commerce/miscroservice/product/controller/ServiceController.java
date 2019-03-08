package com.e_commerce.miscroservice.product.controller;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.JsonUtil;
import com.e_commerce.miscroservice.product.vo.ServiceParamView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 服务模块
 * 包含发布服务  下架服务  删除服务  上架服务
 */
@RestController
@RequestMapping("/api/v2/service")
public class ServiceController extends BaseController{
	Log logger = Log.getInstance(ServiceController.class);

	/**
	 * 删除服务
	 * @param token 当前用户token
	 * @param productId 商品ID
	 *                  {
	 *     "success": 是否成功,
	 *     "msg": "成功或错误消息"
	 * }
	 * @return
	 */
	@PostMapping("/delService")
	public Object delService(String token, Long productId) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		try {
			productService.del(user, productId);
			result.setSuccess(true);
			result.setMsg("删除服务成功");
			return result;
		} catch (MessageException e) {
			logger.warn("删除服务失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg("删除服务失败," + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error("删除服务失败" + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("删除服务失败");
			return result;
		}
	}

	/**
	 * 下架服务
	 * @param token 用户token
	 * @param productId 商品ID
	 *{
	 *     "success": 是否成功,
	 *     "msg": "成功或失败的消息"
	 * }
	 * @return
	 */
	@PostMapping("/lowerFrameService")
	public Object lowerFrameService(String token, Long productId) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		try {
			productService.lowerFrame(user, productId);
			result.setSuccess(true);
			result.setMsg("下架服务成功");
			return result;
		} catch (MessageException e) {
			logger.warn("下架服务失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg("下架服务失败," + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error("下架服务失败" + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("下架服务失败");
			return result;
		}
	}

	/**
	 *
	 * 功能描述:发布服务
	 * 作者:马晓晨
	 * 创建时间:2018年11月20日 下午5:44:44
	 * @return
	 */
	@PostMapping("/submit")
	public Object submitService(HttpServletRequest request) {
		AjaxResult result = new AjaxResult();
		//从拦截器中获取参数的String
		String paramString = (String)request.getAttribute("paramString");
		request.removeAttribute("paramString");
		ServiceParamView param = JsonUtil.parseFromJson(paramString, ServiceParamView.class);
		String token = param.getToken();
		TUser user = (TUser) redisUtil.get(token);
		try {
			productService.submitService(user, param, token);
			result.setSuccess(true);
			result.setMsg("发布服务成功");
			return result;
		} catch (MessageException e) {
			logger.warn("发布服务失败，" + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg("发布服务失败，" + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error("发布服务失败" + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("发布服务失败");
			return result;
		}
	}




}
