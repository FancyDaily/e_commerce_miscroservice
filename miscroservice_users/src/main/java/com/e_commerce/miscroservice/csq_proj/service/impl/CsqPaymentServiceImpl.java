package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqPaymentDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @ClassName CsqPaymentServiceImpl
 * @Auhor huangyangfeng
 * @Date 2019-06-17 15:15
 * @Version 1.0
 */
@Transactional(rollbackFor = Throwable.class)
@Service
public class CsqPaymentServiceImpl implements CsqPaymentService {
	@Autowired
	private CsqPaymentDao csqPaymentDao;
	@Autowired
	private WechatService wechatService;

	@Override
	public QueryResult<TCsqUserPaymentRecord >  findWaters(Integer pageNum, Integer pageSize, Long userId) {
		QueryResult<TCsqUserPaymentRecord> queryResult = new QueryResult<TCsqUserPaymentRecord>();

		Page<Object> page = PageHelper.startPage(pageNum,pageSize);
		List<TCsqUserPaymentRecord> records = csqPaymentDao.findWaters(userId);

		queryResult.setResultList(records);
		queryResult.setTotalCount(page.getTotal());
		return queryResult;
	}

	@Override
	public Map<String,Object>  findMyCertificate(Long recordId, Long userId) {
		Map<String,Object> map = new HashMap<>();
		TCsqUserPaymentRecord record = csqPaymentDao.findWaterById(recordId);
		Double accountMoney = csqPaymentDao.countMoney(userId,1);
		if (record!=null){
			map.put("serviceName",record.getServiceName());
			map.put("name",record.getUser().getName());
			map.put("money",record.getMoney());
			map.put("countMoney",accountMoney);
			// TODO: 2019-06-20  二维码生成
			map.put("code",wechatService.genQRCode(String.valueOf(record.getId()),"跳转连接地址"));
			map.put("time",DateUtil.timeStamp2Date(record.getCreateTime().getTime()));
			map.put("date",DateUtil.timeStamp2Date(record.getCreateTime().getTime(),"yyyy-MM-dd"));
		}
		return map;
	}

	@Override
	public Double countMoney(Long userId, Integer inOut) {
		return csqPaymentDao.countMoney(userId,inOut);
	}
}
