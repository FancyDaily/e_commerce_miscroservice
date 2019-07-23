package com.e_commerce.miscroservice.guanzhao_proj.product_order.controller;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZOrderDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZSubjectDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZVoucherDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVoucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品-订单 模块对外提供层
 *
 */
@Component
public class GZProductOrderCommonController {

    @Autowired
    private GZSubjectDao productDao;

    @Autowired
    private GZOrderDao orderDao;

    @Autowired
    private GZVoucherDao voucherDao;

    /**
     * 插入代金券记录
     * @param voucher
     * @param voucherCopy
     */
    public int insertVoucher(TGzVoucher voucher, TGzVoucher voucherCopy) {
        return voucherDao.insert(voucher,voucherCopy);
    }

}
