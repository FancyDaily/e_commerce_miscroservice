package com.miscroservice.module.module1.config;

import com.miscroservice.module.module1.entity.RedisTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RedisTestConfig {


    @PostConstruct
    public void init() {
        System.out.println("我初始化了111111111111");
    }

    @Bean
    public RedisTest newRedis() {
        return new RedisTest();
    }


}
