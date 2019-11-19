package com.e_commerce.miscroservice.sdx_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqPayService;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookOrderDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxShippingAddressDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxUserDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookOrderEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShippingAddressPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookInfoService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookOrderService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxScoreRecordService;
import com.e_commerce.miscroservice.sdx_proj.vo.*;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单的service层
 */

@Service
@Log
public class SdxBookOrderServiceImpl implements SdxBookOrderService {
	private final long ERROR_LONG = 0L;
	private final int ERROR_INT = 0;
	@Autowired
	private SdxBookOrderDao sdxBookOrderDao;
	@Autowired
	private SdxUserDao sdxUserDao;
	@Autowired
	private CsqPayService csqPayService;
	@Autowired
	private SdxBookService sdxBookService;
	@Autowired
	private SdxScoreRecordService sdxScoreRecordService;
	@Autowired
	private SdxBookDao sdxbookDao;
	@Autowired
	SdxShippingAddressDao sdxShippingAddressDao;
	@Autowired
	SdxBookInfoService sdxBookInfoService;

	@Override
	public long modTSdxBookOrder(TSdxBookOrderPo tSdxBookOrderPo) {
		if (tSdxBookOrderPo == null) {
			log.warn("操作订单参数为空");
			return ERROR_LONG;
		}
		Long id;
		if ((id=tSdxBookOrderPo.getId()) == null) {
			log.info("start添加订单={}", tSdxBookOrderPo);
			int result = sdxBookOrderDao.saveTSdxBookOrderIfNotExist(tSdxBookOrderPo);
			return result != 0 ? tSdxBookOrderPo.getId() : ERROR_LONG;
		} else {
			log.info("start修改订单={}", tSdxBookOrderPo.getId());
			String expressNo = tSdxBookOrderPo.getExpressNo();
			//如果是添加快递单号 -> 状态变成已发货
			TSdxBookOrderPo orderPo = sdxBookOrderDao.selectByPrimaryKey(id);
			Integer originStatus = orderPo.getStatus();
			TSdxBookOrderPo toUpdater = TSdxBookOrderPo.builder().build();
			toUpdater.setId(id);
			if(SdxBookOrderEnum.STATUS_INITAIL.getCode() == originStatus && !StringUtil.isEmpty(expressNo)) {
				toUpdater.setStatus(SdxBookOrderEnum.STATUS_PROCESSING.getCode());	//订单进行中
			}
			return sdxBookOrderDao.modTSdxBookOrder(tSdxBookOrderPo);
		}
	}

	@Override
	public int delTSdxBookOrderByIds(Long... ids) {
		if (ids == null || ids.length == 0) {
			log.warn("删除订单,ids集合为空");
			return ERROR_INT;
		}
		log.warn("start删除Id集合={},订单", Arrays.asList(ids));
		return sdxBookOrderDao.delTSdxBookOrderByIds(ids);
	}

	@Override
	public TSdxBookOrderVo findTSdxBookOrderById(Long id) {
		if (id == null || id <= 0L) {
			log.warn("根据Id查找订单,所传Id不符合规范");
			return new TSdxBookOrderVo();
		}
		log.info("start根据Id={}查找订单", id);
		TSdxBookOrderPo tSdxBookOrderPo = sdxBookOrderDao.findTSdxBookOrderById(id);
		return tSdxBookOrderPo == null ? new TSdxBookOrderVo() : tSdxBookOrderPo.copyTSdxBookOrderVo();
	}

	@Override
	public List<TSdxBookOrderVo> findTSdxBookOrderByAll(TSdxBookOrderPo tSdxBookOrderPo, Integer page, Integer size) {
		List<TSdxBookOrderVo> tSdxBookOrderVos = new ArrayList<>();
		if (tSdxBookOrderPo == null) {
			log.warn("根据条件查找订单,参数不对");
			return tSdxBookOrderVos;
		}
		log.info("start根据条件查找订单={}", tSdxBookOrderPo);
		List<TSdxBookOrderPo> tSdxBookOrderPos = sdxBookOrderDao.findTSdxBookOrderByAll(tSdxBookOrderPo, page, size);
		for (TSdxBookOrderPo po : tSdxBookOrderPos) {
			tSdxBookOrderVos.add(po.copyTSdxBookOrderVo());
		}
		return tSdxBookOrderVos;
	}

