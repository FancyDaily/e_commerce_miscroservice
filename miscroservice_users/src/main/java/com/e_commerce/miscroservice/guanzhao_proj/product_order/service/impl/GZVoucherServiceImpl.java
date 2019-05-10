package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZVoucherDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVoucher;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GZVoucherServiceImpl implements GZVoucherService {

    @Autowired
    private GZVoucherDao gzVoucherDao;

    @Override
    public List<TGzVoucher> myVoucherList(TUser user, Integer... option) {
        Long userId = user.getId();
        if(userId == null) {
            return new ArrayList<>();
        }
        Integer[] availableStatus = null;
        if(option!=null && option.length!=0) {
            availableStatus = option;
        }
        return gzVoucherDao.selectByUserIdAndAvailableStatusWithCondition(userId, availableStatus);
    }
}
