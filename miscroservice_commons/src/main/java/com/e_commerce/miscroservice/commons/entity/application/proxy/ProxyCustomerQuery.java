package com.e_commerce.miscroservice.commons.entity.application.proxy;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 描述  代理查询集合
 * @author hyq
 * @date 2018/10/11 17:57
 */
@Data
public class ProxyCustomerQuery extends ProxyCustomer{

    private String customerNum;

    private String agentCustomerNum;

    private String customerOrderNum;

    private String agentCustomerOrderNum;

    private String customerMoney;

    private String customerMoneyReward;

    private String agentCustomerMoney;

    private String agentCustomerMoneyReward;

    private String alreadyMoney;

    private String noSendMoney;

    private String startTime;

    private String endTime;

    private Integer pageSize;

    private Integer pageNum;

}
