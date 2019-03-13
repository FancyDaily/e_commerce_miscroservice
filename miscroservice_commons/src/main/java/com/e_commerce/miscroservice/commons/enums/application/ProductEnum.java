package com.e_commerce.miscroservice.commons.enums.application;

/**
 * 功能描述:求助服务枚举
 * 模块:
 * 项目:时间银行
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱:747052172
 * 创建时间:2018/10/30 下午2:54
 */
public enum ProductEnum {
	/**
	 * 求助服务状态为待审核，等待管理员审核，  刚发布的求助服务都是待审核状态 状态值为1
	 */
    STATUS_WAIT_EXAMINE(1, "待审核"),
    /**
     * 求助服务状态为审核通过，上架状态 状态值为2
     */
    STATUS_UPPER_FRAME(2, "通过审核，上架状态"),
    /**
     * 求助服务状态为手动下架
     */
    STATUS_LOWER_FRAME_MANUAL(3, "手动下架"),
    /**
     * 求助服务状态为自动下架 , 超时或者互助时不够
     */
    STATUS_LOWER_FRAME_TIME_OUT(4, "自动下架"),
    /**
     * 求助服务状态为逻辑删除
     */
    STATUS_DELETE(5, "删除不可见"),
    /**
     * 求助服务状态为审核不通过
     */
    STATUS_EXAMINE_NOPASS(6, "审核未通过"),

    /**
     * 求助服务类型为求助 状态值为1
     */
    TYPE_SEEK_HELP(1, "求助"),
    /**
     * 求助服务类型为服务, 状态值为2
     */
    TYPE_SERVICE(2, "服务"),
    
    /**
     * 求助服务来源为个人 状态值为1
     */
    SOURCE_PERSONAL(1, "个人"),
    /**
     * 求助服务来源为组织 状态值为2
     */
    SOURCE_GROUP(2, "组织"),
    /**
     * 求助服务平台为线上  状态值为1
     */
    PLACE_ONLINE(1, "线上"),
    /**
     * 求助服务平台为线下 状态值为2
     */
    PLACE_UNDERLINE(2, "线下"),
    /**
     * 组织发布时的报名权限是公开的，都可以报名  值为1
     */
    OPEN_AUTH_OPEN(1, "公开对外"),
    /**
     * 组织发布时的报名权限是仅内部的，只有内部可以报名  值为2
     */
    OPEN_AUTH_INNER(2, "仅内部成员可见"),
    /**
     * 收取时间币类型为互助时  值为1
     */
    COLLECT_TYPE_EACHHELP(1, "互助时"),
    /**
     * 收取时间币类型为公益时 值为2
     */
    COLLECT_TYPE_COMMONWEAL(2, "公益时"),
    /**
     * 时间类型为固定时间 值为0
     */
    TIME_TYPE_FIXED(0, "固定时间"),
    /**
     * 时间类型为可重复时间 值为1
     */
    TIME_TYPE_REPEAT(1, "重复时间"),
    
    /**
     * 求助服务没有发布权限的状态码 状态码值为80001
     */
    SUBMIT_NOT_INVITED_PERMISSION_CODE(80001, "没有被邀请的发布权限");
	

    private int value;
    private String desc;

    ProductEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }
    public String getDesc() {
    	return desc;
    }
}
