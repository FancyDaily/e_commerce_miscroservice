package com.e_commerce.miscroservice.commons.entity.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/13 18:42
 * @Copyright 玖远网络
 */
@Data
public class YjjOrder {

    /**
     * `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     *   `OrderNo` varchar(30) NOT NULL COMMENT '订单编号，前台展示，格式为当前时间yyyyMMddHHmmssSSS后面加6位字母数字随机串',
     *   `UserId` bigint(20) NOT NULL COMMENT '用户id',
     *   `OrderType` tinyint(4) NOT NULL COMMENT '订单类型：0-结算支付 1-回寄旧衣',
     *   `OrderStatus` tinyint(4) NOT NULL COMMENT '订单付款状态：参照参照OrderStatus',
     *   `TotalMoney` decimal(10,2) NOT NULL COMMENT '订单金额总价，不包含邮费',
     *   `TotalExpressMoney` decimal(10,2) NOT NULL COMMENT '邮费总价',
     *   `ExpressInfo` varchar(1024) NOT NULL COMMENT '邮寄信息',
     *   `AvalCoinUsed` int(11) NOT NULL COMMENT '使用的激活玖币',
     *   `UnavalCoinUsed` int(11) NOT NULL COMMENT '使用的未激活玖币',
     *   `PayAmountInCents` int(11) NOT NULL COMMENT '用户为该笔订单支付的钱总数',
     *   `Remark` varchar(1024) DEFAULT NULL COMMENT '用户订单备注',
     *   `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
     *   `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
     *   `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
     *   `ExpressSupplier` varchar(32) DEFAULT NULL COMMENT '快递提供商',
     *   `ExpressOrderNo` varchar(64) DEFAULT NULL COMMENT '快递订单号',
     *   `ExpressUpdateTime` bigint(20) DEFAULT NULL COMMENT '快递信息更新时间',
     *   `Sended` tinyint(4) DEFAULT '0' COMMENT '回寄订单是否已填写资料标记',
     *   `Platform` varchar(30) DEFAULT NULL COMMENT '生成订单平台',
     *   `PlatformVersion` varchar(45) DEFAULT NULL COMMENT '生成订单平台版本号',
     *   `Ip` varchar(50) DEFAULT NULL COMMENT '客户端ip',
     *   `PaymentNo` varchar(50) DEFAULT NULL COMMENT '第三方的支付订单号',
     *   `PaymentType` tinyint(4) DEFAULT '0' COMMENT '使用的第三方支付方式，参考常量PaymentType',
     */


    private Long id;
    private Long orderNo;
    private Long userId;
    private Integer orderType;
    private Integer orderStatus;
    private BigDecimal totalMoney;
    private BigDecimal totalExpressMoney;
    private String expressInfo;
    private Integer avalCoinUsed;
    private Integer unavalCoinUsed;
    private Integer payAmountInCents;
    private String remark;
    private Integer status;
    private Long createTime;
    private Long updateTime;
    private String expressSupplier;
    private String expressOrderNo;
    private Long expressUpdateTime;
    private Integer sended;
    private String platform;
    private String platformVersion;
    private String ip;
    private String paymentNo;
    private Integer paymentType;


}
