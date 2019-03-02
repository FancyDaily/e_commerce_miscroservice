package com.e_commerce.miscroservice.user.rpc;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author hyf
 * @Date 2019/1/17 14:47
 */
@FeignClient(value = "ORDER", path = "/order/rpc/shopMemberOrder")
@Component
public interface ShopMemberOrderRpcService {
    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    @RequestMapping("findOrderByOrderNo")
    public ShopMemberOrder findOrderByOrderNo(@RequestParam("orderNo") String orderNo);
}
