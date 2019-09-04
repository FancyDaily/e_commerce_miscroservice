package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TOldPayment;
import com.sun.mail.util.LineOutputStream;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-09 16:25
 */
public interface CsqOldPaymentDao {
	List<TOldPayment> selectByOptionUserInPFId(String id, List<String> serviceIds);

	List<TOldPayment> selectInPfIds(List<String> oldFundIds);

	List<TOldPayment> selectInPfIdsDesc(List<String> oldFundIds);

	List<TOldPayment> selectInPfIdsAsc(List<String> oldFundIds);

	List<TOldPayment> selectNotInPFId(Long... pfIds);
}
