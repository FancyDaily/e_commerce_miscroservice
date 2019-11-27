package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxWishListPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 心愿单
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxWishListVo {

    /**
    *心愿单的Id,修改或者查询的需要
    *
    */

    private Long id;

    private Long userId;

    /**
    *书本信息编号
    *
    */

    private Long bookInfoId;

	/**
	 * 捐助时可折抵积分
	 */
	@Column(commit = "可折抵积分", length = 11)
	private Integer expectedScore;

	/**
	 * 想要人数
	 */
	@Transient
	private Integer wishNum;

	/**
	 * 常年关联的公益项目编号
	 */
	@Column(commit = "关联项目编号")
	private Long serviceId;

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

	/**
	 * 缺货标记
	 */
	private boolean outOfStock;

	@Column(commit = "是否等待来货提醒")
	private Integer isWaitingForNotice;

    public TSdxWishListPo copyTSdxWishListPo() {
        return null;
    }

	public void setBookInfo(TSdxBookInfoPo po) {
    	this.bookInfoId = po.getId();
    	this.soldNum = po.getSoldNum();
    	this.maximumReserve = po.getMaximumReserve();
    	this.catalog = po.getCatalog();
		this.introduction = po.getIntroduction();
		this.scoreDouban = po.getScoreDouban();
		this.bindingStyle = po.getBindingStyle();
		this.publisher = po.getPublisher();
		this.maximumDiscount = po.getMaximumDiscount();
		this.price = po.getPrice();
		this.tagId = po.getTagId();
		this.tag = po.getTag();
		this.coverPic = po.getCoverPic();
		this.author = po.getAuthor();
		this.name = po.getName();
		this.serviceId = po.getServiceId();
		this.wishNum = po.getWishNum();
		this.expectedScore = po.getExpectedScore();
	}
}
