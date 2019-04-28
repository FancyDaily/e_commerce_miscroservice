package com.e_commerce.miscroservice.user.vo;
/**
 * 功能描述:
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月24日 下午2:57:16
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */

import java.io.Serializable;
import java.util.List;


public class SignUpInfoView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer bonus7;
	boolean state;
	Long count;
	String[] cycleArray;	//完整周期的日期数组（包含position）
	List<DateTypeDictionaryView> signUpList;

	public Integer getBonus7() {
		return bonus7;
	}

	public void setBonus7(Integer bonus7) {
		this.bonus7 = bonus7;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<DateTypeDictionaryView> getSignUpList() {
		return signUpList;
	}

	public void setSignUpList(List<DateTypeDictionaryView> signUpList) {
		this.signUpList = signUpList;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String[] getCycleArray() {
		return cycleArray;
	}

	public void setCycleArray(String[] cycleArray) {
		this.cycleArray = cycleArray;
	}

}
