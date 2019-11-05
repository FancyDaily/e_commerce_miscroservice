package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.POIUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.*;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceUserOutVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-09 16:18
 */
@Service
@Log
public class CsqDataTransferServiceImpl implements CsqDataTransferService {

	@Autowired
	private CsqOfflineDataDao csqOfflineDataDao;

	@Autowired
	private CsqOldUserDao csqOldUserDao;

	@Autowired
	private CsqUserService csqUserService;

	@Autowired
	private CsqUserDao csqUserDao;

	@Autowired
	private CsqOldPaymentDao csqOldPaymentDao;

	@Autowired
	private CsqOldServiceDao csqOldServiceDao;

	@Autowired
	private CsqUserPaymentDao csqUserPaymentDao;

	@Autowired
	private CsqPayService csqPayService;

	@Autowired
	private CsqFundDao csqFundDao;

	@Autowired
	private CsqServiceService csqServiceService;

	@Autowired
	private CsqFundService csqFundService;

	@Autowired
	private CsqServiceDao csqServiceDao;

	@Autowired
	CsqPaymentService csqPaymentService;

	@Autowired
	CsqOrderDao csqOrderDao;

	@Override
	public void dealWithOpenid() {
		//处理openid
		MybatisPlus instance = MybatisPlus.getInstance();
		List<TOldOpenId> all = instance.findAll(new TOldOpenId(), new MybatisPlusBuild(TOldOpenId.class));
		ArrayList<TOldUser> toUpdaters = new ArrayList<>();
		Map<String, String> cache = new ConcurrentHashMap<>();
		all.stream()
			.forEach(a -> {
				if (cache.putIfAbsent(a.getOpenid(), a.getOpenid()) == null) {

					String userid = a.getUserid();
					TOldUser tOldUser = new TOldUser();
					tOldUser.setId(userid);
					tOldUser.setOpenid(a.getOpenid());
					toUpdaters.add(tOldUser);
				}

			});

		//将toUpdaters分段
		ArrayList<TOldUser> toUpdater = new ArrayList<>();
		for (int i = 0; i < toUpdaters.size(); i++) {
			/*if(toUpdaters.size() < 500) {
				csqOldUserDao.update(toUpdaters);
				break;
			}
			toUpdater.add(toUpdaters.get(i));
			if((i > 500 && i % 500 == 0) || i == toUpdater.size() - 1) {	//每五百转更新一次
				csqOldUserDao.update(toUpdater);
				toUpdater = new ArrayList<>();
			}*/

			csqOldUserDao.update(toUpdaters.get(i));
		}
	}

	@Override
	public Object getSthInCommon(List<TOldUser> tOldUsers) {
		List<TCsqUser> tCsqUsers = csqUserDao.selectAll();
		ArrayList<TCsqUser> objects = new ArrayList<>();
		//遍历判断
		tCsqUsers.stream()
			.forEach(a -> {
				String userTel = a.getUserTel();
				String vxOpenId = a.getVxOpenId();
				tOldUsers.stream()
					.forEach(b -> {
						if (a.getAccountType().equals(CsqUserEnum.ACCOUNT_TYPE_PERSON.toCode())) {    //个人用户
							if (vxOpenId != null && vxOpenId.equals(b.getOpenid())) {    //发现重复的openid
								//记录
								a.setOldId(Long.valueOf(b.getId()));
								log.warn("发现重复的用户标示, user={}", a);
								objects.add(a);
							}
						} /*else {
							if(userTel!= null && userTel.equals(b.getAccount())) {	//发现重复的手机号

							}
						}*/
					});
			});
		return objects;
	}

	@Override
	public void transferUser() {
		List<TOldUser> theUsers = csqOldUserDao.selectAll();
		ArrayList<TCsqUser> sthInCommon = (ArrayList<TCsqUser>) getSthInCommon(theUsers);    //包含重复用户信息
		List<Long> unexpectedIds = sthInCommon.stream()
			.map(TCsqUser::getOldId).collect(Collectors.toList());

		//处理用户与流水(1/2 账户充值流水)
		List<TOldUser> tOldUsers = csqOldUserDao.selectAll();
		List<TCsqUserPaymentRecord> toInserterPayment = new ArrayList<>();
		List<TOldService> tOldServices = csqOldServiceDao.selectByNames("随手捐");
		List<String> serviceIds = tOldServices.stream()
			.map(TOldService::getId).collect(Collectors.toList());
		tOldUsers.stream()
			.forEach(a -> {
				String theId = a.getId();
				if (theId.equals("156747275200000001")) {
					System.out.println("got you!");
				}
				if (!unexpectedIds.contains(Long.valueOf(theId))) {
					//有用信息
					boolean flag = false;
					String id = theId;
					if (id.equals("153820346900000003")) {
						flag = true;
					}
					Long oldId = Long.valueOf(theId);
					String userTel = a.getAccount();
					String openid = a.getOpenid();
//				Long createTime = Long.valueOf(a.getAddtime() + "000");
					Long createTime = Long.valueOf(DateUtil.dateToStamp(a.getAddtime()));

					String nickname = a.getNickname();
					String regex = "^[1-9]\\d*$";
					Pattern compile = Pattern.compile(regex);
					boolean matches = compile.matcher(nickname).matches();
					TCsqUser build = TCsqUser.builder()
						.name(nickname.equals("爱心人士") || matches ? null : nickname)
						.oldId(oldId)    //旧用户id
						.userTel(userTel)    //手机号码
						.mail(a.getEmail())
						.vxOpenId(openid).build();    //微信id
					build.setCreateTime(new Timestamp(createTime));
					//余额 -> 统计捐赠到随手捐或者爱心账户的数额 并 使得他们变成余额
//				List<TOldService> tOldServices = csqOldServiceDao.selectByNames("随手捐", "成长基金");
					List<TOldPayment> tOldPayments = serviceIds.isEmpty() ? new ArrayList<>() : csqOldPaymentDao.selectByOptionUserInPFId(theId, serviceIds);

					tOldPayments.stream()
						.forEach(b -> {
							String donationmoney = b.getDonationmoney();
							TCsqUserPaymentRecord build1 = TCsqUserPaymentRecord.builder()
								.orderId(null)    //缺失
								.money(Double.valueOf(donationmoney == null ? "0" : donationmoney))    //金额
								.entityType(CsqEntityTypeEnum.TYPE_HUMAN.toCode())    //账户余额
								.entityId(null)
								.description("充值")
								.inOrOut(CsqUserPaymentEnum.INOUT_IN.toCode())    //收入
								.build();
							build1.setExtend(b.getOptionuser());    //TODO extend存放旧id
							String donationtime = b.getDonationtime();
							build1.setCreateTime(new Timestamp(Long.valueOf(DateUtil.dateToStamp(donationtime.substring(0, donationtime.length() - 4)))));
							toInserterPayment.add(build1);
						});

					Double money = tOldPayments.stream()
						.map(TOldPayment::getDonationmoney).map(Double::valueOf)
						.reduce(0d, Double::sum);

					build.setSurplusAmount(money);    //余额
					csqUserService.testRegister(build);

				}
			});
//		csqUserDao.insert(toInserters);
		csqUserPaymentDao.insert(toInserterPayment);
	}

	@Override
	public void dealWithPayment() {
		List<TOldUser> tOldUsers = csqOldUserDao.selectAll();

		List<TCsqUserPaymentRecord> toInserterPayment = new ArrayList<>();
		tOldUsers.stream()
			.forEach(a -> {
				innerDealPayment(toInserterPayment, a);
			});
		csqUserPaymentDao.insert(toInserterPayment);
	}

	private void innerDealPayment(List<TCsqUserPaymentRecord> toInserterPayment, TOldUser a) {
		List<TOldService> tOldServices = csqOldServiceDao.selectByNames("随手捐");
		List<String> serviceIds = tOldServices.stream()
			.map(TOldService::getId).collect(Collectors.toList());
		List<TOldPayment> tOldPayments = csqOldPaymentDao.selectByOptionUserInPFId(a.getId(), serviceIds);

		tOldPayments.stream()
			.forEach(b -> {
				String donationmoney = b.getDonationmoney();
				TCsqUserPaymentRecord build1 = TCsqUserPaymentRecord.builder()
					.orderId(null)    //缺失
					.money(Double.valueOf(donationmoney == null ? "0" : donationmoney))    //金额
					.entityType(CsqEntityTypeEnum.TYPE_HUMAN.toCode())    //账户余额
					.entityId(null)
					.inOrOut(CsqUserPaymentEnum.INOUT_IN.toCode())    //收入
					.description("充值")
					.build();
				build1.setExtend(b.getOptionuser());    //TODO extend存放旧id
				String donationtime = b.getDonationtime();
				build1.setCreateTime(new Timestamp(Long.valueOf(DateUtil.dateToStamp(donationtime.substring(0, donationtime.length() - 4)))));
				toInserterPayment.add(build1);
			});
	}

