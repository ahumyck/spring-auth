package com.auth.framework.core.configuration.spring.factories;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("identity-properties")
@Data
public class IdentityProviderProperties {
}
