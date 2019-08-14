package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.alibaba.fastjson.JSON;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.enums.application.CsqEntityTypeEnum;
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
					.name(nickname.equals("爱心人士") || matches? null: nickname)
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
				List<TOldPayment> tOldPayments = csqOldPaymentDao.selectByOptionUserInPFId(a.getId(), serviceIds);


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
		List<TOldService> tOldFunds = csqOldServiceDao.selectByStatusByCheckStatusAndAdderEqCHargePersonIdAndFinType(1, 1, "个人冠名基金");
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
		List<TOldPayment> paymentsOfFund = csqOldPaymentDao.selectInPfIdsDesc(oldFundIds);
		List<TOldPayment> paymentsOfService = csqOldPaymentDao.selectInPfIdsDesc(oldServiceIds);

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
						detail = detail == null? "" :detail;
						Long userId = getUserId(a.getOptionuser());    //找到 new
						if (cnt == 0) {
							//作为创建者 -> 找到
							String chargepersonid = tOldService.getChargepersonid();
							userId = Long.valueOf(getUserId(chargepersonid));
							log.info("找到创建者 -> oldId={}, 对应userId={}", chargepersonid, userId);

							TCsqFund.TCsqFundBuilder builder = TCsqFund
								.builder()
								.name(tOldService.getTitle())
								.description(detail.length() > 511 ? "" : detail)
								.trendPubKeys("1,2,3,4");

							if(Double.valueOf(tOldService.getDonationamount()) > 100000) {	//若已达开放条件，则补全（使用占位
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
							if(userId != null) {	//用户数据必须存在
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
					if(!"随手捐".equals(tOldService.getTitle())) {
						String detail = tOldService.getDetail();
						Integer cnt = pfCount.get(pfid);
						if (cnt == null) {
							cnt = 0;
							//开启项目
							if (cnt == 0) {
								if(Double.valueOf(tOldService.getDonationamount()) >= 1) {
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
