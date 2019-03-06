package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import org.springframework.stereotype.Repository;

/**
 * @author 马晓晨
 * @date 2019/3/5
 */
@Repository
public class OrderDaoImpl implements OrderDao {

	@Override
	public int saveOneOrder(TOrder order) {
		return MybatisOperaterUtil.getInstance().save(order);
	}
}
