package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.vo.CsqSysMsgVo;
import com.e_commerce.miscroservice.csq_proj.po.TCsqSysMsg;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 15:09
 */
public interface CsqMsgService {

	QueryResult<CsqSysMsgVo> list(Long userId, Integer pageNum, Integer pageSize);

	int unreadCnt(Long userId);

	void readAll(Long userId);

	void saveMsg(TCsqSysMsg csqSysMsg);

	void insert(Long userId, TCsqSysMsg csqSysMsg);

	void insertTemplateMsg(Long userId, Integer type);
}
