package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.po.TCsqMoneyApplyRecord;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-11 17:32
 */
public interface CsqMoneyApplyRecordDao {

	int insert(TCsqMoneyApplyRecord... build);

	MybatisPlusBuild baseBuild();

	List<TCsqMoneyApplyRecord> selectWithBuildPage(MybatisPlusBuild baseBuild, Page page);

	int update(TCsqMoneyApplyRecord build);
}
