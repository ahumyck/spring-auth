package com.auth.framework.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis-properties")
@Data
public class RedisPasswordConfigurationProperties {
    private String host;
    private Integer port;
    private Integer timeToLive;
}
