package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOfflineDataDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOffLineData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-28 09:00
 */
@Component
public class CsqOffLineDataDaoImpl implements CsqOfflineDataDao {

	@Override
	public List<TCsqOffLineData> selectAll() {
		return MybatisPlus.getInstance().findAll(new TCsqOffLineData(), new MybatisPlusBuild(TCsqOffLineData.class)
		);
	}
}
