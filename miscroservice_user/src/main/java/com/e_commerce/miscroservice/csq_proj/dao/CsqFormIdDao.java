package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqFormId;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-02 14:10
 */
public interface CsqFormIdDao {

	TCsqFormId selectByUserIdAndFormId(Long userId, String formId);

	TCsqFormId selectAvailableFormIdByUserId(Long userId);

	int update(TCsqFormId formid);

	List<TCsqFormId> selectAvailableFormIdInUserIds(List<Long> messageUserId);

	int update(ArrayList<TCsqFormId> toUpdater);
}
