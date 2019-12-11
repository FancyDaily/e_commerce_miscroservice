package com.e_commerce.miscroservice.sdx_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.LimitQueue;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDonateRecordVo;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookOrderDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxShoppingTrolleysDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxUserDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookOrderEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxRedisEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxUserEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShoppingTrolleysPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookOrderService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxUserService;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxGlobalDonateRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SdxUserServiceImpl implements SdxUserService {

	@Autowired
	private SdxUserDao sdxUserDao;
	@Autowired
	private SdxBookOrderDao sdxBookOrderDao;
	@Autowired
	private SdxShoppingTrolleysDao sdxShoppingTrolleysDao;
	@Autowired
	private SdxBookOrderService sdxBookOrderService;

	@Autowired
	@Qualifier("sdxRedisTemplate")
	private HashOperations<String, String, Object> userRedisTemplate;

	@Override
	public TCsqUser infos(Long userId) {
		//可能对兴趣爱好作出处理
		TCsqUser byId = sdxUserDao.findById(userId);
		if(byId != null) {
			Integer sdxScores = byId.getSdxScores();
			Double money = transScoresToMoney(sdxScores);
			byId.setSdxScoreMoney(money);
			//查询购书订单数 -> 捐赠的书本数量
			List<TSdxBookOrderPo> sdxBookOrderPos = sdxBookOrderDao.selectByUserIdAndType(userId, SdxBookOrderEnum.TYPE_DONATE.getCode());
			byId.setBookDonateOrderNum(sdxBookOrderPos.size());
			Integer bookNums = sdxBookOrderService.getOrdersBookNums(sdxBookOrderPos);

			byId.setBookDonateOrderNum(bookNums);
			//查询购物车条数
			List<TSdxShoppingTrolleysPo> tSdxShoppingTrolleysPos = sdxShoppingTrolleysDao.selectByUserId(userId);
			byId.setTrolleyNum(tSdxShoppingTrolleysPos.size());

			if(byId.getIsSdx() != null) {
				byId.setIsSdx(SdxUserEnum.IS_SDX_YES.getCode());
				sdxUserDao.update(byId);
			}
		}
		return byId;
	}

	@Override
	public Object globalDonate() {
		String hashKey = SdxRedisEnum.ALL.getMsg();
		Object exist = userRedisTemplate.get(SdxRedisEnum.CSQ_GLOBAL_DONATE_BROADCAST.getMsg(), hashKey);
		LimitQueue<SdxGlobalDonateRecordVo> voList;
		if (exist == null) {
			voList = new LimitQueue<>(10);    //创建带上限的队列
		} else {
			voList = (LimitQueue<SdxGlobalDonateRecordVo>) exist;
		}
		// 处理得到list
		List<SdxGlobalDonateRecordVo> resultList = new ArrayList<>();
		Iterator<SdxGlobalDonateRecordVo> iterator = voList.iterator();
		while (iterator.hasNext()) {
			SdxGlobalDonateRecordVo csqDonateRecordVo = iterator.next();
			Long createTime = csqDonateRecordVo.getTimeStamp();
			String timeAgo = DateUtil.minutes2TimeAgo(DateUtil.timestamp2MinutesAgo(createTime));

			//构建描述
			StringBuilder builder = new StringBuilder();
			String donate = "捐赠";
			String donaterName = csqDonateRecordVo.getDonaterName();
			String bookInfoNames = csqDonateRecordVo.getBookInfoNames();
			bookInfoNames = bookInfoNames.substring(1);
			String description = builder.append(timeAgo).append(" ").append(donaterName).append(donate).append(sdxBookOrderService.buidBookStrs(bookInfoNames, Boolean.FALSE)).toString();
			csqDonateRecordVo.setDescription(description);
			csqDonateRecordVo.setTimeAgo(timeAgo);
			iterator.remove();
			resultList.add(csqDonateRecordVo);
		}
		//排序
		resultList = resultList.stream()
			.sorted(Comparator.comparing(SdxGlobalDonateRecordVo::getTimeStamp).reversed()).collect(Collectors.toList());
		return resultList;
	}

	@Override
	public Double transScoresToMoney(Integer sdxScores) {
		return getCutDownFee(sdxScores);
	}

	@Override
	public Double getCutDownFee(Integer sdxScores) {
		return sdxScores == null || sdxScores <= 0 ? 0: Double.valueOf(sdxScores);	//TODO 写死 所有积分抵扣3元
	}
}
