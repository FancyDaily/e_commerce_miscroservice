package com.e_commerce.miscroservice.commons.enums.application;

/**
 * 功能描述: 成长值相关枚举类
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月6日 下午6:00:08
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public enum GrowthValueEnum {    //TODO 数值还没有确定
    //new
    //一次性
    GROWTH_TYPE_UNREP_REGISTER(10, 0,0, "注册", 10, 1, 10, 10),    //注册
    GROWTH_TYPE_UNREP_AUTH(11, 0, 1,"实名认证", 10, 1, 10, 10),    //实名认证
    GROWTH_TYPE_UNREP_PAGE(12, 0, 2,"用户个人信息", 15, 1, 15, 15),    //完善个人主页信息
    GROWTH_TYPE_UNREP_SKILL(13, 0, 3,"首次添加个人技能", 5, 1, 5, 5),    //首次添加技能
    GROWTH_TYPE_UNREP_FIRST_ITEM_DONE(14, 0, 4,"首次完成互助", 10, 1, 10, 10),    //首次完成互助
    GROWTH_TYPE_UNREP_FIRST_HELP_SEND(15, 0, 5,"首次发布求助", 10, 1, 10, 10),    //首次发布求助
    GROWTH_TYPE_UNREP_FIRST_SERV_SEND(16, 0, 6,"首次发布服务", 10, 1, 10, 10),    //首次发布服务
    GROWTH_TYPE_UNREP_FIRST_COMMENT(17, 0, 7,"首次对互助进行评价", 5, 1, 5, 5),        //首次对互助进行评价
    GROWTH_TYPE_UNREP_FIRST_JOIN_COMPANY(18, 0, 8,"首次加入组织", 5, 1, 5, 5),    //首次加入组织
    GROWTH_TYPE_UNREP_FIRST_PUBLIC_WELFARE_ACTY_DONE(19, 9,0, "首次完成公益活动", 20, 1, 20, 20),    //首次完成公益活动
    //重复 -> TODO 总上限还未设置
    GROWTH_TYPE_REP_INVITE(30, 0, 20,"邀请好友注册成功", 10, 1, -1, -1),    //邀请好友注册成功
    GROWTH_TYPE_REP_SIGN_UP(31, 0, 21,"每日签到", 1, 1, 1, -1),    //每日签到
    GROWTH_TYPE_REP_HELP_DONE(32, 0, 22,"完成求助", 2, 1, 10, -1),    //完成求助
    GROWTH_TYPE_REP_SERV_DONE(33, 0, 23,"完成服务", 2, 1, 10, -1),    //完成服务
    GROWTH_TYPE_REP_COMMENT(34, 0, 24,"评价", 2, 1, 10, -1),        //评价
    GROWTH_TYPE_REP_SHARE_PRODUCT(35, 0, 25,"每日首次分享互助", 1, 1, 1, -1),    //分享互助
    GROWTH_TYPE_REP_PUBLIC_WELFARE_ACTY_DONE(36, 0, 26,"每日首次参加公益活动", 5, 1, 5, -1);    //每日首次完成公益活动


    // out
//	GROWTH_TYPE_TIME_INVALID(20, "保级失败-成长值失效", 500, 0, -1), // 保级失败 TODO 可能需要按照等级发生变化 TODO 当前版本不存在保级体制

//	GROWTH_TYPE_BAD_REVIEW(21, 0, "差评扣减", 10, 0, -1), // 差评
//	GROWTH_TYPE_COMPLAINT(22, 0, "投诉扣减", 50, 0, -1), // 投诉
//	GROWTH_TYPE_OUT_OF_LINE(23, 0, "违规惩罚扣减", 50, 0, -1); // 违规

    // 代号
    private int code;
    // 预留的副代号
    private int subCode;
    // 任务代号
    private int taskCode;
    // 信息
    private String message;
    // 成长值
    private Integer price;
    // 出|入
    private Integer inOut;
    // 单日获取上限
    private Integer dailyMaxIn;
    // 获取上限
    private Integer maxIn;

    public int getCode() { return code; }

    public int getSubCode() {
        return subCode;
    }

    public int getTaskCode() { return taskCode; }

    public String getMessage() {
        return message;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getInOut() {
        return inOut;
    }

    public Integer getDailyMaxIn() { return dailyMaxIn; }

    public Integer getMaxIn() {
        return maxIn;
    }

    private GrowthValueEnum(int code, int subCode, int taskCode, String message, Integer price, Integer inOut, Integer dailyMaxIn, Integer maxIn) {
        this.code = code;
        this.subCode = subCode;
        this.taskCode = taskCode;
        this.message = message;
        this.price = price;
        this.inOut = inOut;
        this.dailyMaxIn = dailyMaxIn;
        this.maxIn = maxIn;
    }
}
