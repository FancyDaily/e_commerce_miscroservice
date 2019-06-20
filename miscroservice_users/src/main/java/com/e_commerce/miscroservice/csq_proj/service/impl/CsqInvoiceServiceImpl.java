package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CSqUserPaymentEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqInvoiceEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqOrderEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserInvoiceDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserInvoice;
import com.e_commerce.miscroservice.csq_proj.service.CsqInvoiceService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqInvoiceVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 14:42
 */
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
		//检查订单状态
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectInOrderNos(orderNo);
		tCsqOrders.stream()
			.forEach((a) -> {
				Integer status = a.getStatus();
				if (CsqOrderEnum.STATUS_ALREADY_PAY.getCode() != status) {    //如果不是已支付
					throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该订单未成功支付，无法开票!");
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
		Integer taxNo = userInvoice.getTaxNo();
		String addr = userInvoice.getAddr();
		String person = userInvoice.getPerson();
		String telephone = userInvoice.getTelephone();
		if (StringUtil.isAnyEmpty(name, addr, person, telephone) || Objects.isNull(type) || Objects.isNull(taxNo)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "必要参数为空!");
		}

		String orderNos = Arrays.stream(orderNo)
			.reduce("", (a, b) -> a + "," + b);
		orderNos = orderNos.startsWith(",")? orderNos.substring(1):orderNos;
		userInvoice.setOrderNos(orderNos);
		userInvoice.setAmount(totalFee);
		csqUserInvoiceDao.insert(userInvoice);

		//修改订单的开票状态
		List<TCsqOrder> toUpdateList = tCsqOrders.stream().map(a -> {
			a.setStatus(CsqOrderEnum.INVOICE_STATUS_YES.getCode());
			return a;
		}).collect(Collectors.toList());

		csqOrderDao.update(toUpdateList);
	}
	
	@Override
	public QueryResult<CsqInvoiceVo> waitToList(Long userId, Integer pageNum, Integer pageSize) {
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		//查询所有代开票的订单,找到serivce汇总
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndFromTypeAndToTypeInvoiceStatusDesc(userId, CSqUserPaymentEnum.TYPE_HUMAN.toCode(), CSqUserPaymentEnum.TYPE_SERVICE.toCode(), CsqOrderEnum.INVOICE_STATUS_NO.getCode());
		List<Long> serviceIds = tCsqOrders.stream()
			.map(TCsqOrder::getToId)
			.collect(Collectors.toList());
		List<TCsqService> tCsqServices = csqServiceDao.selectInIds(serviceIds);
		Map<Long, List<TCsqService>> collect = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));

		List<CsqInvoiceVo> reusltList = tCsqOrders.stream()
			.map(a -> {
					String orderNo = a.getOrderNo();
					Double myAmount = a.getPrice();
					Long serviceId = a.getToId();
					List<TCsqService> tCsqServices1 = collect.get(serviceId);
					CsqInvoiceVo csqInvoiceVo = null;
					if (tCsqServices1 != null) {
						TCsqService csqService = tCsqServices1.get(0);
						csqInvoiceVo = csqService.copyCsqInvoiceVo();
						csqInvoiceVo.setMyAmount(myAmount);
						csqInvoiceVo.setOrderNo(orderNo);
						csqInvoiceVo.setDateString(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));
					}
					return csqInvoiceVo;
				}
			).collect(Collectors.toList());
		QueryResult<CsqInvoiceVo> queryResult = new QueryResult<>();
		queryResult.setResultList(reusltList);
		queryResult.setTotalCount(startPage.getTotal());
		return queryResult;
	}

	@Override
	public QueryResult<TCsqUserInvoice> doneList(Long userId, Integer pageNum, Integer pageSize) {
		pageNum = pageNum==null? 1:pageNum;
		pageSize = pageSize==null? 0:pageSize;
		Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserInvoice> tCsqUserInvoices = csqUserInvoiceDao.selectByUserId(userId);
		tCsqUserInvoices.stream()
			.map(a -> {
				a.setDateString(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));
				return a;
			}).collect(Collectors.toList());
		QueryResult<TCsqUserInvoice> queryResult = new QueryResult<>();
		queryResult.setResultList(tCsqUserInvoices);
		queryResult.setTotalCount(startPage.getTotal());
		return queryResult;
	}

	@Override
	public TCsqUserInvoice invoiceDetail(Long userId, Long invoiceId) {
		TCsqUserInvoice tCsqUserInvoice = csqUserInvoiceDao.selectByPrimaryKey(invoiceId);
		tCsqUserInvoice.setRecordCnt(tCsqUserInvoice.getOrderNos().length());
		return tCsqUserInvoice;
	}

	@Override
	public QueryResult<CsqInvoiceVo> recordList(Long userId, Long invoiceId, Integer pageNum, Integer pageSize) {
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

		ArrayList<CsqInvoiceVo> csqInvoiceList = new ArrayList<>();
		tCsqOrders.stream()
			.forEach(a -> {
				List<TCsqService> csqServices = serviceMap.get(a.getToId());
				TCsqService csqService = csqServices.get(0);
				CsqInvoiceVo vo = new CsqInvoiceVo();
				vo.setOrderNo(a.getOrderNo());
				vo.setDateString(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy/MM/dd"));
				vo.setMyAmount(a.getPrice());
				vo.setName(csqService.getName());
				csqInvoiceList.add(vo);
			});
		QueryResult<CsqInvoiceVo> queryResult = new QueryResult<>();
		queryResult.setResultList(csqInvoiceList);
		queryResult.setTotalCount(startPage.getTotal());
		return queryResult;
	}

}
