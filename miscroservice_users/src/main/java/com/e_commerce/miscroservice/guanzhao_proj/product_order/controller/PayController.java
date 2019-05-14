package com.e_commerce.miscroservice.guanzhao_proj.product_order.controller;

import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AliPayPo;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.Iptools;
import com.e_commerce.miscroservice.commons.util.colligate.pay.MD5Util;
import com.e_commerce.miscroservice.commons.util.colligate.pay.QRCodeUtil;
import com.e_commerce.miscroservice.commons.util.colligate.pay.WXMyConfigUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付
 */
@RestController
@RequestMapping("pay")
public class PayController {

    private Log logger = Log.getInstance(PayController.class);

    @Autowired
    private GZPayService gzPayService;

    /**
     * 支付宝app支付
     */
    @RequestMapping("/alipay/app")
    public Object alipay( AliPayPo payPo) {
        AjaxResult result = new AjaxResult();

        try {
            gzPayService.appTrade(payPo);

        }catch (MessageException e){
            logger.warn("支付宝app支付失败={}",e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }

        return result;
    }

    /**
     * 支付宝app支付-异步通知
     * @param request
     */
    @PostMapping("/alipay/app/notify")
    public void alipayNotify(HttpServletRequest request) {
        logger.info("支付宝app支付-异步通知");
        Enumeration<String> paramsName = request.getParameterNames();
        String paramName = paramsName.nextElement();
        while (!StringUtils.isEmpty(paramName)) {
            logger.info(request.getParameter(paramName));
        }
    }

    /**
     * 支付宝当面付-二维码-预下单
     * @param payPo
     * @return
     */
    @RequestMapping("/alipay/qr_code/pre")
    public Object qrCodePre(AliPayPo payPo) {
        logger.info("支付宝当面付-二维码-预下单={}",payPo);

        AjaxResult ajaxResult = new AjaxResult();

        try {
            ajaxResult.setSuccess(true);
            ajaxResult.setData(payPo);
            gzPayService.qrCodeTradePre(payPo);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
        }

        return ajaxResult;
    }

    /**
     * 支付宝当面付-二维码-支付结果异步通知
     * @param request
     */
    @PostMapping("/alipay/qr_code/notify")
    public void alipayQrCodeNotify(HttpServletRequest request) {
        logger.info("支付宝当面付-二维码-支付结果异步通知");
        Enumeration<String> paramsName = request.getParameterNames();
        String paramName = paramsName.nextElement();
        while (!StringUtils.isEmpty(paramName)) {
            logger.info(request.getParameter(paramName));
        }
    }



    @RequestMapping(value = "/wx")
    public Object orderPay(@RequestParam(required = true,value = "user_id")String user_id,
                           @RequestParam(required = true,value = "coupon_id")String coupon_id,
                           @RequestParam(required = true,value = "subjectId")Long subjectId,
                           @RequestParam(required = true,value = "subjectName")String subjectName,
                           @RequestParam(required = true,value = "out_trade_no")String out_trade_no,
                           @RequestParam(required = true,value = "total_fee")String total_fee,
                           HttpServletRequest req, HttpServletResponse response) throws Exception {
        logger.info("进入微信支付申请");
        AjaxResult ajaxResult = new AjaxResult();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//可以方便地修改日期格式
        String hehe = dateFormat.format(now);

//        String out_trade_no=hehe+"wxpay";  //777777 需要前端给的参数
//        String total_fee="1";              //7777777  微信支付钱的单位为分
//        String user_id="1";               //77777
//        String coupon_id="7";               //777777

        String attach=user_id+","+coupon_id;
        WXMyConfigUtil config = new WXMyConfigUtil();
        String spbill_create_ip = Iptools.gainRealIp(req);
//        String spbill_create_ip="10.4.21.78";
        logger.info(spbill_create_ip);
        Map<String,String> result = gzPayService.dounifiedOrder(attach,out_trade_no,total_fee,spbill_create_ip,1,subjectId,subjectName);
        if (result.get("result_code").equals("FAIL")){

            ajaxResult.setMsg(result.get("err_code_des"));
            ajaxResult.setSuccess(false);
            return ajaxResult;
        }
        String nonce_str = (String)result.get("nonce_str");
        String prepay_id = (String)result.get("prepay_id");
        Long time =System.currentTimeMillis()/1000;
        String timestamp=time.toString();

        //签名生成算法
        Map<String,String> map = new HashMap<>();
        map.put("appid",config.getAppID());
        map.put("partnerid",config.getMchID());
        map.put("package","Sign=WXPay");
        map.put("noncestr",nonce_str);
        map.put("timestamp",timestamp);
        map.put("prepayid",prepay_id);
        String sign = MD5Util.getSign(map);

        String resultString="{\"appid\":\""+config.getAppID()+"\",\"partnerid\":\""+config.getMchID()+"\",\"package\":\"Sign=WXPay\"," +
                "\"noncestr\":\""+nonce_str+"\",\"timestamp\":"+timestamp+"," +
                "\"prepayid\":\""+prepay_id+"\",\"sign\":\""+sign+"\"}";
        logger.info(resultString);

        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            QRCodeUtil.writeToStream(result.get("code_url"),outputStream, 300, 300);
            ajaxResult.setSuccess(true);
            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        return resultString;    //给前端app返回此字符串，再调用前端的微信sdk引起微信支付
        return ajaxResult;
    }

    /**
     * 订单支付异步通知
     */
    @RequestMapping(value = "wx/native/notify")
    public String WXPayBack(HttpServletRequest request,HttpServletResponse response){
        String resXml="";
        logger.info("进入微信异步通知");
        try{
            //
            InputStream is = request.getInputStream();
            //将InputStream转换成String
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml=sb.toString();
            logger.info(resXml);
            String result = gzPayService.payBack(resXml);
//            return "<xml><return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg></xml>";
            return result;
        }catch (Exception e){
            logger.error("手机支付回调通知失败",e);
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }
}
