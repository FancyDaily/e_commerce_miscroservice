package com.e_commerce.miscroservice.commons.enums.application;


/**
 * 功能描述:举报表枚举类
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
public enum ReportEnum {
    /**
     * 投诉关系状态为举报服务， 状态值为0
     */
    TPYE_SERVICE(0 , "举报服务"),
    /**
     * 投诉关系状态为举报订单 状态值为1
     */
    TYPE_ORDER(1, "投诉订单"),
    /**
     * 投诉关系状态为系统反馈和建议 状态值为2
     */
    TYPE_SYSTEM(2, "反馈和建议"),
    ;

    private String value;
    private int type;
    public String getValue() {
        return value;
    }
    public int getType() {
        return type;
    }
    private ReportEnum(int type , String value) {
        this.value = value;
        this.type = type;
    }
}