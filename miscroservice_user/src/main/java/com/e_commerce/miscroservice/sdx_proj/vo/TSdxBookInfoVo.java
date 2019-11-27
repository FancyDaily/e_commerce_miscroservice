package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 书籍信息
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookInfoVo {

	/**
	 * 剩余可售卖数量
	 */
	private Integer availableNum;

    /**
    *书籍信息的Id,修改或者查询的需要
    *
    */

    private Long id;

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
    public TSdxBookInfoPo copyTSdxBookInfoPo() {
        return null;
    }
}
