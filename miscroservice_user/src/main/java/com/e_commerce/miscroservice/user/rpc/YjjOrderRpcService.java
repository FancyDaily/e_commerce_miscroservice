package com.e_commerce.miscroservice.user.rpc;

import com.e_commerce.miscroservice.commons.entity.order.YjjOrder;
import com.e_commerce.miscroservice.commons.entity.proxy.PreOrderResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 调用订单模块
 */
@FeignClient(value = "ORDER")
public interface YjjOrderRpcService {

    /**
     * 根据订单号查询订单
     *
     * @param orderNo orderNo
     * @return true 操作成功
     * @author Charlie
     * @date 2018/10/11 17:57
     */
    @RequestMapping( "findYjjOrderByOrderNo" )
    public YjjOrder findYjjOrderByOrderNo(@RequestBody String orderNo);

}
