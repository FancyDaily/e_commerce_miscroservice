package com.e_commerce.miscroservice.order.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.order.vo.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单模块
 *
 * 包含首页列表  首页详情   报名/选人列表
 */
@RestController
@RequestMapping("/api/v2/order")
public class OrderController extends BaseController {

	/**
	 * 组织接口：我发布的可以报名的订单列表
	 * @param token token信息
	 * {
	 *     "success": true,
	 *     "errorCode": "",
	 *     "msg": "查询成功",
	 *     "data": [
	 *         {
	 *             "id": 订单ID,
	 *             "serviceId": 求助服务ID,
	 *             "serviceName": "求助服务名称",
	 *             "servicePersonnel": 需要人数,
	 *             "type": 类型 1、求助 2、服务,
	 *             "startTime": 开始时间,
	 *             "endTime": 结束时间,
	 *             "timeType": 0 一次性 1、重复性,
	 *             "collectTime": 金额,
	 *             "collectType": 1、互助时 2、公益时,
	 *             "createUser": 发布者id,
	 *         }
	 *     ]
	 * }
	 * @return
	 */
	@PostMapping("/listGroupOrder")
	public Object listGroupOrder(String token) {
		AjaxResult result = new AjaxResult();
		TUser user = UserUtil.getUser(token);
		try {
			QueryResult<GroupChooseOrderView> data = new QueryResult<>();
			List<GroupChooseOrderView> listGroupOrder = orderService.listGroupOrder(user);
			data.setResultList(listGroupOrder);
			result.setData(data);
			result.setSuccess(true);
			result.setMsg("查询成功");
		} catch (MessageException e) {
			logger.warn("查询失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败," + e.getMessage());
		} catch (Exception e) {
			logger.error("查询失败" + errInfo(e), e);
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败");
		}
		return result;
	}

	/**
	 * 我的订单详情
	 *
	 * @param orderId 用户订单
	 * @param token   当前用户token
	 *                <p>
	 *                {
	 *                "success": 是否成功,
	 *                "msg": "成功失败的消息",
	 *                "data": {
	 *                "order": {
	 *                "id": 订单ID,
	 *                "serviceId": 商品ID,
	 *                "serviceName": "订单标题",
	 *                "servicePersonnel": 订单需要人数,
	 *                "servicePlace": 订单服务场所 1、线下 2、线上,
	 *                "labels": "标签",
	 *                "type": 1、求助  2、服务,
	 *                "source": 1、个人 2组织,
	 *                "startTime": 开始时间,
	 *                "endTime": 结束时间,
	 *                "collectTime": 收取时长,
	 *                "collectType": 收取类型 1、互助时 2、公益时,
	 *                },
	 *                "record": [
	 *                {
	 *                "content": "服务记录",
	 *                "creatTime": 服务记录时间（毫秒值）
	 *                }
	 *                ],
	 *                "listUserView": [
	 *                {
	 *                "id": 报名者ID,
	 *                "name": "报名者名称",
	 *                "userHeadPortraitPath": "报名者头像",
	 *                "pointStatus": 显示状态  待看到UI图定,
	 *                "idString": "68813258559062016"
	 *                ],
	 *                "status": "显示订单状态", 订单状态: 0、无人报名 1、待支付 2、待开始 3、待对方支付  4、待评价 5、待对方评价 6、已取消 7、被取消  8 已完成 9投诉中，
	 *                "reportAction": 举报行为 1、投诉 2、举报，
	 *                "listDesc": [
	 *                {
	 *                "depict": "服务求助详情",
	 *                "url": "图片地址",
	 *                "isCover": 是否封面图  字符串 1、是 0、不是,
	 *                }
	 *                ]
	 *                }
	 *                }
	 * @return
	 */
	@PostMapping("/detailMineOrder")
	public Object detailMineOrder(Long orderId, String token) {
		AjaxResult result = new AjaxResult();
		TUser user = UserUtil.getUser(token);
		try {
			DetailMineOrderReturnView detailMineOrder = orderService.detailMineOrder(user, orderId);
			result.setData(detailMineOrder);
			result.setSuccess(true);
			result.setMsg("查询成功");
		} catch (MessageException e) {
			logger.warn("查询失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败," + e.getMessage());
		} catch (Exception e) {
			logger.error("查询失败" + errInfo(e), e);
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败");
		}
		return result;
	}


