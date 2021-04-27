package com.auth.framework.core.tokens.jwt.managers.session;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
@EqualsAndHashCode
public class Session {

    private final Map<String, Object> sessionDetails;

    public Session(JsonWebToken token) {
        sessionDetails = token.getParameters();
    }

}
