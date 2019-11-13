package com.e_commerce.miscroservice.sdx_proj.utils;

import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-11-12 11:19
 */
@Data
@Builder
@NoArgsConstructor
public class DoubanBookInfo {

	/**
	 * 书名
	 */
	private String name;

	/**
	 * 封面
	 */
	private String coverPic;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 出版社
	 */
	private String publisher;

	/**
	 * 译者
	 */
	private String translator;

	/**
	 * 出版年
	 */
	private String publishTime;

	/**
	 * 页数
	 */
	private String totalPageNum;

	/**
	 * 价格
	 */
	private Double price;

	/**
	 * 装帧
	 */
	private String bindingLayout;

	/**
	 * 丛书
	 */
	private String series;

	/**
	 * ISBN码
	 */
	private String isbnCode;

	/**
	 * 评分
	 */
	private String rating;

	/**
	 * 标签、分类
	 */
	private String tag;

	/**
	 * 简介
	 */
	private String introduction;

	/**
	 * 目录
	 */
	private String catalog;

	public TSdxBookInfoPo copyTSdxBookInfoPo() {
		return TSdxBookInfoPo.builder()
			.name(name)
			.author(author)
			.coverPic(coverPic)
			.price(price)
			.publisher(publisher)
			.bindingStyle(bindingLayout)
			.scoreDouban(Double.valueOf(rating))
			.categoryName(tag)
			.introduction(introduction)
			.catalog(catalog)
			.build();
	}
}
