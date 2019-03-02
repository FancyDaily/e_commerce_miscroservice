package com.e_commerce.miscroservice.commons.entity.activity;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author hyf
 * @Date 2019/1/8 15:38
 */
@Data
@Table(value = "activity_image_share",commit = "分享图片")
public class ActivityImageShare {

    @Id
    private Long id;

    @Column(value = "share_type",length = 11,commit = "分享类型 默认 0：小程序首页活动",defaultVal = "1001")
    private Integer shareType;

    @Column(value = "type",length = 4,commit = "类型：默认 0：小程序",defaultVal = "0")
    private Integer type;

    @Column(value = "main_map",length = 512,commit = "主图")
    private String mainMap;

    @Column(value = "wx_img",length = 512,commit = "二维码")
    private String wxImg;

    @Column(value = "del_status",defaultVal = "0",length = 4,commit = "是否删除 默认0 未删除  1删除")
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;


}

