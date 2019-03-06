package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import com.e_commerce.miscroservice.order.mapper.OrderMapper;
import com.e_commerce.miscroservice.order.vo.PageServiceParamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 马晓晨
 * @date 2019/3/5
 */
@Repository
public class OrderDaoImpl implements OrderDao {

	@Autowired
	OrderMapper orderMapper;

	@Override
	public int saveOneOrder(TOrder order) {
		return MybatisOperaterUtil.getInstance().save(order);
	}

	@Override
	public TOrder selectByPrimaryKey(Long id) {
		return MybatisOperaterUtil.getInstance().findOne(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
				.eq(TOrder::getId, id));
	}

	@Override
	public int updateByPrimaryKey(TOrder order) {
		return MybatisOperaterUtil.getInstance().update(order, new MybatisSqlWhereBuild(TOrder.class)
				.eq(TOrder::getId, order.getId()));
	}

	@Override
	public List<TOrder> pageOrder(PageServiceParamView param) {
		return orderMapper.pageOrder(param);
	}
}
