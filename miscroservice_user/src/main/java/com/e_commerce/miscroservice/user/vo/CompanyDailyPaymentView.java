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
 * 创建时间:2019年1月14日 下午4:27:22
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */

import java.util.List;

public class CompanyDailyPaymentView {
	List<PaymentView> helpPaymentViews;
	List<PaymentView> servPaymentViews;
	public List<PaymentView> getHelpPaymentViews() {
		return helpPaymentViews;
	}
	public void setHelpPaymentViews(List<PaymentView> helpPaymentViews) {
		this.helpPaymentViews = helpPaymentViews;
	}
	public List<PaymentView> getServPaymentViews() {
		return servPaymentViews;
	}
	public void setServPaymentViews(List<PaymentView> servPaymentViews) {
		this.servPaymentViews = servPaymentViews;
	}
}
