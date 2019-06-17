package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import lombok.Data;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 10:02
 */
@Data
public class CsqFundVo extends TCsqFund {

	Integer contributeInCnt;	//贡献人次

	String trendPubName;	//关注方向(名)

	List<TCsqUserPaymentRecord> goTOList;	//去向(捐助项目记录)
}
