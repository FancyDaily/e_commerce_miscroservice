package com.e_commerce.miscroservice.commons.enums.application;
/**
 * 功能描述: 系统信息枚举类
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月25日 下午9:48:18
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public enum SysMsgEnum {
	RIGESTER("欢迎加入晓时互助","恭喜您成功注册，感恩与您同行，晓时互助在此赠送您30分钟互助时，您可以在您的时间账户中查看。"),
	INVITER("邀请好友奖励","您邀请的名称为%s的好友已注册成功，恭喜您获得30分钟的互助时奖励。"),	//TODO 含参数
	FIRST_HELP("完成首次互助奖励","您已经在「晓时互助」完成首次互助，恭喜您获得30分钟的互助时奖励。期待您"),
	AUTH("实名认证奖励","您也是有身份的人啦，恭喜哦！而且也收获了30分钟的互助时奖励。现在，您可以在「晓时互助」看看有没有需要帮助和帮助到你的小伙伴吧！"),
	PAGE_COMPLETE("完善个人主页奖励","您的主页美化成功，恭喜您获得15分钟互助时的最美奖励。"), 
	BEEN_TRANSFER("您收到一笔转账","您收到一笔来自于%s的价值为%d分钟的转账，请查收。"),  //TODO 文本待完善
	BONUS_PACKAGE_DONE("您的红包已被领取","您发布的文本为%s的红包已被%s领取。");	//TODO 文本待完善	
	
	String title;
	String content;
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	private SysMsgEnum(String title, String content) {
		this.title = title;
		this.content = content;
	}
	
}
