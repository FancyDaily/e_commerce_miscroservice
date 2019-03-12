package com.e_commerce.miscroservice.commons.enums.application;

/**
 * 功能描述:
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月9日 下午3:41:46
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public enum DictionaryEnum {

	// 勋章
	METAL(6, 0, -1l),

	// 公益历程
	WELFARE(3, 0, -1l),

	// 邀请人记录
	INVITER(7, 0, -1l),

	// 权益
	INTEREST(1, -1, -1l),
	// 权益 -> 获取互助时
	INTEREST_EARN(1, 0, 1001l),
	// 权益 -> 生日加速权益
	INTEREST_BIRTHDAY(1, 1, -1l),
	// 当type为5时即为任务，sub_type用于细分的类型,-1为示例.
	TASK(5, -1, -1l),

	// 任务 -> 注册类型
	TASK_RIGESTER(5, 0, 30l),
	// 任务 -> 实名认证
	TASK_AUTH(5, 1, 30l),
	// 任务 -> 完善个人主页
	TASK_PAGE(5, 2, 15l),
	// 任务 -> 签到类型
	TASK_SIGNUP(5, 3, 3l),
	// 任务 -> 邀请好友
	TASK_INVITE(5, 4, 30l),
	// 任务 -> 完成首次互助
	TASK_HELP(5, 5, 30l),
	
	// 分享 => 请指派具体的subType（请勿使用"-1"!）
	SHARE(8,-1,-1l),
	// 分享 -> 邀请
	SHARE_INVITE(8, 0, -1l),
	// 分享 -> 个人主页
	SHARE_PERSON(8, 1, -1l), 
	// 分享 -> 求助
	SHARE_HELP(8, 2, -1l),
	// 分享 -> 服务
	SHARE_SERVICE(8, 3, -1l),
	// 分享 -> 加入组织
	SHARE_COMPANY(8, 4, -1l),
	
	//报名记录
	SERVE_SIGN(2,0,-1l), 
	//红包记录
	BONUS_PACKAGE(9,0,-1l);

	private Integer type;
	private Integer subType;
	private Long reward; // 价值或者target_id

	public Integer getType() {
		return type;
	}

	public Integer getSubType() {
		return subType;
	}

	public Long getReward() {
		return reward;
	}

	private DictionaryEnum(Integer type, Integer subType, Long reward) {
		this.type = type;
		this.subType = subType;
		this.reward = reward;
	}
}
