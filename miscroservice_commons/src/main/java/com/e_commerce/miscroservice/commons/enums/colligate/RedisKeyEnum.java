package com.e_commerce.miscroservice.commons.enums.colligate;

/**
 * redis操作的key枚举类
 */
public enum RedisKeyEnum {

    /**
     * redis操作存储类的规范类
     * 公司名称:存储类型(hash/str/zset/..):存储的key名称(多个字母用_连接):key的类型(complete/prefix)
     */
    CODE_STR_REDIS_PREFIX_KEY("eTongJin:str:image_code:prefix:"),
    LIMITER_HASH_REDIS_PREFIX_KEY("eTongJin:hash:limiter_rate:prefix:"),
    //交易网关的redisKey的枚举
    GETWAY_DYNAMIC_CONFIG_KEY("eTongJin:hash:getway_dynamic_config:complete"),
    //用户登陆状态的枚举
    USER_LOAD_STATUS_KEY("eTongJin:hash:user_load_status:complete"),
    //公告中心
    NOTICE_KEY ( "message:center:notice:mainKey"),
    //公众号登录验证码
    PUBLIC_ACCOUNT_LOGIN_AUTH_CODE("publicAccount:login:authCode"),
    //公众号注册代理商
    PUBLIC_ACCOUNT_REGISTER_PROXY_AUTH_CODE("publicAccount:registerProxy:authCode"),
    /**
     * 公众号用户openId
     */
    PUBLIC_ACCOUNT_USER_WX ("publicAccount:user:openId"),
    /**
     * 店中店用户手机号绑定
     */
    IN_SHOP_USER_BIND_PHONE("sendCode:inShop:user:bindPhone"),

    /**
     * 直播间会话
     */
    LIVE_ROOM_TALK("live:room:talk:"),

    STORE_ORDER_PRE_PAY("store:order:prePay"),

    //官方通知
    OFFICIAL_KEY("message:center:official:mainKey");



    RedisKeyEnum(String key) {
        this.key = key;
    }

    private String key;

    public static RedisKeyEnum createKey(Integer type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            //店中店
            case 1:return IN_SHOP_USER_BIND_PHONE;

            default:return null;
        }
    }

    public String toKey() {

        return key;
    }

    public String append(Object object) {
        if (object == null) {
            throw new NullPointerException("请求参数不可为空");
        }
        return key + object.toString ();
    }


}
