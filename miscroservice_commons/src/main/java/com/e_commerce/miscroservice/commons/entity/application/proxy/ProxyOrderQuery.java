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
 *描述 代理订单表
 * @author hyq
 * @date 2018/9/18 13:52
 */
@Data
public class ProxyOrderQuery extends  ProxyOrder {

    private String oneLevelName;

    private int oneLevelSelfRole;

    private String twoLevelName;

    private String rewardMoney;

    /*
     * 自己的名字
     */
    private String selfName;

    private int selfRole;

    private Long refereeUserId;

    private Long recommonUserId;

    private Long refereParentId;

    private String goodsName;

    private String minMoney;

    private String maxMoney;

    private String startTime;

    private String endTime;

    private int pageNum;

    private int pageSize;

}
