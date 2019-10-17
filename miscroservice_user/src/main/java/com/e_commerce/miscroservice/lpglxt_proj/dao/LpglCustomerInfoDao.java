package com.e_commerce.miscroservice.lpglxt_proj.dao;

import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCustomerInfos;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:47
 */
public interface LpglCustomerInfoDao {

	TLpglCustomerInfos selectByPrimaryKey(Long customerInfoId);

	int update(TLpglCustomerInfos tLpglCustomerInfos);

	List<TLpglCustomerInfos> selectByStatusPage(Integer status, Integer pageNum, Integer pageSize);

	int insert(TLpglCustomerInfos... build);

	int insert(List<TLpglCustomerInfos> list);

	List<TLpglCustomerInfos> selectByStatusAndGteUpdateTimePage(Integer status, Long timeStamp, Integer pageNum, Integer pageSize);

	int updateByStatusAndLtUpdateTimePage(Integer status, Long timeStamp);

	int remove(Long id);

	List<TLpglCustomerInfos> selectByStatusAndEstateIdAndIsDoneAndAreAndDepartmentAndIsToday(Integer status, Long estateId, Integer isDone, String area, String department, boolean isToday);
}
