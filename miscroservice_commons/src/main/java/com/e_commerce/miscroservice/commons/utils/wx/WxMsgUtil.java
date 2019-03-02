package com.e_commerce.miscroservice.commons.utils.wx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.enums.wx.TemplateEnum;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.commons.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 发送服务通知, 文本, 图片
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/19 16:29
 * @Copyright 玖远网络
 */
public class WxMsgUtil{

    private static final Logger LOGGER = LoggerFactory.getLogger (WxMsgUtil.class);

    private static final String EMPTY_STR = "";
    /**
     * 小程序模版列表
     */
    private static final String TEMPLATE_LIST_URL = "https://api.weixin.qq.com/cgi-bin/wxopen/template/list";
    /**
     * 添加模版
     */
    private static final String TEMPLATE_ADD_URL = "https://api.weixin.qq.com/cgi-bin/wxopen/template/add";
    /**
     * 发送模版通知
     */
    private static final String TEMPLATE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send";

    /**
     * 发送客服消息
     */
    private static final String CUSTOM_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
    /**
     * 上传素材
     */
    private static final String MEDIA_UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload";


    /**
     * 查询所有模版
     *
     * @param authorizerToken authorizerToken
     * @return java.lang.String
     * <p>
     * {"errcode":0,"errmsg":"ok","list":[{"template_id":"ImGtzM5RUlMrUmwwPtvMaxM-IdGmOtWPVO7iogcP69U","title":"订单支付成功通知","content":"单号{{keyword1.DATA}}\n金额{{keyword2.DATA}}\n下单时间{{keyword3.DATA}}\n物品名称{{keyword4.DATA}}\n订单号码{{keyword5.DATA}}\n支付时间{{keyword6.DATA}}\n订单金额{{keyword7.DATA}}\n订单状态{{keyword8.DATA}}\n订单编号{{keyword9.DATA}}\n商品名称{{keyword10.DATA}}\n","example":"单号：123456\n金额：30元\n下单时间：2016年8月8日\n物品名称：梨子\n订单号码：123456789\n支付时间：2104-12-09 16:00\n订单金额：200元\n订单状态：已受理\n订单编号：20161031162645020777\n商品名称：进口金枕头榴莲（S号）1个54.9元【香甜软糯】\n"}]}
     * </p>
     * @author Charlie
     * @date 2018/9/19 17:24
     */
    public static String listTemplate(String authorizerToken) {
        String url = TEMPLATE_LIST_URL + "?access_token=" + authorizerToken;
        Map<String, String> paramMap = new HashMap<String, String> (2);
        paramMap.put ("offset", "0");
        //必须小于等于20
        paramMap.put ("count", "20");
        String param = JSONObject.toJSONString (paramMap);
        String templateList = HttpClientUtils.post (url, param);
        LOGGER.info ("获取模版列表 url:{},param:{},result:{}", url, param, templateList);
        return templateList;
    }


    /**
     * 组合模板并添加至帐号下的个人模板库
     *
     * @param authorizerToken    authorizerToken
     * @param templateId         模板标题id
     * @param templateKeywordIds 开发者自行组合好的模板关键词列表，关键词顺序可以自由搭配（例如[3,5,4]或[4,5,3]），最多支持10个关键词组合
     * @return java.lang.String
     * <p>{"errcode":0,"errmsg":"ok","template_id":"Gsucvysy4yYCCiyy7j_iRErNl4f9GP5epM_-Twv5fPs"}</p>
     * @author Charlie
     * @date 2018/9/19 17:24
     */
    public static String addTemplate(String authorizerToken, String templateId, Integer[] templateKeywordIds) {
        String url = TEMPLATE_ADD_URL + "?access_token=" + authorizerToken;
        Map<String, Object> paramMap = new HashMap<String, Object> ();
        paramMap.put ("id", templateId);
        paramMap.put ("keyword_id_list", templateKeywordIds);
        String param = JSONObject.toJSONString (paramMap);
        String result = HttpClientUtils.post (url, param);
        LOGGER.info ("新增一个模版 url:{},param:{},result:{}", url, param, result);
        return result;
    }


