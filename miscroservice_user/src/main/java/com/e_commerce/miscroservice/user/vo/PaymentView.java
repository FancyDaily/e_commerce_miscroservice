package com.e_commerce.miscroservice.user.vo;

import lombok.Getter;
import lombok.Setter;

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
@Setter
@Getter
public class PaymentView implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String idString;

	private String serviceName;	//服务标题

	private Integer serviceType;	//求助还是服务

	private String serviceTypeName;	//服务类型名（大类）
	
	private Integer confirmNum;	//参与人数

	private Long collectTime;	//单价

	private Long totalTime;	//总计


}
