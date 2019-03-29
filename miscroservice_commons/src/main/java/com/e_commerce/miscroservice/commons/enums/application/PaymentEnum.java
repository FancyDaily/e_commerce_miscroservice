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
 * 创建时间:2018年11月5日 下午3:10:46
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public enum PaymentEnum {
	/**
	 * 支出
	 */
	PAYMENT_TYPE_ACEPT_SERV(1,"发布求助",-1l),
	PAYMENT_TYPE_BONUS_PACKAGE_OUT(9,"生成红包",-1l),
	PAYMENT_TYPE_REMOVE_ORDER_INDEMNITY_OUT(11,"取消订单赔付",-1l),	//订单违约赔款
	
	/**
	 * 收入
	 */
	PAYMENT_TYPE_PROVIDE_SERV(1,"提供服务",-1l),
	PAYMENT_TYPE_SIGNUP_BONUS(2,"奖励-签到",-1l),		//不固定收入的奖励为-1
	PAYMENT_TYPE_INVITE_BONUS(3,"奖励-成功邀请新用户",30l),
	PAYMENT_TYPE_FIRSTHELP_BONUS(4,"奖励-首次完成",30l),	//TODO 确认是首次完成需求 还是首次互助
	PAYMENT_TYPE_PRIVATE_PAGE_BONUS(5,"奖励-完善个人主页",15l),
	PAYMENT_TYPE_CERT_BONUS(6,"奖励-完成实名认证",30l),
	PAYMENT_TYPE_RIGESTER_BONUS(7,"奖励-新人注册",30l),
	PAYMENT_TYPE_TRANSFER(8,"转账",-1l),	
	PAYMENT_TYPE_BONUS_PACKAGE_IN(10,"收到红包",-1l),
	
	/**
	 * 特殊收入
	 */
	PAYMENT_TYPE_BONUS_PAC_SEND_BACK(900,"退款-红包超时",-1l),	//红包超时退款
	PAYMENT_TYPE_REMOVE_ORDER_INDEMNITY_BACK(901,"退款-赔付领取超时",-1l),	//赔付超时退款
	PAYMENT_TYPE_REMOVE_ORDER_INDEMNITY_IN(902,"被取消订单致歉礼",-1l),	//订单违约赔款
	PAYMENT_TYPE_OTHERS(999,"特殊奖励",-1l);	//活动或者其他非常驻收入类型
	
	private int code;
	private String message;
	private Long bonus;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getBonus() {
		return bonus;
	}
	public void setBonus(Long bonus) {
		this.bonus = bonus;
	}
	private PaymentEnum(int code, String message, Long bonus) {
		this.code = code;
		this.message = message;
		this.bonus = bonus;
	}
}
