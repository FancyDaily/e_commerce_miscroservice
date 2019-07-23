package com.e_commerce.miscroservice.guanzhao_proj.product_order.service;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyVoucherVo;

import java.util.List;

public interface GZVoucherService {

    List<MyVoucherVo> myVoucherList(TUser user, Integer... option);

    void addVoucher(double price, long userId, int count);
}