    /**
     * 发送模版通知
     *
     * @param authorizerToken authorizerToken
     * @param templateId      	所需下发的模板消息的id
     * @param openId          接收者（用户）的 openid
     * @param formId          表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
     *                        <p>
     *                        一些坑 : formId 只能发给获取formId的openId用户他自己, 发送小星星formId只能使用2~3次,7天过期
     *                        </p>
     * @param pageUrl 非必填   点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转
     * <P>
     *     请求参数实例
     * {
     *   "touser": "OPENID",
     *   "template_id": "TEMPLATE_ID",
     *   "page": "index",
     *   "form_id": "FORMID",
     *   "data": {
     *       "keyword1": {
     *           "value": "339208499"
     *       },
     *       "keyword2": {
     *           "value": "2015年01月05日 12:30"
     *       },
     *       "keyword3": {
     *           "value": "粤海喜来登酒店"
     *       } ,
     *       "keyword4": {
     *           "value": "广州市天河区天河路208号"
     *       }
     *   },
     *   "emphasis_keyword": "keyword1.DATA"
     * }
     *
     *
     *     扩展的其他参数:
     *       color	否	模板内容字体的颜色，不填默认黑色 【废弃】
     *       emphasis_keyword	否	模板需要放大的关键词，不填则默认无放大
     * </P>
     * @return java.lang.String <p>{"errcode":0,"errmsg":"ok"}</p>
     * @author Charlie
     * @date 2018/9/19 17:37
     */
    public static String sendTemplate(String authorizerToken, String templateId, Map<String, Map<String,String>> data, String openId, String formId, String pageUrl) {
        String url = TEMPLATE_SEND_URL + "?access_token=" + authorizerToken;
        Map<String, Object> paramMap = new HashMap<> ();
        paramMap.put ("touser", openId);
        paramMap.put ("template_id", templateId);
        paramMap.put ("page", pageUrl);
        if (StringUtils.isNotBlank (formId)) {
            paramMap.put ("form_id", formId);
        }
        paramMap.put("data", data);
        String param = JSONObject.toJSONString (paramMap);
        String result = HttpClientUtils.post (url, param);
        LOGGER.info ("发送模版通知 url:{},param:{},result:{}", url, param, result);
        return result;
    }



    /**
     * 发送客服消息
     * <p>注意!调用此接口,需要用户主动与你交互,先发一条信息才行,否则无法发送出去</p>
     *
     * @param authorizerToken authorizerToken
     * @param content         发送内容
     * @param openId          接收者（用户）的 openid
     * @return java.lang.String <p>{"errcode":0,"errmsg":"ok"}</p>
     * @author Charlie
     * @date 2018/9/19 17:37
     */
    public static String customSendTextUrl(String authorizerToken, String openId, String content) {
        String url = CUSTOM_SEND_URL + "?access_token=" + authorizerToken;
        Map<String, Object> paramMap = new HashMap<> ();
        paramMap.put("touser", openId);
        paramMap.put("msgtype", "text");
        Map<String, Object> contentMap = new HashMap<String, Object>();
        contentMap.put("content", content);
        paramMap.put("text", contentMap);

        String param = JSONObject.toJSONString (paramMap);
        String result = HttpClientUtils.post (url, param);
        LOGGER.info ("发送模版通知 url:{},param:{},result:{}", url, param, result);
        return result;
    }



    /**
     * 发送客服消息
     * <p>先将文件上传到素材库获取mediaId,再发通知</p>
     *
     * @param authorizerToken authorizerToken
     * @param mediaId         发送内容 {@link WxMsgUtil#mediaUpload(String, File)}
     * @param openId          接收者（用户）的 openid
     * @return java.lang.String <p>{"errcode":0,"errmsg":"ok"}</p>
     * @author Charlie
     * @date 2018/9/19 17:37
     */
    public static String customSendImageUrl(String authorizerToken, String openId, String mediaId) {
        String url = CUSTOM_SEND_URL + "?access_token=" + authorizerToken;
        Map<String, Object> paramMap = new HashMap<> ();
        paramMap.put("touser", openId);
        paramMap.put("msgtype", "image");
        Map<String, Object> imageMap = new HashMap<String, Object>();
        imageMap.put("media_id", mediaId);
        paramMap.put("image", imageMap);

        String param = JSONObject.toJSONString (paramMap);
        String result = HttpClientUtils.post (url, param);
        LOGGER.info ("发送模版通知 url:{},param:{},result:{}", url, param, result);
        return result;
    }


    /**
     * 上传临时素材
     *
     * @param authorizerToken authorizerToken
     * @param file file
     * @return java.lang.String <p>{"type":"image","media_id":"DZOGBltDMHQbce1k4AXqpp_fpI2BbzucMPtzKYmjufmjUrOTw0TJP2j4_WiFSuWl","created_at":1537359637}</p>
     * @author Charlie
     * @date 2018/9/19 19:26
     */
    public static String mediaUpload(String authorizerToken, File file) {
        String url = MEDIA_UPLOAD_URL + "?access_token=" + authorizerToken + "&type=image";
        String result = HttpUtils.upload (url, file);
        LOGGER.info ("上传临时素材 url:{},file:{}:result:{}", url, file.getName (), result);
        return result;
    }


