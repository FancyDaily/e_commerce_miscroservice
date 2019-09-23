package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.CsqWechatConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.e_commerce.miscroservice.csq_proj.vo.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqMsgService;
import com.e_commerce.miscroservice.user.wechat.entity.TemplateData;
import com.e_commerce.miscroservice.user.wechat.entity.WxMssVo;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 15:09
 */
@Transactional(rollbackFor = Throwable.class)
@Service
@Log
public class CsqMsgServiceImpl implements CsqMsgService {

	@Autowired
	private CsqMsgDao csqMsgDao;

	@Autowired
	private CsqServiceDao csqServiceDao;

	@Autowired
	private CsqUserDao csqUserDao;

	@Autowired
	private CsqFormIdDao csqFormIdDao;

	@Autowired
	private WechatService wechatService;

	@Autowired
	private CsqOrderDao csqOrderDao;

	@Autowired
	private CsqPaymentService csqPaymentService;

	@Value("${page.person}")
	private String INDEX_PAGE;

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
				if (csqServiceList == null) {
					return csqSysMsgVo;
				}
				//装载项目信息
				TCsqService tCsqService = csqServiceList.get(0);
//				csqSysMsgVo.setCsqService(tCsqService);
				csqSysMsgVo.setFundId(tCsqService.getFundId());
				csqSysMsgVo.setServiceId(tCsqService.getId());
				csqSysMsgVo.setName(tCsqService.getName());
				csqSysMsgVo.setDescription(tCsqService.getDescription());
				String coverPic = tCsqService.getCoverPic();
				csqSysMsgVo.setCoverPic(coverPic.contains(",")? Arrays.asList(coverPic.split(",")).get(0):coverPic);
				csqSysMsgVo.setSumTotalIn(tCsqService.getSumTotalIn());
				csqSysMsgVo.setServiceType(tCsqService.getType());
				csqSysMsgVo.setPersonInCharge(tCsqService.getPersonInCharge());
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
		if (CsqSysMsgEnum.TYPE_SREVICE.getCode() == type && csqSysMsg.getServiceId() == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当为项目类型时，serviceId不能为空!");
		}

