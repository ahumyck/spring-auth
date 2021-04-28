package com.auth.framework.core.tokens.jwt.params;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;


@Data
public class TokenParameters implements Serializable {

    private static final long serialVersionUID = -5116758788574107043L;

    private final Map<String, Object> parameters;

    public TokenParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public TokenParameters() {
        this.parameters = new ConcurrentHashMap<>();
    }

    public void forEach(BiConsumer<? super String, ? super Object> action) {
        parameters.forEach(action);
    }

    public Map<String, Object> asMap() {
        return Collections.unmodifiableMap(parameters);
    }

    public Object get(String key) {
        return parameters.get(key);
    }

    public void put(String key, Object object) {
        parameters.put(key, object);
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "TokenParameters{" +
                "parameters=" + parameters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenParameters)) return false;
        TokenParameters that = (TokenParameters) o;
        return TokenParameters.equals(this, that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters);
    }

    public static class Builder {

        private final Map<String, Object> parameterMap = new ConcurrentHashMap<>();

        private Builder() {

        }

        public Builder addParameter(String key, Object value) {
            this.parameterMap.put(key, value);
            return this;
        }

        public TokenParameters build() {
            return new TokenParameters(parameterMap);
        }
    }


    public static boolean equals(TokenParameters o1, TokenParameters o2) {
        if (o1 == null) {
            if (o2 == null) {
                return true;
            } else return o2.asMap().isEmpty();
        }
        if (o2 == null) {
            return o1.asMap().isEmpty();
        }
        return Objects.equals(o1.asMap(), o2.asMap());
    }
}
