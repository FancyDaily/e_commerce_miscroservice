package com.e_commerce.miscroservice.xiaoshi_proj.message.vo;

import lombok.Data;

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
public class MessageShowLIstView {
	private Long parent;//分组id
	private String userName;//用户姓名
	private String userUrl;//用户头像链接
	private String content;//内容
	private Long time;//时间
	private Long createTime;
	private Long toUserId;//对方用户id
	private Long unReadSum;//未读消息数量

}
