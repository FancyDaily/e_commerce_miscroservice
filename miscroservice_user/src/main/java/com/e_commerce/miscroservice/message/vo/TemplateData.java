package com.e_commerce.miscroservice.message.vo;

import java.io.Serializable;

/**
 * 功能描述:设置推送的文字和颜色
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月21日 下午2:40:13
 */
public class TemplateData implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//keyword1：订单类型，keyword2：下单金额，keyword3：配送地址，keyword4：取件地址，keyword5备注
    private String value;//,,依次排下去
    private String color;//字段颜色（微信官方已废弃，设置没有效果）
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
    
}
