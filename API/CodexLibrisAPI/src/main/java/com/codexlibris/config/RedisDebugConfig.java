package com.codexlibris.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisDebugConfig {

    @Value("${spring.redis.host:NOT_DEFINED}")
    private String redisHost;

    @Value("${spring.redis.port:0}")
    private int redisPort;

    @PostConstruct
    public void logRedisConfig() {
        System.out.println("🔍 Redis host configurado: " + redisHost);
        System.out.println("🔍 Redis puerto configurado: " + redisPort);
    }
}
