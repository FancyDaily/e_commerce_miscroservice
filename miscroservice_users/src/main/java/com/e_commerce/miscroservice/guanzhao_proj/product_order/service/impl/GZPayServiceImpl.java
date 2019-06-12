package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.AliPayPo;
import com.e_commerce.miscroservice.commons.enums.application.GZOrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.GZSubjectEnum;
import com.e_commerce.miscroservice.commons.enums.application.GZVoucherEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.AliOSSUtil;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.util.colligate.pay.AliPayUtil;
import com.e_commerce.miscroservice.commons.util.colligate.pay.MD5Util;
import com.e_commerce.miscroservice.commons.util.colligate.pay.QRCodeUtil;
import com.e_commerce.miscroservice.commons.util.colligate.pay.WXMyConfigUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZLessonService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZPayService;
import com.e_commerce.miscroservice.xiaoshi_proj.product.util.DateUtil;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	private GzUserVideoDao gzUserVideoDao;

	@Autowired
	private GZVideoDao gzVideoDao;

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

    @Autowired
    private GZLessonService gzLessonService;

    @Autowired
    private RedisUtil redisUtil;

    public static String GZ_PAY_TIMESTAMP_DESCRIBE = "gz_pay:timestamp:%s"; //上次递减的时间

    public static String GZ_PAY_NUM_DESCRIBE = "gz_pay:num:%s";   //递减次数

    public static String GZ_PAY_SUBJECT = "gz_pay:subject:%s";  //用户-课程支付

    public static Integer INTEVAL = 60 * 30;    //单位s

	public static Long INTEVALMILLS = INTEVAL * 1000l;

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

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Map<String, String> dounifiedOrder(String orderNum, Long userId, Long couponId, String spbill_create_ip, int type, Long subjectId) {
        Map<String, String> fail = new HashMap<>();
        WXMyConfigUtil config = null;
        try {
            config = new WXMyConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> resultMap = produceOrder(subjectId, orderNum, couponId, userId, true);//TODO
        if(resultMap==null) {
            String img = (String) redisUtil.get("QR" + orderNum);
            if(img!=null) {
                fail.put("img", img);
            }
            return fail;    //TODO
        }
        Double couponMoney = (Double) resultMap.get("couponMoney");
        String orderNo = (String) resultMap.get("orderNo");

        String attach = userId+","+couponId;
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
                    redisUtil.set("QR" + orderNo, img,INTEVAL);
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

	@Override
    public Map<String, Object> produceOrder(Long subjectId, String orderNum, Long couponId, Long userId, boolean isRandomDisCount) {
        TGzSubject tGzSubject = gzSubjectDao.findSubjectById(subjectId);
        if (tGzSubject==null){
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        boolean isContinuePay = !StringUtil.isEmpty(orderNum);	//是否从待支付列表入口进来

		HashMap<String, Object> resultMap = new HashMap<>();
        if(!isContinuePay) {	//入口为商品详情购买 -> 首先尝试获取最近的一条待支付订单
        	TGzOrder tGzOrder = null;
			List<TGzOrder> gzOrders = gzOrderDao.selectBySubjectIdAndUserIdUnpayDesc(subjectId, userId);
			if(!gzOrders.isEmpty()) {
				tGzOrder = gzOrders.get(0);
			}
			if(tGzOrder!=null) {
				Long orderTime = tGzOrder.getOrderTime();
				if(System.currentTimeMillis() - orderTime > INTEVALMILLS) {	//超时
					tGzOrder.setStatus(GZOrderEnum.TIMEOUT_PAY.getCode());
					gzOrderDao.updateByPrimaryKey(tGzOrder);
				} else {
					Long orderId = tGzOrder.getId();
					log.info("已找到最近的一条待支付订单,订单号={}", orderId);
					resultMap.put("orderNo", tGzOrder.getTgzOrderNo());
					resultMap.put("couponMoney", tGzOrder.getPrice());
					return resultMap;
				}
			}
		}

		//入口为购买记录-待支付
		if (isContinuePay){
			TGzOrder order = gzOrderDao.findByOrderNo(orderNum);
			if (order!=null&&order.getStatus().equals(GZOrderEnum.UN_PAY.getCode())){
				Long orderId = order.getId();
				log.info("已存在待支付订单,订单号={}", orderId.toString());
				resultMap.put("orderNo", order.getTgzOrderNo());
				resultMap.put("couponMoney", order.getPrice());
				return resultMap;
			}
		}

		//一段时间内对于同一课程只能下一次单(或者对于同一课程直接判定有无待支付订单)
		Long payRecord = (Long) redisUtil.hget(String.format(GZ_PAY_SUBJECT, subjectId), userId.toString());
		if(payRecord!=null && currentTimeMillis < payRecord && !isContinuePay) {  //过期并且不是继续支付
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "对于同一个课程，半个小时内只能生成一个订单!");
		}

        Double money = 0d;

        boolean isSalePrice = false;
        Integer forSaleSurplusNum = tGzSubject.getForSaleSurplusNum();
        if (tGzSubject.getForSaleStatus().equals(GZSubjectEnum.FORSALE_STATUS_YES.getCode())
                && forSaleSurplusNum >0){
            money = tGzSubject.getForSalePrice();
            isSalePrice = true;
        }else {
            money = tGzSubject.getPrice();
        }

        //对一段时间内每笔订单随机（或依次）减少不等金额，以确保"通过抓包获得的金额去找到唯一订单"可行
        TGzOrder tGzOrder = new TGzOrder();
        String subjectName = tGzSubject.getName();
        String orderNo = UUIdUtil.generateOrderNo();

        Double couponMoney = money;
        if(couponId!=null){
            TGzVoucher tGzVoucher = gzVoucherDao.findByUserIdCouponId(userId,couponId);
            if(tGzVoucher!=null) {
                Double reductionLimit = tGzVoucher.getReductionLimit();
                reductionLimit = reductionLimit==null?0d:reductionLimit;
                if (tGzVoucher!=null&&reductionLimit<=Double.valueOf(money)&&(tGzVoucher.getEffectiveTime()+tGzVoucher.getActivationTime())>= currentTimeMillis){
                    couponMoney = money - tGzVoucher.getPrice();
                    tGzOrder.setVoucherId(tGzVoucher.getId());
                    tGzVoucher.setAvailableStatus(GZVoucherEnum.STATUS_ALREADY_USED.toCode());
                    gzVoucherDao.update(tGzVoucher);
                }
            }
        }

        if(isRandomDisCount) {
            currentTimeMillis = System.currentTimeMillis();
            //对一段时间内每笔订单随机（或依次）减少不等金额，以确保"通过获得的金额去找到唯一订单"可行
//            Object exist = redisUtil.get(GZ_PAY_TIMESTAMP_DESCRIBE);
            Long exist = (Long) redisUtil.hget(String.format(GZ_PAY_TIMESTAMP_DESCRIBE, subjectId), subjectId.toString());
            double perTime = 0.10;
            Integer num = 1;
            if(exist==null || currentTimeMillis > exist) { //过期
//              redisUtil.set(GZ_PAY_NUM_DESCRIBE, 1, INTEVAL);
//              redisUtil.set(GZ_PAY_TIMESTAMP_DESCRIBE, currentTimeMillis, INTEVAL);
                redisUtil.hset(String.format(GZ_PAY_NUM_DESCRIBE, subjectId), subjectId.toString(), num, INTEVAL);
                redisUtil.hset(String.format(GZ_PAY_TIMESTAMP_DESCRIBE, subjectId), subjectId.toString(), currentTimeMillis + INTEVAL * 1000, INTEVAL);
            } else {
//              num = (Integer) redisUtil.get(GZ_PAY_NUM_DESCRIBE);
                num = (int) redisUtil.hget(String.format(GZ_PAY_NUM_DESCRIBE, subjectId), subjectId.toString());
                num = num==null? 1:num;
//              redisUtil.set(GZ_PAY_NUM_DESCRIBE, ++num);
                redisUtil.hset(String.format(GZ_PAY_NUM_DESCRIBE, subjectId), subjectId.toString(), ++num, INTEVAL);
            }
            couponMoney = couponMoney - perTime * num;
            log.info("立减后的金额couponMoney={}", couponMoney);
        }

        if (tGzOrder.getId()==null){
            tGzOrder.setIsSalePrice(isSalePrice? GZOrderEnum.IS_SALE_PRICE_YES.getCode(): GZOrderEnum.IS_SALE_PRICE_NO.getCode());
            tGzOrder.setPrice(couponMoney);
            tGzOrder.setSubjectId(subjectId);
            tGzOrder.setSubjectName(subjectName);
            tGzOrder.setOrderTime(currentTimeMillis);
            tGzOrder.setStatus(GZOrderEnum.UN_PAY.getCode());
            tGzOrder.setTgzOrderNo(orderNo);
            tGzOrder.setUserId(Long.valueOf(userId));
            gzOrderDao.saveOrder(tGzOrder);
            if(isSalePrice) {
                int surplusNum = tGzSubject.getForSaleSurplusNum() - 1;
                tGzSubject.setForSaleSurplusNum(surplusNum);
                if(surplusNum < 1) {
                    tGzSubject.setForSaleStatus(GZSubjectEnum.FORSALE_STATUS_NO.getCode());
                }
                gzSubjectDao.updateByPrimaryKey(tGzSubject);
            }
        }
        redisUtil.hset(String.format(GZ_PAY_SUBJECT, subjectId), userId.toString(), currentTimeMillis + INTEVAL * 1000, INTEVAL);
        resultMap.put("orderNo", orderNo);
        resultMap.put("couponMoney", couponMoney);
        return resultMap;
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
                            afterPaySuccess(tGzOrder, out_trade_no);    //处理内部业务

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

    @Override
    public void afterPaySuccess(TGzOrder tGzOrder, String out_trade_no) {
        if(out_trade_no == null) {
            return;
        }
        if(tGzOrder==null) {
            tGzOrder = gzOrderDao.findByOrderNo(out_trade_no);
        }
        if (tGzOrder != null && tGzOrder.getStatus().equals(GZOrderEnum.UN_PAY.getCode())) {	//仅处理待支付的订单
            final TGzOrder finalOrder = tGzOrder;
            Long userId = finalOrder.getUserId();
            Long subjectId = finalOrder.getSubjectId();

           /* if (tGzOrder.getVoucherId()!=null){
                TGzVoucher tGzVoucher = new TGzVoucher();
                tGzVoucher.setId(finalOrder.getVoucherId());
                tGzVoucher.setAvailableStatus(GZVoucherEnum.STATUS_ALREADY_USED.toCode());
                gzVoucherDao.update(tGzVoucher);
            }*/

            TGzSubject tGzSubject = gzSubjectDao.selectByPrimaryKey(subjectId);

            TGzUserSubject tGzUserSubject = gzUserSubjectDao.selectByUserIdAndSubjectId(userId, subjectId);
            if(tGzUserSubject==null) {
                tGzUserSubject = new TGzUserSubject();
                tGzUserSubject.setUserId(userId);
                tGzUserSubject.setSubjectId(subjectId);
                Long endTime = DateUtil.parse(tGzSubject.getEndDate() + tGzSubject.getEndTime())+30l*24*3600*1000;
                tGzUserSubject.setExpireTime(endTime);
                gzUserSubjectDao.insert(tGzUserSubject);
                List<TGzLesson> list = gzLessonDao.selectBySubjectId(subjectId);
                List<TGzUserLesson> lessonList = new ArrayList<>();
                list.forEach(tGzLesson -> {
                    TGzUserLesson tGzUserLesson = new TGzUserLesson();
                    tGzUserLesson.setUserId(userId);
                    tGzUserLesson.setSubjectId(subjectId);
                    tGzUserLesson.setLessonId(tGzLesson.getId());
                    lessonList.add(tGzUserLesson);
                });

                gzUserLessonDao.insertList(lessonList);

                List<TGzUserVideo> userVideolist = new ArrayList<>();
				List<Long> lessonIds = list.stream().map(TGzLesson::getId).collect(Collectors.toList());
				List<TGzVideo> videoList = gzVideoDao.selectInLessonIds(lessonIds);
				videoList.forEach(
					a -> {
						TGzUserVideo build = TGzUserVideo.builder().userId(userId).subjectId(subjectId).lessonId(a.getLessonId()).videoId(a.getId()).build();
						userVideolist.add(build);
					}
				);
				gzUserVideoDao.multiInsert(userVideolist);
            } else {    //再次购买 -> 续费
//                boolean expired = tGzUserSubject.getExpireTime() < System.currentTimeMillis();
                Long expireTime = tGzUserSubject.getExpireTime();
                expireTime = expireTime==null?0:expireTime;
                long currentTimeMillis = System.currentTimeMillis();
                long timeStamp = currentTimeMillis > expireTime? currentTimeMillis: expireTime;
                tGzUserSubject.setExpireTime(timeStamp + 6l * 30 * 24 * 3600 * 1000);   //续费半年
                gzUserSubjectDao.updateByPrimaryKey(tGzUserSubject);
            }


            gzLessonService.unlockMyLesson(userId, subjectId);
			TGzOrder order = new TGzOrder();
			order.setTgzOrderNo(out_trade_no);
			order.setStatus(GZOrderEnum.PAYED.getCode());
			gzOrderDao.updateOrder(order);
           /* TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    super.afterCommit();
                }
            });*/
        }
    }

    @Override
    public void dealWithPrice(double price) {
        List<TGzOrder> gzOrders = gzOrderDao.selectByPrice(price, GZOrderEnum.UN_PAY.getCode());    //所有待支付的对应金额的订单
        if(gzOrders.isEmpty()) {
            throw new MessageException("没有对应金额的订单！");
        }
        if(gzOrders.size() > 1) {
            StringBuilder builder = new StringBuilder();
            for(TGzOrder gzOrder:gzOrders) {
                builder.append(gzOrder.getId()).append(",");
            }
            String ids = builder.toString();
            if(ids.endsWith(",")){
                ids = ids.substring(0,ids.length()-1);
            }
            throw new MessageException("相同金额存在多笔订单! orderIds:" + ids);
        }

        TGzOrder gzOrder = gzOrders.get(0);
        afterPaySuccess(gzOrder, gzOrder.getTgzOrderNo());
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void dealWithOrderNo(String orderNo) {
		TGzOrder gzOrder = gzOrderDao.selectByOrderNo(orderNo);
		if(gzOrder == null) {
			MessageException messageException = new MessageException("orderNo对应订单不存在!");
			log.warn("微信支付回调【根据订单号支付】,orderNo={},msg={}", orderNo, messageException.getMessage());
			throw messageException;
		}
		afterPaySuccess(gzOrder, gzOrder.getTgzOrderNo());
	}

    @Override
    public Map<String, Object> preOrder(String orderNum, Long couponId, Long subjectId, Long userId) {
        Map<String, Object> resultMap = produceOrder(subjectId, orderNum, couponId, userId, true);
        Double couponMoney = (Double) resultMap.get("couponMoney");
        String moneyStr = String.format("%.2f", couponMoney);
        String nameSuffix = "";
        if(couponMoney!=null) {
            nameSuffix = "" + moneyStr;
            nameSuffix += ".jpg";
        }
//        nameSuffix = "121558577476.jpg";
        //根据金额去获取二维码
        String img = "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/pay/";
        img += nameSuffix;
        resultMap.put("img", img);

        return resultMap;
    }

}
