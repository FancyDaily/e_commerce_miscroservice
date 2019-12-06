package com.e_commerce.miscroservice.sdx_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.LimitQueue;
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
import com.e_commerce.miscroservice.sdx_proj.service.SdxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SdxUserServiceImpl implements SdxUserService {

	@Autowired
	private SdxUserDao sdxUserDao;
	@Autowired
	private SdxBookOrderDao sdxBookOrderDao;
	@Autowired
	private SdxShoppingTrolleysDao sdxShoppingTrolleysDao;

	@Autowired
	@Qualifier("sdxRedisTemplate")
	private HashOperations<String, String, Object> userRedisTemplate;

	@Override
	public TCsqUser infos(Long userId) {
		//可能对兴趣爱好作出处理
		TCsqUser byId = sdxUserDao.findById(userId);
		//查询购书订单数
		List<TSdxBookOrderPo> sdxBookOrderPos = sdxBookOrderDao.selectByUserIdAndType(userId, SdxBookOrderEnum.TYPE_DONATE.getCode());
		byId.setBookDonateOrderNum(sdxBookOrderPos.size());
		//查询购物车条数
		List<TSdxShoppingTrolleysPo> tSdxShoppingTrolleysPos = sdxShoppingTrolleysDao.selectByUserId(userId);
		byId.setTrolleyNum(tSdxShoppingTrolleysPos.size());

		if(byId != null && byId.getIsSdx() != null) {
			byId.setIsSdx(SdxUserEnum.IS_SDX_YES.getCode());
			sdxUserDao.update(byId);
		}
		return byId;
	}

	@Override
	public Object globalDonate() {
		String hashKey = SdxRedisEnum.ALL.getMsg();
		Object exist = userRedisTemplate.get(SdxRedisEnum.CSQ_GLOBAL_DONATE_BROADCAST.getMsg(), hashKey);
		Queue<CsqDonateRecordVo> voList;
		if (exist == null) {
			voList = new LimitQueue<>(10);    //创建带上限的队列
		} else {
			voList = (LimitQueue<CsqDonateRecordVo>) exist;
		}
		/*//TODO 处理得到list
		List<CsqDonateRecordVo> resultList = new ArrayList<>();
		Iterator<CsqDonateRecordVo> iterator = voList.iterator();
		while (iterator.hasNext()) {
			CsqDonateRecordVo csqDonateRecordVo = iterator.next();
			Long createTime = csqDonateRecordVo.getCreateTime();
			long interval = System.currentTimeMillis() - createTime;
			Long minuteAgo = interval / 1000 / 60;
			minuteAgo = minuteAgo > 60 ? 60 : minuteAgo;
			csqDonateRecordVo.setMinutesAgo(minuteAgo.intValue());
			iterator.remove();
			resultList.add(csqDonateRecordVo);
		}
		//排序
		resultList = resultList.stream()
			.sorted(Comparator.comparing(CsqDonateRecordVo::getCreateTime).reversed()).collect(Collectors.toList());
		return resultList;*/
		return null;
	}

	@Override
	public Double getCutDownFee(Integer sdxScores) {
		return sdxScores == null || sdxScores <= 0 ? 0: 3d;	//TODO 写死 所有积分抵扣3元
	}
}
