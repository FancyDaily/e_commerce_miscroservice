package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.AliPayCallBackEntity;
import com.e_commerce.miscroservice.commons.entity.user.ClientPlatform;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.Iptools;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户模块
 *
 * @author hyf
 * @version V1.0
 * @date 2018/11/5 15:08
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("get/auth")
    public Integer get() {
        return IdUtil.getId();
    }

    /**
     * 注册
     *
     * @param name     用户名
     * @param pass     密码
     * @param uid      设备uid 用户手机标识
     * @param isSingle 是否单点登陆 true or false
     * @param platform 平台类型 默认：0 app，
     * @return
     */
    @RequestMapping("register")
    public Response register(@RequestParam(value = "platform", defaultValue = "0") Integer platform,
                             @RequestParam("name") String name,
                             @RequestParam("pass") String pass, @RequestParam("uid") String uid,
                             @RequestParam(value = "isSingle", required = false) Boolean isSingle,
                             HttpServletResponse response) {
        return userService.register(platform, name, pass, uid, isSingle, response);
    }


    /**
     * 修改登陆信息
     *
     * @param name     用户名
     * @param pass     用户密码
     * @param platform 平台类型 默认：0 app，
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public Response update(@RequestParam(value = "platform", defaultValue = "0") Integer platform, @RequestParam("name") String name, @RequestParam("pass") String pass, HttpServletResponse response, HttpServletRequest request) {
        String token = request.getHeader("token");
//       return userService.update(name,pass,token,response);
        return null;
    }


    /**
     * 登录
     *
     * @param name     用户名
     * @param pass     用户密码
     * @param uid      用户手机标识
     * @param platform 平台类型 默认：0 app，
     * @return
     */
    @RequestMapping(value = "load", method = RequestMethod.GET)
    public Response load(@RequestParam(value = "platform", defaultValue = "0") Integer platform, @RequestParam("name") String name, @RequestParam("pass") String pass, @RequestParam("uid") String uid, HttpServletResponse response, HttpServletRequest request) {
        return userService.load(platform, name, pass, uid, request, response);
    }

    /**
     * 验证码登录APP
     *
     * @param phone      手机号
     * @param verifyCode 验证码
     * @param sendType   验证码类型
     * @param request
     * @param response
     * @param client
     * @return
     */
    @RequestMapping("/verify/commit")
    public Response verifyCommit(@RequestParam("phone") String phone,
                                 @RequestParam("verify_code") String verifyCode, @RequestParam("send_type") String sendType,
                                 HttpServletRequest request, HttpServletResponse response, ClientPlatform client) {
        String ip = Iptools.gainRealIp(request);

        Response res = userService.verifyCommit(phone, verifyCode, sendType, request, response, ip, client);

        return res;
    }

    /**
     * 直播间登陆
     */
    @RequestMapping("/live/load")
    public Response liveLoad(String phone,String verifyCode){
        return userService.liveLoad(phone,verifyCode);
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @RequestMapping("send/msg")
    public Response sendMsg(String phone) {
        userService.sendMsg(phone);
        return Response.success();
    }

    /**
     * 微信登录
     *
     * @param openId
     * @param openuid
     * @param accessToken
     * @param client
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/wx/login")
    public Response wxLogin(@RequestParam("openid") String openId,
                            @RequestParam("openuid") String openuid, @RequestParam("access_token") String accessToken,
                            ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {
        String ip = Iptools.gainRealIp(request);
        return userService.wxLogin(openId, openuid, accessToken, ip, client, request, response);
    }

    /**
     * 微信 绑定手机号
     *
     * @param openId
     * @param openuid
     * @param accessToken
     * @param phone
     * @param client
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping("/bind/weixin")
//    public Response phoneBindWeixinOauth(@RequestParam("openid") String openId,
//                                         @RequestParam("openuid") String openuid, @RequestParam("access_token") String accessToken,
//                                         @RequestParam("phone") String phone, ClientPlatform client,
//                                         HttpServletRequest request, HttpServletResponse response) {
//        String ip = Iptools.gainRealIp(request);
//        return userService.phoneBindWeixinCommit(openId, openuid, accessToken, phone, ip, client, request,
//                response);
//
//    }


    /**
     * 微信绑定手机号
     *
     * @param: verifyCode 手机验证码
     * @param: sendType 手机验证方式
     * @param: openId
     * @param: openuid
     * @param: accessToken
     * @param: phone
     * @param: ip
     * @param: client
     * @param: request
     * @param: response
     * @return: com.jiuyuan.web.help.JsonResponse
     * @auther: Charlie(唐静)
     * @date: 2018/5/17 17:06
     */
    @RequestMapping( value = "/bind/weixin")
    @ResponseBody
    public Response bindWeixinOauth(
            @RequestParam( "phone" ) String phone,
            @RequestParam( "verify_code" ) String verifyCode,
            @RequestParam( "send_type" ) String sendType,
            @RequestParam( "openid" ) String openId,
            @RequestParam( "openuid" ) String openuid,
            @RequestParam( "access_token" ) String accessToken,
            ClientPlatform client,
            HttpServletRequest request, HttpServletResponse response
    ) {

        String ip = Iptools.gainRealIp(request);
            Response js =  userService.bindWeixinOauth(verifyCode, sendType, openId, openuid, accessToken, phone, ip, client, request,
                    response);
//            grant(js);
            return js;

    }

    /**
     * 充值
     * @param money 充值金额
     * @param type 充值方式 0 支付宝  1 微信
     * @return
     */
    @RequestMapping("/recharge/auth")
    public Response rechargeMoney(Double money,Integer type,HttpServletRequest request,@RequestParam(name = "openid",required = false) String openid){
        Integer id = IdUtil.getId();
//        Integer id = 3;
        String ip = Iptools.gainRealIp(request);
        return userService.rechargeMoney(money,type,id,openid,ip);
    }
    /**
     * 支付宝充值 回调
     * @param aliPayCallBackEntity 回调内容
     * @param client
     * @return
     */
    @RequestMapping("/callback/recharge/alipay")
    public String callbackRechargeAliPay(AliPayCallBackEntity aliPayCallBackEntity , ClientPlatform client){
        String is = userService.callbackRechargeAliPay(aliPayCallBackEntity,client);
        return is;
    }
    /**
     * 微信充值 回调
     * @param client 回调内容
     * @param client
     * @return
     */
    @RequestMapping("/callback/recharge/weixin")
    public String callbackRechargeWxPay( ClientPlatform client, HttpServletRequest request){
        String is = userService.callbackRechargeWxPay(request,client);
        return is;
    }

    /**
     * 充值后显示金额
     * @return
     */
    @RequestMapping("/show/recharge/money/auth")
    public Response showRechargeMoney(){
                Integer id = IdUtil.getId();
//        Integer id = 3;
        return userService.showRechargeMoney(id);
    }
    /**
     * 余额查询
     * @return
     */
    @RequestMapping("/show/money/auth")
    public Response showMoney(){
                Integer id = IdUtil.getId();
//        Integer id = 3;
        return userService.showMoney(id);
    }
    /**
     * 账户流水
     * @param  typeAdaptor 1可用资金,2待结资金
     * @return
     */
    @RequestMapping("/account/list/auth")
    public Response userAccountList(Integer page,Integer inOut,Integer type,
                                    @RequestParam(value = "typeAdaptor", required = false) Integer typeAdaptor
    ){
//        Integer id = 3;
        Integer id = IdUtil.getId();
        return userService.getUserAccountList(id,page,inOut,type, typeAdaptor);
    }
    /**
     * 账户流水详情
     * @return
     */
    @RequestMapping("/account/detail/auth")
    public Response userAccountDetail(Long id ){
//        Integer userId = 3;
        Integer userId = IdUtil.getId();
        return userService.userAccountDetail(id,userId);
    }

    /**
     * 核对金额
     * @param money
     * @return
     */
    @RequestMapping("/check/money/auth")
    public Response checkMoney(Double money){
        Integer id = IdUtil.getId();
        return userService.checkMoney(money,id);
    }

    /**
     * 余额支付
     * @param orderNo
     * @return
     */
    @RequestMapping("/user/pay/auth")
    public Response userPay(String orderNo,ClientPlatform clientPlatform){
        Integer id = IdUtil.getId();
        return userService.userPay(orderNo,id,clientPlatform.getVersion());
    }

    /**
     * 设置Redis用户id
     * @return
     */
    @RequestMapping("/redis/value")
    public Response redisSetValue(String key,String value){
        return userService.redisSetValue(key,value);
    }

    @RequestMapping("/send/coupon/sys")
    public Response sendCouponByOldSys(Long storeId){
        return userService.sendCouponByOldSys(storeId);
    }
    @RequestMapping("/test")
    public Response test2(Long id,Long memberId,Long roomId,Integer code,Integer moduleType,Long storeId,Long distributorId){
        System.out.println("收到");
//        dataBase.setId(1L);
//        dataBase.setMemberId(2L);
//        dataBase.setRoomId(100L);
//        dataBase.setCode(1);
//        dataBase.setModuleType(0);
        userService.test( id, memberId, roomId, code, moduleType, storeId,distributorId);
        return Response.success("kkkk");
    }

     /**
     * 小程序提现绑定手机号 验证
     * @param phone
     * @param code
     * @return
     */
    @RequestMapping("shop/binding/phone")
    public Response shopBindingPhone(String phone,String code){


        return ResponseHelper.shouldLogin()
                .invokeHasReturnVal(shopMemberId-> userService.shopBindPhone(phone,code,shopMemberId))
                .returnResponse();
    }



}
