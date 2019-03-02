package com.e_commerce.miscroservice.commons.utils.wx;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/14 13:07
 * @Copyright 玖远网络
 */
class WxSupportUtil{
    private static final String XML_ = "<xml>";
    private static final String _XML = "</xml>";

    //========================= 老系统的 start ===========================================
    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }
    //========================= 老系统的 end ===========================================


    /**
     * map===>mxl
     * <p>
     * {@link com/e_commerce/miscroservice/commons/utils/wx/WxPrepay4AppUtil.java}
     * </p>
     * @param source source
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/15 13:35
     */
    public static String toXml(SortedMap<String, Object> source) {
        StringBuilder sb = new StringBuilder ();
        sb.append (XML_);
        for (Map.Entry<String, Object> entry : source.entrySet ()) {
            String key = entry.getKey ();
            Object value = entry.getValue ();
            sb.append ("<").append (key).append (">");
            sb.append (value);
            sb.append ("</").append (key).append (">");
        }
        sb.append (_XML);
        return sb.toString ();
    }

    /**
     * 校验与微信交互是否成功
     *
     * @param response response
     * @return boolean
     * @author Charlie
     * @date 2018/10/12 15:32
     */
    public static boolean isSuccess(String response) {
        if (StringUtils.isBlank (response)) {
            return Boolean.FALSE;
        }
        return response.contains ("\"errcode\":0") && response.contains ("\"errmsg\":\"ok\"");
    }
}
