package com.e_commerce.miscroservice.sdx_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.LimitQueue;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqSysMsgEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqMsgDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqSysMsg;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqPayService;
import com.e_commerce.miscroservice.sdx_proj.dao.*;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookOrderEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookTransRecordEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxRedisEnum;
import com.e_commerce.miscroservice.sdx_proj.po.*;
import com.e_commerce.miscroservice.sdx_proj.service.*;
import com.e_commerce.miscroservice.sdx_proj.vo.*;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BinaryOperator;
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
	private SdxBookTicktService sdxBookTicktService;
	@Autowired
	SdxShippingAddressDao sdxShippingAddressDao;
	@Autowired
	SdxBookInfoService sdxBookInfoService;
	@Autowired
	SdxBookTransRecordService sdxBookTransRecordService;
	@Autowired
	SdxBookTransRecordDao sdxBookTransRecordDao;
	@Autowired
	SdxShoppingTrolleysDao sdxShoppingTrolleysDao;
	@Autowired
	SdxBookInfoUserPreOrderDao sdxBookInfoUserPreOrderDao;
	@Autowired
	SdxBookInfoDao sdxBookInfoDao;
	@Autowired
	SdxUserService sdxUserService;
	@Autowired
	CsqMsgDao csqMsgDao;
	@Autowired
	@Qualifier("sdxRedisTemplate")
	private HashOperations<String, String, Object> userRedisTemplate;

	@Override
	public long modTSdxBookOrder(TSdxBookOrderPo tSdxBookOrderPo) {
		if (tSdxBookOrderPo == null) {
			log.warn("操作订单参数为空");
			return ERROR_LONG;
		}
		String orderNo = tSdxBookOrderPo.getOrderNo();
		if(!StringUtil.isEmpty(orderNo)) {
			TSdxBookOrderPo idContainer = sdxBookOrderDao.selectByOrderNo(orderNo);
			if (idContainer != null) {
				tSdxBookOrderPo.setId(idContainer.getId());
			}
		}
		Long id;
		if ((id=tSdxBookOrderPo.getId()) == null) {
			log.info("start添加订单={}", tSdxBookOrderPo);
			tSdxBookOrderPo.setUserId(IdUtil.getId());
			int result = sdxBookOrderDao.saveTSdxBookOrderIfNotExist(tSdxBookOrderPo);
			return result != 0 ? tSdxBookOrderPo.getId() : ERROR_LONG;
		} else {
			log.info("start修改订单={}", tSdxBookOrderPo.getId());
			String expressNo = tSdxBookOrderPo.getExpressNo();
			//如果是添加快递单号 -> 状态变成已发货
			TSdxBookOrderPo orderPo = sdxBookOrderDao.selectByPrimaryKey(id);
			Integer originStatus = orderPo.getStatus();
			Integer status = tSdxBookOrderPo.getStatus();
			//原订单
			Integer type = orderPo.getType();
			String bookIds = orderPo.getBookIds();
			String bookInfos = orderPo.getBookIfIs();
			Long userId = IdUtil.getId();
			tSdxBookOrderPo.setId(id);
			if(SdxBookOrderEnum.STATUS_INITAIL.getCode() == originStatus && !StringUtil.isEmpty(expressNo)) {
				if(status == null) {
					tSdxBookOrderPo.setStatus(SdxBookOrderEnum.STATUS_PROCESSING.getCode());	//订单进行中
				}
			}

			//如果是确认收货 -> 类型 捐书 -> 书籍状态改变，书籍漂流记录添加, 返积分
			if(status != null &&SdxBookOrderEnum.STATUS_DONE.getCode() != originStatus && SdxBookOrderEnum.STATUS_DONE.getCode() == status) {
				if(SdxBookOrderEnum.TYPE_DONATE.getCode() == type) {
					List<Long>  bookIdList = StringUtil.splitToArray(bookIds);
					List<Long> bookInfoList = StringUtil.splitToArray(bookInfos);
					if(bookIdList.size() != bookInfoList.size()) {
						List<TSdxBookPo> tSdxBookPos = sdxbookDao.selectInIds(bookIdList);
						bookInfoList = tSdxBookPos.stream().map(TSdxBookPo::getBookInfoId).collect(Collectors.toList());
					}

					List<TSdxBookPo> tSdxBookPos = sdxbookDao.selectInIds(bookIdList);
					tSdxBookPos = tSdxBookPos.stream()
						.map(a -> {
							a.setStatus(SdxBookEnum.STATUS_ON_SHELF.getCode());	//上架
							return a;
						}).collect(Collectors.toList());
					//更新
					sdxbookDao.update(tSdxBookPos);
					// 书籍漂流记录 -> 第 1 位主人捐于 xxx ,类型
					List<TSdxBookTransRecordPo> toInserter = new ArrayList<>();
					for(int i=0; i < bookIdList.size(); i++) {
						Long bookId = bookIdList.get(i);
						Long bookInfoId = bookInfoList.get(i);
						//查询是第几任主人
						String description = "第%d位主人捐于" + DateUtil.timeStamp2Date(System.currentTimeMillis());	// 构建描述
						List<TSdxBookTransRecordPo> recordPos = sdxBookTransRecordDao.selectByBookIdAndType(bookId, SdxBookTransRecordEnum.TYPE_BECOME_OWNER.getCode());
						description = String.format(description, recordPos.size());

						TSdxBookTransRecordPo build = TSdxBookTransRecordPo.builder()
							.type(SdxBookTransRecordEnum.TYPE_BECOME_OWNER.getCode())
							.userId(userId)
							.bookId(bookId)
							.bookInfoId(bookInfoId)
							.description(description)
							.notes(null)
							.build();

						toInserter.add(build);
					}
					sdxBookTransRecordDao.save(toInserter);
					// 返还积分（根据约定的积分 -> 包括用户积分增加、流水插入
					sdxScoreRecordService.earnScores(orderPo.getUserId(), orderPo);
					// 书本上架
					sdxBookService.putOnShelf(orderPo.getBookIds());
					//获得书券
					sdxBookTicktService.earnTickt(StringUtil.splitToArray(orderPo.getBookIfIs()), orderPo.getUserId());

					//确认收货之后，捐款播报中加入捐助的记录
					String hashKey = SdxRedisEnum.ALL.getMsg();
					//获取队列
					LimitQueue<SdxGlobalDonateRecordVo> queue = (LimitQueue<SdxGlobalDonateRecordVo>) userRedisTemplate.get(SdxRedisEnum.CSQ_GLOBAL_DONATE_BROADCAST.getMsg(), hashKey);
					queue = queue == null? new LimitQueue<>(): queue;
					//由订单构建vo
					SdxGlobalDonateRecordVo globalDonateRecordVo = transToGlobalDonateRecordVo(orderPo);
					//将vo添加队列末尾
					//覆写
					queue.offer(globalDonateRecordVo);
					userRedisTemplate.put(SdxRedisEnum.CSQ_GLOBAL_DONATE_BROADCAST.getMsg(),  hashKey, queue);
				}
			}
			return sdxBookOrderDao.modTSdxBookOrder(tSdxBookOrderPo);
		}
	}

	private SdxGlobalDonateRecordVo transToGlobalDonateRecordVo(TSdxBookOrderPo orderPo) {
		String bookIfIs = orderPo.getBookIfIs();
		Long userId = orderPo.getUserId();
		TCsqUser csqUser = sdxUserDao.selectByPrimaryKey(userId);
		String donaterName = csqUser.getName();
		String headPic = csqUser.getUserHeadPortraitPath();
		List<TSdxBookInfoPo> bookInfoPos = sdxBookInfoDao.selectInIds(Arrays.stream(bookIfIs.split(",")).map(Long::valueOf).collect(Collectors.toList()));
		String bookInfoNames = bookInfoPos.stream()
			.map(TSdxBookInfoPo::getName).reduce("", bookNamesReducer(","));
		long timeStamp = orderPo.getCreateTime().getTime();
		String timeAgo = DateUtil.minutes2TimeAgo(DateUtil.timestamp2MinutesAgo(timeStamp));
		//构建描述
		StringBuilder builder = new StringBuilder();
		String donate = "捐赠";
		String description = builder.append(timeAgo).append(" ").append(donaterName).append(donate).append(buidBookStrs(bookInfoNames, Boolean.FALSE)).toString();

		return SdxGlobalDonateRecordVo.builder()
			.timeStamp(timeStamp)
			.timeAgo(timeAgo)
			.userId(userId)
			.donaterName(donaterName)
			.headPic(headPic)
			.bookInfoIds(bookIfIs)
			.bookInfoPos(bookInfoPos)
			.bookInfoNames(bookInfoNames)
			.description(description)
			.build();
	}

	@Override
	public String buidBookStrs(String bookInfoNames, Boolean isUseEllipsis) {
		String[] split = bookInfoNames.split(",");
		List<String> names = Arrays.stream(split)
			.map(a -> "《" + a + "》"
			).collect(Collectors.toList());
		if(!isUseEllipsis) return names.stream().reduce("", bookNamesReducer("、"));
		Integer ellipsisLimit = 2;
		//启用缩写 -> 两本书
		return names.stream()
			.limit(ellipsisLimit)
			.reduce("", bookNamesReducer("、")) + "等书";
	}

	private BinaryOperator<String> bookNamesReducer(String s) {
		return (a, b) -> a + s + b;
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
	public Map<String, String> preOrder(String orderNo, Long shippingAddressId, String bookInfoIds, Double bookFee, Long userId, HttpServletRequest httpservletRequest, Double shipFee, Integer scoreUsed) throws Exception {
		//根据书籍信息编号，获取书籍， 判断 -> 可用
		String bookIds = getBookIds(bookInfoIds);

		//构建一张完整的订单
		if(orderNo == null || sdxBookOrderDao.selectByOrderNo(orderNo) == null) {
			orderNo = dealWithPreOrder(shippingAddressId, bookInfoIds, bookIds, bookFee, userId, shipFee, scoreUsed);
		}
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
		//给这些书的上一任拥有者发送系统消息
		String bookIfIs = tSdxBookOrderPo.getBookIfIs();
		List<Long> bookInfoIds = StringUtil.splitToArray(bookIfIs);
		List<TCsqSysMsg> toAdder = new ArrayList<>();
		for(Long bookInfoId: bookInfoIds) {
			List<TSdxBookTransRecordPo> sdxBookTransRecordPos = sdxBookTransRecordDao.selectByBookInfoIdAndTypeDesc(bookInfoId, SdxBookTransRecordEnum.TYPE_BECOME_OWNER.getCode());
			int readerNo = sdxBookTransRecordPos.size();
			readerNo ++;
			TSdxBookTransRecordPo sdxBookTransRecordPo = sdxBookTransRecordPos.get(0);
			//userId
			Long userId = sdxBookTransRecordPo.getUserId();
			//sysMsg
			toAdder.add(TCsqSysMsg.builder()
				.type(CsqSysMsgEnum.TYPE_NORMAL.getCode())
				.isSdx(CsqSysMsgEnum.IS_SDX_TRUE.getCode())
				.serviceId(bookInfoId)
				.userId(userId)
				.title("您的书籍已经到了新用户手上")
				.content("您的书籍已经到了新用户手上")
				.readerNo(readerNo).build());
		}
		csqMsgDao.insert(toAdder);
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
		//今天的日期
		vo.setDate(DateUtil.timeStamp2Date(tSdxBookOrderPo.getCreateTime().getTime()));
		//积分抵扣
		vo.setScoreUsed(tSdxBookOrderPo.getScoreDiscount());
		//书本信息
		dealWithBookInfos(vo, tSdxBookOrderPo.getBookIfIs());
		return vo;
	}

	//查询全部
	@Override
	public QueryResult purchaseList(Long userIds, String option, Integer pageNum, Integer pageSize) {
		String[] split = option.split(",");
		List<Integer> options = Arrays.stream(split)
			.map(Integer::valueOf).collect(Collectors.toList());
		List<TSdxBookOrderPo> list = sdxBookOrderDao.purchaseList(userIds, options, pageNum, pageSize);
		List<SdxPurchaseOrderVo> vos = list.stream()
			.map(a -> {
				SdxPurchaseOrderVo vo = a.copySdxPurchaseOrderVo();
				//书本信息装载
				String bookIfIs = a.getBookIfIs();
				dealWithBookInfos(vo, bookIfIs);

				vo.setTimeStamp(a.getCreateTime().getTime());
				return vo;
			}).collect(Collectors.toList());
		//按日期分组
		groupingByDate(vos);

		return PageUtil.buildQueryResult(vos);
	}

	private void dealWithBookInfos(SdxPurchaseOrderVo vo, String bookIfIs) {
		if(!StringUtil.isEmpty(bookIfIs)) {
			List<Long> bookInfoIds = new ArrayList<>(StringUtil.splitToArray(bookIfIs));
			List<TSdxBookInfoPo> sdxBookInfoPos = sdxBookInfoDao.selectInIds(bookInfoIds);
			vo.setBookInfos(sdxBookInfoPos);
		}
	}

	private List<SdxPurchaseOrderDateGroupVo> groupingByDate(List<SdxPurchaseOrderVo> vos) {
		List<SdxPurchaseOrderDateGroupVo> purchaseOrderDateGroupVos = new ArrayList<>();
		vos.stream()
			.map(a -> {
				Long timeStamp = a.getTimeStamp();
				String wholeDate = DateUtil.timeStamp2Date(timeStamp);
				String monthDay = DateUtil.timeStamp2MonthDay(timeStamp);
				a.setWholeDate(wholeDate);
				a.setMonthDay(monthDay);
				return a;
			}).collect(Collectors.groupingBy(SdxPurchaseOrderVo::getWholeDate)).forEach((k,v) -> {
					SdxPurchaseOrderVo purchaseOrderVo = v.get(0);
					purchaseOrderDateGroupVos.add(SdxPurchaseOrderDateGroupVo.builder()
						.monthDay(purchaseOrderVo.getMonthDay())
						.wholeDate(k)
						.sdxPurchaseOrderVos(v)
						.build());
		});
		return purchaseOrderDateGroupVos;
	}

	@Override
	public QueryResult donateList(Long userIds, String option, Integer pageNum, Integer pageSize) {
		List<Integer> options = StringUtil.splitToArray(option).stream().map(Long::intValue).collect(Collectors.toList());
		List<TSdxBookOrderPo> tSdxBookOrderPos = sdxBookOrderDao.donateList(userIds, options, pageNum, pageSize);
		//装载
		List<SdxDonateOrderVo> vos = tSdxBookOrderPos.stream()
			.map(a -> {
				SdxDonateOrderVo vo = a.copySdxDonateOrderVo();
				//订单解构
				String bookIds = vo.getBookIds();
				String[] bookIdArray = StringUtil.splitString(bookIds, ",");
				List<TSdxBookPo> bookPos = sdxbookDao.selectInIds(Arrays.stream(bookIdArray).map(Long::valueOf).collect(Collectors.toList()));
				vo.setBooks(bookPos);
				String bookInfoIds = vo.getBookInfoIds();
				if(bookInfoIds != null) {
					String[] bookInfoIdArray = StringUtil.splitString(bookInfoIds, ",");
					List<Long> bookInfoIdList = Arrays.stream(bookInfoIdArray).map(Long::valueOf).collect(Collectors.toList());
					List<TSdxBookInfoPo> bookInfoPos = new ArrayList<>();
					for(Long bookInfoId: bookInfoIdList) {
						TSdxBookInfoPo bookInfoPo = sdxBookInfoDao.selectByPrimaryKey(bookInfoId);
						Double money = sdxBookService.dealWithScoreMoney(userIds, bookInfoPo.getPrice());
						bookInfoPo.setMaximumDiscount(money);
						bookInfoPos.add(bookInfoPo);
					}
					vo.setBookInfos(bookInfoPos);
				} else {
					List<Long> ids = bookPos.stream()
						.map(TSdxBookPo::getBookInfoId).collect(Collectors.toList());
					List<TSdxBookInfoPo> bookInfoPos = new ArrayList<>();
					for(Long bookInfoId: ids) {
						TSdxBookInfoPo bookInfoPo = sdxBookInfoDao.selectByPrimaryKey(bookInfoId);
//						Double money = sdxBookService.dealWithScoreMoney(userIds, bookInfoPo.getPrice());
						bookInfoPo.setMaximumDiscount(bookInfoPo.getPrice());
						bookInfoPos.add(bookInfoPo);
					}
					vo.setBookInfos(bookInfoPos);
				}
				return vo;
			}).collect(Collectors.toList());
		return PageUtil.buildQueryResult(vos);
	}

	@Override
	public String createDonateOrder(Long userId, Long[] bookInfoIds, Integer shipType, Long shippingAddressId, Long bookStationId, Long serviceId, Integer status) {
		//将给定的书籍信息 -> 生成出book实体，重复的infoIds数量决定了同infoId的book数量
		Map<Long, Integer> idExpectedScoresMap = sdxBookService.getIdExpectedScoresMap(bookInfoIds);
		List<TSdxBookPo> bookList = new ArrayList<>();
		Integer totalExpectedScores = 0;
		Double fee = bookInfoIds.length > 5? 0d: 5d;
		status = status == null? SdxBookOrderEnum.SHIP_TYPE_MAILING.getCode() == shipType ? fee == 0d? SdxBookOrderEnum.STATUS_INITAIL.getCode() : SdxBookOrderEnum.STATUS_UNPAY.getCode() : SdxBookOrderEnum.STATUS_PROCESSING.getCode() : status;
		for(Long bookInfoId: bookInfoIds) {
			//获取预估积分
			Integer expectedScores = idExpectedScoresMap.get(bookInfoId);
			expectedScores = expectedScores == null? 15: expectedScores;
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
			.bookIds(StringUtil.longListToString(bookList.stream().map(TSdxBookPo::getId).collect(Collectors.toList())))        //书本编号
			.bookIfIs(StringUtil.longListToString(Arrays.asList(bookInfoIds)))        //书本信息编号
			.userId(userId)
			.status(status)    //针对配送方式不同给予不同的状态,eg.自送类型时创建订单即在途中
			.shippingAddressId(shippingAddressId)
			.expectedTotalScores(totalExpectedScores)
//			.exactTotalScores()	//实际获得积分
			.shipType(shipType)
			.shipPirce(fee)
			.bookStationId(bookStationId)
			.build();
		sdxBookOrderDao.saveTSdxBookOrderIfNotExist(build);
		//返回订单号orderNo
		return orderNo;
	}

	@Override
	public String createDonateOrder(Long userId, Long[] bookInfoIds, Integer shipType, Long shippingAddressId, Long bookStationId, Long serviceId) {
		return createDonateOrder(userId, bookInfoIds, shipType, shippingAddressId, bookStationId, serviceId, null);
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

		// 退邮费

		//处理订单状态
		TSdxBookOrderPo build = TSdxBookOrderPo.builder()
			.status(SdxBookOrderEnum.STATUS_CANCLE.getCode())
			.build();
		sdxBookOrderDao.modTSdxBookOrder(build);
	}

	@Override
	public Object preDonateOrder(String orderNo, Long userId, Long[] bookInfoIds, Integer shipType, Long shippingAddressId, Long bookStationId, Long serviceId, HttpServletRequest request) throws Exception {
		//创建订单
		if(orderNo == null || sdxBookOrderDao.selectByOrderNo(orderNo) == null) {
			orderNo = createDonateOrder(userId, bookInfoIds, shipType, shippingAddressId, bookStationId, serviceId, SdxBookOrderEnum.STATUS_UNPAY.getCode());
		}
		String finalOrderNo = orderNo;
		//创建微信支付参数 -> orderNo => webParam
		Double fee = 5d;	//默认收费5元
		String attach = SdxBookOrderEnum.ATTACH_DONATE.getMsg();
		if(bookInfoIds.length > 5) {	//免支付邮费
			return new HashMap<String, String>() {{
				put("orderNo", finalOrderNo);
			}};
		} else {	//生成支付参数
			return buildWebParam(userId, orderNo, attach, fee, request);	// 往csq_proj 下的微信notify_url对应接口添加邮费支付类型
		}
	}

	@Override
	public Map<String, Object> preOrderInfos(Long userId, Long shippingAddressId, String bookInfoIdStr) {
		Map<String, Object> resultMap = new HashMap<>();
		List<String> bookInfoIds = StringUtil.splitStringToList(bookInfoIdStr, ",");
		boolean isSelfPurchase = shippingAddressId == null;	//自提
		Double shipFee = isSelfPurchase || bookInfoIds.size() > 5 ? 0: 5D;
		Double bookFee = getFee(bookInfoIdStr);
		Integer sdxScores = sdxUserDao.selectByPrimaryKey(userId).getSdxScores();
		Double cutDownFee = sdxUserService.getCutDownFee(sdxScores);
		//获得书本总价 85% -> 最大可抵扣积分数
		List<TSdxBookInfoPo> bookInfoPos = sdxBookInfoDao.selectInIds(bookInfoIds.stream().map(Long::valueOf).collect(Collectors.toList()));
		Double reduce = bookInfoPos.stream()
			.map(TSdxBookInfoPo::getPrice)
			.reduce(0d, Double::sum);
		double maximumDiscount = reduce * 0.85;
		cutDownFee = maximumDiscount > cutDownFee? cutDownFee: maximumDiscount;
		Double surplusBookFee = new BigDecimal(reduce.toString()).subtract(new BigDecimal(cutDownFee.toString())).doubleValue();
		resultMap.put("shipFee", shipFee);
		resultMap.put("bookFee", bookFee);
		resultMap.put("sdxScores", sdxScores);
		resultMap.put("cutDwonFee", cutDownFee);
		resultMap.put("surplusBookFee", surplusBookFee);
		resultMap.put("needToPay", new BigDecimal(surplusBookFee.toString()).add(new BigDecimal(shipFee.toString())).doubleValue());

		return resultMap;
	}

	@Override
	public Integer getOrdersBookNums(List<TSdxBookOrderPo> sdxBookOrderPos) {
		return sdxBookOrderPos.stream()
			.filter(a -> SdxBookOrderEnum.STATUS_DONE.getCode() == a.getStatus() && !StringUtil.isEmpty(a.getBookIds()))
			.map(TSdxBookOrderPo::getBookIds)
			.map(StringUtil::splitToArray)
			.map(List::size)
			.reduce(0, Integer::sum);
	}

	private Double getFee(String bookInfoIdStr) {
		//check
		getBookIds(bookInfoIdStr);
		List<Long> bookInfoIds = StringUtil.splitStringToList(bookInfoIdStr, ",").stream().map(Long::valueOf).collect(Collectors.toList());
		return sdxBookInfoDao.selectInIds(bookInfoIds).stream()
			.map(TSdxBookInfoPo::getPrice).reduce(0d, Double::sum);
	}


	private void afterPaySuccess(TSdxBookOrderPo order) {
		Long userId = order.getUserId();
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
		// 如果你是书本的预定者，则释放对应书籍信息一单位预定数，消除用户-书本预定关系( 删除或者改状态
		String bookIfIs = order.getBookIfIs();
		List<String> strings1 = StringUtil.splitStringToList(bookIfIs, ",");
		List<Long> bookInfoIds = strings1.stream()
			.map(Long::valueOf).collect(Collectors.toList());
		List<TSdxBookInfoPo> bookInfoPos = sdxBookInfoDao.selectInIds(bookInfoIds);


		List<TSdxBookInfoUserPreOrderPo> toUpdater = new ArrayList<>();
		List<TSdxBookInfoPo> sdxBookInfoPos = new ArrayList<>();
		bookInfoPos.stream()
			.forEach(a -> {
				Long bookInfoId = a.getId();
				List<TSdxBookInfoUserPreOrderPo> sdxBookInfoUserPreOrderPos = sdxBookInfoUserPreOrderDao.selectByUserIdAndBookInfoId(userId, bookInfoId);
				if(!sdxBookInfoUserPreOrderPos.isEmpty()) {
					TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo = sdxBookInfoUserPreOrderPos.get(0);
					tSdxBookInfoUserPreOrderPo.setDeletedFlag(Boolean.TRUE);	//消除

					toUpdater.add(tSdxBookInfoUserPreOrderPo);
				}
			});
		sdxBookInfoUserPreOrderDao.update(toUpdater);
		sdxbookDao.update(toUpdaters);
	}

	private TSdxBookOrderPo checkAttach(String out_trade_no, String attach) {
		TSdxBookOrderPo tSdxBookOrderPo = sdxBookOrderDao.selectByOrderNo(out_trade_no);
		if(attach == null || tSdxBookOrderPo == null || !tSdxBookOrderPo.getType().equals(SdxBookOrderEnum.getTypeCode(attach))) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "类型不正确!");
		}
		return tSdxBookOrderPo;
	}

	private String getBookIds(String bookInfoIds) {
		//获取书籍编号
		List<String> bookInfos = Arrays.asList(bookInfoIds.contains(",")? bookInfoIds.split(",") : new String[]{bookInfoIds});

		List<Long> bookIdList = new ArrayList<>();
		List<String> errorMsgList = new ArrayList<>();
		for(String bookInfo: bookInfos) {
			Long bookInfoId = Long.valueOf(bookInfo);
			List<TSdxBookPo> books = sdxBookService.getAvailableBooks(bookInfoId);
			//有效数量为 余量 - 已预定数量
			int surplusAmount = books.size();
			//查询预定数量
			List<TSdxBookInfoUserPreOrderPo> bookInfoUserPreOrderPos = sdxBookInfoUserPreOrderDao.selectByBookInfoId(bookInfoId);
			int preOrderNum = bookInfoUserPreOrderPos.size();
//			surplusAmount -= preOrderNum;	//去掉预定限制
			if(surplusAmount < 1) {
				TSdxBookInfoPo tSdxBookInfoPo = sdxBookInfoDao.selectByPrimaryKey(bookInfoId);
				errorMsgList.add(tSdxBookInfoPo.getName());
			}
			if(!books.isEmpty()) {
				bookIdList.add(books.get(0).getId());
			}
		}
		//检查错误信息
		if(!errorMsgList.isEmpty()) {
			String nameStr = StringUtil.reducer(errorMsgList, "、");
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, nameStr + "等商品的余量已不足！请退出查看!");
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
		//check 积分是否充足
		TCsqUser csqUser = sdxUserDao.selectByPrimaryKey(userId);
		if(scoreUsed > csqUser.getSdxScores()) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您的剩余积分已不足，请返回重新打开订单页!");

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
			.orderNo(orderNo)
			.userId(userId)
			.bookIds(bookIds)
			.bookIfIs(bookInfoIds)
			.bookPrice(bookFee)    //书本总价
			.shipPirce(shipFee)    //邮费
			.scoreDiscount(scoreUsed)    //抵扣积分总额
			.scoreDiscountPrice(sdxUserService.getCutDownFee(scoreUsed))
			.price(bookFee + shipFee)        //订单总价
			.status(SdxBookOrderEnum.STATUS_UNPAY.getCode())
			.build();
		sdxBookOrderDao.saveTSdxBookOrderIfNotExist(build);

		//从购物车中删除对应bookInfoId的书籍条目 -> 如果有
		List<Long> bookInfoIdList = StringUtil.splitToArray(bookInfoIds);
		List<TSdxShoppingTrolleysPo> pos = sdxShoppingTrolleysDao.selectByUserIdInBookInfoIds(userId, bookInfoIdList);
		if(!pos.isEmpty()) {
			List<Long> trolleyIds = pos.stream()
				.map(TSdxShoppingTrolleysPo::getId).collect(Collectors.toList());
			sdxShoppingTrolleysDao.delTSdxShoppingTrolleysByIds(trolleyIds);
		}

		return orderNo;
	}

	private TSdxBookOrderPo findSameUnpayOrder(Long shippingAddressId, String bookInfoIds, Double bookFee, Long userId, Double shipFee) {
		return sdxBookOrderDao.selectByShippingAddressIdAndBookInfoIdsAndBookFeeAndUserIdAndShipFee(shippingAddressId, bookInfoIds, bookFee, userId, shipFee, SdxBookOrderEnum.STATUS_INITAIL.getCode(), SdxBookOrderEnum.TYPE_PURCHASE.getCode());
	}

	private Map<String, String> buildWebParam(Long userId, String orderNo, String attach, Double fee, HttpServletRequest httpServletRequest) throws Exception {
		return csqPayService.buildWebParam(userId, orderNo, attach, fee, httpServletRequest, false, null);
	}

}
