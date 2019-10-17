package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:40
 */
public interface LpglHouseService {

	int save(TLpglHouse tLpglHouse);

	QueryResult list(Long userId, Long estateId, Integer pageNum, Integer pageSize, Long groupId);

	TLpglHouse detail(Long houseId);

	int modify(TLpglHouse obj);

	int add(TLpglHouse obj);

	void remove(Long houseId);

	List<String> recordIn(Long estateId, MultipartFile file, boolean skipModify);
}
