package com.auth.framework.core.tokens.jwt.factory;

import com.auth.framework.core.tokens.jwt.JsonWebToken;

import java.util.Map;


/**
 * Фабрика для создания токенов
 *
 * @see TokenFactoryImpl пример создания токенов для InMemory хранилища
 */
public interface TokenFactory {

    /**
     * @param username для кого будет создан токен
     * @param rawToken подписанный токен в виде строке
     * @param duration время жизни токена, после выполнения текущего метода
     * @return Красиво упакованный Token
     */
    JsonWebToken createToken(String username,
                             String rawToken,
                             Integer duration,
                             Map<String, Object> parameters);
}
