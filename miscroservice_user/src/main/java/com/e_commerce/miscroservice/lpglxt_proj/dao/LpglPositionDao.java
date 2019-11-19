package com.e_commerce.miscroservice.lpglxt_proj.dao;

import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglPosistion;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:47
 */
public interface LpglPositionDao {

	int modify(Long positionId, Double discountCredit);

	TLpglPosistion selectByPrimaryKey(Long posistionId);

	List<TLpglPosistion> selectByLevel(Integer level);

	TLpglPosistion getPosition(Integer level);

	List<TLpglPosistion> selectInNames(String... posisitionName);

	List<TLpglPosistion> selectInNames(List<String> posisitionName);
}
