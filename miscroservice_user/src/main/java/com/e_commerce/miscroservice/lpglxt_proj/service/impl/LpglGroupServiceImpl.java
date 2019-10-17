package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.*;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglCertEnum;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglCustomerInfoEnum;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglHouseEnum;
import com.e_commerce.miscroservice.lpglxt_proj.po.*;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglCertService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglGroupService;
import com.e_commerce.miscroservice.lpglxt_proj.utils.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:05
 */
@Service
public class LpglGroupServiceImpl implements LpglGroupService {

	@Autowired
	private LpglCertDao lpglCertDao;

	@Autowired
	private LpglHouseDao lpglHouseDao;

	@Autowired
	private LpglCustomerInfoDao lpglCustomerInfoDao;

	@Autowired
	private LpglUserDao lpglUserDao;

	@Autowired
	private LpglGroupDao lpglGroupDao;

	@Override
	public QueryResult list(Long estateId, Integer pageNum, Integer pageSize) {
		return PageUtil.buildQueryResult(lpglGroupDao.selectByEstateIdPage(estateId, pageNum, pageSize), IdUtil.getTotal());
	}

	@Override
	public void add(Long estateId, String name) {
		TlpglGroup build = TlpglGroup.builder()
			.estateId(estateId)
			.name(name).build();
		lpglGroupDao.insert(build);
	}

	@Override
	public void modify(Long id, String name) {
		TlpglGroup build = TlpglGroup.builder()
			.name(name).build();
		build.setId(id);
		lpglGroupDao.update(build);
	}
}
