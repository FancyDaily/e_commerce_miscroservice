package com.e_commerce.miscroservice.sdx_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.po.*;

import java.util.List;

public interface TSdxFocusDao {
	int saveTSdxFocusIfNotExist(TSdxFocusPo tSdxFocusPo);

	int modTSdxFocus(TSdxFocusPo tSdxFocusPo);

	int delTSdxFocusByIds(Long... ids);

	TSdxFocusPo findTSdxFocusById(Long id);

	//查找所有关注书友表
	List<TSdxFocusPo> findTSdxFocusByAll(TSdxFocusPo tSdxFocusPo, Integer page, Integer size);

	//查询我关注的书友列表（包括互相关注的）
	List<TSdxFocusPo> FindTSdxFocusByUserIdIfocus(Long userId, Integer page, Integer size);

	//查询关注我的书友列表（包括互相关注的）
	List<TSdxFocusPo> FindTSdxFocusByUserIdFocusMe(Long bookFriendId, Integer page, Integer size);

	//互相关注（修改）
	Integer updateFocus(TSdxFocusPo tSdxFocusPo);

	//查询书友表 id,type,typeName
	TSdxFocusPo findTSdxFocus(Long id);

	//查询互相关注（关注我的）
	List<TSdxFocusPo> findTSdxmFocus(Long bookFriendId, int type);

	//查询互相关注（我关注的）
	List<TSdxFocusPo> findTSdxmFocus2(Long userId, int type);

	//根据书名查询书籍编号
	List<TSdxBookInfoPo> findTSdxBookInfoPo(String bookName);

	//2：根据书籍编号和好友ID查询 书袋熊书籍 表
	TSdxBookPo findTSdxBookPo(Long bookInfoId, Long currentOwnerId);

	//根据userid查询用户姓名
	TCsqUser findtSdxFocusDao(Long currentOwnerId);

	//根据用户ID查询书籍读后感 表
	List<TSdxBookAfterReadingNotePo> findTSdxBookAfterReadingNotePo(Long userId);

	//根据书籍信息编号查询书籍详情
	TSdxBookInfoPo findTSdxBookInfoPoById(Long bookInfoId);
}
