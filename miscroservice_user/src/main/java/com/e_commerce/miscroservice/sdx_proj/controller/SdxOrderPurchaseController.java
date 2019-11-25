package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookInfoService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookOrderService;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxPurchaseOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 书袋熊购书订单
 *
 * @Author: FangyiXu
 * @Date: 2019-10-25 16:36
 */
@RestController
@Log
@RequestMapping("sdx/order/purchase")
public class SdxOrderPurchaseController {

	@Autowired
	SdxBookInfoService sdxBookInfoService;
	@Autowired
	SdxBookOrderService sdxBookOrderService;

	/**
	 * 下单(微信支付)
	 *
	 * @param shippingAddressId 邮寄地址编号
	 * @param bookInfoIds       书籍信息编号
	 * @param scoreUsed         使用积分
	 * @param bookFee           最终价格
	 * @return
	 */
	@RequestMapping("pre/order")
	@UrlAuth
	public Object preOrder(Long shippingAddressId, String bookInfoIds, Integer scoreUsed, Double bookFee, HttpServletRequest httpServletRequest, Double shipFee) throws Exception {
		return Response.success(sdxBookOrderService.preOrder(shippingAddressId, bookInfoIds, bookFee, IdUtil.getId(), httpServletRequest, shipFee, scoreUsed));
	}

	/**
	 * 确认收货
	 *
	 * @param orderId 订单编号
	 * @return
	 */
	@RequestMapping("receipt/confirm")
	@UrlAuth
	public Object confirmReceipt(Long orderId) {
		sdxBookOrderService.confirmReceipt(orderId);
		return Response.success();
	}

	/**
	 * 订单详情
	 *
	 * @param orderId 订单编号
	 * @return
	 */
	@RequestMapping("detail")
	@UrlAuth
	public Object orderDetail(Long orderId) {
		SdxPurchaseOrderVo detail = sdxBookOrderService.detail(orderId);
		return Response.success(detail);
	}

	/**
	 * 订单列表
	 *
	 * @param option 操作(表示全部用-2
	 * @param pageNum 页码
	 * @param pageSize 大小
	 * @return
	 */
	@RequestMapping("list")
	@UrlAuth
	public Object list(Integer option, Integer pageNum, Integer pageSize) {
		Long userIds = IdUtil.getId();
		return Response.success(sdxBookOrderService.purchaseList(userIds, option, pageNum, pageSize));
	}

}
