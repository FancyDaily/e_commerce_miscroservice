package com.e_commerce.miscroservice.commons.enums.application;


/**
 * 功能描述:事件表枚举类
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
public enum EventEnum {
    /**
     * 事件模版类型是订单被取消
     */
    TEMPLATE_ID_REMOVE_ORDER(1 , "订单被取消"),
    ;

    private String value;
    private int type;
    public String getValue() {
        return value;
    }
    public int getType() {
        return type;
    }
    private EventEnum(int type , String value) {
        this.value = value;
        this.type = type;
    }
}