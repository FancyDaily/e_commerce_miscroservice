package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.enums.colligate.RedisKeyEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.ValueOperations;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.e_commerce.miscroservice.commons.utils.SmsUtils.YxBuilder.yxHttpHeader;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/24 10:28
 * @Copyright 玖远网络
 */
public class SmsUtils{

    private static Log logger = Log.getInstance(SmsUtils.class);

    public static final String SUCCESS = "\"code\":200";
    /**
     * 发送验证码
     */
    private static final String SMS_SEND_CODE_URL = "https://api.netease.im/sms/sendcode.action";
    /**
     * 校验验证码
     */
    private static final String SMS_VERIFY_CODE_URL = "https://api.netease.im/sms/verifycode.action";

    // 普通模板
    private static final String APP_KEY = "aaf6335640b65963fa91e3b308b0133a";
    private static final String APP_SECRET = "3db2ab33743b";
    // 验证码专用
    private static final String APP_KEY1 = "f4f6a01a98b58f89eeb2efa456f1280d";
    private static final String APP_SECRET1 = "a066ed4df363";
    // 通知模板
    private static final String APP_KEY2 = "30095ee7e6c2462d4114832fc3ff8c32";
    private static final String APP_SECRET2 = "2f66ab5cff7e";


    private static ValueOperations<String, Integer> singleRedis;

    private static ValueOperations<String, Integer> getRedis() {
        //这里就不加锁了,反正spring里也是单例
        if (singleRedis == null) {
            singleRedis = (ValueOperations<String, Integer>) ApplicationContextUtil.getBean ("intRedisTemplate");
        }
        return singleRedis;
    }


    public static boolean sendCodeSafe(RedisKeyEnum key, String phone) {
        //默认最大限制次数
        return sendCodeSafe (key, phone, 20, 1, TimeUnit.MINUTES);
    }



    public static boolean sendCodeSafe(RedisKeyEnum key, String phone, Integer maxCount, Integer validTime, TimeUnit timeUnit) {
        String redisKey = key.toKey () + ":" + phone;
        Integer count = getRedis().get(redisKey);

        Long expire = getRedis().getOperations().getExpire(redisKey);
        logger.info("key={},count={},expire={}", redisKey, count, expire);
        if (BeanKit.notNull (count)) {
            if (count >= maxCount) {
                logger.info ("推送短信验证码 key:{},使用次数超限count:{}",key, count);
                return Boolean.FALSE;
            }

            boolean isSuccess = SmsUtils.isSuccess (SmsUtils.sendCode (phone));
            if (isSuccess) {
                //increment暂时用了报错, 先覆盖
                getRedis().increment (redisKey, 1L);
                return Boolean.TRUE;
            }

        }

        boolean isSuccess = SmsUtils.isSuccess (SmsUtils.sendCode (phone));
        if (isSuccess) {
            //有效期一天
            getRedis().set (redisKey, 1, validTime, timeUnit);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }




    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @author Charlie
     * @date 2018/9/24 11:00
     */
    public static String sendCode(String phone) {
        Map<String, String> param = new HashMap<> (2);
        param.put ("mobile", phone);

        String result = HttpUtils.sendPost (SMS_SEND_CODE_URL, param, yxHttpHeader ());
        logger.info ("发送短信验证码 phone:{},result:{}", phone, result);
        return result;
    }


    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @author Charlie
     * @date 2018/9/24 11:00
     */
    public static boolean verifyCode(String phone, String code) {
//        DebugUtils.todo ("测试验证码");
//
//        if (true) {
//            return true;
//        }
        Map<String, String> param = new HashMap<> (2);
        param.put ("mobile", phone);
        param.put ("code", code);
        String result = HttpUtils.sendPost (SMS_VERIFY_CODE_URL, param, yxHttpHeader ());
        logger.info ("验证短信验证码 phone:{},result:{}", phone, result);
        return isSuccess (result);
    }


    /**
     * 服务调用是否成功
     *
     * @param result result
     * @return boolean
     * @author Charlie
     * @date 2018/9/24 11:16
     */
    public static boolean isSuccess(String result) {
        return result != null && result.contains (SUCCESS);
    }


    static class YxBuilder{

        /**
         * 云信默认请求消息头
         *
         * @return java.util.Map
         * @author Charlie
         * @date 2018/9/24 11:05
         */
        static Map<String, Object> yxHttpHeader() {
            Map<String, Object> headers = new HashMap<> (4);
            headers.put ("AppKey", APP_KEY1);
            String nonce = RandomStringUtils.randomAlphanumeric(20);
            headers.put ("Nonce", nonce);
            long curTime = System.currentTimeMillis ();
            headers.put ("CurTime", curTime);
            String checkSum = YxBuilder.getCheckSum (APP_SECRET1, nonce, curTime);
            headers.put ("CheckSum", checkSum);
            return headers;
        }

        // 计算并获取CheckSum
        private static String getCheckSum(String appSecret, String nonce, Long curTime) {
            return encode("sha1", appSecret + nonce + curTime);
        }

        private static String encode(String algorithm, String value) {
            if (value == null) {
                return null;
            }
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
                messageDigest.update(value.getBytes());
                return getFormattedText(messageDigest.digest());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static String getFormattedText(byte[] bytes) {
            int len = bytes.length;
            StringBuilder buf = new StringBuilder(len * 2);
            for (int j = 0; j < len; j++) {
                buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
                buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
            }
            return buf.toString();
        }

        private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f' };
    }


    public static void main(String[] args) {
        String phone = "13857513104";
        sendCode (phone);
        String code = 1574 + "";
        boolean verifyCode = verifyCode (phone, code);
        System.out.println ("verifyCode = " + verifyCode);
    }
}
