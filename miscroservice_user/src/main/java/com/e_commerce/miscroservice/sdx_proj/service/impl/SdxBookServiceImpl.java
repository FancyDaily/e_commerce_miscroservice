package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.*;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookAfterReadingNoteUserEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookOrderEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookTicketEnum;
import com.e_commerce.miscroservice.sdx_proj.po.*;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookOrderService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookService;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxBookOrderUserInfoVo;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxBookDetailVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* 书袋熊书籍的service层
*
*/

@Service
@Log
public class SdxBookServiceImpl implements SdxBookService {

    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;

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
        }
        else {
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
        if (id == null||id<=0L) {
            log.warn("根据Id查找书袋熊书籍,所传Id不符合规范");
            return new TSdxBookVo();
        }
        log.info("start根据Id={}查找书袋熊书籍", id);
        TSdxBookPo tSdxBookPo = sdxBookDao.findTSdxBookById(id);
        return tSdxBookPo==null?new TSdxBookVo():tSdxBookPo.copyTSdxBookVo() ;
    }

	@Override
	public List<TSdxBookVo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size) {
		return findTSdxBookByAll(tSdxBookPo, page, size, null);
	}

	@Override
	public String detail(Long id, Long userId) {
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
				if (userId.equals(a.getUserId())) a.setNoNeedBuy(Boolean.TRUE);
				return a;
			}).collect(Collectors.toList());
		List<Long> afrdnIds = limitedAfterReadingNoteList.stream()
			.filter(a -> !a.isNoNeedBuy())
			.map(TSdxBookAfterReadingNotePo::getId).collect(Collectors.toList());
		//查看自己是否为创作者
		List<TSdxBookAfterReadingNoteUserPo> userAfrdnList = sdxBookAfterReadingNoteUserDao.selectInAfrdnIdsAndUserIdAndIsPurchase(afrdnIds, SdxBookAfterReadingNoteUserEnum.IS_PURCHASE_YES.getCode(), userId);
		Map<Long, List<TSdxBookAfterReadingNoteUserPo>> idArdnMap = userAfrdnList.stream()
			.collect(Collectors.groupingBy(TSdxBookAfterReadingNoteUserPo::getId));

		limitedAfterReadingNoteList = limitedAfterReadingNoteList.stream()
			.filter(a -> !a.isNoNeedBuy())
			.map(a -> {
				List<TSdxBookAfterReadingNoteUserPo> tSdxBookAfterReadingNoteUserPos = idArdnMap.get(a.getId());
				if(tSdxBookAfterReadingNoteUserPos != null) {
					a.setNoNeedBuy(userId.equals(tSdxBookAfterReadingNoteUserPos.get(0).getUserId()));
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
		int rate = (int)((double)purchasedNums / (purchasedNums + surplusNum) * 100);

		//装配
		SdxBookDetailVo sdxBookDetailVo = sdxBookInfoPo.copySdxBookDetailVo();
		sdxBookDetailVo.setSurplusNum(surplusNum);
		sdxBookDetailVo.setDonateUserRecords(donateList);
		sdxBookDetailVo.setAfterReadingNoteVos(afterReadingNoteVos);
		sdxBookDetailVo.setPurchaseRate(rate);

		return new String();
	}

	@Override
	public QueryResult soldOrPurchaseUserList(Long id, Integer pageNum, Integer pageSize, Boolean isSold) {
    	//初始结果集
		List<TSdxBookOrderPo> tSdxBookOrderPos = sdxBookOrderDao.selectByBookInfoIdAndTypeAndStatus(id,
			isSold ? SdxBookOrderEnum.TYPE_DONATE.getCode() : SdxBookOrderEnum.TYPE_PURCHASE.getCode(),
			SdxBookOrderEnum.STATUS_DONE.getCode(), new Page(pageNum, pageSize), MybatisPlusBuild.OrderBuild.buildDesc(TSdxBookOrderPo::getCreateTime));
		//特殊条件
		if(tSdxBookOrderPos.isEmpty()) return PageUtil.buildQueryResult();
		//构建map备用
		Map<Long, List<TCsqUser>> idUserMap = sdxUserDao.groupingByIdInIds(tSdxBookOrderPos.stream().map(TSdxBookOrderPo::getUserId).collect(Collectors.toList()));
		//装载
		List<TSdxBookOrderPo> vos = tSdxBookOrderPos.stream()
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
				return a;
			}).collect(Collectors.toList());

		return PageUtil.buildQueryResult(vos, IdUtil.getTotal());
	}

	@Override
	public void preOrder(Long id, Long userId) {
		//获取所有预定书券
		List<TSdxBookTicketPo> sdxBookTicketPos = sdxBookTicketDao.selectByUserId(userId);
		if(sdxBookTicketPos.isEmpty()) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "没有可用书券!");

		//检查可用预定数
		TSdxBookInfoPo tSdxBookInfoPo = sdxBookInfoDao.selectByPrimaryKey(id);
		Integer maximumReserve = tSdxBookInfoPo.getMaximumReserve();
		//查询已经预定的数量
		List<TSdxBookInfoUserPreOrderPo> tSdxBookInfoUserPreOrderPos = sdxBookInfoUserPreOrderDao.selectByBookInfoId(id);
		int doneReserve = tSdxBookInfoUserPreOrderPos.size();
		if(doneReserve >= maximumReserve) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当前预定量已达最大!");
		}
		//小号一张书券,若无指定，消耗最早过期的有效券
		sdxBookTicketPos = sdxBookTicketPos.stream()
			.sorted(Comparator.comparing(TSdxBookTicketPo::getCreateTime).reversed()).collect(Collectors.toList());
		TSdxBookTicketPo tSdxBookTicketPo = sdxBookTicketPos.get(0);
		tSdxBookTicketPo.setIsUsed(SdxBookTicketEnum.IS_USED_YES.getCode());
		sdxBookTicketDao.modTSdxBookTickt(tSdxBookTicketPo);
		//TODO 建立书本信息-用户关联 预定
	}

	@Override
	public List<TSdxBookPo> getAvailableBooks(Long bookInfoId) {
		return sdxBookDao.selectByBookInfoIdAndStatus(bookInfoId, SdxBookEnum.STATUS_ON_SHELF.getCode());
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
    public List<TSdxBookVo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size, Integer sortType) {
        List    <TSdxBookVo> tSdxBookVos = new ArrayList<>();
        if (tSdxBookPo == null) {
            log.warn("根据条件查找书袋熊书籍,参数不对");
            return tSdxBookVos;
        }
        log.info("start根据条件查找书袋熊书籍={}", tSdxBookPo);
        List        <TSdxBookPo> tSdxBookPos = sdxBookDao.findTSdxBookByAll(            tSdxBookPo,page,size, sortType);
        for (TSdxBookPo po : tSdxBookPos) {
            tSdxBookVos.add(po.copyTSdxBookVo());
        }
        return tSdxBookVos;
    }
}
