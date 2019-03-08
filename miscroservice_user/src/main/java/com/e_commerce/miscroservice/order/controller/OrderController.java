package com.e_commerce.miscroservice.order.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.order.vo.DetailOrderReturnView;
import com.e_commerce.miscroservice.order.vo.PageEnrollAndChooseReturnView;
import com.e_commerce.miscroservice.order.vo.PageOrderParamView;
import com.e_commerce.miscroservice.order.vo.PageOrderReturnView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单模块
 * 包含首页列表  首页详情   报名/选人列表
 */
@RestController
@RequestMapping("/api/v1/order")
public class OrderController extends BaseController {

	/**
	 * 列出求助服务订单列表
	 *
	 * @param type          类型 1、求助 2、服务
	 * @param serviceTypeId 类型ID 如：温暖公益的ID 用于筛选 全部的id为1000000
	 * @param longitude     经度 获取不到传递0
	 * @param latitude      纬度 获取不到传递0
	 * @param pageNum       分页页数
	 * @param pageSize      每页数量
	 * @param condition     查询条件
	 * @param token         当前用户token
	 *                      {
	 *                      "success": 是否成功,
	 *                      "msg": 成功或失败的消息,
	 *                      "data": {
	 *                      "resultList": [
	 *                      {
	 *                      "user": {
	 *                      "name": "用户名称",
	 *                      "userHeadPortraitPath": "用户头像",
	 *                      "idString": "用户ID"
	 *                      },
	 *                      "order": {
	 *                      "id":订单ID
	 *                      "nameAudioUrl": "音频url",
	 *                      "serviceName": "求助服务名称",
	 *                      "servicePlace": 线上还是线下  1、线上 2、线下,
	 *                      "status": 1 进行中 2、已结束,
	 *                      "source": 1,
	 *                      "serviceTypeId": 15000,
	 *                      "enrollNum": 报名人数,
	 *                      "serveNum":需要的人数
	 *                      "confirmNum": 确定人数（确定人数等于需要报名的人数，显示已满额）,
	 *                      "collectTime": 10,
	 *                      "collectType": 收取类型 1、互助时  2、公益时,
	 *                      "isValid": "1"
	 *                      },
	 *                      "imgUrl": 封面图url   没有则不会返回这个字段,
	 *                      "orderIdString": 订单ID
	 *                      }
	 *                      ],
	 *                      "totalCount": 总记录数
	 *                      }
	 *                      }
	 * @return
	 */
	@PostMapping("/list")
	@Consume(PageOrderParamView.class)
	public Object list(Integer type, Integer serviceTypeId, double longitude, double latitude, Integer pageNum,
					   Integer pageSize, String condition, String token) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		PageOrderParamView param = (PageOrderParamView) ConsumeHelper.getObj();
		try {
			QueryResult<PageOrderReturnView> list = orderService.list(param, user);
			result.setData(list);
			result.setSuccess(true);
			result.setMsg("查询成功");
		} catch (MessageException e) {
			logger.warn("查询服务列表失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败," + e.getMessage());
		} catch (Exception e) {
			logger.error("查询服务列表失败" + errInfo(e), e);
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败");
		}
		return result;
	}

	/**
	 * 首页展示点击进入的详情
	 *
	 * @param orderId 订单ID
	 * @param token   用户token
	 *                {
	 *     "success": 是否成功,
	 *     "msg": "成功失败的消息",
	 *     "data": {
	 *         "order": {
	 *             "id": 订单ID,
	 *             "serviceId": 商品ID,
	 *             "nameAudioUrl": "音频url",
	 *             "serviceName": "名称",
	 *             "servicePlace": 1、线上  2、线下,
	 *             "labels": "标签",
	 *             "type": 1、求助 2、服务,
	 *             "source": 来源 1、个人  2、组织,
	 *             "startTime": 开始时间,
	 *             "endTime": 结束时间,
	 *             "collectTime": 收取时间币,
	 *             "collectType": 收取类型 1、互助时  2、公益时,
	 *         },
	 *         "user": {
	 *             "id": 发布人id,
	 *             "name": "发布人名称",
	 *         },
	 *         "listServiceDescribe": [
	 *             {
	 *                 "sort": 图片排序,
	 *                 "depict": "详情内容",
	 *                 "url": "详情url",
	 *                 "isCover": "是否是封面图",
	 *             }
	 *         ],
	 *         "careStatus": 是否关注  true 已关注  false 未关注,
	 *         "serviceIdString": "101675590041468928",
	 *         "serviceUserIdString": "68813260748488704"
	 *     }
	 * }
	 * @return
	 */
	@PostMapping("/detail")
	public Object detail(Long orderId, String token) {
		TUser user = (TUser) redisUtil.get(token);
		AjaxResult result = new AjaxResult();
		try {
			DetailOrderReturnView data = orderService.orderDetail(orderId, user);
			result.setSuccess(true);
			result.setData(data);
			result.setMsg("获取详情成功");
		} catch (MessageException e) {
			logger.warn("获取详情失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("获取详情失败," + e.getMessage());
		} catch (Exception e) {
			logger.error("获取详情失败" + errInfo(e), e);
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("获取详情失败");
		}
		return result;
	}

	/**
	 * 报名选人列表
	 *
	 * @param token    当前用户token
	 * @param pageNum  分页页数
	 * @param pageSize 每页数量
	 *                 {
	 *                 "success": 是否成功,
	 *                 "msg": "成功或失败的消息",
	 *                 "data": [
	 *                 {
	 *                 "order": {
	 *                 "id": 订单ID,
	 *                 "serviceId": 服务ID,
	 *                 "serviceName": "服务名称",
	 *                 "servicePlace": 1、线上 2、线下,
	 *                 "labels": "标签",
	 *                 "type": 类型 1、求助 2、服务,
	 *                 "source": 来源 1、个人  2、组织,
	 *                 "startTime": 1552526400000,
	 *                 "endTime": 1552527600000,
	 *                 "collectTime": 10,
	 *                 "collectType": 1、 互助时  2、公益时,
	 *                 },
	 *                 "status": "显示状态"
	 *                 }
	 *                 ]
	 *                 }
	 * @return
	 */
	@PostMapping("/enrollList")
	public Object enrollList(String token, Integer pageNum, Integer pageSize) {
		TUser user = (TUser) redisUtil.get(token);
		AjaxResult result = new AjaxResult();
		try {
			List<PageEnrollAndChooseReturnView> data = orderService.enrollList(pageNum, pageSize, user);
			result.setSuccess(true);
			result.setData(data);
			result.setMsg("获取报名列表成功");
		} catch (MessageException e) {
			logger.warn("获取报名选人列表失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("获取报名选人列表失败," + e.getMessage());
		} catch (Exception e) {
			logger.error("获取报名选人列表失败" + errInfo(e), e);
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("获取报名选人列表失败");
		}
		return result;
	}


}