	/**
	 * 捐书订单列表
	 *
	 * @return
	 */
	@Override
	public List<TSdxBookDonateOrderVo> donateOrdersList() {
//		sdxBookOrderDao.selectByType();
		return null;
	}

	@Override
	public List<SdxBookOrderUserInfoVo> getEverDonateList(Long id) {
		//筛选已完成的 捐献 订单
		List<TSdxBookOrderPo> donateOrders = sdxBookOrderDao.selectByBookInfoIdAndTypeAndStatus(id, SdxBookOrderEnum.TYPE_DONATE.getCode(), SdxBookOrderEnum.STATUS_DONE.getCode());
		if (donateOrders.isEmpty()) return new ArrayList<>();
		List<Long> userIds = donateOrders.stream()
			.map(TSdxBookOrderPo::getUserId).collect(Collectors.toList());
		Map<Long, List<TCsqUser>> idUserMap = sdxUserDao.groupingByIdInIds(userIds);

		List<SdxBookOrderUserInfoVo> todoList = donateOrders.stream()
			.map(a -> {
				//提取捐助人编号，获取捐助人信息
				Long userId = a.getUserId();
				String name = "";
				String headPic = "";
				List<TCsqUser> users = idUserMap.get(userId);
				if (users != null) {
					TCsqUser csqUser = users.get(0);
					name = csqUser.getName();
					headPic = csqUser.getUserHeadPortraitPath();
				}
				long timeStamp = a.getCreateTime().getTime();
				String desc = buildDoneTimeDesc(timeStamp);
				return SdxBookOrderUserInfoVo.builder()
					.doneTimeDesc(desc)
					.name(name)
					.timeStamp(timeStamp)
					.headPic(headPic).build();
			}).collect(Collectors.toList());

		//倒序
		return todoList.stream()
			.sorted(Comparator.comparing(SdxBookOrderUserInfoVo::getTimeStamp).reversed()).collect(Collectors.toList());
	}

	@Override
	public String buildDoneTimeDesc(long timeStamp) {
		long currentInterval = System.currentTimeMillis() - timeStamp;
		boolean isToday = currentInterval < DateUtil.interval;
		boolean isYesterDay = !isToday && currentInterval < 2 * DateUtil.interval;
		//组装售出时间信息 分钟前、日期(如果为昨天，显示昨天）
		String desc;
		desc = isYesterDay ? "昨天" : isToday ? DateUtil.getMinsStrWithinOneDay(currentInterval) : DateUtil.timeStamp2Date(timeStamp);
		return desc;
	}

	@Override
	public Map<String, String> preOrder(Long shippingAddressId, String bookInfoIds, Double bookFee, Long userId, HttpServletRequest httpservletRequest, Double shipFee, Integer scoreUsed) throws Exception {
		//根据书籍信息编号，获取书籍， 判断 -> 可用
		String bookIds = getBookIds(bookInfoIds);

		//构建一张完整的订单
		String orderNo = dealWithPreOrder(shippingAddressId, bookInfoIds, bookIds, bookFee, userId, shipFee, scoreUsed);
		String attach = "book";
		//发起微信支付，返回调用参数
//		csqPayService.buildWebParam(userId, orderNo, attach, fee, httpservletRequest, false, null);
		return buildWebParam(userId, orderNo, attach, bookFee, httpservletRequest);
	}

	@Override
	public void dealWithBookPay(String out_trade_no, String attach) {
		TSdxBookOrderPo tSdxBookOrderPo = checkAttach(out_trade_no, attach);
		afterPaySuccess(tSdxBookOrderPo);
	}

