/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.security.SecureRandom;


public class HttpUtils{

    private static Log logger = Log.getInstance(HttpUtils.class);


    public static class CookieMap extends HashMap<String, String>{
        private CookieMap(int size) {
            super(size);
        }

        public static CookieMap me(int size) {
            return new CookieMap (size);
        }

        public CookieMap putCookie(String key, String value) {
            super.put (key, value);
            return this;
        }

        public void putInResponse(HttpServletResponse response) {
            if (this.size () > 0) {
                for (Entry<String, String> entry : this.entrySet ()) {
                    response.addHeader (entry.getKey (), entry.getValue ());
                }
            }
        }
    }


    /**
     * 获取 HttpServletRequest
     */
    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes ()).getResponse ();
        return response;
    }

    public static String sendGet(String url) {
        return sendGet (url, new HashMap<> ());
    }


    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, Map<String, String> param) {
        StringBuilder result = new StringBuilder ();
        BufferedReader in = null;
        try {
            StringBuilder query = new StringBuilder ();

            for (Map.Entry<String, String> kv : param.entrySet ()) {
                query.append (URLEncoder.encode (kv.getKey (), "UTF-8")).append ("=");
                query.append (URLEncoder.encode (kv.getValue (), "UTF-8")).append ("&");
            }
            if (query.lastIndexOf ("&") > 0) {
                query.deleteCharAt (query.length () - 1);
            }

            String urlNameString = "";
            if(query.toString ().length()<1){
                urlNameString =url;
            }else {
                urlNameString = url + "?" + query.toString ();
            }
            URL realUrl = new URL (urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection ();
            // 设置通用的请求属性
            connection.setRequestProperty ("accept", "*/*");
            connection.setRequestProperty ("connection", "Keep-Alive");
            connection.setRequestProperty ("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect ();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields ();
            // 遍历所有的响应头字段
            for (String key : map.keySet ()) {
                System.out.println (key + "--->" + map.get (key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader (new InputStreamReader (connection.getInputStream ()));
            String line;
            while ((line = in.readLine ()) != null) {
                result.append (line);
            }
        } catch (Exception e) {
            System.out.println ("发送GET请求出现异常！" + e);
            e.printStackTrace ();
        } finally {
            IOUtils.close (in);
        }
        return result.toString ();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, String> param, Map<String, Object> headers) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder ();
        try {
            StringBuilder para = new StringBuilder ();
            for (String key : param.keySet ()) {
                para.append (key).append ("=").append (param.get (key)).append ("&");
            }
            if (para.lastIndexOf ("&") > 0) {
                para = new StringBuilder (para.substring (0, para.length () - 1));
            }
            String urlNameString = url + "?" + para;
            URL realUrl = new URL (urlNameString);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection ();
            // 设置通用的请求属性
            conn.setRequestProperty ("accept", "*/*");
            conn.setRequestProperty ("connection", "Keep-Alive");
            conn.setRequestProperty ("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //配置自定义header
            if (headers != null && ! headers.isEmpty ()) {
                for (Map.Entry<String, Object> entry : headers.entrySet ()) {
                    conn.setRequestProperty (entry.getKey (), String.valueOf (entry.getValue ()));
                }
            }
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
            in = new BufferedReader (new InputStreamReader (conn.getInputStream ()));
            String line;
            while ((line = in.readLine ()) != null) {
                result.append (line);
            }
        } catch (Exception e) {
            System.out.println ("发送 POST 请求出现异常！" + e);
            e.printStackTrace ();
        } finally {
            IOUtils.close (in);
            IOUtils.close (out);
        }
        return result.toString ();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, String> param) {
        return sendPost (url, param, null);
    }


    /**
     * 上传文件
     *
     * @param url  url
     * @param file file
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/19 19:50
     */
    public static String upload(String url, File file) {
        if (! file.exists () || ! file.isFile ()) {
            throw new RuntimeException ("文件不存在！");
        }
        OutputStream out = null;
        DataInputStream in = null;
        HttpURLConnection conn;
        BufferedReader reader = null;
        String result = null;
        try {
            URL urlObj = new URL (url);
            //连接
            conn = (HttpURLConnection) urlObj.openConnection ();

            conn.setRequestMethod ("POST");
            conn.setDoInput (true);
            conn.setDoOutput (true);
            conn.setUseCaches (false);

            //请求头
            conn.setRequestProperty ("Connection", "Keep-Alive");
            conn.setRequestProperty ("Charset", "UTF-8");
            //conn.setRequestProperty("Content-Type","multipart/form-data;");

            //设置边界
            String boundary = "----------" + System.currentTimeMillis ();
            conn.setRequestProperty ("Content-Type", "multipart/form-data;boundary=" + boundary);

            String sb = "--" +
                    boundary +
                    "\r\n" +
                    "Content-Disposition:form-data;name=\"file\";filename=\"" + file.getName () + "\"\r\n" +
                    "Content-Type:application/octet-stream\r\n\r\n";
            byte[] head = sb.getBytes ("utf-8");

            //输出流
            out = new DataOutputStream (conn.getOutputStream ());

            out.write (head);

            //文件正文部分
            in = new DataInputStream (new FileInputStream (file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read (bufferOut)) != - 1) {
                out.write (bufferOut, 0, bytes);
            }

            //结尾
            byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes ("utf-8");
            out.write (foot);
            out.flush ();

            //获取响应
            StringBuilder buffer = new StringBuilder ();
            reader = new BufferedReader (new InputStreamReader (conn.getInputStream ()));
            String line;
            while ((line = reader.readLine ()) != null) {
                buffer.append (line);
            }
            result = buffer.toString ();
        } catch (IOException e) {
            e.printStackTrace ();
        } finally {
            IOUtils.close (in);
            IOUtils.close (out);
            IOUtils.close (reader);
        }
        return result;
    }


    public static String webBaseUrl(HttpServletRequest request) {
        StringBuilder webBaseUrl = new StringBuilder ();
        webBaseUrl.append (request.getScheme ());
        webBaseUrl.append ("://");
        webBaseUrl.append (request.getServerName ());
        int port = request.getServerPort ();
        if (port != 80 && port != 0) {
            webBaseUrl.append (":");
            webBaseUrl.append (port);
        }
        String path = request.getContextPath ();
        if (StringUtils.isNotBlank (path)) {
            webBaseUrl.append ("/");
            webBaseUrl.append (path);
        }
        return webBaseUrl.toString ();
    }


    /**
     * URL编码（utf-8）
     *
     * @param source source
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/28 13:34
     */
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode (source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace ();
        }
        return result;
    }


    /**
     * 下载
     *
     * @param url url
     * @return java.io.InputStream
     * @author Charlie
     * @date 2018/9/28 16:00
     */
    public static InputStream download(String url) {
        DefaultHttpClient httpclient = new DefaultHttpClient ();
        HttpGet httpget = new HttpGet (url);
        httpget.setHeader ("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.79 Safari/537.1");
        httpget.setHeader ("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        InputStream in = null;
        try {
            HttpResponse resp = httpclient.execute (httpget);
            if (HttpStatus.SC_OK == resp.getStatusLine ().getStatusCode ()) {
                HttpEntity entity = resp.getEntity ();
                in = entity.getContent ();
                return cloneInputStream(in);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            IOUtils.close(in);
            httpclient.getConnectionManager ().shutdown ();
        }
        return null;
    }



    /**
     *
     * @param input input
     * @return java.io.InputStream
     * @author Charlie
     * @date 2019/1/4 11:52
     */
    public static InputStream cloneInputStream(InputStream input) {
        InputStream inputStream;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            inputStream = new ByteArrayInputStream(baos.toByteArray());
            return inputStream;
        } catch (IOException e) {
            logger.error("复制一个流,IOException");
            return null;
        }
        finally {
            IOUtils.close(baos);
        }
    }


    public static String postSSL(String url, String data, String certPath, String certPass) {
        okhttp3.OkHttpClient httpClient = new okhttp3.OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        okhttp3.RequestBody body = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), data);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();

        InputStream inputStream = null;
        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            //优先从classPath下拿
            ClassPathResource pathResource = new ClassPathResource (certPath);
            if (pathResource.exists ()) {
                inputStream = pathResource.getInputStream ();
            }
            else {
                inputStream = new FileInputStream(certPath);
            }
            char[] passArray = certPass.toCharArray();
            clientStore.load(inputStream, passArray);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientStore, passArray);
            KeyManager[] kms = kmf.getKeyManagers();
            SSLContext sslContext = SSLContext.getInstance("TLSv1");

            sslContext.init(kms, null, new SecureRandom());

            okhttp3.OkHttpClient httpsClient = new okhttp3.OkHttpClient()
                    .newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .build();

            okhttp3.Response response = httpsClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.close (inputStream);
        }
    }



    /**
     * 获取真实IP
     *
     * @return java.lang.String
     * @author Charlie
     * @date 2018/12/11 21:23
     */
    public static String getIpAddress() {
        return getIpAddress (WebUtil.getRequest ());
    }


    /**
     * 获取用户ip
     *
     * @param request request
     * @return java.lang.String
     * @author Charlie
     * @date 2018/7/24 17:10
     */
    public static String getIpAddress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if(index != -1){
                return XFor.substring(0,index);
            }else{
                return XFor;
            }
        }
        XFor = Xip;
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }


    /**
     * 做个简单的http接收请求的校验
     *
     * @param request request
     * @param md5Salt md5Salt
     * @param httpSignKey request请求中,签名对应的key
     * @param httpParams 参数
     * @return boolean
     * @author Charlie
     * @date 2018/10/29 9:40
     */
    public static boolean simpleVerify(HttpServletRequest request, String md5Salt, String httpSignKey, String... httpParams) {
        StringBuilder builder = new StringBuilder ();
        for (String param : httpParams) {
            String value = request.getParameter (param);
            if (StringUtils.isBlank (value)) {
                logger.info ("校验请求,未找到参数的值 md5Salt={}, param={}, fromIp={}, url={}", md5Salt, param, getIpAddress(request), request.getRequestURI ());
                return false;
            }
            builder.append (value);
        }
        builder.append (md5Salt);

        String sign = request.getParameter (httpSignKey);
        if (StringUtils.isBlank (sign)) {
            logger.info ("校验请求,未找到参数的值 httpSignKey={}, fromIp={}, url={}", httpSignKey, getIpAddress(request), request.getRequestURI ());
            return false;
        }
        boolean isLegal = Md5Util.md5 (builder.toString ()).equals (sign.toUpperCase ());
        if (! isLegal) {
            logger.warn ("请求验证未通过 fromIp={}, url={}", getIpAddress(request), request.getRequestURI ());
        }
        return isLegal;
    }

}
