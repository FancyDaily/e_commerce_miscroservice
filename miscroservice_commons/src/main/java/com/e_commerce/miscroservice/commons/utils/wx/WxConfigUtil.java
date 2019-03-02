package com.e_commerce.miscroservice.commons.utils.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信配置管理
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/18 19:05
 * @Copyright 玖远网络
 */
public class WxConfigUtil{

    private static final Logger LOGGER = LoggerFactory.getLogger (WxConfigUtil.class);
    private static final String EMPTY_STR = "";
    private static final String OK = "\"errmsg\":\"ok\"";
    /**
     * 绑定微信试用人员
     */
    private static final String BIND_TEST_URL = "https://api.weixin.qq.com/wxa/bind_tester";
    /**
     * 解绑微信试用人员
     */
    private static final String UN_BIND_TEST_URL = "https://api.weixin.qq.com/wxa/unbind_tester";
    /**
     * 获取用户类目
     */
    private static final String GET_CATEGORY_URL = "https://api.weixin.qq.com/wxa/get_category";
    /**
     * 获取用户小程序体验的二维码
     */
    private static final String QRCODE_URL = "https://api.weixin.qq.com/wxa/get_qrcode";
    /**
     * 获取用户小程序正式发布的二维码
     */
    private static final String WXA_CODE = "https://api.weixin.qq.com/wxa/getwxacode";
    /**
     * 小程序页面配置
     */
    private static final String PAGE_URL = "https://api.weixin.qq.com/wxa/get_page";
    /**
     * 提交审核
     */
    private static final String SUBMIT_AUDIT_URL = "https://api.weixin.qq.com/wxa/submit_audit";
    /**
     * 提交审核
     */
    private static final String LATEST_AUDITSTATUS_URL = "https://api.weixin.qq.com/wxa/get_latest_auditstatus";
    /**
     * 配置小程序域名
     */
    private static final String SET_WEB_VIEW_DOMAIN_URL = "https://api.weixin.qq.com/wxa/setwebviewdomain";
    /**
     * 修改域名
     */
    private static final String MODIFY_DOMAIN_URL = "https://api.weixin.qq.com/wxa/modify_domain";
    /**
     * 提交代码
     */
    private static final String COMMIT_URL = "https://api.weixin.qq.com/wxa/commit";
    /**
     * 获取审核状态
     */
    private static final String AUDIT_STATUS_URL = "https://api.weixin.qq.com/wxa/get_auditstatus";
    /**
     * 发布
     */
    private static final String RELEASE_URL = "https://api.weixin.qq.com/wxa/release";
    /**
     * 修改小程序线上代码的可见状态
     */
    private static final String CHANGE_VISIT_STATUS_URL = "https://api.weixin.qq.com/wxa/change_visitstatus";


    /**
     * 上传小程序代码  原系统上传小程序code的业务流程, 这里没有对异常的处理
     *
     * @param appId           用户appId
     * @param authorizerToken token
     * @param storeId         门店用户id
     * @param storeName       用户商户名
     * @param templateId      模版id
     * @param version         新的小程序版本号
     * @param description     新的小程序的描述
     * @param testerId        试用人员微信号
     * @param domains         域名
     * @param domainVo        修改小程序配置服务器地址的域名
     * @return 体验小程序的体验二维码
     * @author Charlie
     * @date 2018/9/19 11:49
     */
    public static String commitTest(String appId,
                                String authorizerToken,
                                String storeId,
                                String storeName,
                                String templateId,
                                String version,
                                String description,
                                String testerId,
                                String[] domains,
                                DomainVo domainVo
    ) {
        /*
         * 获取默认配置
         */
        String userAppConfig = userAppConfig (appId, storeId, storeName, version);
        /*
         * 提交code
         */
        String commitResult = commit (authorizerToken, templateId, version, description, userAppConfig);
        System.out.println (commitResult.contains (OK));
        /*
         * 配置域名
         */
        String configWebViewDomain = configWebViewDomain (authorizerToken, domains.clone ());
        System.out.println (configWebViewDomain.contains (OK));
        /*
         * 修改小程序配置服务器地址
         */
        String modifyDomain = modifyDomain (authorizerToken, domainVo, OperDomain.SET);
        System.out.println (modifyDomain.contains (OK));
        /*
         * 绑定微信用户为小程序体验者
         */
        if (StringUtils.isNotBlank (testerId)) {
            bindTester (authorizerToken, testerId);
        }
        //體驗版小程序二維碼
        return experienceQrcode (authorizerToken);
    }



