package com.e_commerce.miscroservice.product.service;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.product.vo.DetailServiceReturnView;
import com.e_commerce.miscroservice.product.vo.PageServiceParamView;
import com.e_commerce.miscroservice.product.vo.PageServiceReturnView;
import com.e_commerce.miscroservice.product.vo.ServiceParamView;

/**
 *
 */
public interface ProductService {

	/**
	 *
	 * 功能描述:服务求助列表
	 * 作者:马晓晨
	 * 创建时间:2018年11月1日 上午10:55:23
	 * @param param
	 * @param user PC组织加入了组织发布求助服务是否可见，根据当前用户查询是否可以看见组织发布的求助服务
	 * @return
	 */
	QueryResult<PageServiceReturnView> list(PageServiceParamView param, TUser user);
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
	 *
	 * 功能描述:求助服务详情
	 * 作者:马晓晨
	 * 创建时间:2018年11月4日 上午12:39:03
	 * @param serviceId
	 * @param user
	 * @return
	 */
	DetailServiceReturnView serviceDetail(Long serviceId, TUser user);

	/**
	 *
	 * 功能描述:列出服务评价或个人主页评价
	 * 作者:马晓晨
	 * 创建时间:2018年11月8日 下午9:51:24
	 * @param tUser
	 * @param serviceId
	 * @param lastTime
	 * @param pageSize
	 */
//	QueryResult<PageRemarkView> listRemark(Long userId, Long serviceId, Integer pageNum, Integer pageSize);
	/**
	 *
	 * 功能描述:显示当前用户提供的服务
	 * 作者:马晓晨
	 * 创建时间:2018年11月11日 下午3:17:54
	 * @param pageSize
	 * @param pageNum
	 * @param user
	 * @return
	 */
//	QueryResult<BaseServiceView> mylist(Long userId);


}
