package com.auth.framework.core.tokens.jwt.identity;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.exceptions.ResolveOwnerException;
import com.auth.framework.exceptions.TokenGenerationException;

import java.util.Map;

/**
 * Механизм, генерирующий для пользователя уникально идентифицирующий его токен
 */
public interface IdentityProvider {

    /**
     * Для каждого пользователя должен быть сгенерирован уникальный токен
     *
     * @param username Для кого генерируются токен
     * @param parameters дополнительные параметры для создания токена
     * @return Токен, созданный с помощью фабрики
     * @see com.auth.framework.core.tokens.jwt.factory.TokenFactory
     */
    JsonWebToken generateTokenForUser(String username, Map<String, Object> parameters) throws TokenGenerationException;

    /**
     * @param rawToken значение токена, взятое из cookie/http header
     * @return имя пользователя, для которого был сгенерирован токен
     */
    String resolveOwner(String rawToken) throws ResolveOwnerException;
}
