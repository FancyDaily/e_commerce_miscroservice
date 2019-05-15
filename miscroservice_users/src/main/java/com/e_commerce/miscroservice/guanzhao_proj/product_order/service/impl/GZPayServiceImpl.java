package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AliPayPo;
import com.e_commerce.miscroservice.commons.enums.application.GZOrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.GZSubjectEnum;
import com.e_commerce.miscroservice.commons.enums.application.GZVoucherEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.AliOSSUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.pay.AliPayUtil;
import com.e_commerce.miscroservice.commons.util.colligate.pay.MD5Util;
import com.e_commerce.miscroservice.commons.util.colligate.pay.QRCodeUtil;
import com.e_commerce.miscroservice.commons.util.colligate.pay.WXMyConfigUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZPayService;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private GZSubjectDao gzSubjectDao;
    @Autowired
    private GZVoucherDao gzVoucherDao;

    @Autowired
    private GZUserSubjectDao gzUserSubjectDao;
     @Autowired
    private GZUserLessonDao gzUserLessonDao;
    @Autowired
    private GZLessonDao gzLessonDao;


    @Override
    public AliPayPo qrCodeTradePre(AliPayPo payPo) {
        try {
            payPo.setOrderNo("123455" + payPo.getSubjectId());
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
    public Map<String, String> dounifiedOrder(String orderNum, Integer userId, Integer couponId, String spbill_create_ip, int type, Long subjectId) {
        Map<String, String> fail = new HashMap<>();
        WXMyConfigUtil config = null;
        try {
            config = new WXMyConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double money = 0d;
        TGzSubject tGzSubject = gzSubjectDao.findSubjectById(subjectId);
        if (tGzSubject==null){
            return fail;
        }

        if (tGzSubject.getForSaleStatus().equals(GZSubjectEnum.FORSALE_STATUS_YES.getCode())
                &&tGzSubject.getForSaleSurplusNum()>0){
            money = tGzSubject.getForSalePrice();
        }else {
            money = tGzSubject.getPrice();
        }

        TGzOrder tGzOrder = new TGzOrder();
        String subjectName = tGzSubject.getName();
        String orderNo = UUIdUtil.generateOrderNo();

        //若查待支付订单
        if (orderNum!=null){
            TGzOrder order = gzOrderDao.findByOrderNo(orderNum);
            if (order!=null&&order.getStatus().equals(GZOrderEnum.UN_PAY.getCode())){
                log.info("已存在待支付订单={}",order);
                orderNo = orderNum;
                tGzOrder = order;
            }
        }
        if (tGzOrder==null||tGzOrder.getId()==null){
            tGzOrder.setPrice(money);
            tGzOrder.setSubjectId(subjectId);
            tGzOrder.setSubjectName(subjectName);
            tGzOrder.setOrderTime(System.currentTimeMillis());
            tGzOrder.setStatus(GZOrderEnum.UN_PAY.getCode());
            tGzOrder.setTgzOrderNo(orderNo);
            tGzOrder.setUserId(Long.valueOf(userId));
            gzOrderDao.saveOrder(tGzOrder);
        }

        Double couponMoney = 0d;
        if(couponId!=null){
            TGzVoucher tGzVoucher = gzVoucherDao.findByUserIdCouponId(userId,couponId);
            if (tGzVoucher!=null&&tGzVoucher.getReductionLimit()<=Double.valueOf(money)&&(tGzVoucher.getEffectiveTime()+tGzVoucher.getActivationTime())>=System.currentTimeMillis()){
                couponMoney = money - tGzVoucher.getPrice();
                tGzOrder.setVoucherId(tGzVoucher.getId());
            }
        }
        String attach = userId+","+couponId;
        if(couponMoney==0) {
            couponMoney = money;
        }

        Double minMon = couponMoney * 100;

        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        String body = "订单支付";
        data.put("body", body);
        data.put("out_trade_no", orderNo);
        data.put("total_fee", String.valueOf(minMon.intValue()));
        data.put("spbill_create_ip", spbill_create_ip);
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", "https://test.xiaoshitimebank.com/user/pay/wx/native/notify");

//        data.put("trade_type", "APP");
        data.put("trade_type", "NATIVE");
        data.put("attach", attach);
//        data.put("sign", md5Util.getSign(data));
        StringBuffer url = new StringBuffer();
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
            String returnCode = resp.get("return_code");    //获取返回码
            String returnMsg = resp.get("return_msg");

            if ("SUCCESS".equals(returnCode)) {       //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
                String resultCode = (String) resp.get("result_code");
                String errCodeDes = (String) resp.get("err_code_des");
                System.out.print(errCodeDes);
                if ("SUCCESS".equals(resultCode)) {
                    //获取预支付交易回话标志
                    Map<String, String> map = new HashMap<>();
                    String prepay_id = resp.get("prepay_id");
                    String signType = "MD5";
                    map.put("prepay_id", prepay_id);
                    map.put("signType", signType);
                    String sign = MD5Util.getSign(map);
                    System.out.println("====" + resp.get("code_url"));
                    resp.put("realsign", sign);
                    url.append("prepay_id=" + prepay_id + "&signType=" + signType + "&sign=" + sign);
                    resp.put("orderNo",orderNo);
                    BufferedImage bufferedImage = QRCodeUtil.toBufferedImage(resp.get("code_url"), 300, 300);
//            QRCodeUtil.writeToStream(result.get("code_url"),outputStream, 300, 300);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "png", os);
                    InputStream is = new ByteArrayInputStream(os.toByteArray());
                    String img = AliOSSUtil.uploadQrImg(is,orderNo);
//            ajaxResult.setSuccess(true);
//            ajaxResult.setData();
//            return ajaxResult;
                    resp.put("img",img);
                    return resp;
                } else {
                    log.warn("订单号：{},错误信息：{}", orderNo, errCodeDes);
                    url.append(errCodeDes);
                    return resp;
                }
            } else {
                log.warn("订单号：{},错误信息：{}", orderNo, returnMsg);
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
     * 支付结果通知
     *
     * @param notifyData 异步通知后的XML数据
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
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(notifyData);         // 转换成map
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确
                // 进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//订单号

                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        //处理订单逻辑
                        /**
                         *          更新数据库中支付状态。
                         *          特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功。
                         *          此处需要判断一下。后面写入库操作的时候再写
                         *
                         */
                        log.info(">>>>>支付成功");
                        TGzOrder tGzOrder = gzOrderDao.findByOrderNo(out_trade_no);
                        if (tGzOrder != null || tGzOrder.getStatus().equals(GZOrderEnum.UN_PAY.getCode())) {
                            TGzOrder order = new TGzOrder();
                            order.setTgzOrderNo(out_trade_no);
                            order.setStatus(GZOrderEnum.PAYED.getCode());
                            gzOrderDao.updateOrder(order);

                            TGzSubject tGzSubject = gzSubjectDao.selectByPrimaryKey(tGzOrder.getSubjectId());
                            TGzUserSubject tGzUserSubject = new TGzUserSubject();
                            tGzUserSubject.setUserId(tGzOrder.getUserId());
                            tGzUserSubject.setSubjectId(tGzOrder.getSubjectId());
                            Long endTime = DateUtil.yyyymmddToTime(tGzSubject.getEndTime())+30*24*3600;
                            tGzUserSubject.setExpireTime(endTime);
                            gzUserSubjectDao.insert(tGzUserSubject);

                            if (tGzOrder.getVoucherId()!=null){
                                TGzVoucher tGzVoucher = new TGzVoucher();
                                tGzVoucher.setId(tGzOrder.getVoucherId());
                                tGzVoucher.setAvailableStatus(GZVoucherEnum.STATUS_AVAILABLE.toCode());
                                gzVoucherDao.update(tGzVoucher);
                            }

                            List<TGzLesson> list = gzLessonDao.selectBySubjectId(tGzOrder.getSubjectId());
                            List<TGzUserLesson> lessonList = new ArrayList<>();
                            list.forEach(tGzLesson -> {
                                TGzUserLesson tGzUserLesson = new TGzUserLesson();
                                tGzUserLesson.setUserId(tGzOrder.getUserId());
                                tGzUserLesson.setSubjectId(tGzOrder.getSubjectId());
                                tGzUserLesson.setLessonId(tGzLesson.getId());
                                lessonList.add(tGzUserLesson);
                            });


                            gzUserLessonDao.insertList(list);

                            log.info("微信手机支付回调成功订单号:{}", out_trade_no);
                            xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

                        }
                    } else {
                        log.info("微信手机支付回调失败订单号:{}", out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }

                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                log.error("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            log.error("手机支付回调通知失败", e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }
}
