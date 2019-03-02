package com.e_commerce.miscroservice.commons.rpc.proxy;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrder;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrderQuery;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyRewardQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * 调用代理商品
 */
@FeignClient(value = "ORDER",path = "/order/proxy/order")
public interface ProxyOrderRpcService {

    @RequestMapping(value = "/getOrderList")
    PageInfo<ProxyOrderQuery> getOrderList(@RequestBody ProxyOrderQuery query);

    @RequestMapping("getProfitOrderList")
    PageInfo<ProxyOrder> getProfitOrderList(@RequestParam("isProfit")int isProfit,@RequestParam("pageNum")int pageNum,@RequestParam("pageSize")int pageSize);

    @RequestMapping("getOrderDetail")
    PageInfo<ProxyOrderQuery> getOrderDetailByUserId(@RequestParam("userId")long userId, @RequestParam("type")int type, @RequestParam("pageNum")int pageNum, @RequestParam("pageSize")int pageSize);

    @RequestMapping("getRewardOrderList")
    PageInfo<ProxyRewardQuery> getRewardOrderList(@RequestParam("userId")long userId, @RequestParam("startTime")String startTime, @RequestParam("endTime")String endTime,
                                                     @RequestParam("pageNum")int pageNum, @RequestParam("pageSize")int pageSize,@RequestParam("isGrants")String isGrants);

    @RequestMapping("doOrderReward")
    public int doOrderReward(@RequestParam("id")long id);

    @RequestMapping("doOrderRewardByUser")
    public int doOrderRewardByUser(@RequestParam("userId")long userId);

    @RequestMapping("payOrder")
    public String payOrder(@RequestParam("goodsId")long goodsId, @RequestParam("userId")long userId, @RequestParam("addressId")long addressId,@RequestParam("openid")String openid);

    @RequestMapping("getBeforeOrderDetail")
    public String getBeforeOrderDetail(@RequestParam("goodsId")long goodsId, @RequestParam("userId")long userId);

    @RequestMapping("getAfterOrderDetail")
    public String getAfterOrderDetail(@RequestParam("orderId")String orderId);

    @RequestMapping("cancelOrder")
    public int cancelOrder(@RequestParam("orderId")String orderId);

    @RequestMapping("getAddressList")
    public String getAddressList(@RequestParam("userId")long userId);

    @RequestMapping("selectAddress")
    public String selectAddress(@RequestParam("id")Long id);

    @RequestMapping("editAddressDefault")
    public String editAddressDefault(@RequestParam("id")Long id,@RequestParam("isDefault")Integer isDefault);

    @RequestMapping("collectReward")
    public String collectReward(@RequestParam("userId")long userId,@RequestParam("type")int type);

    @RequestMapping("getTodayCollectReward")
    public String getTodayCollectReward(@RequestParam("userId")long userId,@RequestParam("type")int type);

    @RequestMapping("getOrderByOrderId")
    public List<ProxyOrder> getOrderByOrderId(@RequestParam("orderId")String orderId);

    @RequestMapping("saveAddress")
    public String saveAddress(@RequestParam("id")Long id, @RequestParam("userId")Long userId, @RequestParam("isDefault")Integer isDefault, @RequestParam("province")String province,
                                @RequestParam("city") String city,@RequestParam("county")String county,@RequestParam("receiverName")String receiverName,
                                @RequestParam("receiverPhone")String receiverPhone,@RequestParam("receiverAddress")String receiverAddress,@RequestParam("delStatus")Integer delStatus);

}
