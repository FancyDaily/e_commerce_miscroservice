package com.e_commerce.miscroservice.commons.entity.application.proxy;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;
import tk.mybatis.mapper.annotation.ColumnType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * 描述 代理商品表
 *
 * @author hyq
 * @date 2018/9/18 13:52
 */
@Data
public class ProxyGoodsQuery extends ProxyGoods{



}
