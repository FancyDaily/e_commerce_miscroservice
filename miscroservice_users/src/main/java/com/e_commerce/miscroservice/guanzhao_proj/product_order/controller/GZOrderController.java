package com.e_commerce.miscroservice.guanzhao_proj.product_order.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZOrderService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 观照订单模块
 *
 */
@RestController
@RequestMapping("gz/api/v1/order")
@Log
public class GZOrderController {

    @Autowired
    public GZOrderService gzOrderService;


    /**
     * 订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list/" + TokenUtil.AUTH_SUFFIX)
    public Object findMyOrderList(Integer pageNum,Integer pageSize){

        AjaxResult ajaxResult = new AjaxResult();
        try {
            Integer id = IdUtil.getId();
            QueryResult<TGzOrder> list = gzOrderService.findMyOrderList(id, pageNum, pageSize);
            ajaxResult.setData(list);
            ajaxResult.setSuccess(true);
        }catch (MessageException e){
            log.warn("我的订单列表={}",e.getMessage());
            ajaxResult.setMsg(e.getMessage());
            ajaxResult.setSuccess(false);
        }catch (Exception e){
            log.error("我的订单列表={}",e);
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }

    /**
     * 查询我的订单详情
     * @param orderNo
     * @return
     */
    @RequestMapping("detailed/"+TokenUtil.AUTH_SUFFIX)
    public Object findOrderDetailed(String orderNo){
        AjaxResult result = new AjaxResult();
        try {
            Integer userId  = IdUtil.getId();
            OrderDetailVO tGzOrder = gzOrderService.findOrderDetailed(orderNo,userId);
            result.setData(tGzOrder);
            result.setSuccess(true);
        }catch (MessageException e){
            log.warn("我的订单={}",e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        }catch (Exception e){
            log.error("我的订单={}",e);
            result.setSuccess(false);
        }
        return result;
    }




}