    public enum OperDomain{
        ADD("add"),
        DELETE("delete"),
        /**
         * 覆盖
         */
        SET("set"),
        GET("get");
        private String operCode;
        OperDomain(String operCode) {
            this.operCode = operCode;
        }
    }

    /**
     * 修改域名
     *
     * @param authorizerToken authorizerToken
     * @param domainVo        domainVo
     * @param oper            add添加, delete删除, set覆盖, get获取。当参数是get时不需要填四个域名字段
     * @return java.lang.String <p>{"errcode":0,"errmsg":"ok","requestdomain":["https:\/\/wxatest.yujiejie.com","https:\/\/weixintest.yujiejie.com"],"wsrequestdomain":["wss:\/\/wxatest.yujiejie.com","wss:\/\/weixintest.yujiejie.com"],"uploaddomain":["https:\/\/wxatest.yujiejie.com","https:\/\/weixintest.yujiejie.com"],"downloaddomain":["https:\/\/wxatest.yujiejie.com","https:\/\/weixintest.yujiejie.com"]}</p>
     * @author Charlie
     * @date 2018/9/19 12:42
     */
    public static String modifyDomain(String authorizerToken, DomainVo domainVo, OperDomain oper) {
        if (oper == null) {
            return EMPTY_STR;
        }
        String url = MODIFY_DOMAIN_URL + "?access_token=" + authorizerToken;
        Map<String, Object> paramMap = new HashMap<> ();
        paramMap.put ("action", oper.operCode);
        if (oper == OperDomain.GET) {
            String[] empty = new String[0];
            paramMap.put ("requestdomain", empty);
            paramMap.put ("wsrequestdomain", empty);
            paramMap.put ("uploaddomain", empty);
            paramMap.put ("downloaddomain", empty);
        }
        else {
            paramMap.put ("requestdomain", domainVo.getRequestDomainArr ());
            paramMap.put ("wsrequestdomain", domainVo.getWsRequestDomainArr ());
            paramMap.put ("uploaddomain", domainVo.getUploadDomainArr ());
            paramMap.put ("downloaddomain", domainVo.getDownloadDomainArr ());
        }
        String param = JSONObject.toJSONString (paramMap);
        String result = HttpClientUtils.post (url, param);
        LOGGER.info ("提交小程序代码 url:{},param:{},result:{}", url, param, result);
        return result;
    }


    /**
     * 上床code
     *
     * @param authorizerToken authorizerToken
     * @param templateId      templateId
     * @param version         version
     * @param description     description
     * @param userAppConfig   userAppConfig
     * @return java.lang.String <p>{"errcode":0,"errmsg":"ok"}</p>
     * @author Charlie
     * @date 2018/9/19 12:28
     */
    public static String commit(String authorizerToken, String templateId, String version, String description, String userAppConfig) {
        String url = COMMIT_URL +"?access_token=" + authorizerToken;
        Map<String, String> paramMap = new HashMap<String, String> ();
        //代码库中的代码模版ID
        paramMap.put ("template_id", templateId);
        paramMap.put ("ext_json", userAppConfig);
        //代码版本号，开发者可自定义
        paramMap.put ("user_version", version);
        //代码描述，开发者可自定义
        paramMap.put ("user_desc", description);
        String param = JSONObject.toJSONString (paramMap);
        String commitResult = HttpClientUtils.post (url, param);
        LOGGER.info ("上传小程序代码---- url:{},param:{},commitResult:{}", url, param, commitResult);
        return commitResult;
    }


