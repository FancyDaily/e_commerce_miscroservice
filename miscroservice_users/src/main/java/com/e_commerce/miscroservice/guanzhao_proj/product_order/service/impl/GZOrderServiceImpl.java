package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.GZOrderEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZOrderDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZOrderService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.OrderDetailVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        for(TGzOrder tGzOrder:list) {
            Long originalSurplusTime = AppConstant.PAY_SURPLUS_TIME_ORIGINAL;
            Long paySurplusTime = originalSurplusTime - (System.currentTimeMillis() - tGzOrder.getOrderTime());
            boolean expired = paySurplusTime < 0;
            if(expired && Objects.equals(tGzOrder.getStatus(), GZOrderEnum.UN_PAY.getCode())) {
                tGzOrder.setStatus(GZOrderEnum.TIMEOUT_PAY.getCode());
                gzOrderDao.updateByPrimaryKey(tGzOrder);
            }
        }
        QueryResult<TGzOrder> result = new QueryResult<>();
        result.setResultList(list);
        result.setTotalCount(page.getTotal());
        return result;
    }

    @Override
    public OrderDetailVO findOrderDetailed(String orderNo, Integer userId) {
        logger.info("查询我的订单order={},userId={}",orderNo,userId);
        TGzOrder tGzOrder = gzOrderDao.findByOrderNo(orderNo);
        if(tGzOrder==null) {
            return null;
        }
        Long originalSurplusTime = AppConstant.PAY_SURPLUS_TIME_ORIGINAL;
        Long paySurplusTime = originalSurplusTime - (System.currentTimeMillis() - tGzOrder.getOrderTime());
        boolean expired = paySurplusTime < 0;
        if(expired && Objects.equals(tGzOrder.getStatus(), GZOrderEnum.UN_PAY.getCode())) {
            tGzOrder.setStatus(GZOrderEnum.TIMEOUT_PAY.getCode());
            gzOrderDao.updateByPrimaryKey(tGzOrder);
        }
        OrderDetailVO orderDetailVO = tGzOrder.copyOrderDetailVO();
        paySurplusTime = expired? -1: paySurplusTime;
        orderDetailVO.setPaySurplusTime(paySurplusTime);
        return orderDetailVO;
    }
}
