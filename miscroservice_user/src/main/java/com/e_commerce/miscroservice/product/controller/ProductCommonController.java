package com.e_commerce.miscroservice.product.controller;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 马晓晨
 * @date 2019/3/6
 */
@Component
public class ProductCommonController extends BaseController{
	@Autowired
	ProductService productService;

	/**
	 * 根据ID批量查询所有商品
	 * @param serviceIds 需要查询的商品ID
	 * @return 所有这些ID的service
	 */
	public List<TService> getListProduct(List<Long> serviceIds) {
		return productService.getListProduct(serviceIds);
	}

	/**
	 * 获取商品封面图
	 * @param serviceIds  需要获取封面图的商品ID
	 * @return key:商品ID  value:封面图
	 */
	public Map<Long, String> getProductCoverPic(List<Long> serviceIds) {
		Map<Long, String> coverPic = new HashMap<>();
		List<TServiceDescribe> listProductDesc = productService.getListProductDesc(serviceIds);
		listProductDesc.stream().filter(serviceDesc -> serviceDesc.getIsCover().equals(IS_COVER_YES))
				.forEach(serviceDesc -> coverPic.put(serviceDesc.getServiceId(), serviceDesc.getUrl()));
		return coverPic;
	}

	/**
	 * 获取服务详情（图片及详情）
	 * @param serviceId
	 * @return
	 */
	public List<TServiceDescribe> getProductDesc(Long serviceId) {
		return productService.getProductDesc(serviceId);
	}
}
