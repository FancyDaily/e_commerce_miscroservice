package com.e_commerce.miscroservice.xiaoshi_proj.order.vo;

import lombok.Data;

/**
 * 功能描述:mainKey-theValue 权益view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月3日 下午6:18:50
 */
@Data
public class OrgEnrollUserView {
	
	private static final long serialVersionUID = 1L;
	
	private Long orderId; //string 型的serviceId
	private String title; //标题
	private Integer status; //状态
	private String startTime; //开始时间
	private String endTime; //结束时间
	private Boolean isRepeat; //是否为重复周期
	private Integer enrollSum; //报名人数
	private Integer chooseUserSum; //已选人数
	private Integer canChooseSum; //可选人数
	
}
