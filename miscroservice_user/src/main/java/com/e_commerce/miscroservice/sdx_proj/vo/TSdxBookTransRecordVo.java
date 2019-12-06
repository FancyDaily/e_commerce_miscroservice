package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTransRecordPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 书籍漂流记录
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookTransRecordVo {

    /**
    *书籍漂流记录的Id,修改或者查询的需要
    *
    */

    private Long id;

    /**
    *类型1.xxx成为主人2.xxx的读后感3.存放在书籍驿站多少天etc.
    *
    */

    private Integer type;

    /**
    *书籍编号
    *
    */

    private Long bookId;

    /**
    *书籍主人编号
    *
    */

    private Long userId;

    /**
    *书籍信息编号
    *
    */

    private Long bookInfoId;

    /**
    *描述
    *
    */

    private String description;

	@Column(commit = "读后感编号")
	private Long bookAfterReadingNoteId;

	@Column(commit = "是否点赞/踩", defaultVal = "0")
	private Integer isThumb;

	@Column(commit = "点赞/踩类型")
	private Integer thumbType;

	@Column(commit = "是否购买", defaultVal = "0")
	private Integer isPurchase;

	@Column(commit = "点赞数量", length = 11, defaultVal = "0")
	private Integer thumbUpNum;

	@Column(commit = "点踩数量", length = 11, defaultVal = "0")
	private Integer thumbDownNum;

	@Column(commit = "内容")
	private String content;

	@Transient
	private String date;

    public TSdxBookTransRecordPo copyTSdxBookTransRecordPo() {
        return null;
    }
}
