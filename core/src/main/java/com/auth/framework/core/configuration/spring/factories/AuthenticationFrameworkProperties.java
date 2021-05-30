package com.auth.framework.core.configuration.spring.factories;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("authentication-properties")
@Data
class AuthenticationFrameworkProperties {
    private String adminRoleName;
}
