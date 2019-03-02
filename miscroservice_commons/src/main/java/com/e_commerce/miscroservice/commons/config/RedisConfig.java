package com.e_commerce.miscroservice.commons.config;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.RedisTemplateConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.*;

import javax.annotation.Resource;

@Configuration
public class RedisConfig extends RedisTemplateConfig {


    @Bean(name = "userHashRedisTemplate")
    public HashOperations<String, String, String> createUserHashRedisTemplate() {
        return createTemplateCache(OperateEnum.HASH).opsForHash();
    }

    @Bean(name = "userStrHashRedisTemplate")
    public ValueOperations<String, String> createStrHashRedisTemplate() {
        return createTemplateCache(OperateEnum.STR).opsForValue();
    }

    @Bean(name = "intRedisTemplate")
    public ValueOperations<String, Integer> createIntRedisTemplate() {
        return createTemplateCache(OperateEnum.DEFAULT).opsForValue();
    }

    @Bean(name = "userJsonObjHashRedisTemplate")
    public ValueOperations<String,JSONObject> createJsonObjHashRedisTemplate() {
//        RedisTemplate<String,JSONObject> templateCache = createTemplateCache(OperateEnum.DEFAULT);
//        return templateCache;
        return createTemplateCache(OperateEnum.DEFAULT).opsForValue();
    }


    @Bean(name = "strRedisTemplate")
    public RedisTemplate createRedisTemplate() {
        return createTemplateCache (OperateEnum.STR);
    }

    @Bean(name = "hashRedisTemplate")
    public RedisTemplate createHashRedisTemplate() {
        return createTemplateCache (OperateEnum.HASH);
    }

    @Bean( name = "zSetRedisTemplate" )
    public ZSetOperations<String, Object> createZSet() {
        return createTemplateCache(OperateEnum.DEFAULT).opsForZSet();
    }

    @Bean( name = "listStringRedisTemplate" )
    public ListOperations<String, String> createListString() {
        return createTemplateCache(OperateEnum.DEFAULT).opsForList();
    }

}
