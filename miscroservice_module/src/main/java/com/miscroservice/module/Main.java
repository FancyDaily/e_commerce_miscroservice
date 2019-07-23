package com.miscroservice.module;

import com.jcraft.jsch.*;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.config.ConfigurationManager;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    private static final String URL_SUFFIX = "modules/load";


    private static final Pattern namePattern = Pattern.compile("[a-zA-Z]+-\\d+_\\d+(?=.jar)");

    private static final String SUCCESS_SIGN = "200";
    private static final String EXIST_SIGN = "400";
    private static final String EMPTY = "";

    /**
     * 执行java -jar ./xx.jar 后面接任何参数表示正式环境
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

        if (args == null || args.length != 1) {
            System.err.println("请输入正确的服务名称");
            return;
        }

        String classPathName = System.getProperty("java.class.path");

        Matcher matcher = namePattern.matcher(classPathName == null ? EMPTY : classPathName);
        if (!matcher.find()) {
            return;
        }

        uploadFile("root", "yjj.p4j.cc", 22, classPathName);


        notifyRemote(args[0], matcher.group());


    }

    /**
     * 上传文件
     *
     * @param username 用户姓名
     * @param host     连接的host
     * @param port     连接的端口
     * @param filename 上传的文件名
     * @throws JSchException
     * @throws SftpException
     * @throws IOException
     */
    private static void uploadFile(String username, String host, int port, String filename) throws JSchException, SftpException, IOException {

        JSch jsch = new JSch();
        jsch.addIdentity(System.getProperty("user.home") + File.separator + ".ssh/id_rsa");
        Session session = jsch.getSession(username, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(2000);

        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();

        channelSftp.cd("/home/download");

        File file = Paths.get(filename).toFile();
        InputStream in = new FileInputStream(file);
        channelSftp.put(in, file.getName());
        in.close();
        session.disconnect();

    }

    /**
     * 通知远程服务
     *
     * @param serviceName 远程服务的名称
     * @param jarName     注册服务名称
     * @throws Exception
     */
    private static void notifyRemote(String serviceName, String jarName) {

        List<InstanceInfo> serviceList = getServiceList(serviceName);
        if (serviceList.isEmpty()) {
            System.err.printf("所填服务%s不正确", serviceName);
            return;
        }

        for (InstanceInfo instanceInfo : serviceList) {

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(instanceInfo.getHomePageUrl() + URL_SUFFIX).openConnection();

                conn.setRequestMethod("POST");
                conn.setConnectTimeout(3000);
                conn.setDoOutput(Boolean.TRUE);
                conn.setDoInput(Boolean.TRUE);
                conn.connect();
                DataOutputStream stream = new DataOutputStream(conn.getOutputStream());


                stream.write(("name=" + jarName).getBytes());
                stream.flush();
                stream.close();

                InputStream readInputStream = conn.getInputStream();
                if (readInputStream.available() < 0) {
                    readInputStream = conn.getErrorStream();
                }
                String result = new BufferedReader(new InputStreamReader(readInputStream)).lines().collect(Collectors.joining());

                if (result.contains(SUCCESS_SIGN)) {
                    System.out.println("exec success");
                } else if ((result.contains(EXIST_SIGN))) {
                    System.out.println("has registered");

                } else {
                    System.out.println("exec fail ");
                }
                readInputStream.close();
                conn.disconnect();
            } catch (IOException e) {
                continue;

            }

        }
    }

    /**
     * 获取服务的地址
     *
     * @param serviceName 服务的名称
     * @return
     */
    private static List<InstanceInfo> getServiceList(String serviceName) {
        try {
            Properties properties = new Properties();
            properties.put("eureka.serviceUrl.default", getDnsTextVal());
            ConfigurationManager.loadProperties(properties);
            DiscoveryClient discoveryClient = new DiscoveryClient(new ApplicationInfoManager(new MyDataCenterInstanceConfig()),
                    new DefaultEurekaClientConfig());
            return discoveryClient.getInstancesByVipAddress(serviceName, true);
        } catch (Exception var6) {
            return new ArrayList<>();
        }
    }

    /**
     * 获取注册中心地址
     *
     * @return
     * @throws NamingException
     */
    private static String getDnsTextVal() throws NamingException {
        String dnsVal = "";
        Hashtable<String, String> env = new Hashtable();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        DirContext dirContext = new InitialDirContext(env);
        Attributes attrs = dirContext.getAttributes("eureka.test.register.p4j.cc", new String[]{"TXT"});
        if (attrs == null) {
            return dnsVal;
        } else {
            Attribute next = attrs.getAll().next();
            return next == null ? dnsVal : (String) next.get();
        }
    }
}
