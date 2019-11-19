package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxBookDetailVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoVo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍信息
 * @Author: FangyiXu
 * @Date: 2019-10-23 15:51
 */
@Table(commit = "书籍信息")
@Data
@Builder
@NoArgsConstructor
public class TSdxBookInfoPo extends BaseEntity {

	/**
	 * 捐助时可折抵积分
	 */
	@Transient
	private Integer expectedScore;

	/**
	 * 书名
	 */
	@Column(commit = "书名")
	private String name;

	/**
	 * 作者
	 */
	@Column(commit = "作者")
	private String author;

	/**
	 * 图片
	 */
	@Column(commit = "图片")
	private String coverPic;

	/**
	 * 类型名
	 */
	@Column(commit = "类型名")
	private String tag;

	/**
	 * 类型编号
	 */
	@Column(commit = "类型编号")
	private String tagId;

	/**
	 * 价格
	 */
	@Column(commit = "价格", precision = 2)
	private Double price;

	/**
	 * 最高可抵扣价格
	 */
	@Column(commit = "最高可抵扣价格", precision = 2)
	private Double maximumDiscount;

	/**
	 * 出版社
	 */
	@Column(commit = "出版社")
	private String publisher;

	/**
	 * 装帧
	 */
	@Column(commit = "装帧风格")
	private String bindingStyle;

	/**
	 * 豆瓣评分
	 */
	@Column(commit = "豆瓣评分", precision = 1)
	private Double scoreDouban;

	/**
	 * 简介
	 */
	@Column(commit = "简介")
	private String introduction;

	/**
	 * 目录
	 */
	@Column(commit = "目录")
	private String catalog;

	@Column(commit = "最大预购接收数", length = 11)
	private Integer maximumReserve;

	@Column(commit = "销量", length = 11)
	private Integer soldNum;

    public TSdxBookInfoVo  copyTSdxBookInfoVo() {
        return null;
     }

    public SdxBookDetailVo copySdxBookDetailVo() {return null; }

}
