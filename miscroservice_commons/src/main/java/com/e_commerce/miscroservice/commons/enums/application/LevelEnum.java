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

	LEVEL_ONE(1,"爱心人士", 0l, 3l), LEVEL_TWO(2,"爱心义工", 3l, 5l), LEVEL_THREE(3,"爱心先锋", 5l, 7l), LEVEL_FOUR(4,"公益志愿者", 7l, 10l),
	LEVEL_FIVE(5,"公益达人", 10l, 30l), LEVEL_SIX(6,"公益先锋", 30l, 60l), LEVEL_SEVEN(7,"公益使者", 60l, 120l) ,LEVEL_EIGHT(8,"厚德流光",120l,-1l);

	private Integer level;	//等级
	private String name;	//等级名
	private Long min;	//下限
	private Long max;	//上限
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
