package com.auth.framework.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("token-storage")
@Data
public class TokenStorageConfigurationProperties {
    private boolean useDefaultRedisStorage;
    private String host;
    private Integer port;
}
