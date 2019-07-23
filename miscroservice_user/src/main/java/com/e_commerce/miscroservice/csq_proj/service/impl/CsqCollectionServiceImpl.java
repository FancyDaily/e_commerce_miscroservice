package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.dao.CsqCollectionDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserCollection;
import com.e_commerce.miscroservice.csq_proj.service.CsqCollectionService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqCollectionVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 收藏表
 * @ClassName CsqCollectionServiceImpl
 * @Auhor huangyangfeng
 * @Date 2019-06-16 10:54
 * @Version 1.0
 */
@Transactional(rollbackFor = Throwable.class)
@Service
public class CsqCollectionServiceImpl implements CsqCollectionService {
	@Autowired
	private CsqServiceDao csqServiceDao;
	@Autowired
	private CsqCollectionDao csqCollectionDao;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Object clickCollection(Long serviceId, Integer userId) {
		TCsqService tCsqService = csqServiceDao.findOne(serviceId);
		if (tCsqService==null){
			throw new  MessageException("项目不存在");
		}
		TCsqUserCollection in = new TCsqUserCollection();

		TCsqUserCollection csqUserCollection = csqCollectionDao.findOne(serviceId,Long.valueOf(userId));
		if (csqUserCollection==null){
			in.setServiceId(serviceId);
			in.setUserId(Long.valueOf(userId));

			Integer i = csqCollectionDao.insert(in);
			if (i==0){
				throw new MessageException("收藏失败");
			}
		}else {
			in.setId(csqUserCollection.getId());
			if (csqUserCollection.getIsValid().equals(AppConstant.IS_VALID_YES)){
				in.setIsValid(AppConstant.IS_VALID_NO);
			}
			if (csqUserCollection.getIsValid().equals(AppConstant.IS_VALID_NO)){
				in.setIsValid(AppConstant.IS_VALID_YES);
			}
			Integer i = csqCollectionDao.update(in);
			if (i==0){
				throw new MessageException("收藏失败");
			}
		}


		return "success";
	}

	@Override
	public QueryResult<CsqCollectionVo> collectionList(Integer pageNum, Integer pageSize, Integer userId) {
		QueryResult<CsqCollectionVo> queryResult = new QueryResult<>();

		Page<Object> page = PageHelper.startPage(pageNum,pageSize);
		List<TCsqUserCollection> li = csqCollectionDao.findAll(Long.valueOf(userId));

		List<CsqCollectionVo> list = new ArrayList<>();
		if (li==null||li.size()==0){
			queryResult.setTotalCount(page.getTotal());
			queryResult.setResultList(list);
			return queryResult;
		}
		List<Long> serviceIdList = new ArrayList<>();
		li.forEach(tCsqUserCollection -> {
			serviceIdList.add(tCsqUserCollection.getServiceId());
		});
		List<TCsqService> serviceList = csqServiceDao.findAll(serviceIdList);
		serviceList.forEach(csqService->{
			CsqCollectionVo csqCollectionVo = new CsqCollectionVo();
			csqCollectionVo.setDesc(csqService.getDescription());
			csqCollectionVo.setDetailPic(csqService.getDetailPic());
			csqCollectionVo.setName(csqService.getName());
			csqCollectionVo.setPurpose(csqService.getPurpose());
			csqCollectionVo.setRecordNo(csqService.getRecordNo());
			csqCollectionVo.setServiceId(csqService.getId());
			csqCollectionVo.setSumTotalIn(csqService.getSumTotalIn());
			csqCollectionVo.setSurplusAmount(csqService.getSurplusAmount());
			csqCollectionVo.setType(csqService.getType());
			csqCollectionVo.setDonateCnt(csqService.getDonaterCnt());	//人数
			csqCollectionVo.setDonatePercent(NumberFormat.getPercentInstance().format(csqService.getExpectedAmount() == 0? 0 :csqService.getSumTotalIn() / csqService.getExpectedAmount()).replaceAll("%", ""));//进度
			list.add(csqCollectionVo);
		});

		queryResult.setTotalCount(page.getTotal());
		queryResult.setResultList(list);
		return queryResult;
	}

	@Override
	public boolean isCollection(Long userId, Long serviceId) {
		boolean flag = false;
		TCsqUserCollection one = csqCollectionDao.findOne(serviceId, userId);
		if(one!=null) {
			flag = true;
		}
		return flag;
	}
}
