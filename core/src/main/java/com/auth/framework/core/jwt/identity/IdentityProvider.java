package com.auth.framework.core.jwt.identity;

import com.auth.framework.core.jwt.Token;

/**
 * Механизм, генерирующий для пользователя уникально идентифицирующий его токен
 */
public interface IdentityProvider {

    /**
     * Для каждого пользователя должен быть сгенерирован уникальный токен
     *
     * @param username Для кого генерируются токен
     * @return Токен, созданный с помощью фабрики
     * @see com.auth.framework.core.jwt.factory.TokenFactory
     */
    Token generateTokenForUser(String username);

    /**
     * @param rawToken значение токена, взятое из cookie/http header
     * @return имя пользователя, для которого был сгенерирован токен
     */
    String resolveOwner(String rawToken);
}
