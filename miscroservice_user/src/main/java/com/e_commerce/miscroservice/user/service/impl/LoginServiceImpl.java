package com.e_commerce.miscroservice.user.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.*;
import com.e_commerce.miscroservice.user.wechat.entity.WechatSession;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import com.e_commerce.miscroservice.order.service.impl.BaseService;
import com.e_commerce.miscroservice.user.dao.UserDao;
import com.e_commerce.miscroservice.user.service.LoginService;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.service.apiImpl.SendSmsService;
import com.e_commerce.miscroservice.user.vo.WechatLoginVIew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl extends BaseService implements LoginService {

	private static Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);
	@Autowired
	private UserDao userDao;

	@Autowired
	private WechatService wechatService;

	@Autowired
	private SendSmsService sendSmsService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private UserService userService;

	private static final String LOGIN_STATUS = "loginStatus";

	private static final String WECHAT_SESSION = "wechatSession";

	private static final String TELEPHONE = "telephone";

	private static final String MOBILE = "mobile";

	private static final String OPENID = "openid";

	private static final String VIEW = "view";

	@Value("${redis_user}")
	private String REDIS_USER;

	@Value("${redis_score}")
	private String REDIS_SCORE;
	
	@Value("${debug}")
	private String DEBUG;
	
	private Long HASH_INTERVAL = getUserTokenInterval() / 24;

	/**
	 * 电话授权 step2
	 * @param encryptedData
	 * @param iv
	 */
	@SuppressWarnings("unchecked")
	public String phoneAutho(String openid, String encryptedData, String iv) {

		LOG.info("/n-------------------------开始获取用户手机号码----------------------------");
		// 校验授权
		Map<String, Object> map = (HashMap<String, Object>) redisUtil.hget(REDIS_USER, openid);
		WechatSession session = (WechatSession) map.get(WECHAT_SESSION);
		// 获取电话号码
		String telephone = wechatService.getPhoneNumber(encryptedData, iv, session);
		// 存放手机号到以openid为item的redis库
		map.put(TELEPHONE, telephone);
		redisUtil.hset(REDIS_USER, openid, map,HASH_INTERVAL);

		Map<String, Object> params = new HashMap<>();
		params.put(MOBILE, telephone);
		// 区分测试和生产环境
		String validCode = "666666";
		if (StringUtil.equals(AppConstant.DEBUG_STATUS_FALSE, DEBUG)) { // 表示当前运行环境为生产 TODO
			validCode = UUIDGenerator.messageCode();
		}
		params.put(AppConstant.VALID_CODE, validCode);

		String bVal = "true";
		if (StringUtil.equals(AppConstant.DEBUG_STATUS_FALSE, DEBUG)) { // 表示当前运行环境为生产
			bVal = sendSmsService.execute(params);
		}

		if (StringUtil.equals("false", bVal)) {
			throw new MessageException(AppErrorConstant.SendSms_Error, "手机号码为:" + telephone + "验证码发送错误");
		}

		// 将之前的验证码置不可用
		redisUtil.hdel(REDIS_SCORE, telephone);
		redisUtil.hdel(REDIS_USER, telephone);

		// 缓存验证码
		long time = Calendar.getInstance().getTimeInMillis();

		redisUtil.hset(REDIS_SCORE, telephone, time, HASH_INTERVAL);
		redisUtil.hset(REDIS_USER, telephone, validCode ,HASH_INTERVAL);

		return telephone;
	}

	/**
	 * 校验短信验证码 step3
	 * @param openid
	 * @param validCode
	 * @return
	 */
	public Map<String, Object> validSmsCode(String openid, String validCode) {
		if (!StringUtil.isNotEmpty(validCode)) {
			throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "验证码不能为空！");
		}

		Map<String, Object> map = (HashMap<String, Object>) redisUtil.hget(REDIS_USER, openid);
		String telephone = (String) map.get(TELEPHONE);

		String tempValidCode = String.valueOf(redisUtil.hget(REDIS_USER, telephone));

		if (redisUtil.isExpire(REDIS_SCORE, telephone, AppConstant.SMS_EXPIRED)) { // 验证码过期
			// 将验证码记录为不可用()
			redisUtil.hdel(REDIS_SCORE, telephone);
			redisUtil.hdel(REDIS_USER, telephone);
			throw new MessageException(AppErrorConstant.SendSms_Valid_Error, "验证码已过期，请重新授权");
		}
		if (!StringUtil.equals(validCode, tempValidCode)) {
			throw new MessageException(AppErrorConstant.SendSms_Valid_Error, "验证码错误，请重新输入");
		}
		// 验证通过将验证码记录为不可用
		redisUtil.hdel(REDIS_SCORE, telephone);
		redisUtil.hdel(REDIS_USER, telephone);

		// 注册新用户，并返回登录态
		WechatSession session = (WechatSession) map.get(WECHAT_SESSION);
		WechatLoginVIew view = (WechatLoginVIew) map.get(VIEW);
		TUser user = rigester(view, session);
		user.setUserTel(telephone);
		userDao.updateByPrimaryKey(user);
		// token
