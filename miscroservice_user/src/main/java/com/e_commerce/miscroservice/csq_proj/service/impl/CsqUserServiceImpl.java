package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.service.Token;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.*;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.dto.WechatPhoneAuthDto;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.*;
import com.e_commerce.miscroservice.csq_proj.vo.*;
import com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.wechat.entity.WechatSession;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService.DEFAULT_PASS;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:45
 */
@Transactional(rollbackFor = Throwable.class)
@Service
@Log
public class CsqUserServiceImpl implements CsqUserService {

	@Autowired
	private CsqMsgDao csqMsgDao;

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

	@Autowired
	private CsqOrderDao csqOrderDao;

	@Autowired
	private CsqFundDao csqFundDao;

	@Autowired
	private WechatService wechatService;

	@Autowired
	private CsqPayService csqPayService;

	@Autowired
	private CsqMsgService csqMsgService;

	@Autowired
	private CsqServiceService csqServiceService;

	@Autowired
	@Qualifier("csqRedisTemplate")
	HashOperations<String, String, Object> userRedisTemplate;

	@Value("${page.fund}")
	private String FUND_PAGE;

	@Value("${page.person}")
	private String PERSON_PAGE;

	@Value("${page.service}")
	private String SERVICE_PAGE;

	@Override
	public void checkAuth(TCsqUser user) {
		if (user == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "用户不存在");
		}
		Integer authenticationStatus = user.getAuthenticationStatus();
		if (!CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
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
	public Map<String, Object> openidLogin(String openid, String uuid, WechatPhoneAuthDto wechatPhoneAuthDto) {
		boolean isRegister = false;
		String phoneNumber = null;
		String encryptData = wechatPhoneAuthDto.getEncryptedData();
		String iv = wechatPhoneAuthDto.getIv();
		String sessionKey = wechatPhoneAuthDto.getSessionKey();
		if (!StringUtil.isAnyEmpty(encryptData, iv, sessionKey)) {
			WechatSession wechatSession = new WechatSession();
			wechatSession.setSession_key(sessionKey);
			phoneNumber = wechatService.getPhoneNumber(encryptData, iv, wechatSession);
		}

		TCsqUser tCsqUser = csqUserDao.selectByVxOpenIdAndAccountType(openid, CsqUserEnum.ACCOUNT_TYPE_PERSON.toCode());
		if (tCsqUser == null) {    //进行注册
			tCsqUser = new TCsqUser();
			tCsqUser.setUuid(uuid);
			tCsqUser.setVxOpenId(openid);
			tCsqUser.setUserTel(phoneNumber);
			tCsqUser = register(tCsqUser);
			isRegister = true;
		}
		//登录
		String token = tCsqUser.getToken();
		if (token == null) {
			tCsqUser.setUuid(uuid);
			tCsqUser = UserUtil.login(tCsqUser, ApplicationEnum.CONGSHANQIAO_APPLICATION.toCode(), authorizeRpcService);
			token = tCsqUser.getToken();
			if(token==null||token.isEmpty()){
				tCsqUser = new TCsqUser();
				tCsqUser.setUuid(uuid);
				tCsqUser.setVxOpenId(openid);
				tCsqUser.setUserTel(phoneNumber);
				tCsqUser = register(tCsqUser);
				isRegister = true;
			}


		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("token", token);
		resultMap.put("user", tCsqUser.copyCsqBasicUserVo());
		resultMap.put("isRegister", isRegister);
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
	public void sendPersonAuth(TCsqUserAuth csqUserAuth, String smsCode) throws Exception {
		String cardId;
		String name;
		String telephone;
		if (StringUtil.isAnyEmpty(cardId = csqUserAuth.getCardId(), name = csqUserAuth.getName(), telephone = csqUserAuth.getPhone())) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "必填参数不全！");
		}
		userService.checkSMS(telephone, smsCode);
		Long userId = csqUserAuth.getUserId();
		//判断是否已经实名
		TCsqUser tCsqUser = csqUserDao.selectByPrimaryKey(userId);
		Integer authenticationStatus = tCsqUser.getAuthenticationStatus();
		if (CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已经实名过，无需重复实名!");    //或改为return
		}

		//重复提交判断
		List<TCsqUserAuth> existList = csqUserAuthDao.selectByUserIdAndStatus(userId, CsqUserAuthEnum.STATUS_UNDER_CERT.getCode());
		if (!existList.isEmpty()) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已存在待审核的实名认证记录!");
		}

		// 判空
		if (!IDCardUtil.checkNameAndNo(cardId, name)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "身份证号不正确或与姓名不匹配");
		}

		// 判断是否已经有记录
		TCsqUserAuth auths = csqUserAuthDao.findByCardId(cardId);
		if (auths != null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该身份证号已被使用！");
		}

