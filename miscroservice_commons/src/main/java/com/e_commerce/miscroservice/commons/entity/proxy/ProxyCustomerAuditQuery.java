package com.e_commerce.miscroservice.commons.entity.proxy;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerAudit;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 代理商申请审核表
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 15:42
 * @Copyright 玖远网络
 */
@Data
public class ProxyCustomerAuditQuery extends ProxyCustomerAudit{
    private String wxUserIcon;
    private String wxName;
    private String createTimeBefore;
    private String createTimeAfter;
    private String createTimeReadable;
}
