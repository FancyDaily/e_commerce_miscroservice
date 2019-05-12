package com.e_commerce.miscroservice.guanzhao_proj.product_order.controller;

import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZOrderService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 观照律师训练营
 * 订单模块
 *
 */
@RestController
@RequestMapping("gz/api/v1/order")
@Data
public class GZOrderController {

    @Autowired
    public GZOrderService gzOrderService;

    Log logger = Log.getInstance(GZOrderController.class);

    @RequestMapping("list")
    public Object findMyOrderList(Integer pageNum,Integer pageSize){

        AjaxResult ajaxResult = new AjaxResult();
        try {
//            Integer id = 1;
            Integer id = IdUtil.getId();
            QueryResult<TGzOrder> list = gzOrderService.findMyOrderList(id, pageNum, pageSize);
            ajaxResult.setData(list);
            ajaxResult.setSuccess(true);
        }catch (MessageException e){
            logger.warn("我的订单列表={}",e.getMessage());
            ajaxResult.setMsg(e.getMessage());
            ajaxResult.setSuccess(false);
        }catch (Exception e){
            logger.error("我的订单列表={}",e);
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }




}
