package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqEntityTypeEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqInvoiceEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.vo.CsqWaitToInvoiceOrderVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserInvoiceVo;
import com.e_commerce.miscroservice.csq_proj.service.CsqInvoiceService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqInvoiceRecord;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

		csqOrderDao.update(toUpdateList);
	}
	
	@Override
	public QueryResult<CsqWaitToInvoiceOrderVo> waitToList(Long userId, Integer pageNum, Integer pageSize) {
		pageNum = pageNum==null? 1: pageNum;
		pageSize = pageSize==null? 0: pageSize;
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		//查询所有待开票的订单,找到serivce汇总
//		List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndFromTypeAndToTypeInvoiceStatusAndStatusDesc(userId, CsqEntityTypeEnum.TYPE_HUMAN.toCode(), CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqOrderEnum.INVOICE_STATUS_NO.getCode(), CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		//TODO 处理 爱心账户、基金、项目的数据，其中基金和项目要获取到名字
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndFromTypeAndInvoiceStatusAndStatusDesc(userId, CsqEntityTypeEnum.TYPE_HUMAN.toCode(), CsqOrderEnum.INVOICE_STATUS_NO.getCode(), CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
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
					return csqOrderVo;
				}
			).collect(Collectors.toList());
		QueryResult<CsqWaitToInvoiceOrderVo> queryResult = new QueryResult<>();
		queryResult.setResultList(resultList);
		queryResult.setTotalCount(startPage.getTotal());
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
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserInvoice> tCsqUserInvoices = csqUserInvoiceDao.selectByUserIdDesc(userId);
		List<CsqUserInvoiceVo> copyList = tCsqUserInvoices.stream()
			.map(a -> {
				String orderNos = a.getOrderNos();
				int recordCnt = orderNos.split(",").length;
				a.setRecordCnt(recordCnt);
				a.setDateString(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));
				return a.copyCsqUserInvoice();
			}).collect(Collectors.toList());
		QueryResult<CsqUserInvoiceVo> queryResult = new QueryResult<>();
		queryResult.setResultList(copyList);
		queryResult.setTotalCount(startPage.getTotal());
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
		return tCsqUserInvoice.copyCsqUserInvoice();
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
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectInOrderNos(split);

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
		queryResult.setTotalCount(startPage.getTotal());
		return queryResult;
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
					name = new StringBuilder().append(myName == null? "我": myName).append("的爱心账户").toString();
				}
				break;
		}
		return name;
	}

}
