package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqSysMsgEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqMsgDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqSysMsg;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqMsgService;
import com.e_commerce.miscroservice.user.dao.UserDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 15:09
 */
@Transactional(rollbackFor = Throwable.class)
@Service
public class CsqMsgServiceImpl implements CsqMsgService {

	@Autowired
	private CsqMsgDao csqMsgDao;

	@Autowired
	private CsqServiceDao csqServiceDao;

	@Autowired
	private CsqUserDao csqUserDao;

	@Override
	public QueryResult<TCsqSysMsg> list(Long userId, Integer pageNum, Integer pageSize) {
		return list(userId, pageNum, pageSize, Boolean.FALSE);
	}

	private QueryResult<TCsqSysMsg> list(Long userId, Integer pageNum, Integer pageSize, boolean isUnread) {
		pageNum = pageNum == null ? 1 : pageNum;
		pageSize = pageSize == null ? 0 : pageSize;
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
		List<TCsqService> tCsqServices = serviceIds.isEmpty() ? new ArrayList<>() : csqServiceDao.selectInIds(serviceIds);
		Map<Long, List<TCsqService>> serviceMap = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));
		List<TCsqSysMsg> resultList = tCsqSysMsgs.stream()
			.map(a -> {
				String dateString = DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd");
				a.setDateString(dateString);
				List<TCsqService> csqServiceList = serviceMap.get(a.getServiceId());
				if(csqServiceList == null) {
					return a;
				}
				TCsqService tCsqService = csqServiceList.get(0);
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

	@Override
	public void insert(Long operatorId, TCsqSysMsg csqSysMsg) {
		List<TCsqSysMsg> toInserter = new ArrayList<>();
		//check operator

		//判断userId
		Long userId = csqSysMsg.getUserId();
		if(userId == null) {
			//	全部
			List<TCsqUser> csqUsers = csqUserDao.selectAll();
			List<Long> userIds = csqUsers.stream()
				.map(TCsqUser::getId).collect(Collectors.toList());
			toInserter = userIds.stream().map(
				a -> {
					TCsqSysMsg tCsqSysMsg = csqSysMsg.copyTCsqSysMsg();
					tCsqSysMsg.setUserId(a);
					return tCsqSysMsg;
				}
			).collect(Collectors.toList());
		} else  {	//特定
			TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
			if(csqUser == null) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的用户id!");
			}
			toInserter.add(csqSysMsg);
		}
		csqMsgDao.insert(toInserter);
	}

}
