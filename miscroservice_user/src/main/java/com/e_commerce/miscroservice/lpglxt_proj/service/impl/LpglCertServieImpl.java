package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglCertDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglCustomerInfoDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglHouseDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglUserDao;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglCertEnum;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglCustomerInfoEnum;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglHouseEnum;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCustomerInfos;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUser;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglCertService;
import com.e_commerce.miscroservice.lpglxt_proj.utils.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:05
 */
@Service
public class LpglCertServieImpl implements LpglCertService {

	@Autowired
	private LpglCertDao lpglCertDao;

	@Autowired
	private LpglHouseDao lpglHouseDao;

	@Autowired
	private LpglCustomerInfoDao lpglCustomerInfoDao;

	@Autowired
	private LpglUserDao lpglUserDao;

	@Override
	public QueryResult underCertList(Integer type, Integer status, Integer pageNum, Integer pageSize, boolean isToday, Long groupId) {
		List<Long> userIds = null;
		if(groupId != null) {
			List<TLpglUser> tLpglUsers = lpglUserDao.selectByGroupId(groupId);
			userIds = tLpglUsers.stream()
				.map(TLpglUser::getId).collect(Collectors.toList());
		}
		List<TLpglCert> tLpglCerts = lpglCertDao.selectByTypeAndStatusInApplyUserIdsPage(type, status, pageNum, pageSize, isToday, userIds);
		long total = IdUtil.getTotal();

		return PageUtil.buildQueryResult(tLpglCerts, total);
	}

	@Override
	public void cert(Long userId, Long certId, Integer status) {
		boolean certPass = TlpglCertEnum.STATUS_PASS.getCode() == status;
		TLpglCert tLpglCert = lpglCertDao.selectByPrimaryKey(certId);
		dealWithLpglHouse(userId, tLpglCert, certPass);

		tLpglCert.setCertUserId(userId);
		tLpglCert.setStatus(status);
		lpglCertDao.update(tLpglCert);
	}

	@Override
	public void commitCert(Long houseId, Integer type, Double disCountPrice, String description) {
		TLpglCert build = TLpglCert.builder()
			.houseId(houseId)
			.type(type)
			.discountPrice(disCountPrice)
			.description(description).build();
		lpglCertDao.insert(build);
	}

	public static void main(String[] args) {
		String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now());
		System.out.println(format);
	}

	private void dealWithLpglHouse(Long userId, TLpglCert tLpglCert, boolean certPass) {
		/*boolean pass = TlpglCertEnum.TYPE_PRICEDISCOUNT.getCode() != tLpglCert.getType();
		if(pass) return;*/
		TlpglCertEnum type = TlpglCertEnum.getType(tLpglCert.getType());
		if (type == null) {
			return;
		}
		Long houseId = tLpglCert.getHouseId();
		TLpglHouse tLpglHouse = lpglHouseDao.selectByPrimaryKey(houseId);
		switch (type) {
			case TYPE_PRICEDISCOUNT:
				//确认当前用户，所处职位的优惠额度, 如果不够审批，则提示//TODO 或通知上级（将上级信息返回给前端，用于可选发送通知）
				//TODO 优惠金额大于自己可支配额度的，递交上级处理(通知上级)


				tLpglHouse.setDisCountStatus(!certPass ? TlpglHouseEnum.DISCOUNT_STAUTS_NO.getCode() : TlpglHouseEnum.DISCOUNT_STATUS_YES.getCode());
				if (certPass) {
					tLpglHouse.setDisCountPrice(tLpglHouse.getDisCountPrice());
				}
				//TODO 消息通知：审核结果抄送销售经理确认
				List<String> openIds = new ArrayList<>();

				//发送通知消息
				for(String openId: openIds) {
					/*HashMap<String, WxUtil.TemplateData> templateDatas = new HashMap<>();
					String tipMsg = "";
					templateDatas.put("first", new WxUtil.TemplateData(tipMsg));
//					templateDatas.put("keyword1", new WxUtil.TemplateData(productName));
//					templateDatas.put("keyword2", new WxUtil.TemplateData(payMoney));
//					templateDatas.put("keyword3", new WxUtil.TemplateData(payerName));
					templateDatas.put("keyword4", new WxUtil.TemplateData(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now())));
//					templateDatas.put("keyword5", new WxUtil.TemplateData(orderNo));
					templateDatas.put("keyword6", new WxUtil.TemplateData("点击查看详情"));
					String templateId = "";
					String url = "";
					WxUtil.publicNotify(openId, templateId, url, templateDatas);*/
				}

				break;
			case TYPE_SOLDOUTREQUEST:
				tLpglHouse.setStatus(!certPass ? TlpglHouseEnum.STATUS_INITAIL.getCode() : TlpglHouseEnum.STATUS_SOLDOUT.getCode());
				//TODO 消息通知：审核结果抄送最高级领导[总经理,总经办]
				//获得总经理和总经办的openid
				List<String> theOpenIds = new ArrayList<>();

				//发送通知消息
				for(String openId: theOpenIds) {
					HashMap<String, WxUtil.TemplateData> templateDatas = new HashMap<>();
					String tipMsg = "";
					templateDatas.put("first", new WxUtil.TemplateData(tipMsg));
//					templateDatas.put("keyword1", new WxUtil.TemplateData(productName));
//					templateDatas.put("keyword2", new WxUtil.TemplateData(payMoney));
//					templateDatas.put("keyword3", new WxUtil.TemplateData(payerName));
					templateDatas.put("keyword4", new WxUtil.TemplateData(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now())));
//					templateDatas.put("keyword5", new WxUtil.TemplateData(orderNo));
					templateDatas.put("keyword6", new WxUtil.TemplateData("点击查看详情"));
					String templateId = "";
					String url = "";
					WxUtil.publicNotify(openId, templateId, url, templateDatas);
				}

				break;
			case TYPE_CUSTOMER:
				Long customerInfoId = tLpglCert.getCustomerInfoId();
				TLpglCustomerInfos tLpglCustomerInfos = lpglCustomerInfoDao.selectByPrimaryKey(customerInfoId);
				tLpglCustomerInfos.setStatus(!certPass ? TlpglCustomerInfoEnum.STATUS_INVALID.getCode() : TlpglCustomerInfoEnum.STATUS_VALID.getCode());
				lpglCustomerInfoDao.update(tLpglCustomerInfos);
				return;
		}
		lpglHouseDao.update(tLpglHouse);
	}
}
