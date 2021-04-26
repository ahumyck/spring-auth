package com.auth.framework.core.jwt.factory;

import com.auth.framework.core.jwt.Token;


/**
 * Фабрика для создания токенов
 * @see com.auth.framework.core.jwt.redis.RedisTokenFactory Пример фабрики токенов для хранения в базе Redis
 */
public interface TokenFactory {

    /**
     * @param username для кого будет создан токен
     * @param rawToken подписанный токен в виде строке
     * @param duration время жизни токена, после выполнения текущего метода
     * @return Красиво упакованный Token
     */
    Token createToken(String username, String rawToken, Integer duration);
}
