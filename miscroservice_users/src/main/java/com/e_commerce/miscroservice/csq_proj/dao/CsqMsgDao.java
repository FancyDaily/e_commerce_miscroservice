package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqSysMsg;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 15:14
 */
public interface CsqMsgDao {

	List<TCsqSysMsg> selectByUserId(Long userId);

	List<TCsqSysMsg> selectByUserIdAndIsRead(Long userId, int isRead);

	int update(List<TCsqSysMsg> toUpdater);

	int insert(TCsqSysMsg... csqSysMsg);

	int insert(List<TCsqSysMsg> toInserter);
}