    /**
     * 配置小程序域名
     *
     * @param authorizerToken authorizerToken
     * @param domains         域名
     * @return java.lang.String <p>{"errcode":0,"errmsg":"ok","webviewdomain":["https:\/\/wxatest.yujiejie.com"]}</p>
     * @author Charlie
     * @date 2018/9/18 12:46
     */
    public static String configWebViewDomain(String authorizerToken, String... domains) {
        if (domains == null || domains.length == 0) {
            return EMPTY_STR;
        }

        String url = SET_WEB_VIEW_DOMAIN_URL + "?access_token=" + authorizerToken;
        Map<String, Object> paramMap = new HashMap<> (2);
        paramMap.put ("action", "add");
        paramMap.put ("webviewdomain", domains.clone ());
        String param = JSONObject.toJSONString (paramMap);
        //{"errcode":61007,"errmsg":"api is unauthorized to component hint: [SIUf06783064]"}
        String result = HttpClientUtils.post (url, param);
        LOGGER.info ("配置小程序业务域名 url:{},param:{},result{}", url, param, result);
        return result;
    }


    /**
     * 添加体验者
     *
     * @param authorizerToken 用户访问的token
     *                        {@link WxAuthUtil#queryAuthorizationInfo(String, String, String)}
     *                        {@link WxAuthUtil#refreshAuthorizerToken(String, String, String, String)}
     * @param testerId        微信名
     * @return 是否成功
     * <p> {"errcode":85001,"errmsg":"user not exist or user cannot be searched hint: [vQl8HA08350729]"} </p>
     * <p> {"errcode":0,"errmsg":"ok","userstr":"75e66abce5e224e7232c9fb1a39b465427719ddbe9d808040dfad7386cce1569"} </p>
     * @author Charlie
     * @date 2018/9/15 22:04
     */
    public static boolean bindTester(String authorizerToken, String testerId) {
        if (StringUtils.isBlank (authorizerToken)) {
            LOGGER.info ("绑定微信用户为小程序体验者authorizer_token为空，请排查问题！！！！");
            return false;
//            return new HashMap<> (0);
        }
        String url = BIND_TEST_URL + "?access_token=" + authorizerToken;
        Map<String, Object> paramMap = new HashMap<> ();
        paramMap.put ("wechatid", testerId);
        String param = JSONObject.toJSONString (paramMap);
        LOGGER.info ("绑定微信用户为小程序体验者, url[{}].param[{}]", url, param);
        String binderResult = HttpClientUtils.post (url, param);
        LOGGER.info ("绑定微信用户为小程序体验者, binderResult[{}]", binderResult);

        return StringUtils.isNotBlank (binderResult) && binderResult.contains (OK);
    }


    /**
     * 添加体验者
     *
     * @param authorizerToken 用户访问的token
     *                        {@link WxAuthUtil#queryAuthorizationInfo(String, String, String)}
     *                        {@link WxAuthUtil#refreshAuthorizerToken(String, String, String, String)}
     * @param testerId        微信名
     * @return 是否成功
     * <p> {"errcode":85001,"errmsg":"user not exist or user cannot be searched hint: [vQl8HA08350729]"} </p>
     * <p> {"errcode":0,"errmsg":"ok","userstr":"75e66abce5e224e7232c9fb1a39b465427719ddbe9d808040dfad7386cce1569"} </p>
     * @author Charlie
     * @date 2018/9/15 22:04
     */
    public static boolean unBindTester(String authorizerToken, String testerId) {
        if (StringUtils.isBlank (authorizerToken)) {
            LOGGER.info ("绑定微信用户为小程序体验者authorizer_token为空，请排查问题！！！！");
            return false;
        }
        String url = UN_BIND_TEST_URL + "?access_token=" + authorizerToken;
        Map<String, Object> paramMap = new HashMap<> ();
        paramMap.put ("wechatid", testerId);
        String param = JSONObject.toJSONString (paramMap);
        LOGGER.info ("解绑微信用户为小程序体验者, url[{}].param[{}]", url, param);
        String result = HttpClientUtils.post (url, param);
        LOGGER.info ("解绑微信用户为小程序体验者, result[{}]", result);
        return StringUtils.isNotBlank (result) && result.contains (OK);
    }


