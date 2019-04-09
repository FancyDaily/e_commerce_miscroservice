package com.e_commerce.miscroservice.order.vo;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TService;
import lombok.Data;

/**
 * @author 马晓晨
 * @date 2019/4/9
 */
@Data
public class GroupChooseOrderView {
	private TOrder order;
	private TService service;
}
