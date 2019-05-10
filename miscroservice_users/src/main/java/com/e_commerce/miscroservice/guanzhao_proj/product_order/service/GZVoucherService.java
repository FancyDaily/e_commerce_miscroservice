package com.e_commerce.miscroservice.guanzhao_proj.product_order.service;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVoucher;

import java.util.List;

public interface GZVoucherService {

    List<TGzVoucher> myVoucherList(TUser user, Integer... option);
}
