package com.e_commerce.miscroservice.commons.utils.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.e_commerce.miscroservice.commons.utils.HttpUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/13 20:35
 * @Copyright 玖远网络
 */
public class WxAuthUtil {

    private static final Map<String, String> EMPTY_MAP = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(WxAuthUtil.class);
    private static final String API_AUTHORIZER_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token";
    private static final String COMPONENT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
    private static final String SESSION_KEY_URL = "https://api.weixin.qq.com/sns/component/jscode2session";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    private static final String PRE_AUTH_CODE_URL = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode";

    /**
     * 配置小程序域名
     */
    private static final String SET_WEB_VIEW_DOMAIN_URL = "https://api.weixin.qq.com/wxa/setwebviewdomain";
    /**
     * 微信授权后根据auth_code换取微信公众号,小程序用户信息的地址
     */
    private static final String API_QUERY_AUTH_URL = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth";
    /**
     * 用户登入小程序授权的路径
     */
    private static final String COMPONENT_LOGIN_PAGE_URL = "https://mp.weixin.qq.com/cgi-bin/componentloginpage";

    /**
     * 获取第三方accessToken
     *
     * @param cpnAppId component_appid
     * @param cpnAppSecret component_appsecret
     * @param cpnVerifyTicket component_verify_ticket
     * @return java.lang.String
     * <p>
     * accessToken = {component_access_token=13_gaKx6NdNJB3BlO4AaLE5wxJuql1B_GEakUs04xvNTzrbS3G31tG2Q2_WCg9GwubPj8_y01QsbcHs8awTNNczQjZmmOpBADyh4JegXm1Of6LPwDLURc2PbGTf5qJqiZuUn-VxiNY7WgBglIAbHMChAJADIY,
     * expires_in=7200}
     * </p>
     * @author Charlie
     * @date 2018/9/13 21:31
     */
    public static Map<String, String> componentAccessToken(String cpnAppId, String cpnAppSecret, String cpnVerifyTicket) {

        Map<String, Object> param = new HashMap<>(4);
        param.put("component_appid", cpnAppId);
        param.put("component_appsecret", cpnAppSecret);
        param.put("component_verify_ticket", cpnVerifyTicket);
        String paramJson = JSONObject.toJSONString(param);
//        String paramJson = new Gson().toJson (param);
        LOGGER.info("获取第三方票据 url --> url[{}].paramJson[{}]", COMPONENT_ACCESS_TOKEN_URL, paramJson);
        String result = HttpClientUtils.post(COMPONENT_ACCESS_TOKEN_URL, paramJson);
        LOGGER.info("获取第三方票据 componentAccessToken --> retMap[{}]", result);
        if (StringUtils.isBlank(result)) {
            return EMPTY_MAP;
        } else {
            return JSON.parseObject(result, new TypeReference<Map<String, String>>() {
            });
//            return new Gson ().fromJson (result, new TypeToken<HashMap<String, Object>> (){}.getType());
        }
    }


    /**
     * 获取sessionKey 可用
     *
     * @param componentAccessToken componentAccessToken
     * @param appId appId
     * @param jsCode jsCode
     * @param cpnAppId cpnAppId
     * @return <P> sessionKey = {"session_key":"vOHeWWhpZLgBb6bmnrwUHw==","openid":"o01of0W-wibaVzzYKHJoTlNH5a7Y"} </P>
     * @author Charlie
     * @date 2018/9/13 22:06
     */
    public static String sessionKey(String componentAccessToken, String appId, String jsCode, String cpnAppId) {
        Map<String, Object> params = new HashMap(6);
        //小程序的AppID
        params.put("appid", appId);
        params.put("js_code", jsCode);
        params.put("grant_type", "authorization_code");
        params.put("component_access_token", componentAccessToken);
        params.put("component_appid", cpnAppId);
        return HttpClientUtils.get(SESSION_KEY_URL, params);
    }


