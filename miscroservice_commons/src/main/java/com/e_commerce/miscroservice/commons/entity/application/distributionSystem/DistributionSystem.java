package com.e_commerce.miscroservice.commons.entity.application.distributionSystem;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;


@Data
@Table("yjj_distribution_system")
public class DistributionSystem {

    @Id
    private Long id;

    @Column(value = "grade",commit = "分销级别 0 无等级 1 店长 2分销商 3合伙人",defaultVal = "0")
    private Integer grade;

    @Column(value = "higher_up",commit = "上级id")
    private Long  higherUp;
    @Column(value = "top_up",commit = "上上级id")
    private Long  topUp;

    @Column(value = "user_id",commit = "用户id")
    private Long  userId;

    @Column(value = "distributor_id",commit = "分销商")
    private Long  distributorId;

    @Column(value = "partner_id",commit = "合伙人id")
    private Long  partnerId;

    @Column(value = "team_user_count",commit = "团队人数",defaultVal = "0")
    private Long  teamUserCount;

    @Column(value = "fans_user_count",commit = "粉丝人数",defaultVal = "0")
    private Long  fansUserCount;


    /**
     * 状态 0:正常,1:失效
     */
    @Column(value = "del_status", commit = "状态 0:正常,1:失效", defaultVal = "0", length = 4)
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy= DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
    private Timestamp updateTime;

}
