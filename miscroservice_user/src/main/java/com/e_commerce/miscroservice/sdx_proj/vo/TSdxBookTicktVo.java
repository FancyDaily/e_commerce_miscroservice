package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTicketPo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 预定书券
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class TSdxBookTicktVo {

    /**
    *预定书券的Id,修改或者查询的需要
    *
    */

    private Long id;

    /**
    *过期时间点
    *
    */

    private Long expire;

    /**
    *拥有者用户编号
    *
    */

    private Long userId;

	/**
	 * 是否已使用
	 */
    private Integer isUsed;

    public TSdxBookTicketPo copyTSdxBookTicktPo() {
        return null;
    }
}
