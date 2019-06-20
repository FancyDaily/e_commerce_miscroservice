package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqPublish;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 11:16
 */
public interface CsqPublishDao {
	TCsqPublish selectByMainKey(int mainKey);

	int update(TCsqPublish... tCsqPublish);

	int insert(TCsqPublish... tCsqPublishes);
}
