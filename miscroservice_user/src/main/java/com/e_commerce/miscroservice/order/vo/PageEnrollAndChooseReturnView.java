package com.e_commerce.miscroservice.order.vo;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import lombok.Data;

/**
 * @author 马晓晨
 * @date 2019/3/7
 */
@Data
public class PageEnrollAndChooseReturnView {
	private TOrder order;
	/**
	 * 封面图
	 */
	private String porductCoverPic;
	/**
	 * 显示状态: 1、已结束  2、已取消 3、待选人 4、被拒绝  5、已报名 6、已入选
	 */
	private Integer status;
}