//		String key = MD5.crypt(session.getOpenid());
//		redisUtil.hset(REDIS_USER, key, user);

		String userId = String.valueOf(user.getId());
		String token = TokenUtil.genToken(userId);
		// redis
		redisUtil.set(token, user, getUserTokenInterval());
		redisUtil.set("" + userId, user, getUserTokenInterval());
		redisUtil.set("str" + userId, token, getUserTokenInterval());

		Map<String, Object> resultMap = (HashMap<String, Object>) redisUtil.hget(REDIS_USER, openid);
		resultMap.put(AppConstant.USER_TOKEN, token);
		return resultMap; // 返回登录状态
	}

	/**
	 * 登陆校验 step1
	 * @return
	 */
	public Map<String, String> checkLogin(WechatLoginVIew view) {
//		WechatSession session = wechatService.checkAuthCode(view.getCode());
		WechatSession session = new WechatSession();
		session.setOpenid(view.getOpenid());
		session.setSession_key(view.getSession_key());
		String openid = session.getOpenid();
		// 解析手机号(仅用作判定账户唯一性，并不收集用户数据)
		String telephone = wechatService.getPhoneNumber(view.getEncryptedData(), view.getIv(), session);
		TUser user = null;
		TUser openUser = getUser(openid); // 根据微信openid判定账户是否存在 TODO
		TUser telephoneUser = userService.getUserAccountByTelephone(telephone); // 根据手机号判定账户是否存在 TODO
		Map<String, String> resultMap = new HashMap<>();
		Map<String, Object> redisMap = new HashMap<>();
		// 存储微信session
		redisMap.put(WECHAT_SESSION, session);
		if (openUser != null) { // openid存在
			user = openUser;
		} else if (openUser == null && telephoneUser != null) { // openid不存在，手机号存在
			user = telephoneUser;
			user.setIsFake(AppConstant.IS_FAKE_NO);	//TODO 设置为真实用户
			user.setVxOpenId(openid); // 设置openid
			userDao.updateByPrimaryKey(user);
		} else { // 存放注册信号并返回openid和注册情形
			resultMap.put(LOGIN_STATUS, "01");
			resultMap.put(OPENID, openid);
			redisMap.put(LOGIN_STATUS, "01");
			redisMap.put(VIEW, view);
			// redisMap存在wechatSession、openid、WechatLoginVIew
			redisUtil.hset(REDIS_USER, openid, redisMap, HASH_INTERVAL);
			return resultMap;
		}
		//校验用户可用状态
		if(AppConstant.AVALIABLE_STATUS_NOT_AVALIABLE.equals(user.getAvaliableStatus())) {
			throw new MessageException("当前用户被封禁!禁止登录！");
		}
		
		// 实名状态
		resultMap.put("certStatus", userService.getCertStatus(user.getId()));
		redisMap.put(LOGIN_STATUS, "02");
		resultMap.put(LOGIN_STATUS, "02");
		String key = MD5.crypt(session.getOpenid());

		// 存储用户
		String userId = StringUtil.numberToString(user.getId());
		String token = TokenUtil.genToken(userId);
		// redis
		redisUtil.set(token, user, getUserTokenInterval());
		redisUtil.set("" + userId, user, getUserTokenInterval());
		redisUtil.set("str" + userId, token, getUserTokenInterval());

		redisUtil.hset(REDIS_USER, key, user,HASH_INTERVAL);
		resultMap.put("userId", userId);
		resultMap.put(AppConstant.USER_TOKEN, token);
		// 返回map，包含自定义状态
		return resultMap;
	}

	/**
	 * 注册
	 * @param view
	 * @param session
	 */
	private TUser rigester(WechatLoginVIew view, WechatSession session) {
		// 向数据库插入一个新的用户
		TUser user = new TUser();
		LOG.info("/n-------------------数据库新建一个用户---------------------");
		user = getWechatUser(view);
		user.setVxOpenId(session.getOpenid()); // 往微信账户字段中记录openid
		return userService.rigester(user);
	}

	/**
	 * 根据openid获取用户
	 * @param openId
	 * @return
	 */
	private TUser getUser(String openId) {

		if (StringUtil.isEmpty(openId)) {
			throw new MessageException(AppErrorConstant.Field_Error, "微信账号字段为空");
		}
		List<TUser> list = userDao.selectByVxOpenId(openId);
		TUser user = null;
		if (list != null && !list.isEmpty()) {
			for(TUser thisUser:list) {
				if(!AppConstant.IS_COMPANY_ACCOUNT_YES.equals(thisUser.getIsCompanyAccount())) {
					user = thisUser;
				}
			}
			if (AppConstant.AVALIABLE_STATUS_NOT_AVALIABLE.equals(user.getAvaliableStatus())) {
				throw new MessageException("当前用户被封禁!禁止登录！");
			}
		}
		return user;
	}

	/**
	 * 更新验证码状态
	 * @param telephone
	 * @return
	 */
	@SuppressWarnings("unused")
	private void updateValidCode(String telephone) {
	}

	/**
	 * 获取用户信息
	 */
	private TUser getWechatUser(WechatLoginVIew view) {
		TUser user = new TUser();
		user = BeanUtil.copy(view, TUser.class);
		String nickName = view.getNickName();
		String headUrl = view.getAvatarUrl();
		int sex = view.getGender();
		if (StringUtil.isNotEmpty(nickName) && StringUtil.isNotEmpty(headUrl) && StringUtil.isNotEmpty(sex)) {
			user.setName(nickName);
			user.setUserHeadPortraitPath(headUrl);
			user.setSex(sex);
		}
		return user;
	}

	/**
	 * 功能描述: 根据openid更新
	 * 作者: 许方毅
	 * 创建时间: 2018年11月27日 下午6:55:07
	 * @param openid
	 * @return
	 */
	@Override
	public Map<String, Object> loginByOpenid(String openid) {//TODO 用户封禁
		if (openid == null) {
			return new HashMap<String, Object>();
		}
		TUser user = getUser(openid);
		if(user == null) {
			return new HashMap<String, Object>();
		}
		String userId = StringUtil.numberToString(user.getId());
		String token = TokenUtil.genToken(userId);
		
		String key = "str" + userId;
		
		//使之前的用户登录失效
		if(redisUtil.hasKey(key)) {
			String formerToken = (String) redisUtil.get(key);
			redisUtil.del(formerToken);
			//TO DEL REMARK
			/*redisUtil.del(formerToken);
			redisUtil.del(userId);*/
		}
		
		// redis
		redisUtil.set(token, user, getUserTokenInterval());
		redisUtil.set("" + userId, user, getUserTokenInterval());
		redisUtil.set("str" + userId, token, getUserTokenInterval());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("token", token);
		resultMap.put("userId", userId);
		
		return resultMap;
	}

}
