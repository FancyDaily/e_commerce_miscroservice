package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.tuckey.web.filters.urlrewrite.utils.URLDecoder;


public class EncodeUtil {
     private   static Log logger = Log.getInstance(EncodeUtil.class);
    /**
     * 如果容器使用ISO-8859-1对url进行解码，那么在服务端需要按ISO-8859-1编码还原为字节数组，再使用正确的字符集重新解码。
     * 
     * @param text 文本
     * @param charsetUsedToDecode 重新解码使用的字符集
     * @return 重新编码后的文本
     */
    public static String reEncode(String text, String charsetUsedToDecode) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        byte[] bytes = StringUtil.getBytes(text, "ISO-8859-1");
        return StringUtil.newString(bytes, charsetUsedToDecode);
    }
    /**
     *  需对url 做URL编码
     * @param url
     * @return
     */
    public static String encodeURL(String url ) {
    	return encodeURL(url,"UTF-8");
    }
    /**
     *  需对url 做URL编码
     * @param url
     * @return
     */
    public static String encodeURL(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     *  需对url 做URL解码
     * @param url
     * @return
     */
    public static String decodeURL(String url ) {
    	return decodeURL(url,"UTF-8");
    }
    
    /**
     *  需对url 做URL解码
     * @param url
     * @return
     */
    public static String decodeURL(String url, String charset) {
			try {
				return URLDecoder.decode(url, charset);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
    }
    public static String encodeBase64String(String string) {
        return encodeBase64String(string,"UTF-8");
    }
    public static String encodeBase64String(String string, String charset) {
        byte[] bytes = StringUtil.getBytes(string, charset);
        return Base64.encodeBase64String(bytes);
    }

    public static String encodeBase64URLSafeString(String string, String charset) {
        byte[] bytes = StringUtil.getBytes(string, charset);
        return Base64.encodeBase64URLSafeString(bytes);
    }

    public static String decodeBase64(String base64String, String charset) {
        return StringUtil.newString(Base64.decodeBase64(base64String), charset);
    }
    public static String decodeBase64(String base64String ) {
        return decodeBase64(base64String,"UTF-8");
    }
    
 // 过滤特殊字符 
    public static String StringFilter1(String str)  { 
    	//
    	logger.info("过滤特殊字符之前str：" +str);
 // 只允许字母和数字
    	 String regEx ="[^a-zA-Z0-9\u4e00-\u9fa5]"; 
 // 清除掉所有特殊字符 
    	 //String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 
    	Pattern p = Pattern.compile(regEx); 
    	Matcher m = p.matcher(str);
    	str = m.replaceAll("").trim();
    	logger.info("过滤特殊字符之后str：" +str);
    	return str;
    } 
    /**
     * 微信昵称过滤表情
     * @param source
     * @return
     */
    public static String filterEmoji(String source) { 
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find())
            {
                source = emojiMatcher.replaceAll("");
                return source ;
            }
            return source;
        }
        return source; 
    }
    
    public static void main(String[] args) {
    	String str = "*adCVs*34_a _09_b5*[/435^*&城池()^$$&*).{}+.|.)%%*(*.中国}34{45[]12.fd'*&999下面是中文的字符￥……{}【】。，；’“‘”？"; 
    	System.out.println(str); 
    	System.out.println(StringFilter1(str));	
	}
}