	@Override
	public void dealWithOldIdAtPayment() {    //TODO 处理旧userId （extend)  -> 新userId(entityId)
		//在transferUser()之后
		List<TCsqUserPaymentRecord> paymentsWithOldIds = csqUserPaymentDao.selectByNotNullExtend();
		List<TCsqUserPaymentRecord> toUpdater = paymentsWithOldIds.stream()
			.map(a -> {
				String oldIdStr = a.getExtend();
				TCsqUser userWithCurrentId = csqUserDao.selectByOldId(oldIdStr);
				Long userId = userWithCurrentId.getId();

				TCsqUserPaymentRecord csqUserPaymentRecord = TCsqUserPaymentRecord.builder()
					.userId(userId)
					.id(a.getId())
					.entityId(userId)    //补缺 -> 获取正确的用户编号
					.build();
//				csqUserPaymentRecord.setExtend(null);	//清理
				return csqUserPaymentRecord;
			}).collect(Collectors.toList());
		csqUserPaymentDao.update(toUpdater);
	}

	@Override
	public String getGrowthFundRecords() {
		//获取成长基金捐赠记录(Json)
		List<TOldService> oldServices = csqOldServiceDao.selectByNames("成长基金");
		if (oldServices.isEmpty()) {
			return null;
		}
		TOldService oldService = oldServices.get(0);
		String pfId = oldService.getId();
		List<TOldPayment> tOldPayments = csqOldPaymentDao.selectInPfIds(Arrays.asList(pfId));
		Map<String, List<TOldPayment>> collect = tOldPayments.stream()
			.collect(Collectors.groupingBy(TOldPayment::getOptionuser));

		Map<String, Double> doubleMap = new HashMap<>();    //手机号-捐款到"成长基金"总和map
		ArrayList<String> failList = new ArrayList<>();
		collect.forEach((k, v) -> {
			TOldUser tOldUser = csqOldUserDao.selectByPrimaryKey(Long.valueOf(k));
			try {
				String account = tOldUser.getAccount();
				doubleMap.put(account, v.stream().map(TOldPayment::getDonationmoney).map(Double::valueOf).reduce(0d, Double::sum));
			} catch (NullPointerException e) {
				failList.add(k);
				log.error("oldId={}不存在,失败次数={}, 程序继续进行...", k, failList.size());
			} catch (Exception e) {
				failList.add(k);
				log.error("错误,失败次数={}, 程序继续进行...", k, failList.size());
			}
		});

		return JSON.toJSONString(doubleMap);
	}

