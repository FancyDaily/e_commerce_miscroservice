package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.alipay.api.domain.Voucher;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZVoucherDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVoucher;
import org.springframework.stereotype.Component;
import sun.security.jca.GetInstance;

import java.util.List;

@Component
public class GZVoucherDaoImpl implements GZVoucherDao {

    @Override
    public int insert(TGzVoucher voucher, TGzVoucher voucherCopy) {
        return MybatisOperaterUtil.getInstance().save(voucher, voucherCopy);
    }

    @Override
    public List<TGzVoucher> selectByUserIdAndAvailableStatusWithCondition(Long userId, Integer... availableStatus) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(TGzVoucher.class);
        mybatisSqlWhereBuild.eq(TGzVoucher::getUserId, userId)
                .eq(TGzVoucher::getIsValid, AppConstant.IS_VALID_YES);
        if(availableStatus != null) {
            mybatisSqlWhereBuild.in(TGzVoucher::getAvailableStatus, availableStatus);
        }
        return MybatisOperaterUtil.getInstance().finAll(new TGzVoucher(),mybatisSqlWhereBuild);
    }

    @Override
    public int batchUpdate(List<TGzVoucher> toUpdater, List<Long> toUpdaterId) {
        return MybatisOperaterUtil.getInstance().update(toUpdater, new MybatisSqlWhereBuild(TGzVoucher.class)
        .in(TGzVoucher::getId, toUpdaterId)
        .eq(TGzVoucher::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int update(TGzVoucher voucher) {
        return MybatisOperaterUtil.getInstance().update(voucher, new MybatisSqlWhereBuild(TGzVoucher.class)
                .eq(TGzVoucher::getId, voucher.getId())
                .eq(TGzVoucher::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public TGzVoucher findByUserIdCouponId(Integer userId, Integer couponId) {
        return MybatisOperaterUtil.getInstance().findOne(new TGzVoucher(),new MybatisSqlWhereBuild(TGzVoucher.class)
                .eq(TGzVoucher::getUserId,userId).eq(TGzVoucher::getId,couponId));
    }

}