	@Override
	public void confirmReceipt(Long orderId) {
		TSdxBookOrderPo tSdxBookOrderPo = sdxBookOrderDao.selectByPrimaryKey(orderId);
		checkExist(tSdxBookOrderPo);
		//订单状态改变
		tSdxBookOrderPo.setStatus(SdxBookOrderEnum.STATUS_DONE.getCode());
		sdxBookOrderDao.modTSdxBookOrder(tSdxBookOrderPo);
	}

	private void checkExist(TSdxBookOrderPo tSdxBookOrderPo) {
		if(tSdxBookOrderPo == null) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "订单不存在!");
	}

	@Override
	public SdxPurchaseOrderVo detail(Long orderId) {
		//查找收货人信息，补全
		TSdxBookOrderPo tSdxBookOrderPo = sdxBookOrderDao.selectByPrimaryKey(orderId);
		checkExist(tSdxBookOrderPo);
		SdxPurchaseOrderVo vo = tSdxBookOrderPo.copySdxPurchaseOrderVo();
		Long shippingAddressId = tSdxBookOrderPo.getShippingAddressId();
		if(shippingAddressId != null) {	//自送或没有邮寄地址,则dismiss
			TSdxShippingAddressPo tSdxShippingAddressPo = sdxShippingAddressDao.selectByPrimaryKey(shippingAddressId);
			vo.setAddress(tSdxShippingAddressPo);
		}
		return vo;
	}

	@Override
	public QueryResult purchaseList(Long userIds, Integer option, Integer pageNum, Integer pageSize) {
		List<TSdxBookOrderPo> list = sdxBookOrderDao.purchaseList(userIds, option, pageNum, pageSize);
		List<SdxPurchaseOrderVo> vos = list.stream()
			.map(TSdxBookOrderPo::copySdxPurchaseOrderVo).collect(Collectors.toList());
		return PageUtil.buildQueryResult(vos);
	}

	@Override
	public QueryResult donateList(Long userIds, Integer option, Integer pageNum, Integer pageSize) {
		List<TSdxBookOrderPo> tSdxBookOrderPos = sdxBookOrderDao.donateList(userIds, option, pageNum, pageSize);
		//装载
		List<SdxDonateOrderVo> vos = tSdxBookOrderPos.stream()
			.map(a -> {
				SdxDonateOrderVo vo = a.copySdxDonateOrderVo();
				//订单解构
				String bookIds = vo.getBookIds();
				String[] bookIdArray = StringUtil.splitString(bookIds, ",");
				List<TSdxBookPo> bookPos = sdxbookDao.selectInIds(Arrays.stream(bookIdArray).map(Long::valueOf).collect(Collectors.toList()));
				vo.setBooks(bookPos);
//				Long bookInfoIds = vo.getBookInfoIds();
				return vo;
			}).collect(Collectors.toList());
		//按日期分组 TODO
		return PageUtil.buildQueryResult(vos);
	}

	@Override
	public String createDonateOrder(Long userId, Long[] bookInfoIds, Integer shipType, Long shippingAddressId, Long bookStationId, Long serviceId) {
		//将给定的书籍信息 -> 生成出book实体，重复的infoIds数量决定了同infoId的book数量
		Map<Long, Integer> idExpectedScoresMap = sdxBookService.getIdExpectedScoresMap(bookInfoIds);
		List<TSdxBookPo> bookList = new ArrayList<>();
		Integer totalExpectedScores = 0;
		for(Long bookInfoId: bookInfoIds) {
			//获取预估积分
			Integer expectedScores = idExpectedScoresMap.get(bookInfoId);
			totalExpectedScores += expectedScores;
			//生成book实体
			TSdxBookPo build = TSdxBookPo.builder()
				.status(SdxBookEnum.STATUS_INITIAL.getCode())
				.bookInfoId(bookInfoId)
				.currentOwnerId(userId)    //当前拥有者 -> 捐助过程中填写捐助人( 或者应当不填
				.donaterId(userId)
				.expectedScore(expectedScores)    //预估积分
				.serviceId(serviceId)
				.build();
			sdxbookDao.saveTSdxBookIfNotExist(build);
			//用bookList替代 bookInfoIds
			bookList.add(build);
		}
		//创建订单
		String orderNo = UUIdUtil.generateOrderNo();
		TSdxBookOrderPo build = TSdxBookOrderPo.builder()
			.type(SdxBookOrderEnum.TYPE_DONATE.getCode())
//			.expressNo()	//运单号, 填写之后(订单由初始 -> 途中(配送或者自送))
			.orderNo(orderNo)        //订单号
			.bookIds(StringUtil.longListToString(Arrays.asList(bookInfoIds)))        //书本编号
			.bookIfIs(StringUtil.longListToString(bookList.stream().map(TSdxBookPo::getId).collect(Collectors.toList())))        //书本信息编号
			.userId(userId)
			.status(SdxBookOrderEnum.SHIP_TYPE_MAILING.getCode() == shipType? SdxBookOrderEnum.STATUS_INITAIL.getCode() : SdxBookOrderEnum.STATUS_PROCESSING.getCode())    //针对配送方式不同给予不同的状态,eg.自送类型时创建订单即在途中
			.shippingAddressId(shippingAddressId)
			.expectedTotalScores(totalExpectedScores)
//			.exactTotalScores()	//实际获得积分
			.shipType(shipType)
			.bookStationId(bookStationId)
			.build();
		sdxBookOrderDao.saveTSdxBookOrderIfNotExist(build);
		//返回订单号orderNo
		return orderNo;
	}

	@Override
	public void cancel(Long orderId) {
		TSdxBookOrderPo po = sdxBookOrderDao.selectByPrimaryKey(orderId);
		String bookIds = po.getBookIds();
		List<String> bookIdList = Arrays.asList(bookIds.split(","));
		//处理书本状态
		List<TSdxBookPo> toUpdater = bookIdList.stream()
			.map(Long::valueOf)
			.map(a -> {
				TSdxBookPo build = TSdxBookPo.builder()
					.status(SdxBookEnum.STATUS_CANCLE.getCode())    //STATUS REMARK
					.build();
				build.setId(a);
				return build;
			}).collect(Collectors.toList());
		sdxbookDao.update(toUpdater);

		//处理订单状态
		TSdxBookOrderPo build = TSdxBookOrderPo.builder()
			.status(SdxBookOrderEnum.STATUS_CANCLE.getCode())
			.build();
		sdxBookOrderDao.modTSdxBookOrder(build);
	}

	private void afterPaySuccess(TSdxBookOrderPo order) {
		//积分减少，插入流水
		sdxScoreRecordService.dealWithScoreOut(order);
		//订单状态变化
		order.setStatus(SdxBookOrderEnum.STATUS_PROCESSING.getCode());	//订单进行中
		sdxBookOrderDao.modTSdxBookOrder(order);
		//书本变为售出中
		String bookIdStr = order.getBookIds();
		List<String> strings = StringUtil.splitStringToList(bookIdStr, ",");
		List<Long> bookIds = strings.stream()
			.map(Long::valueOf).collect(Collectors.toList());
		List<TSdxBookPo> sdxBookPos = sdxbookDao.selectInIds(bookIds);
		List<TSdxBookPo> toUpdaters = sdxBookPos.stream().map(a -> {
			TSdxBookPo build = TSdxBookPo.builder()
				.status(SdxBookEnum.STATUS_BOUGHT.getCode())
				.build();
			build.setId(a.getId());
			return build;
		}).collect(Collectors.toList());
		sdxbookDao.update(toUpdaters);
	}

	public static void main(String[] args) {
		String bookIds = "12";
		String[] strings = StringUtil.splitString(bookIds, ",");
		System.out.println(Arrays.asList(strings));
	}

	private TSdxBookOrderPo checkAttach(String out_trade_no, String attach) {
		TSdxBookOrderPo tSdxBookOrderPo = sdxBookOrderDao.selectByOrderNo(out_trade_no);
		if(attach == null || tSdxBookOrderPo == null || !tSdxBookOrderPo.getOrderNo().equals(out_trade_no)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "类型不正确!");
		}
		return tSdxBookOrderPo;
	}

	private String getBookIds(String bookInfoIds) {
		//获取书籍编号
		List<String> bookInfos = Arrays.asList(bookInfoIds.contains(",")? bookInfoIds.split(",") : new String[]{bookInfoIds});

		List<Long> bookIdList = new ArrayList<>();
		for(String bookInfo: bookInfos) {
			Long bookInfoId = Long.valueOf(bookInfo);
			List<TSdxBookPo> books = sdxBookService.getAvailableBooks(bookInfoId);
			if(books.isEmpty()) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "部分商品的余量已不足！请退出查看!");
			bookIdList.add(books.get(0).getId());
		}
		//拼接成String
		return StringUtil.longListToString(bookIdList);
	}

	private void checkBookSurplusAmount(String bookInfoIds) {
		List<String> bookInfos = Arrays.asList(bookInfoIds.contains(",")? bookInfoIds.split(",") : new String[]{bookInfoIds});

		for(String bookInfo: bookInfos) {
			Long bookInfoId = Long.valueOf(bookInfo);
			List<TSdxBookPo> books = sdxBookService.getAvailableBooks(bookInfoId);
			if(books.isEmpty()) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "部分商品的余量已不足！请退出查看!");
		}
	}

	private String dealWithPreOrder(Long shippingAddressId, String bookInfoIds, String bookIds, Double bookFee, Long userId, Double shipFee, Integer scoreUsed) {
		String orderNo;
		orderNo = UUIdUtil.generateOrderNo();    //默认生成新的订单号
		//查看对于同一用户同一业务(entityId、entityType、fee相同)	是否有可复用的订单
		TSdxBookOrderPo sdxOrder = findSameUnpayOrder(shippingAddressId, bookInfoIds, bookFee, userId, shipFee);
		if (sdxOrder != null) {
			boolean expired = System.currentTimeMillis() - sdxOrder.getCreateTime().getTime() > 1000L * 60 * 30;
			if (!expired) {    //复用
				orderNo = sdxOrder.getOrderNo();
				return orderNo;
			}
		}
		bookFee = bookFee == null? 0: bookFee;
		shipFee = shipFee == null? 0: shipFee;
		TSdxBookOrderPo build = TSdxBookOrderPo.builder()
			.type(SdxBookOrderEnum.TYPE_PURCHASE.getCode())
			.shipType(SdxBookOrderEnum.SHIP_TYPE_MAILING.getCode())
			.shippingAddressId(shippingAddressId)
			.userId(userId)
			.bookIds(bookIds)
			.bookIfIs(bookInfoIds)
			.bookPrice(bookFee)    //书本总价
			.shipPirce(shipFee)    //邮费
			.scoreDiscount(scoreUsed)    //抵扣积分总额
			.price(bookFee + shipFee)        //订单总价
			.status(SdxBookOrderEnum.STATUS_UNPAY.getCode())
			.build();
		sdxBookOrderDao.saveTSdxBookOrderIfNotExist(build);
		return orderNo;
	}

	private TSdxBookOrderPo findSameUnpayOrder(Long shippingAddressId, String bookInfoIds, Double bookFee, Long userId, Double shipFee) {
		return sdxBookOrderDao.selectByShippingAddressIdAndBookInfoIdsAndBookFeeAndUserIdAndShipFee(shippingAddressId, bookInfoIds, bookFee, userId, shipFee, SdxBookOrderEnum.STATUS_INITAIL.getCode(), SdxBookOrderEnum.TYPE_PURCHASE.getCode());
	}

	private Map<String, String> buildWebParam(Long userId, String orderNo, String attach, Double fee, HttpServletRequest httpServletRequest) throws Exception {
		return csqPayService.buildWebParam(userId, orderNo, attach, fee, httpServletRequest, false, null);
	}

}
