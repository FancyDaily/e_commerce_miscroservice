/*
package com.e_commerce.miscroservice.commons.config.colligate;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.Iptools;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ValueOperations;

*/
/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/16 15:25
 * @Copyright 玖远网络
 *//*


@Configuration
public class FilterConfig {
    private Log logger = Log.getInstance(FilterConfig.class);
    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;


    public static final String INTERCEPTOR_PATH = "/*";
    private final int FIRST_ORDER = 1;
    private final int SECOND_ORDER = 2;
    private static final String COOKIE_NAME_SESSION = "yjj_sess";
    private static final String COOKIE_ENCRYPT_KEY = "=W!xB^&0U5AkNr]FQz{S?B+OU[OAa90J";
    private static final String COOKIE_PART_SPLIT = "#";
    public FilterConfig() {
    }


    @Bean
    @ConditionalOnExpression("${filter.path.enabled}")
    public FilterRegistrationBean createPathFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new FilterConfig.PathFilter());
        filterRegistrationBean.addUrlPatterns(new String[]{"/*"});
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    @ConditionalOnExpression("${filter.auth.enabled}")
    public FilterRegistrationBean createAuthFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new FilterConfig.AuthFilter());
        filterRegistrationBean.addUrlPatterns(new String[]{"/*"});
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }

    private class AuthFilter implements Filter {
        private AuthFilter() {
        }

        public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
        }

        public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest)request;
            if (req.getHeader("forwardUrl") == null) {
                HttpServletResponse response = (HttpServletResponse)res;
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
                response.addHeader("Access-Control-Expose-Headers", "token");
                response.addHeader("Access-Control-Allow-Headers", "uid,token");
                String token = req.getHeader("token");
                String uid = req.getHeader("uid");
                if (StringUtils.isNoneEmpty(new CharSequence[]{token, uid})) {
                    ;
                }
            } else {
                String id = req.getHeader("id");
                if (StringUtils.isNotEmpty(id)) {
                    IdUtil.setId(Integer.valueOf(id));
                }else {
                    String cookieValue = getCookieValue(req,COOKIE_NAME_SESSION, null);
                    if (cookieValue == null) {
                        cookieValue = (String) req.getSession().getAttribute(COOKIE_NAME_SESSION);
                    }
                    logger.error("OldModelCookieValue 值={}",cookieValue);
                    if (cookieValue!=null){
                        String result = AESUtil.decrypt(cookieValue, "UTF-8", COOKIE_ENCRYPT_KEY);
                        if (StringUtils.isNotBlank(result)) {
                            String[] oldModelValueKeyArray = StringUtils.split(result, COOKIE_PART_SPLIT);
                            logger.error("oldModelValueKeyArray 值={}",oldModelValueKeyArray);
                            if (oldModelValueKeyArray!=null&&oldModelValueKeyArray.length>0){
                                String key = oldModelValueKeyArray[0];
                                String oldModelId = userStrHashRedisTemplate.get(key);
                                logger.error("oldModelId 值={}",oldModelValueKeyArray);
                                if (StringUtils.isNotEmpty(oldModelId)){
                                    IdUtil.setId(Integer.valueOf(oldModelId));
                                }
                            }
                        }
                    }
                }
            }

            Log.traceRequest(req);
            chain.doFilter(request, res);
            Log.clearTrace();
        }

        public void destroy() {
        }
    }
    public String getCookieValue(HttpServletRequest request, String cookieName, String defaultValue) {
        Cookie cookieList[] = request.getCookies();
        if (cookieList == null || cookieName == null)
            return defaultValue;
        for (int i = 0; i < cookieList.length; i++) {
            try {
                if (cookieList[i].getName().equals(cookieName))
                    return cookieList[i].getValue();
            } catch (Exception e) {
                logger.error("Cookie", e);
            }
        }
        return defaultValue;
    }
    private class PathFilter implements Filter {
        private final String CONFIG_NAME;
        private Properties properties;
        private Log log;

        private PathFilter() {
            this.CONFIG_NAME = "properties/pathFilter.properties";
            this.properties = new Properties();
            this.log = Log.getInstance(FilterConfig.PathFilter.class);

            try {
                this.properties.load((new ClassPathResource("properties/pathFilter.properties")).getInputStream());
            } catch (Exception var3) {
                this.log.error("加载路径拦截器配置文件出错", new Object[]{var3});
            }

        }

        public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
        }

        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest)request;
            String path = req.getRequestURI();
            String[] pathArr = path.split("/");
            String allowedPaths = this.properties.getProperty(pathArr.length == 0 ? "" : pathArr[pathArr.length - 1]);
            Boolean allowedPassFlag = Boolean.FALSE;
            String ip = Iptools.gainRealIp(req);
            if (StringUtils.isNotEmpty(allowedPaths)) {
                String[] pathsArr = allowedPaths.split(",");
                String[] var11 = pathsArr;
                int var12 = pathsArr.length;

                for(int var13 = 0; var13 < var12; ++var13) {
                    String str = var11[var13];
                    if (!StringUtils.isEmpty(str)) {
                        if (str.contains(":")) {
                            String[] sp = str.split(":");
                            if (req.getHeader(sp[0]).equals(sp[1])) {
                                allowedPassFlag = Boolean.TRUE;
                                break;
                            }
                        } else if (str.equals(ip)) {
                            allowedPassFlag = Boolean.TRUE;
                            break;
                        }
                    }
                }
            } else {
                allowedPassFlag = Boolean.TRUE;
            }

            if (allowedPassFlag) {
                this.log.info("Ip={} 访问路径={} 允许通过", new Object[]{ip, path});
                Log.traceRequest(req);
                chain.doFilter(request, response);
                Log.clearTrace();
            } else {
                this.log.info("Ip={} 访问路径={} 不允许通过", new Object[]{ip, path});
            }

        }

        public void destroy() {
        }
    }
}
*/
