package com.e_commerce.miscroservice.commons.entity.application.order;

import lombok.Data;

/**
 * <p>
 * 快递公司管理
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-25
 */
//@TableName("yjj_ExpressSupplier")
@Data
public class ExpressSupplier {

	//@TableId(value="Id", type= IdType.AUTO)
	private Long id;
    /**
     * 快递供应商中文名
     */
	private String cnName;
    /**
     * 快递供应商英文名
     */
	private String engName;
    /**
     * 网上查询快递信息，快递信息链接前缀 
     */
	private String queryLink;
    /**
     * 状态 -1：删除，0：正常
     */
	private Integer status;

	private Long createTime;
	private Long updateTime;




}
