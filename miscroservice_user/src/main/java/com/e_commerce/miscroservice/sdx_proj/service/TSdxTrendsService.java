package com.e_commerce.miscroservice.sdx_proj.service;

import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTrendsPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxTrendsVo;

import java.util.List;
import java.util.Map;

public interface TSdxTrendsService {
	long modTSdxTrends(TSdxTrendsPo tSdxTrendsPo);

	int delTSdxTrendsByIds(Long... ids);

	TSdxTrendsVo findTSdxTrendsById(Long id);

	List<TSdxTrendsVo> findTSdxTrendsByAll(TSdxTrendsPo tSdxTrendsPo, Integer page, Integer size);


	//查询跟我有相同书籍的人
	List<Map<String, Object>> findTSdxTrendsFriend(Long userId, String bookName);

	//查询最俊看的书籍;
	List<TSdxBookInfoVo> findBookInfos(Long userId);
}
