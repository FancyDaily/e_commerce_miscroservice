package com.e_commerce.miscroservice.commons.entity.application.user;

import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 小程序会员表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月23日 上午 10:39:37
 */
@Data
public class ShopMemberVo extends BaseEntity implements Serializable{

	private Long id;

	/**
	 * 商家ID
	 */
	private Long storeId;

	/**
	 * 手机号码
	 */
	private String bindPhone;

	/**
	 * 微信UId
	 */
	private String bindWeixin;

	/**
	 * 用户昵称
	 */
	private String userNickname;

	/**
	 * 用户头像
	 */
	private String userIcon;

	/**
	 * 用户二维码
	 */
	private String minicodeUrl;

	/**
	 * 最后会员发送消息时间
	 */
	private Long lastMessageTime;

	/**
	 *  最后会员发送消息类型：消息类型：0文本text、1图片image、2链接link、3语音消息voice、4视频消息video、5小视频消息shortvideo、6地理位置消息location、9事件event
	 */
	private Integer lastMessageType;

	/**
	 *  最后会员发送消息显示内容
	 */
	private String lastMessageContent;

	/**
	 *  未读消息数量
	 */
	private String notReadMessageCount;

	/**
	 * 1,2,3
	 */
	private String tagIds;

	/**
	 * 状态 -1:删除 0:正常 1:取消关注
	 */
	private Integer status;

	/**
	 * 备注名称
	 */
	private String memoName;

	/**
	 * 创建时间
	 */
	private Long createTime;

	/**
	 * 更新时间
	 */
	private Long updateTime;

	/**
	 * 注册来源, 0手机,1邀请(分销商)
	 */
	private String source;

	private Integer sex;

	/**
	 * 店中店openId
	 */
	private String inShopOpenId;

	private Long inShopMemberId;

	/**
	 *
	 * 绑定的微信号码
	 */
	private String wxPhone;

 }