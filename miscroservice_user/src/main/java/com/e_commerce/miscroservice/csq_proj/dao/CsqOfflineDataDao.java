package com.e_commerce.miscroservice.csq_proj.dao;


import com.e_commerce.miscroservice.csq_proj.po.TCsqOffLineData;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-28 09:00
 */
public interface CsqOfflineDataDao {

	List<TCsqOffLineData> selectAll();
}
