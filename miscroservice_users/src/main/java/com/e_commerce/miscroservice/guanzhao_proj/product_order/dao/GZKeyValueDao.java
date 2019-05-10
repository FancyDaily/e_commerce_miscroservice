package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzKeyValue;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 21:09
 */
public interface GZKeyValueDao {

    int insert(TGzKeyValue keyValue);

    TGzKeyValue selectByTypeAndValue(Integer toCode, String sign);
}