	@Override
	public void dealWithFundNService() {
//		List<TOldService> tOldFunds = csqOldServiceDao.selectByStatusByCheckStatusAndAdderEqCHargePersonIdAndFinType(1, 1, "个人冠名基金");
		List<TOldService> tOldFunds = csqOldServiceDao.selectByStatusByCheckStatusByDTypeAndDonationAmountNeq(1, 1, "Fund", "0.00");
		List<TOldService> tOldServices = csqOldServiceDao.selectByStatusByCheckStatusByDType(1, 1, "pf");
		//Map
		Map<String, List<TOldService>> fundMap = tOldFunds.stream()
			.collect(Collectors.groupingBy(TOldService::getId));

		Map<String, List<TOldService>> pfMap = tOldServices.stream()
			.collect(Collectors.groupingBy(TOldService::getId));
		//区分
		List<TOldService> pfTypeServices = tOldServices
//			.stream()
//			.filter(a -> "pf".equals(a.getDtype()))
//			.collect(Collectors.toList())
			;
		List<String> oldServiceIds = pfTypeServices.stream()
			.map(TOldService::getId).collect(Collectors.toList());

		List<TOldService> fundTypeServices = tOldFunds.stream()
			.filter(a -> "Fund".equals(a.getDtype())).collect(Collectors.toList());

		List<String> oldFundIds = fundTypeServices.stream()
			.map(TOldService::getId).collect(Collectors.toList());
		//流水
		List<TOldPayment> paymentsOfFund = oldFundIds.isEmpty() ? new ArrayList<>() : csqOldPaymentDao.selectInPfIdsAsc(oldFundIds);
		List<TOldPayment> paymentsOfService = oldServiceIds.isEmpty() ? new ArrayList<>() : csqOldPaymentDao.selectInPfIdsAsc(oldServiceIds);

		//TODO 拷贝
		//TODO 1.拷贝基金(排除"随手捐"和"成长基金")到 -> 专项基金 现平台所有信息保留(创建专项基金流程、充值专项基金流程
		Map<String, Integer> pfCount = new HashMap<>();
		Map<String, Long> fundIdMap = new HashMap<>();
		List<Map<String, Object>> toDealWithFakePay = new ArrayList<>();
		paymentsOfFund.stream()
			.forEach(a -> {
				boolean flag = false;
				if ("153820346900000001".equals(a.getOptionuser())) {
					flag = true;
				}
				String pfid = a.getPfid();
				log.info("拷贝基金...当前的pfid={}, paymentId={}", pfid, a.getId());
				List<TOldService> tOldServices1 = fundMap.get(pfid);
				boolean createAFaker = false;
				if (!tOldServices1.isEmpty()) {
					Integer cnt = pfCount.get(pfid);
					if (cnt == null) cnt = 0;
					TOldService tOldService = tOldServices1.get(0);

					Double singleAmount = Double.valueOf(a.getDonationmoney());
					Double amount = Double.valueOf(tOldService.getDonationamount());
					String remark = a.getRemark();
					remark = remark == null ? "" : remark;
					if (amount > 0 && !remark.contains("成长基金")) {    //仅对筹款大于0的做保留
						String detail = tOldService.getDetail();
						detail = detail == null ? "" : detail;
						Long userId = getUserId(a.getOptionuser());    //找到 new
						if (cnt == 0) {
							//作为创建者 -> 找到
							String chargepersonid = tOldService.getChargepersonid();
							if (!"个人冠名基金".equals(tOldService.getFintype())) {    //若不是个人冠名基金类型
								//创建一个用户
								TCsqUser csqUser = csqUserService.testRegister(
									TCsqUser.builder()
										.remarks("为基金名\"" + tOldService.getTitle() + "\",旧编号为:" + tOldService.getId() + "而创建的假用户，该基金负责人名称为" + tOldService.getChargeperson() + ",编号为" + getUserId(chargepersonid)).build()
								);
								userId = csqUser.getId();
								createAFaker = true;
							} else {
								userId = Long.valueOf(getUserId(chargepersonid));
							}
							log.info("找到创建者 -> oldId={}, 对应userId={}", chargepersonid, userId);

							TCsqFund.TCsqFundBuilder builder = TCsqFund
								.builder()
								.name(tOldService.getTitle())
								.description(detail.length() > 511 ? "" : detail)
								.trendPubKeys("1,2,3,4");

							if (Double.valueOf(tOldService.getDonationamount()) > 100000) {    //若已达开放条件，则补全（使用占位
								builder = builder.coverPic("https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png")
									.detailPic("https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png");
							}

							TCsqFund build = builder
								.build();
							build.setExtend(pfid);    //旧的基金编号
							Long fundId;
							if (createAFaker) {    //代持
								build.setUserId(userId);
								build.setStatus(CsqFundEnum.STATUS_ACTIVATED.getVal());
								csqFundDao.insert(build);
								fundId = build.getId();
							} else {
								fundId = csqPayService.fakeWechatPay(userId, null, CsqEntityTypeEnum.TYPE_FUND.toCode(), singleAmount,
									build, Long.valueOf(DateUtil.wholeDateToStamp(a.getAddtime()))
									//						.description(a.getDetail())
								);//创建
							}

							if (createAFaker) {
								csqPayService.fakeWechatPay(getUserId(a.getOptionuser()), fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), singleAmount, null, Long.valueOf(DateUtil.dateToStamp(a.getAddtime())));    //单笔充值
								/*Map<String, Object> theHoldingParam = new HashMap<>();
								theHoldingParam.put("userId", userId);
								theHoldingParam.put("entityId", fundId);
								theHoldingParam.put("type", CsqEntityTypeEnum.TYPE_FUND.toCode());
								toDealWithFakePay.add(theHoldingParam);*/
							}


							//fundId map
							fundIdMap.put(pfid, fundId);
						} else {
							if (userId != null) {    //用户数据必须存在
								//							Long fundId = getFundId(pfid);    //找到new
								Long fundId = fundIdMap.get(pfid);
								csqPayService.fakeWechatPay(userId, fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), singleAmount, null, Long.valueOf(DateUtil.dateToStamp(a.getAddtime())));    //单笔充值
							}
						}
						++cnt;
						pfCount.put(pfid, cnt);
					}
				}
			});
		//TODO 2.拷贝项目 -> 项目(创建项目流程、捐赠流程
		paymentsOfService.stream()
			.forEach(b -> {
				String pfid = b.getPfid();
				List<TOldService> oldServices = pfMap.get(pfid);
				if (!oldServices.isEmpty()) {
					TOldService tOldService = oldServices.get(0);
					if (!"随手捐".equals(tOldService.getTitle())) {
						String detail = tOldService.getDetail();
						Integer cnt = pfCount.get(pfid);
						if (cnt == null) {
							cnt = 0;
							//开启项目
							if (cnt == 0) {
								if (Double.valueOf(tOldService.getDonationamount()) >= 1) {
									String adder = tOldService.getAdder();
									Long userId = getUserId(adder);
									TCsqService build = TCsqService.builder()
										.expectedAmount(Double.valueOf(tOldService.getTotalamount()))
										.name(tOldService.getTitle())
										.description(detail.length() > 511 ? "" : detail)
										//								.typePubKeys()	//意向
										.build();
									build.setExtend(tOldService.getId());
									Long serviceId = csqServiceService.publish(userId, build);//需要一个真实的用户id
									fundIdMap.put(pfid, serviceId);
								}
							}
							cnt++;
							pfCount.put(pfid, cnt);
						}
						Long serviceId = fundIdMap.get(pfid);
						//捐赠
						csqPayService.fakeWechatPay(getUserId(b.getOptionuser()), serviceId, CsqEntityTypeEnum.TYPE_SERVICE.toCode(), Double.valueOf(b.getDonationmoney()), null, Long.valueOf(DateUtil.wholeDateToStamp(b.getAddtime())));
					}
				}
			});
	}

	@Override
	public void dealWithFundNeqGuanMing() {
//		List<TOldService> tOldFunds = csqOldServiceDao.selectByStatusByCheckStatusAndAdderEqCHargePersonIdAndFinType(1, 1, "个人冠名基金");
		List<TOldService> tOldFunds = csqOldServiceDao.selectByStatusByCheckStatusByDTypeAndDonationAmountNeqAndFinTypeNeq(1, 1, "Fund", "0.00", "个人冠名基金");
		//Map
		Map<String, List<TOldService>> fundMap = tOldFunds.stream()
			.collect(Collectors.groupingBy(TOldService::getId));
		List<TOldService> fundTypeServices = tOldFunds.stream()
			.filter(a -> "Fund".equals(a.getDtype())).collect(Collectors.toList());

		List<String> oldFundIds = fundTypeServices.stream()
			.map(TOldService::getId).collect(Collectors.toList());
		//流水
		List<TOldPayment> paymentsOfFund = csqOldPaymentDao.selectInPfIdsDesc(oldFundIds);

		//TODO 拷贝
		//TODO 1.拷贝基金(排除"随手捐"和"成长基金")到 -> 专项基金 现平台所有信息保留(创建专项基金流程、充值专项基金流程
		Map<String, Integer> pfCount = new HashMap<>();
		Map<String, Long> fundIdMap = new HashMap<>();
		paymentsOfFund.stream()
			.forEach(a -> {
				String pfid = a.getPfid();
				log.info("拷贝基金...当前的pfid={}, paymentId={}", pfid, a.getId());
				List<TOldService> tOldServices1 = fundMap.get(pfid);
				if (!tOldServices1.isEmpty()) {
					Integer cnt = pfCount.get(pfid);
					if (cnt == null) cnt = 0;
					TOldService tOldService = tOldServices1.get(0);

					Double singleAmount = Double.valueOf(a.getDonationmoney());
					Double amount = Double.valueOf(tOldService.getDonationamount());
					if (amount > 0) {    //仅对筹款大于0的做保留
						String detail = tOldService.getDetail();
						detail = detail == null ? "" : detail;
						Long userId = getUserId(a.getOptionuser());    //找到 new
						if (cnt == 0) {
							//作为创建者 -> 找到
							String chargepersonid = tOldService.getChargepersonid();
							if (!"个人冠名基金".equals(tOldService.getFintype())) {    //若不是个人冠名基金类型
								//创建一个用户
								TCsqUser csqUser = csqUserService.testRegister(
									TCsqUser.builder()
										.remarks("为基金名\"" + tOldService.getTitle() + "\",旧编号为:" + tOldService.getId() + "而创建的假用户，该基金负责人名称为" + tOldService.getChargeperson() + ",编号为" + getUserId(chargepersonid)).build()
								);
								userId = csqUser.getId();
							} else {
								userId = Long.valueOf(getUserId(chargepersonid));
							}
							log.info("找到创建者 -> oldId={}, 对应userId={}", chargepersonid, userId);

							TCsqFund.TCsqFundBuilder builder = TCsqFund
								.builder()
								.name(tOldService.getTitle())
								.description(detail.length() > 511 ? "" : detail)
								.trendPubKeys("1,2,3,4");

							if (Double.valueOf(tOldService.getDonationamount()) > 100000) {    //若已达开放条件，则补全（使用占位
								builder = builder.coverPic("https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png")
									.detailPic("https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png");
							}

							TCsqFund build = builder
								.build();
							build.setExtend(pfid);    //旧的基金编号
							Long fundId = csqPayService.fakeWechatPay(userId, null, CsqEntityTypeEnum.TYPE_FUND.toCode(), singleAmount,
								build, Long.valueOf(DateUtil.wholeDateToStamp(a.getAddtime()))
								//						.description(a.getDetail())
							);//创建
							//fundId map
							fundIdMap.put(pfid, fundId);
						} else {
							if (userId != null) {    //用户数据必须存在
								//							Long fundId = getFundId(pfid);    //找到new
								Long fundId = fundIdMap.get(pfid);
								csqPayService.fakeWechatPay(userId, fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), singleAmount, null, Long.valueOf(DateUtil.wholeDateToStamp(a.getAddtime())));    //单笔充值
							}
						}
						++cnt;
						pfCount.put(pfid, cnt);
					}
				}
			});
	}

	@Override
	public void dealWithPic() {
		//基金和项目。
		List<TCsqService> csqServices = csqServiceService.findAllByTypeAndIdGreaterThan(CsqServiceEnum.TYPE_SERIVE.getCode(), 0L);
		List<TCsqFund> csqFunds = csqFundService.selectAllAndIdGreaterThan(0L);

		//项目
		List<String> serviceNames = csqServices.stream()
			.map(TCsqService::getName).collect(Collectors.toList());
		Map<String, List<TCsqService>> serviceNameMap = csqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getName));

//		List<TCsqService> toUpdaterService = new ArrayList<>();
		List<TOldService> oldServices = csqOldServiceDao.selectInNames(serviceNames);
		List<TCsqService> toUpdaterService = oldServices.stream()
			.map(a -> {
				TCsqService csqService = null;
				String name = a.getTitle();
				List<TCsqService> tCsqServices = serviceNameMap.get(name);
				if (tCsqServices != null) {
					csqService = tCsqServices.get(0);
				}
				if (csqService != null) {
					//TODO 处理前缀 aliyun
					String titleimgurl = a.getTitleimgurl();
					String detailimgurl = a.getDetailimgurl();

					titleimgurl = titleimgurl.replaceAll("/images/", "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/");
					detailimgurl = detailimgurl.replaceAll("/images/", "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/");

					Long id = csqService.getId();
					csqService = TCsqService.builder()
						.id(id)
						.coverPic(titleimgurl)
						.detailPic(detailimgurl).build();
				}
				return csqService;
			}).collect(Collectors.toList());

		//基金 -> TOOD 同步service
