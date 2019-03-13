package com.e_commerce.miscroservice.order.service;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.order.vo.*;

/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2019年3月2日 下午3:46:32
 */
public interface OrderService {
	/**
	 * 生成订单
	 * @param order order实体类
	 * @return 插入的订单数量
	 */
	int saveOrder(TOrder order);

	/**
	 * 显示首页的订单列表
	 * @param param 显示列表的分页参数及筛选条件
	 * @param user 当前用户
	 * @return
	 */
	QueryResult<PageOrderReturnView> list(PageOrderParamView param, TUser user);

	/**
	 * 首页订单列表的订单详情
	 * @param orderId 订单ID
	 * @param user 当前用户
	 * @return 详情view
	 */
	DetailOrderReturnView orderDetail(Long orderId, TUser user);

	/**
	 * 报名选人列表
	 * @param pageNum 页码数
	 * @param pageSize 每页数量
	 * @param user 当前用户
	 */
	QueryResult<PageEnrollAndChooseReturnView> enrollList(Integer pageNum, Integer pageSize, TUser user);

	/**
	 * 同步商品和订单的状态
	 * @param productId 商品ID
	 * @param status  要同步成为的状态
	 */
	void synOrderServiceStatus(Long productId, Integer status);

	/**
	 * 查询自己的订单列表
	 * @param pageNum 页数
	 * @param pageSize 每页数量
	 * @param user 当前用户
	 * @return
	 */
	QueryResult<PageOrderReturnView> listMineOrder(Integer pageNum, Integer pageSize, TUser user);

	/**
	 * 查询订单详情
	 * @param user 当前用户
	 * @param orderId 订单ID
	 * @return
	 */
	DetailMineOrderReturnView detailMineOrder(TUser user, Long orderId);

	/**
	 * 根据商品派生订单
	 * @param service 商品ID
	 * @param type 类型 是发布派生还是报名派生
	 * @param date 报名或者选满人派生的日期
	 */
	void produceOrder(TService service, Integer type, String date);

	/**
	 * 我选人的详情页面
	 * @param orderId 订单ID
	 * @param user 用户
	 * @return 选人列表详情
	 */
	DetailChooseReturnView chooseDetail(Long orderId, TUser user);
}
