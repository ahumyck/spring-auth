package com.auth.framework.core.configuration.spring.factories;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis-jwt-properties")
@Data
public class RedisJwtTokenConfigurationProperties {
    private String host;
    private Integer port;
    private Integer timeToLive;
}
