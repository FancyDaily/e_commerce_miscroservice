package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookInfoService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookOrderService;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxDonateOrderVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 书袋熊捐书订单
 * @Author: FangyiXu
 * @Date: 2019-10-25 16:36
 */
@RestController
@Log
@RequestMapping("sdx/order/donate")
public class SdxOrderDonateController {

	@Autowired
	SdxBookInfoService sdxBookInfoService;

	@Autowired
	SdxBookOrderService sdxBookOrderService;

	/**
	 * 平台书籍去向列表
	 * @return
	 */
	@RequestMapping("donate/goto/list")
	@UrlAuth
	public Object donateGotoList() {
		return null;
	}

	/**
	 * 根据ISBN码获取书本信息
	 * @param isbnCode ISBN码
	 * @return
	 */
	@RequestMapping("getBookInfo")
	@UrlAuth
	public Object getBookInfo(String isbnCode) {
		Response response;
		try {
			response = Response.success(sdxBookInfoService.getBookInfo(isbnCode));
		} catch (MessageException e) {
			response = Response.errorMsg(e.getMessage());
		}
		return response;
	}

	/**
	 * 创建捐赠订单(跳过邮费支付或者自送)
	 * @param bookInfoIds 书籍信息编号(一或多个)
	 * @param serviceId 关联公益项目编号
	 * @param shipType 捐书方式(1邮寄2自送)
	 * @param shippingAddressId 邮寄地址编号（指定一个）
	 * @param bookStationId 书籍驿站编号（指定一个）
	 * @return
	 */
	@RequestMapping("donate/create")
	@UrlAuth
	public Object createDonateOrder(Integer shipType, Long shippingAddressId, Long bookStationId, Long serviceId, Long... bookInfoIds) {
		return Response.success(sdxBookOrderService.createDonateOrder(IdUtil.getId(), bookInfoIds, shipType, shippingAddressId, bookStationId, serviceId));
	}

	/**
	 * 创建捐赠订单(邮寄支付邮费)
	 * @param orderNo 订单号
	 * @param bookInfoIds 书籍信息编号(一或多个)
	 * @param serviceId 关联公益项目编号
	 * @param shippingAddressId 邮寄地址编号（指定一个）
	 * @param shipType 捐书方式1邮寄
	 * @return
	 */
	@RequestMapping("preOrder")
	@UrlAuth
	public Object preOrder(String orderNo, HttpServletRequest request, Integer shipType, Long shippingAddressId, Long bookStationId, Long serviceId, Long... bookInfoIds) {
		try {
			return Response.success(sdxBookOrderService.preDonateOrder(orderNo, IdUtil.getId(), bookInfoIds, shipType, shippingAddressId, bookStationId, serviceId, request));
		} catch (Exception e) {
			e.printStackTrace();
			return Response.errorMsg("");
		}
	}

	/**
	 * 取消捐赠订单
	 * @param orderId 订单编号
	 * @return
	 */
	@RequestMapping("donate/cancel")
	@UrlAuth
	public Object cancelBookDonateOrder(Long orderId) {
		sdxBookOrderService.cancel(orderId);
		return Response.success();
	}

	/**
	 * 确认收货/书本已寄出
	 * @param orderNo 订单编号
	 * @param status 状态
	 * @param expressNo 快递单号
	 * @return
	 */
	@Consume(SdxDonateOrderVo.class)
	@UrlAuth
	@RequestMapping("modify")
	public Object modify(String orderNo, Integer status, String expressNo) {
		SdxDonateOrderVo obj = (SdxDonateOrderVo) ConsumeHelper.getObj();
		obj.setOrderNo(orderNo);
		TSdxBookOrderPo tSdxBookOrderPo = obj.copyTSdxBookOrder();
		sdxBookOrderService.modTSdxBookOrder(tSdxBookOrderPo);
		return Response.success();
	}

	/**
	 * 修改捐书订单
	 *
	 * @param id        订单的Id,修改或者查询的需要
	 * @param type        订单类型(捐书、购书)
	 * @param price        总价
	 * @param status        状态
	 * @param bookIds        多个书本编号
	 * @param shipType        配送类型(邮寄、自送）
	 * @param bookPrice        书本总价
	 * @param shipPirce        运费
	 * @param totalPrice        订单总价
	 * @param bookInfoIds        书本信息编号
	 * @param bookStationId        书籍驿站编号
	 * @param scoreDiscount        积分抵扣总额
	 * @param exactTotalScores        实际获得积分
	 * @param shippingAddressId        邮寄地址编号
	 * @param expectedTotalScores        预计获得总积分
	 *
	 *                 code==503,代表服务器出错,请先检测参数类型是否正确
	 *                 code==500,代表参数不正确
	 *                 code==200,代表请求成功
	 *                 data==0,代表操作不成功
	 *                 data!=0,新增时候data返回的是自增id,修改的时候代表影响的数量
	 *
	 * @return
	 */
	@PostMapping("mod")
	@Consume(TSdxBookOrderVo.class)
	@UrlAuth
	public Response modTSdxBookOrder(@RequestParam(required = false) Long id, @RequestParam(required = false) Integer type, @RequestParam(required = false) Double price, @RequestParam(required = false) Integer status, @RequestParam(required = false) String bookIds, @RequestParam(required = false) Integer shipType, @RequestParam(required = false) Double bookPrice, @RequestParam(required = false) Double shipPirce, @RequestParam(required = false) Double totalPrice, @RequestParam(required = false) Long bookInfoIds, @RequestParam(required = false) Long bookStationId, @RequestParam(required = false) Integer scoreDiscount, @RequestParam(required = false) Integer exactTotalScores, @RequestParam(required = false) Long shippingAddressId, @RequestParam(required = false) Integer expectedTotalScores) {
		TSdxBookOrderVo tSdxBookOrderVo = (TSdxBookOrderVo) ConsumeHelper.getObj();
		if (tSdxBookOrderVo == null) {
			return Response.fail();
		}
		return Response.success(sdxBookOrderService.modTSdxBookOrder(tSdxBookOrderVo.copyTSdxBookOrderPo()));
	}

	/**
	 * 捐赠订单列表
	 * @param option 操作-2全部
	 * @param pageNum 页码
	 * @param pageSize 大小
	 * @return
	 */
	@RequestMapping("list")
	@UrlAuth
	public Object list(String option, Integer pageNum, Integer pageSize) {
		return Response.success(sdxBookOrderService.donateList(IdUtil.getId(), option, pageNum, pageSize));
	}

}
