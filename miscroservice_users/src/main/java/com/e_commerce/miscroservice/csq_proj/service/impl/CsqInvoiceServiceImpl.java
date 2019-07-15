package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqEntityTypeEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqInvoiceEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.vo.CsqWaitToInvoiceOrderVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserInvoiceVo;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserInvoiceDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserInvoice;
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
		if (StringUtil.isAnyEmpty(name, addr, person, telephone, taxNo) || Objects.isNull(type)) {
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
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndFromTypeAndToTypeInvoiceStatusAndStatusDesc(userId, CsqEntityTypeEnum.TYPE_HUMAN.toCode(), CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqOrderEnum.INVOICE_STATUS_NO.getCode(), CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		List<Long> serviceIds = tCsqOrders.stream()
			.map(TCsqOrder::getToId)
			.collect(Collectors.toList());
		List<TCsqService> tCsqServices = tCsqOrders.isEmpty()? new ArrayList<>() : csqServiceDao.selectInIds(serviceIds);
		Map<Long, List<TCsqService>> collect = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));

		List<CsqWaitToInvoiceOrderVo> resultList = tCsqOrders.stream()
			.map(a -> {
					CsqWaitToInvoiceOrderVo csqOrderVo = a.copyCsqOrderVo();    //包含了金额、orderNo
					Long serviceId = a.getToId();
					List<TCsqService> tCsqServices1 = collect.get(serviceId);
					if (tCsqServices1 != null) {
						TCsqService csqService = tCsqServices1.get(0);
						csqOrderVo.setServiceName(csqService.getName());    //项目名
						csqOrderVo.setDate(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));    //日期
					}
					return csqOrderVo;
				}
			).collect(Collectors.toList());
		QueryResult<CsqWaitToInvoiceOrderVo> queryResult = new QueryResult<>();
		queryResult.setResultList(resultList);
		queryResult.setTotalCount(startPage.getTotal());
		return queryResult;
	}

	@Override
	public QueryResult<CsqUserInvoiceVo> doneList(Long userId, Integer pageNum, Integer pageSize) {
		pageNum = pageNum==null? 1:pageNum;
		pageSize = pageSize==null? 0:pageSize;
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserInvoice> tCsqUserInvoices = csqUserInvoiceDao.selectByUserId(userId);
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
		tCsqUserInvoice.setRecordCnt(tCsqUserInvoice.getOrderNos().length());
		return tCsqUserInvoice.copyCsqUserInvoice();
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

		List<Long> serviceIds = tCsqOrders.stream()
			.map(TCsqOrder::getToId).collect(Collectors.toList());
		List<TCsqService> tCsqServices = csqServiceDao.selectInIds(serviceIds);
		Map<Long, List<TCsqService>> serviceMap = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));

		ArrayList<CsqInvoiceRecord> csqInvoiceList = new ArrayList<>();
		tCsqOrders.stream()
			.forEach(a -> {
				List<TCsqService> csqServices = serviceMap.get(a.getToId());
				TCsqService csqService = csqServices.get(0);
				CsqInvoiceRecord vo = new CsqInvoiceRecord();
				vo.setItemId(a.getToId());
				vo.setItemType(a.getToType());
				vo.setOrderNo(a.getOrderNo());
				vo.setDateString(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));
				vo.setMyAmount(a.getPrice());
				vo.setName(csqService.getName());
				csqInvoiceList.add(vo);
			});
		QueryResult<CsqInvoiceRecord> queryResult = new QueryResult<>();
		queryResult.setResultList(csqInvoiceList);
		queryResult.setTotalCount(startPage.getTotal());
		return queryResult;
	}

}
