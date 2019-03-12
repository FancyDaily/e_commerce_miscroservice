package com.e_commerce.miscroservice.product.service;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.product.vo.PageMineReturnView;
import com.e_commerce.miscroservice.product.vo.ServiceParamView;

import java.util.List;

/**
 *
 */
public interface ProductService {

	/**
	 * 功能描述:提交求助
	 * 作者:马晓晨
	 * 创建时间:2018/10/30 下午12:06
	 * @param token
	 * @param
	 * @return
	 */
	void submitSeekHelp(TUser user, ServiceParamView param, String token);
	/**
	 *
	 * 功能描述:提交服务
	 * 作者:马晓晨
	 * 创建时间:2018年10月31日 下午2:58:14
	 * @param user
	 * @param param
	 * @param token
	 */
	void submitService(TUser user, ServiceParamView param, String token);

	/**
	 * 获取商品信息根据商品的idList
	 * @param productIds 商品ID的list
	 * @return 商品list
	 */
	List<TService> getListProduct(List<Long> productIds);

	/**
	 * 获取商品的所有desc根据商品的idList
	 * @param productIds 商品id的list
	 * @return 所有商品的desc
	 */
	List<TServiceDescribe> getListProductDesc(List<Long> productIds);

	/**
	 * 根据产品ID获取产品详情
	 * @param serviceId
	 * @return
	 */
	List<TServiceDescribe> getProductDesc(Long serviceId);

	/**
	 * 求助服务下架
	 * @param user 当前用户
	 * @param productId 商品ID
	 */
	void lowerFrame(TUser user, Long productId);

	/**
	 * 求助服务删除
	 * @param user 当前用户
	 * @param productId 商品ID
	 */
	void del(TUser user, Long productId);

	/**
	 * 求助服务上架
	 * @param user 当前用户
	 * @param productId 上架的产品ID
	 */
	void upperFrame(TUser user, Long productId);

	/**
	 * 分页我发布的
	 * @param user 当前用户
	 * @param pageNum 页数
	 * @param pageSize 每页数量
	 * @param type 类型 1、求助 2、服务
	 * @return
	 */
	QueryResult<PageMineReturnView> pageMine(TUser user, Integer pageNum, Integer pageSize, Integer type);

	/**
	 * 根据商品ID获取商品
	 * @param serviceId 商品ID
	 * @return 商品
	 */
	TService getProductById(Long serviceId);
}
