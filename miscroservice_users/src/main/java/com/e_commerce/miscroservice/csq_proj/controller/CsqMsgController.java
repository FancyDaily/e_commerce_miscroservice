package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
import com.e_commerce.miscroservice.csq_proj.po.TCsqSysMsg;
import com.e_commerce.miscroservice.csq_proj.service.CsqMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 从善桥消息
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:38
 */
@RequestMapping("msg")
@RestController
@Log
public class CsqMsgController {

	@Autowired
	private CsqMsgService csqMsgService;

	/**
	 * 消息列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("list")
	public Object msgList(Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("消息列表, userId={}, pageNum={}, pageSize={}", userId, pageNum, pageSize);
			QueryResult<TCsqSysMsg> list = csqMsgService.list(userId, pageNum, pageSize);
			result.setData(list);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "消息列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("消息列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 未读消息数目
	 * @return
	 */
	public Object unreadCnt() {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("未读消息数目, userId={}", userId);
			int count = csqMsgService.unreadCnt(userId);
			result.setData(count);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "未读消息数目", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("未读消息数目", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 全部标为已读
	 * @return
	 */
	public Object readAll() {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("全部标为已读, userId={}", userId);
			csqMsgService.readAll(userId);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "全部标为已读", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("全部标为已读", e);
			result.setSuccess(false);
		}
		return result;
	}
}
