package com.e_commerce.miscroservice.commons.entity.application.order;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/21 14:37
 * @Copyright 玖远网络
 */
@Data
@Table("shop_order_after_sale")
public class ShopOrderAfterSale {
    @Id
    private Long id;

    @Column(value = "after_sale_id", length = 256, commit = "售后订单号")
    private String afterSaleId;
    @Column(value = "refund_remark", length = 256, commit = "退款说明")
    private String refundRemark;
    @Column(value = "msg", length = 256, commit = "理由：退款失败理由等")
    private String msg;
    @Column(value = "store_id", length = 255, commit = "商家Id")
    private Long storeId;
    @Column(value = "member_id", length = 255, commit = "会员Id")
    private Long memberId;
    @Column(value = "name", length = 256, commit = "姓名")
    private String name;
    @Column(value = "refund_count", length = 20, commit = "退款数量")
    private Integer refundCount;
    @Column(value = "refund_name", length = 256, commit = "退款商品名称")
    private String refundName;
    @Column(value = "phone", length = 256, commit = "手机号")
    private String phone;
    @Column(value = "order_id", length = 256, commit = "订单号")
    private String orderId;

    @Column(value = "apply_time", length = 20, commit = "申请时间")
    private Long applyTime;

    @Column(value = "operate_time", length = 20, commit = "操作时间：退款时间，拒绝时间等")
    private Long operateTime;

    @Column(value = "type", length = 4, defaultVal = "0", commit = "退款类型：0 默认 退款，1 退货退款")
    private Integer type;

    @Column(value = "status", length = 4, defaultVal = "0", commit = "退款状态：0 默认 退款中，1 退货成功 2 退款失败")
    private Integer status;

    @Column(value = "apply_back_money", length = 11, precision = 2, defaultVal = "0.00", commit = "申请退款金额")
    private Double applyBackMoney;

    @Column(value = "back_money", length = 11, precision = 2, defaultVal = "0.00", commit = "退款金额")
    private Double backMoney;

    @Column(value = "sku_id", length = 20, commit = "skuId")
    private Long skuId;

    @Column(value = "reasons", length = 2048, commit = "退款理由")
    private String reasons;

    @Column(value = "img_first", length = 1000, commit = "图片凭证")
    private String imgFirst;

    @Column(value = "img_second", length = 256, commit = "图片凭证")
    private String imgSecond;

    @Column(value = "img_third", length = 256, commit = "图片凭证")
    private String imgThird;

    @Column(value = "img_fourth", length = 256, commit = "图片凭证")
    private String imgFourth;

    @Column(value = "img_fifth", length = 256, commit = "图片凭证")
    private String imgFifth;

    @Column(value = "del_status", defaultVal = "0", length = 4, commit = "是否删除:0 默认 正常, 1 删除")
    private Integer delStatus;

    @Column(value = "create_time", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE, commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE, commit = "修改时间")
    private Timestamp updateTime;
}
