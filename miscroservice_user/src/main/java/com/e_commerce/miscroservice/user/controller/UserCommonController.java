package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserFreeze;
import com.e_commerce.miscroservice.commons.entity.application.TUserTimeRecord;
import com.e_commerce.miscroservice.commons.enums.application.GrowthValueEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.user.dao.UserDao;
import com.e_commerce.miscroservice.user.dao.UserFreezeDao;
import com.e_commerce.miscroservice.user.dao.UserTimeRecordDao;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User共用Controller
 * userController
 */
@Component
public class UserCommonController {


	Log logger = Log.getInstance(UserCommonController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private WechatService wechatService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserFreezeDao userFreezeDao;

	@Autowired
	private UserTimeRecordDao userTimeRecordDao;

	/**
	 * 根据id获取用户
	 *
	 * @param userId
	 * @return
	 */
	public TUser getUserById(Long userId) {
		return userService.getUserbyId(userId);
	}

	/**
	 * 冻结用户金额
	 *
	 * @param userId      用户ID
	 * @param freeTime    将要冻结金额
	 * @param serviceId   商品ID
	 * @param serviceName 商品名称
	 */
	public void freezeTimeCoin(Long userId, long freeTime, Long serviceId, String serviceName) {
		userService.freezeTimeCoin(userId, freeTime, serviceId, serviceName);
	}

	/**
	 * 根据userId集合差找用户id
	 *
	 * @param userIds
	 * @return
	 */
	public List<TUser> selectUserByIds(List userIds) {
		return userDao.queryByIds(userIds);
	}

	/**
	 * 根据userId更新用户
	 *
	 * @param user
	 * @return
	 */
	public int updateByPrimaryKey(TUser user) {
		return userDao.updateByPrimaryKey(user);
	}

	/**
	 * 根据userId、orderId查找冻结记录
	 *
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public TUserFreeze selectUserFreezeByUserIdAndOrderId(Long userId, Long orderId) {
		return userFreezeDao.selectUserFreezeByUserIdAndOrderId(userId, orderId);
	}

	/**
	 * 更新冻结记录
	 *
	 * @param userFreeze
	 * @return
	 */
	public int updateUserFreeze(TUserFreeze userFreeze) {
		return userFreezeDao.update(userFreeze);
	}

	/**
	 * 批量任务完成(包含增加成长值、等级提升、根据用户等级修改用户授信额度、任务记录)
	 *
	 * @param user
	 * @param growthValueEnum
	 */
	public void taskComplete(TUser user, GrowthValueEnum growthValueEnum, Integer counts) {
		userService.taskComplete(user, growthValueEnum, counts);
	}

	/**
	 * 插入一条流水
	 *
	 * @param record
	 * @return
	 */
	public Long insertUserTimeRecords(TUserTimeRecord record) {
		return userTimeRecordDao.insert(record);
	}

	/**
	 * 根据主键查找流水表
	 * @param id
	 * @return
	 */
	public TUserTimeRecord selectUserTimeRecordById(Long id){return userTimeRecordDao.selectById(id);}

	/**
	 * 查询用户再某条订单下的冻结记录
	 * @param createUser 用户ID
	 * @param orderId 订单ID
	 * @return 冻结记录
	 */
	public TUserFreeze getUserFreeze(Long createUser, Long orderId) {
		return userFreezeDao.getUserFreeze(createUser, orderId);
	}

	/**
	 * 增加用户发布次数
	 * @param user 用户
	 * @param type 类型 1、求助 2、服务
	 */
	public void addPublishTimes(TUser user, int type) {
		userService.addPublishTimes(user, type);
	}

	/**
	 * 是否关注了该用户
	 * @param userId 当前用户ID
	 * @param userFollowId 被关注用户ID
	 * @return
	 */
	public boolean isCareUser(Long userId, Long userFollowId) {
		return userService.isCareUser(userId, userFollowId);
	}


	/**
	 * 发送短信
	 * @param telephone
	 * @param content
	 */
	public boolean sendSMS(String telephone, String content) {
		return userService.genrateSMSWithContent(telephone,content);
	}

	/**
	 * 查找一个订单的所有流水
	 * @param orderId
	 * @return
	 */
	public List<TUserTimeRecord> selectGetTimeByOrder(Long orderId){return userTimeRecordDao.selectGetTimeByOrder(orderId);}

	/**
	 * 获取微信token
	 */
	public String getWechatToken() {
		return wechatService.getToken();
	}

}
