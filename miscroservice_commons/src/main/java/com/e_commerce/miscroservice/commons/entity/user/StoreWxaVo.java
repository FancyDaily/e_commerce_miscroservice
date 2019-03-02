package com.e_commerce.miscroservice.commons.entity.user;

import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/6 15:35
 * @Copyright 玖远网络
 */
@Data
public class StoreWxaVo{

    /**
     * 商家ID
     */
    private Long storeId;
    /**
     * 授权方APPID
     */
    private String appId;
    /**
     * 授权方昵称
     */
    private String nickName;
    /**
     * 授权方头像
     */
    private String headImg;
    /**
     * 授权方公众号的原始IDgh_eb5e3a772040
     */
    private String userName;
    /**
     * 授权方所设置的微信号，可能为空
     */
    private String alias;
    /**
     * 小程序二维码图片的URL
     */
    private String qrcodeUrl;
    /**
     * 公众号的主体名称，例：腾讯计算机系统有限公司
     */
    private String principalName;
    /**
     * 签名描述
     */
    private String signature;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 商户号
     */
    private String mchId;

    /**
     商户秘钥
     */
    private String payKey;


    /**
     * 小程序二维码图片的URL
     */
    private String authorizerInfoJson;

    /**
     * 证书地址
     */
    private String cerPath;

}
