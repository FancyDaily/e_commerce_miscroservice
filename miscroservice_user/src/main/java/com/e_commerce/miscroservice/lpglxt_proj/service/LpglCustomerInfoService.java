package com.e_commerce.miscroservice.lpglxt_proj.service;

import java.util.HashMap;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:40
 */
public interface LpglCustomerInfoService {


	HashMap<String, Object> list(Integer integer, Integer status, Integer pageNum, Long estateId, Integer pageSize, String area, String department, boolean isToday);

	void commit(Long estateId, Long id, Long houseId, String telephone, String description);

	void remove(Long id);
}
