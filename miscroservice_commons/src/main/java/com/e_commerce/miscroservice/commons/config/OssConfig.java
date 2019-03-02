package com.e_commerce.miscroservice.commons.config;

import com.e_commerce.miscroservice.commons.utils.OssKit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/11/5 10:08
 * @Copyright 玖远网络
 */
@Configuration
public class OssConfig{


    @Bean
    @ConfigurationProperties("oss.config")
    public OssKit createOssUtil() {
        return new OssKit ();
    }
}
