package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookTransRecordVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍漂流记录
 * @Author: FangyiXu
 * @Date: 2019-10-23 15:51
 */
@Data
@Builder
@Table(commit = "书籍漂流记录")
@NoArgsConstructor
public class TSdxBookTransRecordPo extends BaseEntity {

	/**
	 * 读后感
	 */
	@Transient
	private TSdxBookAfterReadingNotePo notes;

	@Column(commit = "书籍编号")
	private Long bookId;

	@Column(commit = "书籍信息编号")
	private Long bookInfoId;

	@Column(commit = "读后感编号")
	private Long bookAfterReadingNoteId;

	@Column(commit = "书籍主人编号")
	private Long userId;

	@Column(commit = "类型1.xxx成为主人2.xxx的读后感3.存放在书籍驿站多少天 etc.")
	private Integer type;

	@Column(commit = "描述")
	private String description;

    public TSdxBookTransRecordVo  copyTSdxBookTransRecordVo() {
        return null;
     }

}
