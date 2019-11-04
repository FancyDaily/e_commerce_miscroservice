package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍读后感
 * @Author: FangyiXu
 * @Date: 2019-10-23 15:51
 */
@Table(commit = "书籍读后感")
@Data
@Builder
@NoArgsConstructor
public class TSdxBookAfterReadingNotePo extends BaseEntity {

	/**
	 * 是否无需购买
	 */
	@Transient
	private boolean noNeedBuy;

	/**
	 * 创作日期
	 */
	@Transient
	private String date;

	@Column(commit = "书本编号")
	private Long bookId;

	@Column(commit = "书籍信息编号")
	private Long bookInfoId;

	@Column(commit = "创作者编号")
	private Long userId;

	@Column(commit = "点赞数量", length = 11)
	private Integer thumbUpNum;

	@Column(commit = "点踩数量", length = 11)
	private Integer thumbDownNum;

    public TSdxBookAfterReadingNoteVo  copyTSdxBookAfterReadingNoteVo() {
        return null;
     }

}
