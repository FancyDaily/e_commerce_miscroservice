package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AliPayPo;
import com.e_commerce.miscroservice.commons.enums.application.GZOrderEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.pay.AliPayUtil;
import com.e_commerce.miscroservice.commons.util.colligate.pay.MD5Util;
import com.e_commerce.miscroservice.commons.util.colligate.pay.WXMyConfigUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZOrderDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZPayService;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @ClassName GZPayServiceImpl
 * @Auhor huangyangfeng
 * @Date 2019-05-12 18:59
 * @Version 1.0
 */
@Log
@Service
public class GZPayServiceImpl implements GZPayService {

    @Autowired
    private GZOrderDao gzOrderDao;
    @Override
    public AliPayPo qrCodeTradePre(AliPayPo payPo) {
        try {
            payPo.setOrderNo("123455"+payPo.getSubjectId());
            payPo.setPayMoney(1D);
            payPo = AliPayUtil.doQrCodeTradePre(payPo);
        } catch (Exception e) {
            if (payPo.getCode() == null || StringUtils.isEmpty(payPo.getCode())) {
                payPo.setCode("-1");
            } else {
                payPo.setCode(payPo.getCode());
            }

            if (payPo.getMsg() == null || StringUtils.isEmpty(payPo.getMsg())) {
                payPo.setMsg("交易异常");
            } else {
                payPo.setMsg(payPo.getMsg());
            }
            e.printStackTrace();
            throw new MessageException("交易异常");

        }
        TGzOrder tGzOrder = new TGzOrder();
        tGzOrder.setPrice(payPo.getPayMoney());
        tGzOrder.setSubjectId(payPo.getSubjectId());
        tGzOrder.setSubjectName(payPo.getSubjectName());
        tGzOrder.setOrderTime(System.currentTimeMillis());
        tGzOrder.setStatus(GZOrderEnum.UN_PAY.getCode());
        gzOrderDao.saveOrder(tGzOrder);
        return payPo;
    }

    @Override
    public void appTrade(AliPayPo payPo) {
        try {
            AliPayUtil.doAppTrade(payPo);
        } catch (Exception e) {
            if (payPo.getCode() == null || StringUtils.isEmpty(payPo.getCode())) {
                payPo.setCode("-1");
            } else {
                payPo.setCode(payPo.getCode());
            }

            if (payPo.getMsg() == null || StringUtils.isEmpty(payPo.getMsg())) {
                payPo.setMsg("交易异常");
            } else {
                payPo.setMsg(payPo.getMsg());
            }
            e.printStackTrace();

        }
        TGzOrder tGzOrder = new TGzOrder();
        tGzOrder.setPrice(payPo.getPayMoney());
        tGzOrder.setSubjectId(payPo.getSubjectId());
        tGzOrder.setSubjectName(payPo.getSubjectName());
        tGzOrder.setOrderTime(System.currentTimeMillis());
        tGzOrder.setStatus(GZOrderEnum.UN_PAY.getCode());
        gzOrderDao.saveOrder(tGzOrder);
    }

    @Override
    public Map<String, String> dounifiedOrder(String attach, String out_trade_no, String total_fee, String spbill_create_ip, int type, Long subjectId, String subjectName) {
        Map<String, String> fail = new HashMap<>();
        WXMyConfigUtil config = null;
        try {
            config = new WXMyConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String orderNo = UUIdUtil.generateOrderNo();
        Double money = Double.valueOf(total_fee);
        Double minMon = money*100;
        TGzOrder tGzOrder = new TGzOrder();
        tGzOrder.setPrice(money);
        tGzOrder.setSubjectId(subjectId);
        tGzOrder.setSubjectName(subjectName);
        tGzOrder.setOrderTime(System.currentTimeMillis());
        tGzOrder.setStatus(GZOrderEnum.UN_PAY.getCode());
        tGzOrder.setTgzOrderNo(orderNo);
        gzOrderDao.saveOrder(tGzOrder);
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        String body="订单支付";
        data.put("body", body);
        data.put("out_trade_no", orderNo);
        data.put("total_fee", String.valueOf(minMon.intValue()));
        data.put("spbill_create_ip",spbill_create_ip);
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", "https://test.xiaoshitimebank.com/user/pay/wx/native/notify");

//        data.put("trade_type", "APP");
        data.put("trade_type", "NATIVE");
        data.put("attach", attach);
//        data.put("sign", md5Util.getSign(data));
        StringBuffer url= new StringBuffer();
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
            String returnCode = resp.get("return_code");    //获取返回码
            String returnMsg = resp.get("return_msg");

            if("SUCCESS".equals(returnCode)){       //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
                String resultCode = (String)resp.get("result_code");
                String errCodeDes = (String)resp.get("err_code_des");
                System.out.print(errCodeDes);
                if("SUCCESS".equals(resultCode)){
                    //获取预支付交易回话标志
                    Map<String,String> map = new HashMap<>();
                    String prepay_id = resp.get("prepay_id");
                    String signType = "MD5";
                    map.put("prepay_id",prepay_id);
                    map.put("signType",signType);
                    String sign = MD5Util.getSign(map);
                    System.out.println("===="+resp.get("code_url"));
                    resp.put("realsign",sign);
                    url.append("prepay_id="+prepay_id+"&signType="+signType+ "&sign="+sign);
                    return resp;
                }else {
                    log.warn("订单号：{},错误信息：{}",out_trade_no,errCodeDes);
                    url.append(errCodeDes);
                    return resp;
                }
            }else {
                log.warn("订单号：{},错误信息：{}",out_trade_no,returnMsg);
                url.append(returnMsg);
                return resp;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return fail;
    }


    /**
     *  支付结果通知
     * @param notifyData    异步通知后的XML数据
     * @return
     */
    @Override
    public String payBack(String notifyData) {
        WXMyConfigUtil config = null;
        try {
            config = new WXMyConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config);
        String xmlBack="";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(notifyData);         // 转换成map
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确
                // 进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                String  return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//订单号

                if(return_code.equals("SUCCESS")){
                    if(out_trade_no!=null){
                        //处理订单逻辑
                        /**
                         *          更新数据库中支付状态。
                         *          特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功。
                         *          此处需要判断一下。后面写入库操作的时候再写
                         *
                         */
                        log.info(">>>>>支付成功");

                        log.info("微信手机支付回调成功订单号:{}",out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    }else {
                        log.info("微信手机支付回调失败订单号:{}",out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }

                }
                return xmlBack;
            }
            else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                log.error("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            log.error("手机支付回调通知失败",e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }
}
