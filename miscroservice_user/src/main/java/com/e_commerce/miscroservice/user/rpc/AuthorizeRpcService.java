package com.e_commerce.miscroservice.user.rpc;


import com.e_commerce.miscroservice.commons.entity.service.Token;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

/**
 * 认证中心的rpc调用
 */
@FeignClient(value = "AUTHORIZE")
public interface AuthorizeRpcService {

    String DEFAULT_PASS = "XIOASHI_AUTHORIZE_" + Objects.hashCode(AuthorizeRpcService.class);
    String DEFAULT_USER_NAME_PREFIX = "XIOASHI_AUTHORIZE_";


    /**
     * 校验
     *
     * @param uid   设备标识
     * @param token 认证凭证
     * @return
     */
    @RequestMapping(value = "check", method = RequestMethod.GET)
    Token check(@RequestParam("uid") String uid, @RequestParam("token") String token);

    /**
     * 修改登陆信息
     *
     * @param name 用户名
     * @param pass 用户密码
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public Token update(@RequestParam("name") String name, @RequestParam("pass") String pass, @RequestParam("token") String token);


    /**
     * 进行注册
     *
     * @param name     用户名
     * @param pass     用户密码
     * @param uid      用户手机标识
     * @param isSingle 是否是单点登录
     * @return
     */

    @RequestMapping(value = "reg", method = RequestMethod.GET)
    public Token reg(@RequestParam("name") String name, @RequestParam("pass") String pass, @RequestParam("userId") String userId, @RequestParam("uid") String uid, @RequestParam("isSingle") Boolean isSingle);


    /**
     * 登录
     *
     * @param name 用户名
     * @param pass 用户密码
     * @param uid  用户手机标识
     * @return
     */

    @RequestMapping(value = "load", method = RequestMethod.GET)
    public Token load(@RequestParam("name") String name, @RequestParam("pass") String pass, @RequestParam("uid") String uid);
}
