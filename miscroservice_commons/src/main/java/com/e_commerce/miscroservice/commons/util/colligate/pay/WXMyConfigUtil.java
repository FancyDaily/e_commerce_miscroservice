package com.e_commerce.miscroservice.commons.util.colligate.pay;

import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Description TODO
 * @ClassName WXMyConfigUtil
 * @Auhor huangyangfeng
 * @Date 2019-03-30 14:33
 * @Version 1.0
 */
public class WXMyConfigUtil implements WXPayConfig {
    private byte[] certData;

    public WXMyConfigUtil() throws Exception {
//        String certPath = "/src/main/resources/apiclient_cert.p12";//从微信商户平台下载的安全证书存放的目录
//        String certPath = "/data/code/xiaoshi_java/miscroservice_commons/src/main/resources/apiclient_cert.p12";//从微信商户平台下载的安全证书存放的目录

        File file = ResourceUtils.getFile("classpath:apiclient_cert.p12");
//        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return "wxd19384cf3643922a";
    }

    //parnerid
    @Override
    public String getMchID() {
        return "1514711641";
    }

    @Override
    public String getKey() {
        return "8kE3HYvJCznUJk7amiKE2CxGAwLSys5Y";
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}