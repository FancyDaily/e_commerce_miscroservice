package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxPublish;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 11:16
 */
public interface SdxPublishDao {
	TSdxPublish selectByMainKey(int mainKey);

	int update(TSdxPublish... tCsqPublish);

	int update(TSdxPublish tCsqPublish);

	int insert(TSdxPublish... tCsqPublishes);
}
