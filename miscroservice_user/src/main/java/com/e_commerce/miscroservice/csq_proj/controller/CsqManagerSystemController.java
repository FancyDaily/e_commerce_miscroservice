package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Check;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.*;
import com.e_commerce.miscroservice.csq_proj.vo.CsqBasicUserVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqFundVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqMoneyApplyRecordVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserInvoiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

	@Autowired
	private CsqMoneyApplyRecordService csqMoneyApplyRecordService;

	@RequestMapping("alive")
	public Object test() {
		return "true";
	}

	/**
	 * 项目列表
	 *
	 * @param searchParam   搜索参数(id/名称)
	 * @param pageNum       页码
	 * @param pageSize      大小
	 * @param isFuzzySearch 是否模糊查找(boolean)
	 *                      {
	 *                      "resultList": [
	 *                      {
	 *                      "id": 49,
	 *                      "createTime": 1566325113000,
	 *                      "updateTime": 1566554025000,
	 *                      "deletedFlag": false,
	 *                      "extend": "154199277700000001",
	 *                      "createUser": 0,
	 *                      "updateUser": "",
	 *                      "isValid": "1",
	 *                      "userId": 2134,
	 *                      "fundId": "",
	 *                      "sumTotalOut": "",
	 *                      "donePercent": "",
	 *                      "sumTotalPayMine": "",
	 *                      "donaterCnt": "",
	 *                      "donaters": "",
	 *                      "trendPubValues": "",
	 *                      "csqUserPaymentRecords": "",
	 *                      "reports": "",
	 *                      "fundStatus": "",
	 *                      "type": 0,
	 *                      "typePubKeys": "",
	 *                      "name": "关爱帕金森病人",
	 *                      "recordNo": "",
	 *                      "status": 0,
	 *                      "purpose": "",
	 *                      "sumTotalIn": 4.77,
	 *                      "totalInCnt": 25,
	 *                      "surplusAmount": 4.77,
	 *                      "expectedAmount": 298000,
	 *                      "expectedRemainAmount": 297999.9,
	 *                      "startDate": "",
	 *                      "endDate": "",
	 *                      "coverPic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/2018/11/154223535700000001.png",
	 *                      "description": "项目介绍\r\n沉闷的夏天，夜已深，工地的简易工房里，辛苦了一天的工友们都已经渐入梦乡，唯有他还僵坐在床边上，凝视着已经熟睡的儿子，泪水再也控住不住的噼里啪啦滚落下来。\r\n儿子，今天是你的生日，爸爸却没有好好给你买个蛋糕，陪你好好的过九岁生日，因为爸爸要在服药后，状态好的时候拼命的工作，多赚一点钱，好让妈妈压力少一点，可以早一天让妈妈不要天天风里来雨里去跑客户，多赚钱为爸爸挣钱买药，周末也可以在家里陪陪姐姐......让爷爷再也不要天天去工地捡废纸板卖钱补贴我们......让奶奶不要天天去菜市场卖菜，卖菜得来的那点小钱也要偷偷塞给我......还有你的姐姐才13岁，放学回家就得在家里烧饭做菜等妈妈回家吃饭，从来没有怨言的姐姐很少说话，成熟懂事的让爸爸心疼......儿子，因为爸爸的疾病连累了一家人，让一家人跟着我受累。\r\n儿子，知道吗，今天晚饭时候你对爸爸说的那几句话，又把爸爸弄哭了，你说：爸爸，之前我一直以为你很少回家，一回家就摆一副可怜样，我一直以为你是装的，我讨厌你，可是，今年暑假跟着爸爸生活了2个月后，我明白爸爸工作真的真的很辛苦，爸爸要做那么多的工作，一天又要那么多次的不会动，我明白了爸爸是因为疾病才会这样的，爸爸，以后我为你去食堂拿饭菜，我为你拿药，我为你敲背，我要学习爸爸，做一个堂堂男子汉......\r\n他轻轻的摸着儿子的小脸，轻轻的俯下身子想亲吻下儿子，却不料因为身体平衡没有控制好自己，却把儿子弄醒了。\r\n“爸爸，爸爸，你怎么了？ 怎么哭了，是疼了吗？ 爸爸不哭，我来为你敲敲背......”儿子的小手为爸爸擦着眼泪。\r\n他一把抱过儿子，贴着儿子的脸蛋，说：“不是的，儿子，爸爸是高兴的，因为你长大了......”\r\n帕金森病是神经系统疾病，患者依赖药物生活，无药效时生活不能自理，医学界称之为最痛苦的疾病之一，当今医学还无法治愈该疾病。帕金森病人的痛苦是常人无法理解的，没有真正接触过就触及不到他们的挣扎。平常的起床，上厕所，走路，吃饭等等一些最简单的日常生活他们在没药效的时候可以说都是奢望，常年靠药物控制，每隔几个小时就得吃一次药。有时忘记吃药了等反应过来发现身体已经僵硬走不了路了，亦或是坐着站不起来了，亦或是端着的碗已经摔碎在地上了.....病人不能自理需家人陪同照顾，致使病人和家属都不能正常上班，而帕金森病的药很多都是进口的，大部分病人每个月光是吃药就要好几千的费用，这足以拖垮很多家庭,这些家庭的孩子一年没几天能吃上点鱼肉就更别说什么蛋糕冰淇淋了......在这里浙江公助帕金森病关爱中心呼吁更多的朋友来帮助患者走出阴霾，直面疾病，正确面对带病人生，留着眼泪依然奔跑.\r\n项目预算\r\n项目预算主要有公助关爱中心场地含房租水电设备购买等，护理社工，公益活动的开展以及参会病友的一些交通伙食费等等：\r\n配捐使用方向：企业和腾讯配捐部分的预算计划将主要用于项目有关方向，99公益日结束并确认了“企业和腾讯配捐”具体金额后15天内，再补充详细预算表。该项目未提取管理费，管理费由机构从其他途径进行筹措。\r\n执行计划\r\n项目执行计划目前先安排的会有赠送药品（考虑帕金森病的药物比较贵很多家庭承受不起），租用场地来给患者及家属做些心理疏导，印刷些宣传用的书籍以及普及些常规的帕金森病知识等等。\r\n发起方\r\n浙江公助帕金森病关爱中心\r\n执行方\r\n浙江公助帕金森病关爱中心\r\n公募支持\r\n浙江省爱心事业基金会\r\n(善款由该基金会接收)\r\n募捐方案备案编号\r\n53330000501874064BA18023\r\n执行能力说明\r\n2016年1月，浙江公助帕金森病关爱中心经浙江省民政厅批准成立。 浙江公助帕金森病关爱中心为病友群体开展救助和关怀服务;帮助病友提高生存质量;倡导病友自助互助、自立自强，以坚强、感恩的心面对生活的精神，倡导与疾病和平相处的理念。\r\n发票说明\r\n1. 公众号申请：如果你的捐款额度超过100元（基于票据以及寄送成本考虑），请长按或者扫描浙江省爱心事业基金会公众号二维码，点击申请捐赠发票。我们预计在收到捐赠开票申请的25个工作日内开具捐赠收据并寄出，敬请谅解。\r\n2. 邮箱申请：浙江省爱心事业基金会将为捐款金额100元（含）以上的乐捐网友开具个人捐赠票据。希望爱心用户按需申请。（需要捐赠收据的必须将交易单号、商户单号、QQ号、发票抬头、金额、捐赠渠道、捐赠日期、联系电话、地址、邮编等信息发至jwang@icpp.org.cn，经确认后我会会尽快回邮捐赠收据）。\r\n3. 有相关问题可以致电办公室电话0571-85395063。",
	 *                      "detailPic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/2018/11/154223535700000001.png",
	 *                      "beneficiary": "",
	 *                      "creditCard": "",
	 *                      "personInCharge": "",
	 *                      "personInChargePic": "",
	 *                      "certificatedNo": ""
	 *                      }
	 *                      ],
	 *                      "totalCount": 1
	 *                      }
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
	 *                       <p>
	 *                       {"success":true,"errorCode":"","msg":"","data":""}
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
	 *
	 * @param serviceId 项目编号
	 *                  <p>
	 *                  {
	 *                  "donated": false,
	 *                  "serviceVo": {
	 *                  "id": 49,
	 *                  "userId": 2134,
	 *                  "fundId": "",
	 *                  "donePercent": "0",
	 *                  "sumTotalPayMine": "",
	 *                  "donaterCnt": "",
	 *                  "trendPubValues": "",
	 *                  "fundStatus": "",
	 *                  "type": 0,
	 *                  "typePubKeys": "1,2,3,4",
	 *                  "name": "关爱帕金森病人",
	 *                  "recordNo": "",
	 *                  "status": 1,
	 *                  "purpose": "为患有帕金森综合症的患者构建公助关爱中心提供场地含房租水电设备购买等，护理社工，公益活动的开展以及参会病友的一些交通伙食费",
	 *                  "sumTotalIn": 4.77,
	 *                  "totalInCnt": 25,
	 *                  "surplusAmount": 4.77,
	 *                  "expectedAmount": 298000,
	 *                  "expectedRemainAmount": 297999.9,
	 *                  "startDate": "",
	 *                  "endDate": "",
	 *                  "coverPic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/49_gapjs/49_gapjs_cover.png",
	 *                  "sharePic": "",
	 *                  "description": "项目介绍\r\n沉闷的夏天，夜已深，工地的简易工房里，辛苦了一天的工友们都已经渐入梦乡，唯有他还僵坐在床边上，凝视着已经熟睡的儿子，泪水再也控住不住的噼里啪啦滚落下来。\r\n儿子，今天是你的生日，爸爸却没有好好给你买个蛋糕，陪你好好的过九岁生日，因为爸爸要在服药后，状态好的时候拼命的工作，多赚一点钱，好让妈妈压力少一点，可以早一天让妈妈不要天天风里来雨里去跑客户，多赚钱为爸爸挣钱买药，周末也可以在家里陪陪姐姐......让爷爷再也不要天天去工地捡废纸板卖钱补贴我们......让奶奶不要天天去菜市场卖菜，卖菜得来的那点小钱也要偷偷塞给我......还有你的姐姐才13岁，放学回家就得在家里烧饭做菜等妈妈回家吃饭，从来没有怨言的姐姐很少说话，成熟懂事的让爸爸心疼......儿子，因为爸爸的疾病连累了一家人，让一家人跟着我受累。\r\n儿子，知道吗，今天晚饭时候你对爸爸说的那几句话，又把爸爸弄哭了，你说：爸爸，之前我一直以为你很少回家，一回家就摆一副可怜样，我一直以为你是装的，我讨厌你，可是，今年暑假跟着爸爸生活了2个月后，我明白爸爸工作真的真的很辛苦，爸爸要做那么多的工作，一天又要那么多次的不会动，我明白了爸爸是因为疾病才会这样的，爸爸，以后我为你去食堂拿饭菜，我为你拿药，我为你敲背，我要学习爸爸，做一个堂堂男子汉......\r\n他轻轻的摸着儿子的小脸，轻轻的俯下身子想亲吻下儿子，却不料因为身体平衡没有控制好自己，却把儿子弄醒了。\r\n“爸爸，爸爸，你怎么了？ 怎么哭了，是疼了吗？ 爸爸不哭，我来为你敲敲背......”儿子的小手为爸爸擦着眼泪。\r\n他一把抱过儿子，贴着儿子的脸蛋，说：“不是的，儿子，爸爸是高兴的，因为你长大了......”\r\n帕金森病是神经系统疾病，患者依赖药物生活，无药效时生活不能自理，医学界称之为最痛苦的疾病之一，当今医学还无法治愈该疾病。帕金森病人的痛苦是常人无法理解的，没有真正接触过就触及不到他们的挣扎。平常的起床，上厕所，走路，吃饭等等一些最简单的日常生活他们在没药效的时候可以说都是奢望，常年靠药物控制，每隔几个小时就得吃一次药。有时忘记吃药了等反应过来发现身体已经僵硬走不了路了，亦或是坐着站不起来了，亦或是端着的碗已经摔碎在地上了.....病人不能自理需家人陪同照顾，致使病人和家属都不能正常上班，而帕金森病的药很多都是进口的，大部分病人每个月光是吃药就要好几千的费用，这足以拖垮很多家庭,这些家庭的孩子一年没几天能吃上点鱼肉就更别说什么蛋糕冰淇淋了......在这里浙江公助帕金森病关爱中心呼吁更多的朋友来帮助患者走出阴霾，直面疾病，正确面对带病人生，留着眼泪依然奔跑.\r\n项目预算\r\n项目预算主要有公助关爱中心场地含房租水电设备购买等，护理社工，公益活动的开展以及参会病友的一些交通伙食费等等：\r\n配捐使用方向：企业和腾讯配捐部分的预算计划将主要用于项目有关方向，99公益日结束并确认了“企业和腾讯配捐”具体金额后15天内，再补充详细预算表。该项目未提取管理费，管理费由机构从其他途径进行筹措。\r\n执行计划\r\n项目执行计划目前先安排的会有赠送药品（考虑帕金森病的药物比较贵很多家庭承受不起），租用场地来给患者及家属做些心理疏导，印刷些宣传用的书籍以及普及些常规的帕金森病知识等等。\r\n发起方\r\n浙江公助帕金森病关爱中心\r\n执行方\r\n浙江公助帕金森病关爱中心\r\n公募支持\r\n浙江省爱心事业基金会\r\n(善款由该基金会接收)\r\n募捐方案备案编号\r\n53330000501874064BA18023\r\n执行能力说明\r\n2016年1月，浙江公助帕金森病关爱中心经浙江省民政厅批准成立。 浙江公助帕金森病关爱中心为病友群体开展救助和关怀服务;帮助病友提高生存质量;倡导病友自助互助、自立自强，以坚强、感恩的心面对生活的精神，倡导与疾病和平相处的理念。\r\n发票说明\r\n1. 公众号申请：如果你的捐款额度超过100元（基于票据以及寄送成本考虑），请长按或者扫描浙江省爱心事业基金会公众号二维码，点击申请捐赠发票。我们预计在收到捐赠开票申请的25个工作日内开具捐赠收据并寄出，敬请谅解。\r\n2. 邮箱申请：浙江省爱心事业基金会将为捐款金额100元（含）以上的乐捐网友开具个人捐赠票据。希望爱心用户按需申请。（需要捐赠收据的必须将交易单号、商户单号、QQ号、发票抬头、金额、捐赠渠道、捐赠日期、联系电话、地址、邮编等信息发至jwang@icpp.org.cn，经确认后我会会尽快回邮捐赠收据）。\r\n3. 有相关问题可以致电办公室电话0571-85395063。",
	 *                  "raiseStatus": 0,
	 *                  "donaters": [
	 *                  {
	 *                  "id": 181,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": 1,
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": 1,
	 *                  "totalDonate": 3,
	 *                  "minutesAgo": "",
	 *                  "userAccount": "",
	 *                  "name": "张炳钩",
	 *                  "userTel": "13777771934",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "sex": 0,
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "1,2,3,4",
	 *                  "authStatus": 0
	 *                  },
	 *                  {
	 *                  "id": 231,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": 1,
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": 1,
	 *                  "totalDonate": 1.11,
	 *                  "minutesAgo": "",
	 *                  "userAccount": "",
	 *                  "name": "周涛",
	 *                  "userTel": "18657217667",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "sex": 0,
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "1,2,3,4",
	 *                  "authStatus": 0
	 *                  },
	 *                  {
	 *                  "id": 1,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": 1,
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": 1,
	 *                  "totalDonate": 0.54,
	 *                  "minutesAgo": "",
	 *                  "userAccount": "",
	 *                  "name": "张景春",
	 *                  "userTel": "18014803100",
	 *                  "userHeadPortraitPath": "https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJwy3I8DFo54Z2ibdfCoUe4CjKrbv0WiaKRxB9wGpw4kIBibOz4TfHw1sGdZyeicFApAZyq5IibuV7kjhg/132",
	 *                  "sex": 0,
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "1,2,3,4",
	 *                  "authStatus": 1
	 *                  }
	 *                  ],
	 *                  "sumTotalOut": 0,
	 *                  "csqUserPaymentRecordVos": [
	 *                  {
	 *                  "id": 4956,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 444491,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1473,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 4959,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 444490,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1474,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 4962,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 444490,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1475,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 4965,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 444489,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1476,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 4968,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 444486,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1477,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 4971,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 444484,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1478,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 4974,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 444481,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1479,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 4977,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 444228,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1480,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.12
	 *                  },
	 *                  {
	 *                  "id": 4980,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 443207,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1481,
	 *                  "userId": 102,
	 *                  "entityId": 102,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 4983,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 441797,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1482,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 4986,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 420071,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1483,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.1
	 *                  },
	 *                  {
	 *                  "id": 4989,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 396045,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1484,
	 *                  "userId": 231,
	 *                  "entityId": 231,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.1
	 *                  },
	 *                  {
	 *                  "id": 4992,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 396045,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1485,
	 *                  "userId": 231,
	 *                  "entityId": 231,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 1
	 *                  },
	 *                  {
	 *                  "id": 4995,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 396034,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1486,
	 *                  "userId": 231,
	 *                  "entityId": 231,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 4998,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 384938,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1487,
	 *                  "userId": 181,
	 *                  "entityId": 181,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 1
	 *                  },
	 *                  {
	 *                  "id": 5001,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 384937,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1488,
	 *                  "userId": 181,
	 *                  "entityId": 181,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 1
	 *                  },
	 *                  {
	 *                  "id": 5004,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 384936,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1489,
	 *                  "userId": 181,
	 *                  "entityId": 181,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 1
	 *                  },
	 *                  {
	 *                  "id": 5007,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 384380,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1490,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.1
	 *                  },
	 *                  {
	 *                  "id": 5010,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 384375,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1491,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.1
	 *                  },
	 *                  {
	 *                  "id": 5013,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 384052,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1492,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 5016,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 384048,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1493,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 5019,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 384047,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1494,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 5022,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 384046,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1495,
	 *                  "userId": 1,
	 *                  "entityId": 1,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 5328,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 358366,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 1597,
	 *                  "userId": 57,
	 *                  "entityId": 57,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.01
	 *                  },
	 *                  {
	 *                  "id": 6984,
	 *                  "weiboAccount": "",
	 *                  "wechatPubAccount": "",
	 *                  "csqUserAuth": "",
	 *                  "existDayCnt": "",
	 *                  "balanceStatus": "",
	 *                  "gotFund": false,
	 *                  "gotCompanyAccount": false,
	 *                  "accountType": "",
	 *                  "totalDonate": "",
	 *                  "minutesAgo": 80301,
	 *                  "userAccount": "",
	 *                  "name": "",
	 *                  "userTel": "",
	 *                  "userHeadPortraitPath": "",
	 *                  "sex": "",
	 *                  "remarks": "",
	 *                  "contactPerson": "",
	 *                  "contactNo": "",
	 *                  "trendPubKeys": "",
	 *                  "authStatus": "",
	 *                  "orderId": 2149,
	 *                  "userId": 1852,
	 *                  "entityId": 1852,
	 *                  "entityType": 1,
	 *                  "fromType": "",
	 *                  "serviceName": "",
	 *                  "date": "",
	 *                  "description": "向\"关爱帕金森病人\"项目",
	 *                  "inOrOut": 1,
	 *                  "money": 0.1
	 *                  }
	 *                  ],
	 *                  "csqServiceReportVos": [],
	 *                  "detailPic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/2018/11/154223535700000001.png",
	 *                  "beneficiary": "",
	 *                  "creditCard": "",
	 *                  "personInCharge": "周涛",
	 *                  "occupation": "浙江省爱心事业基金会项目专员",
	 *                  "personInChargePic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "certificatedNo": "",
	 *                  "trendPubNames": [
	 *                  "教育助学",
	 *                  "医疗救助",
	 *                  "灾害援助",
	 *                  "扶贫济困"
	 *                  ]
	 *                  },
	 *                  "broadCast": [
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "采薇东篱 都市悠客",
	 *                  "donateAmount": 0.1,
	 *                  "createTime": 1566325474333,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "庄文超",
	 *                  "donateAmount": 0.01,
	 *                  "createTime": 1566325178479,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张景春1",
	 *                  "donateAmount": 0.01,
	 *                  "createTime": 1566325125176,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张景春1",
	 *                  "donateAmount": 0.01,
	 *                  "createTime": 1566325124685,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张景春1",
	 *                  "donateAmount": 0.01,
	 *                  "createTime": 1566325124174,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张景春1",
	 *                  "donateAmount": 0.01,
	 *                  "createTime": 1566325123667,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张景春1",
	 *                  "donateAmount": 0.1,
	 *                  "createTime": 1566325123178,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张景春1",
	 *                  "donateAmount": 0.1,
	 *                  "createTime": 1566325122693,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张炳钩",
	 *                  "donateAmount": 1,
	 *                  "createTime": 1566325122178,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张炳钩",
	 *                  "donateAmount": 1,
	 *                  "createTime": 1566325121645,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张炳钩",
	 *                  "donateAmount": 1,
	 *                  "createTime": 1566325121145,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "周涛",
	 *                  "donateAmount": 0.01,
	 *                  "createTime": 1566325120638,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "周涛",
	 *                  "donateAmount": 1,
	 *                  "createTime": 1566325120137,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "周涛",
	 *                  "donateAmount": 0.1,
	 *                  "createTime": 1566325119635,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张景春1",
	 *                  "donateAmount": 0.1,
	 *                  "createTime": 1566325119114,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张景春1",
	 *                  "donateAmount": 0.01,
	 *                  "createTime": 1566325118609,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "伍雪冰",
	 *                  "donateAmount": 0.01,
	 *                  "createTime": 1566325118105,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张景春1",
	 *                  "donateAmount": 0.12,
	 *                  "createTime": 1566325117592,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张景春1",
	 *                  "donateAmount": 0.01,
	 *                  "createTime": 1566325117065,
	 *                  "toName": "关爱帕金森病人"
	 *                  },
	 *                  {
	 *                  "minutesAgo": 60,
	 *                  "minutesAgoStr": "",
	 *                  "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                  "name": "张景春1",
	 *                  "donateAmount": 0.01,
	 *                  "createTime": 1566325116540,
	 *                  "toName": "关爱帕金森病人"
	 *                  }
	 *                  ],
	 *                  "isMine": false,
	 *                  "isFund": false
	 *                  }
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
	 *
	 * @param searchParam     编号或名字
	 * @param pageNum         页码
	 * @param pageSize        大小
	 * @param isFuzzySearch   是否模糊查询
	 * @param accountType     用户类型
	 * @param availableStatus 可用状态
	 * @param gotBalance      是否有账户
	 * @param gotFund         是否有基金
	 *                        <p>
	 *                        {
	 *                        "resultList": [
	 *                        {
	 *                        "id": 2864,
	 *                        "createTime": 1568864595000,
	 *                        "updateTime": 1568947432000,
	 *                        "deletedFlag": false,
	 *                        "extend": "",
	 *                        "createUser": 0,
	 *                        "updateUser": "",
	 *                        "isValid": "1",
	 *                        "totalDonate": "",
	 *                        "minutesAgo": "",
	 *                        "uuid": "",
	 *                        "token": "",
	 *                        "password": "",
	 *                        "userAccount": "",
	 *                        "name": "母笑阳",
	 *                        "userTel": "",
	 *                        "jurisdiction": 0,
	 *                        "userHeadPortraitPath": "https://wx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEKLajcwEbibuU7c7ibzLkSTsMYQGOOIdTqicNNJCtOM0phxYP4L6XZMgKk6TCicichuk0vfEGvLx3DDczQ/132",
	 *                        "userPicturePath": "",
	 *                        "weiboAccount": "",
	 *                        "wechatPubAccount": "",
	 *                        "vxOpenId": "ojyAN5OM9A6T5QVmzAFa2Yj96sZw",
	 *                        "vxId": "",
	 *                        "occupation": "",
	 *                        "workPlace": "",
	 *                        "college": "",
	 *                        "age": "",
	 *                        "birthday": "",
	 *                        "sex": 0,
	 *                        "followNum": 0,
	 *                        "remarks": "",
	 *                        "level": 1,
	 *                        "growthValue": "",
	 *                        "payNum": 0,
	 *                        "sumTotalPay": 0,
	 *                        "surplusAmount": 0,
	 *                        "balanceStatus": 0,
	 *                        "authenticationStatus": 0,
	 *                        "authenticationType": 1,
	 *                        "skill": "",
	 *                        "integrity": 0,
	 *                        "masterStatus": 0,
	 *                        "authStatus": 1,
	 *                        "inviteCode": "",
	 *                        "avaliableStatus": 1,
	 *                        "accountType": 1,
	 *                        "isFake": 0,
	 *                        "contactPerson": "",
	 *                        "contactNo": "",
	 *                        "trendPubKeys": "",
	 *                        "oldId": "",
	 *                        "mail": ""
	 *                        }
	 *                        ],
	 *                        "totalCount": 2864
	 *                        }
	 * @return
	 */
	@RequestMapping("user/list")
	@UrlAuth(withoutPermission = true)
	public AjaxResult listUser(String searchParam, Integer pageNum, Integer pageSize, Boolean isFuzzySearch, Integer accountType, Integer availableStatus, Boolean gotBalance, Boolean gotFund) {
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
	 *
	 * @param userId          用户编号
	 * @param availableStatus 可用状态
	 *                        <p>
	 *                        {"success":true,"errorCode":"","msg":"","data":""}
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
	 *
	 * @param userId 用户编号
	 *               <p>
	 *               {
	 *               "id": 2,
	 *               "weiboAccount": "",
	 *               "wechatPubAccount": "",
	 *               "csqUserAuth": {
	 *               "id": "",
	 *               "userId": "",
	 *               "type": "",
	 *               "cardId": "",
	 *               "name": "",
	 *               "phone": "",
	 *               "licensePic": "",
	 *               "licenseId": "",
	 *               "status": ""
	 *               },
	 *               "existDayCnt": 1187,
	 *               "balanceStatus": 0,
	 *               "gotFund": false,
	 *               "gotCompanyAccount": false,
	 *               "accountType": 1,
	 *               "totalDonate": "",
	 *               "minutesAgo": "",
	 *               "userAccount": "",
	 *               "name": "灏灏",
	 *               "userTel": "15950210733",
	 *               "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *               "sex": 0,
	 *               "remarks": "",
	 *               "contactPerson": "",
	 *               "contactNo": "",
	 *               "availableStatus": 1,
	 *               "trendPubKeys": "",
	 *               "authStatus": 0
	 *               }
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
	 *
	 * @param pageNum       页码
	 * @param pageSize      大小
	 * @param searchParam   搜索参数
	 * @param isFundParam   是否为基金
	 * @param isFuzzySearch 是否为模糊查询
	 * @param status        状态
	 * @param trendPubKeys  倾向
	 *                      <p>
	 *                      "resultList": [
	 *                      {
	 *                      "id": 27,
	 *                      "createTime": 1566324564000,
	 *                      "updateTime": 1568858356000,
	 *                      "deletedFlag": false,
	 *                      "extend": "154198893500000001",
	 *                      "createUser": 0,
	 *                      "updateUser": "",
	 *                      "isValid": "1",
	 *                      "userId": 2412,
	 *                      "totalToItemCnt": "",
	 *                      "purpose": "由优酷《这！就是街舞1》年度总冠军-韩宇担任“筑梦计划，街舞公益课堂大使”，2019年4月11日，由共青团中央社会联络，中国舞蹈家协会共同发起的“新时代，艺起来”——全国街舞联盟公益教室进学校（乡村）项目正式启动。",
	 *                      "fundNo": "",
	 *                      "trendPubKeys": "1,2,3,4",
	 *                      "name": "浙里有街舞基金",
	 *                      "detailPic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_whole_description_new_1.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_whole_description_new_2.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_whole_description_new_3.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_whole_description_new_4.png",
	 *                      "description": "",
	 *                      "coverPic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/27_zlyjwjj_cover.jpg,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_1.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_2.png",
	 *                      "sharePic": "",
	 *                      "orgName": "",
	 *                      "orgAddr": "",
	 *                      "contact": "",
	 *                      "personInCharge": "海边",
	 *                      "occupation": "主任",
	 *                      "personInChargePic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/fund_person_in_charge/fund27.jpg",
	 *                      "creditCardName": "",
	 *                      "creditCardId": "",
	 *                      "balance": 12000,
	 *                      "sumTotalIn": 12000,
	 *                      "totalInCnt": 6,
	 *                      "agentModeStatus": 0,
	 *                      "status": 2,
	 *                      "helpCnt": 0,
	 *                      "isShown": 1
	 *                      }
	 *                      ],
	 *                      "totalCount": 1
	 *                      }
	 * @return
	 */
	@RequestMapping("fund/list")
	@UrlAuth(withoutPermission = true)
	public AjaxResult fundList(Integer pageNum, Integer pageSize, Boolean isFundParam, String searchParam, Integer status, Boolean isFuzzySearch, String... trendPubKeys) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		try {
			log.info("基金列表,pageNum={}, pageSize={}, managerId={}, isFundParam={}, searchParam={}, status={}, isFuzzySearch={}, trendPubKeys={}", pageNum, pageSize, managerId, isFundParam, searchParam, status, isFuzzySearch, trendPubKeys);
			QueryResult<TCsqFund> queryResult = csqFundService.searchList(isFundParam, searchParam, status, trendPubKeys == null ? new ArrayList<>() : Arrays.asList(trendPubKeys), pageNum, pageSize, isFundParam);
			result.setData(queryResult);
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
	 *
	 * @param fundId 基金编号
	 * @param status 状态
	 *               <p>
	 *               {"success":true,"errorCode":"","msg":"","data":""}
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
	 *
	 * @param fundId 基金编号
	 *               <p>
	 *               {
	 *               "donaterList": [],
	 *               "isDonated": false,
	 *               "serviceId": 27,
	 *               "contributeInCnt": 6,
	 *               "trendPubNames": [
	 *               "教育助学",
	 *               "医疗救助",
	 *               "灾害援助",
	 *               "扶贫济困"
	 *               ],
	 *               "goToList": [],
	 *               "mine": false,
	 *               "raiseStatus": 1,
	 *               "stationorgName": "浙里有街舞基金",
	 *               "stationorgAddr": "",
	 *               "stationcontact": "",
	 *               "stationPersonIncharge": "",
	 *               "stationcreditCardName": "",
	 *               "stationcreditCardId": "",
	 *               "id": 27,
	 *               "userId": 2412,
	 *               "purpose": "由优酷《这！就是街舞1》年度总冠军-韩宇担任“筑梦计划，街舞公益课堂大使”，2019年4月11日，由共青团中央社会联络，中国舞蹈家协会共同发起的“新时代，艺起来”——全国街舞联盟公益教室进学校（乡村）项目正式启动。",
	 *               "helpCnt": 0,
	 *               "totalInCnt": 6,
	 *               "trendPubKeys": "1,2,3,4",
	 *               "name": "浙里有街舞基金",
	 *               "description": "",
	 *               "detailPic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_whole_description_new_1.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_whole_description_new_2.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_whole_description_new_3.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_whole_description_new_4.png",
	 *               "coverPic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/27_zlyjwjj_cover.jpg,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_1.png,https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/27_zlyjwjj/zlyjwjj_2.png",
	 *               "sharePic": "",
	 *               "orgName": "",
	 *               "orgAddr": "",
	 *               "contact": "",
	 *               "personInCharge": "海边",
	 *               "occupation": "主任",
	 *               "personInChargePic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/csq/fund_person_in_charge/fund27.jpg",
	 *               "creditCardName": "",
	 *               "creditCardId": "",
	 *               "balance": 12000,
	 *               "sumTotalIn": 12000,
	 *               "agentModeStatus": 0,
	 *               "status": 2
	 *               }
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
			CsqFundVo fundVo = csqFundService.fundDetail(managerId, fundId);
			result.setData(fundVo);
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
	 *
	 * @param searchParam 搜索参数
	 * @param isOut       是否支出
	 * @param pageNum     页码
	 * @param pageSize    大小
	 *                    <p>
	 *                    {
	 *                    "resultList": [],
	 *                    "totalCount": 0
	 *                    }
	 * @return
	 */
	@RequestMapping("invoice/list")
	@UrlAuth(withoutPermission = true)
	public AjaxResult invoiceList(String searchParam, Integer isOut, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long managerId = IdUtil.getId();
		try {
			log.info("发票申请列表, searchParam={}, inOut={}, pageNum={}, pageSize={}", searchParam, isOut, pageNum, pageSize);
			QueryResult<CsqUserInvoiceVo> list = csqInvoiceService.list(searchParam, isOut, pageNum, pageSize);
			result.setData(list);
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
	 *
	 * @param invoiceId 发票编号
	 * @param isOut     是否开票0未开1已开
	 *                  <p>
	 *                  {"success":true,"errorCode":"","msg":"","data":""}
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
	 *
	 * @param invoiceId 发票编号
	 *                  <p>
	 *                  {
	 *                  "success": false,
	 *                  "errorCode": "",
	 *                  "msg": "错误的发票编号！",
	 *                  "data": ""
	 *                  }
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
	 *
	 * @param searchParam 搜索参数
	 * @param pageNum     页码
	 * @param pageSize    大小
	 *                    <p>
	 *                    {
	 *                    "resultList": [
	 *                    {
	 *                    "id": 1,
	 *                    "createTime": 1566323688000,
	 *                    "updateTime": 1566452997000,
	 *                    "deletedFlag": false,
	 *                    "extend": "",
	 *                    "createUser": 0,
	 *                    "updateUser": "",
	 *                    "isValid": "1",
	 *                    "userId": 1,
	 *                    "dateString": "",
	 *                    "csqService": "",
	 *                    "type": 1,
	 *                    "title": "欢迎加入丛善桥",
	 *                    "content": "欢迎来到丛善桥！\n在这里，您可以拥有自己的爱心账户，设立专项基金。您从平台捐赠的每一笔善款都将被详细记录，永久保留！马上开启您的公益体验之旅吧~",
	 *                    "serviceId": "",
	 *                    "isRead": 1
	 *                    },
	 *                    {
	 *                    "id": 2,
	 *                    "createTime": 1566323688000,
	 *                    "updateTime": 1566452997000,
	 *                    "deletedFlag": false,
	 *                    "extend": "",
	 *                    "createUser": 0,
	 *                    "updateUser": "",
	 *                    "isValid": "1",
	 *                    "userId": 2,
	 *                    "dateString": "",
	 *                    "csqService": "",
	 *                    "type": 1,
	 *                    "title": "欢迎加入丛善桥",
	 *                    "content": "欢迎来到丛善桥！\n在这里，您可以拥有自己的爱心账户，设立专项基金。您从平台捐赠的每一笔善款都将被详细记录，永久保留！马上开启您的公益体验之旅吧~",
	 *                    "serviceId": "",
	 *                    "isRead": 0
	 *                    }
	 *                    ],
	 *                    "totalCount": 73
	 *                    }
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
	 * @param content    描述
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
	 *
	 * @param userAuthId 实名认证编号
	 *                   <p>
	 *                   {
	 *                   "id": 1,
	 *                   "createTime": 1566326173000,
	 *                   "updateTime": 1566326173000,
	 *                   "deletedFlag": false,
	 *                   "extend": "",
	 *                   "createUser": 0,
	 *                   "updateUser": "",
	 *                   "isValid": "1",
	 *                   "userId": 2431,
	 *                   "type": 0,
	 *                   "cardId": "331022391082906317",
	 *                   "name": "张学友",
	 *                   "phone": "1345123676137",
	 *                   "licensePic": "",
	 *                   "licenseId": "",
	 *                   "status": 1
	 *                   }
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
	 *
	 * @param status   状态
	 * @param pageNum  页码
	 * @param pageSize 分页大小
	 *                 <p>
	 *                 {
	 *                 "resultList": [
	 *                 {
	 *                 "id": 2,
	 *                 "createTime": 1568171217000,
	 *                 "updateTime": 1568171217000,
	 *                 "deletedFlag": false,
	 *                 "extend": "",
	 *                 "createUser": 0,
	 *                 "updateUser": "",
	 *                 "isValid": "1",
	 *                 "userId": 2385,
	 *                 "type": 0,
	 *                 "cardId": "362324196602100614",
	 *                 "name": "楼清宇",
	 *                 "phone": "15925611999",
	 *                 "licensePic": "",
	 *                 "licenseId": "",
	 *                 "status": 1
	 *                 },
	 *                 {
	 *                 "id": 1,
	 *                 "createTime": 1566326173000,
	 *                 "updateTime": 1566326173000,
	 *                 "deletedFlag": false,
	 *                 "extend": "",
	 *                 "createUser": 0,
	 *                 "updateUser": "",
	 *                 "isValid": "1",
	 *                 "userId": 2431,
	 *                 "type": 0,
	 *                 "cardId": "331021199608290617",
	 *                 "name": "许方毅",
	 *                 "phone": "13867655157",
	 *                 "licensePic": "",
	 *                 "licenseId": "",
	 *                 "status": 1
	 *                 }
	 *                 ],
	 *                 "totalCount": 2
	 *                 }
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
	 *
	 * @param status 状态
	 *               <p>
	 *               {
	 *               "success": true,
	 *               "errorCode": "",
	 *               "msg": "",
	 *               "data": 2
	 *               }
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

	/**
	 * 爱心账户充值记录
	 *
	 * @param searchParma   搜索参数(用户昵称)
	 * @param pageNum       页码
	 * @param pageSize      大小
	 * @param isFuzzySearch 是否模糊查询
	 *                      <p>
	 *                      {
	 *                      "totalAmount": 70,
	 *                      "queryResult": {
	 *                      "resultList": [
	 *                      {
	 *                      "id": 5168,
	 *                      "weiboAccount": "",
	 *                      "wechatPubAccount": "",
	 *                      "csqUserAuth": "",
	 *                      "existDayCnt": "",
	 *                      "balanceStatus": "",
	 *                      "gotFund": false,
	 *                      "gotCompanyAccount": false,
	 *                      "accountType": "",
	 *                      "totalDonate": "",
	 *                      "minutesAgo": "",
	 *                      "userAccount": "",
	 *                      "name": "",
	 *                      "userTel": "",
	 *                      "userHeadPortraitPath": "",
	 *                      "sex": "",
	 *                      "remarks": "",
	 *                      "contactPerson": "",
	 *                      "contactNo": "",
	 *                      "availableStatus": "",
	 *                      "trendPubKeys": "",
	 *                      "authStatus": "",
	 *                      "orderId": 1543,
	 *                      "userId": 57,
	 *                      "entityId": 57,
	 *                      "entityType": 2,
	 *                      "fromType": "",
	 *                      "serviceName": "",
	 *                      "date": "",
	 *                      "description": "充值",
	 *                      "inOrOut": 0,
	 *                      "money": 10
	 *                      },
	 *                      {
	 *                      "id": 5171,
	 *                      "weiboAccount": "",
	 *                      "wechatPubAccount": "",
	 *                      "csqUserAuth": "",
	 *                      "existDayCnt": "",
	 *                      "balanceStatus": "",
	 *                      "gotFund": false,
	 *                      "gotCompanyAccount": false,
	 *                      "accountType": "",
	 *                      "totalDonate": "",
	 *                      "minutesAgo": "",
	 *                      "userAccount": "",
	 *                      "name": "",
	 *                      "userTel": "",
	 *                      "userHeadPortraitPath": "",
	 *                      "sex": "",
	 *                      "remarks": "",
	 *                      "contactPerson": "",
	 *                      "contactNo": "",
	 *                      "availableStatus": "",
	 *                      "trendPubKeys": "",
	 *                      "authStatus": "",
	 *                      "orderId": 1544,
	 *                      "userId": 254,
	 *                      "entityId": 254,
	 *                      "entityType": 2,
	 *                      "fromType": "",
	 *                      "serviceName": "",
	 *                      "date": "",
	 *                      "description": "充值",
	 *                      "inOrOut": 0,
	 *                      "money": 10
	 *                      },
	 *                      {
	 *                      "id": 5174,
	 *                      "weiboAccount": "",
	 *                      "wechatPubAccount": "",
	 *                      "csqUserAuth": "",
	 *                      "existDayCnt": "",
	 *                      "balanceStatus": "",
	 *                      "gotFund": false,
	 *                      "gotCompanyAccount": false,
	 *                      "accountType": "",
	 *                      "totalDonate": "",
	 *                      "minutesAgo": "",
	 *                      "userAccount": "",
	 *                      "name": "",
	 *                      "userTel": "",
	 *                      "userHeadPortraitPath": "",
	 *                      "sex": "",
	 *                      "remarks": "",
	 *                      "contactPerson": "",
	 *                      "contactNo": "",
	 *                      "availableStatus": "",
	 *                      "trendPubKeys": "",
	 *                      "authStatus": "",
	 *                      "orderId": 1545,
	 *                      "userId": 80,
	 *                      "entityId": 80,
	 *                      "entityType": 2,
	 *                      "fromType": "",
	 *                      "serviceName": "",
	 *                      "date": "",
	 *                      "description": "充值",
	 *                      "inOrOut": 0,
	 *                      "money": 50
	 *                      }
	 *                      ],
	 *                      "totalCount": 3
	 *                      }
	 *                      }
	 * @return
	 */
	@RequestMapping("account/record/list")
	public AjaxResult accountRecordList(String searchParma, Integer pageNum, Integer pageSize, Boolean isFuzzySearch) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		try {
			log.info("爱心账户充值记录, userIds={}, searchParma={}, pageNum={}, pageSize={}, isFuzzySearch={}", userIds, searchParma, pageNum, pageSize, isFuzzySearch);
			Map<String, Object> watersAndTotal = csqPaymentService.findWatersAndTotal(searchParma, pageNum, pageSize, isFuzzySearch);
			result.setData(watersAndTotal);
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

	/**
	 * 递(提)交打款申请
	 *
	 * @param entityType         实体类型
	 * @param entityId           实体编号
	 * @param money              金额
	 * @param invoicePic         发票图片
	 * @param applyPerson        申请人
	 * @param applyPersonContact 申请人联系方式
	 * @param description        描述
	 *                           <p>
	 *                           {"success":true,"errorCode":"","msg":"","data":""}
	 * @return
	 */
	@RequestMapping("money/apply/add")
	@Consume(CsqMoneyApplyRecordVo.class)
	public AjaxResult addMoneyApply(@Check("==null || ==''") Integer entityType,
									@Check("==null || ==''") Long entityId,
									@Check("==null || ==''") Double money,
									@Check("==null || ==''") String invoicePic,
									@Check("==null || ==''") String applyPerson,
									@Check("==null || ==''") String applyPersonContact,
									String description) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		CsqMoneyApplyRecordVo obj = (CsqMoneyApplyRecordVo) ConsumeHelper.getObj();
		obj.setUserId(userIds);    //即便是代申请，也将代申请人记录写入申请者id
		TCsqMoneyApplyRecord tCsqMoneyApplyRecord = obj.copyTCsqMoneyApplyRecord();
		try {
			log.info("递(提)交打款申请, userIds={}, entityType={}, entityId={}, money={}, invoicePic={}, applyPerson={}, applyPersonContact={}, description={}", userIds, entityType, entityId, money, invoicePic, applyPerson, applyPersonContact, description);
			csqMoneyApplyRecordService.addMoneyApply(tCsqMoneyApplyRecord);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "递(提)交打款申请", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("递(提)交打款申请", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 打款申请列表
	 *
	 * @param searchParam   搜索参数
	 * @param searchType    搜索参数类型
	 * @param status        状态(多选)
	 * @param pageNum       页码
	 * @param pageSize      大小
	 * @param isFuzzySearch 是否采用模糊查询
	 *                      <p>
	 *                      {
	 *                      "resultList": [
	 *                      {
	 *                      "id": 1,
	 *                      "entityId": 42,
	 *                      "entityType": 3,
	 *                      "userId": "",
	 *                      "applyPerson": "王二麻子",
	 *                      "applyPersonContact": "13867655158",
	 *                      "money": 30,
	 *                      "description": "提款，公司急用谢谢",
	 *                      "invoicePic": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png",
	 *                      "status": 1
	 *                      }
	 *                      ],
	 *                      "totalCount": 1
	 *                      }
	 * @return
	 */
	@RequestMapping("money/apply/list")
	@Consume(Page.class)
	public AjaxResult addMoneyApply(String searchParam,
									@Check("==null || ==''") Integer searchType,
									Integer pageNum,
									Integer pageSize,
									Boolean isFuzzySearch,
									@Check("==null || ==''") Integer... status) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		Page page = (Page) ConsumeHelper.getObj();
		try {
			log.info("打款申请列表, searchParam={}, searchType={}, status={}, pageNum={}, pageSize={}, isFuzzySearch={}", userIds, searchParam, searchType, status, pageNum, pageSize, isFuzzySearch);
			QueryResult queryResult = csqMoneyApplyRecordService.moneyApplyList(searchParam, searchType, isFuzzySearch, page, status);
			result.setData(queryResult);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "打款申请列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("打款申请列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 打款申请审核
	 *
	 * @param csqMoneyApplyRecordId 打款申请编号
	 * @param status                状态
	 *                              <p>
	 *                              {
	 *                              "success": true,
	 *                              "errorCode": "",
	 *                              "msg": "",
	 *                              "data": ""
	 *                              }
	 * @return
	 */
	@RequestMapping("money/apply/cert")
	@Consume(CsqMoneyApplyRecordVo.class)
	public AjaxResult certMoneyApply(Long csqMoneyApplyRecordId,
									 @Check("==null || ==''") Integer status) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		CsqMoneyApplyRecordVo obj = (CsqMoneyApplyRecordVo) ConsumeHelper.getObj();
		try {
			log.info("打款申请审核, status={}", userIds, status);
			csqMoneyApplyRecordService.certMoneyApply(userIds, csqMoneyApplyRecordId, status);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "打款申请审核", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("打款申请审核", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 基金/项目捐赠记录
	 *
	 * @param searchParam   搜索参数
	 * @param pageNum       页码
	 * @param pageSize      大小
	 * @param isFuzzySearch 是否模糊查询
	 *                      <p>
	 *                      {"success":true,"errorCode":"","msg":"","data":""}
	 * @return
	 */
	@RequestMapping("donate/record/list")
	@Consume(Page.class)
	public AjaxResult donateRecordList(String searchParam, Integer pageNum, Integer pageSize, boolean isFuzzySearch) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		Page page = (Page) ConsumeHelper.getObj();
		try {
			log.info("基金/项目捐赠记录, searchParam={}, pageNum={}, pageSize={}, isFuzzySearch={}", searchParam, pageNum, pageSize, isFuzzySearch);
			HashMap<String, Object> map = csqPaymentService.donateRecordList(userIds, searchParam, page, isFuzzySearch);
			result.setData(map);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "基金/项目捐赠记录", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("基金/项目捐赠记录", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 数据BI(收支统计)
	 *
	 * @param searchParam   搜索参数
	 * @param startDate     开始日期,eg.1999-01-01
	 * @param endDate       结束日期
	 * @param pageNum       页码
	 * @param pageSize      大小
	 * @param isServiceOnly 是否为仅服务和基金
	 * @return
	 */
	@RequestMapping("statistics")
	public Object platformDataStatistics(String searchParam, String startDate, String endDate, Integer pageNum, Integer pageSize, Boolean isFuzzySearch, Boolean isServiceOnly) {
		AjaxResult result = new AjaxResult();
		Long userIds = IdUtil.getId();
		Page page = (Page) ConsumeHelper.getObj();
		try {
			log.info("数据BI(收支统计), searchParam={}, startDate={}, endDate={}, pageNum={}, pageSize={}, isFuzzySearch={}, isServiceOnly={}", searchParam, startDate, endDate, pageNum, pageSize, isFuzzySearch, isServiceOnly);
			csqPaymentService.platformDataStatistics(userIds, searchParam, startDate, endDate, pageNum, pageSize, isFuzzySearch, isServiceOnly);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "数据BI(收支统计)", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("数据BI(收支统计)", e);
			result.setSuccess(false);
		}
		return result;
	}

}
