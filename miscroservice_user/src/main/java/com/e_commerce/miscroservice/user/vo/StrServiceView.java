package com.e_commerce.miscroservice.user.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 功能描述: String化的ServiceView
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月19日 下午9:04:55
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
@Data
public class StrServiceView implements Serializable {

	private String idString;	//字符串类型的id

	private String serviceIdString;	//字符串类型的服务id

	private Long id;

	private Long serviceId;

	private Long mainId;

	private String nameAudioUrl;

	private String serviceName;

	private Integer servicePersonnel;

	private Integer servicePlace;

	private String labels;

	private Integer type;

	private Integer status;

	private Integer source;

	private Long serviceTypeId;

	private String addressName;

	private Double longitude;

	private Double latitude;

	private Integer totalEvaluate;

	private Integer enrollNum;

	private Integer confirmNum;

	private Long startTime;

	private Long endTime;

	private Integer serviceStatus;

	private Integer openAuth;

	private Integer timeType;

	private Long collectTime;

	private Integer collectType;

	private Long createUser;

	private String createUserName;

	private Long createTime;

	private Long updateUser;

	private String updateUserName;

	private Long updateTime;

	private Long companyId;

	private String isValid;


}
