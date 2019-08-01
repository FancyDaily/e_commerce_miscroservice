package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.vo.CsqSysMsgVo;
import com.e_commerce.miscroservice.csq_proj.dao.CsqMsgDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqSysMsg;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqMsgService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
	public QueryResult<CsqSysMsgVo> list(Long userId, Integer pageNum, Integer pageSize) {
		return list(userId, pageNum, pageSize, Boolean.FALSE);
	}

	private QueryResult<CsqSysMsgVo> list(Long userId, Integer pageNum, Integer pageSize, boolean isUnread) {
		pageNum = pageNum == null ? 1 : pageNum;
		pageSize = pageSize == null ? 0 : pageSize;
//		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqSysMsg> tCsqSysMsgs;
		if (isUnread) {
			tCsqSysMsgs = csqMsgDao.selectByUserIdAndIsReadDescPage(pageNum, pageSize, userId, CsqSysMsgEnum.IS_READ_FALSE.getCode());
		} else {
			tCsqSysMsgs = csqMsgDao.selectByUserIdDescPage(pageNum, pageSize, userId);
		}
		long total = IdUtil.getTotal();
		//如果为"收到一个项目"类型，查询项目
		List<Long> serviceIds = tCsqSysMsgs.stream()
			.filter(a -> CsqSysMsgEnum.TYPE_SREVICE.getCode() == a.getType())
			.map(TCsqSysMsg::getServiceId).collect(Collectors.toList());
		List<TCsqService> tCsqServices = serviceIds.isEmpty() ? new ArrayList<>() : csqServiceDao.selectInIds(serviceIds);
		Map<Long, List<TCsqService>> serviceMap = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));

		List<CsqSysMsgVo> resultList = tCsqSysMsgs.stream()
//			.sorted(Comparator.comparing(TCsqSysMsg::getCreateTime))
			.map(a -> {
				CsqSysMsgVo csqSysMsgVo = a.copyCsqSysMsgVo();
				String dateString = DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd");
				csqSysMsgVo.setDateString(dateString);
				List<TCsqService> csqServiceList = serviceMap.get(a.getServiceId());
				if(csqServiceList == null) {
					return csqSysMsgVo;
				}
				//装载项目信息
				TCsqService tCsqService = csqServiceList.get(0);
//				csqSysMsgVo.setCsqService(tCsqService);
				csqSysMsgVo.setFundId(tCsqService.getFundId());
				csqSysMsgVo.setServiceId(tCsqService.getId());
				csqSysMsgVo.setName(tCsqService.getName());
				csqSysMsgVo.setDescription(tCsqService.getDescription());
				csqSysMsgVo.setCoverPic(tCsqService.getCoverPic());
				csqSysMsgVo.setSumTotalIn(tCsqService.getSumTotalIn());
				csqSysMsgVo.setServiceType(tCsqService.getType());
				return csqSysMsgVo;
			}).collect(Collectors.toList());

		QueryResult<CsqSysMsgVo> queryResult = new QueryResult<>();
		queryResult.setResultList(resultList);
		queryResult.setTotalCount(total);
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
//				a.setDeletedFlag(null);	//TODO
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
		//check参数
		Integer type = csqSysMsg.getType();
		if(CsqSysMsgEnum.TYPE_SREVICE.getCode() ==  type && csqSysMsg.getServiceId() == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当为项目类型时，serviceId不能为空!");
		}

		if(CsqSysMsgEnum.TYPE_NORMAL.getCode() == type && StringUtil.isAnyEmpty(csqSysMsg.getTitle(), csqSysMsg.getContent())) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当为普通类型时，title，content不能为空!");
		}

		String title = csqSysMsg.getTitle();
		title = title == null? "收到一个项目": title;	//默认项目类型消息名称
		csqSysMsg.setTitle(title);

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

	@Override
	public void insertTemplateMsg(CsqSysMsgTemplateEnum currentEnum, Long... userId) {
		insertTemplateMsg(null, currentEnum, userId);
	}

	@Override
	public void insertTemplateMsg(String contentChanger, CsqSysMsgTemplateEnum currentEnum, Long... userId) {
		String title = currentEnum.getTitle();
		String content = currentEnum.getContent();
		content = contentChanger != null? String.format(content, contentChanger):content;

		List<TCsqSysMsg> toInserter = new ArrayList<>();
		for(Long theId:userId) {
			TCsqSysMsg.TCsqSysMsgBuilder builder = getBaseBuilder();
			builder.type(CsqSysMsgEnum.TYPE_NORMAL.getCode())
				.userId(theId)
				.title(title)
				.content(content);
			TCsqSysMsg build = builder.build();
			toInserter.add(build);
		}
		csqMsgDao.insert(toInserter);
	}

	@Override
	public void insertTemplateMsg(Integer type, Long... userId) {
		CsqSysMsgTemplateEnum currentEnum = CsqSysMsgTemplateEnum.getType(type);
		if(currentEnum == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "type参数有误！");
		}
		insertTemplateMsg(currentEnum, userId);
	}

	private TCsqSysMsg getBaseEntity() {
		return getBaseBuilder().build();
	}

	private TCsqSysMsg.TCsqSysMsgBuilder getBaseBuilder() {
		return TCsqSysMsg.builder();
	}
	
}
