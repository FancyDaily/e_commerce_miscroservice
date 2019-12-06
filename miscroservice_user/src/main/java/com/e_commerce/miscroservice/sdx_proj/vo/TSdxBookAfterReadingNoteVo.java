package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNotePo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 书籍读后感
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookAfterReadingNoteVo {

	/**
	 * 创作者昵称
	 */
	private String userName;

	/**
	 * 创作者头像
	 */
	private String headPic;

    /**
    *书籍读后感的Id,修改或者查询的需要
    *
    */

    private Long id;

	/**
	 * 是否无需购买
	 */
	@Transient
	private boolean noNeedBuy;

	/**
	 * 是否点赞/踩
	 */
	@Transient
	private Integer isThumb;

	/**
	 * 点赞/踩类型
	 */
	@Transient
	private Integer thumbType;

	/**
	 * 创作日期
	 */
	@Transient
	private String date;

	@Column(commit = "购买所需积分", length = 11)
	private Integer forSaleScore;

	@Column(commit = "书本编号")
	private Long bookId;

	@Column(commit = "书籍信息编号")
	private Long bookInfoId;

	@Column(commit = "创作者编号")
	private Long userId;

	@Column(commit = "点赞数量", length = 11, defaultVal = "0")
	private Integer thumbUpNum;

	@Column(commit = "点踩数量", length = 11, defaultVal = "0")
	private Integer thumbDownNum;

	@Column(commit = "内容")
	private String content;

    public TSdxBookAfterReadingNotePo copyTSdxBookAfterReadingNotePo() {
        return null;
    }
}
