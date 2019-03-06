package com.e_commerce.miscroservice.order.service;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.order.vo.PageOrderParamView;
import com.e_commerce.miscroservice.order.vo.PageOrderReturnView;

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
}
