package com.e_commerce.miscroservice.commons.entity.application.order;


import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 供应商收货地址表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-23
 */
//@TableName("supplier_delivery_address")
	@Data
public class SupplierDeliveryAddress {

    private static final long serialVersionUID = 1L;
    
    public static final int NORMAL_STATUS = 0;
    
    public static final int DEL_STATUS = -1;

    /**
     * 主键id
     */
	//@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 供应商id
     */
	//@TableField("supplier_id")
	private Long supplierId;
    /**
     * 收货信息名称
     */
	//@TableField("receipt_info_name")
	private String receiptInfoName;
    /**
     * 售后收货人姓名
     */
	//@TableField("receiver_name")
	private String receiverName;
    /**
     * 售后收货联系电话
     */
	//@TableField("phone_number")
	private String phoneNumber;
    /**
     * 售后收货地址
     */
	private String address;
    /**
     *   状态  -1：删除   0：正常
     */
	private Integer status;
    /**
     *   状态     0：非默认收货地址，1：默认收货地址
     */
	//@TableField("default_address")
	private Integer defaultAddress;
    /**
     * 创建时间
     */
	//@TableField("create_time")
	private Long createTime;
    /**
     * 修改时间
     */
	//@TableField("update_time")
	private Long updateTime;
}
