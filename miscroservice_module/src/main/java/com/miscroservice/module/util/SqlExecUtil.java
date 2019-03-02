package com.miscroservice.module.util;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * sql执行的工具类
 */
public class SqlExecUtil {


    private SqlExecUtil() {

    }

    /**
     * 执行sql
     * 返回结果
     * 1---empty  访问失败
     * 2----data: 返回数据
     * _____row: 返回影响的数目 (返回0表示操作影响数据为0)
     *
     * @param sql sql语句 不支持delete
     * @param url 访问的地址
     * @return
     */
    public static Map<String, Object> execSql(String sql, String url) {

        Map<String, Object> resultMap = new HashMap<>(2);
        HttpURLConnection conn = null;
        try {

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setDoOutput(Boolean.TRUE);
            conn.setDoInput(Boolean.TRUE);
            conn.setRequestProperty("uid", "e0fd34c18d6fa90bc973b67ba06d0a4f2d62e8e1e6a875aca8ff");
            conn.connect();
            DataOutputStream stream = new DataOutputStream(conn.getOutputStream());
            Map<String, Object> dataMap = new TreeMap<>();
            dataMap.put("sql", sql);
            Long time = System.currentTimeMillis();
            dataMap.put("time", time);
            dataMap.put("auth", Rc4Utils.toHexString(sql, time + ""));
            StringBuilder paramBuild = new StringBuilder(dataMap.size());

            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                paramBuild.append(entry.getKey());
                paramBuild.append("=");
                paramBuild.append(entry.getValue());
                paramBuild.append("&");

            }
            paramBuild.append("sign=" + md5(paramBuild.toString()));

            stream.write((paramBuild.toString()).getBytes());


            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                bos.write(arr, 0, len);
                bos.flush();
            }
            bos.close();
            bis.close();
            System.out.println(bos.toString());

            resultMap = JSONObject.parseObject(bos.toString("utf-8"), Map.class);

        } catch (Exception e) {

            System.err.printf("sql=%s访问url=%s失败%s", sql, url, e.getMessage());

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return resultMap;


    }


    public static void main(String[] args) {
        Map<String, Object> stringObjectMap = execSql("select  u.user_id AS a,u.type_id as b  from  user as u limit 1,20", "https://www.etongjin.net/activity/exec");
        System.out.println(stringObjectMap.get("data"));
        System.out.println(stringObjectMap.get("row"));
    }

    public static String md5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {

                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {

        }
        return result.toUpperCase();
    }


}
