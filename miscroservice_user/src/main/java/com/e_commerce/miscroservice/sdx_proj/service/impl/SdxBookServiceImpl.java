package com.e_commerce.miscroservice.sdx_proj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqServiceEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.*;
import com.e_commerce.miscroservice.sdx_proj.enums.*;
import com.e_commerce.miscroservice.sdx_proj.po.*;
import com.e_commerce.miscroservice.sdx_proj.service.*;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxBookOrderUserInfoVo;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxBookDetailVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 书袋熊书籍的service层
 */

@Service
@Log
public class SdxBookServiceImpl implements SdxBookService {

	private final long ERROR_LONG = 0L;
	private final int ERROR_INT = 0;

	@Autowired
	private SdxBookService sdxBookService;

	@Autowired
	private SdxBookDao sdxBookDao;

	@Autowired
	private SdxBookAfterReadingNoteDao sdxBookAfterReadingNoteDao;

	@Autowired
	private SdxBookAfterReadingNoteUserDao sdxBookAfterReadingNoteUserDao;

	@Autowired
	private SdxBookInfoDao sdxBookInfoDao;

	@Autowired
	private SdxBookOrderService sdxorderservice;

	@Autowired
	private SdxBookOrderDao sdxBookOrderDao;

	@Autowired
	private SdxUserDao sdxUserDao;

	@Autowired
	private SdxBookTicketDao sdxBookTicketDao;

	@Autowired
	private SdxBookInfoUserPreOrderDao sdxBookInfoUserPreOrderDao;

	@Autowired
	private SdxWishListDao sdxWishListDao;

	@Autowired
	private SdxPublishService sdxPublishService;

	@Autowired
	private SdxPublishDao sdxPublishDao;

	@Autowired
	private CsqServiceDao csqServiceDao;

	@Autowired
	private SdxUserService sdxUserService;

	@Autowired
	private SdxShoppingTrolleysService sdxShoppingTrolleysService;

	@Override
	public long modTSdxBook(TSdxBookPo tSdxBookPo) {
		if (tSdxBookPo == null) {
			log.warn("操作书袋熊书籍参数为空");
			return ERROR_LONG;
		}
		if (tSdxBookPo.getId() == null) {
			log.info("start添加书袋熊书籍={}", tSdxBookPo);
			int result = sdxBookDao.saveTSdxBookIfNotExist(tSdxBookPo);
			return result != 0 ? tSdxBookPo.getId() : ERROR_LONG;
		} else {
			log.info("start修改书袋熊书籍={}", tSdxBookPo.getId());
			return sdxBookDao.modTSdxBook(tSdxBookPo);
		}
	}

	@Override
	public int delTSdxBookByIds(Long... ids) {
		if (ids == null || ids.length == 0) {
			log.warn("删除书袋熊书籍,ids集合为空");
			return ERROR_INT;
		}
		log.warn("start删除Id集合={},书袋熊书籍", Arrays.asList(ids));
		return sdxBookDao.delTSdxBookByIds(ids);
	}

	@Override
	public TSdxBookVo findTSdxBookById(Long id) {
		if (id == null || id <= 0L) {
			log.warn("根据Id查找书袋熊书籍,所传Id不符合规范");
			return new TSdxBookVo();
		}
		log.info("start根据Id={}查找书袋熊书籍", id);
		TSdxBookPo tSdxBookPo = sdxBookDao.findTSdxBookById(id);
		return tSdxBookPo == null ? new TSdxBookVo() : tSdxBookPo.copyTSdxBookVo();
	}

