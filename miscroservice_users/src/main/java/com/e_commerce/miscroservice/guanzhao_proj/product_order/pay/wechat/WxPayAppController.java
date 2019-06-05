package com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@RestController
@RequestMapping("/wxPay")
@Log
public class WxPayAppController {

	@Autowired
	private  WeChatPay weChatPay;

	@Autowired
	private GZPayService gzPayService;


	/**
	 * 回调函数
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/wxNotify")
	public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//解析参数
		Map param = weChatPay.doParseRquest(request);
		if ("SUCCESS".equals(param.get("return_code"))) {
			// 如果返回成功
			String mch_id = (String) param.get("mch_id"); // 商户号
			String out_trade_no = (String) param.get("out_trade_no"); // 商户订单号
			String total_fee = (String) param.get("total_fee");
			// 查询订单 根据订单号查询订单
			System.out.println("商户号" + mch_id + "out_trade_no" + out_trade_no + "total_fee" + total_fee);

			//支付成功业务流程
			gzPayService.dealWithOrderNo(out_trade_no);
		}
	}


	@RequestMapping("/pay")
	public Map<String, String> getPaySign(@RequestParam(required = false) Long subjectId,
										  @RequestParam(required = false) Long voucherId,
										  HttpServletRequest request) throws Exception {
		return weChatPay.createWebParam(System.currentTimeMillis() + "", 0.01, request);


	}

	@RequestMapping("pay/" + TokenUtil.AUTH_SUFFIX)
	public AjaxResult getPaySignAuth(@RequestParam(required = false) String orderNo,
									 @RequestParam(required = true) Long subjectId,
									 @RequestParam(required = false) Long voucherId,
									 HttpServletRequest request) throws Exception {
		Long id = UserUtil.getId();
		log.info("微信公共号支付,orderNo={}, subjectId={}, voucherId={}", orderNo, subjectId, voucherId);
		AjaxResult result = new AjaxResult();
		try {
			Map<String, Object> map = gzPayService.produceOrder(subjectId, orderNo, voucherId, id, Boolean.TRUE);
			orderNo = (String) map.get("orderNo");
			double payMoney = (double) map.get("couponMoney");
			Map<String, String> webParam = weChatPay.createWebParam(orderNo, payMoney, request);
			result.setData(webParam);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("微信支付{}", e);
			result.setMsg(e.getMessage());
		} catch (Exception e) {
			log.error("微信支付{}", e);
		}
		return result;
	}

	@RequestMapping("test")
	public void test(String orderNo, Long subjectId, Long voucherId) {
		Long id = 1150l;
		log.info("微信公共号支付,orderNo={}, subjectId={}, voucherId={}", orderNo, subjectId, voucherId);
		try {
			Map<String, Object> map = gzPayService.produceOrder(subjectId, orderNo, voucherId, id, Boolean.TRUE);
		} catch (MessageException e) {
			log.warn("微信支付{}", e);
		} catch (Exception e) {
			log.error("微信支付{}", e);
		}
	}

}
