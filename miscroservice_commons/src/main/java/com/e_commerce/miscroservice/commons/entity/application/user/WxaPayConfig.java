package com.e_commerce.miscroservice.commons.entity.application.user;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付配置
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月23日 下午 06:10:03
 */
@Data
@Table("yjj_wxa_pay_config")
public class WxaPayConfig implements Serializable{
 
	/**
	 * 主键
	 */
	@Id
	private Long id;
 
	/**
	 * 小程序id
	 */  
	private String appId;
 
	/**
	 * 微信支付商户id
	 */  
	private String mchId;
 
	/**
	 * 密文
	 */  
	private String paternerKey;
 
	/**
	 * 证书地址
	 */  
	private String certPath;
 
	/**
	 * 订单前缀
	 */  
	private String orderPrefix;

 }