    /**
     * 获取sessionKey
     *
     * @param appId appId
     * @param jsCode jsCode
     * @param cpnAppId cpnAppId
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/13 22:06
     */
    @Deprecated
    public static String sessionKey(String appId, String jsCode, String cpnAppId, String cpnAppSecret, String cpnVerifyTicket) {
        Map<String, Object> params = new HashMap(6);
        //小程序的AppID
        params.put("appid", appId);
        params.put("js_code", jsCode);
        params.put("grant_type", "authorization_code");
        params.put("component_appid", cpnAppId);
        Object accessToken = componentAccessToken(cpnAppId, cpnAppSecret, cpnVerifyTicket).get("component_access_token");
        if (null == accessToken) {
            LOGGER.info("获取sessionKey, componentAccessToken为空");
            return null;
        }
        params.put("component_access_token", accessToken);
        return HttpClientUtils.get(SESSION_KEY_URL, params);
    }


    /**
     * accessToken(好像没用)
     * <p>
     * 1.为了保密 appsecrect，第三方需要一个 access_token 获取和刷新的中控服务器。而其他业务逻辑服务器所使用的 access_token 均来自于该中控服务器，不应该各自去刷新，否则会造成
     * access_token 覆盖而影响业务； 2.目前 access_token 的有效期通过返回的 expires_in 来传达，目前是7200秒之内的值。中控服务器需要根据这个有效时间提前去刷新新
     * access_token。在刷新过程中，中控服务器对外输出的依然是老 access_token，此时公众平台后台会保证在刷新短时间内，新老 access_token 都可用，这保证了第三方业务的平滑过渡；
     * 3.access_token 的有效时间可能会在未来有调整，所以中控服务器不仅需要内部定时主动刷新，还需要提供被动刷新 access_token 的接口，这样便于业务服务器在 API 调用获知 access_token
     * 已超时的情况下，可以触发 access_token 的刷新流程。
     * </p>
     *
     * @author Charlie(唐静)
     * @date
     */
    @Deprecated
    public static String accessToken(String appId, String secret) {
        String url = ACCESS_TOKEN_URL + "&appid=" + appId + "&secret=" + secret;
        LOGGER.info("获取访问token url[{}].", url);
        String result = HttpClientUtils.get(url);
        LOGGER.info("获取访问token accessToken[{}].", result);
        return result;
    }


    /**
     * 解密
     *
     * @param skRes sessionKey
     * @param ivData 解密算法?
     * @param encryptedData 加密数据包
     * @return java.lang.String
     * @author Charlie
     * @date 2018/12/27 16:20
     */
    public static String decrypt(String skRes, String ivData, String encryptedData) throws Exception {
        //解密
        byte[] key = Base64.decode(skRes);
        byte[] encData = Base64.decode(encryptedData);
        byte[] iv = Base64.decode(ivData);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        //解析解密后的字符串  
        return new String(cipher.doFinal(encData),"UTF-8");
    }


    @Data
    public static class XiaochengxuLoginVo implements IsNull {
        /**
         * 小程序授权url
         */
        private String loginUrl;
        /**
         * 参数
         */
        private String componentAppid;
        /**
         * 参数 预授权code
         */
        private String preAuthCode;
        /**
         * 有效时间
         */
        private Integer expiresIn;
        /**
         * 参数 授权成功后微信回调地址
         */
        private String redirectUri;

        /**
         * isNull
         */
        private boolean isNull;

        public XiaochengxuLoginVo(String componentLoginPageUrl, String cpnAppId, String redirectUri, HashMap<String, String> preCodeMap) {
            this.loginUrl = componentLoginPageUrl;
            this.componentAppid = cpnAppId;
            this.redirectUri = redirectUri;
            String preAuthCode = preCodeMap.get("pre_auth_code");
            if (StringUtils.isBlank(preAuthCode)) {
                isNull = true;
            } else {
                isNull = false;
                this.preAuthCode = preAuthCode;
                String expiresIn = preCodeMap.get("expires_in");
                expiresIn = StringUtils.isBlank(expiresIn) ? "0" : expiresIn;
                this.expiresIn = Integer.parseInt(expiresIn);
            }
        }

        @Override
        public String toString() {
            return loginUrl +
                    "?component_appid=" + componentAppid +
                    "&pre_auth_code=" + preAuthCode +
                    "&redirect_uri=" + redirectUri;
        }

        @Override
        public boolean isNull() {
            return isNull;
        }
    }

