package com.e_commerce.miscroservice.commons.utils;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/18 8:37
 * @Copyright 玖远网络
 */
public class WebUtil{

    private static final UrlPathHelper urlPathHelper = new UrlPathHelper ();


    public static HttpServletRequest getRequest() {
        return getRequestAttributes ().getRequest ();
    }

    public static HttpServletResponse getResponse() {
        return getRequestAttributes ().getResponse ();
    }

    public static HttpSession getSession() {
        return getRequest ().getSession ();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes ());
    }

    public static ServletContext getServletContext() {
        return ContextLoader.getCurrentWebApplicationContext ().getServletContext ();
    }


    public static String getCurrentRequestPath() {
        return urlPathHelper.getLookupPathForRequest (getRequest ());
    }


    public static String getRequestPath(HttpServletRequest request) {
        return urlPathHelper.getLookupPathForRequest (request);
    }

}
