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
public class NoticesFirstView {
	private Long sysTime;//最新一条系统消息时间
	private Long sysUnReadSum;//系统消息未读数目

}
