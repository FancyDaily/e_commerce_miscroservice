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
public class EnrollUserInfoView {
	
	private static final long serialVersionUID = 1L;
	
	private Long userIdToString; //string 型的userid
	private String userNameForTeam; //名字
	private String userName; //用户姓名
	private String userUrl; //用户头像
	private Integer sex; //性别
	private String age; //年龄
	private String occupation; //职业
	private Boolean isGroup; //是否是组织成员
	private String skill; //技能
	private Integer serve_num; //服务次数
	private int total_eva; //评价总分
	private String creatTime; //申请时间
	private Integer status; //状态
}
