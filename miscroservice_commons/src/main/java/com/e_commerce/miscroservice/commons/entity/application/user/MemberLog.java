package com.e_commerce.miscroservice.commons.entity.application.user;

import java.math.BigDecimal;
import java.util.Date;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户会员日志表
 * </p>
 *
 * @author Charlie
 * @since 2018-09-29
 */
@Table("yjj_member_log")
@Data
public class MemberLog implements Serializable{


	@Id
	private Long id;
    /**
     * 会员表id
     */
	private Long memberId;
    /**
     * 操作来源(原因)
     */
	private String source;
    /**
     * 订单号
     */
	private String orderNo;
    /**
     * 操作前删除状态
     */
	private Integer beforeDelState;
    /**
     * 操作后删除状态
     */
	private Integer afterDelState;
    /**
     * 操作前金额
     */
	private Double beforeTotalMoney;
    /**
     * 操作后金额
     */
	private Double afterTotalMoney;
    /**
     * 操作前有效期
     */
	private Long beforeEndTime;
    /**
     * 操作后有效期
     */
	private Long afterEndTime;
    /**
     * 操作前有效期记录
     */
	private String beforeTimeQueue;
    /**
     * 操作后有效期记录
     */
	private String afterTimeQueue;
    /**
     * 创建时间
     */
	private Date createTime;
    /**
     * 操作人
     */
	private Long createUserId;
    /**
     * 更新前微信小程序时间
     */
	private Long beforeWxClosedTime;
    /**
     * 更新后微信小程序时间
     */
	private Long afterWxClosedTime;


	/**
	 * 获取一个默认的对象
	 *
	 * @param member member
	 * @return com.yujj.entity.product.MemberLog
	 * @author Charlie
	 * @date 2018/8/16 17:38
	 */
	public static MemberLog createLog(Member member) {
		MemberLog log = new MemberLog ();
		log.setMemberId (member.getId ());
		log.setBeforeDelState (member.getDelState ());
		log.setBeforeEndTime (member.getEndTime ());
		log.setBeforeTimeQueue (member.getValidTimeQueue ());
		BigDecimal moneyTotal = member.getMoneyTotal ();
		log.setBeforeTotalMoney (moneyTotal == null ? 0D : moneyTotal.doubleValue ());
		return log;
	}

	/**
	 * 组装更新后的数据
	 *
	 * @return com.yujj.entity.product.MemberLog
	 * @author Charlie
	 * @date 2018/8/16 17:44
	 */
//	public static MemberLog recAfterData(Member member, MemberLog log) {
//		log.setAfterDelState (member.getDelState ());
//		log.setAfterEndTime (member.getEndTime ());
//		log.setAfterTimeQueue (member.getValidTimeQueue ());
//		BigDecimal moneyTotal = member.getMoneyTotal ();
//		log.setAfterTotalMoney (moneyTotal == null ? 0D : moneyTotal.doubleValue ());
//		return log;
//	}
}
