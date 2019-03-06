package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import com.e_commerce.miscroservice.product.vo.PageServiceParamView;
import org.springframework.stereotype.Repository;

import java.util.List;

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
	public List<TService> pageOrder(PageServiceParamView param) {
		return null;
	}
}