    @Data
    @AllArgsConstructor
    public static class CategoryVo{
        private boolean isOk;
        /**
         * 分类,json数组字符串
         */
        private List<Map<String, Object>> categoryList;
    }


    /**
     * 查看类目
     *
     * @param authorizerToken authorizerToken
     *                        {@link WxAuthUtil#queryAuthorizationInfo(String, String, String)}
     *                        {@link WxAuthUtil#refreshAuthorizerToken(String, String, String, String)}
     * @return com.test.config.util.WxConfigUtil.CategoryVo
     * @author Charlie
     * @date 2018/9/18 20:05
     */
    public static CategoryVo findCategory(String authorizerToken) {
        String url = GET_CATEGORY_URL + "?access_token=" + authorizerToken;
        String findCategoryResult = HttpClientUtils.get (url);
        LOGGER.info ("查询小程序用户类项目 url:{},findCategoryResult:{}", url, findCategoryResult);
        if (StringUtils.isNotBlank (findCategoryResult) && findCategoryResult.contains (OK)) {
            HashMap<String, String> retMap = JSON.parseObject (findCategoryResult, new TypeReference<HashMap<String, String>> (){});
            return new CategoryVo (Boolean.TRUE, JSON.parseObject (retMap.get ("category_list"), new TypeReference<ArrayList<Map<String, Object>>> (){}));
        }
        return new CategoryVo (Boolean.FALSE, null);
    }


    /**
     * 获取小程序的体验二维码
     *
     * @param authorizerToken authorizerToken
     *                        {@link WxAuthUtil#queryAuthorizationInfo(String, String, String)}
     *                        {@link WxAuthUtil#refreshAuthorizerToken(String, String, String, String)}
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/18 20:10
     */
    public static String experienceQrcode(String authorizerToken) {
        return QRCODE_URL + "?access_token=" + authorizerToken;
    }


    /**
     * 获取小程序的体验二维码
     *
     * @param authorizerToken authorizerToken
     *                        {@link WxAuthUtil#queryAuthorizationInfo(String, String, String)}
     *                        {@link WxAuthUtil#refreshAuthorizerToken(String, String, String, String)}
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/18 20:10
     */
    public static String onlineQrcode(String authorizerToken) {
        return WXA_CODE + "?access_token=" + authorizerToken;
    }


    /**
     * 查看页面的配置
     *
     * @param authorizerToken authorizerToken
     *                        {@link WxAuthUtil#queryAuthorizationInfo(String, String, String)}
     *                        {@link WxAuthUtil#refreshAuthorizerToken(String, String, String, String)}
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/18 20:24
     */
    public static String findPage(String authorizerToken) {
        String url = PAGE_URL + "?access_token=" + authorizerToken;
        return HttpClientUtils.get (url);
    }


    /**
     * 提交审核
     *
     * @param authorizerToken authorizerToken
     * @return java.lang.Object
     * @author Charlie
     * @date 2018/9/18 20:34
     */
    public static String submitAudit(String authorizerToken) {
        CategoryVo category = findCategory (authorizerToken);
        if (! category.isOk ()) {
            return EMPTY_STR;
        }

        List<Map<String, Object>> categoryList = category.getCategoryList ();
        if (ObjectUtils.isEmpty (category)) {
            return EMPTY_STR;
        }

        Map<String, Object> categoryMap = categoryList.get (0);
        String firstId = String.valueOf (categoryMap.get ("first_id"));
        String firstClass = (String) categoryMap.get ("first_class");
        String secondId = String.valueOf (categoryMap.get ("second_id"));
        String secondClass = (String) categoryMap.get ("second_class");

        List<Map<String, Object>> itemList = new ArrayList<> ();
        Map<String, Object> item1 = new HashMap<String, Object> ();
        item1.put ("address", "pages/index/index");
        item1.put ("tag", "女装");
        item1.put ("first_class", firstClass);
        item1.put ("second_class", secondClass);
        item1.put ("first_id", firstId);
        item1.put ("second_id", secondId);
        item1.put ("title", "首页");
        itemList.add (item1);
        Map<String, Object> item2 = new HashMap<String, Object> ();
        item2.put ("address", "pages/product/product");
        item2.put ("tag", "女装");
        item2.put ("first_class", firstClass);
        item2.put ("second_class", secondClass);
        item2.put ("first_id", firstId);
        item2.put ("second_id", secondId);
        item2.put ("title", "商城");
        itemList.add (item2);
        Map<String, Object> item3 = new HashMap<String, Object> ();
        item3.put ("address", "pages/want/want");
        item3.put ("tag", "女装");
        item3.put ("first_class", firstClass);
        item3.put ("second_class", secondClass);
        item3.put ("first_id", firstId);
        item3.put ("second_id", secondId);
        item3.put ("title", "喜欢");
        itemList.add (item3);
        Map<String, Object> item4 = new HashMap<String, Object> ();
        item4.put ("address", "pages/my/my");
        item4.put ("tag", "女装");
        item4.put ("first_class", firstClass);
        item4.put ("second_class", secondClass);
        item4.put ("first_id", firstId);
        item4.put ("second_id", secondId);
        item4.put ("title", "我的");
        itemList.add (item4);
        return submitAudit (authorizerToken, itemList);
    }

