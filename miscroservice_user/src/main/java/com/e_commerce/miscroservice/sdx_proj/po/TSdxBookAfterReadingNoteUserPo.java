package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteUserVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;

/**
 * 书籍读后感用户相关
 * @Author: FangyiXu
 * @Date: 2019-10-23 15:51
 */
@Table(commit = "书籍读后感用户相关")
@Data
@Builder
public class TSdxBookAfterReadingNoteUserPo extends BaseEntity {

	@Column(commit = "读后感编号")
	private Long bookAfterReadingNoteId;

	@Column(commit = "书籍编号")
	private Long bookId;

	@Column(commit = "书籍信息编号")
	private Long bookInfoId;

	@Column(commit = "用户编号")
	private Long userId;

	@Column(commit = "是否点赞/踩", defaultVal = "0")
	private Integer isThumb;

	@Column(commit = "点赞/踩类型")
	private Integer thumbType;

	@Column(commit = "类型(1.点赞与购买2.留言)", defaultVal = "1")
	private Integer type;

	@Column(commit = "是否购买", defaultVal = "0")
	private Integer isPurchase;

    public TSdxBookAfterReadingNoteUserVo  copyTSdxBookAfterReadingNoteUserVo() {
        return null;
     }

}
