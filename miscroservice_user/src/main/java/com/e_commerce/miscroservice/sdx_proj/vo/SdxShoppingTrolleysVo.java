package com.e_commerce.miscroservice.sdx_proj.vo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShoppingTrolleysPo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.cms.TimeStampAndCRL;

import java.sql.Timestamp;

/**
* 购物车
*/
@Data(matchSuffix = true)
@NoArgsConstructor
public class SdxShoppingTrolleysVo {

    /**
    *购物车的Id,修改或者查询的需要
    *
    */

    private Long id;

    /**
    *用户编号
    *
    */

    private Long userId;

    /**
    *书籍信息编号
    *
    */

    private Long bookInfoId;

	/**
	 * 公益项目编号
	 */
	private Long serviceId;

	/**
	 * 书本信息名称
	 */
	private String bookInfoName;

	/**
	 * 书本信息封面
	 */
	private String bookInfoCoverPic;

	/**
	 * 书本信息价格
	 */
	private Double price;

	/**
	 * 更新时间
	 */
	private Timestamp updateTime;

    public TSdxShoppingTrolleysPo copyTSdxShoppingTrolleysPo() {
        return null;
    }

	public void setBookInfo(TSdxBookInfoPo po) {
    	this.bookInfoCoverPic = po.getCoverPic();
    	this.bookInfoName = po.getName();
    	this.price = po.getPrice();
	}
}
