package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.application.GZVoucherEnum;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZVoucherDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVoucher;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZVoucherService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyVoucherVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GZVoucherServiceImpl implements GZVoucherService {

    @Autowired
    private GZVoucherDao gzVoucherDao;

    @Override
    public List<MyVoucherVo> myVoucherList(TUser user, Integer... option) {
        Long userId = user.getId();
        if(userId == null) {
            return new ArrayList<>();
        }
        Integer[] availableStatus = null;
        if(option!=null && option.length!=0) {
            availableStatus = option;
        }
        List<TGzVoucher> vouchers = gzVoucherDao.selectByUserIdAndAvailableStatusWithCondition(userId, availableStatus);
        List<MyVoucherVo> resultList = new ArrayList<>();
        List<TGzVoucher> toUpdater = new ArrayList<>();
        List<Long> toUpdaterId = new ArrayList<>();
        for(TGzVoucher voucher:vouchers) {
            MyVoucherVo myVoucherVo = voucher.copyMyVoucherVo();
            long intervel = System.currentTimeMillis() - voucher.getActivationTime();
            boolean expired = intervel > voucher.getEffectiveTime();
            if(expired && voucher.getAvailableStatus().intValue() == GZVoucherEnum.STATUS_AVAILABLE.toCode().intValue()) {  //修正
                voucher.setAvailableStatus(GZVoucherEnum.STATUS_EXPIRED.toCode());
                toUpdater.add(voucher);
                toUpdaterId.add(voucher.getId());
            }
            intervel = voucher.getEffectiveTime() - intervel;
            Long expectedDayCnt = intervel/ DateUtil.interval;
            Long expectedHourCnt = intervel% DateUtil.interval/60/60/1000;

            Integer surplusDayCnt = expired?-1:expectedDayCnt.intValue();
            expectedHourCnt = expired?-1:expectedHourCnt;

            myVoucherVo.setSurplusDayCnt(surplusDayCnt);
            myVoucherVo.setSurplusHourCnt(expectedHourCnt.intValue());
            resultList.add(myVoucherVo);
        }
        gzVoucherDao.batchUpdate(toUpdater, toUpdaterId);
        return resultList;
    }

    public static void main(String[] args) {
        Long interval = (long)24*60*60*1000;
        Long expectedDayCnt = interval/ DateUtil.interval;
        Long expectedHourCnt = interval % DateUtil.interval/60/60/1000;
        System.out.println(expectedDayCnt + "," + expectedHourCnt);
    }
}
