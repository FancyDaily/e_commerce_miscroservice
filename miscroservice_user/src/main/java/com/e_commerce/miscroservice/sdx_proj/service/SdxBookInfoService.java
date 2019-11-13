package com.e_commerce.miscroservice.sdx_proj.service;

import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.utils.DoubanBookInfo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoVo;

import java.util.List;

public interface SdxBookInfoService {
	long modTSdxBookInfo(TSdxBookInfoPo tSdxBookInfoPo);

	int delTSdxBookInfoByIds(Long... ids);

	TSdxBookInfoVo findTSdxBookInfoById(Long id);

	List<TSdxBookInfoVo> findTSdxBookInfoByAll(TSdxBookInfoPo tSdxBookInfoPo, Integer page, Integer size, Integer sortType);

	DoubanBookInfo getBookInfo(String isbnCode);
}
