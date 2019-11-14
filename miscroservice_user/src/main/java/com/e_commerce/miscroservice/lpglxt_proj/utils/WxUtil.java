package com.e_commerce.miscroservice.lpglxt_proj.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class WxUtil {

    private static final String APPID = WeixinUtil.APP_ID;
    private static final String OPENID_BASE_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    private static final String OPENID_PUBLIC_BASE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    private static final String ACCESSTOKEN_BASE_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String QRCODE_BASE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";
    private static final String SECRET = WeixinUtil.APP_SECRET;
    private static final String TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=%s";
    private static final String PUBLIC_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    private static final String JSTICKER_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static Map<String, Object> ACCESS_TOKEN_CACHE = new ConcurrentHashMap<>();


    /**
     * code转openId
     *
     * @param code 微信的code
     * @return
     */
    public static String code2OpenId(String code) {
        String stringToken = String.format(OPENID_BASE_URL, APPID, SECRET, code);
        String response = httpsRequestToString(stringToken, GET, null);
        return JSON.parseObject(response).getString("openid");
    }


    /**
     * code转公众号openId
     *
     * @param code 微信的code
     * @return
     */
    public static String code2PublicOpenId(String code) {
        String stringToken = String.format(OPENID_PUBLIC_BASE_URL, APPID, SECRET, code);

        String response = httpsRequestToString(stringToken, GET, null);
		System.out.println(response);
        return JSON.parseObject(response).getString("openid");
    }


    public static void main(String[] args) {
        System.out.println(code2PublicOpenId("021LgDLw1ScrJg01FwLw1mFJLw1LgDLf"));
//        String openId = "o8Qkiv7DqEl4lDioRQeReCcv2dSQ";
//        sendPublicMsg(openId, "url","sss", "wo",
//                "11","111", "11");
//        String token = getAccessToken();
//        System.out.println(token);
		sendPublicWaitMsg("ob5kc1St_xBVeAssuOQN3Qt4ua-Q","头部消息","订单号","订单类型","订单内容","点击查看详情","wwww.baidu.com");
//        JSONObject jsonObject = getUserList(token);
//        JSONObject data = jsonObject.getJSONObject("data");
//        JSONArray jsonArray = data.getJSONArray("openid");
//        String ab = "[\"ob5kc1UBuFkEzRjCOqva3un4IJkE\",\"ob5kc1dBCozMhzND51KuN0DLRODI\",\"ob5kc1VeAOVWlaCv8bWj3n_pNA84\",\"ob5kc1cjiXLdqNI2gQPdrllNQ5Ys\",\"ob5kc1QYxISdOVo6qAdPfw79DirM\",\"ob5kc1cZ8Df7IuAyXfMUquFBTiqQ\",\"ob5kc1XlqFXsTgjWITHJBiwNiD-g\",\"ob5kc1ZWuWEizVWcUvHpelHIKqnE\",\"ob5kc1XiG-BO2xYEPukcL6wbeXnM\",\"ob5kc1ay3zUNe8lKjdi2l8fjbkYI\",\"ob5kc1bprrridS91nEwDnEiO7vxU\",\"ob5kc1W_7E8p4Nx-G52aRXvYB5HU\",\"ob5kc1TedrUT4Q6CzdPFCF-UdsKA\",\"ob5kc1RZDSSY_ZMg5IOtODE3iiuo\",\"ob5kc1QRm7sz7mwux_nW1_T8xrVo\",\"ob5kc1TyDU5AR7-aljIOiCfomLH0\",\"ob5kc1X2ct-m1SDJh0eweDqlVWWA\",\"ob5kc1aNXkOJKpqjoQv8DbNImUrY\",\"ob5kc1ZOckA9JWbfHCYTxzfPY-oA\",\"ob5kc1ThAp0xKnou7tI_dQjY3yzg\",\"ob5kc1dAQK66kTd7h07x1OXiyt8w\",\"ob5kc1bpyOm3p4L1_SZK8dO3ambQ\",\"ob5kc1U8PtEJ6YwY5_JiP9ijK2SI\",\"ob5kc1ax-bsihUTEL6Qymyp8TRpQ\",\"ob5kc1QxOa-iBcy-N2-ORAL_6FP4\",\"ob5kc1aSI0tDCcaQGtN9dhliBbSU\",\"ob5kc1f2pmj00495yNSyMR8LswE8\",\"ob5kc1bpC6us0da24C4ICxPYI4aY\",\"ob5kc1QujZPkk6z3ZngJ9YrwCN7U\",\"ob5kc1aSMgyuNpbHz1NxMR1RVseU\",\"ob5kc1ZnAqQ0IRP2IvvYWmUAYHTw\",\"ob5kc1UzZTauphbC3tQHTwLe06l4\",\"ob5kc1aU2eYv_4rIhV6NDa7Vyji4\",\"ob5kc1eQFZMsoKXMGMJBHPLACr3w\",\"ob5kc1aieJGX0DzSENA8wQhC4QjU\",\"ob5kc1SMHv1dIPDBdLIgL4A3h0gI\",\"ob5kc1U7GTG-tE-VMVDaw9Uhixt4\",\"ob5kc1aJYaxDQkqTQlrN53OzRx24\",\"ob5kc1W0h4ySOqVByDi_ZCQ2cjS0\",\"ob5kc1VKplA57N34q-N2bC6vI2-M\",\"ob5kc1cNiqEpYHTtbdxiR8D-rmoY\",\"ob5kc1aI_jX-f8YT4g3-kbgDagb4\",\"ob5kc1UoPCy46-dhdzEXx90TDuDI\",\"ob5kc1ecKgPJu79XqSvBHIet6sVY\",\"ob5kc1fdQMb8k2ge3HU9eEc7MERE\",\"ob5kc1bLbTtEoTi8guhtuEuePzEU\",\"ob5kc1RnqbsojSxYh8gTtNqnODeo\",\"ob5kc1YjNymZEJzHpzXXs57Zw9Wc\",\"ob5kc1TyUzbtRFS6i3danySh9mJA\",\"ob5kc1fpqybvd9e2gXvL8DI-_oRE\",\"ob5kc1WLpX_Sr5fTt7Y_bKdv_jjE\",\"ob5kc1dt_wCNuZmdnTpqCxJQI72g\",\"ob5kc1YG0-tlYxCQe23iNre4EZt0\",\"ob5kc1TfcWg4lIbTZKupGKkPR0EI\",\"ob5kc1YKdP1eLA-FVvGZPu2o3e3s\",\"ob5kc1bkK-8aVIjuzTXQiJzkfMtk\",\"ob5kc1QYE5P83Q_O97Qqxigr5jFg\",\"ob5kc1ZZAHLO6ZINsrMeskAJVUCs\",\"ob5kc1XEeTQVmoVPFI2RoHNRqbXg\",\"ob5kc1V5k51XTepjQFV2pbQ94KK4\",\"ob5kc1eueLfein2f1E9MjFj6IxcY\",\"ob5kc1d4c8YtA8AtS-V_HKN4l2qM\",\"ob5kc1bGa0PLKITmtvfWFjGTPheU\",\"ob5kc1TohddvqxNiXvRIFu7u6XYg\",\"ob5kc1b57rJLZaXP47h3gvBDHSxs\",\"ob5kc1esjSiLqFkM6Eq1RJ_6SK-I\",\"ob5kc1fH5NNYmRT3ZEVYN8kDQXzs\",\"ob5kc1f8XGUMR_LuCw9ybuSRXV2E\",\"ob5kc1VGnQ_f-FzARH8GoXH0EPwI\",\"ob5kc1UwEeZF-WQWoYhqnHDDqQSU\",\"ob5kc1cMX71BtQoRo2Oj7IDZ2o14\",\"ob5kc1Uq7X6dqFBbAxIHOEf-klFc\",\"ob5kc1QcYZSdeyBTALbkrYfrYrNQ\",\"ob5kc1atxtVMuFtO_HJoFcF-hpIs\",\"ob5kc1XS5Bhg9IH4LoC3JFo3rlg4\",\"ob5kc1ZGLxP08-CZvRZC85Xef2s8\",\"ob5kc1S1-O-2bA6uVarxMSIqU44o\",\"ob5kc1d26utwpspRzeOjD0KNy5_A\",\"ob5kc1a5jzL-0CmTaLwTK2FehbK0\",\"ob5kc1UTUVHxQ0lGWhZPbJNcDGUM\",\"ob5kc1Rap9CMSdv0fdk-cLZf-vkA\",\"ob5kc1REFwcgybBpYVgc3w2Dp_VY\",\"ob5kc1V0Ws_sm-BuIGxJ83CB4s_A\",\"ob5kc1RdE0oGVWUPXK_L3PmvACQ8\",\"ob5kc1ZFB0vPZyRVFo5-T21rfios\",\"ob5kc1Z_XQeLpEV7E-4udZ5j7QcU\",\"ob5kc1YVdFiEmMd8m65XIBetOlWM\",\"ob5kc1QCswgPUPcahVSpWX3sYGVg\",\"ob5kc1S2EBwXgce4tU53UkCCFaCk\",\"ob5kc1RObVruVpgadSq1Jp5Uh28c\",\"ob5kc1S-Xf1eew-MNLyQFSofuIpQ\",\"ob5kc1SKUvknNHlFv2SSi9hqxCbE\",\"ob5kc1Q_Ubq5wh0upLF0MwiEJETA\",\"ob5kc1Un4t5uw-hd1D47OM8WPSY4\",\"ob5kc1damlZWsvBv6QDi3N54BStE\",\"ob5kc1eBDi5TAjawe-bevjvNjuqo\",\"ob5kc1csCW4ERiy9TTOn9b-WNkdU\",\"ob5kc1TeYybPmRsnbk0AqJ1-4c_k\",\"ob5kc1QzRcnt9vmwMj_pleFlj4NE\",\"ob5kc1Qx2oel-wckc3QYZ1boedmA\",\"ob5kc1bTZQa_zG2CzblQQ_pjUYws\",\"ob5kc1TXGxDZtQtyCBswbJTC9wAM\",\"ob5kc1SNxYmLQngSNNdZGr1xAx9I\",\"ob5kc1fg4Py5wxxAqROYcqxaZ8lg\",\"ob5kc1TllEp7Kazi4wj-1-i3aZnQ\",\"ob5kc1St_xBVeAssuOQN3Qt4ua-Q\",\"ob5kc1Vz1natePC3Jw17R2QoD00E\",\"ob5kc1SZkB5Fne7_1eX-pCnMyxsQ\",\"ob5kc1fcS_QTdaToWsb7ofHnsMmg\",\"ob5kc1fJo9oViHf1mDTURVSZZqco\",\"ob5kc1V1NW1BMub91Eh-r1w5IFVQ\",\"ob5kc1dtJAnj4Wr03sbZ00Dmb5wU\",\"ob5kc1UeDN4j6VVhoZAkh4stGrNw\",\"ob5kc1U8q04_E164zWCcJQzaIOo8\",\"ob5kc1fUEj0_lyOXK0JcJ2VMqsWw\",\"ob5kc1XKCE0X5Teyk4vk0amz3WtA\",\"ob5kc1YXdbutkV8-94yBcoY_hU6Y\",\"ob5kc1VNc5OfEumdq1iCscUvX5yQ\",\"ob5kc1S800nxGqndOC31w5mmZ7RQ\",\"ob5kc1cUBZWTBQZ84bzyKxIeN5XE\",\"ob5kc1VG63LnTxpv7NkoqYWBRI6A\",\"ob5kc1UpRd7nuFw7coj8bAbttBdI\",\"ob5kc1Xt4V7iWsIeYcReF_M0908E\",\"ob5kc1UAADgvPQ6ZBUazflGn7pxg\",\"ob5kc1SlMczCx6fscEGZOGPzgCtQ\",\"ob5kc1fhrQlB9uc1vYboaLAeYVPQ\",\"ob5kc1YOOqqHdi3bxKb99VY8vBJE\",\"ob5kc1Wl8EpZ2W1UfN5XTENCf-TU\",\"ob5kc1YGpMWLdg-NA5Je3uozcmNI\",\"ob5kc1Tfray9QrbsmgweVcIK-8Pk\",\"ob5kc1buirE3X7UhSLgT73_kcjTM\",\"ob5kc1V6OBLdKe4bb0kQ6EoKCrtM\",\"ob5kc1fGvHiGUruWaBxvnAd4ONzM\",\"ob5kc1WzER6ExD9GE_9uEghlaK3w\",\"ob5kc1WW25W7yzvhXN7EsSkfjAUk\",\"ob5kc1Qs80Bw2OIeWV-SCf48dAKY\",\"ob5kc1f_HWq0YuouwIkkWxaXUYig\",\"ob5kc1bBTAtJNHHlmeVnTP1kAlIA\",\"ob5kc1TRhYhLciBP-7NNVIun6VFY\",\"ob5kc1b4FlD50XY1iVUMYdfOiVMM\",\"ob5kc1YqEpxdne1VZBnVxXU2-vZk\",\"ob5kc1QjcWajRwfP8-4_-_8S4hoI\",\"ob5kc1Vjlp-Xl9BwKO07nQ1bUBDA\",\"ob5kc1RMz1bsjGpUbNG8fV-ixNuI\",\"ob5kc1b6Hl3q3B_sycdzpywm3Y-A\",\"ob5kc1QCDGHfBlPku8qlrTxEeSSo\",\"ob5kc1UxnkU7T1zVaAGGFjjHjf-s\",\"ob5kc1f6hjg9wDQyV9gw_yHs8tYg\",\"ob5kc1SWVN3XB_nPZ272liQSIrcQ\",\"ob5kc1VAdHP2pWkG5JSX96J_ivDA\",\"ob5kc1UGGprYCphVvTHk27Hdhlmo\",\"ob5kc1Q3la7gcAxqQtgtV-u-f3_U\",\"ob5kc1XS_D4dHglEolS5b1CjBF1k\",\"ob5kc1VzcDTY1dsqMsEz8Dr5gXME\",\"ob5kc1XGIz8K0EMSM7sD5dekQrdE\",\"ob5kc1eraP9m0PlLk4huYi9_W1Pg\",\"ob5kc1Swr45SAbQ3V8D8n9KpB3i0\",\"ob5kc1U5Gpal_jogqW-G1gfCE3rc\",\"ob5kc1enZamcF3Wtl5MCwL9dtCKo\",\"ob5kc1eGU80Bpt2s0cTz1wrc0EFQ\",\"ob5kc1fAVTVKzBegprBDOBRTRe4Q\",\"ob5kc1ZxhH5yo32980xI4Xhf6YMs\",\"ob5kc1UzBcVn5tnGuQrR5TbMdi98\",\"ob5kc1eNoSscvvuqQiqDXQlRZKKk\",\"ob5kc1XloTvDyh1MtR2Y-hqa8vqs\",\"ob5kc1T-KYnyMraoEiqge_2XimBQ\",\"ob5kc1fWGdmZqpTE1EfwR_McPo6s\",\"ob5kc1S7k18WStmPaN91el7fPxC0\",\"ob5kc1R4aYgUjQI7qUPwxFwrD8C0\",\"ob5kc1b0c3hYhZz3w3hq32q1Ty2A\",\"ob5kc1VgPk-yrhvdRZi0Yp5jIB6s\",\"ob5kc1TsiwqYzeG8qRRc-N6FGYc4\",\"ob5kc1R9_jvg5DVwnuF54LIbXl68\",\"ob5kc1drjo29DiOxHSHYL8rQ4n4M\",\"ob5kc1T7fvt7mt8YvQLyw5D-2cdU\",\"ob5kc1dR1n38T9Q9oyeEbg0oB1gI\",\"ob5kc1RS6eknbngUxSL7AE4AhqGU\",\"ob5kc1V7FSbMeKjG9pJOdzZmC_Qw\",\"ob5kc1XN3QvhiuShjj6-ibQMDoZY\",\"ob5kc1XAJNaaRtkwY64LwhBrRRkM\",\"ob5kc1fnvB6BEFRfvGD21nqjsmLk\",\"ob5kc1Zcxyl8usJQmcOlwVUQF2LI\",\"ob5kc1bDPC17NexieOYvPtaE6bUI\",\"ob5kc1U9q3QiGK2SA5F-EvKrRI5E\",\"ob5kc1aEmO8KuQoS39nTtisOl04s\",\"ob5kc1X19Pwr6IfJBSaM6jC-eDxo\",\"ob5kc1VKJRv3g2BGOvtOU0qE8Fgk\",\"ob5kc1RtFPtvOcEV-7NXLfiAF5Bg\",\"ob5kc1VBxYd4jHCNVKgZftwxE1RA\",\"ob5kc1Vm_r1ciace8ia-hNDB20Mg\",\"ob5kc1Sap0Ik_wGRWbJL7JBLk264\",\"ob5kc1T00u2OpgJU4OV-lEm-V2W0\",\"ob5kc1ReJ5p9rQ7t6-xfXEhGHOgw\",\"ob5kc1e3JnL7kY34Dj87-QvCXJiQ\",\"ob5kc1Y0f85ajDSXXAd-gd1FbWuk\"]";
//        JSONArray array = JSONArray.parseArray(ab);
//        for (Object o : array) {
//            JSONObject userInfor = null;
////        System.out.println(array);
//            try {
//                userInfor = getUserInformation("ob5kc1St_xBVeAssuOQN3Qt4ua-Q", token);
//            } catch (Exception e) {
//                for (; ; ) {
//                    userInfor = getUserInformation(o.toString(), token);
//                    if (userInfor != null) {
//                        System.out.println("继续");
//                        continue;
//                    }
//                }
//            }
//
//            System.out.println(userInfor);
//        }
//       JSONObject block =  getBlackUserInformation(token);
//        System.out.println(userInfor);
    }

    /**
     * 获取jsapi的票据
     *
     * @return
     */
    private static String getTicket() {
        String ticketKey = "JSAPI_TICKET";
        String ticketTokenKey = ticketKey + "_TOKEN";
        String ticket;
        Object val = ACCESS_TOKEN_CACHE.get(ticketTokenKey);
        if (val != null) {
            if (Long.valueOf(val.toString()) - System.currentTimeMillis() > 0) {
                Object _token = ACCESS_TOKEN_CACHE.get(ticketTokenKey);
                if (_token != null) {
                    ticket = _token.toString();
                    return ticket;
                }

            }

        }

        String tickerUrl = String.format(JSTICKER_URL, getAccessToken());

        String result = httpsRequestToString(tickerUrl, GET, "");

        JSONObject json = JSONObject.parseObject(result);

        ticket = json.getString("ticket");

        Long expireTime = json.getLong("expires_in") + System.currentTimeMillis() - 100;
        ACCESS_TOKEN_CACHE.put(ticketKey, ticket);
        ACCESS_TOKEN_CACHE.put(ticketTokenKey, expireTime);


        return ticket;


    }

    /**
     * 获取二维码图片
     *
     * @param openId
     * @return
     */
    public static File getQRcode(String openId) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scene", openId);
        String qrCodeUrl = String.format(QRCODE_BASE_URL, getAccessToken());

        return httpsRequestToFile(qrCodeUrl, POST, jsonObject.toJSONString());


    }

    /**
     * 公众号发送通知
     *
     * @param openId      用户Id
     * @param tipMsg      提醒的标示
     * @param productName 商品名称
     * @param payMoney    支付金额
     */
    public static void sendPublicMsg(String openId,
                                     String tipMsg, String productName,
                                     String payMoney, String payerName, String orderNo, String url,String templateId) {

		if (templateId==null){
			templateId = "rCJDaNnvJcS_GJX_DQ9gabYIkvrAg_LalH7s8oBuMoQ";

		}
        Map<String, TemplateData> map = new HashMap<>();
        map.put("first", new TemplateData(tipMsg));
        map.put("keyword1", new TemplateData(productName));
        map.put("keyword2", new TemplateData(payMoney));
        map.put("keyword3", new TemplateData(payerName));
        map.put("keyword4", new TemplateData(format.format(LocalDateTime.now())));
        map.put("keyword5", new TemplateData(orderNo));
        map.put("keyword6", new TemplateData("点击查看详情"));


        publicNotify(openId, templateId, url, map);


    }
	/**
	 * 公众号发送通知
	 * @param openId
	 * @param firstData
	 * @param orderNo
	 * @param orderType
	 * @param content
	 * @param lastData
	 * @param url
	 */
    public static void sendPublicWaitMsg(String openId,
                                     String firstData, String orderNo, String orderType,String content,String lastData,String url
                                     ) {

    	String templateId = "IbNz7SuGLwQqQz7br-EXPGsXwoXS4Yv0fVApJ3gSfMI";

        Map<String, TemplateData> map = new HashMap<>();
        map.put("first", new TemplateData(firstData));
        map.put("keyword1", new TemplateData(orderNo));
        map.put("keyword2", new TemplateData(orderType));
        map.put("keyword3", new TemplateData(format.format(LocalDateTime.now())));
        map.put("keyword4", new TemplateData(content));
        map.put("remark", new TemplateData(lastData));


        publicNotify(openId, templateId, url, map);


    }

    public static JSONObject getUserList(String token) {
//        https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN
        if (StringUtils.isEmpty(token)) {
            token = getAccessToken();
        }
        String str = httpsRequestToString("https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + token, GET, null);
        JSONObject jsonObject = JSONObject.parseObject(str);
        return jsonObject;
    }

    public static JSONObject getUserInformation(String openId, String token) {
        if (StringUtils.isEmpty(token)) {
            token = getAccessToken();
        }
        String userInformation = httpsRequestToString("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + openId + "&lang=zh_CN", GET, null);
        JSONObject jsonObject = JSONObject.parseObject(userInformation);
        return jsonObject;
    }

    public static JSONObject getBlackUserInformation(String token) {
        if (StringUtils.isEmpty(token)) {
            token = getAccessToken();
        }
        String userInformation = httpsRequestToString("https://api.weixin.qq.com/cgi-bin/tags/members/getblacklist?access_token=" + token , POST, new JSONObject().toJSONString());
        JSONObject jsonObject = JSONObject.parseObject(userInformation);
        return jsonObject;
    }

    public static void publicNotify(String openId, String templateId, String url, Map<String, TemplateData> map) {
        JSONObject params = new JSONObject();
        params.put("touser", openId);
        params.put("template_id", templateId);
        params.put("topcolor", "#00DD00");
        params.put("url", url);
        params.put("data", map);
        httpsRequestToString(String.format(PUBLIC_TEMPLATE_URL, getAccessToken()), POST, params.toJSONString());
    }

    /**
     * 小程序发送通知
     *
     * @param openId
     * @param formId
     * @param payMoney
     * @param orderNo
     * @param payCount
     * @param address
     */
    public static void sendPromoteMsg(String openId,
                                      String formId, String payMoney,
                                      String orderNo, String payCount, String address) {

        String templateId = "rCJDaNnvJcS_GJX_DQ9gabYIkvrAg_LalH7s8oBuMoQ";

        Map<String, TemplateData> map = new HashMap<>();
        map.put("keyword1", new TemplateData(orderNo));
        map.put("keyword2", new TemplateData(payCount));
        map.put("keyword3", new TemplateData(payMoney));
        map.put("keyword4", new TemplateData(format.format(LocalDateTime.now())));
        map.put("keyword5", new TemplateData("15355001281"));
        map.put("keyword6", new TemplateData(address));
        sendMsg(openId, templateId, formId, map);

    }

    private static void sendMsg(String openId, String templateId, String formId, Map<String, TemplateData> data) {
        JSONObject params = new JSONObject();
        params.put("touser", openId);
        params.put("template_id", templateId);
        params.put("form_id", formId);
        params.put("page", "/pages/my/my");
        params.put("data", data);
        String url = String.format(TEMPLATE_URL, getAccessToken());
        String get = httpsRequestToString(url, POST, params.toJSONString());

    }

    /**
     * 获取统一token
     *
     * @return
     */
    private static String getAccessToken() {
        String result;
        String token = "access_token";
        String tokenExpire = token + "_expire";
        Object val = ACCESS_TOKEN_CACHE.get(tokenExpire);
        if (val != null) {

            if (Long.valueOf(val.toString()) - System.currentTimeMillis() > 0) {
                Object _token = ACCESS_TOKEN_CACHE.get(token);
                if (_token != null) {
                    result = _token.toString();
                    return result;
                }

            }

        }

        String tokenUrl = String.format(ACCESSTOKEN_BASE_URL, APPID, SECRET);

        String response = httpsRequestToString(tokenUrl, GET, null);
        JSONObject json = JSON.parseObject(response);
        result = json.getString(token);
        ACCESS_TOKEN_CACHE.put(token, result);
        ACCESS_TOKEN_CACHE.put(tokenExpire, System.currentTimeMillis() + json.getLong("expires_in") * 1000);

        return result;

    }

    private static HttpsURLConnection baseHttpsRequest(String path, String method, String body) {
        if (path == null || method == null) {
            return null;
        }
        HttpsURLConnection conn = null;
        try {
            // 创建SSLConrext对象，并使用我们指定的信任管理器初始化
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            TrustManager[] tm = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

            }};
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上面对象中得到SSLSocketFactory
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(path);
//            conn = (HttpsURLConnection) url.openConnection(
//                    new Proxy(Proxy.Type.HTTP
//                            , new InetSocketAddress("47.96.70.20", 3129)));

			conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            // 设置请求方式（get|post）
            conn.setRequestMethod(method);

            // 有数据提交时
            if (null != body) {
                if (!body.contains("&")) {
                    //json
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                }
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(body.getBytes(StandardCharsets.UTF_8));
                outputStream.close();
            }


        } catch (Exception e) {
            e.printStackTrace();

        } finally {


        }
        return conn;
    }

    private static String httpsRequestToString(String path, String method, String body) {

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String response = "";
        HttpsURLConnection conn = null;
        InputStream inputStream = null;
        try {
            conn = baseHttpsRequest(path, method, body);
            if (conn == null) {
                return response;
            }
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            response = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (conn != null) {
                conn.disconnect();
            }
            try {
                if (bufferedReader != null) {

                    bufferedReader.close();
                }
                if (inputStreamReader != null) {

                    inputStreamReader.close();
                }
                if (inputStream != null) {

                    inputStream.close();
                }
            } catch (IOException execption) {

            }
        }
        return response;
    }

    private static File httpsRequestToFile(String path, String method, String body) {

        OutputStream os = null;
        BufferedInputStream bis = null;
        String randomFileName = UUID.randomUUID().toString();
        File tmpFile = null;
        HttpsURLConnection conn = null;
        InputStream inputStream = null;
        try {
            conn = baseHttpsRequest(path, method, body);
            if (conn == null) {
                return tmpFile;
            }

            tmpFile = File.createTempFile(randomFileName, randomFileName);
            inputStream = conn.getInputStream();
            bis = new BufferedInputStream((inputStream));
            os = new FileOutputStream(tmpFile);
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                os.write(arr, 0, len);
                os.flush();
            }
            os.close();
        } catch (Exception e) {

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            try {
                os.close();
                bis.close();
                inputStream.close();
            } catch (IOException execption) {

            }
        }
        return tmpFile;
    }

    private static String shaEncode(String inStr) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * sha1加密
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String sha1(String data) throws NoSuchAlgorithmException {
        //加盐   更安全一些

        //信息摘要器                                算法名称
        MessageDigest md = MessageDigest.getInstance("SHA1");
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //使用指定的字节来更新我们的摘要
        md.update(b);
        //获取密文  （完成摘要计算）
        byte[] b2 = md.digest();
        //获取计算的长度
        int len = b2.length;
        //16进制字符串
        String str = "0123456789abcdef";
        //把字符串转为字符串数组
        char[] ch = str.toCharArray();

        //创建一个40位长度的字节数组
        char[] chs = new char[len * 2];
        //循环20次
        for (int i = 0, k = 0; i < len; i++) {
            byte b3 = b2[i];//获取摘要计算后的字节数组中的每个字节
            // >>>:无符号右移
            // &:按位与
            //0xf:0-15的数字
            chs[k++] = ch[b3 >>> 4 & 0xf];
            chs[k++] = ch[b3 & 0xf];
        }

        //字符数组转为字符串
        return new String(chs);
    }

    /**
     * 获取分享请求参数
     *
     * @return
     * @throws Exception
     */
    public Map<String, String> createJSAPiParam(String url) {
        SortedMap<String, String> param = new TreeMap<>();
        param.put("jsapi_ticket", getTicket());
        String ten_time = String.valueOf(System.currentTimeMillis());
        param.put("timestamp", ten_time.substring(0, 10));
        param.put("nonceStr", ten_time);
        param.put("url", url);
        param.put("signature", sign(param));
        param.put("appId", WeixinUtil.APP_ID);

        System.out.println("获取参数" + param);

        return param;

    }

    private String sign(Map<String, String> param) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entries : param.entrySet()) {
            if (sb.length() != 0) {
                sb.append("&");
            }
            sb.append(entries.getKey().toLowerCase());
            sb.append("=");
            sb.append(entries.getValue());

        }

        return shaEncode(sb.toString());


    }

    /**
     * 小程序发送的实体类
     */
    public static class TemplateData {

        private String value;


        public TemplateData(String value) {
            this.value = value;
        }

        public TemplateData() {

        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }


}
