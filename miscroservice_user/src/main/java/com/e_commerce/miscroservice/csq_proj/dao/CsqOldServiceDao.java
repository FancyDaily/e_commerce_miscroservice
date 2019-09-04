package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TOldService;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-09 16:26
 */
public interface CsqOldServiceDao {
	List<TOldService> selectByNames(String... names);

	List<TOldService> selectByStatusByCheckStatus(int status, int checkStatus);

	List<TOldService> selectByStatusByCheckStatusNeqTotalAmount(int i, int i1, String s);

	List<TOldService> selectByStatusByCheckStatusAndAdderEqCHargePersonId(int i, int i1);

	List<TOldService> selectByStatusByCheckStatusAndAdderEqCHargePersonIdAndFinType(int i, int i1, String type);

	List<TOldService> selectByStatusByCheckStatusByDType(int i, int i1, String pf);

	List<TOldService> selectByStatusByCheckStatusByDTypeAndDonationAmountNeq(int i, int i1, String fund, String s);

	List<TOldService> selectByStatusByCheckStatusByDTypeAndDonationAmountNeqAndFinTypeNeq(int i, int i1, String fund, String s, String 个人冠名基金);

	List<TOldService> selectInNames(List<String> serviceNames);

	TOldService selectByPrimaryKey(String pfid);
}
