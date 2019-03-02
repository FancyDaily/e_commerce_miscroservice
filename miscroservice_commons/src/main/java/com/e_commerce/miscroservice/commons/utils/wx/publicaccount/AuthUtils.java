package com.e_commerce.miscroservice.commons.utils.wx.publicaccount;

import com.alibaba.fastjson.JSON;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.HttpUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/28 13:27
 * @Copyright 玖远网络
 */
public class AuthUtils{

    private static final Log logger = Log.getInstance(AuthUtils.class);
    /**
     * 获取网页授权凭证
     *
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code 微信授权code
     * @return WeixinAouth2Token
     */
    public static Oauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
        Oauth2Token wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        requestUrl = requestUrl.replace("CODE", code);
        // 获取网页授权凭证
        String result = HttpUtils.sendGet (requestUrl);
        logger.info ("公账号使用code获取 requestUrl={},result={}", requestUrl, result);
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
        if (null != jsonObject) {
            try {
                wat = new Oauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInteger("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wat = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                logger.error ("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return wat;
    }





    /**
     * 通过网页授权获取用户信息
     *
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code 微信授权code
     * @return SNSUserInfo
     */
    public static SNSUserInfo getSNSUserInfo(String appId, String appSecret, String code) {
        Oauth2Token accessToken = getOauth2AccessToken (appId, appSecret, code);
        if (accessToken == null) {
            logger.error ("获取accessToken失败");
            return null;
        }

        if("snsapi_base".equals(accessToken.getScope())){
            SNSUserInfo snsUserInfo = new SNSUserInfo();
            snsUserInfo.setOpenId(accessToken.getOpenId());
            return snsUserInfo;
        }else {
            //snsapi_userinfo
            return getSNSUserInfo (accessToken.getAccessToken (), accessToken.getOpenId ());
        }
    }


    /**
     * 通过网页授权获取用户信息
     *
     * @param accessToken 网页授权接口调用凭证
     * @param openId 用户标识
     * @return SNSUserInfo
     */
    public static SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
        SNSUserInfo snsUserInfo = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 通过网页授权获取用户信息
        String result = HttpUtils.sendGet (requestUrl);
        logger.info ("公账号使用accessToken换用户信息 requestUrl={},result={}", requestUrl, result);
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
        if (null != jsonObject) {
            try {
                snsUserInfo = new SNSUserInfo();
                // 用户的标识
                snsUserInfo.setOpenId(jsonObject.getString("openid"));
                // 昵称
                snsUserInfo.setNickname(jsonObject.getString("nickname"));
                // 性别（1是男性，2是女性，0是未知）
                snsUserInfo.setSex(jsonObject.getInteger("sex"));
                // 用户所在国家
                snsUserInfo.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                snsUserInfo.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                snsUserInfo.setCity(jsonObject.getString("city"));
                // 用户头像
                snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                // 用户特权信息
                List<String> list = JSON.parseArray(jsonObject.getString("privilege"),String.class);
                snsUserInfo.setPrivilegeList(list);
                //与开放平台共用的唯一标识，只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
                snsUserInfo.setUnionid(jsonObject.getString("unionid"));
            } catch (Exception e) {
                snsUserInfo = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                logger.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return snsUserInfo;
    }


}
