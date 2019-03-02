package com.e_commerce.miscroservice.commons.entity.application.order;


import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 供应商客户表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-13
 */
@Data
public class SupplierCustomer {


    /**
     * 主键id
     */
	//@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 客户名称：店铺名称
     */
	private String businessName;
    /**
     * 客户姓名：法人
     */
	//@TableField("customer_name")
	private String customerName;
	/**
	 * 供应商Id
	 */
	//@TableField("supplier_id")
	private Long supplierId;
	/**
	 * 门店id
	 */
	//@TableField("store_id")
	private Long storeId;
    /**
     * 备注名
     */
	//@TableField("remark_name")
	private String remarkName;
    /**
     * 手机号码
     */
	//@TableField("phone_number")
	private String phoneNumber;
    /**
     * 分组Id
     */
	//@TableField("group_id")
	private Long groupId;
    /**
     * 所在省份
     */
	private String province;
    /**
     * 所在城市
     */
	private String city;
    /**
     * 所在区县
     */
	private String businessAddress;
    /**
     *   状态  0：新客户   1：老客户
     */
	//@TableField("customer_type")
	private Integer customerType;
    /**
     *   状态  -1：删除   0：正常
     */
	private Integer status;
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
