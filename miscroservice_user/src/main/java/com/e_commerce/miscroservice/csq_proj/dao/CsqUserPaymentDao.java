package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 10:35
 */
public interface CsqUserPaymentDao {

	List<TCsqUserPaymentRecord> selectByEntityIdAndEntityTypeAndInOut(Long fundId, int toCode, int toCode1);

	int insert(TCsqUserPaymentRecord... build);

	int insert(List<TCsqUserPaymentRecord> builds);

	int multiInsert(List<TCsqUserPaymentRecord> builds);

	List<TCsqUserPaymentRecord> selectByEntityIdAndEntityTypeAndInOutDesc(Long serviceId, int toCode, int toCode1);

	List<TCsqUserPaymentRecord> selectInOrderIdsAndInOut(List<Long> orderIds, int toCode);

	List<TCsqUserPaymentRecord> selectByUserIdAndInOrOut(Long userId, int toCode);

	List<TCsqUserPaymentRecord> selectInOrderIdsAndInOutDesc(List<Long> orderIds, int toCode);

	List<TCsqUserPaymentRecord> selectByEntityIdAndEntityTypeAndInOutDescPage(Long serviceId, int toCode, int toCode1, int pageNum, int pageSize);

	List<TCsqUserPaymentRecord> selectInOrderIdsAndInOutDescPage(Integer pageNum, Integer pageSize, List<Long> orderIds, int toCode);

	List<TCsqUserPaymentRecord> selectByNotNullExtend();

	int update(List<TCsqUserPaymentRecord> toUpdater);

	List<TCsqUserPaymentRecord> selectInEntityType(int... entityType);

	TCsqUserPaymentRecord selectByPrimaryKey(Long recordId);

	List<TCsqUserPaymentRecord> selectInPrimaryKeys(List<Long> recordIds);

	List<TCsqUserPaymentRecord> selectInOrderIds(List<Long> orderIds);

	MybatisPlusBuild baseBuild();

	List<TCsqUserPaymentRecord> selectWithBuildPage(MybatisPlusBuild baseBuild, Integer pageNum, Integer pageSize);

	List<TCsqUserPaymentRecord> selectWithBuild(MybatisPlusBuild baseBuild);

	List<TCsqUserPaymentRecord> selectInEntityIdsAndEntityTypeAndInOrOut(List<Long> serviceIds, int entityType, int iOrOut);

	List<TCsqUserPaymentRecord> selectInEntityIdsAndEntityTypeAndInOrOutDesc(List<Long> serviceIds, int entityType, int iOrOut);
}
