package com.e_commerce.miscroservice.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.AliPayCallBackEntity;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.entity.service.Token;
import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.entity.task.LiveData;
import com.e_commerce.miscroservice.commons.entity.task.MemberAudienceData;
import com.e_commerce.miscroservice.commons.entity.user.*;
import com.e_commerce.miscroservice.commons.enums.pay.AliPayConfig;
import com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums;
import com.e_commerce.miscroservice.commons.enums.user.StoreAuditStatusEnum;
import com.e_commerce.miscroservice.commons.enums.user.UserPrefixEnums;
import com.e_commerce.miscroservice.commons.enums.user.UserType;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.HttpUtils;
import com.e_commerce.miscroservice.commons.utils.SmsUtils;
import com.e_commerce.miscroservice.commons.utils.pay.*;
import com.e_commerce.miscroservice.commons.utils.wx.AliPrepayUtil;
import com.e_commerce.miscroservice.user.dao.*;
import com.e_commerce.miscroservice.user.entity.ShopMember;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.rpc.AuthRpcService;
import com.e_commerce.miscroservice.user.po.UserPO;
import com.e_commerce.miscroservice.user.rpc.ProxyPayRpcService;
import com.e_commerce.miscroservice.user.rpc.TaskRpcService;
import com.e_commerce.miscroservice.user.rpc.YjjOrderRpcService;
import com.e_commerce.miscroservice.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

import static com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums.LIVE_ROOM_MEMBER_INTO;


/**
 * 用户的service 实现层
 */
