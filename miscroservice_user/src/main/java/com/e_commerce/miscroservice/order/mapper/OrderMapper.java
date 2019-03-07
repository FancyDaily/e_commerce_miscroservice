package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.order.po.TOrder;
import com.e_commerce.miscroservice.order.vo.PageOrderParamView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

	/**
	 * 分页查询首页订单
	 * @param param 查询参数
	 * @return order列表
	 */
	List<TOrder> pageOrder(PageOrderParamView param);
}
