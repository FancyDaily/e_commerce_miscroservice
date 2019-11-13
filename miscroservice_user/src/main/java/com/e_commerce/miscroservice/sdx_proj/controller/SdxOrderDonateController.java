package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookInfoService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookOrderService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public Object donateGotoList() {
		return null;
	}

	/**
	 * 根据ISBN码获取书本信息
	 * @param isbnCode ISBN码
	 * @return
	 */
	@RequestMapping("getBookInfo")
	public Object getBookInfo(String isbnCode) {
		return Response.success(sdxBookInfoService.getBookInfo(isbnCode));
	}

	/**
	 * 创建或者修改捐书订单
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
	public Response modTSdxBookOrder(@RequestParam(required = false) Long id, @RequestParam(required = false) Integer type, @RequestParam(required = false) Double price, @RequestParam(required = false) Integer status, @RequestParam(required = false) String bookIds, @RequestParam(required = false) Integer shipType, @RequestParam(required = false) Double bookPrice, @RequestParam(required = false) Double shipPirce, @RequestParam(required = false) Double totalPrice, @RequestParam(required = false) Long bookInfoIds, @RequestParam(required = false) Long bookStationId, @RequestParam(required = false) Integer scoreDiscount, @RequestParam(required = false) Integer exactTotalScores, @RequestParam(required = false) Long shippingAddressId, @RequestParam(required = false) Integer expectedTotalScores) {
//		type =
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
	public Object list(Integer option, Integer pageNum, Integer pageSize) {
		return Response.success(sdxBookOrderService.donateList(IdUtil.getId(), option, pageNum, pageSize));
	}

}
