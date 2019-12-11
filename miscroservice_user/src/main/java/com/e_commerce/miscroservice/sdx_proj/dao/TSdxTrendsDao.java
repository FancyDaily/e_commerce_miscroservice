package com.e_commerce.miscroservice.sdx_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTrendsPo;

import java.util.List;

public interface TSdxTrendsDao {
	int saveTSdxTrendsIfNotExist(TSdxTrendsPo tSdxTrendsPo);

	int modTSdxTrends(TSdxTrendsPo tSdxTrendsPo);

	int delTSdxTrendsByIds(Long... ids);

	TSdxTrendsPo findTSdxTrendsById(Long id);

	List<TSdxTrendsPo> findTSdxTrendsByAll(TSdxTrendsPo tSdxTrendsPo, Integer page, Integer size);

}