    /**
     * 随机四位字符串
     *
     * @return java.lang.String
     * @author Charlie
     * @date 2018/10/12 17:09
     */
    private static String get4Code(){
        StringBuilder sb = new StringBuilder ();
        String codes ="qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        for(int i=0;i<codes.length();i++){
            char c = codes.charAt(random.nextInt(codes.length()));
            if(sb.length()==4) {
                break;
            }
            if(!sb.toString().contains(c+"")) {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        testSendTemplateMsg();
        //阿静在测试线app中的openId
        String openId = "oPosB0fneQzK3nUoA5T-uhtbyovc";

        /*
            8:商品名称
            7:订单号
            9:金额
            26:收益来源
            2:收益金额
            3:到账时间
         */
//        String 发个消息给你 = customSendTextUrl (accessToken, openId, "发个消息给你");
//        System.out.println ("发个消息给你 = " + 发个消息给你);

        /*
         * 素材库上传一张图片
         */
//        File img = new File ("C:\\Users\\Think\\Desktop\\chaplin.jpg");
//        System.out.println ("img.getName () = " + img.getName ());
//        String mediaUpload = mediaUpload (accessToken, img);
//        System.out.println ("mediaUpload = " + mediaUpload);

        /*
         * 客服发送图片
         */
//        String customSendImageUrl = customSendImageUrl (accessToken, openId, "DZOGBltDMHQbce1k4AXqpp_fpI2BbzucMPtzKYmjufmjUrOTw0TJP2j4_WiFSuWl");
//        System.out.println ("customSendImageUrl = " + customSendImageUrl);

    }




    /**
     * 测试发送模版地址
     *
     * @author Charlie
     * @date 2018/10/12 17:04
     */
    private static void testSendTemplateMsg() {
        //!!!!!! 注意IP的白名单要配置
        String appId = "wx23d3c43d2f0428c1";
        Map map = new HashMap (3);
        String code = get4Code();
        map.put ("ytoken", Md5Util.md5 (appId + code + "yjj_weixin"));
        map.put ("appId", appId);
        map.put ("code", code);
        //获取token
        String accessToken = HttpUtils.sendGet ("https://weixintest.yujiejie.com/third/componentAccessToken", map);
        System.out.println ("response = " + accessToken);

        if (StringUtils.isNotBlank (accessToken)) {
            //获取模版列表
            String listTemplate = listTemplate (accessToken);
            System.out.println ("listTemplate = " + listTemplate);
            if (StringUtils.isNotBlank (listTemplate)) {
                JSONObject listTemplateJson = JSONObject.parseObject (listTemplate);
                Integer errcode = listTemplateJson.getInteger ("errcode");
                JSONArray listJson = listTemplateJson.getJSONArray ("list");
                if (errcode != null && errcode == 0 && listJson != null) {
                    //找到需要发送的模版id
                    String templateId = null;

                    for (int i = 0; i < listJson.size (); i++) {
                        JSONObject templateJson = listJson.getJSONObject (i);
                        //找到标题是收益通知的
                        if (TemplateEnum.DISTRIBUTION_EARNINGS_ADVICE.getTitle ().equals (templateJson.getString ("title"))) {
                            System.out.println ("templateJson.getString (\"title\") = " + templateJson.getString ("title"));
                            templateId = templateJson.getString ("template_id");
                        }
                    }
                    //没有找到就新建
                    if (StringUtils.isBlank (templateId)) {
                        //新增通知模版
                        String response = addTemplate (accessToken, TemplateEnum.DISTRIBUTION_EARNINGS_ADVICE.getId (), TemplateEnum.DISTRIBUTION_EARNINGS_ADVICE.getKeysId ());
                        System.out.println ("response = " + response);
                        if (WxSupportUtil.isSuccess (response)) {
                            JSONObject newTemplate = JSONObject.parseObject (response);
                            templateId = newTemplate.getString ("template_id");
                        }
                    }

                    if (StringUtils.isNotBlank (templateId)) {
                        //发送通知
                        Map<String, Map<String, String>> data = new HashMap<> (6);

                        HashMap<String, String> keyword1 = new HashMap<> (1);
                        keyword1.put ("value", "new");
                        data.put ("keyword1", keyword1);

                        HashMap<String, String> keyword2 = new HashMap<> (1);
                        keyword2.put ("value", "2015年01月05日 12:30");
                        data.put ("keyword2", keyword2);

                        HashMap<String, String> keyword3 = new HashMap<> (1);
                        keyword3.put ("value", "粤海喜来登酒店");
                        data.put ("keyword3", keyword3);

                        HashMap<String, String> keyword4 = new HashMap<> (1);
                        keyword4.put ("value", "2015年01月05日 12:30");
                        data.put ("keyword4", keyword4);

                        HashMap<String, String> keyword5 = new HashMap<> (1);
                        keyword5.put ("value", "2015年01月05日 12:30");
                        data.put ("keyword5", keyword5);

                        HashMap<String, String> keyword6 = new HashMap<> (1);
                        keyword6.put ("value", "2015年01月05日 12:30");
                        data.put ("keyword6", keyword6);
                        String response = sendTemplate (
                                accessToken,
                                templateId,
                                data,
                                "oPosB0fneQzK3nUoA5T-uhtbyovc",
                                "wx121636403667899c84cb7cbc0418842019",
                                ""
                        );
                        System.out.println ("sendTemplate.response = " + response);
                    }
                    else {
                        //获取模版id失败
                    }

                }
                else {
                    // 获取微信通知模版列表失败 errmsg
                }

            }
            else {
                // 获取微信通知模版列表失败 返回空
            }
        }
        else {
            //
        }


    }
}
