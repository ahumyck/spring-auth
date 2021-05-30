package com.auth.framework.core.configuration.spring.factories;


import com.auth.framework.core.tokens.jwt.transport.TokenTransportType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("transport-properties")
@Data
class TokenTransportProperties {

    private String fieldName;
    private TokenTransportType type;




}


