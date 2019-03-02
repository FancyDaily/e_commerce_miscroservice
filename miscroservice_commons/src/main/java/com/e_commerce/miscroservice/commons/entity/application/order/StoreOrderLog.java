package com.e_commerce.miscroservice.commons.entity.application.order;


import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单日志记录
 * </p>
 *
 * @author 赵兴林
 * @since 2017-12-25
 */
//@TableName("store_OrderLog")
@Data
public class StoreOrderLog {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
	//@TableId(value="Id", type= IdType.AUTO)
	private Long id;
    /**
     * 关联User表的userId
     */
	private Long storeId;
    /**
     * 关联Order表的id
     */
	private Long orderNo;
    /**
     * 老的订单状态
     */
	private Integer oldStatus;
    /**
     * 更新的订单状态
     */
	private Integer newStatus;
    /**
     * 记录创建时间
     */
	private Long createTime;
	private Long updateTime;




}