    /**
     * 提交审核
     *
     * @param authorizerToken authorizerToken
     * @param itemList        提交审核项的一个列表（至少填写1项，至多填写5项）
     * @return java.lang.Object
     * @author Charlie
     * @date 2018/9/18 20:34
     */
    private static String submitAudit(String authorizerToken, List<Map<String, Object>> itemList) {
        String url = SUBMIT_AUDIT_URL + "?access_token=" + authorizerToken;
        Map<String, Object> paramMap = new HashMap<> ();
        paramMap.put ("item_list", itemList);
        String param = JSON.toJSONString (paramMap);
        String auditResult = HttpClientUtils.post (url, param);
        LOGGER.info ("微信小程序提交审核 url:{},param:{},auditResult:{}", url, param, auditResult);
        return auditResult;
    }


    /**
     * 查询最新一次提交的审核状态
     *
     * @param authorizerToken authorizerToken
     * @return java.lang.String
     * {"errcode":0,"errmsg":"ok","auditid":668470563,"status":2}
     * <p>status 0成功,1失败,2审核中</p>
     * @author Charlie
     * @date 2018/9/19 10:28
     */
    public static String lastAuditState(String authorizerToken) {
        String url = LATEST_AUDITSTATUS_URL + "?access_token=" + authorizerToken;
        String lastAuditState = HttpClientUtils.get (url);
        LOGGER.info ("查询最新一次提交的审核状态 url:{},result:{}", url, lastAuditState);
        return lastAuditState;
    }


    /**
     * 获取第三方提交的审核版本的审核状态（仅供第三方代小程序调用）
     *
     * @param authorizerToken authorizerToken
     * @param auditId         审核id
     * @return java.lang.String
     * <p>
     * {"errcode":0,"errmsg":"ok","status":1,"reason":"1:小程序内容不符合规则:<br>(1):小程序服务提供的内容涉及商家自营模式（如含购物车、立即购买按钮、结算按钮等），属个人未开放类目，建议选择企业主体小程序，并选择对应的商家自营类目<br>"}
     * </p>
     * @author Charlie
     * @date 2018/9/19 10:35
     */
    public static String auditState(String authorizerToken, String auditId) {
        String url = AUDIT_STATUS_URL+"?access_token=" + authorizerToken;
        Map<String, Object> paramMap = new HashMap<String, Object> ();
        paramMap.put ("auditid", auditId);
        String param = JSONObject.toJSONString (paramMap);
        String auditState = HttpClientUtils.post (url, param);
        LOGGER.info ("获取第三方提交的审核版本的审核状态 auditId:{},url:{},auditState:{}", auditId, url, auditState);
        return auditState;
    }


    /**
     * 发布已通过审核的小程序（仅供第三方代小程序调用）
     */
    public static String releaseWxa(String authorizerToken) {
        String url = RELEASE_URL + "?access_token=" + authorizerToken;
        //必须post请求
        String releaseResult = HttpClientUtils.post (url, "{}");
        LOGGER.info ("发布已通过审核的小程序 url:{},releaseResult:{}", url, releaseResult);
        return releaseResult;
    }


