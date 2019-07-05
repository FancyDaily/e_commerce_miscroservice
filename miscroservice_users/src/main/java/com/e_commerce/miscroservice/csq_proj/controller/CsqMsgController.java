package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.vo.CsqSysMsgVo;
import com.e_commerce.miscroservice.csq_proj.po.TCsqSysMsg;
import com.e_commerce.miscroservice.csq_proj.service.CsqMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 从善桥消息
 *
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:38
 */
@RequestMapping("csq/msg")
@RestController
@Log
public class CsqMsgController {

	@Autowired
	private CsqMsgService csqMsgService;

	/**
	 * 消息列表
	 *
	 * @param pageNum  页码
	 * @param pageSize 大小
	 *                 <p>
	 *                 {
	 *                 "resultList":[
	 *                 {
	 *                 "id":8,
	 *                 "userId":2000,
	 *                 "dateString":"2019/07/03",
	 *                 "serviceId":14, //项目编号
	 *                 "name":"测试标题",		//项目名
	 *                 "description":"测试发布项目",	//项目描述
	 *                 "coverPic":"",	//封面图
	 *                 "sumTotalIn":0,	//总计捐入
	 *                 "title":"您被警方正式通缉",	//消息标题
	 *                 "content":"您因为太过mean而被警方通报",	//消息内容
	 *                 "type":2,	//消息类型
	 *                 "isRead":1	//是否已读
	 *                 }
	 *                 }
	 * @return
	 */
	@RequestMapping("list")
	public AjaxResult msgList(Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getTestId();
		try {
			log.info("消息列表, userId={}, pageNum={}, pageSize={}", userId, pageNum, pageSize);
			QueryResult<CsqSysMsgVo> list = csqMsgService.list(userId, pageNum, pageSize);
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
	 * <p>
	 * {
	 * "success": true,
	 * "errorCode": "",
	 * "msg": "",
	 * "data": 1	//数量
	 * }
	 *
	 * @return
	 */
	@RequestMapping("unreadCnt")
	public AjaxResult unreadCnt() {
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
	 * <p>
	 * {
	 * "success": true,
	 * "errorCode": "",
	 * "msg": "",
	 * "data": ""
	 * }
	 *
	 * @return
	 */
	@RequestMapping("readAll")
	public AjaxResult readAll() {
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

	/**
	 * 手动推送系统消息
	 *
	 * @param userId    用户编号
	 * @param title     标题
	 * @param content   内容
	 * @param serviceId 项目编号
	 * @param type      类型
	 *                  <p>
	 *                  {
	 *                  "success": true,
	 *                  "errorCode": "",
	 *                  "msg": "",
	 *                  "data": ""
	 *                  }
	 * @return
	 */
	@Consume(CsqSysMsgVo.class)
	@RequestMapping("insert")
	public AjaxResult insert(Long userId, String title, String content, Long serviceId, Integer type) {
		AjaxResult result = new AjaxResult();
		Long operatorId = UserUtil.getTestId();
		CsqSysMsgVo vo = (CsqSysMsgVo) ConsumeHelper.getObj();
		TCsqSysMsg tCsqSysMsg = vo.copyTCsqSysMsg();
		try {
			log.info("手动推送系统消息, userId={}, title={}. content={}, serviceId={}, type={}", userId, title, content, serviceId, type);
			csqMsgService.insert(userId, tCsqSysMsg);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "手动推送系统消息", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("手动推送系统消息", e);
			result.setSuccess(false);
		}
		return result;
	}


}
