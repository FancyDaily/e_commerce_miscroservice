package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZVoucherDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVoucher;
import org.springframework.stereotype.Component;

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

}
