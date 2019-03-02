package com.e_commerce.miscroservice.commons.utils.pay;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * 描述
 * @author hyq
 * @date 2018/9/21 14:09
 * @return
 */
public class Sign {
	/**
	 * 微信支付签名算法sign
	 */
	public static String createSign(String characterEncoding,
			SortedMap<Object, Object> parameters, String key) {
		StringBuffer sb = new StringBuffer();
		// 所有参与传参的参数按照accsii排序（升序）
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding)
				.toUpperCase();
		return sign;
	}
	/**
	 * 生成签名
	 */
	public static String genSign(List<NameValuePair> params, String apiKey) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(apiKey);

		String packageSign = DigestUtils.md5Hex(sb.toString()).toUpperCase();
		return packageSign;
	}
	public static String getNonceStr() {
		Random random = new Random();
		return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
	}
	/**
	 * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
	 *
	 * @return API签名是否合法
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static boolean checkIsSignValidFromResponseString(Map<String, String> map, String apiKey) {
		String signFromAPIResponse = map.get("sign");
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			return false;
		}
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		map.put("sign", "");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		String signForAPIResponse = getSign(map, apiKey);

		if (!signForAPIResponse.equals(signFromAPIResponse)) {
			// 签名验不过，表示这个API返回的数据有可能已经被篡改了
			return false;
		}
		return true;
	}
	public static String getSign(Map<String, String> map, String apiKey) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue() != "") {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + apiKey;
		result = DigestUtils.md5Hex(result).toUpperCase();
		return result;
	}
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
}
