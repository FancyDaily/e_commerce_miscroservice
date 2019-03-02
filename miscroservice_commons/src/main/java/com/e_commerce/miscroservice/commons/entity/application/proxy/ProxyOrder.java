package com.e_commerce.miscroservice.commons.entity.application.proxy;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
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
@Table("yjj_proxy_order")
@javax.persistence.Table(name = "yjj_proxy_order")
public class ProxyOrder implements Serializable {
    @Id
    @javax.persistence.Id
    private Long id;

    @Column(value = "order_no",commit = "订单号",length = 36)
    private String orderNo;

    @Column(value = "user_id",commit = "用户ID",length = 20)
    private Long userId;

    @Column(value = "goods_id",commit = "商品ID",length = 20)
    private Long goodsId;

    @Column(value = "goods_type_id",commit = "商品类型ID",length = 20)
    private Long goodsTypeId;

    @Column(value = "goods_name",commit = "商品名称",length = 36)
    private String goodsName;

    @Column(value = "goods_price",commit = "商品价格",length = 36,precision = 2)
    private BigDecimal goodsPrice;

    @Column(value = "goods_time_num", commit = "时间数量", length = 2, defaultVal = "1")
    private Integer goodsTimeNum;

    @Column(value = "goods_time_unit", commit = "时间单位 0 年 1 月 2 日", length = 2, defaultVal = "0")
    private Integer goodsTimeUnit;

    @Column(value = "goods_images",commit = "商品主图",length = 256)
    private String goodsImages;

    @Column(value = "source",commit = "下单来源 1 公众号",defaultVal ="1" ,length = 2)
    private Integer source;

    @Column(value = "is_grant",commit = "是否发放收益 0 未放 1 已放",defaultVal ="0" ,length = 2)
    private Integer isGrant;

    @Column(value = "is_profit",commit = "是否是收益订单 0 否 1 是",defaultVal ="0" ,length = 2)
    private Integer isProfit;

    @Column(value = "status",commit = "订单状态 0 处理中 1 成功  2 失败  9 新订单",defaultVal ="0" ,length = 2)
    private Integer status;

    @Column(value = "pay_way",commit = "支付方式 1 微信",defaultVal ="1" ,length = 2)
    private Integer payWay;

    @Column(value = "pay_order_no",commit = "交易订单号",length = 50)
    private String payOrderNo;

    @Column(value = "count",commit = "购买数量",length = 4)
    private Integer count;

    @Column(value = "price",commit = "购买价格",length = 7,precision = 2)
    private BigDecimal price;

    @Column(value = "favorable_price",commit = "优惠金额",length = 7,precision = 2,defaultVal = "0")
    private BigDecimal favorablePrice;

    @Column(value = "actual_price",commit = "实际付款价格",length = 7,precision = 2)
    private BigDecimal actualPrice;

    @Column(value = "receiver_name",commit = "收件人姓名",length = 40)
    private String receiverName;

    @Column(value = "receiver_phone",commit = "收件人手机号",length = 11)
    private String receiverPhone;

    @Column(value = "receiver_address",commit = "收件人手地址",length = 255)
    private String receiverAddress;

    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy= DbHandler.DateGeneStrategy.UPDATE,commit = "修改时间")
    private Timestamp updateTime;

}
