package com.e_commerce.miscroservice.commons.wechat.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.HttpResult;
import com.e_commerce.miscroservice.commons.enums.SetTemplateIdEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.*;
import com.e_commerce.miscroservice.commons.wechat.common.MyX509TrustManager;
import com.e_commerce.miscroservice.commons.wechat.common.WechatConst;
import com.e_commerce.miscroservice.commons.wechat.common.WechatErrorConst;
import com.e_commerce.miscroservice.commons.wechat.entity.*;
import com.e_commerce.miscroservice.commons.wechat.service.WechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Aug 20, 2018 10:57:56 AM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
@Component
public class WechatServiceImpl implements WechatService {

//	@Autowired
	private HttpAPIService httpService;
//	@Autowired
	private RestTemplate restTemplate;

	private Logger logger = LoggerFactory.getLogger(getClass());

	// 日志记录器
	private static Logger LOG = LoggerFactory.getLogger(WechatServiceImpl.class);

	// 小程序APPID
	@Value("${wechat.app_id}")
	private String APP_ID;
	// 小程序密钥
	@Value("${wechat.app_secret}")
	private String APP_SECRET;

	/**
	 * 微信授权
	 */
	public WechatSession checkAuthCode(String code) {
		// 对请求参数进行封装
		Map<String, Object> params = new HashMap<>();
		params.put("js_code", code);
		params.put("appid", APP_ID);
		params.put("secret", APP_SECRET);
		params.put(WechatConst.GRANT_TYPE, "authorization_code");

		LOG.info("---------------------开始请求微信服务器进行登录凭证校验---------------------");
		LOG.info("请求参数信息为:" + JsonUtil.toJson(params));
		// 请求校验登录凭证,并返回相关信息
		String res;
		try {
			HttpResult result = httpService.doPost(WechatConst.JSCODE_SESSION_URL, params);
			res = new String(result.getBody().getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new MessageException("字符串编码格式错误");
		} catch (Exception e) {
			throw new MessageException("微信授权->发送http请求中出现错误");
		}
		LOG.info("返回信息为:" + res);
		// 把结果转换为实体
		WechatSession wechatSession = JsonUtil.parseFromJson(res, WechatSession.class);
		// 接口调用方需要对返回错误码为40029的错误进行处理
		if (StringUtil.isNotEmpty(wechatSession.getErrcode())) {
			throw new MessageException(wechatSession.getErrcode(), wechatSession.getErrmsg());
		}
		return wechatSession;
	}

	/**
	 * 获取token
	 */
	public String getToken() {
		Map<String, Object> params = new HashMap<>();
		params.put(WechatConst.GRANT_TYPE, "client_credential");
		params.put("appid", APP_ID);
		params.put("secret", APP_SECRET);
		String res;
		try {
			String result = httpService.doGet(WechatConst.TOKEN_URL, params);
			res = new String(result.getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new MessageException("字符串编码格式错误");
		} catch (Exception e) {
			throw new MessageException("获取token->发送http请求中出现错误");
		}
		WechatToken token = JsonUtil.parseFromJson(res, WechatToken.class);
		if (StringUtil.isEmpty(token.getErrcode())) {
			return token.getAccess_token();
		} else {
			LOG.info("errCode:" + token.getErrcode() + "--------------错误:" + token.getErrmsg());
			throw new MessageException(token.getErrcode(), token.getErrmsg());
		}
	}

	/**
	 * 获取二维码
	 */
	public String genQRCode(String scene, String page) {
		OutputStream os = null;
		String imgUrl = "";
		try {

			SSLContext sslContext = SSLContext.getInstance("SSL");
			TrustManager[] tm = { new MyX509TrustManager() };
			// 初始化
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 获取SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(WechatConst.QCODE_URL + "?access_token=" + getToken());
			HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			httpURLConnection.setConnectTimeout(10000);// 连接超时 单位毫秒
			httpURLConnection.setReadTimeout(2000);// 读取超时 单位毫秒
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setSSLSocketFactory(ssf);
			// 获取URLConnection对象对应的输出流
			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			Map<String, Object> params = new HashMap<>();
			params.put("scene", scene);
			params.put("page", page);
			params.put("width", 430);
			params.put("auto_color", false);
			Map<String, Object> line_color = new HashMap<>();
			line_color.put("r", 0);
			line_color.put("g", 0);
			line_color.put("b", 0);
			params.put("line_color", line_color);
			params.put("is_hyaline", false);
			printWriter.write(JsonUtil.toJson(params));
			// flush输出流的缓冲
			printWriter.flush();
			// 开始获取数据
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());

			// 上传到阿里云
			imgUrl = AliOSSUtil.uploadQrImg(bis, scene);

			/*
			 * os = new FileOutputStream(new File(filePath)); int len; byte[] arr = new
			 * byte[1024]; while ((len = bis.read(arr)) != -1) { os.write(arr, 0, len);
			 * os.flush(); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return imgUrl;
	}

	/**
	 * 获取个人手机号码信息
	 * @param encryptedData
	 * @param iv
	 * @param session
	 * @return
	 */
	public String getPhoneNumber(String encryptedData, String iv, WechatSession session) {

		try {
			byte[] ency = Base64Util.decode(encryptedData);
			byte[] sessionKey = Base64Util.decode(session.getSession_key());

			byte[] ivs = Base64Util.decode(iv);
			String phone = new String(AESUtil.decrypt(ency, sessionKey, AESUtil.CBC_MODE, ivs));

			WechatPhone wechatPhone = JsonUtil.parseFromJson(phone, WechatPhone.class);
			if (StringUtil.isNotEmpty(wechatPhone.getErrcode())) {
				throw new MessageException(wechatPhone.getErrcode(), wechatPhone.getErrmsg());
			}
			return wechatPhone.getPhoneNumber();

		} catch (Exception ex) {
			throw new MessageException(WechatErrorConst.GET_PHONE_FAILURE, "获取手机号码信息失败");
		}

	}

	/**
	 * 
	 * 功能描述:推送服务通知
	 * 作者:马晓晨
	 * 创建时间:2018年11月21日 下午3:29:09
	 * @param openid
	 * @param formid
	 * @return
	 */
	public String pushOneUser(String openid, String formid) {
		String token = getToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send" + "?access_token=" + token;

		// 拼接推送的模版
		WxMssVo wxMssVo = new WxMssVo();
		wxMssVo.setTouser(openid);// 用户openid
		wxMssVo.setTemplate_id("bfbK0qEOnLFsQIhCbS_Li45g12OODWA-GimeqFrwSgQ");// 模版id
//        wxMssVo.setTemplate_id("i6L0VOb2rfdd4UegnJiNSaYOzCN1msyGQVfD5VC5jwA");//模版id
		wxMssVo.setForm_id(formid);// formid

		Map<String, TemplateData> m = new HashMap<>(5);

		// keyword1：订单类型，keyword2：下单金额，keyword3：配送地址，keyword4：取件地址，keyword5备注

		TemplateData keyword1 = new TemplateData();
		keyword1.setValue("新下单待抢单");
		m.put("keyword1", keyword1);

		TemplateData keyword2 = new TemplateData();
		keyword2.setValue("这里填下单金额的值");
		m.put("keyword2", keyword2);
		wxMssVo.setData(m);

		TemplateData keyword3 = new TemplateData();
		keyword3.setValue("这里填配送地址");
		m.put("keyword3", keyword3);
		wxMssVo.setData(m);

		TemplateData keyword4 = new TemplateData();
		keyword4.setValue("这里填取件地址");
		keyword4.setColor("#0000FF");
		m.put("keyword4", keyword4);
		wxMssVo.setData(m);

		TemplateData keyword5 = new TemplateData();
		keyword5.setValue("这里填备注");
		m.put("keyword5", keyword5);
		wxMssVo.setData(m);

		TemplateData keyword6 = new TemplateData();
		keyword6.setValue("这里填备注");
		m.put("keyword6", keyword6);
		wxMssVo.setData(m);
//        for (int i = 0; i < cishu; i++) {
//        	TemplateData keyword7 = new TemplateData();
//            keyword7.setValue("stinglist.get(i)");
//            m.put("keyword"+i, keyword7);
//            wxMssVo.setData(m);
//		}

		logger.error("小程序推送结果={}", url);
		logger.error("小程序推送结果={}", wxMssVo.toString());
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxMssVo, String.class);
		logger.error("小程序推送结果={}", responseEntity.getBody());
		return responseEntity.getBody();
	}

	/**
	 * 
	 * 功能描述:发送服务通知
	 * 作者:姜修弘
	 * 创建时间:2018年11月24日 下午5:02:24
	 * @param openid
	 * @param formid
	 * @param msg
	 * @param type
	 * @return
	 */
	public String pushOneUserMsg(String openid, String formid, List<String> msg, SetTemplateIdEnum setTemplateIdEnum , String parameter) {
		String token = getToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send" + "?access_token=" + token;

		// 拼接推送的模版
		WxMssVo wxMssVo = new WxMssVo();
		wxMssVo.setTouser(openid);// 用户openid
		wxMssVo.setTemplate_id(setTemplateIdEnum.getSetTemplateId());
		if (!StringUtil.isEmpty(setTemplateIdEnum.getUrl())) {
			wxMssVo.setPage(setTemplateIdEnum.getUrl()+parameter);
		}
		wxMssVo.setForm_id(formid);// formid

		Map<String, TemplateData> m = new HashMap<>(msg.size());

		// keyword1：订单类型，keyword2：下单金额，keyword3：配送地址，keyword4：取件地址，keyword5备注
		for (int i = 0; i < msg.size(); i++) {
			TemplateData keyword = new TemplateData();
			keyword.setValue(msg.get(i));
			m.put("keyword" + (i + 1), keyword);
			wxMssVo.setData(m);
		}
//		if (setTemplateIdEnum.getEnlarge() != 0) {
//			wxMssVo.setEmphasis_keyword("keyword" + setTemplateIdEnum.getEnlarge()+".DATA");
//		}
		logger.info("小程序推送结果={}", url);
		logger.info("小程序推送结果={}", wxMssVo.toString());
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxMssVo, String.class);
		logger.info("小程序推送结果={}", responseEntity.getBody());
		return responseEntity.getBody();
	}
}
