package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.NumberUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqMsgService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceMsgParamVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqWaitToInvoiceOrderVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserInvoiceVo;
import com.e_commerce.miscroservice.csq_proj.service.CsqInvoiceService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqInvoiceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 14:42
 */
@Transactional(rollbackFor = Throwable.class)
@Service
public class CsqInvoiceServiceImpl implements CsqInvoiceService {

	@Autowired
	private CsqOrderDao csqOrderDao;

	@Autowired
	private CsqUserInvoiceDao csqUserInvoiceDao;

	@Autowired
	private CsqServiceDao csqServiceDao;

	@Autowired
	private CsqFundDao csqFundDao;

	@Autowired
	private CsqUserDao csqUserDao;

	@Autowired
	private CsqMsgService csqMsgService;

	@Override
	public void submit(Long userId, TCsqUserInvoice userInvoice, String... orderNo) {
		MessageException messageException = new MessageException(AppErrorConstant.NOT_PASS_PARAM, "订单编号不能为空!");
		if(orderNo == null || StringUtil.isAnyEmpty(orderNo)) {
			throw messageException;
		}
		//检查订单状态
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectInOrderNos(orderNo);
		tCsqOrders.stream()
			.forEach((a) -> {
				Integer status = a.getStatus();
				Integer invoiceStatus = a.getInVoiceStatus();
				//TODO 是否需要校验订单发起人与发票发起人身份
				if (CsqOrderEnum.STATUS_ALREADY_PAY.getCode() != status) {    //如果不是已支付
					throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "存在未成功支付订单，无法开票!");
				}
				if (CsqOrderEnum.INVOICE_STATUS_YES.getCode() == invoiceStatus) {
					throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "存在已开过票的订单，无法再开票!");
				}
			});
		Double totalFee = tCsqOrders.stream()
			.map(TCsqOrder::getPrice)
			.reduce(0d, Double::sum);
		if(totalFee < CsqInvoiceEnum.MINIMUM_AMOUNT) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "所选订单总额未达到"+ CsqInvoiceEnum.MINIMUM_AMOUNT + "元,无法开票!");
		}
		//开票
		Integer type = userInvoice.getType();
		String name = userInvoice.getName();
		String taxNo = userInvoice.getTaxNo();
		String addr = userInvoice.getAddr();
		String person = userInvoice.getPerson();
		String telephone = userInvoice.getTelephone();
		if (StringUtil.isAnyEmpty(name, addr, person, telephone) || Objects.isNull(type) || (CsqInvoiceEnum.TYPE_CORP.getCode() == type && StringUtil.isEmpty(taxNo))) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "必要参数为空!");
		}

		String orderNos = Arrays.stream(orderNo)
			.reduce("", (a, b) -> a + "," + b);
		orderNos = orderNos.startsWith(",")? orderNos.substring(1):orderNos;
		userInvoice.setUserId(userId);
		userInvoice.setOrderNos(orderNos);
		userInvoice.setAmount(totalFee);
		csqUserInvoiceDao.insert(userInvoice);

		//修改订单的开票状态
		List<TCsqOrder> toUpdateList = tCsqOrders.stream().map(a -> {
			Long originId = a.getId();
			a = new TCsqOrder();
			a.setId(originId);
			a.setInVoiceStatus(CsqOrderEnum.INVOICE_STATUS_YES.getCode());
			return a;
		}).collect(Collectors.toList());

		//sysMsg
