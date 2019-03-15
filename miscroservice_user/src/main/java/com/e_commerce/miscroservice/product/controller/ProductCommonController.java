package com.e_commerce.miscroservice.product.controller;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
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
public class ProductCommonController extends BaseController {
	@Autowired
	ProductService productService;

	/**
	 * 根据ID批量查询所有商品
	 *
	 * @param serviceIds 需要查询的商品ID
	 * @return 所有这些ID的service
	 */
	public List<TService> getListProduct(List<Long> serviceIds) {
		return productService.getListProduct(serviceIds);
	}

	/**
	 * 根据商品ID获取商品
	 *
	 * @param serviceId 商品ID
	 * @return 商品
	 */
	public TService getProductById(Long serviceId) {
		return productService.getProductById(serviceId);
	}

	/**
	 * 获取商品封面图
	 *
	 * @param serviceIds 需要获取封面图的商品ID
	 * @return key:商品ID  value:封面图
	 */
	public Map<Long, String> getProductCoverPic(List<Long> serviceIds) {
		Map<Long, String> coverPic = new HashMap<>();
		List<TServiceDescribe> listProductDesc = productService.getListProductDesc(serviceIds);
//		if (listProductDesc != null && listProductDesc.size() > 0) {
//			listProductDesc.stream().filter(serviceDesc -> serviceDesc.getIsCover().equals(IS_COVER_YES))
//					.forEach(serviceDesc -> coverPic.put(serviceDesc.getServiceId(), serviceDesc.getUrl()));
//		}
		for (TServiceDescribe tServiceDescribe : listProductDesc) {
			if (tServiceDescribe.getIsCover().equals(IS_COVER_YES)) {
				coverPic.put(tServiceDescribe.getServiceId(), tServiceDescribe.getUrl());
			}
		}
		return coverPic;
	}

	/**
	 * 获取服务详情（图片及详情）
	 *
	 * @param serviceId
	 * @return
	 */
	public List<TServiceDescribe> getProductDesc(Long serviceId) {
		return productService.getProductDesc(serviceId);
	}

	public void autoLowerFrameService(TService service) {
		service.setStatus(ProductEnum.STATUS_LOWER_FRAME_TIME_OUT.getValue());
		productService.autoLowerFrameService(service);
	}


}
