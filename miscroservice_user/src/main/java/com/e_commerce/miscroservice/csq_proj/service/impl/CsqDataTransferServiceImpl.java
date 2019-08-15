package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.alibaba.fastjson.JSON;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.enums.application.CsqEntityTypeEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqServiceEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqUserEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqUserPaymentEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.*;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Timestamp;
import java.util.*;
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

	@Override
	public void dealWithOpenid() {
		//处理openid
		MybatisPlus instance = MybatisPlus.getInstance();
		List<TOldOpenId> all = instance.findAll(new TOldOpenId(), new MybatisPlusBuild(TOldOpenId.class));
		ArrayList<TOldUser> toUpdaters = new ArrayList<>();
		all.stream()
			.forEach(a -> {
				String userid = a.getUserid();
				TOldUser tOldUser = new TOldUser();
				tOldUser.setId(userid);
				tOldUser.setOpenid(a.getOpenid());
				toUpdaters.add(tOldUser);
			});

		csqOldUserDao.update(toUpdaters);
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
							if (vxOpenId.equals(b.getOpenid())) {    //发现重复的openid
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
		//处理用户与流水(1/2 账户充值流水)
		List<TOldUser> tOldUsers = csqOldUserDao.selectAll();
		List<TCsqUserPaymentRecord> toInserterPayment = new ArrayList<>();
		List<TCsqUser> toInserters = tOldUsers.stream()
			.map(a -> {
				//有用信息

				Long oldId = Long.valueOf(a.getId());
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
				List<TOldService> tOldServices = csqOldServiceDao.selectByNames("随手捐");
				List<String> serviceIds = tOldServices.stream()
					.map(TOldService::getId).collect(Collectors.toList());
				List<TOldPayment> tOldPayments = serviceIds.isEmpty() ? new ArrayList<>() : csqOldPaymentDao.selectByOptionUserInPFId(a.getId(), serviceIds);


				tOldPayments.stream()
					.forEach(b -> {
						String donationmoney = b.getDonationmoney();
						TCsqUserPaymentRecord build1 = TCsqUserPaymentRecord.builder()
							.orderId(null)    //缺失
							.money(Double.valueOf(donationmoney == null ? "0" : donationmoney))    //金额
							.entityType(CsqEntityTypeEnum.TYPE_ACCOUNT.toCode())    //账户余额
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

				return build;
			}).collect(Collectors.toList());
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
					.entityType(CsqEntityTypeEnum.TYPE_ACCOUNT.toCode())    //账户余额
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
		List<TOldPayment> paymentsOfFund = oldFundIds.isEmpty() ? new ArrayList<>() : csqOldPaymentDao.selectInPfIdsDesc(oldFundIds);
		List<TOldPayment> paymentsOfService = oldServiceIds.isEmpty() ? new ArrayList<>() : csqOldPaymentDao.selectInPfIdsDesc(oldServiceIds);

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
								build
								//						.description(a.getDetail())
							);//创建
							//fundId map
							fundIdMap.put(pfid, fundId);
						} else {
							if (userId != null) {    //用户数据必须存在
								//							Long fundId = getFundId(pfid);    //找到new
								Long fundId = fundIdMap.get(pfid);
								csqPayService.fakeWechatPay(userId, fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), singleAmount, null);    //单笔充值
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
										.name(tOldService.getTitle())
										.description(detail.length() > 511 ? "" : detail)
										//								.typePubKeys()	//意向
										.build();
									build.setExtend(tOldService.getId());
									Long serviceId = csqServiceService.publish(userId, build);//需要一个真实的用户id
									fundIdMap.put(pfid, serviceId);
								}
							}
							Long serviceId = fundIdMap.get(pfid);
							//捐赠
							csqPayService.fakeWechatPay(getUserId(b.getOptionuser()), serviceId, CsqEntityTypeEnum.TYPE_SERVICE.toCode(), Double.valueOf(b.getDonationmoney()), null);
							cnt++;
							pfCount.put(pfid, cnt);
						}
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
								build
								//						.description(a.getDetail())
							);//创建
							//fundId map
							fundIdMap.put(pfid, fundId);
						} else {
							if (userId != null) {    //用户数据必须存在
								//							Long fundId = getFundId(pfid);    //找到new
								Long fundId = fundIdMap.get(pfid);
								csqPayService.fakeWechatPay(userId, fundId, CsqEntityTypeEnum.TYPE_FUND.toCode(), singleAmount, null);    //单笔充值
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
		List<TCsqService> csqServices = csqServiceService.findAllByTypeAndIdGreaterThan(CsqServiceEnum.TYPE_SERIVE.getCode(), 379L);
		List<TCsqFund> csqFunds = csqFundService.selectAllAndIdGreaterThan(23L);

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

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				collect.stream().forEach(
					a -> {
						csqServiceService.synchronizeService(a);
					}
				);
			}
		});
	}

	@Override
	public void dealWithServicePic() {
		int[] ints = {64, 64, 64, 57, 50, 60, 67, 62, 59, 56, 44, 26, 65, 66, 50, 29, 24, 31, 32, 39, 38, 37, 63, 41, 33, 28, 36, 35, 34, 58, 61, 30, 25, 27, 43, 55, 54, 52, 53, 40, 50, 51, 49, 48, 47, 46, 45, 44, 42, 68};
		for(int theInt:ints) {
			csqServiceService.synchronizeService(Long.valueOf(theInt));
		}
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

}