	@Override
	public List<TSdxBookVo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size) {
		return findTSdxBookByAll(tSdxBookPo, page, size, null);
	}

	@Override
	public SdxBookDetailVo detail(Long id, Long userId) {
		//曾经捐过这本书的列表
		List<SdxBookOrderUserInfoVo> donateList = sdxorderservice.getEverDonateList(id);
		//基本信息
		TSdxBookInfoPo sdxBookInfoPo = sdxBookInfoDao.selectByPrimaryKey(id);

		//余量
		List<TSdxBookPo> tSdxBookPos = sdxBookDao.selectByBookInfoIdAndStatus(id, SdxBookEnum.STATUS_ON_SHELF.getCode());
		int surplusNum = tSdxBookPos.size();

		//读后感列表(前5条)
		List<TSdxBookAfterReadingNotePo> limitedAfterReadingNoteList = sdxBookAfterReadingNoteDao.selectByBookInfoIdPageDesc(id, 1, 5);
		//确认读后感购买状态
		limitedAfterReadingNoteList = limitedAfterReadingNoteList.stream()
			.map(a -> {
				//查询点赞状态
				Long afrId = a.getId();
				TSdxBookAfterReadingNoteUserPo afterReadingNoteUserPo = sdxBookAfterReadingNoteUserDao.selectByBookAfrnIdAndUserIdAndIsThumbAndType(afrId, userId, SdxBookAfterReadingNoteUserEnum.IS_THUMB_YES.getCode(), SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode());
				//查看是点赞还是点踩
				a.setThumbType(afterReadingNoteUserPo == null || afterReadingNoteUserPo.getThumbType() == null ? SdxBookAfterReadingNoteUserEnum.THUMB_TYPE_UP.getCode(): afterReadingNoteUserPo.getThumbType());
				a.setIsThumb(afterReadingNoteUserPo == null? SdxBookAfterReadingNoteUserEnum.IS_THUMB_NO.getCode(): afterReadingNoteUserPo.getIsThumb());
				if (userId != null && userId.equals(a.getUserId())) a.setNoNeedBuy(Boolean.TRUE);
				return a;
			}).collect(Collectors.toList());
		List<Long> afrdnIds = limitedAfterReadingNoteList.stream()
			.filter(a -> !a.isNoNeedBuy())
			.map(TSdxBookAfterReadingNotePo::getId).collect(Collectors.toList());
		//查看自己是否为创作者
		List<TSdxBookAfterReadingNoteUserPo> userAfrdnList = afrdnIds.isEmpty()? new ArrayList<>(): sdxBookAfterReadingNoteUserDao.selectInAfrdnIdsAndUserIdAndIsPurchase(afrdnIds, SdxBookAfterReadingNoteUserEnum.IS_PURCHASE_YES.getCode(), userId);
		Map<Long, List<TSdxBookAfterReadingNoteUserPo>> idArdnMap = userAfrdnList.stream()
			.collect(Collectors.groupingBy(TSdxBookAfterReadingNoteUserPo::getId));

		limitedAfterReadingNoteList = limitedAfterReadingNoteList.stream()
			.map(a -> {
				List<TSdxBookAfterReadingNoteUserPo> tSdxBookAfterReadingNoteUserPos = idArdnMap.get(a.getId());
				if (tSdxBookAfterReadingNoteUserPos != null) {
					a.setNoNeedBuy(userId != null && userId.equals(tSdxBookAfterReadingNoteUserPos.get(0).getUserId()));
				}
				return a;
			}).collect(Collectors.toList());

		//装载创作者基本信息
		List<Long> userIds = limitedAfterReadingNoteList.stream()
			.map(TSdxBookAfterReadingNotePo::getUserId).collect(Collectors.toList());
		Map<Long, List<TCsqUser>> idUserMap = sdxUserDao.groupingByIdInIds(userIds);
		List<TSdxBookAfterReadingNoteVo> afterReadingNoteVos = limitedAfterReadingNoteList.stream()
			.map(a -> {
				Long tUserId = a.getUserId();
				List<TCsqUser> csqUsers = idUserMap.get(tUserId);
				TSdxBookAfterReadingNoteVo vo = a.copyTSdxBookAfterReadingNoteVo();
				if (csqUsers != null) {
					TCsqUser csqUser = csqUsers.get(0);
					vo.setUserName(csqUser.getName());
					vo.setHeadPic(csqUser.getUserHeadPortraitPath());
				}
				return vo;
			}).collect(Collectors.toList());
		//书籍去向(购书订单、剩余书籍做比例)  TODO 待定
		List<TSdxBookOrderPo> purchasedOrders = sdxBookOrderDao.selectByBookInfoIdAndTypeAndStatus(id, SdxBookOrderEnum.TYPE_PURCHASE.getCode(), SdxBookOrderEnum.STATUS_DONE.getCode());
		int purchasedNums = purchasedOrders.size();
		int rate = (int) ((double) purchasedNums / (purchasedNums + surplusNum) * 100);

		//库存
		List<TSdxBookPo> availableBooks = sdxBookService.getAvailableBooks(sdxBookInfoPo.getId());

		//装配
		SdxBookDetailVo sdxBookDetailVo = sdxBookInfoPo.copySdxBookDetailVo();
		sdxBookDetailVo.setBookInfoId(sdxBookInfoPo.getId());
		sdxBookDetailVo.setPublisher(sdxBookInfoPo.getPublisher());
		sdxBookDetailVo.setBindingStyle(sdxBookInfoPo.getBindingStyle());
		sdxBookDetailVo.setCoverPic(sdxBookInfoPo.getCoverPic());
		sdxBookDetailVo.setSurplusNum(surplusNum);
		sdxBookDetailVo.setDonateUserRecords(donateList);
		sdxBookDetailVo.setAfterReadingNoteVos(afterReadingNoteVos);
		sdxBookDetailVo.setPurchaseRate(rate);
		sdxBookDetailVo.setAvailableNum(availableBooks.size());
		sdxBookDetailVo.setInTrolley(sdxShoppingTrolleysService.isInTrolley(sdxBookDetailVo.getBookInfoId(), userId));

		//获取用户所有积分，计算积分能够抵扣多少钱，和剩余多少钱
		Double maximumDiscount = dealWithScoreMoney(userId, sdxBookDetailVo.getPrice());
		sdxBookDetailVo.setMaximumDiscount(maximumDiscount);
		sdxBookDetailVo.setSurplusPrice(new BigDecimal(sdxBookDetailVo.getPrice().toString()).subtract(new BigDecimal(maximumDiscount.toString())).doubleValue());

		return sdxBookDetailVo;
	}

	public static void main(String[] args) {
		/*Double price = 58.7d;
		Double maximumDiscount = price * 0.85;
		maximumDiscount = Math.floor(maximumDiscount);
		System.out.println(maximumDiscount);*/
		BigDecimal bigDecimal = new BigDecimal(String.valueOf("49.8"));
		BigDecimal decimal = new BigDecimal("25.0");
		double v = bigDecimal.subtract(decimal).doubleValue();
		System.out.println(v);
	}

	@Override
	public Double dealWithScoreMoney(Long userId, Double price) {
		Integer sdxScores;
		if(userId == null) {
			sdxScores = 0;
		} else {
			TCsqUser csqUser = sdxUserDao.selectByPrimaryKey(userId);
			sdxScores = csqUser.getSdxScores();
		}
		Double cutDownFee = sdxUserService.getCutDownFee(sdxScores);
		// 书本价格的 85%为最高可抵扣积分，与用户的积分价值取最小即可得到最终结果
		Double maximumDiscount = price * 0.85;
		maximumDiscount = Math.floor(maximumDiscount);
		if(maximumDiscount > cutDownFee) {
			maximumDiscount = cutDownFee;
		}
		return maximumDiscount;
	}

	@Override
	public QueryResult soldOrPurchaseUserList(Long id, Integer pageNum, Integer pageSize, Boolean isSold) {
		//初始结果集
		List<TSdxBookOrderPo> tSdxBookOrderPos = sdxBookOrderDao.selectByBookInfoIdAndTypeAndStatus(id,
			isSold ? SdxBookOrderEnum.TYPE_DONATE.getCode() : SdxBookOrderEnum.TYPE_PURCHASE.getCode(),
			SdxBookOrderEnum.STATUS_DONE.getCode(), new Page(pageNum, pageSize), MybatisPlusBuild.OrderBuild.buildDesc(TSdxBookOrderPo::getCreateTime));
		//特殊条件
		if (tSdxBookOrderPos.isEmpty()) return PageUtil.buildQueryResult();
		//构建map备用
		Map<Long, List<TCsqUser>> idUserMap = sdxUserDao.groupingByIdInIds(tSdxBookOrderPos.stream().map(TSdxBookOrderPo::getUserId).collect(Collectors.toList()));
		//装载
		List<SdxBookOrderUserInfoVo> vos = tSdxBookOrderPos.stream()
			.map(a -> {
				Long userId = a.getUserId();
				List<TCsqUser> tCsqUsers = idUserMap.get(userId);
				SdxBookOrderUserInfoVo vo = new SdxBookOrderUserInfoVo();
				if (tCsqUsers != null) {
					TCsqUser csqUser = tCsqUsers.get(0);
					vo.setName(csqUser.getName());
					vo.setHeadPic(csqUser.getUserHeadPortraitPath());
				}
				long timeStamp = a.getCreateTime().getTime();
				vo.setTimeStamp(timeStamp);
				vo.buildDoneTimeDesc(timeStamp);
				return vo;
			}).collect(Collectors.toList());

		return PageUtil.buildQueryResult(vos, IdUtil.getTotal());
	}

	@Override
	public void preOrder(Long id, Long userId) {
		//获取所有预定书券
		List<TSdxBookTicketPo> sdxBookTicketPos = sdxBookTicketDao.selectByUserIdAndIsUsed(userId, SdxBookTicketEnum.IS_USED_NO.getCode());
		if (sdxBookTicketPos.isEmpty()) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "没有可用书券!");

		//检查可用预定数
		TSdxBookInfoPo tSdxBookInfoPo = sdxBookInfoDao.selectByPrimaryKey(id);
		Integer maximumReserve = tSdxBookInfoPo.getMaximumReserve();
		//查询已经预定的数量
		List<TSdxBookInfoUserPreOrderPo> tSdxBookInfoUserPreOrderPos = sdxBookInfoUserPreOrderDao.selectByBookInfoId(id);
		int doneReserve = tSdxBookInfoUserPreOrderPos.size();
		if (doneReserve >= maximumReserve) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当前预定量已达最大!");
		}
		//小号一张书券,若无指定，消耗最早过期的有效券
		sdxBookTicketPos = sdxBookTicketPos.stream()
			.sorted(Comparator.comparing(TSdxBookTicketPo::getCreateTime).reversed()).collect(Collectors.toList());
		TSdxBookTicketPo tSdxBookTicketPo = sdxBookTicketPos.get(0);
		tSdxBookTicketPo.setIsUsed(SdxBookTicketEnum.IS_USED_YES.getCode());
		sdxBookTicketDao.modTSdxBookTickt(tSdxBookTicketPo);
		// 建立书本信息-用户关联 预定
		TSdxBookInfoUserPreOrderPo build = TSdxBookInfoUserPreOrderPo.builder()
			.userId(userId)
			.bookInfoId(id)
			.build();
		sdxBookInfoUserPreOrderDao.saveTSdxBookInfoUserPreOrderIfNotExist(build);
		//TODO 预定的消耗问题
	}

	@Override
	public List<TSdxBookPo> getAvailableBooks(Long bookInfoId) {
		List<TSdxBookPo> tSdxBookPos = sdxBookDao.selectByBookInfoIdAndStatus(bookInfoId, SdxBookEnum.STATUS_ON_SHELF.getCode());
		return tSdxBookPos.isEmpty()? new ArrayList<>(): tSdxBookPos;
	}

	@Override
	public Map<Long, Integer> getIdExpectedScoresMap(Long... bookInfoIds) {
		List<TSdxBookInfoPo> sdxBookInfoPos = sdxBookInfoDao.selectInIds(bookInfoIds);
		Map<Long, List<TSdxBookInfoPo>> idEntityMap = sdxBookInfoPos.stream()
			.collect(Collectors.groupingBy(TSdxBookInfoPo::getId));
		Map<Long, Integer> res = new HashMap<>();
		idEntityMap.forEach((k, v) -> {
			res.put(k, v.stream()
				.map(TSdxBookInfoPo::getExpectedScore).collect(Collectors.toList()).get(0)
			);
		});
		return res;
	}

	@Override
	public void putOnShelf(String bookIds) {
		//书本上架
		List<Long> bookIdList = StringUtil.splitToArray(bookIds);
		List<TSdxBookPo> books = sdxBookDao.selectInIds(bookIdList);
		List<TSdxBookPo> toUpdater = books.stream()
			.filter(a -> SdxBookEnum.STATUS_INITIAL.getCode() == a.getStatus())
			.map(a -> {
				Long id = a.getId();
				TSdxBookPo build = TSdxBookPo.builder()
					.status(SdxBookEnum.STATUS_ON_SHELF.getCode())
					.build();
				build.setId(id);
				return build;
			}).collect(Collectors.toList());
		sdxBookDao.update(toUpdater);
	}

	@Override
	public List<TSdxBookInfoPo> mostFollowList(Long userId) {
		//获得心愿单信息，统计最高的几个
		List<TSdxWishListPo> sdxWishListPos = sdxWishListDao.selectAll();
		Map<Long, List<TSdxWishListPo>> bookInfoIdWishMap = sdxWishListPos.stream()
			.collect(Collectors.groupingBy(TSdxWishListPo::getBookInfoId));
		List<TSdxBookInfoPo> sdxBookInfoPos = new ArrayList<>();
		bookInfoIdWishMap.forEach((k, v) -> {
			TSdxBookInfoPo bookInfoPo = new TSdxBookInfoPo();
			int count = v.size();
			bookInfoPo.setId(k);
			bookInfoPo.setWishNum(count);
			sdxBookInfoPos.add(bookInfoPo);
		});
		List<Long> ids = sdxBookInfoPos.stream()
			.map(TSdxBookInfoPo::getId).collect(Collectors.toList());
		List<TSdxBookInfoPo> tSdxBookInfoPos = sdxBookInfoDao.selectInIds(ids);

		List<TSdxBookInfoPo> result = tSdxBookInfoPos
			.stream()
			.map(a -> {
				int count = bookInfoIdWishMap.get(a.getId()).size();
				a.setWishNum(count);
				a.setInTrolley(userId !=null && sdxShoppingTrolleysService.isInTrolley(a.getId(), userId));
				return a;
			}).collect(Collectors.toList());
		result = result.stream()
			.sorted(Comparator.comparing(TSdxBookInfoPo::getWishNum).reversed())
			.limit(10).collect(Collectors.toList());

		return result;
	}

	@Override
	public List<TSdxBookInfoPo> suggestList(Long userId) {
		//从publish中获取 -> mainkey = "suggest"
		Map map = getBookDailySuggestMap();
		Integer today = DateUtil.getWeekDayInt(System.currentTimeMillis());
		List<Long> list = (List<Long>) map.get(today.toString());
		List<TSdxBookInfoPo> bookInfoPos = sdxBookInfoDao.selectInIds(list);
		return bookInfoPos.stream()
			.map(a -> {
				Long id = a.getId();
				List<TSdxBookPo> availableBooks = sdxBookService.getAvailableBooks(id);
				a.setAvailableNum(availableBooks.size());
				a.setInTrolley(userId !=null && sdxShoppingTrolleysService.isInTrolley(a.getId(), userId));
				return a;
			}).collect(Collectors.toList());
	}

	private Map getBookDailySuggestMap() {
		String s = sdxPublishService.getValue(SdxPublishEnum.MAIN_KEY_BOOK_DAILY_SUGGEST.getCode());
		return toMap(s);
	}

	private Map toMap(String s) {
		return JSONObject.toJavaObject(JSONObject.parseObject(s), Map.class);
	}

	@Override
	public String getSuggestInitail() {
		List<Long> bookInfoIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
		String s2 = JSONObject.toJSONString(bookInfoIds);
		System.out.println(s2);

		Map<String, List<Long>> resultMap = new HashMap() {
			{
				put("1", bookInfoIds);
				put("2", bookInfoIds);
				put("3", bookInfoIds);
				put("4", bookInfoIds);
				put("5", bookInfoIds);
				put("6", bookInfoIds);
				put("7", bookInfoIds);
			}
		};

		String s = JSONObject.toJSONString(resultMap);
		return s;
	}

	@Override
	public List<TCsqService> gotoServiceList() {
		List<TCsqService> csqServices = csqServiceDao.selectByIsSdx(CsqServiceEnum.IS_SDX_TRUE.getCode());
		Map<Long, List<TCsqService>> idServiceListMap = csqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));
		List<Long> serviceIds = csqServices.stream()
			.map(TCsqService::getId).collect(Collectors.toList());
		List<TSdxBookInfoPo> sdxBookInfoPos = sdxBookInfoDao.selectInServiceIds(serviceIds);
		Map<Long, List<TSdxBookInfoPo>> serviceIdBookInfoPo = sdxBookInfoPos.stream()
			.collect(Collectors.groupingBy(TSdxBookInfoPo::getServiceId));
		List<TCsqService> resultList = new ArrayList<>();
		serviceIdBookInfoPo
			.forEach((k, v) -> {
				TCsqService csqService = idServiceListMap.get(k).get(0);
				int count = v.size();	//当前项目总相关书目（流通书本
				csqService.setRelatedBookInfoNum(count);
				resultList.add(csqService);
			});
		return resultList;
	}

	@Override
	public boolean preOrderStatus(Long id, Long bookInfoId) {
		List<TSdxBookInfoUserPreOrderPo> bookInfoUserPreOrderPos = sdxBookInfoUserPreOrderDao.selectByUserIdAndBookInfoId(id, bookInfoId);
		return !bookInfoUserPreOrderPos.isEmpty();
	}

	@Override
	public void setSuggestList(Integer dayNo, List<Long> bookInfoIds) {
		//check
		if(dayNo > 7 || dayNo < 1) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "周几不合法！");

		TSdxPublish tSdxPublish = sdxPublishDao.selectByMainKey(SdxPublishEnum.MAIN_KEY_BOOK_DAILY_SUGGEST.getCode());
		String s = tSdxPublish.getValue();

		Map<String, List<Long>> resultMap = toMap(s);
		resultMap.put(dayNo.toString(), bookInfoIds);

		s = JSONObject.toJSONString(resultMap);
		tSdxPublish.setValue(s);
		sdxPublishDao.update(tSdxPublish);
	}

	@Override
	public List<TSdxBookVo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size, Integer sortType) {
		List<TSdxBookVo> tSdxBookVos = new ArrayList<>();
		if (tSdxBookPo == null) {
			log.warn("根据条件查找书袋熊书籍,参数不对");
			return tSdxBookVos;
		}
		log.info("start根据条件查找书袋熊书籍={}", tSdxBookPo);
		List<TSdxBookPo> tSdxBookPos = sdxBookDao.findTSdxBookByAll(tSdxBookPo, page, size, sortType);
		for (TSdxBookPo po : tSdxBookPos) {
			tSdxBookVos.add(po.copyTSdxBookVo());
		}
		return tSdxBookVos;
	}
}
