package com.e_commerce.miscroservice.sdx_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxCommonsPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxFocusPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTrendsPo;

import java.util.List;

public interface SdxCommonsDao {
	//查询我关注的书友列表（包括我互相关注的）
	List<TSdxFocusPo> findTSdxFocusPoByUserId(long userId);

	//查询关注我的书友列表（互相关注）
	List<TSdxFocusPo> findTSdxFocusPoByUserId2(long userId);

	//根据ID查询用户信息表；
	TCsqUser findTCsqUserByUserId(Long id);

	//根据用户ID查询好友动态表
	List<TSdxTrendsPo> findsdxCommonsDaoByUserId(Long id);

	//根据动态ID查询下面的评论
	List<TSdxCommonsPo> findsdxCommonsDaoByTrendsId(Long id);

	//根据用户ID查询动态表信息；
	List<TSdxTrendsPo> findTSdxTrendsPoByUserId(Long userId);

	//根据书籍信息编号查询动态表得到userID
	List<TSdxTrendsPo> findTSdxTrendsPoByBookInfoId(Long bookInfoId);
}
