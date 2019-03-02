package com.e_commerce.miscroservice.commons.entity.application.order;

import lombok.Data;

/**
 * <p>
 * 邮寄信息表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-30
 */

//@TableName("store_ExpressInfo")
@Data
public class StoreExpressInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	//@TableId(value="Id", type= IdType.AUTO)
	private Long id;
    /**
     * 订单表OrderNo
     */
	private Long orderNo;
    /**
     * 快递提供商
     */
	private String expressSupplier;
    /**
     * 快递订单号
     */
	private String expressOrderNo;
    /**
     * 快递信息更新时间
     */
	private Long expressUpdateTime;
    /**
     * 创建时间
     */
	private Long createTime;
    /**
     * 更新时间
     */
	private Long updateTime;
    /**
     * 状态
     */
	private Integer status;
	/**
     * 物流信息内容
     */
	private String expressInfo;

}
