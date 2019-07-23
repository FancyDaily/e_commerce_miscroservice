package com.e_commerce.miscroservice.xiaoshi_proj.order.mapper;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.xiaoshi_proj.order.vo.PageOrderParamView;
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
