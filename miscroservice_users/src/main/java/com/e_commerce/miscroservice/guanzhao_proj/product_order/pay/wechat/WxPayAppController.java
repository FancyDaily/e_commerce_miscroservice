package com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/wxAppPay")
@Log
public class WxPayAppController {

	@Autowired
	WeChatPay weChatPay;

	/**
	 * 微信支付下单
	 * @param subjectId
	 * @param voucherId
	 * @param request
	 * @return
	 */
	@RequestMapping("preOrder/" + TokenUtil.AUTH_SUFFIX)
	public Object preOrder(String orderNo,
						   Long subjectId,
						   Long voucherId,
						   HttpServletRequest request) {
		AjaxResult result = new AjaxResult();
		Long userId = UserUtil.getId();
		try {
			log.info("微信支付下单,subjectId={},voucherId={}", subjectId, voucherId);
			String openid = weChatPay.preOrder(orderNo, subjectId, voucherId, userId);
			result.setData(openid);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("微信支付下单失败,{}", e);
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取openid
	 * @param code
	 */
	@RequestMapping("openid/get")
	public Object getOpenid(String code) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("获取openid,code={}", code);
			String openid = weChatPay.getOpenid(code);
			result.setData(openid);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("获取openid失败,{}", e);
			result.setMsg(e.getMessage());
		}
		return result;
	}

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

            }
        }



}
