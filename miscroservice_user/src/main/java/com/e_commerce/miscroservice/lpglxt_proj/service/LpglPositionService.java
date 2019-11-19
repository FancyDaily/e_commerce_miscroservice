package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglPosistion;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUser;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:39
 */
public interface LpglPositionService {

	void modify(Long positionId, Double discountCredit);

	TLpglPosistion getPosition(Long userId);

	TLpglPosistion getHigherPosition(TLpglPosistion position);

	List<TLpglUser> findUsersByPositionNames(List<String> posisitionName);
}
