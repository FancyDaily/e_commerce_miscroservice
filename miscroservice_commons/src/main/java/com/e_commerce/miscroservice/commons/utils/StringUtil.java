package com.e_commerce.miscroservice.commons.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class StringUtil {

    public static String deleteCharAt(String string, int index) {
        return new StringBuilder(string).deleteCharAt(index).toString();
    }

    public static String deleteLastChar(String string, char character) {
        int index = string.lastIndexOf(character);
        if (index != -1) {
            return deleteCharAt(string, index);
        } else {
            return string;
        }
    }

    public static String join(Object... objects) {
        StringBuilder builder = new StringBuilder();
        for (Object object : objects) {
            if (object != null) {
                builder.append(object.toString());
            }
        }
        return builder.toString();
    }

    public static List<String> split(String string, char separator) {
        if (StringUtils.isBlank(string)) {
            return new ArrayList<String>();
        }
        return Arrays.asList(StringUtils.split(string, separator));
    }

    public static Set<String> splitAsSet(String string, char separator) {
        return new HashSet<String>(split(string, separator));
    }

    public static String toUnicodeCharacterSequence(String string) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            builder.append("\\u").append(Integer.toHexString(string.codePointAt(i)));
        }
        return builder.toString();
    }
    
    public static String replaceHtmlSpaceChar(String text) {
        return text.replaceAll("&nbsp;", " ");
    }

    public static String escapeHtml(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if (c == '<' || c == '>' || c == '&' || c == '"') {
                StringBuilder builder = new StringBuilder(text.substring(0, i));
                builder.append(escapeCharAsHtml(c));
                i++;
                int next = i;
                while (i < length) {
                    c = text.charAt(i);
                    if (c == '<' || c == '>' || c == '&' || c == '"') {
                        builder.append(text.substring(next, i));
                        builder.append(escapeCharAsHtml(c));
                        next = i + 1;
                    }
                    i++;
                }
                if (next < length) {
                    builder.append(text.substring(next));
                }
                text = builder.toString();
                break;
            }
        }
        return text;
    }



    private static String escapeCharAsHtml(char c) {
        switch (c) {
            case '<':
                return "&lt;";
            case '>':
                return "&gt;";
            case '&':
                return "&amp;";
            case '"':
                return "&quot;";
            default:
                return String.valueOf(c);
        }
    }

    /**
     * UTF-8 encoding
     * 
     * <pre>
     * 0000 0000 —— 0000 007F 
     *  0xxxxxxx 
     *  
     * 0000 0080 —— 0000 07FF 
     *  110xxxxx 10xxxxxx 
     *  
     * 0000 0800 —— 0000 FFFF 
     *  1110xxxx 10xxxxxx 10xxxxxx 
     *  
     * 0001 0000 —— 001F FFFF 
     *  11110xxx 10xxxxxx 10xxxxxx 10xxxxxx 
     *  
     * 0020 0000 —— 03FF FFFF 
     *  111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 
     *  
     * 0400 0000 —— 7FFF FFFF 
     *  1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
     * </pre>
     */
    public static String getTextWithUTF8BytesLimit(String text, int maxBytes) {
        try {
            if (StringUtils.isBlank(text)) {
                return text;
            } else {
                byte[] bytes = getBytes(text, "UTF-8");
                if (bytes.length <= maxBytes) {
                    return text;
                } else {
                    for (int i = maxBytes; i > 0; i--) {
                        byte b = bytes[i];
                        if ((b & 0x80) == 0 || (b & 0x40) != 0) { // first byte of UTF-8 bytes
                            return new String(bytes, 0, i, "UTF-8");
                        }
                    }
                }
                throw new RuntimeException();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean in(String string, boolean ignoreCase, String... collection) {
        if (ignoreCase) {
            for (String item : collection) {
                if (StringUtils.equalsIgnoreCase(string, item)) {
                    return true;
                }
            }
        } else {
            for (String item : collection) {
                if (StringUtils.equals(string, item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean in(String string, String... collection) {
        return in(string, false, collection);
    }

    public static boolean startsWithAny(String string, boolean ignoreCase, String postfix, String... collection) {
        postfix = StringUtils.defaultString(postfix);

        if (ignoreCase) {
            for (String item : collection) {
                if (string.toLowerCase().startsWith(item.toLowerCase() + postfix.toLowerCase())) {
                    return true;
                }
            }
        } else {
            for (String item : collection) {
                if (string.startsWith(item + postfix)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String defaultIfBlank(Object obj, String defaultValue) {
        if (obj == null) {
            return defaultValue;
        } else {
            String value = obj.toString();
            return StringUtils.defaultIfBlank(value, defaultValue);
        }
    }

    public static String trim(String string, String defaultValue) {
        if (string == null) {
            return defaultValue;
        }
        string = string.trim();
        if (StringUtils.isBlank(string)) {
            return defaultValue;
        }
        return string;
    }

    public static String toUnderscoreNaming(String name) {
        String[] parts = StringUtils.splitByCharacterTypeCamelCase(name);
        return StringUtils.join(parts, '_').toLowerCase();
    }

    public static String newString(byte[] bytes, String charset) {
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getBytes(String string, String charset) {
        try {
            return string.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String abbreviateAndJoin(String textToAbbreviate, int maxTotalWidth, String separator,
                                           String... completeParts) {
        List<String> parts = new ArrayList<String>();
        for (String completePart : completeParts) {
            if (completePart != null) {
                parts.add(completePart);
            }
        }

        int maxWidth = maxTotalWidth;
        int sepLen = separator.length();
        for (String part : parts) {
            maxWidth -= part.length() + sepLen;
        }

        textToAbbreviate = StringUtils.abbreviate(textToAbbreviate, maxWidth);
        parts.add(0, textToAbbreviate);
        return StringUtils.join(parts.iterator(), separator);
    }

    /**
     * @param length 最长支持中文字数
     */
    public static String ellipsis(String text, int length) {
        return ellipsis(text, length, "...");
    }

    /**
     * @param length 最长支持中文字数
     */
    public static String ellipsis(String text, int length, String ellipsis) {
        if (StringUtils.isBlank(text)) {
            return "";
        }

        if (text.length() <= length) {
            return text;
        }

        length = 2 * length;
        int endIndex = 0;
        for (char c : text.toCharArray()) {
            int t = isChinese(c) ? 2 : 1;
            if (length - t < 0) {
                break;
            }
            endIndex++;
            length = length - t;
        }

        if (endIndex == text.length()) {
            return text;
        }

        if (isChinese(text.charAt(endIndex - 1))) {
            endIndex--;
        } else {
            endIndex = endIndex - (length > 0 ? 1 : 2);
        }

        StringBuilder builder = new StringBuilder(text.substring(0, endIndex)).append(ellipsis);
        return builder.toString();
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
            ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
            ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
            ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B ||
            ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
            ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS ||
            ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
    
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
        try {
            byte[] bytes = text.getBytes("ISO-8859-1");
            return new String(bytes, charsetUsedToDecode);
        } catch (UnsupportedEncodingException e) {
            // 如果执行到这里，应该是编码书写错误，需要在开发阶段发现并修复。
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 手机号中间4位隐藏显示
     */
    public static String maskPhoneNo(String phoneNo) {
    	if (StringUtils.isBlank(phoneNo)) {
    		return "";
    	}
    	if (phoneNo.length() >= 7) {
			StringBuilder builder = new StringBuilder(phoneNo.substring(0, 3));
			builder.append("****");
			builder.append(phoneNo.substring(phoneNo.length()-4));
			return builder.toString();
		}
		return phoneNo;
    }

    /**
     * 判断字符串是否不为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断多个字符串是否不为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String... str) {
        for (String s : str) {
            if (s == null || s.trim().equals("")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        // TODO Auto-generated method stub
        return str == null || str.trim().equals("");
    }

    /**
     * 判断多个字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String... str) {
        for (String s : str) {
            if (s == null || s.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 取UUID
     * @return
     */
    public static String genGUID() {

        return java.util.UUID.randomUUID().toString();

    }
    public static String genNonceStr() {
        Random random = new Random();
        return DigestUtils.md5Hex(String.valueOf(random.nextInt(10000)));
    }


    public static String firstElemOfJsonArray(String jsonArrStr) {
        String res = "";
        if (StringUtils.isNotBlank(jsonArrStr)) {
            JSONArray jsonArray = JSONObject.parseArray(jsonArrStr);
            if (! jsonArray.isEmpty()) {
                Object first = jsonArray.get(0);
                if (first != null) {
                    res = first.toString();
                }
            }
        }
        return res;
    }


    /**
     * 截取定长
     *
     * @param jsonArrStr jsonArrStr
     * @param maxLen maxLen
     * @return java.lang.String
     * @author Charlie
     * @date 2019/1/16 16:52
     */
    public static String subJsonArray(String jsonArrStr, int maxLen) {
        if (StringUtils.isBlank(jsonArrStr)) {
            return jsonArrStr;
        }
        JSONArray jsonArray = JSONObject.parseArray(jsonArrStr);
        if (jsonArray.size() > maxLen) {
            JSONArray newJsonArr = new JSONArray(maxLen);
            for (int i = 0; i < maxLen; i++) {
                newJsonArr.add(jsonArray.get(i));
            }
            jsonArrStr = newJsonArr.toJSONString();
        }
        return jsonArrStr;
    }
}
