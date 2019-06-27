package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.application.GZOrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.GZVoucherEnum;
import com.e_commerce.miscroservice.commons.enums.application.OrderEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZOrderDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZVoucherDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVoucher;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZVoucherService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyVoucherVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GZVoucherServiceImpl implements GZVoucherService {

    @Autowired
    private GZVoucherDao gzVoucherDao;

    @Autowired
    private GZOrderDao gzOrderDao;

    private Log log = Log.getInstance(GZVoucherServiceImpl.class);

    @Override
    public List<MyVoucherVo> myVoucherList(TUser user, Integer... option) {
//        Long userId = user.getId();
		Long userId = 1150L;
        log.info("我的代金券列表userId={}, option={}", userId, option);
        if(userId == null) {
            return new ArrayList<>();
        }
        Integer[] availableStatus = null;
        if(option!=null && option.length!=0) {
            availableStatus = option;
        }

        List<Integer> availableStatusList = Arrays.asList(availableStatus);
        List<Long> toUpdaterIds = new ArrayList<>();
        List<TGzVoucher> toUpdaters = new ArrayList<>();
        if(availableStatusList.size() == 1 && availableStatusList.contains(GZVoucherEnum.STATUS_AVAILABLE.toCode())) {  //如果仅请求可用代金券
            //对过期的订单进行返还代金券
            List<TGzOrder> gzOrders = gzOrderDao.selectByUserIdExpiredUsedVoucher(userId);
            for(TGzOrder gzOrder:gzOrders) {
                if(GZOrderEnum.UN_PAY.getCode().equals(gzOrder.getStatus())) {  //时间过期但是状态未改变(认定为未返还代金券)
                    Long voucherId = gzOrder.getVoucherId();
                    TGzVoucher voucher = gzVoucherDao.selectByPrimaryKey(voucherId);
                    Long activationTime = voucher.getActivationTime();
                    if(activationTime==null) {
                        continue;
                    }
                    Long effectiveTime = voucher.getEffectiveTime();
                    if(System.currentTimeMillis() > effectiveTime + activationTime) {   //过期优惠券
                        continue;
                    }
					Long originId = voucher.getId();
                    voucher = new TGzVoucher();
                    voucher.setId(originId);
					voucher.setAvailableStatus(GZVoucherEnum.STATUS_AVAILABLE.toCode());    //恢复为可用
                    toUpdaterIds.add(voucherId);
                    toUpdaters.add(voucher);
                }
            }
            gzVoucherDao.batchUpdate(toUpdaters, toUpdaterIds); //批量更新
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
				Long originId = voucher.getId();
				voucher = new TGzVoucher();
				voucher.setId(originId);
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
            voucher1.setAvailableStatus(GZVoucherEnum.STATUS_AVAILABLE.toCode());
            voucher1.setActivationTime(System.currentTimeMillis());
            addList.add(voucher1);
        }
        gzVoucherDao.multiInsert(addList);
    }

}
