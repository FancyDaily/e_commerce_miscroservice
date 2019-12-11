package com.e_commerce.miscroservice.sdx_proj.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxTrendsDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTrendsPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 发布动态的dao层
 */

@Repository
public class TSdxTrendsDaoImpl implements TSdxTrendsDao {
	private final int PAGE_SIZE = 10;

	@Override
	public int saveTSdxTrendsIfNotExist(TSdxTrendsPo tSdxTrendsPo) {
		return tSdxTrendsPo.save();
	}

	@Override
	public int modTSdxTrends(TSdxTrendsPo tSdxTrendsPo) {
		return tSdxTrendsPo.update(tSdxTrendsPo.build().eq(TSdxTrendsPo::getId, tSdxTrendsPo.getId()));
	}

	@Override
	public int delTSdxTrendsByIds(Long... ids) {
		TSdxTrendsPo tSdxTrendsPo = TSdxTrendsPo.builder().build();
		tSdxTrendsPo.setDeletedFlag(Boolean.TRUE);
		return tSdxTrendsPo.update(tSdxTrendsPo.build().in(TSdxTrendsPo::getId, ids));
	}

	@Override
	public TSdxTrendsPo findTSdxTrendsById(Long id) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxTrendsPo.class);
		build.eq(TSdxTrendsPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxTrendsPo::getId, id);
		return MybatisPlus.getInstance().findOne(TSdxTrendsPo.builder().build(), build);
	}

	@Override
	public List<TSdxTrendsPo> findTSdxTrendsByAll(TSdxTrendsPo tSdxTrendsPo, Integer page, Integer size) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxTrendsPo.class);
		build.eq(TSdxTrendsPo::getDeletedFlag, Boolean.FALSE);
		if (tSdxTrendsPo.getId() == null) {
			if (tSdxTrendsPo.getPrice() != null) {
				build.eq(TSdxTrendsPo::getPrice, tSdxTrendsPo.getPrice());
			}
			if (tSdxTrendsPo.getUserId() != null) {
				build.eq(TSdxTrendsPo::getUserId, tSdxTrendsPo.getUserId());
			}
			if (StringUtils.isNotEmpty(tSdxTrendsPo.getAddress())) {
				build.like(TSdxTrendsPo::getAddress, tSdxTrendsPo.getAddress());
			}
			if (StringUtils.isNotEmpty(tSdxTrendsPo.getBookPic())) {
				build.like(TSdxTrendsPo::getBookPic, tSdxTrendsPo.getBookPic());
			}
			if (StringUtils.isNotEmpty(tSdxTrendsPo.getUserPic())) {
				build.like(TSdxTrendsPo::getUserPic, tSdxTrendsPo.getUserPic());
			}
			if (StringUtils.isNotEmpty(tSdxTrendsPo.getBookName())) {
				build.like(TSdxTrendsPo::getBookName, tSdxTrendsPo.getBookName());
			}
			if (StringUtils.isNotEmpty(tSdxTrendsPo.getBoookPic())) {
				build.like(TSdxTrendsPo::getBoookPic, tSdxTrendsPo.getBoookPic());
			}
			if (tSdxTrendsPo.getFriendId() != null) {
				build.eq(TSdxTrendsPo::getFriendId, tSdxTrendsPo.getFriendId());
			}
			if (tSdxTrendsPo.getLatitude() != null) {
				build.eq(TSdxTrendsPo::getLatitude, tSdxTrendsPo.getLatitude());
			}
			if (StringUtils.isNotEmpty(tSdxTrendsPo.getUserName())) {
				build.like(TSdxTrendsPo::getUserName, tSdxTrendsPo.getUserName());
			}
			if (tSdxTrendsPo.getLongitude() != null) {
				build.eq(TSdxTrendsPo::getLongitude, tSdxTrendsPo.getLongitude());
			}
			if (StringUtils.isNotEmpty(tSdxTrendsPo.getTrentInfo())) {
				build.like(TSdxTrendsPo::getTrentInfo, tSdxTrendsPo.getTrentInfo());
			}
			if (tSdxTrendsPo.getBookInfoId() != null) {
				build.eq(TSdxTrendsPo::getBookInfoId, tSdxTrendsPo.getBookInfoId());
			}
			if (StringUtils.isNotEmpty(tSdxTrendsPo.getFriendName())) {
				build.like(TSdxTrendsPo::getFriendName, tSdxTrendsPo.getFriendName());
			}
			if (tSdxTrendsPo.getScoreDouban() != null) {
				build.eq(TSdxTrendsPo::getScoreDouban, tSdxTrendsPo.getScoreDouban());
			}
			if (page == null) {
				page = 1;
			}
			IdUtil.setTotal(build);
			build.page(page, size == null ? PAGE_SIZE : size);
		} else {
			build.eq(TSdxTrendsPo::getId, tSdxTrendsPo.getId());
		}
		return MybatisPlus.getInstance().findAll(TSdxTrendsPo.builder().build(), build);
	}


}
