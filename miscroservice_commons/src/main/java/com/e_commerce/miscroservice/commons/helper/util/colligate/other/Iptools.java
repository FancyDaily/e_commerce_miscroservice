//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.e_commerce.miscroservice.commons.helper.util.colligate.other;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

import com.e_commerce.miscroservice.commons.utils.WebUtil;
import org.apache.commons.lang3.StringUtils;

public class Iptools {
    private static Log logger = Log.getInstance(Iptools.class);

    public Iptools() {
    }


    /**
     * 获取真实IP
     *
     * @return java.lang.String
     * @author Charlie
     * @date 2018/12/11 21:23
     */
    public static String gainRealIp() {
        return gainRealIp (WebUtil.getRequest ());
    }

    public static String gainRealIp(HttpServletRequest request) {
        String ipAddress = "";

        try {
            if (request == null) {
                return ipAddress;
            }

            if (ipAddress != null && !ipAddress.isEmpty()) {
                return ipAddress;
            }

            if ("127.0.0.1".equals(request.getServerName()) || "localhost".equals(request.getServerName())) {
                ipAddress = "127.0.0.1";
                return ipAddress;
            }

            ipAddress = request.getHeader("X-Forwarded-For");
            if (StringUtils.isNoneEmpty(new CharSequence[]{ipAddress})) {
                if (ipAddress.contains(",")) {
                    String[] var7 = ipAddress.split(",");
                    int var8 = var7.length;

                    for(int var9 = 0; var9 < var8; ++var9) {
                        String ip_add = var7[var9];
                        ipAddress = ip_add;
                        if (StringUtils.isNoneEmpty(new CharSequence[]{ip_add})) {
                            break;
                        }
                    }
                }

                return ipAddress;
            }

            ipAddress = request.getRemoteAddr();
            if (!StringUtils.isEmpty(ipAddress)) {
                return ipAddress;
            }

            ipAddress = request.getRemoteHost();
            if (StringUtils.isNotEmpty(ipAddress)) {
                return ipAddress;
            }

            ipAddress = request.getHeader("Referer");
            ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }

            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("X-Real-IP");
                if (ipAddress != null && (ipAddress.equals("") || ipAddress.endsWith("0:0:0:0:0:0:0:1"))) {
                    Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();

                    while(allNetInterfaces.hasMoreElements()) {
                        NetworkInterface netInterface = (NetworkInterface)allNetInterfaces.nextElement();
                        Enumeration addresses = netInterface.getInetAddresses();

                        while(addresses.hasMoreElements()) {
                            InetAddress ip = (InetAddress)addresses.nextElement();
                            if (ip != null && ip instanceof Inet4Address) {
                                ipAddress = ip.getHostAddress();
                            }
                        }
                    }
                }
            }

            if (ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        } catch (Exception var6) {
            logger.error("ip{},解析异常", new Object[]{ipAddress, var6});
        }

        return ipAddress;
    }
}
