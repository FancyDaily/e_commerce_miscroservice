package com.e_commerce.miscroservice.lpglxt_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserAuthVo;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglPositionDao;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 楼盘管理Controller
 *
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:33
 */
@RequestMapping("lpgl/manager")
@RestController
@Log
public class LpglManagerController {

	@Autowired
	private LpglPositionService lpglPositionService;

	@RequestMapping("isAlive")
	public Object isAlive() {
		return 11111;
	}

	/**
	 * 设定职位的优惠额度
	 * @return
	 */
	@RequestMapping("position/discountCredit/modify")
	public Object setDiscountCredit(Long positionId, Double discountCredit) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("设定职位的优惠额度, positionId={}, discountCredit={}", positionId, discountCredit);
			lpglPositionService.modify(positionId, discountCredit);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "设定职位的优惠额度", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("设定职位的优惠额度", e);
			result.setSuccess(false);
		}
		return result;
	}

}
