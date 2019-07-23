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
 * 创建时间:2019年1月19日 下午3:34:52
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

public class CompanyPaymentView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//basic
	private Integer available_time;
	private Integer monthly_in;
	private Integer monthly_out;
	private Integer freeze_time;
	
	//detail
	private List<SinglePaymentView> singlePaymentViews;

	public Integer getAvailable_time() {
		return available_time;
	}

	public void setAvailable_time(Integer available_time) {
		this.available_time = available_time;
	}

	public Integer getMonthly_in() {
		return monthly_in;
	}

	public void setMonthly_in(Integer monthly_in) {
		this.monthly_in = monthly_in;
	}

	public Integer getMonthly_out() {
		return monthly_out;
	}

	public void setMonthly_out(Integer monthly_out) {
		this.monthly_out = monthly_out;
	}

	public Integer getFreeze_time() {
		return freeze_time;
	}

	public void setFreeze_time(Integer freeze_time) {
		this.freeze_time = freeze_time;
	}

	public List<SinglePaymentView> getSinglePaymentViews() {
		return singlePaymentViews;
	}

	public void setSinglePaymentViews(List<SinglePaymentView> singlePaymentViews) {
		this.singlePaymentViews = singlePaymentViews;
	}
}