    /**
     * 小程序授权路径 URL
     *
     * @param cpnAppId 第三方appid
     * @param componentAccessToken com.test.config.util.WxAuthUtil#componentAccessToken(java.lang.String,
     * java.lang.String, java.lang.String)
     * @param redirectUri 回调URI
     * <p>
     * 在授权回调接口做的事: 验证授权 记录授权方账号信息 进行授权 给小程序设置业务域名
     * </p>
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/15 21:22
     */
    public static XiaochengxuLoginVo xiaochengxuLoginUrl(String cpnAppId, String componentAccessToken, String redirectUri) {
        //1.预授权code
        String preAuthCodeUrl = PRE_AUTH_CODE_URL + "?component_access_token=" + componentAccessToken;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("component_appid", cpnAppId);
        String param = JSONObject.toJSONString(paramMap);
        /* {"pre_auth_code":"preauthcode@@@K6qo1-dtkJEfDbACzNWjLvIoicocyPKE_JqUa4mnB6xl7uu06g1vqnm4sK9iCUn9","expires_in":1800} */
        String codeResult = HttpClientUtils.post(preAuthCodeUrl, param);
//        String authCode = JSON.parseObject (codeResult, new TypeReference<HashMap<String, String>> (){}).get ("pre_auth_code");
        LOGGER.info("小程序授权页面路径---预授权code codeResult:{}", codeResult);
        return new XiaochengxuLoginVo(COMPONENT_LOGIN_PAGE_URL, cpnAppId, redirectUri, JSON.parseObject(codeResult, new TypeReference<HashMap<String, String>>() {
        }));
    }


    /**
     * 微信授权后,获取授权code, 利用授权code换取的用户信息
     */
    @Data
    @NoArgsConstructor( access = AccessLevel.PROTECTED )
    @AllArgsConstructor( access = AccessLevel.PROTECTED )
    public static class AuthorizationInfo implements IsNull {
        private String authorizerAppId;
        private String authorizerAccessToken;
        private Integer expiresIn;
        private String authorizerRefreshToken;
        private JSONArray funcInfoArray;
        private boolean isNull;

        private static AuthorizationInfo build(JSONObject json) {
            if (null == json.getString("authorizer_access_token")) {
                AuthorizationInfo empty = new AuthorizationInfo();
                empty.isNull = true;
                return empty;
            }

            return new AuthorizationInfo(
                    json.getString("authorizer_appid"),
                    json.getString("authorizer_access_token"),
                    json.getInteger("expires_in"),
                    json.getString("authorizer_refresh_token"),
                    json.getJSONArray("func_info"),
                    false
            );
        }

        @Override
        public boolean isNull() {
            return isNull;
        }
    }

    /**
     * 授权后,微信回调redirectUrl,推送auth_code,获取的code换取用户接口调用token
     * <p>
     * 获取的token {@link AuthorizationInfo#authorizerAccessToken}会过期的,这时需要 用{@link AuthorizationInfo#authorizerRefreshToken}做入参调用{@link
     * WxAuthUtil#refreshAuthorizerToken(String, String, String, String)} 刷新获取最新的 AuthorizationInfo
     * </p>
     *
     * @param authCode 通过回调得到auth_code
     * @param componentAppId 第三方appid
     * @param componentAccessToken {@link WxAuthUtil#componentAccessToken(String, String, String)}
     * @author Charlie
     * @date 2018/9/18 11:35
     */
    public static AuthorizationInfo queryAuthorizationInfo(String authCode, String componentAppId, String componentAccessToken) {
        //使用授权码换取公众号或小程序的接口调用凭据和授权信息
        String url = API_QUERY_AUTH_URL + "?component_access_token=" + componentAccessToken;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("component_appid", componentAppId);
        paramMap.put("authorization_code", authCode);
        String param = JSONObject.toJSONString(paramMap);
        String authorizationInfoResult = HttpClientUtils.post(url, param);
        LOGGER.info("授权回调---使用授权码换取公众号或小程序的授权信息 authorizationInfoResult:{}", authorizationInfoResult);
        JSONObject authorizationInfoJson = JSONObject.parseObject(authorizationInfoResult).getJSONObject("authorization_info");
        if (authorizationInfoJson == null) {
            return new AuthorizationInfo() {
                @Override
                public boolean isNull() {
                    return Boolean.TRUE;
                }
            };
        }
        //授权信息
        return AuthorizationInfo.build(authorizationInfoJson);
    }


