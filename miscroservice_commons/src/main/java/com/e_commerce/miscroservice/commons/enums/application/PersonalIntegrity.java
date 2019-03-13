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
public enum PersonalIntegrity {
	//个人主页背景
	USER_PICTURE_PATH(30,30),
	//昵称
	NAME(10,10),
	//教育
	EDUCATION(20,20),
	//公司
	COMPANY(20,20),
	//职位
	POST(10,10),
	//描述
	REMARKS(10,10);

	private int code;	//单次
	private int num;	//总
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
