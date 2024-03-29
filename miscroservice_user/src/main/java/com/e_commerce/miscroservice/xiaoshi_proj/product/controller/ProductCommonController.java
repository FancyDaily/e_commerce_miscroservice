package com.e_commerce.miscroservice.xiaoshi_proj.product.controller;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.xiaoshi_proj.product.dao.ProductDao;
import com.e_commerce.miscroservice.xiaoshi_proj.product.service.ProductService;
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

	@Autowired
	ProductDao productDao;

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
	 * @return mainKey:商品ID  theValue:封面图
	 */
	public Map<Long, String> getProductCoverPic(List<Long> serviceIds) {
		Map<Long, String> coverPic = new HashMap<>();
		List<TServiceDescribe> listProductDesc = productService.getListProductDesc(serviceIds);
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

	/**
	 * 超时或者互助时不足自动下架
	 * @param service 商品
	 * @param type 1、超过结束时间下架 2、互助时不足下架
	 */
	public void autoLowerFrameService(TService service, Integer type) {
		productService.autoLowerFrameService(service, type);
	}

	/**
	 * 更新商品信息
	 * @param service
	 */
	public void update(TService service) {
		productService.updateServiceByKey(service);
	}

	public List<TService> selectServProByUser(Long userId){return productDao.selectUserServ(userId); }

	public long updateServiceByList(List<TService> serviceList, List<Long> serviceIdList){
		return  productDao.updateServiceByList(serviceList ,serviceIdList);
	}

    public TService selectByProductId(Long productId) {
		return productDao.selectByPrimaryKey(productId);
    }

	public TServiceDescribe getProductDescTop(Long serviceId) {
		return productService.getProductDescTop(serviceId);

	}

    public List<TService> selectProductsByUserId(Long userId) {
		return productDao.selectByUserId(userId);
    }
}
