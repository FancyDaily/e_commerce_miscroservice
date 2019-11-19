package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglPositionDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglUserDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglUserPositionDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglPosistion;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUser;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUserPosistion;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:40
 */
@Service
public class LpglPositionServiceImpl implements LpglPositionService {

	@Autowired
	private LpglPositionDao lpglPositionDao;

	@Autowired
	private LpglUserPositionDao lpglUserPositionDao;

	@Autowired
	private LpglUserDao lpglUserDao;

	@Override
	public void modify(Long positionId, Double discountCredit) {
		lpglPositionDao.modify(positionId, discountCredit);
	}

	@Override
	public TLpglPosistion getPosition(Long userId) {
		List<TLpglUserPosistion> tLpglUserPosistions = lpglUserPositionDao.selectByUserId(userId);
		if (!tLpglUserPosistions.isEmpty()) {
			TLpglUserPosistion lpglUserPosistion = tLpglUserPosistions.get(0);
			Long posistionId = lpglUserPosistion.getPosistionId();
			return lpglPositionDao.selectByPrimaryKey(posistionId);
		}
		return null;
	}

	@Override
	public TLpglPosistion getHigherPosition(TLpglPosistion position) {
		Integer level = position.getLevel();
		level = level == null ? null : --level < 0 ? 0 : level;
		List<TLpglPosistion> posistions = lpglPositionDao.selectByLevel(level);
		if (!posistions.isEmpty()) {
			return posistions.get(0);
		}
		return null;
	}

	@Override
	public List<TLpglUser> findUsersByPositionNames(List<String> posisitionName) {
		if (posisitionName.isEmpty()) return new ArrayList<>();
		List<TLpglPosistion> bossPositions = lpglPositionDao.selectInNames(posisitionName);
		if (bossPositions.isEmpty()) return new ArrayList<>();
		List<Long> positionIds = bossPositions.stream()
			.map(a -> a.getId()).collect(Collectors.toList());
		List<TLpglUserPosistion> lpglUserPosistions = lpglUserPositionDao.selectInPositionIds(positionIds);
		if (lpglUserPosistions.isEmpty()) return new ArrayList<>();
		List<Long> userIds = lpglUserPosistions.stream()
			.map(TLpglUserPosistion::getUserId)
			.distinct()
			.collect(Collectors.toList());
		List<TLpglUser> tLpglUsers = lpglUserDao.selectInIds(userIds);
		return tLpglUsers.stream()
			.map(a -> {
				TLpglPosistion position = getPosition(a.getId());
				if (position != null) {
					a.setPositionName(position.getPosisitionName());
				}
				return a;
			}).collect(Collectors.toList());
	}

}
