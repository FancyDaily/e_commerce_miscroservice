package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShoppingTrolleysPo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 购物车
*/
@Data(matchSuffix = true)
@NoArgsConstructor
@Builder
public class SdxShoppingTrolleysServiceGroupVo {

    /**
    *用户编号
    *
    */
    private Long userId;

	/**
	 * 公益项目编号
	 */
	private Long serviceId;

	/**
	 * 公益项目名称
	 */
	private String serviceName;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 封面图
	 */
	private String coverPic;

	/**
	 * 购物车书本条目集合
	 */
	private List<SdxShoppingTrolleysVo> trolleysBookInfos;

	/**
	 * 时间戳
	 */
	private Long timeStamp;

    public TSdxShoppingTrolleysPo copyTSdxShoppingTrolleysPo() {
        return null;
    }
}
