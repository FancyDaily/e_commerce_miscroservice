package com.e_commerce.miscroservice.commons.entity.application.activity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author charlie
 * @version V1.0  
 * @date 2018年11月09日 上午 09:57:39
 */
@Data
@Table ("yjj_coupon_new")
public class Coupon  {
 
	/**
	 * 主键
	 */
	@Id
	private Long id;
 
	/**
	 * 小程序用户idS
	 */  
	private Long memberId;
 
	/**
	 * 门店用户id
	 */  
	private Long storeId;
 
	/**
	 * 用户类型: 1门店宝 2小程序
	 */  
	private Integer userType;
 
	/**
	 * 模板id
	 */  
	private Long templateId;
 
	/**
	 * 优惠券面额
	 */  
	private BigDecimal price;
 
	/**
	 * 优惠折扣
	 */  
	private BigDecimal discount;
 
	/**
	 * 优惠券名称
	 */  
	private String templateName;
 
	/**
	 * 是否可用:-2删除 -1:作废  0:未用 1:已使用
	 */  
	private Integer status;
 
	/**
	 * 订单优惠金额
	 */  
	private BigDecimal orderFavorableMone;
 
	/**
	 * 订单金额
	 */  
	private BigDecimal orderMoney;
 
	/**
	 * 订单号
	 */  
	private String orderNo;
 
	/**
	 * 使用时间区间
	 */  
	private Date useBeginTime;
 
	/**
	 * 使用时间区间
	 */  
	private Date useEndTime;
 
	/**
	 * 创建时间
	 */  
	private Date createTime;
 
	/**
	 * 发放方:供应商id 或者storeId
	 */  
	private Long publishUserId;
 
	/**
	 * 发布方姓名
	 */  
	private String publishUser;
 
	/**
	 * 修改时间
	 */  
	private Date updateTime;
 
	/**
	 * 订单满足多少可用
	 */  
	private BigDecimal limitMoney;
 
	/**
	 * 临时字段导数据用的
	 */  
	private Long oldId;
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }