package com.e_commerce.miscroservice.commons.utils.wx;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/15 15:30
 * @Copyright 玖远网络
 */
class HttpClientUtils{

    private static final Logger LOGGER = LoggerFactory.getLogger (HttpClientUtils.class);

    static String get(String url, Map<String, Object> param) {
        StringBuilder builder = new StringBuilder (url);
        builder.append ("?");

        for (Map.Entry<String, Object> entry : param.entrySet ()) {
            String key = entry.getKey ();
            Object value = entry.getValue ();
            builder.append (key).append ("=").append (String.valueOf (value)).append ("&");
        }
        return get (builder.toString ().substring (0, builder.length () - 1));
    }


    static String get(String url) {
        LOGGER.info ("get请求 url===>" + url);
        String responseBody = "";
        CloseableHttpClient httpclient = HttpClients.createDefault ();
        HttpGet httpGet = new HttpGet (url);
        try {

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine ().getStatusCode ();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity ();
                    return entity != null ? EntityUtils.toString (entity) : null;
                }
                else {
                    LOGGER.error ("Unexpected response status: " + status);
                    throw new ClientProtocolException ("Unexpected response status: " + status);
                }
            };

            responseBody = httpclient.execute (httpGet, responseHandler);
        } catch (Exception e) {
            LOGGER.error ("httpclient get error!" + e);
        } finally {
            try {
                httpclient.close ();
            } catch (IOException e) {
                LOGGER.error ("close httpclient error!" + e);
            }
        }

        LOGGER.error ("responseBody:" + responseBody);
        return responseBody;
    }


    static String post(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL (url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection ();
            // 设置通用的请求属性
            conn.setRequestProperty ("accept", "*/*");
            conn.setRequestProperty ("connection", "Keep-Alive");
            conn.setRequestProperty ("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput (true);
            conn.setDoInput (true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter (conn.getOutputStream ());
            // 发送请求参数
            out.print (param);
            // flush输出流的缓冲
            out.flush ();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader (
                    new InputStreamReader (conn.getInputStream ()));
            String line;
            while ((line = in.readLine ()) != null) {
                result += line;
            }
        } catch (Exception e) {
            LOGGER.error ("发送 POST 请求出现异常！" + e);
            e.printStackTrace ();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close ();
                }
                if (in != null) {
                    in.close ();
                }
            } catch (IOException ex) {
                ex.printStackTrace ();
            }
        }
        return result;
    }


    public static void main(String[] args) {

        String s = get ("http://storelocal.yujiejie.com/mobile/product/brand/list.json?type=1&page=1&search_brand=");
        System.out.println (s);
        System.out.println (s.length ());

    }


    private static void close(Closeable... closeable) {
        if (closeable != null && closeable.length > 0) {
            for (Closeable c : closeable) {
                try {
                    c.close ();
                } catch (IOException e) {
                    //ignore
                }
            }
        }

    }
}
