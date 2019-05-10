package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
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
        return MybatisOperaterUtil.getInstance().save(keyValue);
    }

    @Override
    public TGzKeyValue selectByTypeAndValue(Integer toCode, String sign) {
        return MybatisOperaterUtil.getInstance().findOne(new TGzKeyValue(), new MybatisSqlWhereBuild(TGzKeyValue.class)
        .eq(TGzKeyValue::getType, toCode)
        .eq(TGzKeyValue::getValue, sign)
        .eq(TGzKeyValue::getIsValid, AppConstant.IS_VALID_YES));
    }
}
