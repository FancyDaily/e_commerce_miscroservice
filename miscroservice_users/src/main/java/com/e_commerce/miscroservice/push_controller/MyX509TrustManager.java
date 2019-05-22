package com.e_commerce.miscroservice.push_controller;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-22 19:48
 */

public class MyX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate certificates[], String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
