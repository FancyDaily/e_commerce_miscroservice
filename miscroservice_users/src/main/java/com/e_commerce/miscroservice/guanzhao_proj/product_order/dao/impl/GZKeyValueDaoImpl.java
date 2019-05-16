package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZKeyValueDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzKeyValue;
import org.springframework.stereotype.Component;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 21:09
 */
@Component
public class GZKeyValueDaoImpl implements GZKeyValueDao {
    @Override
    public int insert(TGzKeyValue keyValue) {
        return MybatisPlus.getInstance().save(keyValue);
    }

    @Override
    public TGzKeyValue selectByTypeAndValue(Integer toCode, String sign) {
        return MybatisPlus.getInstance().findOne(new TGzKeyValue(), new MybatisPlusBuild(TGzKeyValue.class)
        .eq(TGzKeyValue::getType, toCode)
        .eq(TGzKeyValue::getGzvalue, sign)
        .eq(TGzKeyValue::getIsValid, AppConstant.IS_VALID_YES));
    }
}
