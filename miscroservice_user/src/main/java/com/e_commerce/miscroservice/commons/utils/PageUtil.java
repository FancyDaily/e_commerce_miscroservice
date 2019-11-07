package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.entity.colligate.*;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxScoreRecordVo;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-09 16:44
 */
public class PageUtil<T> {

	public static <T> QueryResult buildQueryResult(List<T> vos, long total) {
		QueryResult<T> queryResult = new QueryResult<>();

		queryResult.setResultList(vos);
		queryResult.setTotalCount(total);

		return queryResult;
	}

	public static <T> QueryResult buildQueryResult() {
		return new QueryResult();
	}

	public static Page prePage(Integer pageNum, Integer pageSize) {
		pageNum = prePageNum(pageNum);
		pageSize = prePageSize(pageSize);
		Page page = new Page();
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		return page;
	}

	private static Integer prePageSize(Integer pageSize) {
		pageSize = pageSize == null? 0: pageSize;
		return pageSize;
	}

	public static Integer prePageNum(Integer pageNum) {
		pageNum = pageNum == null? 0: pageNum;
		return pageNum;
	}

	public static Page prePage(Page page) {
		return prePage(page.getPageNum(), page.getPageSize());
	}

	public static MybatisPlusBuild dealWithPage(MybatisPlusBuild build, Page page) {
		if(page == null) return build;
		IdUtil.setTotal(build);
		build = build.page(page.getPageNum(), page.getPageSize());
		return build;
	}

	public static MybatisPlusBuild pageBuild(MybatisPlusBuild eq, Integer pageNum, Integer pageSize) {
		IdUtil.setTotal(eq);
		return eq.page(pageNum, pageSize);
	}

	public static <T> QueryResult buildQueryResult(List<T> vos) {
		return buildQueryResult(vos, IdUtil.getTotal());
	}
}
