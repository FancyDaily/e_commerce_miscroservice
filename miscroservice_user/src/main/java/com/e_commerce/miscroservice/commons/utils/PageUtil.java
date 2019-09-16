package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.entity.colligate.*;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-09 16:44
 */
public class PageUtil {

	public static <T> QueryResult buildQueryResult(List<T> vos, long total) {
		QueryResult<T> queryResult = new QueryResult<>();

		queryResult.setResultList(vos);
		queryResult.setTotalCount(total);

		return queryResult;
	}

	public static Page prePage(Integer pageNum, Integer pageSize) {
		pageNum = prePageNum(pageNum);
		pageSize = prePageSize(pageSize);
		return Page.builder()
					.pageNum(pageNum)
					.pageSize(pageSize).build();
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
}