    /**
     * 配置小程序域名
     *
     * @param authorizerToken {@link AuthorizationInfo#authorizerAppId}
     * @param domains 域名
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/18 12:46
     */
    public static String configWebViewDomain(String authorizerToken, String... domains) {
        if (domains == null || domains.length == 0) {
            return null;
        }

        String url = SET_WEB_VIEW_DOMAIN_URL + "?access_token=" + authorizerToken;
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("action", "add");
        paramMap.put("webviewdomain", domains.clone());
        String param = JSONObject.toJSONString(paramMap);
        //{"errcode":61007,"errmsg":"api is unauthorized to component hint: [SIUf06783064]"}
        String result = HttpClientUtils.post(url, param);
        LOGGER.info("配置小程序业务域名 url:{},param:{},result{}", url, param, result);
        return result;
    }


    /**
     * 刷新授权公众号或小程序的接口调用凭据（令牌）
     *
     * @param componentAppId 第三方appId
     * @param componentAccessToken 第三方访问的token
     * @param historyAuthorizerRefreshToken 历史过期的authorizer_access_token 对应的 authorizer_refresh_token
     * @param authorizerAppid 授权用户的appId
     * @return com.test.config.util.WxAuthUtil.AuthorizationInfo
     * @author Charlie
     * @date 2018/9/18 14:01
     */
    public static AuthorizationInfo refreshAuthorizerToken(String componentAppId, String componentAccessToken, String authorizerAppid, String historyAuthorizerRefreshToken) {
        //1、准备数据
        String url = API_AUTHORIZER_TOKEN_URL + "?component_access_token=" + componentAccessToken;

        //组装参数调用接口
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("component_appid", componentAppId);
        paramMap.put("authorizer_appid", authorizerAppid);
        paramMap.put("authorizer_refresh_token", historyAuthorizerRefreshToken);
        String param = JSONObject.toJSONString(paramMap);
        String authorizerAppidResult = HttpClientUtils.post(url, param);
        LOGGER.info("刷新小程序接口调用凭证, url[{}].param[{}].authorizerAppidResult[{}]", url, param, authorizerAppidResult);
        //解析返回值
        JSONObject authorizationInfoJson = JSONObject.parseObject(authorizerAppidResult);
        //授权信息
        return AuthorizationInfo.build(authorizationInfoJson);
    }