	/**
	 * 订单列表
	 *
	 * @param pageNum  分页页数
	 * @param pageSize 每页数量
	 * @param token    当前用户token
	 *                 <p>
	 *                 {
	 *                 "success": true,
	 *                 "msg": "查询成功",
	 *                 "data": {
	 *                 "resultList": [
	 *                 {
	 *                 "order": {
	 *                 "id": 订单ID,
	 *                 "serviceName": "名称",
	 *                 "servicePersonnel": 需要人数,
	 *                 "servicePlace": 1、线上 2、线下,
	 *                 "labels": "标签",
	 *                 "type": 类型 1、求助 2、服务,
	 *                 "source": 来源 1、个人 2、组织,
	 *                 "startTime": 1552526400000,
	 *                 "endTime": 1552527600000,
	 *                 "collectTime": 收取时长,
	 *                 "collectType": 收取类型  1、互助时 2、公益时,
	 *                 },
	 *                 "imgUrl": "封面图",
	 *                 "status": 订单状态: 0、无人报名 1、待支付 2、待开始 3、待对方支付  4、待评价 5、待对方评价 6、已取消 7、被取消  8 已完成 9投诉中，
	 *                 "orderIdString": "101675590041468928"
	 *                 }
	 *                 ],
	 *                 "totalCount": 2
	 *                 }
	 *                 }
	 * @return
	 */
	@RequestMapping("/listMineOrder")
	public Object listMineOrder(Integer pageNum, Integer pageSize, String token) {
		AjaxResult result = new AjaxResult();
		TUser user = UserUtil.getUser(token);
		try {
			QueryResult<PageOrderReturnView> list = orderService.listMineOrder(pageNum, pageSize, user);
			result.setData(list);
			result.setSuccess(true);
			result.setMsg("查询成功");
		} catch (MessageException e) {
			logger.warn("查询失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败," + e.getMessage());
		}
		return result;
	}


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
	 *                      <p>
	 *                      {
	 *                      "success": 是否成功,
	 *                      "msg": 成功或失败的消息,
	 *                      "data": {
	 *                      "resultList": [
	 *                      {
	 *                      "user": {
	 *                      "id": 用户ID,
	 *                      "name": "用户名称",
	 *                      "userHeadPortraitPath": "用户头像",
	 *                      authenticationStatus;1、未实名  2、已实名
	 *                      "userType":用户类型 1、个人 2、公益组织 3、一般组织
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
	 *                      "servicePersonnel":需要的人数
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
	public Object listOrder(Integer type, Integer serviceTypeId, double longitude, double latitude, Integer pageNum,
							Integer pageSize, String condition, String token) {
		AjaxResult result = new AjaxResult();
		TUser user = UserUtil.getUser(token);
		PageOrderParamView param = (PageOrderParamView) ConsumeHelper.getObj();
		try {
			QueryResult<PageOrderReturnView> list = orderService.list(param, user);
			result.setData(list);
			result.setSuccess(true);
			result.setMsg("查询成功");
		} catch (MessageException e) {
			logger.warn("查询失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败," + e.getMessage());
		}
		return result;
	}

	/**
	 * 首页展示点击进入的详情
	 *
	 * @param orderId 订单ID
	 * @param token   用户token
	 *                <p>
	 *                {
	 *                "success": 是否成功,
	 *                "msg": "成功失败的消息",
	 *                "data": {
	 *                "order": {
	 *                "id": 订单ID,
	 *                "serviceId": 商品ID,
	 *                "nameAudioUrl": "音频url",
	 *                "serviceName": "名称",
	 *                "serviceStatus"：3、4 已下架  5、已删除
	 *                "status": 2、已结束,
	 *                "servicePlace": 1、线上  2、线下,
	 *                "labels": "标签",
	 *                "servicePersonnel":"需要人数"，
	 *                "type": 1、求助 2、服务,
	 *                "source": 来源 1、个人  2、组织,
	 *                "startTime": 开始时间,
	 *                "endTime": 结束时间,
	 *                "collectTime": 收取时间币,
	 *                "collectType": 收取类型 1、互助时  2、公益时,
	 *                "createUser": 发布者ID,
	 *                },
	 *                "user": {
	 *                "id": 发布人id,
	 *                "name": "发布人名称",
	 *                "careStatus": 关注状态 1、显示关注 2、显示已关注
	 *                },,
	 *                "orderRelationship": {
	 *                "serviceReportType": "0、未投诉  1、已投诉  2、被投诉 3、已解决",
	 *                "status": 0、3、 4、 5、 6、可报名,  其他：已报名
	 *                "serviceCollectionType": 0、2 未收藏  1、已收藏,
	 *                },
	 *                "listServiceDescribe": [
	 *                {
	 *                "sort": 图片排序,
	 *                "depict": "详情内容",
	 *                "url": "详情url",
	 *                "isCover": "是否是封面图",
	 *                }
	 *                ],
	 *                "serviceIdString": "101675590041468928",
	 *                "serviceUserIdString": "68813260748488704"
	 *                }
	 *                }
	 * @return
	 */
	@RequestMapping("/detail")
	public Object detailIndexOrder(Long orderId, String token) {
		TUser user = UserUtil.getUser(token);
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
	 *                 <p>
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
	 *                 "servicePersonnel"：需要人数,
	 *                 "type": 类型 1、求助 2、服务,
	 *                 "source": 来源 1、个人  2、组织,
	 *                 "startTime": 1552526400000,
	 *                 "endTime": 1552527600000,
	 *                 "collectTime": 10,
	 *                 "collectType": 1、 互助时  2、公益时,
	 *                 },
	 *                 "status": "显示状态",
	 *                 "porductCoverPic":封面图
	 *                 }
	 *                 ],
	 *                 "totalCount": 总条数
	 *                 }
	 * @return
	 */
	@PostMapping("/enrollList")
	public Object enrollList(String token, Integer pageNum, Integer pageSize) {
		TUser user = UserUtil.getUser(token);
		AjaxResult result = new AjaxResult();
		try {
			QueryResult<PageEnrollAndChooseReturnView> data = orderService.enrollList(pageNum, pageSize, user);
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

	/**
	 * 我的选人列表 --> 组织专属
	 *
	 * @param token    用户token
	 * @param pageNum  页数
	 * @param pageSize 每页数量
	 *                 {
	 *                 "success": true,
	 *                 "errorCode": "",
	 *                 "msg": "获取报名列表成功",
	 *                 "data": {
	 *                 "resultList": [
	 *                 {
	 *                 "order": {
	 *                 "id": 订单ID,
	 *                 "nameAudioUrl": "音频地址",
	 *                 "serviceName": "求助服务名称",
	 *                 "servicePersonnel": 要求人数,
	 *                 "servicePlace": 1、线上 2、线下,
	 *                 "labels": "标签",
	 *                 "type": 类型 1、求助 2、服务,
	 *                 "source": 来源 1、个人 2、组织,
	 *                 "addressName": 地址名称,
	 *                 "longitude": 经度,
	 *                 "latitude": 纬度,
	 *                 "enrollNum": 报名人数,
	 *                 "confirmNum": 确认人数,
	 *                 "startTime": 开始时间,
	 *                 "endTime": 结束时间,
	 *                 "timeType": 0 一次性 1、可重复,
	 *                 "collectTime": 收取时间,
	 *                 "collectType": 收取分类 1、互助时 2、公益时,
	 *                 "createUser": 创建人ID,
	 *                 },
	 *                 "porductCoverPic": 封面图地址,
	 *                 "status": 状态： 1、已结束 2、已取消 3、待选人
	 *                 }
	 *                 ],
	 *                 "totalCount": 总条数
	 *                 }
	 *                 }
	 * @return
	 */
	@PostMapping("/mineChooseList")
	public Object mineChooseList(String token, Integer pageNum, Integer pageSize) {
		TUser user = UserUtil.getUser(token);
		AjaxResult result = new AjaxResult();
		try {
			QueryResult<PageEnrollAndChooseReturnView> data = orderService.mineChooseList(pageNum, pageSize, user);
			result.setSuccess(true);
			result.setData(data);
			result.setMsg("获取报名列表成功");
		} catch (MessageException e) {
			logger.warn("查询失败," + e.getMessage());
			result.setSuccess(false);
			result.setMsg("查询失败," + e.getMessage());
		} catch (Exception e) {
			logger.error("获取报名列表成功" + errInfo(e), e);
			result.setSuccess(false);
			result.setMsg("获取报名列表失败");
		}
		return result;
	}

	/**
	 * 选人详情
	 *
	 * @param token   用户token
	 * @param orderId 订单ID
	 *                {
	 *                "success": 是否成功,
	 *                "errorCode": "",
	 *                "msg": "获取报名列表成功",
	 *                "data": {
	 *                "order": {
	 *                "nameAudioUrl": "音频地址",
	 *                "serviceName": "名称",
	 *                "servicePersonnel": 要求人数,
	 *                "servicePlace": 平台 1、线上 2、线下,
	 *                "labels": "hehe,haha",
	 *                "type": 1,
	 *                "status": 1,
	 *                "source": 1,
	 *                "serviceTypeId": 15000,
	 *                "addressName": "地址",
	 *                "longitude": "经度",
	 *                "latitude": "纬度",
	 *                "startTime": 开始时间,
	 *                "endTime": 结束时间,
	 *                "collectTime": 收取时长,
	 *                "collectType": 收取分类 1、互助时  2、公益时,
	 *                },
	 *                "listUser": [  "报名者列表"
	 *                {
	 *                "id": 68813259653775360,
	 *                "name": "左岸",
	 *                "userHeadPortraitPath": 头像地址,
	 *                "careStatus": 关注状态 1、显示关注 2、显示已关注,
	 *                }
	 *                ]
	 *                }
	 *                }
	 * @return
	 */
	@PostMapping("/chooseDetail")
	public Object chooseDetail(String token, Long orderId) {
		TUser user = UserUtil.getUser(token);
		AjaxResult result = new AjaxResult();
		try {
			DetailChooseReturnView data = orderService.chooseDetail(orderId, user);
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


}
