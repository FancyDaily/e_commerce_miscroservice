package com.e_commerce.miscroservice.product.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.product.vo.ProductSubmitParamView;
import com.e_commerce.miscroservice.product.vo.ServiceParamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 求助服务
 * 求助服务controller层
 */
@RestController
@RequestMapping("/api/v1/seekHelp")
public class SeekHelpController extends BaseController {

	Log logger = Log.getInstance(SeekHelpController.class);

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 功能描述:发布求助
	 *
	 * @return
	 * @author 马晓晨
	 * @date 2019/3/4 10:29
	 * @params
	 */
	@RequestMapping("/submit")
	@Consume(ProductSubmitParamView.class)
	public Object submitSeekHelp(HttpServletRequest request, @RequestBody ServiceParamView param, String token) {
		AjaxResult result = new AjaxResult();
		//从拦截器中获取参数的String
/*		String paramString = (String) request.getAttribute("paramString");
		request.removeAttribute("paramString");
		ServiceParamView param = JsonUtil.parseFromJson(paramString, ServiceParamView.class);
		String token = param.getToken();*/

		TUser user = (TUser) redisUtil.get(token);
		//这一层可判断出是求助，手动设置type参数
		param.getService().setType(ProductEnum.TYPE_SEEK_HELP.getValue());
		try {
			productService.submitSeekHelp(user, param, token);
			result.setSuccess(true);
			result.setMsg("发布求助成功");
			return result;
		} catch (MessageException e) {
			logger.warn("发布求助失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg("发布求助失败," + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error("发布求助失败" + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("发布求助失败");
			return result;
		}

	}
}
