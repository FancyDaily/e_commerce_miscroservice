//package com.e_commerce.miscroservice.commons.utils;
//
//import com.e_commerce.miscroservice.commons.enums.user.CookieConstants;
//import com.e_commerce.miscroservice.commons.enums.user.UserType;
//import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.AesUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.tomcat.util.http.ServerCookie;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class LoginUtil {
//
//    private static final String COOKIE_ENCRYPT_KEY = "=W!xB^&0U5AkNr]FQz{S?B+OU[OAa90J";
//
//    private static final String COOKIE_PART_SPLIT = "#";
//
//    public static void deleteLoginCookie(HttpServletResponse response) {
//        CookieUtil.deleteCookie(response, CookieConstants.COOKIE_NAME_SESSION, CookieConstants.COOKIE_DOMAIN, "/");
//    }
//
//    public static String buildLoginCookieHeaderValue(String value) {
//        ServerCookie
//        return CookieUtil.buildCookieHeaderValue(CookieConstants.COOKIE_NAME_SESSION, value, "/",
//            CookieConstants.COOKIE_DOMAIN, null, true);
//    }
//
//    public static String buildLoginCookieValue(String userRelatedName, UserType userType) {
//        long time = System.currentTimeMillis();
//        String souce = userRelatedName + COOKIE_PART_SPLIT + userType.getIntValue() + COOKIE_PART_SPLIT + time;
//        return AesUtil.encode(souce, COOKIE_ENCRYPT_KEY);
//    }
//
//    public static String[] getLoginCookieParts(HttpServletRequest request) {
//        String cookieValue = CookieUtil.getCookieValue(request, CookieConstants.COOKIE_NAME_SESSION, null);
//        if (cookieValue == null) {
//			cookieValue = (String) request.getSession().getAttribute(CookieConstants.COOKIE_NAME_SESSION);
//			if (cookieValue == null)
//				return null;
//        }
//
//        String result = AesUtil.decode(cookieValue, COOKIE_ENCRYPT_KEY);
//        if (StringUtils.isBlank(result)) {
//            return null;
//        }
//        return StringUtils.split(result, COOKIE_PART_SPLIT);
//    }
//
//}
