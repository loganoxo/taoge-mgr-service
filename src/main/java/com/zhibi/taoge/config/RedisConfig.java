package com.zhibi.taoge.config;

import com.zhibi.taoge.common.generator.RedisObjectIdGenerator;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by qinhe_admin on 2017/7/18.
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisObjectIdGenerator redisIdGenerator(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();
        RedisObjectIdGenerator redisIdGenerator = new RedisObjectIdGenerator();
        redisIdGenerator.setTemplate(redisTemplate);
        return redisIdGenerator;
    }

    @Bean
    public RedisTemplate adminRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {

        RedisStandaloneConfiguration standaloneConfiguration = lettuceConnectionFactory.getStandaloneConfiguration();
        LettuceClientConfiguration clientConfiguration = lettuceConnectionFactory.getClientConfiguration();
        standaloneConfiguration.setDatabase(5);
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(standaloneConfiguration, clientConfiguration);

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


//    @Bean
//    public CacheManager cacheManager(RedisTemplate normalRedisTemplate) {
//        normalRedisTemplate.afterPropertiesSet();
//        RedisCacheManager cacheManager = new RedisCacheManager(normalRedisTemplate);
//        cacheManager.setUsePrefix(true);
//        cacheManager.setDefaultExpiration(43200);
//        Map<String, Long> expiresMap = new HashMap<>();
//        expiresMap.put("Task", 3600 * 72L);
//        expiresMap.put("User", 3600 * 72L);
//        expiresMap.put("Alitm", 3600 * 72L);
//        expiresMap.put("UserMember", 3600 * 24 * 3650L);
//        expiresMap.put("RewardRules", 3600 * 24 * 3650L);
//        expiresMap.put("UnRegInfo", 3600 * 24 * 3650L);
//        expiresMap.put("UserVerify", 3600 * 24 * 3650L);
//        expiresMap.put("Tag", 3600 * 24L);
//        cacheManager.setExpires(expiresMap);
//        return cacheManager;
//    }
}