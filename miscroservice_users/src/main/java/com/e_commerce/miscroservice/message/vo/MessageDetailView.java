package com.e_commerce.miscroservice.message.vo;

import lombok.Data;

import java.util.List;

/**
 * 功能描述:key-value 权益view
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
public class MessageDetailView {
	private Long id;
	private String userName;
	private String userUrl;
	private Long userId;
	private Long time;
	private Long createTime;
	private int status;
	private String message;
	private int type;
	private String url;

}
