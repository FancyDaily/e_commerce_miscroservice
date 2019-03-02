package com.e_commerce.miscroservice.commons.entity.user;

import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import com.e_commerce.miscroservice.commons.entity.application.user.WxaPayConfig;
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
//@Table("支付配置")
public class WxaPayConfigQuery extends WxaPayConfig{
}