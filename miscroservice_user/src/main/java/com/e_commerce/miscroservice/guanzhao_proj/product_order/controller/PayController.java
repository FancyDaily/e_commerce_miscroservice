package com.e_commerce.miscroservice.guanzhao_proj.product_order.controller;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AliPayPo;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.Iptools;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.pay.MD5Util;
import com.e_commerce.miscroservice.commons.util.colligate.pay.WXMyConfigUtil;
import com.e_commerce.miscroservice.commons.utils.SmsUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @Autowired
    private SmsUtil smsUtil;

    /**
     * 支付宝支付回调
     * @param money
     */
    @PostMapping("earnNotify")
    public Object pay(Double money){
        logger.info("根据订单金额去支付订单={}", money);
        AjaxResult result = new AjaxResult();
        String telephone = "17767079179";
//        String telephone = "13867655157";
        int application = ApplicationEnum.GUANZHAO_APPLICATION.toCode();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        try {
            gzPayService.dealWithPrice(money);
            result.setSuccess(true);
        } catch (MessageException e) {
            if (ApplicationContextUtil.isDevEnviron()) { // 表示当前运行环境为非调试
                smsUtil.sendSms(telephone, "提示:" + e.getMessage() + "，当前金额:" + money +  ", 时间:" + format, application);
            }
            logger.error("根据订单金额去支付订单错误={}", e);
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            if (ApplicationContextUtil.isDevEnviron()) { // 表示当前运行环境为非调试
                smsUtil.sendSms(telephone, "服务器错误，订单支付有误。目标金额" + money + ", 时间:" + format, application);
            }
            logger.error("根据订单金额去支付订单错误={}", e);
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }
    
    /**
     * 预生成订单
     * @return
     */
    @PostMapping("preOrder/" + TokenUtil.AUTH_SUFFIX)
    public Object preOrder(@RequestParam(required = false,value = "orderNo")String orderNo,
                           @RequestParam(required = false,value = "coupon_id")Long coupon_id,
                           @RequestParam(required = true,value = "subjectId")Long subjectId,
                           @RequestParam(required = false) boolean isSalePrice,
                           HttpServletRequest req, HttpServletResponse response) {
        TUser user = UserUtil.getUser();
        logger.info("支付宝-预生成订单orderNo={},coupon_id={},subjectId={}",orderNo,coupon_id,subjectId);
        AjaxResult result = new AjaxResult();
        try {
            Map<String, Object> resultMap = gzPayService.preOrder(orderNo, coupon_id, subjectId, user.getId());
            result.setData(resultMap);
            result.setSuccess(true);
        } catch (MessageException e) {
			logger.warn("支付成功错误={}", e);
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
            logger.error("支付成功错误={}", e);
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

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
            gzPayService.qrCodeTradePre(payPo);
            ajaxResult.setSuccess(true);
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



    @RequestMapping(value = "/wx/" + TokenUtil.AUTH_SUFFIX)
    public Object orderPay(
                           @RequestParam(required = false,value = "orderNo")String orderNo,
                           @RequestParam(required = false,value = "coupon_id")Long coupon_id,
                           @RequestParam(required = true,value = "subjectId")Long subjectId,
                           @RequestParam(required = false) boolean isSalePrice,
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
//        Integer userId = user_id;
        Long userId = Long.valueOf(IdUtil.getId());
        WXMyConfigUtil config = new WXMyConfigUtil();
        String spbill_create_ip = Iptools.gainRealIp(req);
//        String spbill_create_ip="10.4.21.78";
        logger.info(spbill_create_ip);
        Map<String,String> result = gzPayService.dounifiedOrder(orderNo,userId,coupon_id,spbill_create_ip,1,subjectId);
        if (result==null){
            ajaxResult.setSuccess(false);
            return ajaxResult;
        }
        if ("FAIL".equals(result.get("return_code"))||"FAIL".equals(result.get("result_code"))){

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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderNo",result.get("orderNo"));
        jsonObject.put("resultString",resultString);
        jsonObject.put("img",result.get("img"));
        ajaxResult.setSuccess(true);
        ajaxResult.setData(jsonObject);
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
