package com.e_commerce.miscroservice.user.rpc;

import com.e_commerce.miscroservice.commons.entity.proxy.PreOrderResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 调用微信支付
 */
@FeignClient(value = "PAYMENT")
public interface ProxyPayRpcService {


    @RequestMapping(value = "pay/rpc/wx/placeOrderApp")
    PreOrderResult placeOrderApp(@RequestParam(value = "amount") BigDecimal amount,
                                  @RequestParam(value = "title") String title,
                                  @RequestParam(value = "out_trade_no") String out_trade_no,
                                  @RequestParam(value = "ip") String ip
    );

}
