package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.alipay.api.domain.Voucher;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZVoucherDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVoucher;
import org.springframework.stereotype.Component;
import sun.security.jca.GetInstance;

import java.util.List;

@Component
public class GZVoucherDaoImpl implements GZVoucherDao {

    @Override
    public int insert(TGzVoucher voucher, TGzVoucher voucherCopy) {
        return MybatisPlus.getInstance().save(voucher, voucherCopy);
    }

    @Override
    public List<TGzVoucher> selectByUserIdAndAvailableStatusWithCondition(Long userId, Integer... availableStatus) {
        MybatisPlusBuild mybatisPlusBuild = new MybatisPlusBuild(TGzVoucher.class);
        mybatisPlusBuild.eq(TGzVoucher::getUserId, userId)
                .eq(TGzVoucher::getIsValid, AppConstant.IS_VALID_YES);
        if(availableStatus != null) {
            mybatisPlusBuild.in(TGzVoucher::getAvailableStatus, availableStatus);
        }
        mybatisPlusBuild.orderBy(MybatisPlusBuild.OrderBuild.buildAsc(TGzVoucher::getAvailableStatus), MybatisPlusBuild.OrderBuild.buildDesc(TGzVoucher::getCreateTime));
        return MybatisPlus.getInstance().finAll(new TGzVoucher(),mybatisPlusBuild);
    }

    @Override
    public int batchUpdate(List<TGzVoucher> toUpdater, List<Long> toUpdaterId) {
        return MybatisPlus.getInstance().update(toUpdater, new MybatisPlusBuild(TGzVoucher.class)
        .in(TGzVoucher::getId, toUpdaterId)
        .eq(TGzVoucher::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int update(TGzVoucher voucher) {
        return MybatisPlus.getInstance().update(voucher, new MybatisPlusBuild(TGzVoucher.class)
                .eq(TGzVoucher::getId, voucher.getId())
                .eq(TGzVoucher::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public TGzVoucher findByUserIdCouponId(Integer userId, Integer couponId) {
        return MybatisPlus.getInstance().findOne(new TGzVoucher(),new MybatisPlusBuild(TGzVoucher.class)
                .eq(TGzVoucher::getUserId,userId).eq(TGzVoucher::getId,couponId));
    }

}
