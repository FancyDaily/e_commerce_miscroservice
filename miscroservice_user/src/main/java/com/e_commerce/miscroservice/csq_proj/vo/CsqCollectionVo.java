package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUserCollection;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @ClassName CsqCollectionVo
 * @Auhor huangyangfeng
 * @Date 2019-06-16 13:15
 * @Version 1.0
 */
@Data(matchSuffix = true)
public class CsqCollectionVo implements Serializable {

	/**
	 * 封面图
	 */
	private String coverPic;

	/**
	 * 捐助进度
	 */
	private String donatePercent;

	/**
	 * 捐助人数
	 */
	private Integer donateCnt;

	/**
	 * 类型
	 */
	private Integer type;

	/**
	 * 项目内容
	 */
	private String purpose;
	/**
	 * 项目名称
	 */
	private String name;
	/**
	 * 描述图
	 */
	private String detailPic;
	/**
	 * 描述
	 */
	private String desc;
	/**
	 * 备案id
	 */
	private String recordNo;
	/**
	 * 项目id
	 */
	private Long serviceId;
	/**
	 * 项目对应基金类型编号
	 */
	private Long fundId;
	/**
	 * 累计筹到金额
	 */
	private Double sumTotalIn;
	/**
	 * 剩余金额
	 */
	private Double surplusAmount;

	public TCsqUserCollection copyTCsqUserCollection() {return null;}
}