    /**
     * 修改小程序线上代码的可见状态（仅供第三方代小程序调用）
     *
     * @param authorizerToken authorizerToken
     * @param open            发布后默认可访问，close为不可见，open为可见
     * @return java.lang.String
     * <p>
     * {"errcode":0,"errmsg":"ok"}
     * </p>
     * @author Charlie
     * @date 2018/9/19 10:45
     */
    public static String changeVisitStatus(String authorizerToken, boolean open) {
        String url = CHANGE_VISIT_STATUS_URL + "?access_token=" + authorizerToken;
        Map<String, String> paramMap = new HashMap<String, String> ();
        paramMap.put ("action", open ? "open" : "close");
        String param = JSONObject.toJSONString (paramMap);
        String result = HttpClientUtils.post (url, param);
        LOGGER.info ("发布已通过审核的小程序 url:{},param:{},result:{}", url, param, result);
        return result;
    }


    /**
     * 第三方自定义的配置JSON
     * <p>
     * 提交/上传代码时候使用
     * </p>
     */
    public static String userAppConfig(String appId, String storeId, String storeName, String version) {
        //扩展字段
        Map<String, String> extMap = new HashMap<String, String> ();
        extMap.put ("storeId", storeId);
        extMap.put ("appId", appId);

//		大于3.3.0版本的页面配置
        String[] pagesArr1 = {"pages/index/index",
                "pages/footprint/footprint",
                "pages/product/product",
                "pages/shoppingCart/shoppingCart",
                "pages/want/want",
                "pages/component/secondBuyActivity/secondBuyActivity",
                "pages/component/teamBuyActivity/teamBuyActivity",
                "pages/component/articleDetail/articleDetail",
                "pages/component/tagProduct/tagProduct",
                "pages/component/searchList/searchList",
                "pages/component/detail/detail",
                "pages/component/confirmOrder/confirmOrder",
                "pages/component/chooseCoupon/chooseCoupon",
                "pages/component/newAddress/newAddress",
                "pages/component/editAddress/editAddress",
                "pages/component/goodsWay/goodsWay",
                "pages/component/useredit/useredit",
                "pages/component/editname/editname",
                "pages/component/storeInfo/storeInfo",
                "pages/component/couponProduct/couponProduct",
                "pages/component/couponCenter/couponCenter",
                "pages/component/myCoupon/myCoupon",
                "pages/component/myOrder/myOrder",
                "pages/component/express/express",
                "pages/component/orderDetail/orderDetail",
                "pages/component/shareRule/shareRule",
                "pages/component/settlementRule/settlementRule",
                "pages/component/myCoinDetail/myCoinDetail",
                "pages/component/myShare/myShare",
                "pages/component/myCoin/myCoin",
                "pages/component/myTeamBuy/myTeamBuy",
                "pages/component/withdrawRule/withdrawRule",
                "pages/component/withdrawCash/withdrawCash",
                "pages/component/withdrawSuccess/withdrawSuccess",
                "pages/my/my"};
//		小于3.3.0版本的页面配置
        String[] pagesArr2 = {"pages/index/index",
                "pages/footprint/footprint",
                "pages/want/want",
                "pages/component/detail/detail",
                "pages/component/confirmOrder/confirmOrder",
                "pages/component/chooseCoupon/chooseCoupon",
                "pages/component/useredit/useredit",
                "pages/component/editname/editname",
                "pages/component/storeInfo/storeInfo",
                "pages/component/couponCenter/couponCenter",
                "pages/component/myCoupon/myCoupon",
                "pages/component/myOrder/myOrder",
                "pages/component/orderDetail/orderDetail",
                "pages/my/my"
        };


        Map<String, String> windowMap = new HashMap<String, String> ();
        windowMap.put ("backgroundTextStyle", "light");
        windowMap.put ("navigationBarBackgroundColor", "#fff");
        windowMap.put ("navigationBarTitleText", storeName);
        windowMap.put ("navigationBarTextStyle", "black");
        Map<String, String> networkTimeoutMap = new HashMap<String, String> ();
        networkTimeoutMap.put ("request", "10000");
        networkTimeoutMap.put ("downloadFile", "10000");
        Map<String, Object> tabBarMap = new HashMap<String, Object> ();
        tabBarMap.put ("color", "#828282");
        tabBarMap.put ("selectedColor", "#ff2272");
        tabBarMap.put ("borderStyle", "black");
        Map<String, String> map1 = new HashMap<String, String> ();
        map1.put ("pagePath", "pages/index/index");
        map1.put ("iconPath", "images/icon/home.png");
        map1.put ("selectedIconPath", "images/icon/home-active.png");
        map1.put ("text", "首页");
        Map<String, String> map2 = new HashMap<String, String> ();
        map2.put ("pagePath", "pages/product/product");
        map2.put ("iconPath", "images/icon/product.png");
        map2.put ("selectedIconPath", "images/icon/product-active.png");
        map2.put ("text", "商城");
        Map<String, String> map3 = new HashMap<String, String> ();
        map3.put ("pagePath", "pages/shoppingCart/shoppingCart");
        map3.put ("iconPath", "images/icon/cart.png");
        map3.put ("selectedIconPath", "images/icon/cart-active.png");
        map3.put ("text", "购物车");
        Map<String, String> map4 = new HashMap<String, String> ();
        map4.put ("pagePath", "pages/my/my");
        map4.put ("iconPath", "images/icon/my.png");
        map4.put ("selectedIconPath", "images/icon/my-active.png");
        map4.put ("text", "我的");
        Map[] listArr = {map1, map2, map3, map4};
        tabBarMap.put ("list", listArr);

        Map<String, Object> appCofingMap = new HashMap<String, Object> ();
        //授权方Appid，可填入商户AppID，以区分不同商户
        appCofingMap.put ("extAppid", appId);
        //自定义字段仅允许在这里定义，可在小程序中调用
        appCofingMap.put ("ext", extMap);
        Map<String, String> extPagesMap = new HashMap<String, String> ();
        appCofingMap.put ("extPages", extPagesMap);

        version = version.replace (".", "");
        if (Integer.parseInt (version) < 330) {
            //小于3.3.0版本
            LOGGER.info ("小于3.3.0==========version:" + version + ",pagesArr2:" + pagesArr2);
            appCofingMap.put ("pages", pagesArr2);
        }
        else {
            LOGGER.info ("大于等于3.3.0==========version:" + version + ",pagesArr1:" + pagesArr1);
            appCofingMap.put ("pages", pagesArr1);
        }
        appCofingMap.put ("window", windowMap);
        appCofingMap.put ("networkTimeout", networkTimeoutMap);
        appCofingMap.put ("tabBar", tabBarMap);
        return JSONObject.toJSONString (appCofingMap);
    }


