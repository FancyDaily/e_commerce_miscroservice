package com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/wxAppPay")
public class WxPayAppController {

	@Autowired
	WeChatPay weChatPay;



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