//		csqFundService.modifyFund(666L, );
		List<String> fundNames = csqFunds.stream()
			.map(TCsqFund::getName).collect(Collectors.toList());
		Map<String, List<TCsqFund>> fundNameMap = csqFunds.stream()
			.collect(Collectors.groupingBy(TCsqFund::getName));

		List<TOldService> oldFunds = csqOldServiceDao.selectInNames(fundNames);
		List<TCsqFund> toUpdaterFunds = oldFunds.stream()
			.map(a -> {
				TCsqFund csqFund = null;
				String name = a.getTitle();
				List<TCsqFund> tCsqFunds = fundNameMap.get(name);
				if (tCsqFunds != null) {
					csqFund = tCsqFunds.get(0);
				}
				if (csqFund != null) {
					//TODO 处理前缀 aliyun
					String titleimgurl = a.getTitleimgurl();
					String detailimgurl = a.getDetailimgurl();

					titleimgurl = titleimgurl.replaceAll("/images/", "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/");
					detailimgurl = detailimgurl.replaceAll("/images/", "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/");
					Long id = csqFund.getId();
					csqFund = TCsqFund.builder().id(id)
						.coverPic(titleimgurl)
						.detailPic(detailimgurl).build();
				}
				return csqFund;
			}).collect(Collectors.toList());
	/*	//用户头像
		List<TCsqUser> tCsqUsers = csqUserDao.selectAll();
		List<Long> oldIds = tCsqUsers.stream()
			.map(TCsqUser::getOldId).collect(Collectors.toList());
		Map<Long, List<TCsqUser>> oldIdUserMap = tCsqUsers.stream()
			.filter(a -> a != null)
			.collect(Collectors.groupingBy(TCsqUser::getOldId));

		List<TOldUser> tOldUsers = csqOldUserDao.selectInPrimaryKeys(oldIds);
		List<TCsqUser> toUpdaterUsers = tOldUsers.stream()
			.map(a -> {
				TCsqUser csqUser = null;
				String id = a.getId();
				List<TCsqUser> tCsqUsers1 = oldIdUserMap.get(id);
				if(tCsqUsers1 != null) {
					csqUser = tCsqUsers1.get(0);
				}
				if(csqUser != null) {
					a.get
				}
			})
*/
		//update
		csqServiceDao.multiUpdate(toUpdaterService);

		csqFundDao.multiUpdate(toUpdaterFunds);

		List<Long> collect = toUpdaterFunds.stream().map(TCsqFund::getId).collect(Collectors.toList());
		log.info("json =======>{}", JSON.toJSONString(collect));

		/*TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				collect.stream().forEach(
					a -> {
						csqServiceService.synchronizeService(a);
					}
				);
			}
		});*/
	}

	@Override
	public void dealWithServicePic() {
		int[] ints = {20, 20, 20, 13, 39, 28, 4, 27, 32, 17, 46, 3, 19, 18, 39, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 23, 2, 21, 22, 1, 24, 25, 29, 26, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48};
		for (int theInt : ints) {
			csqServiceService.synchronizeService(Long.valueOf(theInt));
		}
	}

	@Override
	public void dealWithServicePayment() {
		//TODO 注意！ 在此之前需要清除对应的csqUserPayments
		List<TCsqService> tCsqServices = csqServiceService.selectInIds(396l, 397l, 398l, 399l, 400l, 401l);
		List<String> serviceNames = tCsqServices.stream()
			.map(TCsqService::getName).collect(Collectors.toList());

		List<TOldService> oldServices = csqOldServiceDao.selectInNames(serviceNames);
		List<String> oldServiceIds = oldServices.stream()
			.map(TOldService::getId).collect(Collectors.toList());
		List<TOldPayment> tOldPayments = csqOldPaymentDao.selectInPfIds(oldServiceIds);

		//transfer oldPayments -> csqUserPayments
		/*tOldPayments.stream()
			.map(a -> {
				TCsqUserPaymentRecord.builder()

			})*/
	}

	@Override
	public void fixUpServiceRelated() {
		List<TOldService> tOldServices = csqOldServiceDao.selectByStatusByCheckStatusByDType(1, 1, "pf");
		//Map
		Map<String, List<TOldService>> pfMap = tOldServices.stream()
			.collect(Collectors.groupingBy(TOldService::getId));
		//区分
		List<TOldService> pfTypeServices = tOldServices;
		List<String> oldServiceIds = pfTypeServices.stream()
			.map(TOldService::getId).collect(Collectors.toList());

		//流水
		List<TOldPayment> paymentsOfService = oldServiceIds.isEmpty() ? new ArrayList<>() : csqOldPaymentDao.selectInPfIdsAsc(oldServiceIds);

		//拷贝
		Map<String, Integer> pfCount = new HashMap<>();
		Map<String, Long> fundIdMap = new HashMap<>();
		List<Long> collect = pfTypeServices.stream()
			.filter(a -> !"随手捐".equals(a.getTitle()))
			.map(TOldService::getId).map(Long::valueOf).distinct().collect(Collectors.toList());
		List<TCsqService> tCsqServices = csqServiceService.selectInExtends(collect);
		tCsqServices.stream()
			.forEach(a -> {
				fundIdMap.put(a.getExtend(), a.getId());
			});
		//TODO 2.拷贝项目 -> 项目(创建项目流程、捐赠流程
		paymentsOfService.stream()
			.forEach(b -> {
				String pfid = b.getPfid();
				List<TOldService> oldServices = pfMap.get(pfid);
				if (!oldServices.isEmpty()) {
					TOldService tOldService = oldServices.get(0);
					if (!"随手捐".equals(tOldService.getTitle())) {
						String detail = tOldService.getDetail();
						Integer cnt = pfCount.get(pfid);
						if (cnt == null) {
							cnt = 0;
							//开启项目
							if (cnt == 0) {
								if (Double.valueOf(tOldService.getDonationamount()) >= 1) {

								}
							}

						} else {
							Long serviceId = fundIdMap.get(pfid);
							//捐赠
							csqPayService.fakeWechatPay(getUserId(b.getOptionuser()), serviceId, CsqEntityTypeEnum.TYPE_SERVICE.toCode(), Double.valueOf(b.getDonationmoney()), null, Long.valueOf(DateUtil.wholeDateToStamp(b.getAddtime())));
						}
						cnt++;
						pfCount.put(pfid, cnt);
					}
				}
			});
	}

	@Override
	public void offLineDeal() {
		//找到记录。
		List<TCsqOffLineData> tCsqOffLineData = csqOfflineDataDao.selectAll();
		if (tCsqOffLineData.isEmpty()) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "没有数据!");

		dealWithOffLineData(tCsqOffLineData);
	}

	@Override
	public void dealWithPersonInChargeInfos(String givenFolderPath) {
		String folderPath = "";
		if (!StringUtil.isEmpty(givenFolderPath)) {
			folderPath = givenFolderPath;
		}
		try {
			File file = new File(folderPath);
			if (!file.isDirectory()) {
				System.out.println("not a directory");
				System.out.println(file.getName());
			}
			File[] files = file.listFiles();
			List<String> names = Arrays.stream(files)
				.map(File::getName).collect(Collectors.toList());
			//插入到记录
			List<TCsqPersonInChargeInfo> toInserter = names.stream()
				.map(a ->
				{
					if (!a.contains("tore")) {
						//分理出id fundName name
						String[] s = a.split(" ");
						String fundName = s[0];
						String name = s[1];

						TCsqFund csqFund = csqFundDao.selectByName(fundName);
						if (csqFund == null) {    //记录异常
							log.info("请检查fundName={}, description={}", fundName, a);
						}
						TCsqPersonInChargeInfo build = TCsqPersonInChargeInfo.builder()
							.fundId(csqFund == null ? null : csqFund.getId())
							.fundName(fundName)
							.description(a).build();
						return build;
					}
					return TCsqPersonInChargeInfo.builder()
						.fundId(null)
						.fundName(null)
						.build()
						;
				}).collect(Collectors.toList());
			MybatisPlus.getInstance().save(toInserter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<TCsqPersonInChargeInfo> findAllPersonInChargeInfos() {
		List<TCsqPersonInChargeInfo> tCsqPersonInChargeInfos = MybatisPlus.getInstance().findAll(new TCsqPersonInChargeInfo(), new MybatisPlusBuild(TCsqPersonInChargeInfo.class)
			.isNotNull(TCsqPersonInChargeInfo::getFundId)
		);
		return tCsqPersonInChargeInfos.stream()
			.map(a -> {
				String personInChargePic = a.getPersonInChargePic();
				if (personInChargePic == null || "".equals(personInChargePic)) {
					personInChargePic = "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/fund_person_in_charge/fund" + a.getFundId();
					a.setPersonInChargePic(personInChargePic);
					MybatisPlus.getInstance().update(a, new MybatisPlusBuild(TCsqPersonInChargeInfo.class)
						.eq(TCsqPersonInChargeInfo::getId, a.getId())
					);
				}
				return a;
			}).collect(Collectors.toList());
	}

	@Override
	public void insert(String json) {
		List<TCsqPersonInChargeInfo> tCsqPersonInChargeInfos = JSONObject.parseArray(json, TCsqPersonInChargeInfo.class);
		MybatisPlus.getInstance().save(tCsqPersonInChargeInfos);
	}

	@Override
	public void findOutTheIdOfFund() {
		findOutTheIdOfFund(true);
	}

	@Override
	public void findOutTheIdOfFund(boolean isTransferData) {
		if (isTransferData) {
			List<TCsqTransferData> csqTransferDataList = MybatisPlus.getInstance().findAll(new TCsqTransferData(), new MybatisPlusBuild(TCsqTransferData.class));
			List<String> names = csqTransferDataList.stream()
				.map(TCsqTransferData::getFundName)
				.distinct().collect(Collectors.toList());
			List<TCsqFund> tCsqFundList = csqFundDao.selectInNames(names);
			Map<String, List<TCsqFund>> nameMap = tCsqFundList.stream()
				.collect(Collectors.groupingBy(TCsqFund::getName));
			List<TCsqTransferData> collect = csqTransferDataList.stream()
				.map(a -> {
					String fundName = a.getFundName();
					List<TCsqFund> tCsqFundList1 = nameMap.get(fundName);
					if (tCsqFundList1 != null) {
						TCsqFund csqFund = tCsqFundList1.get(0);
						a.setFundId(csqFund.getId());
					}
					return a;
				}).collect(Collectors.toList());
			List<Long> ids = collect.stream().map(TCsqTransferData::getId).collect(Collectors.toList());
			MybatisPlus.getInstance().update(collect, new MybatisPlusBuild(TCsqTransferData.class)
				.in(TCsqTransferData::getId, ids));
			return;
		}

		List<TCsqToDeleteRecordClue> csqToDeleteRecordClues = MybatisPlus.getInstance().findAll(new TCsqToDeleteRecordClue(), new MybatisPlusBuild(TCsqToDeleteRecordClue.class));
		List<String> names = csqToDeleteRecordClues.stream()
			.map(TCsqToDeleteRecordClue::getFundName)
			.distinct().collect(Collectors.toList());
		List<TCsqFund> tCsqFundList = csqFundDao.selectInNames(names);
		Map<String, List<TCsqFund>> nameMap = tCsqFundList.stream()
			.collect(Collectors.groupingBy(TCsqFund::getName));
		List<TCsqToDeleteRecordClue> collect = csqToDeleteRecordClues.stream()
			.map(a -> {
				String fundName = a.getFundName();
				List<TCsqFund> tCsqFundList1 = nameMap.get(fundName);
				if (tCsqFundList1 != null) {
					TCsqFund csqFund = tCsqFundList1.get(0);
					a.setFundId(csqFund.getId());
				}
				return a;
			}).collect(Collectors.toList());
		List<Long> ids = collect.stream().map(TCsqToDeleteRecordClue::getId).collect(Collectors.toList());
		MybatisPlus.getInstance().update(collect, new MybatisPlusBuild(TCsqToDeleteRecordClue.class)
			.in(TCsqToDeleteRecordClue::getId, ids));
	}

	@Override
	public void dealWithTransferData() {
		dealWithTransferData(null);
	}

	private void dealWithTransferData(List<TCsqTransferData> csqTransferDataList) {
		csqTransferDataList = csqTransferDataList == null || csqTransferDataList.isEmpty() ? MybatisPlus.getInstance().findAll(new TCsqTransferData(), new MybatisPlusBuild(TCsqTransferData.class).eq(TCsqTransferData::getIsValid, AppConstant.IS_VALID_YES)) : csqTransferDataList;
		List<TCsqOffLineData> offLineData = csqTransferDataList.stream()
			.map(TCsqTransferData::copyTCsqOffLineData).collect(Collectors.toList());

		dealWithOffLineData(offLineData);
	}

	@Override
	public void currentDealWithServicePayment() {
		//排除随手捐和快乐冬衣
		List<TOldPayment> tOldPayments = csqOldPaymentDao.selectNotInPFId(156710913200000001L, 153820441700000001L);
		//拆解成fakePay
		tOldPayments.stream()
			.forEach(a -> {
				Double money = Double.valueOf(a.getDonationmoney());
				String oldId = a.getOptionuser();
				TCsqUser csqUser = csqUserDao.selectByOldId(oldId);
				Long userId = csqUser.getId();

				String addtime = a.getAddtime();
				addtime = Arrays.asList(addtime.split(" ")).get(0);
				Long timeStamp = Long.valueOf(DateUtil.dateToStamp(addtime));
				String pfid = a.getPfid();
				TOldService tOldService = csqOldServiceDao.selectByPrimaryKey(pfid);
				String name = tOldService.getTitle();
				TCsqService csqService = csqServiceDao.selectByName(name);
				Integer type = csqService.getType();
				Long entityId = csqService.getId();
				Integer entityType = CsqEntityTypeEnum.TYPE_SERVICE.toCode();
				if (CsqServiceEnum.TYPE_FUND.getCode() == type) {    //为基金
					entityId = csqService.getFundId();
					entityType = CsqEntityTypeEnum.TYPE_FUND.toCode();
				}
				csqPayService.fakeWechatPay(userId, entityId, entityType, money, null, timeStamp);
			});

	}

	@Override
	public void deleteRecords(List<Long> recordIds) {
		//删除指定项目下，指定流水id集合的捐赠记录（流水、订单置为0，累计捐款人数减少，余额和累计捐入变动
		//通过订单找到用户id，衰减用户的累计支出，以及捐赠次数(由于是微信支付，所以不涉及余额变动)
		//通过订单找到to_id，对目标基金的累计捐款人数减少，衰减余额以及累计捐入
		//通过流水找到订单号，锁定本订单所有record记录，全部置为0，并将订单置为0

		//通过指定的recordid 流水编号找到所有相关订单id
		List<TCsqUserPaymentRecord> records = csqUserPaymentDao.selectInPrimaryKeys(recordIds);
		List<Long> orderIds = records.stream()
			.map(TCsqUserPaymentRecord::getOrderId).collect(Collectors.toList());

		//orderIds -> records
		List<TCsqUserPaymentRecord> recordsByOrderIds = csqUserPaymentDao.selectInOrderIds(orderIds);
		//筛选出捐赠的用户
		List<TCsqOrder> csqOrders = csqOrderDao.selectInOrderIds(orderIds);
		List<Long> userIds = csqOrders.stream()
			.map(TCsqOrder::getUserId).distinct().collect(Collectors.toList());
		List<TCsqUser> csqUsers = csqUserDao.selectInIds(userIds);
		Map<Long, List<TCsqUser>> idUserMap = csqUsers.stream().collect(Collectors.groupingBy(TCsqUser::getId));

		Map<Long, TCsqUser> userMap = new HashMap<>();
		//USER -> 修改数据
		csqOrders.stream()
			.forEach(a -> {
				Double price = a.getPrice();
				Long userId = a.getUserId();

				TCsqUser csqUser = userMap.get(userId);
				if (csqUser == null) {
					List<TCsqUser> csqUserContainer = idUserMap.get(userId);
					if (!csqUserContainer.isEmpty()) {
						csqUser = csqUserContainer.get(0);
					}
				}

				Double sumTotalPay = csqUser.getSumTotalPay();
				Double currentResult;
				sumTotalPay = (currentResult = sumTotalPay - price) < 0d ? 0d : currentResult;
				Integer payNum = csqUser.getPayNum();
				Integer currentPayNum;
				payNum = (currentPayNum = payNum - 1) < 0 ? 0 : currentPayNum;
				TCsqUser build = TCsqUser.builder()
					.id(csqUser.getId())
					.sumTotalPay(sumTotalPay)
					.payNum(payNum)
					.build();
				userMap.put(userId, csqUser);
			});

		List<Long> userIdsToDo = csqOrders.stream()
			.map(TCsqOrder::getUserId).collect(Collectors.toList());
		List<TCsqUser> toUpdaterUsers = userIdsToDo.stream()
			.map(a -> {
				TCsqUser csqUser = userMap.get(a);
				return csqUser;
			}).collect(Collectors.toList());

		List<Long> fundIds = csqOrders.stream()
			.map(a -> {
				Long toId = a.getToId();
				//此处由于知道来源是基金，略过了type判断
				return toId;
			}).collect(Collectors.toList());
		List<TCsqFund> csqFunds = csqFundDao.selectInIds(fundIds);
		Map<Long, List<TCsqFund>> idFundMap = csqFunds.stream().collect(Collectors.groupingBy(TCsqFund::getId));
		Map<Long, TCsqFund> fundMap = new HashMap<>();
		csqOrders.stream()
			.forEach(a -> {
				Double price = a.getPrice();
				Long fundId = a.getToId();
				TCsqFund csqFund = fundMap.get(fundId);
				if (csqFund == null) {
					List<TCsqFund> csqFundContainer = idFundMap.get(fundId);
					if (csqFundContainer != null) {
						csqFund = csqFundContainer.get(0);
					}
				}
				Double sumTotalIn = csqFund.getSumTotalIn();    //累计捐入
				Double balance = csqFund.getBalance();    //金额
				Integer totalInCnt = csqFund.getTotalInCnt();    //捐款人次

				sumTotalIn = sumTotalIn - price;
				sumTotalIn = sumTotalIn < 0d ? 0d : sumTotalIn;

				balance = balance - price;
				balance = balance < 0d ? 0d : balance;

				totalInCnt--;
				totalInCnt = totalInCnt < 0 ? 0 : totalInCnt;

				TCsqFund build = TCsqFund.builder()
					.id(csqFund.getId())
					.sumTotalIn(sumTotalIn)
					.balance(balance)
					.totalInCnt(totalInCnt).build();

				fundMap.put(fundId, build);
			});

		List<Long> distinctFundIds = csqOrders.stream()
			.filter(a -> a.getToType() == CsqEntityTypeEnum.TYPE_FUND.toCode())
			.map(TCsqOrder::getToId).distinct().collect(Collectors.toList());
		List<TCsqFund> toUpdateFunds = distinctFundIds.stream()
			.map(a -> {
				TCsqFund csqFund = fundMap.get(a);
				return csqFund;
			}).collect(Collectors.toList());

		List<TCsqFund> brandNewFundList = csqFundDao.selectInIds(fundIds);

		// REOCRD -> 置 '0'
		List<TCsqUserPaymentRecord> toDeleteByUpdateRecords = recordsByOrderIds.stream()
			.map(a -> {
				TCsqUserPaymentRecord build = TCsqUserPaymentRecord.builder()
					.id(a.getId()).build();
				build.setIsValid(AppConstant.IS_VALID_NO);
				return build;
			}).collect(Collectors.toList());

		// ORDER -> 置 '0'
		List<TCsqOrder> toDeleteByUpdateOrders = recordsByOrderIds.stream()
			.map(a -> {
				TCsqOrder build = TCsqOrder.builder()
					.id(a.getId()).build();
				build.setIsValid(AppConstant.IS_VALID_NO);
				return build;
			}).collect(Collectors.toList());

		csqUserDao.update(toUpdaterUsers);
		csqFundDao.batchUpdate(toUpdateFunds);
		csqUserPaymentDao.update(toDeleteByUpdateRecords);
		csqOrderDao.update(toDeleteByUpdateOrders);

		/*TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				csqServiceService.synchronizeService(brandNewFundList);
			}
		});*/

		List<Long> funIdNotified = brandNewFundList.stream().map(TCsqFund::getId).collect(Collectors.toList());

		String s = funIdNotified.stream()
			.map(a -> {
				return a.toString();
			})
			.reduce((a, b) -> (a + "," + b)).get();
		log.info("fundIds={}", s);

	}

	@Override
	public void synchronizeService(String fundIds) {
		List<Long> fundIdsLong = Arrays.asList(fundIds.split(",")).stream()
			.map(Long::valueOf).collect(Collectors.toList());
		fundIdsLong.stream()
			.distinct()
			.forEach(a -> {
				csqServiceService.synchronizeService(a);
			});
	}

	@Override
	public List<TCsqUserPaymentRecord> findRecords() {
		List<TCsqToDeleteRecordClue> allRecords = MybatisPlus.getInstance().findAll(new TCsqToDeleteRecordClue(), new MybatisPlusBuild(TCsqToDeleteRecordClue.class)
			.eq(TCsqToDeleteRecordClue::getIsValid, AppConstant.IS_VALID_YES)
		);

		List<String> userNames = allRecords.stream()
			.map(TCsqToDeleteRecordClue::getDonaterName).collect(Collectors.toList());
		List<TCsqUser> users = csqUserDao.selectInNames(userNames);
		Map<String, List<TCsqUser>> nameUserMap = users.stream()
			.collect(Collectors.groupingBy(TCsqUser::getName));
		List<Long> orderIds = new ArrayList<>();
		allRecords.stream()
			.forEach(a -> {
				Double money = a.getMoney();
				String name = a.getDonaterName();
				TCsqUser csqUser = null;
				List<TCsqUser> csqUsers = nameUserMap.get(name);
				if (csqUsers == null) {
					System.out.println("got' em");
				}
				if (csqUsers != null) {
					csqUser = csqUsers.get(0);
				}
				String date = a.getDate();
				Long timeStamp = Long.valueOf(DateUtil.dateToStamp(date));
				List<TCsqOrder> tCsqOrders = csqOrderDao.selectByUserIdAndToTypeAndToIdAndPriceAndCreateTimeDesc(csqUser.getId(), CsqEntityTypeEnum.TYPE_FUND.toCode(), a.getFundId(), money, timeStamp);
				if (!tCsqOrders.isEmpty()) {
					List<Long> orIds = tCsqOrders.stream()
						.map(TCsqOrder::getId).collect(Collectors.toList());
					orderIds.addAll(orIds);
				}
			});
		return csqUserPaymentDao.selectInOrderIds(orderIds);
	}

	@Override
	public void findAndDeleteRecords() {
		List<TCsqUserPaymentRecord> records = findRecords();
		List<Long> recordIds = records.stream()
			.map(TCsqUserPaymentRecord::getId).collect(Collectors.toList());
		deleteRecords(recordIds);
	}

	@Override
	public void transeferGrowthFundRecord() {
		dealWithGrowthValueTransfer(false);
	}

	@Override
	public void transeferGrowthFundRecordAfter() {
		dealWithGrowthValueTransfer(true);
	}

	@Override
	public void offlineDataRecordIn(List<TCsqTransferData> datas) {
		//确认基金名填写正确
		if (datas.stream().anyMatch(a -> a.getFundId() == null))
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "存在错误基金名字的行项，请确认！");
		//将数据排序，收入支出正序排列
		datas = datas.stream()
			.sorted(Comparator.comparing(TCsqTransferData::getInOrOut)).collect(Collectors.toList());
		//校验支出是否大于收入
		Map<Long, List<TCsqTransferData>> fundIdTransferDataMap = datas.stream()
			.collect(Collectors.groupingBy(TCsqTransferData::getFundId));
		List<Long> fundIds = new ArrayList<>();
		fundIdTransferDataMap.forEach((k, v) -> {
			fundIds.add(k);
		});
		List<TCsqFund> csqFunds = csqFundDao.selectInIds(fundIds);
		Map<Long, List<TCsqFund>> idFundMap = csqFunds.stream()
			.collect(Collectors.groupingBy(TCsqFund::getId));

		List<String> messages = new ArrayList<>();
		fundIdTransferDataMap.forEach((fundId, dataList) -> {
			Map<Integer, List<TCsqTransferData>> inOutMap = dataList.stream()
				.collect(Collectors.groupingBy(TCsqTransferData::getInOrOut));
			List<TCsqTransferData> inData = inOutMap.get(CsqUserPaymentEnum.INOUT_IN.toCode());
			List<TCsqTransferData> outData = inOutMap.get(CsqUserPaymentEnum.INOUT_OUT.toCode());

			List<TCsqFund> tCsqFunds = idFundMap.get(fundId);
			if (tCsqFunds != null) {
				TCsqFund csqFund = tCsqFunds.get(0);
				Double balance = csqFund.getBalance();
				Double inDouble = 0d;
				Double outDouble = 9d;
				inDouble = inData == null ? 0d : inData.stream()
					.map(TCsqTransferData::getMoney).reduce(0d, Double::sum);
				outDouble = outData == null ? 0d : outData.stream()
					.map(TCsqTransferData::getMoney).reduce(0d, Double::sum);
				if ((balance + inDouble - outDouble) < 0) {
					StringBuilder builder = new StringBuilder();

					builder.append("基金 ").append(csqFund.getName()).append("余额不足！");
					messages.add(builder.toString());
				}
			}
		});

		if (!messages.isEmpty()) {
			String message = "所有信息并未提交！请确认以上信息，重新整理数据并提交。";
			messages.add(message);
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, messages.toString());
		}

		datas = datas.stream()
			.map(a -> {
				a.setIsValid(AppConstant.IS_VALID_YES);
				return a;
			}).collect(Collectors.toList());

		//进行数据转移
		dealWithTransferData(datas);
	}

	@Override
	public String dataOut_1() {
		//查询订单表
		List<TCsqOrder> orignOrders = csqOrderDao.selectByFromTypeAndToIdAndToTypeAndStatusDesc(1, 76L, 4, 2);    //TODO 写死的数据
		List<Long> userIds = orignOrders.stream()
			.map(TCsqOrder::getFromId)
			.distinct()
			.collect(Collectors.toList());
		Map<Long, List<TCsqOrder>> userIdOrderMap = orignOrders.stream()
			.collect(Collectors.groupingBy(TCsqOrder::getFromId));
		List<TCsqUser> tCsqUsers = csqUserDao.selectInIds(userIds);
		Map<Long, List<TCsqUser>> idUserMap = tCsqUsers.stream()
			.collect(Collectors.groupingBy(TCsqUser::getId));

		List<CsqServiceUserOutVo> outPutVo = userIds.stream()
			.map(userId -> {
				List<TCsqOrder> orders = userIdOrderMap.get(userId);
				//捐款笔数
				int donateNum = orders.size();
				//累计捐款金额
				Double donateAmount = orders.stream()
					.map(TCsqOrder::getPrice).reduce(0d, Double::sum);
				//最近订单创建时间
				orders = orders.stream()
					.sorted(Comparator.comparing(TCsqOrder::getCreateTime).reversed()).collect(Collectors.toList());
				TCsqOrder lastOrder = orders.get(0);
				Timestamp lastOrderTime = lastOrder.getCreateTime();
				//最近订单金额
				Double lastOrderPrice = lastOrder.getPrice();
				//用户头像、昵称、手机号、创建时间
				List<TCsqUser> theList = idUserMap.get(userId);
				TCsqUser theUser = theList.get(0);
				String headPic = theUser.getUserHeadPortraitPath();
				String name = theUser.getName();
				String userTel = theUser.getUserTel();
				Timestamp createTime = theUser.getCreateTime();

				return CsqServiceUserOutVo.builder()
					.createTime(createTime)
					.telephone(userTel)
					.name(name)
					.headPic(headPic)
					.donateNum(donateNum)
					.donateAmount(donateAmount)
					.lastDonatetime(lastOrderTime)
					.lastDonateAmount(lastOrderPrice).build();
			}).collect(Collectors.toList());

		//将列表打印成excel的第二行
		//第一行为头像、昵称、手机号、注册时间、捐赠笔数、累计捐赠金额、最近一次捐款时间、最近一次捐款金额
		OutputStream outputStream = null;
		String path = "/Users/xufangyi/Downloads/newFile";
		try {
			outputStream = new FileOutputStream(new File(path));
			List<Object[]> objects = outPutVo.stream()
				.map(a -> new Object[]{
					a.getHeadPic(),
					a.getName(),
					a.getTelephone(),
					a.getCreateTime(),
					a.getDonateNum(),
					a.getDonateAmount(),
					a.getLastDonatetime(),
					a.getLastDonateAmount()
				}).collect(Collectors.toList());
			POIUtil.export(null, Arrays.asList("头像", "昵称", "手机号", "注册时间", "捐赠笔数", "累计捐赠金额", "最近一次捐款时间", "最近一次捐款金额"), objects, outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	private void dealWithGrowthValueTransfer(boolean isAfterDeal) {
		//从老数据得到成长基金数据
		// -> 每个用户以自己的第一笔捐助建立专项基金(日期同步）
		//	  并且后续的捐助，成为捐助流水进入到成长基金当中去
		//获取成长基金捐赠记录(Json)
		List<TOldService> oldServices = csqOldServiceDao.selectByNames("成长基金");
		if (oldServices.isEmpty()) {
			return;
		}
		TOldService oldService = oldServices.get(0);
		String pfId = oldService.getId();
		List<TOldPayment> tOldPayments = csqOldPaymentDao.selectInPfIds(Arrays.asList(pfId));
		Map<String, List<TOldPayment>> optionUserPaymentMap = tOldPayments.stream()
			.collect(Collectors.groupingBy(TOldPayment::getOptionuser));

		//进行前的基金持有检查。对每个用户持有的基金进行检查，如果已经存在一个基金，则提示。
		List<Long> oldUserIds = new ArrayList<>();
		if (!isAfterDeal) {
			oldUserIds = checkBeforeTranseferGrowthFundRecord(optionUserPaymentMap);
			/*if(!oldUserIds.isEmpty()) {
				return;
			}*/
		}
		final List<Long> theIds = oldUserIds;
		optionUserPaymentMap.forEach((k, v) -> {
//			Long oldUserId = Long.valueOf(k);
			TCsqUser csqUser = csqUserDao.selectByOldId(k);
			Long userId = csqUser.getId();
			v = v.stream()
				.sorted(Comparator.comparing(TOldPayment::getDonationtime).reversed()).collect(Collectors.toList());//捐助时间倒序
			//找到名下的基金
			List<TCsqFund> csqFunds = csqFundDao.selectByUserIdAndNotEqStatus(userId, CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal());
			for (int i = 0; i < v.size(); i++) {
				TOldPayment tOldPayment = v.get(i);
				Double amount = Double.valueOf(tOldPayment.getDonationmoney());
				String date = tOldPayment.getDonationtime();
				date = date.substring(0, date.length() - 4);
				Long timeStamp = Long.valueOf(DateUtil.dateToStamp(date));
				//第一条记录将被转换成专项基金
				if (!isAfterDeal) {
					if (theIds.contains(Long.valueOf(k))) {    //如果包含该基金说明已重复，按充值处理
						if (!csqFunds.isEmpty()) {
							TCsqFund csqFund = csqFunds.get(0);
							csqPayService.fakeWechatPay(userId, csqFund.getId(), CsqEntityTypeEnum.TYPE_FUND.toCode(), amount, null, timeStamp);
						}
					} else {
						csqPayService.fakeWechatPay(userId, null, CsqEntityTypeEnum.TYPE_FUND.toCode(), amount, TCsqFund.builder().build(), timeStamp);
					}
					break;
				} else {
					if (i != 0) {
						if (!csqFunds.isEmpty()) {
							TCsqFund csqFund = csqFunds.get(0);
							csqPayService.fakeWechatPay(userId, csqFund.getId(), CsqEntityTypeEnum.TYPE_FUND.toCode(), amount, null, timeStamp);
						}
					}
				}
			}
		});
	}

	private List<Long> checkBeforeTranseferGrowthFundRecord(Map<String, List<TOldPayment>> optionUserPaymentMap) {
		List<Long> oldUserIds = new ArrayList<>();
		List<Long> userIds = new ArrayList<>();
		List<Long> oldUserIdsTotalInCnt = new ArrayList<>();
		optionUserPaymentMap.forEach((k, v) -> {
			Long oldUserId = Long.valueOf(k);
			TCsqUser csqUser = csqUserDao.selectByOldId(k);
			Long userId = csqUser.getId();
			List<TCsqFund> csqFunds = csqFundDao.selectByUserIdAndNotEqStatus(userId, CsqFundEnum.STATUS_WAIT_ACTIVATE.getVal());
			if (!csqFunds.isEmpty()) {
				oldUserIds.add(oldUserId);
				userIds.add(userId);
				TCsqFund csqFund = csqFunds.get(0);
				Integer totalInCnt = csqFund.getTotalInCnt();
				if (totalInCnt > 0) {
					oldUserIdsTotalInCnt.add(oldUserId);
				}
			}
		});
		if (!oldUserIds.isEmpty()) {
			log.info("已经拥有一个基金的旧账户编号={}", oldUserIds);
			log.info("已经拥有一个基金的账户编号={}", userIds);
			log.info("已经拥有一个基金, 且基金收入大于0的旧账户编号={}", oldUserIds);
		}
		return oldUserIds;
	}

	private void dealWithOffLineData(List<TCsqOffLineData> offLineData) {
		offLineData = offLineData.stream()
			.filter(a -> AppConstant.IS_VALID_YES.equals(a.getIsValid())).collect(Collectors.toList());
		List<TCsqOffLineData> inList = offLineData.stream()
			.filter(a -> 0 == a.getType()).collect(Collectors.toList());
		List<TCsqOffLineData> outList = offLineData.stream()
			.filter(a -> 1 == a.getType()).collect(Collectors.toList());
		dealWithOffLineIn(inList);
		dealWithOffLineOut(outList);
	}


	/*public static void main(String[] args) {
		for (; ; ) {
			System.out.println("write down please:");
			String folderPath = new Scanner(System.in).nextLine();
			try {
				File file = new File(folderPath);
				if (!file.isDirectory()) {
					System.out.println("not a directory");
					System.out.println(file.getName());
					break;
				}
				File[] files = file.listFiles();
				List<String> names = Arrays.stream(files)
					.map(File::getName).collect(Collectors.toList());
				System.out.println(names);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/

	private void dealWithOffLineOut(List<TCsqOffLineData> outList) {
		//调用recordForConsumption
		//TODO
		outList.stream()
			.forEach(a -> {
				boolean isService = a.getServiceId() != null;
				if(!StringUtil.isEmpty(a.getFundName())) {
					//试图找到对应编号
					TCsqFund fund = csqFundDao.selectByName(a.getFundName());
					a.setFundId(fund == null? a.getFundId(): fund.getId());
				}

				Long fundId = a.getFundId();
				Long serviceId = a.getServiceId();
				if (fundId != null || serviceId != null) {
					String description = a.getDescription();
					//处理description
					if (description.contains(" ")) {
						String[] descStrArray = description.split(" ");
						description = descStrArray[1];    //得到的文本可能需要再处理
					}
					String date = a.getDate();
					Long timeStamp = StringUtil.isEmpty(date) ? System.currentTimeMillis() : Long.valueOf(DateUtil.dateToStamp(date));
					csqUserService.recordForConsumption(null, isService ? serviceId : fundId, isService ? CsqEntityTypeEnum.TYPE_SERVICE.toCode() : CsqEntityTypeEnum.TYPE_FUND.toCode(), a.getMoney(), description, timeStamp);
				}
			});
	}

	private void dealWithOffLineIn(List<TCsqOffLineData> offLineData) {
		List<String> userWithNamesToCreate = offLineData.stream()
//			.filter(a -> a.getFundId() != null || a.getServiceId() != null)
			.map(TCsqOffLineData::getUserName)
			.distinct()
			.collect(Collectors.toList());
		//创建虚拟账户
		//TODO
		List<TCsqUser> users = userWithNamesToCreate.stream()
			.map(a ->
				TCsqUser.builder()
					.name(a).build()
			).collect(Collectors.toList());
		Map<String, List<TCsqUser>> csqUserNameUserMap = users.stream()
			.map(a -> {
				a.setUuid("123");
				//校验同名用户是否存在
				String name = a.getName();
				List<TCsqUser> tCsqUsers = csqUserDao.selectByName(name);
				TCsqUser returnUser = null;
				if (!tCsqUsers.isEmpty()) {
					returnUser = tCsqUsers.get(0);
					if (tCsqUsers.size() > 1) {
						for (TCsqUser csqUser : tCsqUsers) {
							if (!StringUtil.isEmpty(csqUser.getUserTel())) {    //不为空
								returnUser = csqUser;
							}
						}
					}
				}
				return returnUser == null ? csqUserService.testRegister(a) : returnUser;
			}).collect(Collectors.groupingBy(TCsqUser::getName));
		//调用fakeWechatPay(after commit)
		offLineData.stream()
			.forEach(a -> {
				log.info("已排除基金编号为空并且项目编号为空的数据...");
//				if (a.getFundId() != null || a.getServiceId() != null) {
					log.info("当前转移的数据 a={}", a);
					String userName = a.getUserName();
					String fundName = a.getFundName();
					boolean emptyFundName = StringUtil.isEmpty(fundName);
					TCsqFund csqFund = emptyFundName? null: TCsqFund.builder().name(fundName).build();
					if(!emptyFundName) {
						//试图找到对应编号
						TCsqFund fund = csqFundDao.selectByName(fundName);
						a.setFundId(fund == null? a.getFundId(): fund.getId());
					}
					List<TCsqUser> tCsqUsers = csqUserNameUserMap.get(userName);
					if (tCsqUsers != null && !tCsqUsers.isEmpty()) {
						boolean isService = a.getServiceId() != null;

						TCsqUser csqUser = tCsqUsers.get(0);
						Long userId = csqUser.getId();
						String date = a.getDate();//format
						StringBuffer stringBuffer = new StringBuffer(date);
						if (date.contains("-")) {
							date = date.replaceAll("-", ".");
						}
						int i = date.lastIndexOf(".");
						stringBuffer.replace(i, i + 1, "-");
						stringBuffer.replace(4, 5, "-");
						Long timeStamp = Long.valueOf(DateUtil.dateToStamp(stringBuffer.toString()));

						csqPayService.fakeWechatPay(userId, isService ? a.getServiceId() : a.getFundId(), isService ? CsqEntityTypeEnum.TYPE_SERVICE.toCode() : CsqEntityTypeEnum.TYPE_FUND.toCode(), a.getMoney(), csqFund, timeStamp);
					}
//				}
			});
	}
/*
	public static void main(String[] args) {
		String date = "2018-07-25";
		if(date.contains("-")) {
			date = date.replaceAll("-", ".");
			System.out.println(date);
		}
	    System.out.println(date);

		StringBuffer stringBuffer = new StringBuffer(date);
		int i = date.lastIndexOf(".");
		stringBuffer.replace(i, i + 1, "-");
		stringBuffer.replace(4, 5, "-");
		String s = stringBuffer.toString();
		Long timeStamp = Long.valueOf(DateUtil.dateToStamp(s));
		System.out.println(s);
		System.out.println(timeStamp);
	}*/

	private void dealWithPaymentThatIsFundAndService() {
		/*List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = csqUserPaymentDao.selectInEntityType();
		List<Long> fundIds = tCsqUserPaymentRecords.stream()
			.filter(a -> a.getEntityType() == CsqEntityTypeEnum.TYPE_FUND.toCode())
			.map(TCsqUserPaymentRecord::getEntityId).collect(Collectors.toList());

		List<Long> serviceIds = tCsqUserPaymentRecords.stream()
			.filter(a -> a.getEntityType() == CsqEntityTypeEnum.TYPE_SERVICE.toCode())
			.map(TCsqUserPaymentRecord::getEntityId).collect(Collectors.toList());

		List<TCsqFund> tCsqFunds = csqFundDao.selectInIds(fundIds);
		List<TCsqService> tCsqServices = csqServiceDao.selectInIds(serviceIds);

		//构建map
		Map<Long, List<TCsqFund>> fundMap = tCsqFunds.stream()
			.collect(Collectors.groupingBy(TCsqFund::getId));

		Map<Long, List<TCsqService>> serviceMap = tCsqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));

		//装载待更新项
		tCsqUserPaymentRecords.stream()
			.map(a -> {
				//类型推断
				Integer entityType = a.getEntityType();
				Long benificialId =
				if(CsqEntityTypeEnum.TYPE_FUND.toCode() == entityType) {	//基金

				} else if(CsqEntityTypeEnum.TYPE_SERVICE.toCode() == entityType) {	//项目

				}
			})
*/
	}


	private Long getUserId(String oldUserId) {
		TCsqUser csqUser = csqUserDao.selectByOldId(oldUserId);
		return csqUser == null ? null : csqUser.getId();
	}

	private Long getFundId(String extend) {
		TCsqFund tCsqFund = csqFundDao.selectByExtend(extend);
		return tCsqFund == null ? null : tCsqFund.getId();
	}

	private Long getServiceId(String extend) {
		TCsqService csqService = csqServiceService.selectByExtend(extend);
		return csqService == null ? null : csqService.getId();
	}

	public static String picDeal(String src, Integer num) {
		String[] s = src.split("_");
		String suffix = s[1];
		String prefix = "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/";
		String destination = "";
		for (int i = 1; i <= num; i++) {
			String simpleOne = prefix + src + "/" + suffix + "_" + i + ".png" + ",";
			destination += simpleOne;
		}
		if (destination.endsWith(",")) {
			destination = destination.substring(0, destination.length() - 1);
		}
		return destination;
	}

	public static String picDealAddSuffix(String srcDir, String halfPrefix, Integer num) {
		String prefix = "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/";
		String destination = "";
		for (int i = 1; i <= num; i++) {
			String simpleOne = prefix + srcDir + "/" + halfPrefix + "_" + i + ".png" + ",";
			destination += simpleOne;
		}
		if (destination.endsWith(",")) {
			destination = destination.substring(0, destination.length() - 1);
		}
		return destination;
	}

	public static void main(String[] args) {
		String s = picDealAddSuffix("83_chxyjh", "chxyjh", 4);
		System.out.println(s);
		/*String date = "2017-09-18 12:00:00.000";
		date = date.substring(0, date.length()-4);
		String s = DateUtil.dateToStamp(date);
		System.out.println(s);*/

	}

}
