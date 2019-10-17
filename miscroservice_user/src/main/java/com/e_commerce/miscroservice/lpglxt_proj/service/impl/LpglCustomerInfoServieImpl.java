package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglCertDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglCustomerInfoDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglHouseDao;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglCertEnum;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglCustomerInfoEnum;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCustomerInfos;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglCustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:05
 */
@Service
public class LpglCustomerInfoServieImpl implements LpglCustomerInfoService {

	@Autowired
	private LpglCertDao lpglCertDao;

	@Autowired
	private LpglHouseDao lpglHouseDao;

	@Autowired
	private LpglCustomerInfoDao lpglCustomerInfoDao;

	@Override
	public HashMap<String, Object> list(Integer status, Integer pageNum, Integer pageSize, Long estateId, Integer isDone, String area, String department, boolean isToday) {
		//十五天有效
		Long timeStamp = System.currentTimeMillis() - 15L * 24 * 60 * 60 * 1000;
//		List<TLpglCustomerInfos> list = TlpglCustomerInfoEnum.STATUS_VALID.getCode() == status? lpglCustomerInfoDao.selectByStatusAndGteUpdateTimePage(status, timeStamp, pageNum, pageSize) : lpglCustomerInfoDao.selectByStatusPage(status, pageNum, pageSize);
		List<TLpglCustomerInfos> list = lpglCustomerInfoDao.selectByStatusAndEstateIdAndIsDoneAndAreAndDepartmentAndIsToday(status, estateId, isDone, area, department, isToday);
		pageNum --;
		//手动分片
		int total = list.size();	//总数
		long isDoneCnt = list.stream()	//带看总数
			.filter(a -> TlpglCustomerInfoEnum.IS_DONE_YES.getCode() == a.getIsDone()).count();
		list = list.stream()
			.skip(pageNum * pageSize)
			.limit(pageNum * pageSize + pageSize).collect(Collectors.toList());


		//处理手机号敏感信息
		list = list.stream()
			.map(a -> {
				String telephone = a.getTelephone();
				a.setTelephone(replaceTelephone(telephone));
				return a;
			}).collect(Collectors.toList());

		lpglCustomerInfoDao.updateByStatusAndLtUpdateTimePage(status, timeStamp);	//清理失效
		HashMap<String, Object> res = new HashMap<>();
		QueryResult result = PageUtil.buildQueryResult(list, total);
		res.put("queryResult", result);
		res.put("isDoneCnt", isDoneCnt);
		return res;
	}

	private static String replaceTelephone(String telephone) {
		if(telephone != null && telephone.length() > 10) {
			char[] split = telephone.toCharArray();
			for(int i = 3; i < split.length - 4; i++) {
				split[i] = '*';
			}
			return new String(split);
		}
		return telephone;
	}

	@Override
	public void commit(Long id, Long houseId, String telephone, String description) {
		boolean isModify = id != null;
		Long customerId = null;
		if(isModify) {	//修改
			TLpglCustomerInfos tLpglCustomerInfos = lpglCustomerInfoDao.selectByPrimaryKey(id);
			customerId = tLpglCustomerInfos.getId();
			boolean unpass = !tLpglCustomerInfos.getTelephone().equals(telephone);
			if(unpass) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "输入的手机号码与报备信息不匹配!");
			}
			tLpglCustomerInfos.setStatus(TlpglCustomerInfoEnum.STATUS_INVALID.getCode());	//待审核
			tLpglCustomerInfos.setDescription(description);
			lpglCustomerInfoDao.update(tLpglCustomerInfos);
		} else {
			TLpglHouse tLpglHouse = houseId == null? null: lpglHouseDao.selectByPrimaryKey(houseId);
			TLpglCustomerInfos.TLpglCustomerInfosBuilder builder = TLpglCustomerInfos.builder()
				.telephone(telephone)
				.description(description);
			if(tLpglHouse != null) {
				builder.estateId(tLpglHouse.getEstateId())
					.houseId(houseId)
					.buildingNum(tLpglHouse.getBuildingNum())
					.groupName(tLpglHouse.getGroupName())
					.floorNum(tLpglHouse.getFloorNum())
					.houseNum(tLpglHouse.getHouseNum());
			}
			TLpglCustomerInfos build = builder.build();
			lpglCustomerInfoDao.insert(build);
			customerId = build.getId() == null? customerId: build.getId();
		}
		final Long customerIdFinal = customerId;

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				//插入一条 '审核' 记录
				TLpglCert toInserter = TLpglCert.builder()
					.customerInfoId(customerIdFinal)
					.houseId(houseId)
					.type(TlpglCertEnum.TYPE_CUSTOMER.getCode())
					.description(description).build();
				lpglCertDao.insert(toInserter);
			}
		});
	}

	@Override
	public void remove(Long id) {
		lpglCustomerInfoDao.remove(id);
	}
}
