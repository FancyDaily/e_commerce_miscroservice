package com.e_commerce.miscroservice.commons.entity.activity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/12/18 15:32
 * @Copyright 玖远网络
 */
@Data
@Table(value = "lottery_draw_activity")
public class LotteryDrawActivity {

    @Id
    private Long id;

    @Column(value = "banner", length = 512, defaultVal = "0", commit = "banner图")
    private String banner;

    @Column(value = "button_pic", length = 512, defaultVal = "0", commit = "商品图片")
    private String buttonPic;

    @Column(value = "type",length = 11,commit = "活动类型")
    private Integer type;


    @Column(value = "type_value",length = 256,commit = "活动类型")
    private String typeValue;

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
