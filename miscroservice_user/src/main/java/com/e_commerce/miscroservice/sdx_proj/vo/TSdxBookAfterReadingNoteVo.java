package com.e_commerce.miscroservice.sdx_proj.vo;
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
    *书本编号
    *
    */

    private Long bookId;

    /**
    *创作者编号
    *
    */

    private Long userId;

    /**
    *书籍信息编号
    *
    */

    private Long bookInfoId;

    /**
    *点赞数量
    *
    */

    private Integer thumbUpNum;

    /**
    *点踩数量
    *
    */

    private Integer thumbDownNum;
    public TSdxBookAfterReadingNotePo copyTSdxBookAfterReadingNotePo() {
        return null;
    }
}