    /**
     * 小程序版本回退（仅供第三方代小程序调用）
     * <p>新加的, 这个暂时没用到, 也没有做测试</p>
     *
     * @param authorizerToken authorizerToken
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/19 11:14
     */
    public static String revertCodeRelease(String authorizerToken) {
        String url = "https://api.weixin.qq.com/wxa/revertcoderelease?access_token=" + authorizerToken;
        return HttpClientUtils.get (url);
    }


    public static void main(String[] args) {
        String cpnAppId = "wx3ef25e066e478873";
        String cpnAppSecret = "f646bd674afb7357fd06536c2fea626b";
//        System.out.println ("accessToken = " + accessToken);
//
        String appId = "wxf99f985dc7f79695";

        String cpnVerifyTicket = "ticket@@@otsxzX8gGhg-A5QOwzwteSYt1946U0hrRHuDydKfVXaJwyif7-NdsWcI3RjB5uH63d9xvaeUk-AGaATwd0ulPQ";
        String jsCode = "071cv5hz0XcROf1kc3iz0yt7hz0cv5ho";

//        String componentAccessToken = componentAccessToken (cpnAppId, cpnAppSecret, cpnVerifyTicket).get ("component_access_token");
        //=================================== 以上準備參數 ===================================
        /*
         * 绑定体验者
         */
//        boolean isOk = bindTester (
//                "13_Ga6B3EiseSl56Nn8FymiFS9Pw0hND600xeRr3L62YHGEhOCphhyby0Ac9E2JMr6gaa1jDlbenG23gKWqpHsbixSVvyD-ZYJTTN1EZLD7h7N1JCPnqMuhf5TmYAUAtHrCKFBPkim-NRbCoVBfZRGhAGDTLA",
//                "date905"
//        );
//        System.out.println ("isOk = " + isOk);

        String authorizerToken = "13__WfBZ5VSSNdtg7kXWN2lo49BOfEBb7SZMCPszvdQeEz1tBGahYg2DLQjUWGl4f29xzSA1InRfqeWQ6_Y2cNXTnOV0gtHZGfCH8pTIEZ2fH4lD7zGSTyWS4WNMiIFSJe8OuMyz192amoM-DJ7KRQcAGDTAZ";
        /*
         * 查看类目
         */
//        CategoryVo category = findCategory (authorizerToken);
//        System.out.println ("category = " + category);

//        String page = findPage (authorizerToken);
//        System.out.println ("page = " + page);

        /**
         * 提交审核
         */
//        String submitAudit = submitAudit (authorizerToken);
//        System.out.println ("submitAudit = " + submitAudit);

        /**
         * 查询最新一次提交的审核状态
         */
//        String lastAuditState = lastAuditState (authorizerToken);
//        System.out.println ("lastAuditState = " + lastAuditState);


        /**
         *  获取第三方提交的审核版本的审核状态（仅供第三方代小程序调用）
         */
//        String state = auditState (authorizerToken, "668470563");
//        System.out.println ("state = " + state);

        /**
         * 发布已通过审核的小程序（仅供第三方代小程序调用）
         */
//        String releaseWxa = releaseWxa (authorizerToken);
//        System.out.println ("releaseWxa = " + releaseWxa);


        /**
         * 修改小程序线上代码的可见状态（仅供第三方代小程序调用）
         */
//        String changeVisitStatus = changeVisitStatus (authorizerToken, Boolean.TRUE);
//        System.out.println ("changeVisitStatus = " + changeVisitStatus);
//         changeVisitStatus = changeVisitStatus (authorizerToken, Boolean.FALSE);
//        System.out.println ("changeVisitStatus = " + changeVisitStatus);

        /**
         * 上传代码 流程测试
         */
        commitTest ("wx23d3c43d2f0428c1",
                "13_tqIZl6cu1SVf6dOQV_eUbVantWa64mCi2HMfPaihcZAwNt2idHv2MYvRbRi0lBoE3fr7c9xcnOm3e5Gm9V4nmgBJBDipzHY0wNkSE10b5Ge3wjJpgD0uVO90Cm9pfBsdQAV9aIobYW_A8bVFOQVaAJDGIN",
                "3",
                "测试",
                "147",
                "3.8.1",
                "俞姐姐",
                null,
                new String[]{"https://wxatest.yujiejie.com"},
                new DomainVo (
                        new String[]{"https://wxatest.yujiejie.com","https://weixintest.yujiejie.com"},
                        new String[]{"wss://wxatest.yujiejie.com","wss://weixintest.yujiejie.com"},
                        new String[]{"https://wxatest.yujiejie.com","https://weixintest.yujiejie.com"},
                        new String[]{"https://wxatest.yujiejie.com","https://weixintest.yujiejie.com"}
                )
                );
    }


    @Data
    @AllArgsConstructor
    public static class DomainVo{
        /**
         * request合法域名，当action参数是get时不需要此字段
         */
        private String[] requestDomainArr;
        /**
         * socket合法域名，当action参数是get时不需要此字段
         */
        private String[] wsRequestDomainArr;
        /**
         * uploadFile合法域名，当action参数是get时不需要此字段
         */
        private String[] uploadDomainArr;
        /**
         * downloadFile合法域名，当action参数是get时不需要此字段
         */
        private String[] downloadDomainArr;
    }
}
