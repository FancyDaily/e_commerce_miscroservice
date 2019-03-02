package com.e_commerce.miscroservice.commons.entity.application.system;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author Charlie
 * @since 2018-09-29
 */
@Data
@Table("yjj_data_dictionary")
public class DataDictionary {


	@Id
	private Long id;
    /**
     * 编码唯一
     */
	private String code;
    /**
     * 分组编码
     */
	private String groupCode;
    /**
     * 值
     */
	private String val;
    /**
     * 中文名称
     */
	private String name;

	private String comment;

	/**
	 * 启用状态:0 禁用 1启用
	 */
	private Integer status;

	private String createUserId;

	private String createUserName;

	private Date createTime;

	private String lastUserId;

	private String lastUserName;

	private Date lastUpdateTime;

	private Long parentId;

}
