package com.auth.framework.core.tokens.jwt.params;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;


public class TokenParameters {

    private final Map<String, Object> parameters;

    public TokenParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public TokenParameters() {
        this.parameters = new ConcurrentHashMap<>();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return Collections.unmodifiableSet(parameters.entrySet());
    }

    public void forEach(BiConsumer<? super String, ? super Object> action) {
        parameters.forEach(action);
    }

    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
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
}
