package com.e_commerce.miscroservice.commons.entity.application.proxy;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 描述 代理返现表
 *
 * @author hyq
 * @date 2018/9/18 13:52
 */
@Data
@Table("yjj_proxy_reward")
@javax.persistence.Table(name = "yjj_proxy_reward")
public class ProxyReward implements Serializable {
    @Id
    @javax.persistence.Id
    private Long id;

    @Column(value = "order_no", commit = "订单号", length = 36)
    private String orderNo;

    @Column(value = "order_money", commit = "订单金额", length = 7, precision = 2)
    private BigDecimal orderMoney;

    @Column(value = "order_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE, commit = "订单时间")
    private Timestamp orderTime;

    @Column(value = "user_id", commit = "受益人", length = 20)
    private Long userId;

    @Column(value = "order_user_id", commit = "生成订单userId", length = 20)
    private Long orderUserId;

    @Column(value = "order_user_role",commit = "1 市代理  0 客户 2 县代理",defaultVal ="0" ,length = 2)
    private Integer orderUserRole;

    @Column(value = "is_grant",commit = "是否发放收益 0 未放 1 已放",defaultVal ="0" ,length = 2)
    private Integer isGrant;

    @Column(value = "reward_money", commit = "返现金额", length = 7, precision = 2)
    private BigDecimal rewardMoney;

    @Column(value = "reward_rate", commit = "返现费率", length = 7, precision = 4)
    private BigDecimal rewardRate;

    @Column(value = "create_time", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE, commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE, commit = "修改时间")
    private Timestamp updateTime;


}
