package com.auth.framework.core.configuration.spring.factories;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("identity-properties")
@Data
class IdentityProviderProperties {
    private String encryptionPassword;
    private String jwtSignPassword;
    private boolean asymmetric;

    private String privateKeyPath;
    private String publicKeyPath;

    private Integer timeToLive;
    private Integer activeBefore;
    private Integer allowedClockSkew;
}
