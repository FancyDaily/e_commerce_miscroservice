package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.*;
import com.e_commerce.miscroservice.lpglxt_proj.enums.*;
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
		String firstData = "您有一个待处理请求";
		TlpglCertEnum theEnum = null;
		String lastData = "点击查看或处理";
		String url = "";
		String content = "";
		switch (theEnum=TlpglCertEnum.getType(type)) {
			case TYPE_SOLDOUTREQUEST:
				//1.递交售卖完成审核 -> TODO 通知销售经理、财务 level3除了市场角色 ;总经理、总经办 level1
//				TODO url = "";
//				TODO content = "";
				roleNames = getRoleNamesExceptName(names, "市场经理");

				List<String> names1 = TlpglRoleEnum.getNames(1);
				roleNames.addAll(names1);

				break;
			case TYPE_PRICEDISCOUNT:
				//2.申请优惠 -> TODO 通知市场经理、销售经理 level3除了财务角色
//				TODO url = "";
//				TODO content = "";
				roleNames = getRoleNamesExceptName(names, "财务经理");

				break;
			case TYPE_CUSTOMER:
				//3.客户报备 -> TODO 通知前台人员、销售经理、销售主管 level3销售经理 level4销售主管、前台
//				TODO url = "";
//				TODO content = "";
				roleNames = getRoleNamesInArray(names, "前台人员", "销售经理", "销售主管");

				List<String> names2 = TlpglRoleEnum.getNames(4);
				names2 = getRoleNamesInArray(names2, "销售主管", "前台");
				roleNames.addAll(names2);

				break;
		}
		//根据角色列表找到职位，找到用户
		//对以下列表判isEmpty()
		List<String> openIds = getOpenIds(roleNames);
		for(String openId: openIds) {
			//TODO 发送模版消息，针对不同类型有不同的页面，可能有不同的消息内容
			HashMap<String, WxUtil.TemplateData> templateDatas = new HashMap<>();
//			String tipMsg = "";
			//String openId, String firstData, String orderNo, String orderType, String content, String lastData, String url
			WxUtil.sendPublicWaitMsg(openId, firstData, build.getId().toString(), theEnum.getMsg(), content, lastData, url);
		}
	}

	private List<String> getOpenIds(List<String> roleNames) {
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
		return users.stream()
			.map(TLpglUser::getVxOpenId).collect(Collectors.toList());
	}

	private List<String> getRoleNamesInArray(List<String> names, String... nameClude) {
		List<String> roleNames;
		roleNames = names.stream()
			.filter(a -> Arrays.asList(nameClude).contains(a)).collect(Collectors.toList());
		return roleNames;
	}

	private List<String> getRoleNamesExceptName(List<String> names, String name) {
		List<String> roleNames;
		roleNames = names.stream()
			.filter(a -> !name.equals(a)).collect(Collectors.toList());
		return roleNames;
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
		List<String> openIds = new ArrayList<>();
		String firstData = "您有一个待处理请求";
		TlpglCertEnum theEnum = null;
		String lastData = "点击查看或处理";
		String url = "";
		String content = "";
		switch (type) {
			case TYPE_PRICEDISCOUNT:
				url = TlpglServMsgEnum.TYPE_PRICEDISCOUNT.getUrl();
				Double discountPrice = tLpglCert.getDiscountPrice();
				TLpglPosistion position = lpglPositionService.getPosition(userId);
				if(position == null || position.getDiscountCredit() < discountPrice) {
					throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "超过审核权限!");
				/*	//获取直属上级
					TLpglPosistion higherPosition = lpglPositionService.getHigherPosition(position);
					if(higherPosition == null) {
						throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "已超过最高审核权限!");
					}
					List<String> openIds = getOpenIds(higherPosition);
					// 发送通知
					for(String openId: openIds) {

//						WxUtil.publicNotify(openId, templateId, url, templateDatas);
					}

					throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "超过您的审核权限，现已通知更高级别处理!");*/
				}

				tLpglHouse.setDisCountStatus(!certPass ? TlpglHouseEnum.DISCOUNT_STAUTS_NO.getCode() : TlpglHouseEnum.DISCOUNT_STATUS_YES.getCode());
				if (certPass) {
					tLpglHouse.setDisCountPrice(discountPrice);
				}
				// 消息通知：审核结果抄送销售经理确认 => level = 3
				Integer level = 3;
				openIds = getOpenIds(level, "销售经理");
				//TODO url
//				url = "";
//				content = "";

				break;
			case TYPE_SOLDOUTREQUEST:
				url = TlpglServMsgEnum.TYPE_SOLDOUTREQUEST.getUrl();
				tLpglHouse.setStatus(!certPass ? TlpglHouseEnum.STATUS_INITAIL.getCode() : TlpglHouseEnum.STATUS_SOLDOUT.getCode());
				//TODO 消息通知：审核结果抄送最高级领导[总经理,总经办] => level = 1
				//获得总经理和总经办的openid
				level = 1;
//				List<String> theOpenIds = getOpenIds(lpglPositionDao.getPosition(level));
				List<String> theOpenIds = getOpenIds(level, "总经理", "总经办");
//				url = "";
//				content = "";

				break;
			case TYPE_CUSTOMER:
				Long customerInfoId = tLpglCert.getCustomerInfoId();
				TLpglCustomerInfos tLpglCustomerInfos = lpglCustomerInfoDao.selectByPrimaryKey(customerInfoId);
				tLpglCustomerInfos.setStatus(!certPass ? TlpglCustomerInfoEnum.STATUS_INVALID.getCode() : TlpglCustomerInfoEnum.STATUS_VALID.getCode());
				lpglCustomerInfoDao.update(tLpglCustomerInfos);
				return;
		}
		//发送通知
		for(String openId: openIds) {
			//TODO 发送模版消息，针对不同类型有不同的页面，可能有不同的消息内容
			HashMap<String, WxUtil.TemplateData> templateDatas = new HashMap<>();
//			String tipMsg = "";
			//String openId, String firstData, String orderNo, String orderType, String content, String lastData, String url
			WxUtil.sendPublicWaitMsg(openId, firstData, tLpglCert.getId().toString(), type.getMsg(), content, lastData, url);
		}


		lpglHouseDao.update(tLpglHouse);
	}

	private List<String> getOpenIds(Integer level, String... nameParams) {
		List<String> names = TlpglRoleEnum.getNames(level);
		List<String> roleNames = getRoleNamesInArray(names, nameParams);
		return getOpenIds(roleNames);
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