@Service
public class UserServiceImpl implements UserService {
    private Log logger = Log.getInstance(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthRpcService authRpcService;

    @Resource
    private WhitePhoneDao whitePhoneDao;
    @Resource
    private YjjStoreBusinessAccountDao yjjStoreBusinessAccountDao;
    @Resource
    private YjjStoreBusinessAccountLogDao yjjStoreBusinessAccountLogDao;

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;
    @Autowired
    private ProxyPayRpcService proxyPayRpcService;

    @Autowired
    private YjjOrderRpcService yjjOrderRpcService;
    @Autowired
    private TaskRpcService taskRpcService;

    @Autowired
    private ShopMemberDao shopMemberDao;

    @Autowired
    private LiveDao liveDao;
    /**
     * app版本号
     */
    public static final String APP_VERSION_372 = "372";
    private static final int FIRST_LOGIN = 1;

    /**
     * 根据手机号码获取用户信息
     *
     * @param userPhone
     * @return
     */
    @Override
    public UserPO getUserByUserPhone(String userPhone) {


        return this.userDao.getUserByUserPhone(userPhone);
    }

    /**
     * 注册
     *
     * @param platform
     * @param name     用户名
     * @param pass     密码
     * @param uid      设备uid
     * @param isSingle 是否单点登陆 true or false
     * @param response
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response register(Integer platform, String name, String pass, String uid, Boolean isSingle, HttpServletResponse response) {
//        logger.info("注册name={},pass={},uid={},isSingle={}", name, pass, uid, isSingle);
//        if (StringUtils.isAnyEmpty(name, pass, uid)) {
//            logger.warn("参数为空");
//            return Response.errorMsg("参数为空");
//        }
//        //解密
////        String deCodePass = AesUtil.decode(pass,uid);
//        //double MD5
////        String doubleMd5Pass = Md5Util.md5(Md5Util.md5(deCodePass));
//
//        String nameU = null;
//        if (platform.equals(UserPrefixEnums.SYSTEM_PLATFORM_APP.getCode())) {
//            nameU = UserPrefixEnums.SYSTEM_PLATFORM_APP.getPlatform() + name;
//            // TODO: 2018/11/6 注入数据库
//
//        }
//        Token token = authRpcService.reg(nameU, pass, uid, isSingle);
//        if (token == null) {
//            return Response.errorMsg("注册失败");
//        }
//        response.addHeader("token", token.getToken());
//        return Response.success(token.getMsg());
        return null;
    }

    /**
     * 登录
     *
     * @param platform
     * @param name     用户名
     * @param pass     用户密码
     * @param uid      用户手机标识
     * @return
     */
    @Override
    public Response load(Integer platform, String name, String pass, String uid, HttpServletRequest request, HttpServletResponse response) {
        logger.info("登陆name={},pass={},uid={}", name, pass, uid);
        if (StringUtils.isAnyEmpty(name, pass, uid)) {
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        String nameU = null;
        if (platform.equals(UserPrefixEnums.SYSTEM_PLATFORM_APP.getCode())) {
            nameU = UserPrefixEnums.SYSTEM_PLATFORM_APP.getPlatform() + name;
        }
        Token token = authRpcService.load(nameU, pass, uid);
        if (token == null) {
            return Response.errorMsg("登陆失败");
        }

        response.addHeader("token", token.getToken());
        return Response.success(token.getMsg());
    }

    /**
     * 验证码登录APP
     *
     * @param phone      手机号
     * @param verifyCode 验证码
     * @param sendType   验证码类型
     * @param request
     * @param response
     * @param ip
     * @param client
     * @return
     */
    @Override
    public Response verifyCommit(String phone, String verifyCode, String sendType, HttpServletRequest request, HttpServletResponse response, String ip, ClientPlatform client) {
        if (StringUtils.isAnyEmpty(phone,verifyCode,sendType)&&client!=null){
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        if (whitePhoneDao.getWhitePhone(phone) == 0) {
            // 如果手机号不在白名单
            boolean phoneVerifySuccess = phoneVerify(verifyCode, sendType, phone);
            if (!phoneVerifySuccess) {
                return Response.errorMsg("短信验证码错误");
            }

        }
      return storeBusinessLogin(phone,request,response,ip,client);
    }

    /**
     * storeBusinessLogin用户登陆
     * @param phone
     * @param request
     * @param response
     * @param ip
     * @param client
     * @return
     */
    @Override
    public Response storeBusinessLogin(String phone, HttpServletRequest request, HttpServletResponse response, String ip, ClientPlatform client){
        String nameU = null;
        if (client.isIphone()||client.isAndroid()) {
            nameU = UserPrefixEnums.SYSTEM_PLATFORM_APP.getPlatform() + phone;
        }
        String doubleMd5Pass = Md5Util.md5(Md5Util.md5(phone));
        HashMap<String, Object> result = new HashMap<>(2);
        result.put("isFirstLogin", false);
        //获取用户信息, 首次登录则生成一条新的用户信息
        StoreBusiness user = userDao.getStoreBusinessByPhone(phone);
//        设备id
        String uniqueId = request.getHeader("uid");
        if (StringUtils.isEmpty(uniqueId)){
            logger.warn("设备号为空");
            return Response.errorMsg("设备号为空");
        }
        Boolean flag = Boolean.FALSE;

        if (user == null) {
            user = buildDefaultStore();
            user.setPhoneNumber(phone);
            userDao.addStoreBusiness(user);
            StoreBusiness storeBusiness = userDao.getStoreBusinessByPhone(phone);
            Token token = authRpcService.reg(nameU, doubleMd5Pass, String.valueOf(storeBusiness.getId()),uniqueId, flag);
            if (token == null) {
                return Response.errorMsg("注册失败");
            }
            user.setBusinessNumber(user.getId().longValue() + 800000000);
            userDao.updateStoreBusiness(user);
            result.put("isFirstLogin", true);
            result.put("coupon", true);
            result.put("storeId", user.getId());
            //发优惠券
            // sendFirstLoginCoupon(user);
        }



        //登录
        loginUser(user, request, response, ip, client);
        result.put("tip", "俞姐姐门店宝批发精品女装，欢迎您");

        Token token = storeBusinessToken(uniqueId,user);
        if (token==null){
            logger.warn("登录认证服务器失败");
            return Response.errorMsg("登录认证服务器失败");
        }
        if (token!=null&&token.getToken()==null){
            logger.warn("认证失败={}",token.getMsg());
            return Response.errorMsg(token.getMsg());

        }
        logger.info("token信息 = {}，",token.getToken());
        response.addHeader("token", token.getToken());

//        userLoginLogSwitcherV372(request, 1, phone);
        return Response.success(result);
    }
    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @Override
    public void sendMsg(String phone) {
        SmsUtils.sendCode(phone);
    }

    /**
     * 微信登陆
     * @param openId
     * @param openuid
     * @param accessToken
     * @param ip
     * @param client
     * @param request
     * @param response
     * @return
     */
    @Override
    public Response wxLogin(String openId, String openuid, String accessToken, String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isAnyEmpty(openId,openuid,accessToken)){
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        logger.info("微信登陆获取用户信息 openId={},openuid={},accessToken={}",openId,openuid,accessToken);
        Map<String, Object> result = new HashMap<>(4);
        result.put("isFirstLogin", true);
        //        设备id
        String uniqueId = request.getHeader("uid");
        Enumeration<String>  headera = request.getHeaderNames();
        if (StringUtils.isEmpty(uniqueId)){
            logger.warn("设备号为空");
            return Response.errorMsg("设备号为空");
        }
        //用户微信信息
        Map<String, String> wxRequestParams = new HashMap<>(2);
        wxRequestParams.put("access_token", accessToken);
        wxRequestParams.put("openid", openId);
        String baseUri = "https://api.weixin.qq.com/sns/userinfo";
        String httpString =  HttpUtils.sendPost(baseUri,wxRequestParams);
        logger.info("HTTP_返回信息={}",httpString);
        JSONObject httpJson = JSONObject.parseObject(httpString);
        if (httpJson.get("errcode")!=null||!httpJson.get("unionid").equals(openuid)){
            logger.warn("请求微信获取用户信息失败");
            result.put("needBindPhone", true);
            result.put("phone", null);
            result.put("msg", "微信认证失败");
            return Response.success(result);
        }
        WeixinLoginData weixinLoginData = JSONObject.toJavaObject((JSON) JSON.parse(httpString), WeixinLoginData.class);
        //平台用户信息
        StoreBusiness storeBusiness = userDao.getStoreBusinessByWeixinId(weixinLoginData.getUnionid());
        if (storeBusiness == null || StringUtils.isBlank(storeBusiness.getPhoneNumber())) {
            logger.warn("没有用户信息, 请绑定手机号");
            //没有用户信息, 请绑定手机号
            result.put("needBindPhone", true);
            result.put("phone", null);
            result.put("msg", "请绑定手机号");
            return Response.success(result);
        }

        //用户需要更新微信信息
        boolean needUpdWeiXinInfo = WeiXinInfoIsChange(weixinLoginData, storeBusiness);
        if (needUpdWeiXinInfo) {
            logger.info("更新用户微信信息");
            userDao.updateStoreBusiness(storeBusiness);
        }

        //登录
        Map<String, Object> data = loginUser(storeBusiness, request, response, ip, client);
        data.put("isFirstLogin", false);
        data.put("tip", "俞姐姐门店宝批发精品女装，价格仅对“门店采购负责人”开放，请理解...");
        data.put("needBindPhone", false);
        data.put("phone", storeBusiness.getPhoneNumber());
        result.put("msg", "success");

        Token token = storeBusinessToken(uniqueId,storeBusiness);
        if (token==null){
            logger.warn("登录认证服务器失败");
            return Response.errorMsg("登录认证服务器失败");
        }
        if (token!=null&&token.getToken()==null){
            logger.warn("认证失败={}",token.getMsg());
            return Response.errorMsg(token.getMsg());

        }
        response.addHeader("token", token.getToken());

        return Response.success(data);
    }

    /**
     * 微信 绑定手机号
     *
     * @param openId
     * @param openuid
     * @param accessToken
     * @param phone
     * @param ip
     * @param client
     * @param request
     * @param response
     * @return
     */
    @Override
    public Response phoneBindWeixinCommit(String openId, String openuid, String accessToken, String phone, String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {
        //        设备id
        String uniqueId = request.getHeader("uid");
        if (StringUtils.isEmpty(uniqueId)){
            logger.warn("设备号为空");
            return Response.errorMsg("设备号为空");
        }
        // 1、获取用户信息
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", accessToken);
        map.put("openid", openId);
        String baseUri = "https://api.weixin.qq.com/sns/userinfo";
        String httpString =  HttpUtils.sendPost(baseUri,map);
        logger.info("HTTP_返回信息={}",httpString);
        JSONObject httpJson = JSONObject.parseObject(httpString);
        if (httpJson.get("errcode")!=null||!httpJson.get("unionid").equals(openuid)){
            logger.warn("请求微信获取用户信息失败");
//            result.put("needBindPhone", true);
//            result.put("phone", null);
//            result.put("msg", "微信认证失败");
            return Response.errorMsg("请求微信获取用户信息失败");
        }


        // 2.获取应用用户信息
        WeixinLoginData weixinLoginData = JSONObject.toJavaObject((JSON) JSON.parse(httpString), WeixinLoginData.class);
        StoreBusiness storeBusiness = userDao.getStoreBusinessByWeixinId(weixinLoginData.getUnionid());
        StoreBusiness storeBusinessByPhone = userDao.getStoreBusinessByPhone(phone);
        String weiXinNickName = weixinLoginData.getNickname();
        String nextStep = "fillData";

        if (storeBusiness != null&&storeBusiness.getPhoneNumber()!=null) {

            logger.warn("Weixin 已绑定其它手机，无法重复绑定");
            return Response.errorMsg("Weixin 已绑定其它手机，无法重复绑定");
        } else if (storeBusinessByPhone != null) {
            if (storeBusinessByPhone.getBindWeixinId() != null
                    && storeBusinessByPhone.getBindWeixinId().trim().length() > 0) {
                logger.warn("手机号已绑定其它微信，无法重复绑定");
                return Response.errorMsg("手机号已绑定其它微信，无法重复绑定");
            }
//            更新老的微信信息
            int count = userDao.oldUserBindWeixin(storeBusinessByPhone.getId(),
                    weixinLoginData.getUnionid(), weixinLoginData.getNickname(), weixinLoginData.getHeadimgurl());
            if (count == 1) {
                storeBusiness = userDao.getStoreBusinessByWeixinId(weixinLoginData.getUnionid());
                loginUser(storeBusiness, request, response, ip, client);
                nextStep = "index";
                data.put("needFillData", "NO");
            }

        } else {
            //添加微信信息绑定手机号
            userDao.addUserWeixinAndPhone(phone, UserType.PHONE, client, weixinLoginData.getUnionid(),
                    weiXinNickName, weixinLoginData.getHeadimgurl());
            storeBusiness = userDao.getStoreBusinessByWeixinId(weixinLoginData.getUnionid());
            loginUser(storeBusiness, request, response, ip, client);
            data.put("needFillData", "YES");
        }
        storeBusiness.setPhoneNumber(phone);
        Token token = storeBusinessToken(uniqueId,storeBusiness);
        if (token==null){
            logger.warn("登录认证服务器失败");
            return Response.errorMsg("登录认证服务器失败");
        }

        if (token!=null&&token.getToken()==null){
            logger.warn("认证失败={}",token.getMsg());
            return Response.errorMsg(token.getMsg());

        }
        response.addHeader("token", token.getToken());
//        String nameU = null;
//        if (client.isIphone()||client.isAndroid()) {
//            nameU = UserPrefixEnums.SYSTEM_PLATFORM_APP.getPlatform() + storeBusiness.getPhoneNumber();
//        }
//        String doubleMd5Pass = Md5Util.md5(Md5Util.md5(storeBusiness.getPhoneNumber()));
//        Boolean flag = Boolean.FALSE;
//        //        设备id
//        String uniqueId = request.getHeader("uniqueId");
//        if (StringUtils.isEmpty(uniqueId)){
//            logger.warn("设备号为空");
//            return Response.errorMsg("设备号为空");
//        }
//        if (storeBusiness.getAuthId()==null||storeBusiness.getAuthId()==0){
//            logger.info("老用户 认证系统导入");
//            Token token = authRpcService.reg(nameU, doubleMd5Pass, uniqueId, flag);
//            logger.info("认证系统返回token={}",token);
//            if (token == null||token.getUserId()==null) {
//                return Response.errorMsg("注册失败");
//            }
//            storeBusiness.setAuthId(token.getUserId());
//            userDao.updateStoreBusiness(storeBusiness);
//        }
        data.put("nextStep", nextStep);

        // 2.4版本临时添加，之后版本不用，可删除可保留
        data.put("phoneNumber", storeBusiness.getPhoneNumber());

        return Response.success(data);
    }
    /**
     * 充值
     * @param money 金额
     * @param type 充值方式 0 支付宝  1 微信
     * @param id
     * @param openid
     * @param ip
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response rechargeMoney(Double money, Integer type, Integer id, String openid, String ip)
    {
        if (money==null||type==null){
            logger.warn("参数为空money={},type = {}",money,type);
            return Response.errorMsg("参数为空");
        }

        String orderNo = UUIdUtil.generateOrderNo();
        String pay = null;
        SortedMap<String, Object> sortedMap=null;
        String remarks = null;
        if (type==0){
            logger.info("支付宝发起充值金额={}",money);
            remarks="支付宝充值";
            pay = AliPrepayUtil.prepay(money, AliPayConfig.partner,AliPayConfig.sellerEmail,orderNo,remarks,remarks
                    ,AliPayConfig.notifyUrlRecharge
                    ,null,AliPayConfig.rsaPrivateKey,AliPayConfig.paymentType,AliPayConfig.inputCharset);
        }else if (type==1){
            logger.info("微信发起充值金额={}",money);
            remarks="微信充值";
            sortedMap = WxCommonUtil.genPayData4AppNew(remarks,orderNo,new BigDecimal(money), ip );
            logger.info ("预支付 sortedMap={}", sortedMap);
//            sortedMap= WxCommonUtil.getBrandWCPayRequest(preOrderResult);
        }
        YjjStoreBusinessAccount yjjStoreBusinessAccount = yjjStoreBusinessAccountDao.findOne(Long.valueOf(id));
        YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
        yjjStoreBusinessAccountLog.setInOutType(0);
        yjjStoreBusinessAccountLog.setOrderNo(orderNo);
        yjjStoreBusinessAccountLog.setRemarks(remarks);
        yjjStoreBusinessAccountLog.setType(1);
        yjjStoreBusinessAccountLog.setOperMoney(money);
        yjjStoreBusinessAccountLog.setUserId(Long.valueOf(id));
        yjjStoreBusinessAccountLog.setStatusType(1);//冻结
        Double remainderMoney=0d;
        if (yjjStoreBusinessAccount!=null){
            remainderMoney=yjjStoreBusinessAccount.getCountMoney();
        }
        yjjStoreBusinessAccountLog.setRemainderMoney(remainderMoney);
//        账户流水
       Integer isAddLog =  yjjStoreBusinessAccountLogDao.addOne(yjjStoreBusinessAccountLog);
       if (isAddLog==null){
           logger.warn("流水生成失败={}",id);
           return Response.errorMsg("流水生成失败");
       }
       if (type==0){
           return Response.success(pay);
       }else if (type==1){
           return Response.success(sortedMap);
       }
       return Response.errorMsg("操作失败");
    }
    /**
     * 充值 回调
     * @param notify 回调内容
     * @param client
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String callbackRechargeAliPay(AliPayCallBackEntity notify, ClientPlatform client) {
       logger.info("回调处理notify={}",notify);
        if(notify == null){
            return "";
        }
//        Map<String, String> notify = MapTrunPojo.object2StringMap(aliPayCallBackEntity);
        String trade_status = notify.getTrade_status();
        //3个月回调空处理
        if (trade_status != null && trade_status.equals("TRADE_FINISHED")){
            return "";
        }
        String out_trade_no = notify.getOut_trade_no();
        logger.info("回调处理-out_trade_no:" + out_trade_no);
       if (out_trade_no==null){
           logger.warn("订单号不存在");
           return "fail";
       }
        //包含冻结状态
        YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = yjjStoreBusinessAccountLogDao.findOneByOrderNo(out_trade_no);
        if (yjjStoreBusinessAccountLog == null) {
            logger.warn("流水不存在={}");
            return "fail";
        }
        if (!Double.valueOf(notify.getTotal_fee()).equals(yjjStoreBusinessAccountLog.getOperMoney())){
            logger.warn("充值失败金额与操作金额不符合");
            return "fail";
        }

        Boolean isTrue = MobileAlipayNotify.verify(notify);
        if (isTrue){
            // 交易状态
            trade_status = notify.getTrade_status();
            logger.info("1-trade_status:" + trade_status);
            if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                logger.info("充值成功");
                String doR = doRecharge(yjjStoreBusinessAccountLog);
                return doR;
            }

        }else {
            logger.error("回调验证失败, out_trade_no:{}, trade_no:{}", notify.getOut_trade_no(), notify.getTrade_no());

        }
        return "fail";
    }

    /**
     * 用户充值处理
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    public String doRecharge ( YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog ){
        YjjStoreBusinessAccount yjjStoreBusinessAccount = yjjStoreBusinessAccountDao.findOne(yjjStoreBusinessAccountLog.getUserId());
        YjjStoreBusinessAccount account = new YjjStoreBusinessAccount();
        account.setUserId(yjjStoreBusinessAccountLog.getUserId());
        if(yjjStoreBusinessAccount==null){
            logger.info("创建新的账户id={}",account.getUserId());
            Integer isAdd = yjjStoreBusinessAccountDao.addOne(account);
            if (isAdd<=0){
                logger.warn("添加失败={}",account.getUserId());
                return "fail";
            }
        }
        Integer isUp = yjjStoreBusinessAccountDao.upUseMoney(yjjStoreBusinessAccountLog.getUserId(),yjjStoreBusinessAccountLog.getOperMoney());
        if (isUp<=0) {
            logger.warn("金额更新失败={}", yjjStoreBusinessAccountLog.getUserId());
            return "fail";
        }
        yjjStoreBusinessAccountLog.setStatusType(0);
        yjjStoreBusinessAccountLogDao.upOne(yjjStoreBusinessAccountLog);
        return "success";
    }
    @Override
    public String callbackRechargeWxPay(HttpServletRequest request, ClientPlatform client) {
        Map<String,String>  map = null;
        try {
             map = WeChatPay.doParseRquest(request);
            logger.info ("微信充值回调 result={}", map);

        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isSign = Sign.checkIsSignValidFromResponseString(map,WxPayStaticValue.KEY);
        if (!isSign){
            logger.warn("验签失败");
            return null;
        }
        boolean isPaid = "SUCCESS".equals (map.get("return_code"));
        YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = yjjStoreBusinessAccountLogDao.findOneByOrderNo(map.get("out_trade_no"));
        if (isPaid) {
            if (yjjStoreBusinessAccountLog==null){
                logger.warn("充值订单不存在");
                return "fail";
            }
            logger.info("微信充值成功");
            return doRecharge(yjjStoreBusinessAccountLog);
        }

        return "fail";
    }

    @Override
    public Response getUserAccountList(Integer id, Integer page, Integer inOut, Integer type, Integer typeAdaptor) {
        logger.info("账户流水 查询账户id={}，page={}，inOut={}，type={}，typeAdaptor={}",id,page,inOut,type,typeAdaptor);
        if (page==null||page<0){
            logger.warn("页码错误");
            return Response.errorMsg("页码错误");
        }
        List<YjjStoreBusinessAccountLog> list = yjjStoreBusinessAccountLogDao.findAll(Long.valueOf(id),page,inOut,type, typeAdaptor);
        return Response.success(list);
    }

    /**
     * 核对余额
     * @param money
     * @param id
     * @return
     */
    @Override
    public Response checkMoney(Double money, Integer id) {
        YjjStoreBusinessAccount yjjStoreBusinessAccount = yjjStoreBusinessAccountDao.findOne(Long.valueOf(id));
        if (yjjStoreBusinessAccount.getUseMoney()<money){
            logger.warn("id={}余额不足money={}",id,money);
            return Response.errorMsg("余额不足");
        }
        return Response.success();
    }

    @Override
    public Response userPay(String orderNo, Integer id, String version) {
//        logger.info("id={}余额支付订单号orderNo={},version={}",id,orderNo,version);
//        YjjOrder order = yjjOrderRpcService.findYjjOrderByOrderNo(orderNo);
//        if (order==null){
//            logger.warn("订单不存在");
//            return Response.errorMsg("订单不存在");
//        }
//        if (order.getOrderStatus() == OrderStatus.UNPAID.getIntValue()) {
//            long time = System.currentTimeMillis();
//            PaymentType paymentType = PaymentType.ALIPAY_SDK;
//
////                        if(order.getOrderType() == OrderType.SEND_BACK){
////                         // 如果是回寄订单，则将状态更新为待审核状态
////                            orderService.updateOrderPayStatus(order, trade_no, paymentType, OrderStatus.UNCHECK,
////                                OrderStatus.UNPAID, time);
////                        }else{
//            // 如果是普通直购订单，则将状态更新为已支付状态
//            logger.info("1-修改状态:");
//            orderService.updateOrderPayStatus(order, trade_no, paymentType, OrderStatus.PAID,OrderStatus.UNPAID, time ,version);
//            logger.info("1-修改状态:成功");
//            //支付成功会的回调
//            shopProductService.updateTabTypeAfterPaySuccess(out_trade_no);
//
//            logger.info("1-店家精选:成功");
//            //商家优惠券已使用,做统计
//            long orderNo = order.getOrderNo();
//            Wrapper<StoreCouponUseLogNew> wrapper = new EntityWrapper<StoreCouponUseLogNew>();
//            wrapper.eq("OrderNo", orderNo)
//                    .eq("Status", 0)
//                    .ne("supplier_id", 0);
//
//            List<ShopStoreOrderItem> orderItemList = orderService.getOrderNewItemsOnlyByOrderNO(order.getOrderNo());
//            order.setOrderItems(orderItemList);
//            logger.info("1-修改商品销量:开始");
//            if (orderHandlers != null) {
//                for (OrderHandler handler : orderHandlers) {
//                    handler.updateSaleCount(order, "");
//                }
//                logger.info("1-修改商品销量:成功");
//            }
//            //倘若是限购活动商品，就添加限购活动销量
//            if(order.getRestriction_activity_product_id() > 0){
//                RestrictionActivityProduct restrictionActivityProduct = restrictionActivityProductMapper.selectById(order.getRestriction_activity_product_id());
//                int saleCount = restrictionActivityProduct.getSaleCount();
//                saleCount = saleCount+order.getTotalBuyCount();
//                RestrictionActivityProduct restrictionActivityProduct2 = new RestrictionActivityProduct();
//                restrictionActivityProduct2.setId(order.getRestriction_activity_product_id());
//                restrictionActivityProduct2.setSaleCount(saleCount);
//                restrictionActivityProductMapper.updateById(restrictionActivityProduct2);
//            }
//            //发送短信[俞姐姐门店宝] ***供应商，您好！您有新订单，请及时关注并确保在24小时之后完成发货。通知供应商发货
//            StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
//            long supplierId = storeOrderNew.getSupplierId();
//            UserNew userNew = userNewMapper.selectById(supplierId);
//            sendText(userNew.getPhone(),userNew.getBusinessName(),templateId);
//            //如果是售后订单，同步更新售后表信息
//            if(order.getOrderType() == OrderType.AFTERSALE.getIntValue()){
//                int count = afterSaleService.updateServiceOrderPaid(order.getOrderNo());
//                System.out.println("afterSaleService update num:" + count);
//            }
////                        }
//            // 给订单订单表加一个字段
//            upSendCoupon(orderNo);
//
//        }
//        // String payinfoAfter = makePaymentFromAlipay(params);
//        // logger.info(payinfoAfter);
        return null;
    }

    @Override
    public Response showRechargeMoney(Integer id) {
        logger.info("id={}充值后显示余额",id);
        YjjStoreBusinessAccountLog yj = yjjStoreBusinessAccountLogDao.findLimitTimeOne(id);
        logger.info("充值日志={}",yj);
//        if (yj==null||yj.getStatusType()==1){
//            return Response.success();
//        }
        return Response.success(yj);
    }

    @Override
    public Response showMoney(Integer id) {
        YjjStoreBusinessAccount yjjStoreBusinessAccount = yjjStoreBusinessAccountDao.findOne(Long.valueOf(id));
        Double money = 0d;
        if (yjjStoreBusinessAccount!=null){
            money=yjjStoreBusinessAccount.getUseMoney();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("countMoney",money);
        return Response.success(map);
    }

    @Override
    public Response userAccountDetail(Long id, Integer userId) {
        YjjStoreBusinessAccountLog req = new YjjStoreBusinessAccountLog();
        req.setId(id);
        req.setUserId(Long.valueOf(userId));
        YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = yjjStoreBusinessAccountLogDao.findOne(req);
        return Response.success(yjjStoreBusinessAccountLog);
    }

    /**
     * 设置Redis用户id
     * @param key
     * @param value
     * @return
     */
    @Override
    public Response redisSetValue(String key, String value) {
        if (StringUtils.isAnyEmpty(key,value)){
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        logger.info("设置Redis 用户=key{},value={}",key,value);
        String oValue = userStrHashRedisTemplate.get(key);
        if (oValue==null){
            userStrHashRedisTemplate.set(key,value);
        }
        return Response.success();
    }

    /**
     * 微信绑定手机号
     * @param verifyCode
     * @param sendType
     * @param openId
     * @param openuid
     * @param accessToken
     * @param phone
     * @param ip
     * @param client
     * @param request
     * @param response
     * @return
     */
    @Override
    public Response bindWeixinOauth(String verifyCode, String sendType, String openId, String openuid, String accessToken, String phone, String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {
        {
            //        设备id
            String uniqueId = request.getHeader("uid");
            if (StringUtils.isEmpty(uniqueId)){
                logger.warn("设备号为空");
                return Response.errorMsg("设备号为空");
            }
            HashMap<String, Object> result = new HashMap<>(4);
            result.put("isFirstLogin", true);

            // 手机验证
            if (whitePhoneDao.getWhitePhone(phone) == 0) {
                // 如果手机号不在白名单
                boolean phoneVerifySuccess = phoneVerify(verifyCode, sendType, phone);
                if (! phoneVerifySuccess) {
                    logger.warn("短信验证码错误");
//                    result.put("needBindPhone", true);
//                    result.put("phone", phone);
                    return Response.errorMsg("短信验证码错误");
                }
            }


            // 获取微信信息
            Map<String, Object> wxRequestParams = new HashMap<>();
            wxRequestParams.put("access_token", accessToken);
            wxRequestParams.put("openid", openId);

            // 1、获取用户信息
            Map<String, Object> data = new HashMap<String, Object>();
            Map<String, String> map = new HashMap<String, String>();
            map.put("access_token", accessToken);
            map.put("openid", openId);
            String baseUri = "https://api.weixin.qq.com/sns/userinfo";
            String httpString =  HttpUtils.sendPost(baseUri,map);
            logger.info("HTTP_返回信息={}",httpString);
            JSONObject httpJson = JSONObject.parseObject(httpString);
            if (httpJson.get("errcode")!=null||!httpJson.get("unionid").equals(openuid)){
                logger.warn("请求微信获取用户信息失败");
//            result.put("needBindPhone", true);
//            result.put("phone", null);
//            result.put("msg", "微信认证失败");
                return Response.errorMsg("请求微信获取用户信息失败");
            }


            // 2.获取应用用户信息
            WeixinLoginData weixinLoginData = JSONObject.toJavaObject((JSON) JSON.parse(httpString), WeixinLoginData.class);
            StoreBusiness storeBusiness = userDao.getStoreBusinessByWeixinId(weixinLoginData.getUnionid());
            StoreBusiness storeBusinessByPhone = userDao.getStoreBusinessByPhone(phone);
            String weiXinNickName = weixinLoginData.getNickname();
            String nextStep = "fillData";

            if (storeBusiness != null&&storeBusiness.getPhoneNumber()!=null) {

                logger.warn("Weixin 已绑定其它手机，无法重复绑定");
                return Response.errorMsg("Weixin 已绑定其它手机，无法重复绑定");
            } else if (storeBusinessByPhone != null) {
                if (storeBusinessByPhone.getBindWeixinId() != null
                        && storeBusinessByPhone.getBindWeixinId().trim().length() > 0) {
                    logger.warn("手机号已绑定其它微信，无法重复绑定");
                    return Response.errorMsg("手机号已绑定其它微信，无法重复绑定");
                }
//            更新老的微信信息
                int count = userDao.oldUserBindWeixin(storeBusinessByPhone.getId(),
                        weixinLoginData.getUnionid(), weixinLoginData.getNickname(), weixinLoginData.getHeadimgurl());
                if (count == 1) {
                    storeBusiness = userDao.getStoreBusinessByWeixinId(weixinLoginData.getUnionid());
                    loginUser(storeBusiness, request, response, ip, client);
                    nextStep = "index";
                    data.put("needFillData", "NO");
                }

            } else {
                //添加微信信息绑定手机号
                userDao.addUserWeixinAndPhone(phone, UserType.PHONE, client, weixinLoginData.getUnionid(),
                        weiXinNickName, weixinLoginData.getHeadimgurl());
                storeBusiness = userDao.getStoreBusinessByWeixinId(weixinLoginData.getUnionid());
                loginUser(storeBusiness, request, response, ip, client);
                data.put("needFillData", "YES");
            }
            storeBusiness.setPhoneNumber(phone);
            Token token = storeBusinessToken(uniqueId,storeBusiness);
            if (token==null){
                logger.warn("登录认证服务器失败");
                return Response.errorMsg("登录认证服务器失败");
            }

            if (token!=null&&token.getToken()==null){
                logger.warn("认证失败={}",token.getMsg());
                return Response.errorMsg(token.getMsg());

            }
            response.addHeader("token", token.getToken());
            data.put("nextStep", nextStep);

            // 2.4版本临时添加，之后版本不用，可删除可保留
            data.put("phoneNumber", storeBusiness.getPhoneNumber());

            return Response.success(data);
        }
    }

    /**
     * 发优惠券
     * @param storeId
     * @return
     */
    @Override
    public Response sendCouponByOldSys(Long storeId)
    {
        Map<String,String> map  = new HashMap<>();
        map.put("storeId",String.valueOf(storeId));
       String str =  HttpUtils.sendPost("http://local.nessary.top:8081/jstore/store/login/send/coupon",map);
        System.out.println(str);
        return Response.success();
    }

    @Override
    public void test(Long id, Long memberId, Long roomId, Integer code, Integer moduleType, Long storeId, Long distributorId) {
        DataBase dataBase = new DataBase();
        dataBase.setId(id);
        dataBase.setMemberId(memberId);
        dataBase.setRoomId(roomId);
        dataBase.setCode(code);
        dataBase.setModuleType(moduleType);
        dataBase.setStoreId(storeId);
        dataBase.setDistributorId(distributorId);

        dataBase.setLiveStatus(TaskTypeEnums.LIVE_COMMON_SHOP.getKey());
        if (LIVE_ROOM_MEMBER_INTO.isMe(dataBase.getCode())){
            MemberAudienceData memberAudienceData = new MemberAudienceData();
            memberAudienceData.setId(1);
            memberAudienceData.setMemberId(memberId);
            dataBase.setObj(memberAudienceData);
        }else {
            LiveData liveData = new LiveData();
            liveData.setCode(1);
            liveData.setStatus(1);
            liveData.setStoreId(storeId);
            liveData.setRoomId(roomId);
            dataBase.setObj(liveData);
        }

        Response  sr = taskRpcService.disposeDistribute(dataBase);
        System.out.println(sr);
        System.out.println(taskRpcService.disposeDistribute(dataBase));
    }
    /**
     * 小程序提现绑定手机号 验证
     * @param phone
     * @param code
     * @param shopMemberId
     * @return
     */
    @Override
    public Map<String, Object>  shopBindPhone(String phone, String code, Long shopMemberId) {
        // 手机验证
        if (whitePhoneDao.getWhitePhone(phone) == 0) {
            // 如果手机号不在白名单
            boolean phoneVerifySuccess = phoneVerify(code, "sms", phone);
            if (! phoneVerifySuccess) {
                logger.warn("短信验证码错误");
                ErrorHelper.declare(false, "短信验证码错误");
            }
        }
        //账号同步
        ShopMember user = shopMemberDao.findById(shopMemberId);
        ErrorHelper.declareNull(user, "没有用户信息");
        if (user.getWxPhone()==null){
            ShopMember upd = new ShopMember();
            upd.setWxPhone(phone);
            upd.setId(shopMemberId);
            int rec = shopMemberDao.updateById(upd);
        }

        return new HashMap<String, Object>(2){
            {
                put("wxPhone", phone);
            }
        };
    }

    @Override
    public Response liveLoad(String phone, String verifyCode) {
        LiveUser liveUser = null;
        if (whitePhoneDao.getWhitePhone(phone) == 0) {
            // 如果手机号不在白名单
            boolean phoneVerifySuccess = phoneVerify(verifyCode, "", phone);
            if (!phoneVerifySuccess) {
                return Response.errorMsg("短信验证码错误");
            }
        }
        liveUser = liveDao.findLiveUserByPhone(phone);

        if (liveUser==null){
            logger.warn("用户不存在={}", phone);
            return Response.errorMsg("用户不存在");
        }
        return Response.success(liveUser);
    }


    /**
     * 如果用户的微信信息改变了, 返回true
     * 并同时更新实体微信字段的信息
     *
     * @param: wxUser
     * @param: phoneStore
     * @return: void
     * @auther: Charlie(唐静)
     * @date: 2018/5/18 11:39
     */
    private boolean WeiXinInfoIsChange(WeixinLoginData wxUser, StoreBusiness phoneStore) {
        boolean noChange = (ObjectUtils.nullSafeEquals(phoneStore.getBindWeixinId(), wxUser.getUnionid()) &&
                ObjectUtils.nullSafeEquals(phoneStore.getBindWeixinName(), wxUser.getNickname()) &&
                ObjectUtils.nullSafeEquals(phoneStore.getBindWeixinIcon(), wxUser.getHeadimgurl())
        );

        if (! noChange) {
            phoneStore.setBindWeixinName(wxUser.getNickname());
            phoneStore.setBindWeixinId(wxUser.getUnionid());
            phoneStore.setBindWeixinIcon(wxUser.getHeadimgurl());
            phoneStore.setUpdateTime(System.currentTimeMillis());
        }
        return ! noChange;
    }


    public Map<String, Object> loginUser(StoreBusiness storeBusiness, HttpServletRequest request,
                                         HttpServletResponse response, String ip,
                                         ClientPlatform client) {
        logger.info("登陆参数="+storeBusiness.toString());
        Map<String, Object> data = new HashMap<String, Object>();
        if (storeBusiness.getActiveTime() == 0) {
            data.put("activeFlag", "0");

        } else {
            data.put("activeFlag", "1");
        }
        if (storeBusiness.getProtocolTime() == null || storeBusiness.getProtocolTime() == 0) {
            data.put("protocolFlag", "0");

        } else {
            data.put("protocolFlag", "1");
        }

//        String cookieValue = LoginUtil.buildLoginCookieValue(storeBusiness.getBusinessNumber() + "", UserType.PHONE);
//        response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
//        logger.debug("cookie :{}", LoginUtil.buildLoginCookieHeaderValue(cookieValue));

        // spring session
//        request.getSession().setAttribute(CookieConstants.COOKIE_NAME_SESSION, cookieValue);

        // 记录用户登录日志
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setBusinessId(storeBusiness.getId());
        if (client != null) {
            if (client.getPlatform()!=null){
                userLoginLog.setClientType(client.getPlatform().getValue());
            }
            if (client.getVersion()!=null){
                userLoginLog.setClientVersion(client.getVersion());
            }
        }
        userLoginLog.setIp(ip);
        userLoginLog.setCreateTime(System.currentTimeMillis());
        userDao.addUserLoginLog(userLoginLog);
        return data;
    }

    /**
     * 手机验证码验证
     *
     * @param: phoneVerifyCode 手机验证码
     * @param: sendType 验证方式
     * @param: phone 手机号
     * @return: boolean 手机验证是否成功
     * @auther: Charlie(唐静)
     * @date: 2018/5/18 9:07
     */
    private boolean phoneVerify(String phoneVerifyCode, String sendType, String phone) {
        boolean smsSuccess = SmsUtils.verifyCode(phone, phoneVerifyCode);
//        boolean smsSuccess = (sendType.equals("sms") && yunXinSmsService.verifyCode(phone, phoneVerifyCode));
//        boolean voiceSuccess = (sendType.equals("voice") && ucpaasService.verifyCode(phone, phoneVerifyCode));
        return smsSuccess;
    }

    /**
     * 创建一个默认的实体bean
     *
     * @param: phone 电话号码
     * @return:
     * @auther: Charlie(唐静)
     * @date: 2018/5/17 14:07
     */
    public static StoreBusiness buildDefaultStore() {
        long curr = System.currentTimeMillis();
        StoreBusiness user = new StoreBusiness ();
        BigDecimal bigDecimalZero = BigDecimal.ZERO;
        //账户资金
        user.setCashIncome(bigDecimalZero);
        user.setAvailableBalance(bigDecimalZero);
        //提现申请次数
        user.setWithdrawApply(0);
        user.setCreateTime(curr);
        user.setUpdateTime(curr);
        user.setBankCardFlag(0);
        user.setAlipayFlag(0);
        user.setWeixinFlag(0);
        user.setBusinessType(0);
        user.setLastErrorWithdrawPasswordTime(0L);
        user.setErrorCount(0);
        user.setDeep(1L);
        user.setOneStageTime(0);
        user.setTwoStageTime(0);
        user.setThreeStageTime(0);
        user.setGroundUserId(0L);
        user.setShopReservationsOrderSwitch(1);
        user.setGreetingSendType(0);
        user.setGreetingWords("");
        user.setGreetingImage("");

        //默认值
        user.setCommissionPercentage(bigDecimalZero);
        user.setMemberCommissionPercentage(bigDecimalZero);
        user.setDefaultCommissionPercentage(bigDecimalZero);
        user.setHasHotonline(0);
        user.setSynchronousButtonStatus(0);
        user.setVip(0);
        user.setSupplierId(0L);
        user.setIsOpenWxa(0);
        user.setUsedCouponTotalMemberCount(0);
        user.setUsedCouponTotalCount(0);
        user.setUsedCouponTotalMoney(bigDecimalZero);
        user.setWxaType(0);
        user.setRate(bigDecimalZero);
        user.setWxaArticleShow(0);
        user.setWxaOpenTime(0L);
        user.setWxaCloseTime(0L);
        user.setBankCardUseFlag(0);
        user.setAlipayUseFlag(0);
        user.setWeixinUseFlag(0);
        user.setMemberNumber(0);
        user.setStoreArea(bigDecimalZero);
        user.setStoreShowImgs("[]");
        user.setGrade(0);
        user.setWxaRenewProtectCloseTime(0L);
        user.setHotOnline("");
        user.setOnlineWxaVersion("1.1.1");
        user.setAppId(APP_VERSION_372);
        //首次登录
        user.setFirstLoginStatus(FIRST_LOGIN);

        user.setAuditStatus(StoreAuditStatusEnum.submit.getIntValue());
        user.setAuditTime(0L);

        //店铺资料认证 2未提交
        user.setDataAuditStatus(2);
        user.setDataAuditTime(0L);

        //0:未激活；>0激活时间
        user.setActiveTime(0L);
        user.setActivationTime(0L);
        //账户状态0正常，-1删除，1 禁用
        user.setStatus(0);
        //0正常，1 禁用
        user.setDistributionStatus(0);

        user.setWxaBusinessType(0);
        return user;
    }

    /**
     * 登录 认证服务器
     * @param uid
     * @param storeBusiness
     * @return
     */
    public Token storeBusinessToken(String uid, StoreBusiness storeBusiness){
        String nameU = null;
//        if (client.isIphone()||client.isAndroid()) {
            nameU = UserPrefixEnums.SYSTEM_PLATFORM_APP.getPlatform() + storeBusiness.getPhoneNumber();
//        }
        String doubleMd5Pass = Md5Util.md5(Md5Util.md5(storeBusiness.getPhoneNumber()));
        Boolean flag = Boolean.FALSE;
        Token check = authRpcService.load(nameU, doubleMd5Pass, uid);
        if (check==null||(check!=null&&check.getToken()==null)){
            logger.info("老用户 认证系统导入");
            Token token = authRpcService.reg(nameU, doubleMd5Pass,String.valueOf(storeBusiness.getId()), uid, flag);
            logger.info("认证系统返回token={}",token);
            if (token == null || (token!=null&&token.getToken()==null)) {
                return null;
            }
//            if (token.getUserId()==null){
//                storeBusiness.setAuthId(token.getUserId());
//                userDao.updateStoreBusiness(storeBusiness);
//            }

        }
        Token token = authRpcService.load(nameU, doubleMd5Pass, uid);
        if (token == null&&token.getToken()==null) {
            return null;
        }
        return token;
    }

    public static void main(String[] args) {
        String a = "{\"openid\":\"o9BxIwt2g_MMq-7mO_5Wwgw889IY\",\"nickname\":\"请叫我上帝\",\"sex\":1,\"language\":\"zh_CN\",\"city\":\"Haidian\",\"province\":\"Beijing\",\"country\":\"CN\",\"headimgurl\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/DYAIOgq83eqteJ8jt3QxhZ6wQd2zPaN3wbAH2QjVU94ffvmEWDmibcmaeibMhVzkuDgZibkeCQrJhIcfWHJvRlWdg\\/132\",\"privilege\":[],\"unionid\":\"odqlrwf-sp_vPZ9RIDTrOF6swVe4\"}";
        WeixinLoginData weixinLoginData = JSONObject.toJavaObject( (JSON)JSON.parse(a), WeixinLoginData.class);
//        JSON.parse(a);
        System.out.println(weixinLoginData);
        System.out.println(JSON.toJSON(a));
        System.out.println((JSON)JSON.parse(a));
    }
}