		if (CsqSysMsgEnum.TYPE_NORMAL.getCode() == type && StringUtil.isAnyEmpty(csqSysMsg.getTitle(), csqSysMsg.getContent())) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当为普通类型时，title，content不能为空!");
		}

		String title = csqSysMsg.getTitle();
		title = title == null ? "收到一个项目" : title;    //默认项目类型消息名称
		csqSysMsg.setTitle(title);

		List<TCsqSysMsg> toInserter = new ArrayList<>();
		//check operator

		//判断userId
		Long userId = csqSysMsg.getUserId();
		if (userId == null) {
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
		} else {    //特定
			TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
			if (csqUser == null) {
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
		content = contentChanger != null ? String.format(content, contentChanger) : content;

		List<TCsqSysMsg> toInserter = new ArrayList<>();
		List<Long> tUserIds = Arrays.stream(userId).distinct().collect(Collectors.toList());	//去重
		for (Long theId : tUserIds) {
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
		if (currentEnum == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "type参数有误！");
		}
		insertTemplateMsg(currentEnum, userId);
	}

	@Override
	public void collectFormId(Long userId, String formId) {
		//排除譬如"the formId is a mock one"等无效的formid
		if(formId == null || "the formId is a mock one".equals(formId.trim())) {
			return;
		}
		//复用
		TCsqFormId csqFormId = csqFormIdDao.selectByUserIdAndFormId(userId, formId);

		if (csqFormId != null) {
			return;
		}
		//插入
		csqFormId = TCsqFormId.builder()
			.userId(userId)
			.formId(formId).build();
		csqFormId.save();
	}

	private TCsqSysMsg getBaseEntity() {
		return getBaseBuilder().build();
	}

	private TCsqSysMsg.TCsqSysMsgBuilder getBaseBuilder() {
		return TCsqSysMsg.builder();
	}

	@Override
	public void sendServiceMsg(Long messageUserId, CsqServiceMsgEnum csqServiceMsgEnum, CsqServiceMsgParamVo csqServiceMsgParamVo) {
		log.info("开始发送服务通知..., userId={}, csqServiceMsgEnum={}", messageUserId, csqServiceMsgEnum.name());
		//check
		TCsqUser toUser = csqUserDao.selectByPrimaryKey(messageUserId);    //获取要发放的对象
		if (StringUtil.isEmpty(toUser.getVxOpenId())) {    //没有微信openid的用户无法收到通知
			log.warn("发送服务通知终止。该用户没有openid. userId={}", messageUserId);
			return;
		}
		TCsqFormId formid = csqFormIdDao.selectAvailableFormIdByUserId(messageUserId);    //获取formid
		if (formid == null) {
			return;
		}
		try {
			//do
			String parameter = "";
			String and = "&";
			StringBuilder builder = new StringBuilder();
			List<String> msg = new ArrayList<>();
			CsqUserAuthVo authVo = csqServiceMsgParamVo.getCsqUserAuthVo();
			switch (csqServiceMsgEnum) {
				case FUND_PUBLIC_SUCCESS:
					CsqServiceListVo entity = csqServiceMsgParamVo.getCsqServiceListVo();
					msg.add(entity.getName());	//项目名称(此处为基金名称)
					msg.add(entity.getExpectedAmount().toString());	//筹款目标(此处为平台标定的定额)
					msg.add(entity.getSurplusAmount().toString());	//实际筹款
					msg.add(entity.getDonePercent());	//完成百分比

					//参数: ?type=1&serviceId=    (此处的serviceId需传fundId
					Long fundId = entity.getFundId();
					builder.append("?type=1").append(and).append("serviceId=").append(fundId);
					break;
				case CORP_CERT_SUCCESS:
					msg.add(authVo.getName());	//公司名
					msg.add("通过");	//审核结果(通过、不通过
					msg.add(DateUtil.timeStamp2Date(System.currentTimeMillis(), "yyyy-MM-dd"));	//认证时间
					break;
				case CORP_CERT_FAIL:
					msg.add(authVo.getName());	//公司名
					msg.add("不通过");	//审核结果(通过、不通过
					msg.add(DateUtil.timeStamp2Date(System.currentTimeMillis(), "yyyy-MM-dd"));	//认证时间
					break;
				case SERVICE_NOTIFY_WHILE_CONSUME:
					//参数 type=3,serviceId=
//					builder.append("?type=3").append(and).append("serviceId=").append(serviceId);
					break;
				case INVOICE_DONE:
					CsqUserInvoiceVo csqUserInvoiceVo = csqServiceMsgParamVo.getCsqUserInvoiceVo();
					String orderNos = csqUserInvoiceVo.getOrderNos();
					String serviceName = "";
					if(!StringUtil.isEmpty(orderNos)) {
						String[] split = orderNos.split(",");
						String orderNo = split[0];
						TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);
						Long toId = tCsqOrder.getToId();
						Integer toType = tCsqOrder.getToType();
						//TODO
						Map<String, Object> beneficiaryMap = csqPaymentService.getBeneficiaryMap(toType, toId);
						serviceName = (String) beneficiaryMap.get("beneficiaryName");
						if(split.length > 1) {
							serviceName += "等";
						}
					}
					msg.add(serviceName);	//开票项目(节选
					msg.add(csqUserInvoiceVo.getName());	//发票抬头
					msg.add(csqUserInvoiceVo.getExpressNo());	//快递单号
					msg.add(csqUserInvoiceVo.getAmount().toString());	//开票金额
					break;
			}
			parameter = builder.append(parameter).toString();
			pushOneUserMsg(toUser.getVxOpenId(), formid.getFormId(), msg, csqServiceMsgEnum, parameter);
			//使用过的formid即失效
			formid.setIsValid(AppConstant.IS_VALID_NO);
			csqFormIdDao.update(formid);
		} catch (Exception e) {
			log.error("发送服务通知失败, userId={}, formid={}, csqServiceMsgEnum={}", messageUserId, formid.getFormId(), csqServiceMsgEnum.name());
		}
	}

	/**
	 * 发送服务通知
	 *
	 * @param openid
	 * @param formid
	 * @param msg
	 * @param setTemplateIdEnum
	 * @param parameter
	 * @return
	 */
	private String pushOneUserMsg(String openid, String formid, List<String> msg, CsqServiceMsgEnum setTemplateIdEnum, String parameter) {
		RestTemplate restTemplate = new RestTemplate();

		String token = wechatService.getToken(CsqWechatConstant.APP_ID, CsqWechatConstant.APP_SECRET);    //获取access_token
		String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send" + "?access_token=" + token;
		// 拼接推送的模版
		WxMssVo wxMssVo = new WxMssVo();
		wxMssVo.setPage(INDEX_PAGE);    //设置默认page -> 首页

		wxMssVo.setTouser(openid);// 用户openid
		wxMssVo.setTemplate_id(setTemplateIdEnum.getTemplateId());
		if (!setTemplateIdEnum.getPage().isEmpty()) {
			wxMssVo.setPage(setTemplateIdEnum.getPage() + parameter);    //构建 跳转的page和参数
		}
		wxMssVo.setForm_id(formid);// formid

		Map<String, TemplateData> m = new HashMap(msg.size());

		// 循环放入数据
		for (int i = 0; i < msg.size(); i++) {
			TemplateData keyword = new TemplateData();
			keyword.setValue(msg.get(i));
			m.put("keyword" + (i + 1), keyword);
			wxMssVo.setData(m);
		}
//		if (setTemplateIdEnum.getEnlarge() != 0) {
//			wxMssVo.setEmphasis_keyword("keyword" + setTemplateIdEnum.getEnlarge()+".DATA");
//		}

		log.info("小程序推送结果={}", url);
		log.info("小程序推送结果={}", wxMssVo.toString());
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxMssVo, String.class);
		log.info("小程序推送结果={}", responseEntity.getBody());
		return responseEntity.getBody();
	}

	@Override
	public void sendServiceMsgForFund(TCsqFund fund, Long userId) {
		insertTemplateMsg(fund.getName(), CsqSysMsgTemplateEnum.FUND_PUBLIC_SUCCESS, userId);
		//TODO 发送服务通知
		double doubleVal = CsqFundEnum.PUBLIC_MINIMUM == 0 ? 1 : fund.getSumTotalIn() / CsqFundEnum.PUBLIC_MINIMUM;
		doubleVal = doubleVal > 1d ? 1d:doubleVal;
		String donePercent = DecimalFormat.getPercentInstance().format(doubleVal).replaceAll("%", "");
		sendServiceMsg(userId, CsqServiceMsgEnum.FUND_PUBLIC_SUCCESS, CsqServiceMsgParamVo.builder()
			.csqServiceListVo(
				CsqServiceListVo.builder()
					.fundId(fund.getId())
					.name(fund.getName())
					.expectedAmount(CsqFundEnum.PUBLIC_MINIMUM)
					.surplusAmount(fund.getSumTotalIn())
					.donePercent(donePercent).build()
			)
			.build()
		);
	}

	@Override
	public QueryResult<TCsqSysMsg> list(String searchParam, Integer pageNum, Integer pageSize) {
		//searchParam为标题
		MybatisPlusBuild baseBuild = csqMsgDao.getBaseBuild();
		if(!StringUtil.isEmpty(searchParam)) {
			baseBuild = baseBuild.eq(TCsqSysMsg::getTitle, searchParam);
		}

		List<TCsqSysMsg> tCsqSysMsgs = csqMsgDao.selectWithBuildPage(baseBuild, pageNum, pageSize);
		List<Long> userIds = tCsqSysMsgs.stream()
			.map(TCsqSysMsg::getUserId).collect(Collectors.toList());
		List<TCsqUser> csqUsers = csqUserDao.selectInIds(userIds);
		Map<Long, List<TCsqUser>> idUserMap = csqUsers.stream()
			.collect(Collectors.groupingBy(TCsqUser::getId));
		tCsqSysMsgs = tCsqSysMsgs.stream()
			.map(a -> {
				Long userId = a.getUserId();
				List<TCsqUser> tCsqUsers = idUserMap.get(userId);
				if(!tCsqUsers.isEmpty()) {
					String name = tCsqUsers.get(0).getName();
					a.setReceiverName(name);
				}
				return a;
			}).collect(Collectors.toList());

		long total = IdUtil.getTotal();

		QueryResult<TCsqSysMsg> tCsqSysMsgQueryResult = new QueryResult<>();
		tCsqSysMsgQueryResult.setResultList(tCsqSysMsgs);
		tCsqSysMsgQueryResult.setTotalCount(total);

		return tCsqSysMsgQueryResult;
	}

}
