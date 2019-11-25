package com.e_commerce.miscroservice.sdx_proj.vo;

import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 全局捐书记录Vo
 * @Author: FangyiXu
 * @Date: 2019-10-24 17:12
 */
@Data
@Builder
public class SdxGlobalDonateRecordVo {

	/**
	 * 多少时间以前
	 */
	private String timeAgo;

	/**
	 * 用户编号
	 */
	private Long userId;

	/**
	 * 捐助者姓名
	 */
	private String donaterName;

	/**
	 * 书籍信息编号
	 */
	private String bookInfoIds;

	/**
	 * 书籍名
	 */
	private String bookInfoNames;

	/**
	 * 书籍信息集合
	 */
	private List<TSdxBookInfoPo> bookInfoPos;

	/**
	 * 描述
	 */
	private String description;

	public TSdxBookOrderPo copyTSdxBookOrder() {return null;}
}
