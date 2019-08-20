package com.e_commerce.miscroservice.csq_proj.demos;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Check;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo
 * @Author: FangyiXu
 * @Date: 2019-08-16 09:49
 */
@RestController
@Log
@RequestMapping("demo/checker")
public class Annotion_Check {

	/**
	 * check注解
	 * @param code
	 * @param age
	 * @return
	 */
	@RequestMapping("checkAuth")
	public AjaxResult checkAuth(@Check("==null") String code, @Check("(>10 || <20 ) && !=40") Integer age) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("check注解，code={}");
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "check注解", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("check注解", e);
			result.setSuccess(false);
		}
		return result;
	}
}
