package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqSysMsgEnum;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqMsgDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqSysMsg;
import com.e_commerce.miscroservice.csq_proj.service.CsqMsgService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 15:09
 */
@Service
public class CsqMsgServiceImpl implements CsqMsgService {

	@Autowired
	private CsqMsgDao csqMsgDao;

	@Autowired
	private CsqServiceDao csqServiceDao;

	@Override
	public QueryResult<TCsqSysMsg> list(Long userId, Integer pageNum, Integer pageSize) {
		return list(userId, pageNum, pageSize, Boolean.FALSE);
	}

	private QueryResult<TCsqSysMsg> list(Long userId, Integer pageNum, Integer pageSize, boolean isUnread) {
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqSysMsg> tCsqSysMsgs;
		if (isUnread) {
			tCsqSysMsgs = csqMsgDao.selectByUserIdAndIsRead(userId, CsqSysMsgEnum.IS_READ_FALSE.getCode());
		} else {
			tCsqSysMsgs = csqMsgDao.selectByUserId(userId);
		}
		//如果为"收到一个项目"类型，查询项目
		List<Long> serviceIds = tCsqSysMsgs.stream()
			.filter(a -> CsqSysMsgEnum.TYPE_SREVICE.getCode() == a.getType())
			.map(TCsqSysMsg::getServiceId).collect(Collectors.toList());
		List<TCsqService> tCsqServices = csqServiceDao.selectInIds(serviceIds);
		Map<Long, List<TCsqService>> serviceMap = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));
		List<TCsqSysMsg> resultList = tCsqSysMsgs.stream()
			.map(a -> {
				TCsqService tCsqService = serviceMap.get(a.getServiceId()).get(0);
				String dateString = DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd");
				a.setDateString(dateString);
				a.setCsqService(tCsqService);
				return a;
			}).collect(Collectors.toList());

		QueryResult<TCsqSysMsg> queryResult = new QueryResult<>();
		queryResult.setResultList(resultList);
		queryResult.setTotalCount(startPage.getTotal());
		return queryResult;
	}

	@Override
	public int unreadCnt(Long userId) {
		List<TCsqSysMsg> tCsqSysMsgs = csqMsgDao.selectByUserIdAndIsRead(userId, CsqSysMsgEnum.IS_READ_FALSE.getCode());
		return tCsqSysMsgs == null ? 0 : tCsqSysMsgs.size();
	}

	@Override
	public void readAll(Long userId) {
		List<TCsqSysMsg> tCsqSysMsgs = csqMsgDao.selectByUserIdAndIsRead(userId, CsqSysMsgEnum.IS_READ_FALSE.getCode());
		List<TCsqSysMsg> toUpdater = tCsqSysMsgs.stream()
			.map(a -> {
				a.setIsRead(CsqSysMsgEnum.IS_READ_TRUE.getCode());
				return a;
			}).collect(Collectors.toList());
		csqMsgDao.update(toUpdater);
	}

	@Override
	public void saveMsg(TCsqSysMsg csqSysMsg) {
		csqMsgDao.insert(csqSysMsg);
	}
}
