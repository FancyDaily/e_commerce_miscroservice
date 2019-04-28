package com.e_commerce.miscroservice.user.vo;

import com.e_commerce.miscroservice.commons.entity.application.TUserTimeRecord;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述:
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月7日 上午11:24:16
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */


public class UserTimeRecordView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long sumIn;
	private Long sumOut;
	private List<TUserTimeRecord> resultList;
	public Long getSumIn() {
		return sumIn;
	}
	public void setSumIn(Long sumIn) {
		this.sumIn = sumIn;
	}
	public Long getSumOut() {
		return sumOut;
	}
	public void setSumOut(Long sumOut) {
		this.sumOut = sumOut;
	}
	public List<TUserTimeRecord> getResultList() {
		return resultList;
	}
	public void setResultList(List<TUserTimeRecord> resultList) {
		this.resultList = resultList;
	}
	
}
