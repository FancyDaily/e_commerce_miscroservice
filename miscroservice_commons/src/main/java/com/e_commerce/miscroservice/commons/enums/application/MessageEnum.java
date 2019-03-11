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
public enum MessageEnum {
    /**
     * 消息类型为图片  状态值为0
     */
    TYPE_PHOTO(0 , "图片"),
    /**
     * 消息类型为文本  状态值为1
     */
    TYPE_TEXT(1 ,"文本"),
    ;

    private String value;
    private int type;
    public String getValue() {
        return value;
    }
    public int getType() {
        return type;
    }
    private MessageEnum(int type , String value) {
        this.value = value;
        this.type = type;
    }
}