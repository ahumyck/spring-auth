package com.auth.framework.core.configuration.spring.factories;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis-properties")
@Data
class RedisPasswordTokenConfigurationProperties {
    private String host;
    private Integer port;
    private Integer timeToLive;
}
