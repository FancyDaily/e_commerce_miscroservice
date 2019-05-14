package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZOrderDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GZOrderServiceImpl implements GZOrderService {
    private Log logger = Log.getInstance(GZOrderServiceImpl.class);

    @Autowired
    private GZOrderDao gzOrderDao;
    @Override
    public QueryResult<TGzOrder> findMyOrderList(Integer id, Integer pageNumber, Integer pageSize) {
        logger.info("我的订单列表 id={},pageNum={},pageSize={}",id,pageNumber,pageSize);
        Page<TGzOrder> page = PageHelper.startPage(pageNumber,pageSize);
        List<TGzOrder> list = gzOrderDao.findMyOrderList(id);
        QueryResult<TGzOrder> result = new QueryResult<>();
        result.setResultList(list);
        result.setTotalCount(page.getTotal());
        return result;
    }


    @Override
    public TGzOrder findOrderDetailed(String orderId, Integer userId)
    {
        logger.info("查询我的订单order={},userId={}",orderId,userId);
        TGzOrder tGzOrder = gzOrderDao.findByOrderId(orderId);
        return tGzOrder;
    }
}
