package com.e_commerce.miscroservice.sdx_proj.vo;

import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNoteUserPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍读后感用户相关
 */
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookAfterReadingNoteUserVo {

	/**
	 * 书籍读后感用户相关的Id,修改或者查询的需要
	 */
	private Long id;

	/**
	 * 类型(1.点赞与购买2.留言)
	 */
	private Integer type;

	/**
	 * 书籍编号
	 */
	private Long bookId;

	/**
	 * 用户编号
	 */
	private Long userId;

	/**
	 * 是否点赞
	 */

	private Integer isThumb;

	/**
	 * 书籍信息编号
	 */

	private Long bookInfoId;

	/**
	 * 是否购买
	 */

	private Integer isPurchase;

	/**
	 * 读后感编号
	 */

	private Long bookAfterReadingNoteId;

	public TSdxBookAfterReadingNoteUserPo copyTSdxBookAfterReadingNoteUserPo() {
		return null;
	}
}
