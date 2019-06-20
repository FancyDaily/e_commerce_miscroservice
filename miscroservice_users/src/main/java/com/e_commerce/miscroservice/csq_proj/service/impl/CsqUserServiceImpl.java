package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.service.Token;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqPublishService;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDailyDonateVo;
import com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService;
import com.e_commerce.miscroservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService.DEFAULT_PASS;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:45
 */
@Service
public class CsqUserServiceImpl implements CsqUserService {

	@Autowired
	private CsqUserDao csqUserDao;

	@Autowired
	private AuthorizeRpcService authorizeRpcService;

	@Autowired
	private UserService userService;

	@Autowired
	private CsqUserAuthDao csqUserAuthDao;

	@Autowired
	private CsqPublishService csqPublishService;

	@Autowired
	private CsqKeyValueDao csqKeyValueDao;

	@Autowired
	private CsqServiceDao csqServiceDao;

	@Autowired
	private CsqUserPaymentDao csqUserPaymentDao;

	@Override
	public void checkAuth(TCsqUser user) {
		if (user == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "用户不存在");
		}
		Integer authenticationStatus = user.getAuthenticationStatus();
		if (!UserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "请先进行实名认证!");
		}
	}

	@Override
	public void checkAuth(Long userId) {
		if (userId == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数用户编号为空");
		}
		TCsqUser tCsqUser = csqUserDao.selectByPrimaryKey(userId);
		checkAuth(tCsqUser);
	}

	@Override
	public Map<String, Object> openidLogin(String openid, String uuid) {
		TCsqUser tCsqUser = csqUserDao.selectByVxOpenIdAndAccountType(openid, CsqUserEnum.ACCOUNT_TYPE_PERSON.toCode());
		if (tCsqUser == null) {    //进行注册
			tCsqUser = new TCsqUser();
			tCsqUser.setUuid(uuid);
			tCsqUser = register(tCsqUser);
		}
		//登录
		String token = tCsqUser.getToken();
		if (token == null) {
			tCsqUser = UserUtil.login(tCsqUser, ApplicationEnum.CONGSHANQIAO_APPLICATION.toCode(), authorizeRpcService);
			token = tCsqUser.getToken();
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("token", token);
		resultMap.put("user", tCsqUser);
		return resultMap;
	}

	@Override
	public void bindCellPhone(Long userId, String telephone, String smsCode) {
		//检查短信
		userService.checkSMS(telephone, smsCode);
		//获取用户信息
		TCsqUser tCsqUser = csqUserDao.selectByPrimaryKey(userId);
		Integer accountType = tCsqUser.getAccountType();
		//检查手机号是否已经绑定其他账号
		TCsqUser existUser = csqUserDao.selectByUserTelAndAccountType(telephone, accountType);    //机构与个人的【绑定手机号】平行互不干扰
		if (existUser != null) {
			if (userId.equals(existUser.getId())) {
				return;
			}
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该手机已绑定其他账号，如需解绑，请联系客服!");
		}
		tCsqUser.setUserTel(telephone);
		csqUserDao.updateByPrimaryKey(tCsqUser);
	}

	@Override
	public void sendPersonAuth(TCsqUserAuth csqUserAuth, String smsCode) {
		if (StringUtil.isAnyEmpty(csqUserAuth.getCardId(), csqUserAuth.getName(), csqUserAuth.getPhone())) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "必填参数不全！");
		}
		Long userId = csqUserAuth.getUserId();
		//判断是否已经实名
		TCsqUser tCsqUser = csqUserDao.selectByPrimaryKey(userId);
		Integer authenticationStatus = tCsqUser.getAuthenticationStatus();
		if (CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已经实名过，无需重复实名!");    //或改为return
		}
		csqUserAuth.setStatus(CsqUserAuthEnum.STATUS_UNDER_CERT.getCode());    //待审核(默认)
		csqUserAuthDao.insert(csqUserAuth);
		//TODO 若提交即实名，此处将用户实名状态改变
	}

	@Override
	public TCsqUser findCsqUserById(Long userId) {

		return csqUserDao.selectByPrimaryKey(userId);
	}

	@Override
	public Map<String, Object> loginByTelephone(String telephone, String password, Integer option, String uuid) {
		Map<String, Object> map = new HashMap<>();
		Integer OPTION_PERSON = CsqUserEnum.ACCOUNT_TYPE_PERSON.toCode();
		Integer OPTION_CORP = CsqUserEnum.ACCOUNT_TYPE_COMPANY.toCode();
		//check option
		Integer accountType = option == null ? OPTION_CORP : option;
		if (!Arrays.asList(OPTION_PERSON, OPTION_CORP).contains(option)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数option不正确!");
		}
		TCsqUser tCsqUser = csqUserDao.selectByUserTelAndPasswordAndAccountType(telephone, password, accountType);
		if (tCsqUser == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "手机号或密码错误!");
		}
		tCsqUser.setUuid(uuid);
		tCsqUser = UserUtil.login(tCsqUser, ApplicationEnum.CONGSHANQIAO_APPLICATION.toCode(), authorizeRpcService);
		String token = tCsqUser.getToken();
		map.put("token", token);
		map.put("user", tCsqUser);
		return map;
	}

	@Override
	public void registerAndSubmitCert(String telephone, String validCode, String uuid, TCsqUserAuth csqUserAuth) {
		Map<String, Object> map = new HashMap<>();
		userService.checkSMS(telephone, validCode);
		//用户是否已经注册，若无注册一个(Corp类型
		TCsqUser tCsqUser = csqUserDao.selectByUserTelAndAccountType(telephone, CsqUserEnum.ACCOUNT_TYPE_COMPANY.toCode());
		if (tCsqUser == null) {    //进行注册
			tCsqUser = new TCsqUser();
			tCsqUser.setUuid(uuid);
			tCsqUser = register(tCsqUser);
			return;
		}
		Long corpUserId = tCsqUser.getId();
		//用户是否已经实名过
		Integer authenticationStatus = tCsqUser.getAuthenticationStatus();
		if (CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已经认证过！");
		}
		//用户是否有正在审核的请求，若无插入一个
		TCsqUserAuth tCsqUserAuth = csqUserAuthDao.selectByUserId(corpUserId);
		if (tCsqUserAuth != null && tCsqUserAuth.getStatus() != CsqUserAuthEnum.STATUS_CERT_REFUSE.getCode()) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您存在待审核的记录!");
		}
		//提交审核
		String name = csqUserAuth.getName();
		String licenseId = csqUserAuth.getLicenseId();
		String licensePic = csqUserAuth.getLicensePic();
		if (StringUtil.isAnyEmpty(name, licenseId, licensePic)) {
			throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "必填参数为空!");
		}
		TCsqUserAuth userAuth = TCsqUserAuth.builder().status(CsqUserAuthEnum.STATUS_UNDER_CERT.getCode())
			.licenseId(licenseId)
			.licensePic(licensePic)
			.name(name)
			.build();
		//略去审核流程标记 REMARK
		csqUserAuthDao.insert(userAuth);
	}

	@Override
	public void certCorp(Long userAuthId, Integer option) {
		Integer OPTION_PASS = CsqUserAuthEnum.STATUS_CERT_PASS.getCode();
		Integer OPTION_REFUSE = CsqUserAuthEnum.STATUS_CERT_REFUSE.getCode();
		if (option == null || !Arrays.asList(OPTION_PASS, OPTION_REFUSE).contains(option)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数option不正确!");
		}
		//审核记录状态修改
		TCsqUserAuth userAuth = csqUserAuthDao.selectByPrimaryKey(userAuthId);
		userAuth.setStatus(option);
		csqUserAuthDao.update(userAuth);
		//用户实名状态修改
		Long userId = userAuth.getUserId();
		TCsqUser build = TCsqUser.builder().id(userId)
			.authenticationStatus(CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode())
			.authenticationType(CsqUserEnum.AUTHENTICATION_TYPE_ORG_OR_CORP.toCode()).build();
		csqUserDao.updateByPrimaryKey(build);
	}

	@Override
	public CsqDailyDonateVo dailyDonateDetail(Long userId) {
		Long serviceId = 1l;    //默认
		//查询publish表，获取日推项目信息，没有的话从未达目标值的项目池中抓取，没有的话给默认值
		Map publishName = csqPublishService.getPublishName(CsqPublishEnum.MAIN_KEY_DAILY_DONATE.toCode());
		//确认今天是周几
		long currentTimeMillis = System.currentTimeMillis();
		Integer weekDayInt = DateUtil.getWeekDayInt(currentTimeMillis);
		Long key = Long.valueOf(weekDayInt);
		serviceId = Long.valueOf((String) publishName.get(key));    //获取到Id
		//查keyValue表，获取连续积善天数
		List<TCsqKeyValue> dailyDonateList = csqKeyValueDao.selectByKeyAndTypeDesc(userId, CsqKeyValueEnum.TYPE_DAILY_DONATE.getCode());
		List<Long> createTimeList = dailyDonateList.stream()
			.map(TCsqKeyValue::getCreateTime)
			.map(Timestamp::getTime)
			.collect(Collectors.toList());
		int continueDayCnt = DateUtil.getContinueDayCnt(createTimeList);
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		CsqDailyDonateVo csqDailyDonateVo = csqService.copyCsqDailyDonateVo();
		csqDailyDonateVo.setDayCnt(continueDayCnt);
		//今日已筹金额、捐款人数
		//判明类别
		Integer type = csqService.getType();
		Double dailyIncome;
		Integer donateCnt;
		Long toId = csqService.getId();
		if (CsqServiceEnum.TYPE_FUND.getCode() == type) {    //基金
			//toType使用基金类型，使用fundId
			//找到fundId
			toId = csqService.getFundId();
		}
		List<TCsqUserPaymentRecord> userPaymentRecords = csqUserPaymentDao.selectByToTypeAndToIdAndCreateTimeBetween(CSqUserPaymentEnum.TYPE_FUND.toCode(), toId);
		dailyIncome = userPaymentRecords.stream()
			.map(TCsqUserPaymentRecord::getMoney)
			.reduce(0d, (a, b) -> a + b);
		donateCnt = userPaymentRecords.size();
		csqDailyDonateVo.setDailyIncome(dailyIncome);
		csqDailyDonateVo.setDonateCnt(donateCnt);

		return csqDailyDonateVo;
	}

	@Override
	public void customizeDailyDonateList(Integer weekDayCnt, Long... serviceIds) {
		if(weekDayCnt != null && (weekDayCnt < 0 || weekDayCnt > 7)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的周号!");
		}

		if (serviceIds == null || serviceIds.length > 7) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的周推数量!" + "length:" + String.valueOf(serviceIds == null ? 0 : serviceIds.length));
		}

		// 获取Map
		Map<Long, Long> dailyDonateMap = csqPublishService.getPublishName(CsqPublishEnum.MAIN_KEY_DAILY_DONATE.toCode());
		if(weekDayCnt == null && serviceIds.length == 7) {	//一次定制所有
			//按顺序覆盖元数据
			for(int i=0; i<serviceIds.length; i++) {
				Long key = 1L + i;
				dailyDonateMap.put(key, serviceIds[i]);	//替换元数据
			}
			csqPublishService.setPublishName(CsqPublishEnum.MAIN_KEY_DAILY_DONATE.toCode(), dailyDonateMap);
			return;
		}

		if(weekDayCnt != null && serviceIds.length == 1) {	//定制某一天
			dailyDonateMap.put(Long.valueOf(weekDayCnt), serviceIds[0]);
			csqPublishService.setPublishName(CsqPublishEnum.MAIN_KEY_DAILY_DONATE.toCode(), dailyDonateMap);
			return;
		}

		throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "定制未进行.请检查参数");
	}

	@Override
	public void register(String telephone, String password, String uuid) {
		TCsqUser build = TCsqUser.builder().userTel(telephone).password(password).uuid(uuid).build();
		TCsqUser register = register(build);
		csqUserDao.insert(register);
	}

	private TCsqUser register(TCsqUser csqUser) {
		//默认头像等...
		csqUser.setUserHeadPortraitPath(CsqUserEnum.DEFAULT_HEADPORTRAITURE_PATH);
		csqUserDao.insert(csqUser);
		//注册到认证中心
		String namePrefix = UserUtil.getApplicationNamePrefix(ApplicationEnum.CONGSHANQIAO_APPLICATION.toCode());
		Token token = authorizeRpcService.reg(namePrefix + csqUser.getId(), DEFAULT_PASS, csqUser.getId().toString(), csqUser.getUuid(), Boolean.FALSE);

		if (token != null && token.getToken() != null) {
			csqUser.setToken(token.getToken());
		}
		return csqUser;
	}

}
