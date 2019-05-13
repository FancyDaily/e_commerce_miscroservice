package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZOrderDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GZOrderDaoImpl implements GZOrderDao {

    @Override
    public List<TGzOrder> findMyOrderList(Integer id) {
        List<TGzOrder> list = MybatisOperaterUtil.getInstance().finAll(new TGzOrder(), new MybatisSqlWhereBuild(TGzOrder.class)
                .eq(TGzOrder::getUserId,id));
        return list;
    }

    @Override
    public void saveOrder(TGzOrder payPo) {
        MybatisOperaterUtil.getInstance().save(payPo);
    }
}
