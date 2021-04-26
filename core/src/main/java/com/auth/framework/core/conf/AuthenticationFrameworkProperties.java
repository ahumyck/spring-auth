package com.auth.framework.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("authentication")
@Data
public class AuthenticationFrameworkProperties {
    private boolean enableTokens;
    private String encryptionServicePassword;
    private String jsonWebTokenPassword;

    private String enableAttributeModel;
    private String adminRoleName;

    @ConfigurationProperties("identity-provider-properties")
    @Data
    public static class IdentityProviderProperties {
        private boolean property;
    }
}
