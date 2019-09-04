package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.*;
import com.e_commerce.miscroservice.csq_proj.vo.CsqBasicUserVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserInvoiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * 丛善桥后台管理系统通用
 *
 * @Author: FangyiXu
 * @Date: 2019-08-29 09:34
 */
@RestController
@Log
@RequestMapping("csq/manage")
public class CsqManagerSystemController {

	@Autowired
	private CsqFundService csqFundService;

	@Autowired
	private CsqServiceService csqServiceService;

	@Autowired
	private CsqUserService csqUserService;

	@Autowired
	private CsqInvoiceService csqInvoiceService;

	@Autowired
	private CsqMsgService csqMsgService;

	@Autowired
	private CsqPaymentService csqPaymentService;

	@RequestMapping("alive")
	public Object test() {
		return "true";
	}

	/**
	 * 项目列表
	 *
	 * @param searchParam 搜索参数(id/名称)
	 * @param pageNum     页码
	 * @param pageSize    大小
	 * @param isFuzzySearch 是否模糊查找(boolean)
	 *                    {
	 *                    "resultList": [
	 *                    {
	 *                    "id": 49,
	 *                    "createTime": 1566325113000,
	 *                    "updateTime": 1566554025000,
	 *                    "deletedFlag": false,
	 *                    "extend": "154199277700000001",
	 *                    "createUser": 0,
	 *                    "updateUser": "",
	 *                    "isValid": "1",
	 *                    "userId": 2134,
	 *                    "fundId": "",
	 *                    "sumTotalOut": "",
	 *                    "donePercent": "",
	 *                    "sumTotalPayMine": "",
	 *                    "donaterCnt": "",
	 *                    "donaters": "",
	 *                    "trendPubValues": "",
	 *                    "csqUserPaymentRecords": "",
	 *                    "reports": "",
	 *                    "fundStatus": "",
	 *                    "type": 0,
	 *                    "typePubKeys": "",
	 *                    "name": "关爱帕金森病人",
	 *                    "recordNo": "",
	 *                    "status": 0,
	 *                    "purpose": "",
	 *                    "sumTotalIn": 4.77,
	 *                    "totalInCnt": 25,
	 *                    "surplusAmount": 4.77,
	 *                    "expectedAmount": 298000,
	 *                    "expectedRemainAmount": 297999.9,
	 *                    "startDate": "",
	 *                    "endDate": "",
	 *                    "coverPic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/2018/11/154223535700000001.png",
	 *                    "description": "项目介绍\r\n沉闷的夏天，夜已深，工地的简易工房里，辛苦了一天的工友们都已经渐入梦乡，唯有他还僵坐在床边上，凝视着已经熟睡的儿子，泪水再也控住不住的噼里啪啦滚落下来。\r\n儿子，今天是你的生日，爸爸却没有好好给你买个蛋糕，陪你好好的过九岁生日，因为爸爸要在服药后，状态好的时候拼命的工作，多赚一点钱，好让妈妈压力少一点，可以早一天让妈妈不要天天风里来雨里去跑客户，多赚钱为爸爸挣钱买药，周末也可以在家里陪陪姐姐......让爷爷再也不要天天去工地捡废纸板卖钱补贴我们......让奶奶不要天天去菜市场卖菜，卖菜得来的那点小钱也要偷偷塞给我......还有你的姐姐才13岁，放学回家就得在家里烧饭做菜等妈妈回家吃饭，从来没有怨言的姐姐很少说话，成熟懂事的让爸爸心疼......儿子，因为爸爸的疾病连累了一家人，让一家人跟着我受累。\r\n儿子，知道吗，今天晚饭时候你对爸爸说的那几句话，又把爸爸弄哭了，你说：爸爸，之前我一直以为你很少回家，一回家就摆一副可怜样，我一直以为你是装的，我讨厌你，可是，今年暑假跟着爸爸生活了2个月后，我明白爸爸工作真的真的很辛苦，爸爸要做那么多的工作，一天又要那么多次的不会动，我明白了爸爸是因为疾病才会这样的，爸爸，以后我为你去食堂拿饭菜，我为你拿药，我为你敲背，我要学习爸爸，做一个堂堂男子汉......\r\n他轻轻的摸着儿子的小脸，轻轻的俯下身子想亲吻下儿子，却不料因为身体平衡没有控制好自己，却把儿子弄醒了。\r\n“爸爸，爸爸，你怎么了？ 怎么哭了，是疼了吗？ 爸爸不哭，我来为你敲敲背......”儿子的小手为爸爸擦着眼泪。\r\n他一把抱过儿子，贴着儿子的脸蛋，说：“不是的，儿子，爸爸是高兴的，因为你长大了......”\r\n帕金森病是神经系统疾病，患者依赖药物生活，无药效时生活不能自理，医学界称之为最痛苦的疾病之一，当今医学还无法治愈该疾病。帕金森病人的痛苦是常人无法理解的，没有真正接触过就触及不到他们的挣扎。平常的起床，上厕所，走路，吃饭等等一些最简单的日常生活他们在没药效的时候可以说都是奢望，常年靠药物控制，每隔几个小时就得吃一次药。有时忘记吃药了等反应过来发现身体已经僵硬走不了路了，亦或是坐着站不起来了，亦或是端着的碗已经摔碎在地上了.....病人不能自理需家人陪同照顾，致使病人和家属都不能正常上班，而帕金森病的药很多都是进口的，大部分病人每个月光是吃药就要好几千的费用，这足以拖垮很多家庭,这些家庭的孩子一年没几天能吃上点鱼肉就更别说什么蛋糕冰淇淋了......在这里浙江公助帕金森病关爱中心呼吁更多的朋友来帮助患者走出阴霾，直面疾病，正确面对带病人生，留着眼泪依然奔跑.\r\n项目预算\r\n项目预算主要有公助关爱中心场地含房租水电设备购买等，护理社工，公益活动的开展以及参会病友的一些交通伙食费等等：\r\n配捐使用方向：企业和腾讯配捐部分的预算计划将主要用于项目有关方向，99公益日结束并确认了“企业和腾讯配捐”具体金额后15天内，再补充详细预算表。该项目未提取管理费，管理费由机构从其他途径进行筹措。\r\n执行计划\r\n项目执行计划目前先安排的会有赠送药品（考虑帕金森病的药物比较贵很多家庭承受不起），租用场地来给患者及家属做些心理疏导，印刷些宣传用的书籍以及普及些常规的帕金森病知识等等。\r\n发起方\r\n浙江公助帕金森病关爱中心\r\n执行方\r\n浙江公助帕金森病关爱中心\r\n公募支持\r\n浙江省爱心事业基金会\r\n(善款由该基金会接收)\r\n募捐方案备案编号\r\n53330000501874064BA18023\r\n执行能力说明\r\n2016年1月，浙江公助帕金森病关爱中心经浙江省民政厅批准成立。 浙江公助帕金森病关爱中心为病友群体开展救助和关怀服务;帮助病友提高生存质量;倡导病友自助互助、自立自强，以坚强、感恩的心面对生活的精神，倡导与疾病和平相处的理念。\r\n发票说明\r\n1. 公众号申请：如果你的捐款额度超过100元（基于票据以及寄送成本考虑），请长按或者扫描浙江省爱心事业基金会公众号二维码，点击申请捐赠发票。我们预计在收到捐赠开票申请的25个工作日内开具捐赠收据并寄出，敬请谅解。\r\n2. 邮箱申请：浙江省爱心事业基金会将为捐款金额100元（含）以上的乐捐网友开具个人捐赠票据。希望爱心用户按需申请。（需要捐赠收据的必须将交易单号、商户单号、QQ号、发票抬头、金额、捐赠渠道、捐赠日期、联系电话、地址、邮编等信息发至jwang@icpp.org.cn，经确认后我会会尽快回邮捐赠收据）。\r\n3. 有相关问题可以致电办公室电话0571-85395063。",
	 *                    "detailPic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/2018/11/154223535700000001.png",
	 *                    "beneficiary": "",
	 *                    "creditCard": "",
	 *                    "personInCharge": "",
	 *                    "personInChargePic": "",
	 *                    "certificatedNo": ""
	 *                    }
	 *                    ],
	 *                    "totalCount": 1
	 *                    }
	 * @return
	 */
	@RequestMapping("service/list")
	@UrlAuth(withoutPermission = true)
	public AjaxResult listService(String searchParam, Integer pageNum, Integer pageSize, Boolean isFuzzySearch) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("项目列表, userId={}, searchParam={}, pageNum={}, pageSize={}, isFuzzySearch={}", userId, searchParam, pageNum, pageSize, isFuzzySearch);
			QueryResult<TCsqService> list = csqServiceService.list(searchParam, pageNum, pageSize, isFuzzySearch);
			result.setData(list);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "项目列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("项目列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 项目状态数量统计
	 * {
	 * "0": 7	//状态: 数量
	 * }
	 *
	 * @return
	 */
	@RequestMapping("service/count")
	@UrlAuth(withoutPermission = true)
	public AjaxResult countService() {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("项目状态数量统计, userId={}", userId);
			Map<Integer, Object> integerObjectMap = csqServiceService.countGroupByStatus(userId);
			result.setData(integerObjectMap);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "项目状态数量统计", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("项目状态数量统计", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 修改项目数据
	 *
	 * @param serviceId      项目编号
	 * @param status         状态
	 * @param expectedAmount 期望金额
	 * @return
	 */
	@RequestMapping("service/modify")
	@UrlAuth(withoutPermission = true)
	@Consume(TCsqService.class)
	public AjaxResult serviceModify(@RequestParam(required = true) Long serviceId, Integer status, Integer expectedAmount) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		TCsqService obj = (TCsqService) ConsumeHelper.getObj();
		try {
			log.info("修改项目数据, userId={}, serviceId={}, status={}, expectedMoney={}", serviceId, userIds, status, expectedAmount);
			obj.setId(serviceId);
			csqServiceService.modify(obj);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "修改项目数据", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改项目数据", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 项目详情
	 * @param serviceId 项目编号
	 * @return
	 */
	@RequestMapping("service/detail")
	@UrlAuth(withoutPermission = true)
	public AjaxResult serviceDetail(Long serviceId) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("项目详情, userId={}, serviceId={}", userId, serviceId);
			Map<String, Object> detail = csqServiceService.detail(userId, serviceId);
			result.setData(detail);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "项目详情", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("项目详情", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 用户列表
	 * @param searchParam 编号或名字
	 * @param pageNum 页码
	 * @param pageSize 大小
	 * @param isFuzzySearch 是否模糊查询
	 * @return
	 */
	@RequestMapping("user/list")
	@UrlAuth(withoutPermission = true)
	public AjaxResult listUser(String searchParam, Integer pageNum, Integer pageSize, Boolean isFuzzySearch, Integer accountType, Integer availableStatus, boolean gotBalance, boolean gotFund) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		try {
			log.info("用户列表, managerId={}, searchParam={}, pageNum={}, pageSize={}, isFuzzySearch={}, accountType={}, availableStatus={}, gotBalance={}, gotFund={}", managerId, searchParam, pageNum, pageSize, isFuzzySearch, accountType, availableStatus, gotBalance, gotFund);
			QueryResult<TCsqUser> list = csqUserService.list(managerId, searchParam, pageNum, pageSize, isFuzzySearch, accountType, availableStatus, gotBalance, gotFund);
			result.setData(list);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "用户列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("用户列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 用户修改
	 * @param userId 用户编号
	 * @param availableStatus 可用状态
	 * @return
	 */
	@RequestMapping("user/modify")
	@UrlAuth(withoutPermission = true)
	@Consume(CsqBasicUserVo.class)
	public AjaxResult modify(Long userId, Integer availableStatus) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		CsqBasicUserVo obj = (CsqBasicUserVo) ConsumeHelper.getObj();
		try {
			log.info("用户列表, userId={}, availableStatus={}", userId, availableStatus);
			csqUserService.modify(userId, obj, false);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "用户列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("用户列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 用户详情
	 * @param userId 用户编号
	 * @return
	 */
	@RequestMapping("user/detail")
	@UrlAuth(withoutPermission = true)
	@Consume(CsqBasicUserVo.class)
	public AjaxResult detail(Long userId) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		try {
			log.info("用户详情, userId={}", userId);
			CsqBasicUserVo infos = csqUserService.infos(userId);
			result.setData(infos);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "用户详情", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("用户详情", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 基金列表
	 * @param pageNum
	 * @param pageSize
	 * @param searchParam
	 * @param status
	 * @param trendPubKeys
	 * @return
	 */
	@RequestMapping("fund/list")
	@UrlAuth(withoutPermission = true)
	public AjaxResult fundList(Integer pageNum, Integer pageSize, Boolean isFundParam, String searchParam, Integer status, Boolean isFuzzySearch, String... trendPubKeys) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		try {
			log.info("基金列表,pageNum={}, pageSize={}, managerId={}, isFundParam={}, searchParam={}, status={}, isFuzzySearch={}, trendPubKeys={}", managerId, searchParam, status, trendPubKeys);
			csqFundService.searchList(isFundParam, searchParam, status, Arrays.asList(trendPubKeys), pageNum, pageSize, isFundParam);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "基金列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("基金列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 基金修改
	 * @param fundId
	 * @param status
	 * @return
	 */
	@RequestMapping("fund/modify")
	@UrlAuth(withoutPermission = true)
	@Consume(TCsqFund.class)
	public AjaxResult fundModify(@RequestParam Long fundId, Integer status) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		TCsqFund obj = (TCsqFund) ConsumeHelper.getObj();
		obj.setId(fundId);
		try {
			log.info("基金修改, fundId={}, status={}", fundId, status);
			csqFundService.modifyFundManager(managerId, obj);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "基金修改", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("基金修改", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 基金详情
	 * @param fundId
	 * @return
	 */
	@RequestMapping("fund/detail")
	@UrlAuth(withoutPermission = true)
	@Consume(TCsqFund.class)
	public AjaxResult fundDetail(@RequestParam Long fundId) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		try {
			log.info("基金详情, fundId={}", fundId);
			csqFundService.fundDetail(managerId, fundId);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "基金详情", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("基金详情", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 发票列表
	 * @param searchParam
	 * @param isOut
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("invoice/list")
	@UrlAuth(withoutPermission = true)
	public AjaxResult invoiceList(String searchParam, Integer isOut, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		try {
			log.info("发票申请列表, searchParam={}, inOut={}, pageNum={}, pageSize={}", searchParam, isOut, pageNum, pageSize);
			csqInvoiceService.list(searchParam, isOut, pageNum, pageSize);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "发票申请列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("发票申请列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 发票修改（标记已开票）
	 * @param invoiceId 发票编号
	 * @param isOut 是否开票0未开1已开
	 * @return
	 */
	@RequestMapping("invoice/modify")
	@UrlAuth(withoutPermission = true)
	@Consume(TCsqUserInvoice.class)
	public AjaxResult invoiceModify(Long invoiceId, Integer isOut) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		TCsqUserInvoice obj = (TCsqUserInvoice) ConsumeHelper.getObj();
		obj.setId(invoiceId);
		try {
			log.info("发票修改, userId={}, invoiceId={}, isOut={}", managerId, invoiceId, isOut);
			csqInvoiceService.modify(obj);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "发票修改", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("发票修改", e);
			result.setSuccess(false);
		}
		return result;
	}


	/**
	 * 发票的详情
	 * @param invoiceId 发票编号
	 * @return
	 */
	@RequestMapping("invoice/detail")
	@UrlAuth(withoutPermission = true)
	public AjaxResult invoiceDetail(Long invoiceId) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		try {
			log.info("发票的详情, userId={}, invoiceId={}", managerId, invoiceId);
			CsqUserInvoiceVo csqUserInvoiceVo = csqInvoiceService.invoiceDetail(managerId, invoiceId);
			result.setData(csqUserInvoiceVo);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "发票的详情", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("发票的详情", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 消息列表
	 * @param searchParam
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("message/list")
	@UrlAuth(withoutPermission = true)
	public AjaxResult msgList(String searchParam, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		try {
			log.info("消息列表, searchParam={},pageNum={}, pageSize={}", searchParam, pageNum, pageSize);
			QueryResult<TCsqSysMsg> list = csqMsgService.list(searchParam, pageNum, pageSize);
			result.setData(list);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "消息列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("消息列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 组织实名的认证审核
	 *
	 * @param userAuthId 认证记录编号
	 * @param option     操作1通过2拒绝
	 * @param content	描述
	 *                   <p>
	 *                   {"success":true,"errorCode":"","msg":"","data":""}
	 * @return
	 */
	@RequestMapping("corp/cert/do")
	public AjaxResult certCorpDo(Long userAuthId, Integer option, String content) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		try {
			log.info("组织实名的认证审核, ");
			csqUserService.certCorp(userAuthId, option, content);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "组织实名的认证审核", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("组织实名的认证审核", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 组织实名认证详情
	 * @param userAuthId  实名认证编号
	 * @return
	 */
	@RequestMapping("corp/detail")
	public AjaxResult certCorpDetail(Long userAuthId) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		try {
			log.info("组织实名的认证详情, ");
			TCsqUserAuth userAuth = csqUserService.corpCertDetail(userAuthId);
			result.setData(userAuth);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "组织实名的认证详情", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("组织实名的认证详情", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 组织实名认证列表
	 * @param status  状态
	 * @param pageNum  页码
	 * @param pageSize  分页大小
	 * @return
	 */
	@RequestMapping("corp/cert/list")
	public AjaxResult certCorpList(Integer status, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		try {
			log.info("组织实名认证列表, userIds={}, status={}, pageNum={}, pageSize={}", userIds, status, pageNum, pageSize);
			QueryResult<TCsqUserAuth> userAuth = csqUserService.certCorpList(status, pageNum, pageSize);
			result.setData(userAuth);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "组织实名认证列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("组织实名认证列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 组织实名认证数量
	 * @param status 状态
	 * @return
	 */
	@RequestMapping("corp/cert/count")
	public AjaxResult certCorpCnt(Integer status) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		try {
			log.info("组织实名认证数量, userIds={}, status={}", userIds, status);
			int count = csqUserService.certCorpCount(status);
			result.setData(count);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "组织实名认证数量", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("组织实名认证数量", e);
			result.setSuccess(false);
		}
		return result;
	}

	public AjaxResult findWaters(String searchParma, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		try {
			log.info("爱心账户充值记录, userIds={}, searchParma={}, pageNum={}, pageSize={}", userIds, searchParma, pageNum, pageSize);
			csqPaymentService.findWaters(searchParma, pageNum, pageSize);
//			result.setData(count);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "爱心账户充值记录", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("爱心账户充值记录", e);
			result.setSuccess(false);
		}
		return result;
	}

}
