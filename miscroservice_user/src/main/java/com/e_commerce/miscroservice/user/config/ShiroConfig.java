//package com.e_commerce.miscroservice.user.config;
//
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
///**
// * Shiro的配置中心
// */
//@Configuration
//public class ShiroConfig {
//
//    @Bean(name = "shiroFilter")
//    public ShiroFilterFactoryBean createShiroFactory(AuthorizingRealm realm) {
//        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
//        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
//        manager.setRealm(realm);
//        factoryBean.setSecurityManager(manager);
//        return factoryBean;
//    }
//
//
//}
