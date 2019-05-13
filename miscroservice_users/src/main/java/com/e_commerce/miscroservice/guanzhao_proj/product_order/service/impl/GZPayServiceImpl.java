package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.AliPayPo;
import com.e_commerce.miscroservice.commons.enums.application.GZOrderEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.pay.AliPayUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZOrderDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZOrderService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @ClassName GZPayServiceImpl
 * @Auhor huangyangfeng
 * @Date 2019-05-12 18:59
 * @Version 1.0
 */
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
        gzOrderDao.saveOrder(tGzOrder);
    }
}
