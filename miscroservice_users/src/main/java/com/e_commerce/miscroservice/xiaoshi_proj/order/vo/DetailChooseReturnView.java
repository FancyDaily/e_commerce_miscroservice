package com.e_commerce.miscroservice.xiaoshi_proj.order.vo;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import lombok.Data;

import java.util.List;

/**
 * 选人详情返回的view视图
 * @author 马晓晨
 * @date 2019/3/12
 */
@Data
public class DetailChooseReturnView {
	private TOrder order;
	private List<BaseUserView> listUser;
}
