package com.e_commerce.miscroservice.user.controller;

import java.util.HashMap;
import java.util.Map;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import com.e_commerce.miscroservice.order.controller.BaseController;
import com.e_commerce.miscroservice.user.service.LoginService;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.vo.WechatLoginVIew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信授权登录模块
 */
@RestController
@RequestMapping("api/v2/login")
public class LoginController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WechatService wechatService;

    @Value("${redis_user}")
    private String REDIS_USER;

    /**
     * 登录状态
     */
    private static final String LOGIN_STATUS = "loginStatus";

    /**
     * 实名状态
     */
    private static final String CERT_STATUS = "certStatus";

    /**
     * 用户id
     */
    private static final String USER_ID = "userId";

    /**
     * 登入状态-登录
     */
    @SuppressWarnings("unused")
    private static final String LOGIN_STATUS_LOGIN = "02";

    /**
     * 手机号码授权
     *
     * @param openid
     * @param encryptedData
     * @param iv
     * @return
     */
    @RequestMapping("phoneAutho")
    public Object phoneAutho(String openid, String encryptedData, String iv) {
        AjaxResult result = new AjaxResult();
        try {
            String telephone = loginService.phoneAutho(openid, encryptedData, iv);
            result.setSuccess(true);
            result.setErrorCode(AppErrorConstant.Auhto_Success);
            result.setMsg("手机授权成功");
            result.setData(telephone);
        } catch (MessageException e) {
            logger.error(e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(errInfo(e));
            result.setSuccess(false);
            result.setErrorCode(AppErrorConstant.AppError.SysError.getErrorCode());
            result.setMsg(AppErrorConstant.AppError.SysError.getErrorMsg());
        }
        return result;
    }

    /**
     * 校验短信
     *
     * @param openid
     * @param validCode
     * @return
     */
    @RequestMapping("validSmsCode")
    public Object validSmsCode(String openid, String validCode, @RequestParam(required = false) String uuid) {
        AjaxResult result = new AjaxResult();
        try {
            Map<String, Object> resultMap = loginService.validSmsCode(openid, validCode, uuid);
            String loginStatus = (String) resultMap.get(LOGIN_STATUS);
            String token = (String) resultMap.get(AppConstant.USER_TOKEN);
            TUser user = (TUser) redisUtil.get(token);
            String certStatus = userService.getCertStatus(user.getId()); // 获取实名信息
            Map<String, String> map = new HashMap<>();
            map.put(CERT_STATUS, certStatus);
            map.put(LOGIN_STATUS, loginStatus);
            map.put(AppConstant.USER_TOKEN, token);
            map.put(USER_ID, String.valueOf(user.getId()));
            result.setErrorCode(AppErrorConstant.SendSms_Success);
            result.setMsg("短信校验成功");
            result.setData(map);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error(e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(errInfo(e));
            result.setSuccess(false);
            result.setErrorCode(AppErrorConstant.AppError.SysError.getErrorCode());
            result.setMsg(AppErrorConstant.AppError.SysError.getErrorMsg());
        }
        return result;
    }

    /**
     * 登陆校验
     *
     * @param view
     * @return
     */
    @RequestMapping("checkLogin")
    public Object checkLogin(WechatLoginVIew view) {
        AjaxResult result = new AjaxResult();
        try {
            Map<String, String> resMap = loginService.checkLogin(view);
            result.setData(resMap);
            result.setSuccess(true);
        } catch (MessageException e) {
            logger.error(e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(errInfo(e));
            result.setSuccess(false);
            result.setErrorCode(AppErrorConstant.AppError.SysError.getErrorCode());
            result.setMsg(AppErrorConstant.AppError.SysError.getErrorMsg());
        }
        return result;
    }

    /**
     * 与微信校验授权
     *
     * @param code
     * @return
     */
    @RequestMapping("checkAuth")
    public Object checkAuth(String code) {
        AjaxResult result = new AjaxResult();
        try {
            result.setSuccess(true);
            result.setData(wechatService.checkAuthCode(code));
        } catch (MessageException e) {
            logger.error(e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(errInfo(e));
            result.setSuccess(false);
            result.setErrorCode(AppErrorConstant.AppError.SysError.getErrorCode());
            result.setMsg(AppErrorConstant.AppError.SysError.getErrorMsg());
        }
        return result;
    }

    /**
     * 请求token
     *
     * @param openid
     * @return
     */
    @PostMapping("reLogin")
    public Object reLogin(String openid) {
        AjaxResult result = new AjaxResult();
        try {
            Map<String, Object> resultMap = loginService.loginByOpenid(openid);
            result.setSuccess(true);
            result.setData(resultMap);
        } catch (MessageException e) {
            logger.error(e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(errInfo(e));
            result.setSuccess(false);
            result.setErrorCode(AppErrorConstant.AppError.SysError.getErrorCode());
            result.setMsg(AppErrorConstant.AppError.SysError.getErrorMsg());
        }
        return result;
    }

}
