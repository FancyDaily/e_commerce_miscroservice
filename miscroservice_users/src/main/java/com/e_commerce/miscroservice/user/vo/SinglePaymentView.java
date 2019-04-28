package com.e_commerce.miscroservice.user.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述:	组织流水轨迹 (相关接口：api/v1/user/queryPayments)
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2019年1月19日 下午4:02:40
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class SinglePaymentView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String idString;	//字符型主键
	
	private String servIdString;	//订单详情id
	
	private String servReceiptStatus;	//订单状态

	private String serviceName;	//服务标题
	
	private String tag;	//服务类型（大类）
	
	private Integer collectType;	//区分互助时和公益时
	
	private Integer servicePersonnel;	//预计参与人数
	
	private Integer totalTime;	//总计
	
	private Integer collectTime;	//单价
	
	private String time;	//日期
	
	private List<String> joinMembers;	//参加人员
	
	private String type;	//区分服务还是需求
	
	private String payOrGainString;	//总价String(格式化，并包含正负号)
	
	private Long createTime;	//排序用
	
	public String getIdString() {
		return idString;
	}

	public void setIdString(String idString) {
		this.idString = idString;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getCollectType() {
		return collectType;
	}

	public void setCollectType(Integer collectType) {
		this.collectType = collectType;
	}

	public Integer getServicePersonnel() {
		return servicePersonnel;
	}

	public void setServicePersonnel(Integer servicePersonnel) {
		this.servicePersonnel = servicePersonnel;
	}

	public Integer getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Integer totalTime) {
		this.totalTime = totalTime;
	}

	public Integer getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Integer collectTime) {
		this.collectTime = collectTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<String> getJoinMembers() {
		return joinMembers;
	}

	public void setJoinMembers(List<String> joinMembers) {
		this.joinMembers = joinMembers;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPayOrGainString() {
		return payOrGainString;
	}

	public void setPayOrGainString(String payOrGainString) {
		this.payOrGainString = payOrGainString;
	}

	public String getServIdString() {
		return servIdString;
	}

	public void setServIdString(String servIdString) {
		this.servIdString = servIdString;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getServReceiptStatus() {
		return servReceiptStatus;
	}

	public void setServReceiptStatus(String servReceiptStatus) {
		this.servReceiptStatus = servReceiptStatus;
	}
}
