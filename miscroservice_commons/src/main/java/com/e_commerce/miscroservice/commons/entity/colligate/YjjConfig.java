package com.e_commerce.miscroservice.commons.entity.colligate;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *描述 config 配置字典表
 * @author hyq
 * @date 2018/9/26 13:52
 */
@Data
@Table("yjj_config")
public class YjjConfig implements Serializable {
    @Id
    private Long id;

    @Column(value = "key_name",commit = "名称",length = 36)
    private String keyName;

    @Column(value = "key_value",commit = "比例值",length = 36)
    private String keyValue;

    @Column(value = "key_desc",commit = "描述",length = 255)
    private String keyDesc;

    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy= DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
    private Timestamp updateTime;


}
