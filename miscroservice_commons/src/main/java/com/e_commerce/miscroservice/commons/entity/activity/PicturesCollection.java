package com.e_commerce.miscroservice.commons.entity.activity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 图片集合表
 * @author hyf
 * @version V1.0
 * @date 2018/12/20 14:07
 * @Copyright 玖远网络
 */
@Data
@Table("pictures_collection")
public class PicturesCollection {
    @Id
    private Long id;

    @Column(value = "need_pic_id",commit = "相关联的id",length = 20)
    private Long needPicId;

    @Column(value = "picture",commit = "图片地址",length = 1024)
    private String picture;

    @Column(value = "type_code",commit = "类型 参考PictureTypeEnums 例如：1001为抽奖活动图片内容 1002：抽奖活动banner 1003",length = 11)
    private Integer typeCode;

    @Column(value = "type_value",commit = "类型名称",length = 256)
    private String typeValue;

    @Column(value = "sort",commit = "图片排序",defaultVal = "0",length = 11)
    private Integer sort;
    /**
     * 是否删除 0 正常  1 删除
     */
    @Column(value = "del_status", length = 4, defaultVal = "0", commit = "是否删除 0 正常  1 删除")
    private Integer delStatus;

    /**
     * 创建时间
     */
    @Column(value = "create_time", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE, commit = "创建时间")
    private Timestamp createTime;

    /**
     * 更新时间
     */
    @Column(value = "update_time", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE, commit = "更新时间")
    private Timestamp updateTime;

}
