package com.e_commerce.miscroservice.lpglxt_proj.dao;

import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUserPosistion;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:47
 */
public interface LpglUserPositionDao {

	List<TLpglUserPosistion> selectByUserId(Long userId);

	List<TLpglUserPosistion> selectByPositionId(Long id);

	List<TLpglUserPosistion> selectInPositionIds(List<Long> positionIds);
}
