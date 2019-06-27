package com.e_commerce.miscroservice.commons.config;

import com.e_commerce.miscroservice.commons.config.colligate.RedisTemplateConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 马晓晨
 * @date 2019/3/4
 */
@Configuration
public class RedisConfig extends RedisTemplateConfig {
	@Bean(name = "redisTemplate")
	public RedisTemplate create() {
		return createTemplateCache(OperateEnum.STR);
	}

	@Bean(name = "userRedisTemplate")
    public HashOperations<String, String, String> createUserRedisTemplate() {
        return createTemplateCache(OperateEnum.HASH).opsForHash();
    }

	@Bean(name = "csqRedisTemplate")
	public HashOperations<String, String, Object> createCsqRedisTemplate() {
		return createTemplateCache(OperateEnum.HASH).opsForHash();
	}

}
