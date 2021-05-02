package com.auth.framework.core.configuration.spring.factories;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("authentication")
@Data
public class AuthenticationFrameworkProperties {
    private boolean enableTokens;
    private String encryptionServicePassword;
    private String jsonWebTokenPassword;

    private String adminRoleName;
}
