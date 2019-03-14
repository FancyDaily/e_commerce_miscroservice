package com.e_commerce.miscroservice.commons.enums.application;

/**
 * 功能描述: 等级枚举类
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月12日 下午9:33:06
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public enum LevelEnum {	//TODO 待给出细则

	LEVEL_ONE(1,"互助公民", 0l, 15l), LEVEL_TWO(2,"互助顾问", 15l, 100l), LEVEL_THREE(3,"互助议员", 100l, 300l), LEVEL_FOUR(4,"互助市长", 300l, 800l),
	LEVEL_FIVE(5,"互助部长", 800l, 1500l), LEVEL_SIX(6,"互助公务员", 1500l, 2500l), LEVEL_SEVEN(7,"互助英雄", 2500l, -1l);

	private Integer level;
	private String name;
	private Long min;
	private Long max;
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getMin() {
		return min;
	}
	public void setMin(Long min) {
		this.min = min;
	}
	public Long getMax() {
		return max;
	}
	public void setMax(Long max) {
		this.max = max;
	}
	private LevelEnum(Integer level, String name, Long min, Long max) {
		this.level = level;
		this.name = name;
		this.min = min;
		this.max = max;
	}
	
}