		csqUserAuth.setStatus(CsqUserAuthEnum.STATUS_UNDER_CERT.getCode());    //待审核(默认)
		csqUserAuth.setStatus(CsqUserAuthEnum.STATUS_CERT_PASS.getCode());    //审核通过
		csqUserAuthDao.insert(csqUserAuth);
		//TODO 若提交即实名，此处将用户实名状态改变
		tCsqUser.setAuthenticationStatus(CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode());
		csqUserDao.updateByPrimaryKey(tCsqUser);
	}

	@Override
	public Map<String, Object> findCsqUserById(Long userId) {
		TCsqUser tCsqUser = csqUserDao.selectByPrimaryKey(userId);
		List<TCsqFund> list = csqFundDao.selectByUserIdAndNotEqStatus(userId, CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal());
		Map<String, Object> map = new HashMap<>();
		Integer status = 0;
		Double balance = 0D;
		TCsqFund tCsqFund = null;
		if (list != null && list.size() > 0) {
			tCsqFund = list.get(0);
			status = list.get(0).getStatus();
			balance = list.get(0).getBalance();
		}
//		map.put("name", tCsqUser.getName());	//姓名
//		map.put("userHeadPortraitPath", tCsqUser.getUserHeadPortraitPath());
//		map.put("remarks", tCsqUser.getRemarks());
//		map.put("existDays", null);

		//获取累积捐助总额
		/*List<TCsqUserPaymentRecord> userPaymentRecords = csqUserPaymentDao.selectByUserIdAndInOrOut(userId, CsqUserPaymentEnum.INOUT_OUT.toCode());//我的总支出(平台内捐助	//或修改成到他人的项目或基金
		Double reduce = userPaymentRecords.stream()
			.map(TCsqUserPaymentRecord::getMoney)
			.reduce(0d,Double::sum);
*/
		Double sumTotalPay = tCsqUser.getSumTotalPay();
		map.put("sumDonate", sumTotalPay);    //我的累积捐款总额
		map.put("surplusAmount", tCsqUser.getSurplusAmount());    //账户余额
		Double sumTotalIn = tCsqFund != null ? tCsqFund.getSumTotalIn() : 0d;
		Double publicMinimum = CsqFundEnum.PUBLIC_MINIMUM;
		map.put("sumTotalIn", sumTotalIn > publicMinimum ? publicMinimum : sumTotalIn);    //基金账户筹备累积
		map.put("expected", publicMinimum);    //期望金额
		map.put("status", status);        //基金账户状态

		//获取我唯一的基金以及他双生项目的编号
		if(tCsqFund == null) {
			map.put("fundId", "");
			map.put("serviceId", "");
			return map;
		}
		Long fundId = tCsqFund.getId();
		map.put("fundId", fundId);
		TCsqService service = csqServiceService.getService(fundId);
		map.put("serviceId", service.getId()==null? "": service.getId());

		return map;
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
		//encrypt password
		TCsqUser tCsqUser = csqUserDao.selectByUserTelAndPasswordAndAccountType(telephone, password, accountType);
		if (tCsqUser == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "手机号或密码错误!");
		}
		tCsqUser.setUuid(uuid);
		tCsqUser = UserUtil.login(tCsqUser, ApplicationEnum.CONGSHANQIAO_APPLICATION.toCode(), authorizeRpcService);
		String token = tCsqUser.getToken();
		map.put("token", token);
		map.put("user", tCsqUser.copyCsqBasicUserVo());
		return map;
	}

	@Override
	public Map<String, Object> registerAndSubmitCert(String telephone, String validCode, String uuid, TCsqUserAuth csqUserAuth, String name, String userHeadPortraitPath, boolean submitOnly) {
		Map<String, Object> map = new HashMap<>();
		userService.checkSMS(telephone, validCode);
		//用户是否已经注册，若无注册一个(Corp类型
		TCsqUser tCsqUser = csqUserDao.selectByUserTelAndAccountType(telephone, CsqUserEnum.ACCOUNT_TYPE_COMPANY.toCode());

		//表明此时仅提交认证信息
		if (!submitOnly) {
			if (tCsqUser != null) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已注册请勿重复注册！");
			}
		}

		if (tCsqUser == null) {    //进行注册
			tCsqUser = new TCsqUser();
			tCsqUser.setUserTel(telephone);
			tCsqUser.setUuid(uuid);
			tCsqUser.setAccountType(CsqUserEnum.ACCOUNT_TYPE_COMPANY.toCode());
			tCsqUser.setName(name);
			tCsqUser.setUserHeadPortraitPath(userHeadPortraitPath);
			tCsqUser = register(tCsqUser);
		}
		insertOrUpdateUserAuth(telephone, csqUserAuth, tCsqUser);

		map.put("user", tCsqUser);
		map.put("token", tCsqUser.getToken());
		return map;
	}

	private void insertOrUpdateUserAuth(String telephone, TCsqUserAuth csqUserAuth, TCsqUser tCsqUser) {
		Long corpUserId = tCsqUser.getId();
		//用户是否已经实名过
		Integer authenticationStatus = tCsqUser.getAuthenticationStatus();
		if (CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode().equals(authenticationStatus)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已经认证过！");
		}
		//用户是否有正在审核的请求，若无插入一个
		TCsqUserAuth tCsqUserAuth = csqUserAuthDao.selectByUserId(corpUserId);
		if (tCsqUserAuth != null && tCsqUserAuth.getStatus() != CsqUserAuthEnum.STATUS_CERT_REFUSE.getCode()) {	//除待审核外，其他状态都可以复用
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您存在待审核的记录!");
		}
		//提交审核
		String authName = csqUserAuth.getName();
		authName = authName == null ? tCsqUser.getName() : authName;
		String licenseId = csqUserAuth.getLicenseId();
		String licensePic = csqUserAuth.getLicensePic();
		if (StringUtil.isAnyEmpty(authName, licensePic)) {
			throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "必填参数为空!");
		}
		//复用记录
		TCsqUserAuth userAuth = TCsqUserAuth.builder().status(CsqUserAuthEnum.STATUS_UNDER_CERT.getCode())
			.id(tCsqUserAuth != null ? tCsqUserAuth.getId() : null)
			.userId(corpUserId)
			.type(CsqUserAuthEnum.TYPE_CORP.getCode())
			.phone(telephone)
			.licenseId(licenseId)
			.licensePic(licensePic)
			.name(authName)
			.build();
		//略去审核流程标记 REMARK
		csqUserAuthDao.insertOrUpdate(userAuth);
	}

	@Override
	public void certCorp(Long userAuthId, Integer option) {
		Integer OPTION_PASS = CsqUserAuthEnum.STATUS_CERT_PASS.getCode();
		Integer OPTION_REFUSE = CsqUserAuthEnum.STATUS_CERT_REFUSE.getCode();
		boolean isPass = OPTION_PASS.equals(option);
		if (option == null || !Arrays.asList(OPTION_PASS, OPTION_REFUSE).contains(option)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数option不正确!");
		}
		//审核记录状态修改
		TCsqUserAuth userAuth = csqUserAuthDao.selectByPrimaryKey(userAuthId);
		if (userAuth == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该审核记录不存在！");
		}
		if (CsqUserAuthEnum.STATUS_UNDER_CERT.getCode() != userAuth.getStatus()) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当前审核状态不正确！");

		}
		userAuth.setStatus(option);
		csqUserAuthDao.update(userAuth);
		//用户实名状态修改
		Long userId = userAuth.getUserId();
		TCsqUser build = TCsqUser.builder().id(userId)
			.authenticationStatus(isPass? CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode() : CsqUserEnum.AUTHENTICATION_STATUS_NO.toCode())
			.authenticationType(CsqUserEnum.AUTHENTICATION_TYPE_ORG_OR_CORP.toCode()).build();
		csqUserDao.updateByPrimaryKey(build);
		//插入一条系统消息
		CsqSysMsgTemplateEnum csqSysMsgTemplateEnum = isPass ? CsqSysMsgTemplateEnum.CORP_CERT_SUCCESS : CsqSysMsgTemplateEnum.CORP_CERT_FAIL;
		CsqServiceMsgEnum csqServiceMsgEnum = isPass ? CsqServiceMsgEnum.CORP_CERT_SUCCESS : CsqServiceMsgEnum.CORP_CERT_FAIL;
		csqMsgService.insertTemplateMsg(csqSysMsgTemplateEnum, userId);
		//插入服务通知
		csqMsgService.sendServiceMsg(userId, csqServiceMsgEnum, CsqServiceMsgParamVo.builder()
			.csqUserAuthVo(userAuth.copyCsqUserAuthVo()).build());
	}

	@Override
	public CsqDailyDonateVo dailyDonateDetail(Long userId) {
		Long serviceId = getDailyDonateServiceId();
		//查keyValue表，获取连续积善天数
		List<TCsqKeyValue> dailyDonateList = userId == null ? new ArrayList<>() : csqKeyValueDao.selectByKeyAndTypeDesc(userId, CsqKeyValueEnum.TYPE_DAILY_DONATE.getCode());
		List<Long> createTimeList = dailyDonateList.stream()
			.map(TCsqKeyValue::getCreateTime)
			.map(Timestamp::getTime)
			.collect(Collectors.toList());
		int continueDayCnt = DateUtil.getContinueDayCnt(createTimeList);
		TCsqService csqService = csqServiceDao.selectByPrimaryKey(serviceId);
		if (csqService == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当前没有可以推荐的项目");
		}
		CsqDailyDonateVo csqDailyDonateVo = csqService.copyCsqDailyDonateVo();
		csqDailyDonateVo.setDayCnt(continueDayCnt);
		//今日已筹金额、捐款人数
		//判明类别
		Integer type = csqService.getType();
		Double dailyIncome;
		Integer donateCnt;
		Long toId = csqService.getId();
		boolean isFund = false;
		if (CsqServiceEnum.TYPE_FUND.getCode() == type) {    //基金
			//toType使用基金类型，使用fundId
			//找到fundId
			toId = csqService.getFundId();
			isFund = true;
		}
		long startStamp = DateUtil.getStartStamp(System.currentTimeMillis());
		long endStamp = DateUtil.getEndStamp(System.currentTimeMillis());
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectByToIdAndToTypeAndStatusAndOrderTimeBetweenDesc(toId, isFund ? CsqEntityTypeEnum.TYPE_FUND.toCode() : CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqOrderEnum.STATUS_ALREADY_PAY.getCode(), startStamp, endStamp);
		dailyIncome = tCsqOrders.stream()
			.map(TCsqOrder::getPrice)
			.reduce(0d, (a, b) -> a + b);
		dailyIncome = NumberUtil.keep2Places(dailyIncome);
		donateCnt = tCsqOrders.size();

		csqDailyDonateVo.setDailyIncome(dailyIncome);
		csqDailyDonateVo.setDonateCnt(donateCnt);
		csqDailyDonateVo.setPersonInCharge(csqService.getPersonInCharge());

		return csqDailyDonateVo;
	}

	private Long getDailyDonateServiceId() {
		Long serviceId = 1l;    //默认
		//查询publish表，获取日推项目信息，没有的话从未达目标值的项目池中抓取，没有的话给默认值
		Map publishName = csqPublishService.getPublishName(CsqPublishEnum.MAIN_KEY_DAILY_DONATE.toCode());
		//确认今天是周几
		long currentTimeMillis = System.currentTimeMillis();
		Integer weekDayInt = DateUtil.getWeekDayInt(currentTimeMillis);
		Long key = Long.valueOf(weekDayInt);
		String serviceIdStr = (String) publishName.get(key.toString());    //获取到Id
		if (serviceIdStr != null) {
			serviceId = Long.valueOf(serviceIdStr);
		}
		return serviceId;
	}

	@Override
	public boolean isDailyDonateServiceId(Long serviceId) {
		return getDailyDonateServiceId().equals(serviceId);
	}

	@Override
	public void customizeDailyDonateList(Integer weekDayCnt, Long... serviceIds) {
		if (weekDayCnt != null && (weekDayCnt < 0 || weekDayCnt > 7)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的周号!");
		}

		if (serviceIds == null || serviceIds.length > 7) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的周推数量!" + "length:" + String.valueOf(serviceIds == null ? 0 : serviceIds.length));
		}

		// 获取Map
		Map<Long, Long> dailyDonateMap = csqPublishService.getPublishName(CsqPublishEnum.MAIN_KEY_DAILY_DONATE.toCode());
		if (weekDayCnt == null && serviceIds.length == 7) {    //一次定制所有
			//按顺序覆盖元数据
			for (int i = 0; i < serviceIds.length; i++) {
				Long key = 1L + i;
				dailyDonateMap.put(key, serviceIds[i]);    //替换元数据
			}
			csqPublishService.setPublishName(CsqPublishEnum.MAIN_KEY_DAILY_DONATE.toCode(), dailyDonateMap);
			return;
		}

		if (weekDayCnt != null && serviceIds.length == 1) {    //定制某一天
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

	@Override
	public Map<String, Object> share(Long userId, Long entityId, Integer option) {
		final int OPTION_PERSON = 0;
		final int OPTION_FUND = 1;
		final int OPTION_SERVICE = 2;
		if (option == null || !Arrays.asList(OPTION_PERSON, OPTION_FUND, OPTION_SERVICE).contains(option)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数option错误!");
		}
		Map<String, Object> map = new HashMap<>();
//		String scene = userId.toString();    //随机数 => 7.16 scene长度过长!(40 > 32)  => 或直接使用json字符串
		String page = "";        //从配置文件读取
		CsqShareVo vo = null;
		UploadPathEnum.innerEnum uploadEnum = null;
		//VO initialize
		CsqSceneVo.CsqSceneVoBuilder builder = CsqSceneVo.builder();
		builder = builder.userId(userId);
		switch (option) {
			case OPTION_FUND:
				page = this.FUND_PAGE;
				TCsqFund csqFund = csqFundDao.selectByPrimaryKey(entityId);
				String fundName = csqFund.getName();
				vo = csqFund.copyCsqShareVo();
				vo.setTitle("我创建了一个" + fundName + "专项基金");
				Double sumTotalIn = csqFund.getSumTotalIn();
				sumTotalIn = NumberUtil.keep2Places(sumTotalIn);
				vo.setCurrentAmont(sumTotalIn);
				vo.setExpectedAmount(CsqFundEnum.PUBLIC_MINIMUM);
				String voCoverPic = vo.getCoverPic();
				vo.setCoverPic(voCoverPic.contains(",")? Arrays.asList(voCoverPic.split(",")).get(0):voCoverPic);	//只给一张
				Integer status = csqFund.getStatus();
				TCsqService tCsqService = csqServiceDao.selectByFundId(csqFund.getId());
				Long serviceId = tCsqService.getId();
				vo.setServiceId(serviceId);
				if (CsqFundEnum.STATUS_PUBLIC.getVal() != status) {    //未公开
					// 获取捐献记录列表
					List<TCsqOrder> tCsqOrders = csqOrderDao.selectByToIdAndToTypeAndStatusDesc(entityId, CsqEntityTypeEnum.TYPE_FUND.toCode(), CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
					List<Long> userIds = tCsqOrders.stream()
						.map(TCsqOrder::getUserId).collect(Collectors.toList());
					List<TCsqUser> tCsqUsers = userIds.isEmpty() ? new ArrayList<>() : csqUserDao.selectInIds(userIds);
					Map<Long, List<TCsqUser>> userMap = tCsqUsers.stream().collect(Collectors.groupingBy(TCsqUser::getId));
					List<CsqDonateRecordVo> donateRecordVos = tCsqOrders.stream()
						.map(a -> {
							long mills = System.currentTimeMillis() - a.getUpdateTime().getTime();
							Long minutesLong = mills / 1000 / 60;
							Integer minutesAgo = minutesLong.intValue();
							minutesAgo = minutesAgo > 60? 60: minutesAgo;
							Long theUserId = a.getUserId();
							List<TCsqUser> csqUsers = userMap.get(theUserId);
							TCsqUser csqUser = csqUsers.get(0);
							CsqDonateRecordVo donateRecordVo = CsqDonateRecordVo.builder().userHeadPortraitPath(csqUser.getUserHeadPortraitPath())
								.name(csqUser.getName())
								.donateAmount(a.getPrice())
								.minutesAgo(minutesAgo).build();
							return donateRecordVo;
						}).collect(Collectors.toList());
					vo.setDonateRecordVos(donateRecordVos);
				}
				uploadEnum = UploadPathEnum.innerEnum.CSQ_FUND;
				Long fundId = csqFund.getId();
				TCsqService service = csqServiceDao.selectByFundId(fundId);
				//scene
				builder = builder.fundId(fundId)
							.serviceId(service.getId())
							.type(CsqSceneEnum.TYPE_FUND.getCode());
				break;
			case OPTION_PERSON:
				page = this.PERSON_PAGE;
				//累积捐赠、捐赠项目数
				TCsqUser csqUser = csqUserDao.selectByPrimaryKey(entityId);
				Long tUserId = csqUser.getId();
				Double totalDonate = csqUser.getSumTotalPay();
				//统计不重复的项目数
				List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndToTypeDesc(tUserId, CsqEntityTypeEnum.TYPE_SERVICE.toCode());
				//统计数量
				long serviceCnt = tCsqOrders.stream()
					.map(TCsqOrder::getId)
					.distinct()    //去重
					.count();
				vo = new CsqShareVo();
				vo.setDonateSum(totalDonate);
				vo.setDonateCnt((int) serviceCnt);
				uploadEnum = UploadPathEnum.innerEnum.CSQ_PERSON;
				break;
			case OPTION_SERVICE:
				page = this.SERVICE_PAGE;
				TCsqService csqService = csqServiceDao.selectByPrimaryKey(entityId);
				String name = csqService.getName();
				String coverPic = csqService.getCoverPic();
				coverPic = Arrays.asList(coverPic.split(",")).get(0);
				vo = CsqShareVo.builder()
					.coverPic(coverPic.contains(",")? Arrays.asList(coverPic.split(",")).get(0):coverPic)	//只给一张
					.name(name)
					.build();
				vo.setCurrentAmont(NumberUtil.keep2Places(csqService.getSumTotalIn()));
				uploadEnum = UploadPathEnum.innerEnum.CSQ_SERVICE;
				builder = builder.serviceId(csqService.getId())
						.type(CsqSceneEnum.TYPE_SERVICE.getCode());
				break;
		}
		//复用记录
		String scene = JSONObject.toJSONString(builder.build());
		TCsqKeyValue tCsqKeyValue = csqKeyValueDao.selectByKeyAndTypeAndTheValue(userId, CsqKeyValueEnum.TYPE_SCENE.getCode(), scene);
		Long sceneKey = null;
		if(tCsqKeyValue == null) {
			tCsqKeyValue = TCsqKeyValue.builder()
				.mainKey(userId)
				.type(CsqKeyValueEnum.TYPE_SCENE.getCode())
				.theValue(scene).build();
			csqKeyValueDao.save(tCsqKeyValue);
		}
		sceneKey = tCsqKeyValue.getId();

		String qrCode = wechatService.genQRCode(sceneKey.toString(), page, uploadEnum);
//		qrCode = "https://timebank-test-img.oss-cn-hangzhou.aliyuncs.com/person/QR0201905161712443084870123470880.jpg";	// 写死的二维码地址
		map.put("qrCode", qrCode);
		map.put("vo", vo);
		return map;
	}

	public static void main(String[] args) {
		CsqSceneVo build = CsqSceneVo.builder()
			.userId(2148L)
			.type(CsqSceneEnum.TYPE_FUND.getCode())
//			.serviceId(1234L)
			.fundId(267L)
			.build();
		String string = JSONObject.toJSONString(build);
		System.out.println(string.length());
		System.out.println(string);
	}

	@Override
	public void recordForConsumption(Long userId, Long fromId, Integer fromType, Double amount, String wholeDescription) {
		//check入餐
		if (fromId == null || fromType == null || amount == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "必填参数为空!");
		}
		Map<Integer, List<CsqEntityTypeEnum>> enumMap = Arrays.stream(CsqEntityTypeEnum.values())
			.filter(a -> a.name().startsWith("TYPE_"))
			.collect(Collectors.groupingBy(CsqEntityTypeEnum::toCode));
		List<CsqEntityTypeEnum> cSqUserPaymentEnums = enumMap.get(fromType);
		if (cSqUserPaymentEnums == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的类型!");
		}

		//check用户权限
//		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);

		//如果为基金，确认基金已经设置为托管(维持一段时间？)
		boolean isFund = CsqEntityTypeEnum.TYPE_FUND.toCode() == fromType;
		if (isFund) {
			TCsqFund csqFund = csqFundDao.selectByPrimaryKey(fromId);
			if (csqFund == null) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "基金不存在!");
			}
			Integer agentModeStatus = csqFund.getAgentModeStatus();
			if (CsqFundEnum.AGENT_MODE_STATUS_ON.getVal() != agentModeStatus) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "当前基金未设置托管!");
			}
		}

		Long ownerId = null;
		//若有完整描述，则不去找寻fromId对应实体名称
		String description = wholeDescription;

		//构建描述
		CsqEntityTypeEnum cSqUserPaymentEnum = cSqUserPaymentEnums.get(0);
		String name = cSqUserPaymentEnum.getMsg();
		boolean fund = CsqEntityTypeEnum.TYPE_FUND.toCode() == cSqUserPaymentEnum.toCode();
		boolean service = CsqEntityTypeEnum.TYPE_SERVICE.toCode() == cSqUserPaymentEnum.toCode();
		String suffix = "";
		if (fund) {
			TCsqFund csqFund = csqFundDao.selectByPrimaryKey(fromId);
			ownerId = csqFund.getUserId();
			name = csqFund.getName();
			suffix = "基金";

			if(csqFund.getBalance() < amount) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "资金不足，消费失败！");
			}
		}

		if (service) {
			TCsqService csqService = csqServiceDao.selectByPrimaryKey(fromId);
			ownerId = csqService.getUserId();
			name = csqService.getName();
			suffix = "项目";

			if(csqService.getSurplusAmount() < amount) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "资金不足，消费失败！");
			}

			Double surplusAmount = csqService.getSurplusAmount();
			surplusAmount = surplusAmount==null? 0: surplusAmount;
			TCsqService build = TCsqService.builder()
				.id(fromId)
				.surplusAmount(surplusAmount - amount)
				.build();
			csqServiceDao.update(build);

			List<TCsqOrder> tCsqOrders = csqOrderDao.selectByToIdAndToTypeAndStatusDesc(fromId, fromType, CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
			List<Long> userIds = tCsqOrders.stream()
				.map(TCsqOrder::getUserId).collect(Collectors.toList());
			Long[] userIdArray = userIds.toArray(new Long[userIds.size()]);
			csqMsgService.insertTemplateMsg(name, CsqSysMsgTemplateEnum.SERVICE_NOTIFY_WHILE_CONSUME, userIdArray);	//TODO distinct
		}
		if (StringUtil.isEmpty(wholeDescription)) {
			description = "从" + name + suffix + "拨款";
		}
		TCsqUserPaymentRecord build = TCsqUserPaymentRecord.builder()
			.userId(ownerId)
			.entityId(fromId)
			.entityType(fromType)
			.description(description)
			.inOrOut(CsqUserPaymentEnum.INOUT_OUT.toCode())
			.money(amount).build();
		build.setCreateUser(userId);
		csqUserPaymentDao.insert(build);

		/*// 平台托管如果为基金类型，应当留下订单记录(用户会在我已捐赠列表中找到) -> 等待托管细则 后续补充或迁移
		if(isFund) {
			TCsqOrder.builder()
				.userId(userId)
				.fromId(fromId)
				.fromType(fromType)
				.toType()	//缺失
				.toId()	//缺失
				.orderNo(UUIdUtil.generateOrderNo())
				.orderTime(System.currentTimeMillis())
				.price(amount)
				.status(CsqOrderEnum.STATUS_UNPAY.getCode()).build();
		}*/
	}

	@Override
	public void certPerson(Long userAuthId, Integer option) {
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
			.authenticationStatus(CsqUserEnum.AUTHENTICATION_STATUS_YES.toCode()).build();
		csqUserDao.updateByPrimaryKey(build);
	}

	@Override
	public Map<String, Object> registerBySMS(String telephone, String validCode, Integer type, TCsqUser user) {
		if (!Arrays.stream(CsqUserEnum.values()).filter(a -> a.name().startsWith("ACCOUNT_TYPE_")).map(a -> a.toCode()).collect(Collectors.toList()).contains(type)) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数type不正确!");
		}

		//check
		userService.checkSMS(telephone, validCode);

		//check手机号是否已经使用
		TCsqUser csqUser = csqUserDao.selectByUserTelAndAccountType(telephone, type);
		if (csqUser != null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该手机号已经注册!");
		}

		//注册用户
		String name = user.getName();
		String userHeadPortraitPath = user.getUserHeadPortraitPath();

		TCsqUser build = TCsqUser.builder()
			.userTel(telephone)
			.name(name)
			.userHeadPortraitPath(userHeadPortraitPath)
			.build();
		TCsqUser register = register(build);

		Map<String, Object> map = new HashMap<>();
		map.put("user", build);
		map.put("token", register.getToken());
		return map;
	}

	@Override
	public Map<String, Object> loginBySMS(String uuid, String telephone, String validCode, Integer type) {
		userService.checkSMS(telephone, validCode);
		//登录
		TCsqUser csqUser = csqUserDao.selectByUserTelAndAccountType(telephone, type);
		if (csqUser == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "账号不存在!");
		}
		csqUser.setUuid(uuid);
		csqUser = UserUtil.login(csqUser, ApplicationEnum.CONGSHANQIAO_APPLICATION.toCode(), authorizeRpcService);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("token", csqUser.getToken());
		resultMap.put("user", csqUser);

		return resultMap;
	}

	@Override
	public void modify(Long userId, CsqBasicUserVo csqBasicUserVo, boolean isWechatAuth) {
		//修改昵称和简介
		/*String name = csqBasicUserVo.getName();
		String remarks = csqBasicUserVo.getRemarks();
		TCsqUser build = TCsqUser.builder()
			.id(userId)
			.name(name)
			.remarks(remarks)
			.build();*/
		//未对组织账户修改手机号做限制
		TCsqUser csqUser = csqBasicUserVo.copyTCsqUser();
		csqUser.setId(userId);
		if(isWechatAuth) {
			csqUser.setAuthStatus(CsqUserEnum.AUTH_STATUS_TRUE.toCode());
		}
		csqUserDao.updateByPrimaryKey(csqUser);
	}

	@Override
	public CsqBasicUserVo infos(Long userId) {
		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
		CsqBasicUserVo csqBasicUserVo = csqUser.copyCsqBasicUserVo();
		//获取实名认证的一些
		Integer accountType = csqUser.getAccountType();
		TCsqUserAuth userAuth = null;
		boolean isPerson = CsqUserEnum.ACCOUNT_TYPE_PERSON.toCode().equals(accountType);
		if (isPerson) {    //用户类型确认
			userAuth = csqUserAuthDao.selectByUserIdAndTypeAndStatus(userId, CsqUserAuthEnum.TYPE_PERSON.getCode(), CsqUserAuthEnum.STATUS_CERT_PASS.getCode());
		} else if (CsqUserEnum.ACCOUNT_TYPE_COMPANY.toCode().equals(accountType)) {
			userAuth = csqUserAuthDao.selectByUserIdAndTypeAndStatus(userId, CsqUserAuthEnum.TYPE_CORP.getCode(), CsqUserAuthEnum.STATUS_CERT_PASS.getCode());
		}
		csqBasicUserVo.setCsqUserAuth(userAuth == null ? new CsqUserAuthVo() : userAuth.copyCsqUserAuthVo());
		//加入小程序累积天数
		Integer existDayStart = 1;    //从1开始
		long time = System.currentTimeMillis() - csqUser.getCreateTime().getTime();
		csqBasicUserVo.setExistDayCnt(existDayStart + DateUtil.timestamp2Days(time));

		//获取基金数量
		boolean hasGotFund = false;
//		csqFundDao.selectByUserIdAndInStatus(userId, Arrays.asList(CsqFundEnum.STATUS_PUBLIC.getVal(), CsqFundEnum.STATUS_CERT_FAIL.getVal(), CsqFundEnum.STATUS_ACTIVATED.getVal(), CsqFundEnum.STATUS_UNDER_CERT.getVal());	//除了待激活
		List<TCsqFund> tCsqFunds = csqFundDao.selectByUserIdAndNotEqStatus(userId, CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal());
		if (!tCsqFunds.isEmpty()) {
			hasGotFund = true;
		}
		//获取对应的组织账号
		//check
		boolean hasGotCompanyAccount = false;
		if (isPerson) {
			TCsqUser companyUser = getCompanyAccount(csqUser);
			if (companyUser != null) {
				hasGotCompanyAccount = true;
			}
		}
		csqBasicUserVo.setGotFund(hasGotFund);
		csqBasicUserVo.setGotCompanyAccount(hasGotCompanyAccount);
		return csqBasicUserVo;
	}

	@Override
	public Map<String, Object> getAuthStatus(Long userId) {
		Map<String, Object> resultMap = new HashMap<>();
		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
		Integer accountType = csqUser.getAccountType();
		boolean isCorp = false;
		Integer status = csqUser.getAuthenticationStatus();
		if (CsqUserEnum.ACCOUNT_TYPE_COMPANY.toCode().equals(accountType)) {
			isCorp = true;
			TCsqUserAuth userAuth = csqUserAuthDao.selectByUserIdAndType(userId, CsqUserAuthEnum.TYPE_CORP.getCode());
			Integer STATUS_NEVER_CERT = -1;    //未实名
			status = STATUS_NEVER_CERT;
			if (userAuth != null) {    //存在有效的认证信息 -> 替换为相应的状态
				status = userAuth.getStatus();
			}
		}
		resultMap.put("status", status);
		resultMap.put("isCorp", isCorp);
		return resultMap;
	}

	@Override
	public List<CsqDonateRecordVo> globleDonateRecord() {
		return csqServiceService.dealWithRedisDonateRecord(null);
	}

	@Override
	public void payInviter(Long beInviterId, String sceneKey, Long inviterId) {
		//处理 sceneKey
		if(!StringUtil.isEmpty(sceneKey)) {
			TCsqKeyValue keyValue = csqKeyValueDao.selectByPrimaryKey(sceneKey);
			if(keyValue == null) {	//对应scene无效
				return;
			}
			inviterId = keyValue.getMainKey();	//邀请人
		}
		//2次check
		if(inviterId==null) {	//无效邀请人信息
			return;
		}
		//建立关系
		//防止重复插入
		String theVal = beInviterId.toString();
		TCsqKeyValue tCsqKeyValue = csqKeyValueDao.selectByKeyAndTypeAndValue(inviterId, CsqKeyValueEnum.TYPE_INVITE.getCode(), theVal);
		if (tCsqKeyValue != null) {
			return;
		}
		TCsqKeyValue build = TCsqKeyValue.builder()
			.type(CsqKeyValueEnum.TYPE_INVITE.getCode())
			.mainKey(inviterId)
			.theValue(theVal)
			.build();
		csqKeyValueDao.save(build);
	}

	@Override
	public void corpSubmit(Long userId, TCsqUserAuth userAuth) {
		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
		csqUser.setName(userAuth.getName());
		csqUserDao.updateByPrimaryKey(csqUser);
		//check
		insertOrUpdateUserAuth(csqUser.getUserTel(), userAuth, csqUser);
	}

	@Override
	public CsqBasicUserVo inviterInfo(Long userIds) {
		//找寻唯一邀请人
		TCsqKeyValue tCsqKeyValue = csqKeyValueDao.selectByValueAndType(userIds, CsqKeyValueEnum.TYPE_INVITE.getCode());
		Long inviterId;
		TCsqUser inviter;
		if(tCsqKeyValue == null || (inviterId=tCsqKeyValue.getMainKey()) == null || (inviter = csqUserDao.selectByPrimaryKey(inviterId)) == null) {
			//若没有邀请人，返回默认头像和默认昵称小善
			return TCsqUser.builder()
				.userHeadPortraitPath(CsqUserEnum.DEFAULT_HEADPORTRAITURE_PATH)
				.name(CsqUserEnum.DEFAULT_INVITER_NAME)
				.build().copyCsqBasicUserVo();
		}
		return inviter.copyCsqBasicUserVo();
	}

	@Override
	public CsqSceneVo getScene(String sceneKey) {
		TCsqKeyValue keyValue = csqKeyValueDao.selectByPrimaryKey(sceneKey);
		String scene = keyValue.getTheValue();
		return JSONObject.parseObject(scene, CsqSceneVo.class);
	}

	private TCsqUser getCompanyAccount(TCsqUser csqUser) {
		String openid = csqUser.getVxOpenId();
		if (openid == null) {
			return null;
		}
		return csqUserDao.selectByVxOpenIdAndAccountType(openid, CsqUserEnum.ACCOUNT_TYPE_COMPANY.toCode());
	}

	private TCsqUser register(TCsqUser csqUser) {
		//默认头像等...
		csqUser = dealWithDefaultVal(csqUser);

		csqUserDao.insert(csqUser);
		Long userId = csqUser.getId();
		//注册到认证中心
		String namePrefix = UserUtil.getApplicationNamePrefix(ApplicationEnum.CONGSHANQIAO_APPLICATION.toCode());
		Token token = authorizeRpcService.reg(namePrefix + userId, DEFAULT_PASS, userId.toString(), csqUser.getUuid(), Boolean.FALSE);


		log.info("认证中心获取的token为={},msg={}",token!=null?token.getToken():"",token!=null?token.getMsg():"");
		if (token != null && token.getToken() != null) {
			csqUser.setToken(token.getToken());
		}
		// sysMsg -> 注册
		csqMsgService.insertTemplateMsg(CsqSysMsgTemplateEnum.REGISTER.getCode(), userId);

		return csqUser;
	}

	private static TCsqUser dealWithDefaultVal(TCsqUser csqUser) {
		if(StringUtil.isEmpty(csqUser.getUserHeadPortraitPath())) {
			csqUser.setUserHeadPortraitPath(CsqUserEnum.DEFAULT_HEADPORTRAITURE_PATH);
		}
		if(StringUtil.isEmpty(csqUser.getName())){
			String name = getDefaultName(csqUser);
			csqUser.setName(name);
		}

		return csqUser;
	}

	private static String getDefaultName(TCsqUser csqUser) {
		String name = csqUser.getName();
		String prefix = "小善";
		long currentTimeMillis = System.currentTimeMillis();
		String nowStringSuffix = String.valueOf(currentTimeMillis).substring(5);
		StringBuilder builder = new StringBuilder();
		name = name == null ? builder.append(prefix).append(nowStringSuffix).toString() : name;
		return name;
	}

	@Override
	public TCsqUser testRegister(TCsqUser csqUser) {
		return register(csqUser);
	}
}
