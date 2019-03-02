package com.e_commerce.miscroservice.commons.entity.application.activity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券模板表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author charlie
 * @version V1.0  
 * @date 2018年11月09日 上午 09:59:15
 */
@Data
@Table ("yjj_coupon_template_new")
public class CouponTemplate {
 
	/**
	 * 主键
	 */
	@Id
	private Long id;
 
	/**
	 * 模板名称
	 */  
	private String name;
 
	/**
	 * 获取规则
	 */  
	private String getRule;
 
	/**
	 * 发放类型:0 立即发放,1自助领取,2新用户注册,3用户购买
	 */  
	private Integer sendType;
 
	/**
	 * 优惠券面额
	 */  
	private BigDecimal price;
 
	/**
	 * 优惠折扣
	 */  
	private BigDecimal discount;
 
	/**
	 * 失效时间: 0 开始时间和结束时间 1是领取后多久
	 */  
	private Integer deadlineType;
 
	/**
	 * 失效时间开始
	 */  
	private Date deadlineEnd;
 
	/**
	 * 失效开始时间
	 */  
	private Date deadlineBegin;
 
	/**
	 * 领取后多久失效
	 */  
	private Integer deadlineCount;
 
	/**
	 * 优惠券模板系统: 1 门店宝app  2 小程序 
	 */  
	private Integer sysType;
 
	/**
	 * 每人限领多少
	 */  
	private Integer eachReceiveCount;
 
	/**
	 * 发行量
	 */  
	private Long issueCount;
 
	/**
	 * 平台注释
	 */  
	private String comment;
 
	/**
	 * 累计被领取了多少
	 */  
	private Long receiveCount;
 
	/**
	 * 发放方:供应商id 或者storeId
	 */  
	private Long publishUserId;
 
	/**
	 * 发布方姓名
	 */  
	private String publishUser;
 
	/**
	 * 状态:-1：删除，0：正常，1：停止发行，2已领完,3已失效
	 */  
	private Integer status;
 
	/**
	 * 订单满足多少可用
	 */  
	private BigDecimal limitMoney;
 
	/**
	 * 使用范围:1 全场可用，2指定商品可用 3指定分类可用
	 */  
	private Integer useRange;
 
	/**
	 * 对应的ids
	 */  
	private String rangeIds;
 
	/**
	 * 创建时间
	 */  
	private Date createTime;
 
	/**
	 * 修改时间
	 */  
	private Date updateTime;
 
	/**
	 * 领取开始时间
	 */  
	private Date drawStartTime;
 
	/**
	 * 领取结束时间
	 */  
	private Date drawEndTime;
 
	private Long oldId;  
 
	/**
	 * 0红包,1优惠券,2打折券
	 */  
	private Integer couponType;
 
	/**
	 * 创建优惠券类型 0 平台  1 供应商  2APP
	 */  
	private Integer platformType;
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }