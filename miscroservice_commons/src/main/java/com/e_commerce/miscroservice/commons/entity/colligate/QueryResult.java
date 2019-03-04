package com.e_commerce.miscroservice.commons.entity.colligate;

import java.util.List;

public class QueryResult<E> {

	private List<E> resultList;
	private Long totalCount = 0l;	
	
	public List<E> getResultList() {
		return resultList;
	}

	public void setResultList(List<E> resultList) {
		this.resultList = resultList;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	
	

}
