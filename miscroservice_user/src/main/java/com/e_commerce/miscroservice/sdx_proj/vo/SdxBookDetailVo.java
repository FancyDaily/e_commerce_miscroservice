package com.e_commerce.miscroservice.sdx_proj.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 书籍详情vo
 * @Author: FangyiXu
 * @Date: 2019-10-28 09:31
 */
@Data
@Builder
public class SdxBookDetailVo {

	/**
	 * 余量
	 */
	private Integer surplusNum;

	/**
	 * 书名
	 */
	private String name;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 图片
	 */
	private String introPic;

	/**
	 * 类型名
	 */
	private String categoryName;

	/**
	 * 类型编号
	 */
	private Integer categoryId;

	/**
	 * 价格
	 */
	private Double price;

	/**
	 * 最高可抵扣价格
	 */
	private Double maximumDiscount;

	/**
	 * 出版社
	 */
	private String press;

	/**
	 * 装帧
	 */
	private String bindingStyle;

	/**
	 * 豆瓣评分
	 */
	private Double scoreDouban;

	/**
	 * 简介
	 */
	private String introduction;

	/**
	 * 目录
	 */
	private String catalog;

	/**
	 * 最大预购接收数
	 */
	private Integer maximumReserve;

	/**
	 * 销量
	 */
	private Integer soldNum;

	/**
	 * 推荐信息(姓名、职位、头像、评语)Json
	 */
	private String introduceInfo;
}
