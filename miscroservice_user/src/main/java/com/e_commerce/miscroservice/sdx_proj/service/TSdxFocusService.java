package com.e_commerce.miscroservice.sdx_proj.service;

import com.e_commerce.miscroservice.sdx_proj.po.TSdxFocusPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxFocusVo;

import java.util.List;

public interface TSdxFocusService {
	long modTSdxFocus(TSdxFocusPo tSdxFocusPo);

	int delTSdxFocusByIds(Long... ids);

	TSdxFocusVo findTSdxFocusById(Long id);

	//查找所有关注书友表
	List<TSdxFocusVo> findTSdxFocusByAll(TSdxFocusPo tSdxFocusPo, Integer page, Integer size);

	//查询书友列表（我关注的/关注我的）
	List<TSdxFocusVo> FindTSdxFocusByUserId(Long userId, Integer type, Integer page, Integer size);

	//互相关注
	int mutualBookFriend(Long userId, Long id);

	//取消关注
	int cancelMutualBookFriend(Long userId, Long id);
}
