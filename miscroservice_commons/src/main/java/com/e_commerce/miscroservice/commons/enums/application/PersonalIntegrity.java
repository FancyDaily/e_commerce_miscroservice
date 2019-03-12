package com.e_commerce.miscroservice.commons.enums.application;
/**
 * 功能描述: 用户完整度枚举类
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月21日 下午7:58:37
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public enum PersonalIntegrity { //TODO
	USER_PICTURE_PATH(0,5),
	SKILL(1,35),
	COMPANY(2,10),
	POST(3,5),
	EDUCATION(4,5),
	JOIN_TIME(5,10),
	
	EDU_TOTAL(10,30),
	WORK_TOTAL(10,30);
	
	private int code;
	private int num;
	public int getCode() {
		return code;
	}
	public int getNum() {
		return num;
	}
	private PersonalIntegrity(int code, int num) {
		this.code = code;
		this.num = num;
	}
	
}
