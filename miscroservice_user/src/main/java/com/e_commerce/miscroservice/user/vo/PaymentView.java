package com.e_commerce.miscroservice.user.vo;

import java.io.Serializable;

/**
 * 功能描述: 组织当日服务、需求情况view
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2019年1月14日 下午4:49:40
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */

public class PaymentView implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String idString;

	private String serviceName;	//服务标题
	
	private String tag;	//服务类型（大类）
	
	private Integer collectType;	//区分互助时和公益时
	
	private Integer servicePersonnel;	//预计参与人数
	
	private Long totalTime;	//总计
	
	private Long collectTime;	//单价

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
	
	public Long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}

	public Long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Long collectTime) {
		this.collectTime = collectTime;
	}

}
