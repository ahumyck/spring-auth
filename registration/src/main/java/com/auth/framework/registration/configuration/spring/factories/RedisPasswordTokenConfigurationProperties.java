package com.auth.framework.registration.configuration.spring.factories;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis-properties")
@Data
public class RedisPasswordTokenConfigurationProperties {
    private String host;
    private Integer port;
    private Integer timeToLive;
}
