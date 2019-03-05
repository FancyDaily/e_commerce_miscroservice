//package com.e_commerce.miscroservice.product.service.impl;
//
//import com.e_commerce.miscroservice.commons.entity.application.*;
//import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
//import com.e_commerce.miscroservice.commons.helper.log.Log;
//import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
//import com.e_commerce.miscroservice.commons.util.colligate.SnowflakeIdWorker;
//import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
//import com.e_commerce.miscroservice.order.service.impl.OrderServiceImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.util.HtmlUtils;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * @author 马晓晨
// * @date 2019/3/4
// */
//public class BaseService {
//	/**
//	 * 有效
//	 */
//	protected String IS_VALID_YES = "1";
//	/**
//	 * 无效
//	 */
//	protected String IS_VALID_NO = "0";
//	/**
//	 * 是封面图片
//	 */
//	protected String IS_COVER_YES = "1";
//	/**
//	 * 是组织账户
//	 */
//	protected Integer IS_COMPANY_ACCOUNT_YES = 1;
//	/**
//	 * 用户类型为公益组织
//	 */
//	protected String USER_TYPE_ORGANIZATION = "2";
//	/**
//	 * 主键生成器
//	 */
//	protected SnowflakeIdWorker idGenerator = new SnowflakeIdWorker();
//
//	@Autowired
//	protected OrderServiceImpl orderServiceImpl;
//
//	protected SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();
//
//	@Autowired
//	protected RedisUtil redisUtil;
//
//	protected Log logger = Log.getInstance(BaseService.class);
//
////	@Autowired
////	protected ObjectMapper objectMapper;
//
//
//	/**
//	 * 功能描述:根据serviceid获取订单列表
//	 * 作者:马晓晨
//	 * 创建时间:2018年11月22日 上午10:34:50
//	 * @param serviceId
//	 */
//	protected List<TServiceReceipt> selectReceiptsByServiceId(Long serviceId) {
////		TServiceReceiptExample serviceReceiptExample = new TServiceReceiptExample();
////		TServiceReceiptExample.Criteria serviceReceiptCriteria = serviceReceiptExample.createCriteria();
////		serviceReceiptCriteria.andIsValidEqualTo(IS_VALID_YES);
////		serviceReceiptCriteria.andServiceIdEqualTo(serviceId);
////		List<TServiceReceipt> serviceReceipt = serviceReceiptDao.selectByExample(serviceReceiptExample);
////		return serviceReceipt;
//		return null;
//	}
//
//	/**
//	 *
//	 * 功能描述:根据serviceid获取订单列表的id
//	 * 作者:马晓晨
//	 * 创建时间:2018年11月22日 上午10:40:10
//	 * @param serviceId
//	 * @return
//	 */
//	protected List<Long> selectReceiptIdsByServiceId(Long serviceId) {
//		List<TServiceReceipt> listReceipt = selectReceiptsByServiceId(serviceId);
//		List<Long> ids = new ArrayList<>(listReceipt.size() + 1);
//		for (int i = 0; i < listReceipt.size(); i++) {
//			ids.add(listReceipt.get(i).getId());
//		}
//		return ids;
//	}
//
//	/**
//	 * 赋值servcieDesc公共字段
//	 * @param user
//	 * @param desc
//	 */
//	protected void setCommonServcieDescField(TUser user, TServiceDescribe desc) {
//		long currentTime = System.currentTimeMillis();
//		desc.setCreateUser(user.getId());
//		desc.setCreateUserName(user.getName());
//		desc.setCreateTime(currentTime);
//		desc.setUpdateUser(user.getId());
//		desc.setUpdateUserName(user.getName());
//		desc.setUpdateTime(currentTime);
//		desc.setIsValid(IS_VALID_YES);
//	}
//
//	/**
//	 * 赋值service的公共字段
//	 *
//	 * @param user
//	 * @param service
//	 */
//	protected void setServiceCommonField(TUser user, TService service) {
//		long currentTime = System.currentTimeMillis();
//		service.setCreateUser(user.getId());
//		service.setCreateUserName(user.getName());
//		service.setCreateTime(currentTime);
//		service.setUpdateUser(user.getId());
//		service.setUpdateUserName(user.getName());
//		service.setUpdateTime(currentTime);
//		service.setIsValid(IS_VALID_YES);
//	}
//
//	/**
//	 * 功能描述:根据用户id的list获取用户
//	 * 作者:马晓晨
//	 * 创建时间:2018年11月8日 下午2:34:11
//	 * @param userIds
//	 * @return
//	 */
//	protected List<TUser> getListUser(List<Long> userIds) {
////		TUserExample userExample = new TUserExample();
////		TUserExample.Criteria userCriteria = userExample.createCriteria();
////		userCriteria.andIdIn(userIds);
////		// 服务者
////		List<TUser> listServ = userDao.selectByExample(userExample);
////		return listServ;
//	}
//
//	/**
//	 * 功能描述:获取用户的所有服务求助列表
//	 * 作者:马晓晨
//	 * 创建时间:2018年11月10日 下午3:36:06
//	 * @param type
//	 */
//	protected List<TService> getUserServiceList(Long userId, Integer type) {
////		TServiceExample serviceExample = new TServiceExample();
////		TServiceExample.Criteria serviceCriteria = serviceExample.createCriteria();
////		serviceCriteria.andIsValidEqualTo(IS_VALID_YES);
////		serviceCriteria.andUserIdEqualTo(userId);
////		serviceCriteria.andSourceEqualTo(SERVICE_SOURCE_PERSON);
////		serviceCriteria.andStatusBetween(2, 3);
////		if (type != null) {
////			serviceCriteria.andTypeEqualTo(type);
////		}
////		List<TService> userServiceList = serviceDao.selectByExample(serviceExample);
////		return userServiceList;
//	}
//
//	/**
//	 * 功能描述:publish获取value
//	 * 作者:马晓晨
//	 * 创建时间:2018年11月21日 下午8:36:07
//	 * @return
//	 */
//	protected String getValueByKey() {
////		TPublishExample publishExample = new TPublishExample();
////		TPublishExample.Criteria publishCriteria = publishExample.createCriteria();
////		publishCriteria.andMainKeyEqualTo("allType");
////		publishCriteria.andIsValidEqualTo(IS_VALID_YES);
////		List<TPublish> listPublish = publishDao.selectByExampleWithBLOBs(publishExample);
////		TPublish publish = null;
////		if (listPublish != null && listPublish.size() > 0) {
////			publish = listPublish.get(0);
////		}
////		String value = publish.getValue();
////		return value;
//	}
//	/**
//	 * 功能描述:publish获取value
//	 * 作者:马晓晨
//	 * 创建时间:2018年11月21日 下午8:36:07
//	 * @return
//	 */
//	protected String getValueByKeyBest(String key) {
////		TPublishExample publishExample = new TPublishExample();
////		TPublishExample.Criteria publishCriteria = publishExample.createCriteria();
////		publishCriteria.andMainKeyEqualTo(key);
////		publishCriteria.andIsValidEqualTo(IS_VALID_YES);
////		List<TPublish> listPublish = publishDao.selectByExampleWithBLOBs(publishExample);
////		TPublish publish = null;
////		if (listPublish != null && listPublish.size() > 0) {
////			publish = listPublish.get(0);
////		}
////		String value = publish.getValue();
////		return value;
//	}
//	/**
//	 * 功能描述:获取服务类型
//	 * 作者:马晓晨
//	 * 创建时间:2018年11月21日 下午9:56:52
//	 */
//	protected String getServiceType(Long serviceTypeId) {
////		String value = getValueByKey();
////		//解析json
////		try {
////			List<AllTypeJsonEntity> listType = objectMapper.readValue(value,new TypeReference<List<AllTypeJsonEntity>>() { });
////			for (int i = 0; i < listType.size(); i++) {
////				AllTypeJsonEntity jsonEntity = listType.get(i);
////				if (Long.parseLong(jsonEntity.getId()) == serviceTypeId.longValue()) {
////					return jsonEntity.getTitle();
////				}
////			}
////			return "";
////		} catch (IOException e) {
////			e.printStackTrace();
////			logger.error("解析字典表allType关键字的json出错，" + e.getMessage());
////			return "";
////		}
//	}
//
//	/**
//	 * 功能描述:获取求助服务类型的map  key:serviceTypeId  value:serviceTypeName
//	 * 作者:马晓晨
//	 * 创建时间:2019年1月16日 下午12:07:06
//	 * @return
//	 */
//	protected Map<String, String> getServiceTypeMap() {
////		//获取类型的值
////		String valueJson = getValueByKey();
////		//将type的key（id）和value（type）存放的map中
////		Map<String, String> serviceTypeMap = new HashMap<>();
////		//解析json
////		try {
////			List<AllTypeJsonEntity> listType = objectMapper.readValue(valueJson,new TypeReference<List<AllTypeJsonEntity>>() { });
////			for (int i = 0; i < listType.size(); i++) {
////				AllTypeJsonEntity jsonEntity = listType.get(i);
////				serviceTypeMap.put(jsonEntity.getId(), jsonEntity.getTitle());
////			}
////		} catch (IOException e) {
////			logger.error("解析字典表allType关键字的json出错，" + errInfo(e));
////		}
////		return serviceTypeMap;
//	}
//
//
//	/**
//	 * 功能描述:获取服务类型
//	 * 作者:马晓晨
//	 * 创建时间:2018年11月21日 下午9:56:52
//	 */
//	protected String getReportValue(Long serviceTypeId) {
////		String value = getValueByKeyBest("complaint");
////		//解析json
////		try {
////			List<ComplaintJsonEntity> listType = objectMapper.readValue(value,new TypeReference<List<ComplaintJsonEntity>>() { });
////			for (int i = 0; i < listType.size(); i++) {
////				ComplaintJsonEntity jsonEntity = listType.get(i);
////				if (Long.parseLong(jsonEntity.getId()) == serviceTypeId.longValue()) {
////					return jsonEntity.getName();
////				}
////			}
////			return "";
////		} catch (IOException e) {
////			e.printStackTrace();
////			logger.error("解析字典表complaint关键字的json出错，" + e.getMessage());
////			return "";
////		}
//	}
//
//	/**
//	 *
//	 * 功能描述:输出错误消息
//	 * 作者:马晓晨
//	 * 创建时间:2018年12月9日 下午2:26:43
//	 * @param e
//	 * @return
//	 */
//	public String errInfo(Exception e) {
//		StringWriter sw = null;
//		PrintWriter pw = null;
//		try {
//			sw = new StringWriter();
//			pw = new PrintWriter(sw);
//			// 将出错的栈信息输出到printWriter中
//			e.printStackTrace(pw);
//			pw.flush();
//			sw.flush();
//		} finally {
//			if (sw != null) {
//				try {
//					sw.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//			if (pw != null) {
//				pw.close();
//			}
//		}
//		return sw.toString();
//	}
//
//	/**
//	 * 功能描述：登录用户是否关注这个用户
//	 * 作者:马晓晨
//	 * 创建时间:2018年11月16日 下午8:57:48
//	 * @param currentUserId
//	 * @param publisherId
//	 * @return
//	 */
//	protected boolean isCare(Long currentUserId, Long publisherId) {
////		TUserFollowExample userFollowExample = new TUserFollowExample();
////		TUserFollowExample.Criteria userFollowCriteria = userFollowExample.createCriteria();
////		userFollowCriteria.andUserIdEqualTo(currentUserId);
////		userFollowCriteria.andUserFollowIdEqualTo(publisherId);
////		userFollowCriteria.andIsValidEqualTo(IS_VALID_YES);
////		long count = userFollowDao.countByExample(userFollowExample);
////		return count > 0;
//	}
//
//	/**
//	 * 将正常字符转换为Unicode
//	 * @param source
//	 * @return
//	 */
//	public String unicode(String source) {
//		StringBuffer sb = new StringBuffer();
//		char[] source_char = source.toCharArray();
//		String unicode = null;
//		for (int i = 0; i < source_char.length; i++) {
//			unicode = Integer.toHexString(source_char[i]);
//			if (unicode.length() <= 2) {
//				unicode = "00" + unicode;
//			}
//			sb.append("\\u" + unicode);
//		}
//		return sb.toString();
//	}
//
//	/**
//	 * 将Unicode编码转换为正常字符
//	 * @param unicode
//	 * @return
//	 */
//	public String decodeUnicode(String unicode) {
//		if (unicode.isEmpty()) {
//			return "";
//		} else {
//			StringBuffer sb = new StringBuffer();
//			String[] hex = unicode.split("\\\\u");
//			for (int i = 1; i < hex.length; i++) {
//				int data = Integer.parseInt(hex[i], 16);
//				sb.append((char) data);
//			}
//			return sb.toString();
//		}
//	}
//
//	/**
//	 * 功能描述: 将unicode转换成正常字符，包含html转码
//	 * 作者: 许方毅
//	 * 创建时间: 2018年11月12日 下午3:48:57
//	 * @param str
//	 * @return
//	 */
//	public String htmlDeUnicode(String str) {
//		return HtmlUtils.htmlUnescape(decodeUnicode(str));
//	}
//
//	/**
//	 * 功能描述: 默认用户redis有效时间
//	 * 作者: 许方毅
//	 * 创建时间: 2018年11月13日 下午3:04:18
//	 * @return
//	 */
//	public Long getUserTokenInterval() {
//		// Generate token for user
//		Long t1 = Calendar.getInstance().getTimeInMillis();
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DATE, 1);
//		Long t2 = cal.getTimeInMillis() - t1;
//		return t2 / 1000;
//	}
//	/**
//	 *
//	 * 功能描述:分钟数转为*小时*分钟 显示
//	 * 作者:姜修弘
//	 * 创建时间:2018年11月22日 下午2:46:28
//	 * @param time
//	 * @return
//	 */
//	public String timeChange(Long time) {
//		int hour = (int) (time / 60) ;
//		int minute = (int) (time % 60) ;
//		String showTime = hour+"小时"+minute+"分钟";
//		if (hour == 0) {
//			showTime = minute+"分钟";
//		}
//		if (minute == 0) {
//			showTime = hour+"小时";
//		}
//		return showTime;
//	}
//
//	/**
//	 * 功能描述:获取服务求助报名人员
//	 * 作者:马晓晨
//	 * 创建时间:2018年11月26日 下午5:23:48
//	 * @param serviceId
//	 * @return
//	 */
//	protected List<TTypeDictionaries> getSignUpPersonal(Long serviceId) {
////		TTypeDictionariesExample tTypeDictionariesExample = new TTypeDictionariesExample();
////		TTypeDictionariesExample.Criteria typeDicCriteria = tTypeDictionariesExample.createCriteria();
////		typeDicCriteria.andEntityIdEqualTo(serviceId);
////		typeDicCriteria.andTypeEqualTo(2);
////		typeDicCriteria.andSubTypeEqualTo(0);
////		typeDicCriteria.andIsValidEqualTo(IS_VALID_YES);
////		List<TTypeDictionaries> typeDicList = typeDictionariesDao.selectByExample(tTypeDictionariesExample);
////		return typeDicList;
//	}
//
//	/**
//	 * 功能描述:根据serviceid来获取service（先从redis中获取）
//	 * 作者:马晓晨
//	 * 创建时间:2018年12月4日 下午10:40:51
//	 * @param serviceId
//	 * @return
//	 */
//	protected TService getServiceById(Long serviceId) {
////		String key = "t_service_" + serviceId;
////		try {
////			Object redis = redisUtil.get(key);
////			if (redis != null) {
////				return (TService) redis;
////			}
////		} catch (Exception e) {
////			redisUtil.del(key);
////		}
////		//从数据库中获取
////		TService service = serviceDao.selectByPrimaryKey(serviceId);
////		//设置缓存
////		try {
////			redisUtil.set(key, service, 86400);
////		} catch (Exception e) {
////			redisUtil.del(key);
////		}
////		return service;
//	}
//	/**
//	 *
//	 * 功能描述:修改时间为yyyy-MM-dd HH:mm
//	 * 作者:姜修弘
//	 * 创建时间:2019年1月15日 下午1:59:21
//	 * @param time
//	 * @return
//	 */
//	public String changeTime(Long time) {
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		String startTime = simpleDateFormat.format(time);
//		return startTime;
//	}
//	/**
//	 *
//	 * 功能描述:修改时间为汉字的日期 yyyy年MM月dd日 HH:mm
//	 * 作者:姜修弘
//	 * 创建时间:2019年1月25日 上午10:31:47
//	 * @param time
//	 * @return
//	 */
//	public String changeTimeToChian(Long time) {
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
//		String startTime = simpleDateFormat.format(time);
//		return startTime;
//	}
//	/**
//	 *
//	 * 功能描述:修改时间为时分 HH:mm
//	 * 作者:姜修弘
//	 * 创建时间:2019年1月25日 上午10:32:24
//	 * @param time
//	 * @return
//	 */
//	public String changeTimeToHour(Long time) {
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
//		String startTime = simpleDateFormat.format(time);
//		return startTime;
//	}
//
//
//	/**
//	 * 功能描述: 获取组织账号对应组织编号
//	 * 作者: 许方毅
//	 * 创建时间: 2019年1月14日 下午1:45:04
//	 * @param id
//	 * @return
//	 */
//	protected Long getOwnCompanyId(Long id) {
////		TUserCompanyExample userCompanyExample = new TUserCompanyExample();
////		TUserCompanyExample.Criteria criteria = userCompanyExample.createCriteria();
////		criteria.andUserIdEqualTo(id);
////		criteria.andCompanyJobEqualTo(AppConstant.JOB_COMPANY_CREATER);
////		criteria.andIsValidEqualTo(AppConstant.IS_VALID_YES);
////		List<TUserCompany> userCompanies = userCompanyDao.selectByExample(userCompanyExample);
////		if(userCompanies.isEmpty()) {
////			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,"没有对应组织！");
////		}
////		return userCompanies.get(0).getCompanyId();
//	}
//
//	/**
//	 *
//	 * 功能描述:修改时间为yyyy-MM
//	 * 作者:姜修弘
//	 * 创建时间:2019年1月18日 下午12:04:56
//	 * @param time
//	 * @return
//	 */
//	public String changeTimeForTimeRecord(Long time) {
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
//		String startTime = simpleDateFormat.format(time);
//		return startTime;
//	}
//
//	/**
//	 *
//	 * 功能描述:修改时间为年份
//	 * 作者:姜修弘
//	 * 创建时间:2019年1月18日 下午12:04:40
//	 * @param time
//	 * @return
//	 */
//	public String changeTimeToYear(Long time) {
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
//		String startTime = simpleDateFormat.format(time);
//		return startTime;
//	}
//
//
//	/**
//	 * 功能描述:根据serviceid获取listService
//	 * 作者:马晓晨
//	 * 创建时间:2019年2月20日 下午4:47:57
//	 * @param listServiceId
//	 */
//	protected List<TService> getListServiceByIds(List<Long> listServiceId) {
////		if (listServiceId.size() == 0) {
////			return new ArrayList<TService>();
////		}
////		TServiceExample serviceExample = new TServiceExample();
////		TServiceExample.Criteria serviceCriteria = serviceExample.createCriteria();
////		serviceCriteria.andIdIn(listServiceId);
////		serviceCriteria.andIsValidEqualTo(IS_VALID_YES);
////		List<TService> listService = serviceDao.selectByExample(serviceExample);
////		return listService;
//	}
//	/**
//	 * 功能描述:根据serviceid获取listServiceDesc
//	 * 作者:马晓晨
//	 * 创建时间:2019年2月20日 下午4:47:57
//	 * @param listServiceId
//	 */
//	protected List<TServiceDescribe> getListServiceDescByServiceIds(List<Long> listServiceId) {
////		if (listServiceId.size() == 0) {
////			return new ArrayList<TServiceDescribe>();
////		}
////		TServiceDescribeExample serviceDescExample = new TServiceDescribeExample();
////		TServiceDescribeExample.Criteria serviceDescCriteria = serviceDescExample.createCriteria();
////		serviceDescCriteria.andServiceIdIn(listServiceId);
////		serviceDescCriteria.andIsValidEqualTo(IS_VALID_YES);
////		List<TServiceDescribe> listServiceDesc = serviceDescribeDao.selectByExample(serviceDescExample);
////		return listServiceDesc;
//	}
//	/**
//	 * 功能描述:获取封面图片
//	 * 作者:马晓晨
//	 * 创建时间:2019年2月20日 下午4:47:57
//	 * @param listServiceId
//	 */
//	protected List<TServiceDescribe> getListServiceCoverImg(List<Long> listServiceId) {
///*		if (listServiceId.size() == 0) {
//			return new ArrayList<TServiceDescribe>();
//		}
//		TServiceDescribeExample serviceDescExample = new TServiceDescribeExample();
//		TServiceDescribeExample.Criteria serviceDescCriteria = serviceDescExample.createCriteria();
//		serviceDescCriteria.andServiceIdIn(listServiceId);
//		serviceDescCriteria.andIsValidEqualTo(IS_VALID_YES);
//		serviceDescCriteria.andIsCoverEqualTo(IS_COVER_YES);
//		List<TServiceDescribe> listServiceDesc = serviceDescribeDao.selectByExample(serviceDescExample);
//		return listServiceDesc;*/
//	}
//}
