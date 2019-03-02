package com.e_commerce.miscroservice.commons.rpc.proxy;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoods;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoodsType;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 调用代理商品
 */
@FeignClient(value = "PRODUCT",path = "/product/proxy")
public interface ProxyGoodsRpcService {


    @RequestMapping(value = "/selectById")
    public ProxyGoods selectById(@RequestParam("id") Long id);

    @RequestMapping(value = "/getGoodsList")
    PageInfo<ProxyGoods> getGoodsList(@RequestParam("pageNum")int pageNum, @RequestParam("pageSize")int pageSize, @RequestParam("id")Long id, @RequestParam("goodsName")String goodsName);

    @RequestMapping(value = "/saveGoods")
    public Response saveGoods(@RequestBody ProxyGoods proxyGoods);

    @RequestMapping("selectGoodsType")
    public ProxyGoodsType selectGoodsType(@RequestParam("id")Long id);

    @RequestMapping("delGoodsList")
    public Response delGoodsList(@RequestParam("ids")String ids);

    @RequestMapping("getGoodsTypeList")
    public List<ProxyGoodsType> getGoodsTypeList(@RequestParam("pageNum")int pageNum,@RequestParam("pageSize")int pageSize);

}
