package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.*;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglCertEnum;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglCustomerInfoEnum;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglHouseEnum;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglRoleEnum;
import com.e_commerce.miscroservice.lpglxt_proj.po.*;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglCertService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglPositionService;
import com.e_commerce.miscroservice.lpglxt_proj.utils.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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

	@Autowired
	private LpglPositionService lpglPositionService;

	@Autowired
	private LpglUserPositionDao lpglUserPositionDao;

	@Autowired
	private LpglPositionDao lpglPositionDao;

	@Autowired
	private LpglPositionRoleDao lpglPositionRoleDao;

	@Autowired
	private LpglRoleDao lpglRoleDao;

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
	public void commitCert(Long userId, Long houseId, Integer type, Double disCountPrice, String description) {
		disCountPrice = disCountPrice <= 0? disCountPrice=null: disCountPrice;
		TLpglCert build = TLpglCert.builder()
			.houseId(houseId)
			.type(type)
			.discountPrice(disCountPrice)
			.description(description).build();
		lpglCertDao.insert(build);

		//递交审核时，根据类别，向相应负责人发送通知
		List<String> roleNames = null;
		List<String> names = TlpglRoleEnum.getNames(3);
		switch (TlpglCertEnum.getType(type)) {
			case TYPE_SOLDOUTREQUEST:
				//1.递交售卖完成审核 -> TODO 通知销售经理、财务 level3除了市场角色 ;总经理、总经办 level1
				roleNames = names.stream()
					.filter(a -> !"市场经理".equals(a)).collect(Collectors.toList());

				List<String> names1 = TlpglRoleEnum.getNames(1);
				roleNames.addAll(names1);

				break;
			case TYPE_PRICEDISCOUNT:
				//2.申请优惠 -> TODO 通知市场经理、销售经理 level3除了财务角色
				roleNames = names.stream()
					.filter(a -> !"财务经理".equals(a)).collect(Collectors.toList());

				break;
			case TYPE_CUSTOMER:
				//3.客户报备 -> TODO 通知前台人员、销售经理、销售主管 level3销售经理 level4销售主管、前台
				roleNames = names.stream()
					.filter(a -> Arrays.asList(new String[]{"前台人员", "销售经理", "销售主管"}).contains(a)).collect(Collectors.toList());

				List<String> names2 = TlpglRoleEnum.getNames(4);
				names2 = names2.stream()
					.filter(a -> Arrays.asList(new String[]{"销售主管", "前台"}).contains(a)).collect(Collectors.toList());
				roleNames.addAll(names2);

				break;
		}
		//根据角色列表找到职位，找到用户
		//对以下列表判isEmpty()
		List<TLpglRole> tLpglRoles = roleNames == null || roleNames.isEmpty()? new ArrayList<>() : lpglRoleDao.selectInNames(roleNames);
		List<Long> roleIds = tLpglRoles.stream()
			.map(TLpglRole::getId).collect(Collectors.toList());
		List<TLpglPosistionRole> lpglPosistionRoles = roleIds.isEmpty()? new ArrayList<>() : lpglPositionRoleDao.selectInRoleIds(roleIds);
		List<Long> positionIds = lpglPosistionRoles.stream()
			.map(TLpglPosistionRole::getPosisitionId)
			.distinct().collect(Collectors.toList());
		List<TLpglUserPosistion> userPosistions = positionIds.isEmpty()? new ArrayList<>() : lpglUserPositionDao.selectInPositionIds(positionIds);
		List<Long> userIds = userPosistions.stream()
			.map(TLpglUserPosistion::getUserId)
			.distinct().collect(Collectors.toList());
		List<TLpglUser> users = userIds.isEmpty()? new ArrayList<>() : lpglUserDao.selectInIds(userIds);
		List<String> openids = users.stream()
			.map(TLpglUser::getVxOpenId).collect(Collectors.toList());
		//TODO 发送模版消息，针对不同类型有不同的页面，可能有不同的消息内容

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
				Double discountPrice = tLpglCert.getDiscountPrice();
				TLpglPosistion position = lpglPositionService.getPosition(userId);
				if(position == null || position.getDiscountCredit() < discountPrice) {
					throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "超过审核权限!");
					/*
						//获取直属上级
						TLpglPosistion higherPosition = lpglPositionService.getHigherPosition(position);
						if(higherPosition == null) {
							throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "已超过最高审核权限!");
						}
						List<String> openIds = getOpenIds(higherPosition);
						//TODO 发送通知
						for(String openId: openIds) {
						*//*HashMap<String, WxUtil.TemplateData> templateDatas = new HashMap<>();
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
						WxUtil.publicNotify(openId, templateId, url, templateDatas);*//*
						}

						throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "超过您的审核权限，现已通知更高级别处理!");
					*/
				}

				tLpglHouse.setDisCountStatus(!certPass ? TlpglHouseEnum.DISCOUNT_STAUTS_NO.getCode() : TlpglHouseEnum.DISCOUNT_STATUS_YES.getCode());
				if (certPass) {
					tLpglHouse.setDisCountPrice(discountPrice);
				}
				//TODO 消息通知：审核结果抄送销售经理确认 => level = 3
				Integer level = 3;
				TLpglPosistion position1 = lpglPositionDao.getPosition(level);
				List<String> openIds = getOpenIds(position1);

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
				//TODO 消息通知：审核结果抄送最高级领导[总经理,总经办] => level = 1
				//获得总经理和总经办的openid
				level = 1;
				List<String> theOpenIds = getOpenIds(lpglPositionDao.getPosition(level));

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
			case STATUS_INITAIL:
				break;
			case STATUS_PASS:
				break;
			case STATUS_REFUSE:
				break;
		}
		lpglHouseDao.update(tLpglHouse);
	}

	private List<String> getOpenIds(TLpglPosistion higherPosition) {
		if(higherPosition == null) return new ArrayList<>();
		List<TLpglUserPosistion> userPosistions = lpglUserPositionDao.selectByPositionId(higherPosition.getId());
		List<Long> userIds = userPosistions.stream()
			.map(TLpglUserPosistion::getUserId).collect(Collectors.toList());
		List<TLpglUser> lpglUsers = lpglUserDao.selectInIds(userIds);
		List<String> openIds = lpglUsers.stream()
			.filter(a -> !StringUtil.isEmpty(a.getVxOpenId()))
			.map(TLpglUser::getVxOpenId).collect(Collectors.toList());
		return openIds;
	}
}
