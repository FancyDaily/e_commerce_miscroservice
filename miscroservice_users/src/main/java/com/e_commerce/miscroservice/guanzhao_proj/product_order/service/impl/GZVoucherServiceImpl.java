package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.application.GZVoucherEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
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

    private Log log = Log.getInstance(GZVoucherServiceImpl.class);

    @Override
    public List<MyVoucherVo> myVoucherList(TUser user, Integer... option) {
        Long userId = user.getId();
        log.info("我的代金券列表userId={}, option={}", userId, option);
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
                voucher.setUpdateUser(userId);
//                gzVoucherDao.update(voucher);
                toUpdater.add(voucher);
                toUpdaterId.add(voucher.getId());
                myVoucherVo.setAvailableStatus(GZVoucherEnum.STATUS_EXPIRED.toCode());
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

    @Override
    public void addVoucher(double price, long userId, int count) {
        ArrayList<TGzVoucher> addList = new ArrayList<>();
        for(int i=0; i<count; i++) {
            TGzVoucher voucher1 = gzVoucherDao.selectByPrimaryKey(111L);
            voucher1.setPrice(price);
            voucher1.setUserId(userId);
            voucher1.setId(null);
            addList.add(voucher1);
        }
        gzVoucherDao.multiInsert(addList);
    }

}