    public Object getPhoneNumber(String encryptedData, String code, String iv) {
        //HttpUtil.getOpenid这个方法是我自己封装的方法，传入code后然后获取openid和session_key的，把他们封装到json里面
        Map<String, String> params = openId(code, "", "");
        String session_key = "";
        if (params != null) {
            session_key = params.get("session_key");
            // 被加密的数据
            byte[] dataByte = Base64.decode(encryptedData);
            // 加密秘钥
            byte[] keyByte = Base64.decode(session_key);
            // 偏移量
            byte[] ivByte = Base64.decode(iv);
            try {
                // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
                int base = 16;
                if (keyByte.length % base != 0) {
                    int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                    byte[] temp = new byte[groups * base];
                    Arrays.fill(temp, (byte) 0);
                    System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                    keyByte = temp;
                }
                // 初始化
                Security.addProvider(new BouncyCastleProvider());
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
                AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
                parameters.init(new IvParameterSpec(ivByte));
                cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
                byte[] resultByte = cipher.doFinal(dataByte);
                if (null != resultByte && resultByte.length > 0) {
                    String result = new String(resultByte, "UTF-8");
                    return JSONObject.parseObject(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Map<String, String> openId(String code, String appId, String secret) { // 小程序端获取的CODE
        Map<String, String> result = new HashMap<>();
        result.put("code", "0");
        try {
            boolean check = (StringUtils.isEmpty(code)) ? true : false;
            if (check) {
                throw new Exception("参数异常");
            }
            StringBuilder urlPath = new StringBuilder("https://api.weixin.qq.com/sns/jscode2session");
            urlPath.append(String.format("?appid=%s", appId));
            urlPath.append(String.format("&secret=%s", secret));
            urlPath.append(String.format("&js_code=%s", code));
            urlPath.append(String.format("&grant_type=%s", "authorization_code"));
            String data = HttpUtils.sendPost(urlPath.toString(), new HashMap<>());
            System.out.println("请求结果：" + data);
            String openId = JSONObject.parseObject(data).getString("openid");
            System.out.println("获得openId: " + openId);
            result.put("openId", openId);
        } catch (Exception e) {
            result.put("code", "1");
            result.put("remark", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取sessionKey 暂时是从老系统的jfinal里获取的
     *
     * @param url 老系统接口地址
     * @param appId appId
     * @param sessionId sessionId
     * @return java.lang.String
     * @author Charlie
     * @date 2018/12/27 15:01
     */
    public static String acquireSessionkey(String url, String appId, String sessionId) {
        String data = HttpUtils.sendGet(url,
                new HashMap<String, String>(2) {
                    {
                        put("appId", appId);
                        put("sessionId", sessionId);
                    }
                });
        return data;
    }


    public static void main(String[] args) {
//        String cpnAppId = "wx3ef25e066e478873";
//        String cpnAppSecret = "f646bd674afb7357fd06536c2fea626b";
////        System.out.println ("accessToken = " + accessToken);
////
//        String appId = "wxf99f985dc7f79695";
//
//        String cpnVerifyTicket = "ticket@@@otsxzX8gGhg-A5QOwzwteSYt1946U0hrRHuDydKfVXaJwyif7-NdsWcI3RjB5uH63d9xvaeUk-AGaATwd0ulPQ";
//        String jsCode = "071cv5hz0XcROf1kc3iz0yt7hz0cv5ho";
//        /*
//         * 第三方token
//         */
//        String componentAccessToken = componentAccessToken(cpnAppId, cpnAppSecret, cpnVerifyTicket).get("component_access_token");
//        /*
//         * 小程序登录, 就是根据前端提交的js_code,获取sessionKey,和openId的过程,
//         * 然后服务端利用sessionKey和openId在服务端登录
//         */
//        String sessionKey2 = sessionKey(componentAccessToken, appId, jsCode, cpnAppId);
////        System.out.println ("sessionKey2 = " + sessionKey2);
//
//        /*
//         * 小程序授权页的参数
//         *
//         * 先获取第三方的componentAccessToken,然后再利用componentAccessToken获取预授权code
//         * 然后登录小程序授权页面,并将第三方id和code带过去
//         */
//        String redictUrl = "http://admintest.yujiejie.com/wxa/gotoAuthCallback?storeId=68";
//        XiaochengxuLoginVo xiaochengxuLoginVo = xiaochengxuLoginUrl(cpnAppId, componentAccessToken, redictUrl);
//        if (!xiaochengxuLoginVo.isNull()) {
//            System.out.println("xiaochengxuLoginVo.toString () = " + xiaochengxuLoginVo.toString());
//        }
//
//
//        /*
//         * 小程序授权后回调
//         *
//         * 用户授权后, 微信回调redirectUrl,推送 auth_code, 用 auth_code 换取用 user-info
//         */
//        String authCode = "queryauthcode@@@1L1bTI4UxCra7m8szs_zkhI8XEnuK8u9zzjdEyOQ3n3NgQZwMrnVFy67NnaAD8Qj9CI4DDFQGEF_US72FGNBsw";
//        AuthorizationInfo authorizationInfo = queryAuthorizationInfo(
//                authCode,
//                cpnAppId,
//                componentAccessToken
//        );
//        System.out.println("authorizationInfo.isNull () = " + authorizationInfo.isNull());
//
//        /*
//         * 刷新用户token(按情况)
//         */
//        AuthorizationInfo newAuthInfo = refreshAuthorizerToken(cpnAppId, componentAccessToken, authorizationInfo.getAuthorizerAppId(), authorizationInfo.getAuthorizerRefreshToken());
//        if (!newAuthInfo.isNull()) {
//            System.out.println("newAuthInfo.toString () = " + newAuthInfo.toString());
//        }
//
//
//        /*
//         * 获取用户用户info后,设置用户域名
//         * todo 未测通,没有上传小程序
//         */
//        String configDomainResult = configWebViewDomain(newAuthInfo.getAuthorizerAccessToken(),
//                "https://wxalocal.yujiejie1.com",
//                "https://wxalocal.yujiejie2.com",
//                "https://wxalocal.yujiejie3.com"
//        );
//        System.out.println("configDomainResult = " + configDomainResult);

        String sessionkey = acquireSessionkey( "https://local.yujiejie.com/jweixin/third/findSessionKeyBySessionId", "wxf99f985dc7f79695", "f9c027d4622c4a4f8362c0fdb459b4a9");
        System.out.println("sessionkey = " + sessionkey);
    }
}