//		csqMsgService.insertTemplateMsg(Arrays.asList(orderNos.split(",")).get(0) ,CsqSysMsgTemplateEnum.INVOICE_DONE, userId);
//		csqMsgService.insertTemplateMsg(CsqSysMsgTemplateEnum.INVOICE_DONE, userId);

		csqOrderDao.update(toUpdateList);
	}

	@Override
	public QueryResult<CsqWaitToInvoiceOrderVo> waitToList(Long userId, Integer pageNum, Integer pageSize) {
		pageNum = pageNum==null? 1: pageNum;
		pageSize = pageSize==null? 0: pageSize;
//		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		//查询所有待开票的订单,找到serivce汇总
//		List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndFromTypeAndToTypeInvoiceStatusAndStatusDesc(userId, CsqEntityTypeEnum.TYPE_HUMAN.toCode(), CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqOrderEnum.INVOICE_STATUS_NO.getCode(), CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		//处理 爱心账户、基金、项目的数据，其中基金和项目要获取到名字
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndFromTypeAndInvoiceStatusAndStatusDescPage(pageNum, pageSize, userId, CsqEntityTypeEnum.TYPE_HUMAN.toCode(), CsqOrderEnum.INVOICE_STATUS_NO.getCode(), CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		long total = IdUtil.getTotal();
		//获取名字、以及对时间进行格式化
		Map<String, Object> typeListMapMap = getTypeListMapMap(userId, tCsqOrders);
		Map<Long, List<TCsqService>> serviceMap = getServiceListMap(typeListMapMap);
		Map<Long, List<TCsqFund>> fundMap = getFundListMap(typeListMapMap);
		Map<Long, List<TCsqUser>> userMap = getUserListMap(typeListMapMap);

		List<CsqWaitToInvoiceOrderVo> resultList = tCsqOrders.stream()
			.map(a -> {
					String name = "未知业务";
					//类型判断
					name = getItemName(serviceMap, fundMap, userMap, a);
					CsqWaitToInvoiceOrderVo csqOrderVo = a.copyCsqOrderVo();    //包含了金额、orderNo
					csqOrderVo.setServiceName(name);    //名
					csqOrderVo.setDate(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));    //日期
					csqOrderVo.setPrice(NumberUtil.keep2Places(a.getPrice()));
					return csqOrderVo;
				}
			).collect(Collectors.toList());
		QueryResult<CsqWaitToInvoiceOrderVo> queryResult = new QueryResult<>();
		queryResult.setResultList(resultList);
		queryResult.setTotalCount(total);
		return queryResult;
	}

	private Map<Long, List<TCsqService>> getServiceListMap(Map<String, Object> typeListMapMap) {
		return (Map<Long, List<TCsqService>>) typeListMapMap.get("serviceMap");
	}

	private Map<Long, List<TCsqFund>> getFundListMap(Map<String, Object> typeListMapMap) {
		return (Map<Long, List<TCsqFund>>) typeListMapMap.get("fundMap");
	}

	private Map<Long, List<TCsqUser>> getUserListMap(Map<String, Object> typeListMapMap) {
		return (Map<Long, List<TCsqUser>>) typeListMapMap.get("userMap");
	}

	private Map<String, Object> getTypeListMapMap(Long userId, List<TCsqOrder> tCsqOrders) {
		Map<String, Object> resultMap = new HashMap<>();
		//准备三个List
		Map<String, Object> typeListMap = getTypeListMap(userId, tCsqOrders);
		List<Long> fundIds = (List<Long>) typeListMap.get("fundIds");
		List<Long> serviceIds = (List<Long>) typeListMap.get("serviceIds");
		List<Long> userIds = (List<Long>) typeListMap.get("userIds");

		//构建serivce
		List<TCsqService> tCsqServices = serviceIds.isEmpty()? new ArrayList<>() : csqServiceDao.selectInIds(serviceIds);
		Map<Long, List<TCsqService>> serviceMap = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));
		//构建fund
		List<TCsqFund> tCsqFunds = fundIds.isEmpty()? new ArrayList<>() : csqFundDao.selectInIds(fundIds);
		Map<Long, List<TCsqFund>> fundMap = tCsqFunds.stream()
			.collect(Collectors.groupingBy(TCsqFund::getId));
		//构建user
		List<TCsqUser> tCsqUsers = userIds.isEmpty() ? new ArrayList<>() : csqUserDao.selectInIds(userIds);
		Map<Long, List<TCsqUser>> userMap = tCsqUsers.stream()
			.collect(Collectors.groupingBy(TCsqUser::getId));

		resultMap.put("serviceMap", serviceMap);
		resultMap.put("fundMap", fundMap);
		resultMap.put("userMap", userMap);

		return resultMap;
	}

	private Map<String, Object> getTypeListMap(Long userId, List<TCsqOrder> tCsqOrders) {
		Map<String, Object> map = new HashMap<>();
		List<Long> fundIds = new ArrayList<>();
		List<Long> serviceIds = new ArrayList<>();
		List<Long> userIds = new ArrayList<>();
		tCsqOrders.stream()
			.forEach(a -> {
				Long toId = a.getToId();
				switch (CsqEntityTypeEnum.getEnum(a.getToType())) {
					case TYPE_FUND:
						fundIds.add(toId);
						break;
					case  TYPE_SERVICE:
						serviceIds.add(toId);
						break;
					case TYPE_ACCOUNT:
						userIds.add(toId==null? userId: toId);
						break;
				}
		});
		map.put("fundIds", fundIds);
		map.put("serviceIds", serviceIds);
		map.put("userIds", userIds);
		return map;
	}

	@Override
	public QueryResult<CsqUserInvoiceVo> doneList(Long userId, Integer pageNum, Integer pageSize) {
		pageNum = pageNum==null? 1:pageNum;
		pageSize = pageSize==null? 0:pageSize;
//		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserInvoice> tCsqUserInvoices = csqUserInvoiceDao.selectByUserIdDescPage(userId, pageNum, pageSize);
		long total = IdUtil.getTotal();
		List<CsqUserInvoiceVo> copyList = tCsqUserInvoices.stream()
			.map(a -> {
				String orderNos = a.getOrderNos();
				int recordCnt = orderNos.split(",").length;
				a.setRecordCnt(recordCnt);
				a.setDateString(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));
				return a.copyCsqUserInvoiceVo();
			}).collect(Collectors.toList());
		QueryResult<CsqUserInvoiceVo> queryResult = new QueryResult<>();
		queryResult.setResultList(copyList);
		queryResult.setTotalCount(total);
		return queryResult;
	}

	@Override
	public CsqUserInvoiceVo invoiceDetail(Long userId, Long invoiceId) {
		TCsqUserInvoice tCsqUserInvoice = csqUserInvoiceDao.selectByPrimaryKey(invoiceId);
		if(tCsqUserInvoice == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的发票编号！");
		}
		String orderNos = tCsqUserInvoice.getOrderNos();
		tCsqUserInvoice.setRecordCnt(StringUtil.isEmpty(orderNos)? 0: orderNos.split(",").length);
		return tCsqUserInvoice.copyCsqUserInvoiceVo();
	}

	public static void main(String[] args) {
	    String name = "";
		System.out.println(name.split(",").length);
	}

	@Override
	public QueryResult<CsqInvoiceRecord> recordList(Long userId, Long invoiceId, Integer pageNum, Integer pageSize) {
		pageNum = pageNum==null? 1:pageNum;
		pageSize = pageSize==null? 0:pageSize;
		TCsqUserInvoice tCsqUserInvoice;
		if(invoiceId == null || (tCsqUserInvoice=csqUserInvoiceDao.selectByPrimaryKey(invoiceId)) == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的发票编号！");
		}
		String orderNos = tCsqUserInvoice.getOrderNos();
		if(StringUtil.isEmpty(orderNos)) {
			return new QueryResult<>();
		}
		String[] split = orderNos.split(",");
//		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectInOrderNosPage(split, pageNum, pageSize);
		long total = IdUtil.getTotal();

		Map<String, Object> typeListMapMap = getTypeListMapMap(userId, tCsqOrders);
		Map<Long, List<TCsqService>> serviceMap = getServiceListMap(typeListMapMap);
		Map<Long, List<TCsqFund>> fundMap = getFundListMap(typeListMapMap);
		Map<Long, List<TCsqUser>> userMap = getUserListMap(typeListMapMap);

		ArrayList<CsqInvoiceRecord> csqInvoiceList = new ArrayList<>();
		tCsqOrders.stream()
			.forEach(a -> {
				Long toId = a.getToId();
				String name = getItemName(serviceMap, fundMap, userMap, a);
				CsqInvoiceRecord vo = new CsqInvoiceRecord();
				vo.setItemId(toId);
				vo.setItemType(a.getToType());
				vo.setOrderNo(a.getOrderNo());
				vo.setDateString(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));
				vo.setMyAmount(a.getPrice());
				vo.setName(name);
				csqInvoiceList.add(vo);
			});
		QueryResult<CsqInvoiceRecord> queryResult = new QueryResult<>();
		queryResult.setResultList(csqInvoiceList);
		queryResult.setTotalCount(total);
		return queryResult;
	}

	@Override
	public int express(Long invoiceId, String expressNo) {
		if(StringUtil.isEmpty(expressNo)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "快递单号不能为空!");
		}
		TCsqUserInvoice tCsqUserInvoice = csqUserInvoiceDao.selectByPrimaryKey(invoiceId);
		if(tCsqUserInvoice == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该发票不存在!");
		}
		Long userId = tCsqUserInvoice.getUserId();

		TCsqUserInvoice build = TCsqUserInvoice.builder()
			.id(invoiceId)
			.expressNo(expressNo)
			.isOut(CsqInvoiceEnum.ISOUT_OUT_ALREADY.getCode()).build();
		//TODO 发送sysMsg & serivceMsg
		csqMsgService.insertTemplateMsg(CsqSysMsgTemplateEnum.INVOICE_DONE, userId);	//sysMsg
		csqMsgService.sendServiceMsg(userId, CsqServiceMsgEnum.INVOICE_DONE, CsqServiceMsgParamVo.builder()
			.csqUserInvoiceVo(tCsqUserInvoice.copyCsqUserInvoiceVo()).build()
		);

		return csqUserInvoiceDao.update(build);
	}

	@Override
	public Map list(String searchParam, Integer isOut, Integer pageNum, Integer pageSize, boolean isFuzzySearch) {
		//已开票总金额统计
		List<TCsqUserInvoice> csqUserInvoices = csqUserInvoiceDao.selectByIsOut(CsqInvoiceEnum.ISOUT_OUT_ALREADY.getCode());
		Double totalAmount = csqUserInvoices.stream()
			.map(TCsqUserInvoice::getAmount).reduce(0d, Double::sum);
		Map map = new HashMap();
		map.put("totalAmount", totalAmount);
		MybatisPlusBuild baseBuild = csqUserInvoiceDao.baseBuild();
		//判断是哪种searchParam
		String pattern = "^[0-9]\\d*$";
		boolean isNum = Pattern.matches(pattern, searchParam);
		if(!StringUtil.isEmpty(searchParam)) {
			if(isNum) {
				boolean matches = searchParam.length() > 30;	//生成的orderoNo长度为31,且以020开头
				if(!matches) {
					List<TCsqOrder> orders = csqOrderDao.selectByOrderNo(searchParam, isFuzzySearch);

					searchParam = orders == null || orders.isEmpty()? "": orders.get(0).getOrderNo();
				}
				if("".equals(searchParam)) {
					map.put("queryResult", new QueryResult<>());
					return map;
				}
				baseBuild.like(TCsqUserInvoice::getOrderNos, "%" + searchParam + "%");	//订单编号
			} else {	//按开票申请人名称搜索
				List<TCsqUser> tCsqUsers = csqUserDao.selectByName(searchParam, isFuzzySearch);//当前为关闭模糊查找
				List<Long> userIds = tCsqUsers.stream()
					.map(TCsqUser::getId).collect(Collectors.toList());
				baseBuild = userIds.isEmpty()? baseBuild : baseBuild.in(TCsqUserInvoice::getUserId, userIds);	//申请人
			}
		}
		baseBuild = isOut == null? baseBuild
			:baseBuild
			.eq(TCsqUserInvoice::getIsOut, isOut);	//是否已开票

		baseBuild
			.orderBy(MybatisPlusBuild.OrderBuild.buildAsc(TCsqUserInvoice::getIsOut));	//排序

		List<TCsqUserInvoice> tCsqUserInvoices = csqUserInvoiceDao.selectWithBuildPage(baseBuild, pageNum, pageSize);
		long total = IdUtil.getTotal();
		if(tCsqUserInvoices.isEmpty()) {
			map.put("queryResult", PageUtil.buildQueryResult(new ArrayList<>(), total));
			return map;
		}
		List<CsqUserInvoiceVo> resultList = tCsqUserInvoices.stream().map(a -> a.copyCsqUserInvoiceVo()).collect(Collectors.toList());
//		List<String> orderNoList = tCsqUserInvoices.stream().map(TCsqUserInvoice::getOrderNos).collect(Collectors.toList());
		List<String> orderNoList = new ArrayList<>();
		tCsqUserInvoices
			.forEach(a -> {
				String orderNos = a.getOrderNos();
				if(orderNos.contains(",")) {
					String[] split = orderNos.split(",");
					orderNoList.addAll(Arrays.asList(split));
				} else {
					orderNoList.add(orderNos);
				}
			});

		List<TCsqOrder> orders = orderNoList.isEmpty()? new ArrayList<>() : csqOrderDao.selectInOrderNos(orderNoList);
		orders = orders.stream()
			.map(a -> {
				Integer toType = a.getToType();
				Long toId = a.getToId();
				String name = "";
				if(toType == CsqEntityTypeEnum.TYPE_FUND.toCode()) {
					TCsqFund csqFund = csqFundDao.selectByPrimaryKey(toId);
					if(csqFund != null) {
						name = csqFund.getName();
					}
				} else if(toType == CsqEntityTypeEnum.TYPE_SERVICE.toCode()){
					TCsqService csqService = csqServiceDao.selectByPrimaryKey(toId);
					if(csqService != null) {
						name = csqService.getName();
					}
				} else {
					TCsqUser csqUser = csqUserDao.selectByPrimaryKey(toId);
					if(csqUser != null) {
						name = csqUser.getName() + "的爱心账户";
					}
				}
				a.setToName(name);
				return a;
			}).collect(Collectors.toList());
		Map<String, List<TCsqOrder>> orderNoOrderMap = orders.stream().collect(Collectors.groupingBy(TCsqOrder::getOrderNo));

		resultList = resultList.stream()
			.map(a -> {
				String orderNos1 = a.getOrderNos();
				List<String> orderNos = Arrays.asList(orderNos1.contains(",")? orderNos1.split(","): new String[]{orderNos1});
				String toIds = "";
				String toNames = "";
				Long firstToId = null;
				int cnt = 0;
				for(String orderNo: orderNos) {
					List<TCsqOrder> orders1 = orderNoOrderMap.get(orderNo);
					if(orders1 != null) {
	//					TCsqOrder tCsqOrder = theOrders.get(0);
						TCsqOrder tCsqOrder = orders1.get(0);
						Long toId = tCsqOrder.getToId();
						if(cnt == 0) {
							firstToId = toId;
						}
						String toName = tCsqOrder.getToName();
						toIds = toIds.contains(toId.toString())? toIds: toIds + toId + ",";
						toNames = toNames.contains(toName)? toNames: toNames + toName + ",";
//						a.setDateString(DateUtil.timeStamp2Date(tCsqOrder.getCreateTime().getTime()));
						cnt ++;
					}
				}
				if(toIds.endsWith(",")) toIds = toIds.substring(0, toIds.length()-1);
				if(toNames.endsWith(",")) toNames = toNames.substring(0, toNames.length()-1);
				a.setToId(firstToId);
				a.setToIds(toIds);
				a.setToName(toNames);
				a.setDateString(DateUtil.timeStamp2Date(a.getCreateTime().getTime()));

				return a;
			}).collect(Collectors.toList());
		List<Long> userIds = resultList.stream()
			.map(CsqUserInvoiceVo::getUserId).collect(Collectors.toList());
		List<TCsqUser> tCsqUsers = csqUserDao.selectInIds(userIds);
		Map<Long, List<TCsqUser>> idUserMap = tCsqUsers.stream().collect(Collectors.groupingBy(TCsqUser::getId));
		resultList = resultList.stream()
			.map(a -> {
				List<TCsqUser> tCsqUsers1 = idUserMap.get(a.getUserId());
				if(tCsqUsers1 != null) {
					a.setUserName(tCsqUsers1.get(0).getName());
				}
				return a;
			}).collect(Collectors.toList());

		QueryResult<CsqUserInvoiceVo> csqUserInvoiceVoQueryResult = new QueryResult<>();
		csqUserInvoiceVoQueryResult.setResultList(resultList);
		csqUserInvoiceVoQueryResult.setTotalCount(total);

		map.put("queryResult", csqUserInvoiceVoQueryResult);

		return map;
	}

	@Override
	public void modify(TCsqUserInvoice obj) {
		obj.setUserId(null);
		csqUserInvoiceDao.update(obj);
	}

	private String getItemName(Map<Long, List<TCsqService>> serviceMap, Map<Long, List<TCsqFund>> fundMap, Map<Long, List<TCsqUser>> userMap, TCsqOrder a) {
		Integer toType = a.getToType();
		Long toId = a.getToId();
		String name = "";
		//类型判断
		switch (CsqEntityTypeEnum.getEnum(toType)) {
			case TYPE_FUND:
				List<TCsqFund> fundList = fundMap.get(toId);
				if (fundList != null) {
					TCsqFund fund = fundList.get(0);
					name = fund.getName();
					name = StringUtil.isEmpty(name)? "我的未命名的基金" : name;
				}
				break;
			case  TYPE_SERVICE:
				List<TCsqService> tCsqServices1 = serviceMap.get(toId);
				if (tCsqServices1 != null) {
					TCsqService csqService = tCsqServices1.get(0);
					name = csqService.getName();
				}
				break;
			case TYPE_ACCOUNT:
				List<TCsqUser> csqUsers = userMap.get(toId);
				if (csqUsers != null) {
					TCsqUser csqUser = csqUsers.get(0);
					String myName = csqUser.getName();
					name = new StringBuilder().append(
//						myName == null?
							"我"
//							: myName
					).append("的爱心账户").toString();
				}
				break;
		}
		return name;
	}

}
