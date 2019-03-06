package com.e_commerce.miscroservice.order.dao;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.order.vo.PageServiceParamView;

import java.util.List;

/**
 * 功能描述:订单dao层
 * @author 马晓晨
 * @date 2019/3/5 21:15
 */
public interface OrderDao {
	/**
	 * 保存一个Order订单
	 * @param order order
	 * @return 保存的订单个数 本方法成功返回1
	 */
	int saveOneOrder(TOrder order);

	/**
	 * 根据id查找订单
	 * @param id 订单id
	 * @return 订单PO
	 */
	TOrder selectByPrimaryKey(Long id);

	/**
	 * 根据主键更新订单信息
	 * @param order 订单PO
	 * @return
	 */
	int updateByPrimaryKey(TOrder order);

	/**
	 * 分页查询首页订单列表
	 * @param param
	 * @return
	 */
	List<TOrder> pageOrder(PageServiceParamView param);
}
