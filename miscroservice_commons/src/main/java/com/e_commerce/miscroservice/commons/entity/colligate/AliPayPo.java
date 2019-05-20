package com.e_commerce.miscroservice.commons.entity.colligate;




public class AliPayPo {

    /**
     * 支付记录ID
     */
    private Integer id;

    /**
     * 课程编号
     */
    private Long subjectId;

    /**
     * 课程名称
     */
    private String subjectName;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单实付金额
     */
    private Double payMoney;

    /**
     * 接口名称
     */
    private String method;

    // --> 业务参数
    /**
     * 该交易在支付宝系统中的交易流水号
     */
    private String tradeNo;

    /**
     * 收款支付宝账号对应的支付宝唯一用户号
     */
    private String sellerId;

    /**
     * 当面付预下单请求生成的二维码码串
     */
    private String qrCode;

    /**
     * 商户网站唯一订单号
     */
    private String outTradeNo;

    // <-- 业务参数

    /**
     * 交易成功失败
     */
    private Boolean isSuccess;

    /**
     * 网关返回码
     */
    private String code;

    /**
     * 网关返回码描述
     */
    private String msg;

    /**
     * 阿里业务返回码
     */
    private String subCode;

    /**
     * 阿里业务返回码描述
     */
    private String subMsg;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Double payMoney) {
        this.payMoney = payMoney;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }
}
