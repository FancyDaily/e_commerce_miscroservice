package com.e_commerce.miscroservice.csq_proj.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 11:29
 */
public interface CsqPublishService {

	String getPublishName(int toCode, Integer trendPubKey);

	List<String> getPublishName(int mainKey, String trendPubKey);

	Map getPublishName(int mainKey);

	void setPublishName(int mainKey, Map<Long, Long> dailyDonateMap);

	void setPublishName(Integer mainKey, Integer[] keys, String[] names, String keyDesc, boolean isObjectArray);

	Map get(Integer mainKey);

	Object getAsList(Integer mainKey);
}
