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
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public enum GrowthValueEnum {

	// in
	GROWTH_TYPE_PUBLISH_SERV_REQUIRE(10, 0, "发布求助", 10, 1, 50), // 发布求助
	GROWTH_TYPE_ACCEPT_SERV_REQUIRE(11, 0, "承接求助", 15, 1, 75), // 承接求助
	GROWTH_TYPE_PUBLISH_SERV_SERVICE(12, 0, "发布服务", 15, 1, 75), // 发布服务
	GROWTH_TYPE_ACCEPT_ACTY_WELFARE(13, 0, "完成公益活动", 20, 1, 40), // 承接活动
	GROWTH_TYPE_INVITE_BONUS(14, 0, "成功邀请新用户", 15, 1, 75), // 邀请奖励
	GROWTH_TYPE_SIGNUP_BONUS(16, 0, "每日签到", 15, 1, 15), // 日签奖励
	GROWTH_TYPE_SHARE_BONUS(15, 0, "分享", 10, 1, 50), // 分享奖励
	GROWTH_TYPE_COMMENT_BONUS(17, 0, "完成求助 获得好评", 15, 1, 50);// 发布评价奖励	

	// out
//	GROWTH_TYPE_TIME_INVALID(20, "保级失败-成长值失效", 500, 0, -1), // 保级失败 TODO 可能需要按照等级发生变化 TODO 当前版本不存在保级体制
	
//	GROWTH_TYPE_BAD_REVIEW(21, 0, "差评扣减", 10, 0, -1), // 差评
//	GROWTH_TYPE_COMPLAINT(22, 0, "投诉扣减", 50, 0, -1), // 投诉
//	GROWTH_TYPE_OUT_OF_LINE(23, 0, "违规惩罚扣减", 50, 0, -1); // 违规

	// 代号
	private int code;
	// 预留的副代号
	private int subCode;
	// 信息
	private String message;
	// 成长值
	private Integer price;
	// 出|入
	private Integer inOut;
	// 单日获取上限
	private Integer maxIn;

	public int getCode() {
		return code;
	}

	public int getSubCode() {
		return subCode;
	}

	public String getMessage() {
		return message;
	}

	public Integer getPrice() {
		return price;
	}

	public Integer getInOut() {
		return inOut;
	}

	public Integer getMaxIn() {
		return maxIn;
	}

	private GrowthValueEnum(int code, int subCode, String message, Integer price, Integer inOut, Integer maxIn) {
		this.code = code;
		this.subCode = subCode;
		this.message = message;
		this.price = price;
		this.inOut = inOut;
		this.maxIn = maxIn;
	}
}
