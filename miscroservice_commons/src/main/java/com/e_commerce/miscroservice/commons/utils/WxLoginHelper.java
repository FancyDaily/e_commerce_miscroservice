package com.e_commerce.miscroservice.commons.utils;


import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/21 16:06
 * @Copyright 玖远网络
 */
public class WxLoginHelper{

    private static final String SECRET = "yjj2018";
    private static final String SIGN = "wxSign";
    private static final String MEMBER_ID = "userId";

    private static Log logger = Log.getInstance (WxLoginHelper.class);


    /**
     * 小程序旧系统调用新系统,新旧系统没有保持会话状态,暂时在这里统一处理
     *
     * @author Charlie
     * @date 2018/11/21 16:14
     */
    public static Optional<Long> getUserId() {
        HttpServletRequest request = WebUtil.getRequest ();
//
//
//        DebugUtils.todo ("模拟登录");
//        if (true) {
//            return Optional.of (Long.parseLong (request.getParameter (MEMBER_ID)));
//        }

        Enumeration<String> names = request.getParameterNames ();
        List<String> nameList = new ArrayList<> (8);
        while (names.hasMoreElements ()) {
            String name = names.nextElement ();
            nameList.add (name);
        }

        if (nameList.size () < 2) {
            //至少有2个参数  storeId,memberId,sign
            return Optional.empty ();
        }
        else if (! nameList.contains (SIGN)) {
            //没有验签
            return Optional.empty ();
        }



        Collections.sort (nameList);
        StringBuilder builder = new StringBuilder (SECRET);
        for (String name : nameList) {
            if (SIGN.equals (name)) {
                //忽略验签参数
                continue;
            }
            String val = request.getParameter (name);
            builder.append (name)
                    .append (val);
        }
        builder.append (SECRET);
        String temp = builder.toString ();
        String validateSign = Md5Util.md5 (temp);

        //equals?
        if (request.getParameter (SIGN).equalsIgnoreCase (validateSign)) {
            try {
                return Optional.of (Long.parseLong (request.getParameter (MEMBER_ID)));
            } catch (Exception e) {
                logger.warn ("获取用户userId失败 userId={}", request.getParameter (MEMBER_ID));
                return Optional.empty ();
            }
        }
        return Optional.empty ();
    }


}
