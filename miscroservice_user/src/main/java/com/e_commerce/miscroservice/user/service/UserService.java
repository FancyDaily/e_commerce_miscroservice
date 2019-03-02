package com.e_commerce.miscroservice.user.service;

import com.e_commerce.miscroservice.commons.entity.AliPayCallBackEntity;
import com.e_commerce.miscroservice.commons.entity.user.ClientPlatform;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.user.po.UserPO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 用户service
 */
public interface UserService {

    /**
     * 根据手机号码获取用户信息
     *
     * @param userPhone 用户手机号码
     * @return 用户信息
     */
    UserPO getUserByUserPhone(String userPhone);

    /**
     * 注册
     *
     * @param platform
     * @param name 用户名
     * @param pass 密码
     * @param uid 设备uid
     * @param isSingle 是否单点登陆 true or false
     * @param response
     * @return
     */
    Response register(Integer platform, String name, String pass, String uid, Boolean isSingle, HttpServletResponse response);

    /**
     * 登录
     *
     *
     * @param platform
     * @param name 用户名
     * @param pass 用户密码
     * @param uid  用户手机标识
     * @return
     */
    Response load(Integer platform, String name, String pass, String uid, HttpServletRequest request, HttpServletResponse response);
    /**
     * 验证码登录APP
     * @param phone 手机号
     * @param verifyCode 验证码
     * @param sendType 验证码类型
     * @param request
     * @param response
     * @param client
     * @return
     */
    Response verifyCommit(String phone, String verifyCode, String sendType, HttpServletRequest request, HttpServletResponse response, String ip, ClientPlatform client);

    Response storeBusinessLogin(String phone, HttpServletRequest request, HttpServletResponse response, String ip, ClientPlatform client);

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    void sendMsg(String phone);

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
    Response wxLogin(String openId, String openuid, String accessToken, String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response);
    /**
     * 微信 绑定手机号
     * @param openId
     * @param openuid
     * @param accessToken
     * @param phone
     * @param client
     * @param request
     * @param response
     * @return
     */
    Response phoneBindWeixinCommit(String openId, String openuid, String accessToken, String phone, String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response);

    /**
     * 充值
     * @param money 金额
     * @param type 充值方式 0 支付宝  1 微信
     * @param id
     * @param openid
     * @param ip
     * @return
     */
    Response rechargeMoney(Double money, Integer type, Integer id, String openid, String ip);
    /**
     * 充值 回调
     * @param notify 回调内容
     * @param client
     * @return
     */
    String callbackRechargeAliPay(AliPayCallBackEntity notify, ClientPlatform client);

    /**
     * 微信充值回调处理
     * @param result
     * @param client
     * @return
     */
    String callbackRechargeWxPay(HttpServletRequest result, ClientPlatform client);

    /**
     * 获取用户流水
     * @param id
     * @param page
     * @param inOut
     * @param type
     * @param typeAdaptor
     * @return
     */
    Response getUserAccountList(Integer id, Integer page, Integer inOut, Integer type, Integer typeAdaptor);

    /**
     * 核对余额
     * @param money
     * @param id
     * @return
     */
    Response checkMoney(Double money, Integer id);

    /**
     * 余额支付
     * @param orderNo
     * @param id
     * @param version
     * @return
     */
    Response userPay(String orderNo, Integer id, String version);

    /**
     * 充值后显示金额
     * @param id
     * @return
     */
    Response showRechargeMoney(Integer id);

    /**
     * 余额查询
     * @param id
     * @return
     */
    Response showMoney(Integer id);

    /**
     * 用户流水详情
     * @param id
     * @param userId
     * @return
     */
    Response userAccountDetail(Long id, Integer userId);

    /**
     * 设置Redis用户id
     * @param key
     * @param value
     * @return
     */
    Response redisSetValue(String key, String value);

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
    Response bindWeixinOauth(String verifyCode, String sendType, String openId, String openuid, String accessToken, String phone, String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response);


    Response sendCouponByOldSys(Long storeId);

    /**
     * 测试
     * @param id
     * @param memberId
     * @param roomId
     * @param code
     * @param moduleType
     * @param storeId
     * @param distributorId
     */
    void test(Long id, Long memberId, Long roomId, Integer code, Integer moduleType, Long storeId, Long distributorId);
    /**
     * 小程序提现绑定手机号 验证
     * @param phone
     * @param code
     * @param shopMemberId
     * @return
     */
    Map<String, Object> shopBindPhone(String phone, String code, Long shopMemberId);


    /**
     * 短信验证登陆
     * @param phone
     * @param verifyCode
     * @return
     */
    Response liveLoad(String phone, String verifyCode);
}
