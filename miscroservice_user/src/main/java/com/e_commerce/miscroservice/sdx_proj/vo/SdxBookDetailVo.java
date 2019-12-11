package com.e_commerce.miscroservice.sdx_proj.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 书籍详情vo
 * @Author: FangyiXu
 * @Date: 2019-10-28 09:31
 */
@Data
@Builder
public class SdxBookDetailVo {

	private Long bookInfoId;

	/**
	 * 是否在购物车
	 */
	private boolean inTrolley;

	/**
	 * 售出比例
	 */
	private Integer purchaseRate;

	/**
	 * 读后感记录
	 */
	private List<TSdxBookAfterReadingNoteVo> afterReadingNoteVos;

	/**
	 * 捐赠记录(用户信息)
	 */
	private List<SdxBookOrderUserInfoVo> donateUserRecords;

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
	private String coverPic;

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
	 * 抵扣后价格
	 */
	private Double surplusPrice;

	/**
	 * 出版社
	 */
	private String publisher;

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

	/**
	 * 可售卖数量
	 */
	private Integer availableNum;
}
