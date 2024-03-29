package com.e_commerce.miscroservice.commons.enums.application;


/**
 * 功能描述:订单关系表枚举类
 * 模块:
 * 项目:时间银行
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱:414368243@qq.com
 * 创建时间:2019/3/4 下午7:22
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public enum OrderRelationshipEnum {
    /**
     * 订单关系状态为初始状态，没报名参与 只算收藏或者投诉了 状态值为0
     */
    STATUS_NO_STATE(0 , "初始状态"),
    /**
     * 订单关系状态为待确认，报名成功未被选择 状态值为1
     */
    STATUS_WAIT_CHOOSE(1, "待确认"),
    /**
     * 订单关系状态为被确认，被发布者选择 状态值为2
     */
    STATUS_ALREADY_CHOOSE(2, "被确认"),
    /**
     * 订单关系状态为被拒绝，被发布者拒绝  状态值为3
     */
    STATUS_NOT_CHOOSE(3, "被拒绝"),
    /**
     * 订单关系状态为取消报名，报名者取消报名  状态值为4
     */
    STATUS_REMOVE_ENROLL(4, "取消报名"),
    /**
     * 订单关系状态为报名者取消订单，  状态值为5
     */
    STATUS_ENROLL_CANCEL(5, "报名者取消"),
    /**
     * 订单关系状态为发布者取消订单  状态值为6
     */
    STATUS_PUBLISH_CANCEL(6, "发布者取消"),
    /**
     * 订单关系状态为进行中，本版本不用  状态值为7
     */
    STATUS_UNDER_WAY(7, "进行中"),
    /**
     * 订单关系状态为待支付，本版本不用  状态值为8
     */
    STATUS_WAIT_PAY(8, "待支付"),
    /**
     * 订单关系状态为双方均未评价，  状态值为9
     */
    STATUS_WAIT_REMARK(9, "双方未评价"),
    /**
     * 订单关系状态为已完成，即双方均评价  状态值为10
     */
    STATUS_IS_COMPLETED(10, "已完成"),
    /**
     * 订单关系状态为已发布评价  状态值为11
     */
    STATUS_IS_REMARK(11, "已评价"),
    /**
     * 订单关系状态被对方评价  状态值为12
     */
    STATUS_BE_REMARK(12, "被评价"),
    /**
     * 订单关系状态为求助者支付0分组，关系不成立  状态值为13
     */
    STATUS_NOT_ESTABLISHED(13, "关系不成立"),
    /**
     * 商品投诉状态为未投诉 初始状态  状态值为0
     */
    SERVICE_REPORT_IS_NO(0 ,"未投诉"),
    /**
     * 商品投诉状态为投诉  状态值为1
     */
    SERVICE_REPORT_IS_TURE(1 ,"已投诉"),
    /**
     * 商品投诉状态为被投诉  状态值为2
     */
    SERVICE_REPORT_IS_BEREPORT(2 ,"被投诉"),
    /**
     * 商品投诉状态为已解决  状态值为3
     */
    SERVICE_REPORT_IS_SOLVE(3 ,"投诉解决"),
    /**
     * 订单投诉状态为未投诉 初始状态  状态值为0
     */
    ORDER_REPORT_IS_NO(0 ,"未投诉"),
    /**
     * 订单投诉状态为投诉  状态值为1
     */
    ORDER_REPORT_IS_TURE(1 ,"已投诉"),
    /**
     * 订单投诉状态为被投诉  状态值为2
     */
    ORDER_REPORT_IS_BEREPORT(2 ,"被投诉"),
    /**
     * 订单投诉状态为已解决  状态值为3
     */
    ORDER_REPORT_EACH_OTHER(3 ,"互相投诉"),
    /**
     * 订单投诉状态为已解决  状态值为4
     */
    ORDER_REPORT_IS_SOLVE(4 ,"投诉解决"),
    /**
     * 商品收藏状态为未收藏 初始状态  状态值为0
     */
    SERVICE_COLLECTION_IS_NO(0 ,"未收藏"),
    /**
     * 商品收藏状态为收藏  状态值为1
     */
    SERVICE_COLLECTION_IS_TURE(1 ,"已收藏"),
    /**
     * 商品收藏状态为取消收藏  状态值为2
     */
    SERVICE_COLLECTION_IS_CANCEL(2 ,"取消收藏"),
    /**
     * 商品类型为求求助 类型值为1
     */
    SERVICE_TYPE_HELP(1,"求助"),
    /**
     * 商品类型为服务 类型值为2
     */
    SERVICE_TYPE_SERV(2,"服务"),
    /**
     * 订单关系状态为未签到，默认状态  状态值为0
     */
    SIGN_TYPE_NO(0 , "未签到"),
    /**
     * 订单关系状态为已签到，开始后这样  状态值为1
     */
    SIGN_TYPE_YES(1 , "已签到"),
    ;

    private String value;
    private int type;
    public String getValue() {
        return value;
    }
    public int getType() {
        return type;
    }
    private OrderRelationshipEnum(int type , String value) {
        this.value = value;
        this.type = type;
    }
}