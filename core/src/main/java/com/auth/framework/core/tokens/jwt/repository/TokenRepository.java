package com.auth.framework.core.tokens.jwt.repository;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.params.TokenParameters;

import java.util.Collection;


/**
 * Обертка для общения с БД, где хранятся токены
 *
 * <p>
 * Если вы не хотите, чтобы токены хранились в базе данных то НЕОБХОДИМО сделать интерфейс-заглушку
 * </p>
 */
public interface TokenRepository {

    /**
     * @return все токены, которые есть в бд
     * <p>
     * В случае заглушки должен вернуть пустой список
     * </p>
     */
    Collection<JsonWebToken> findAll();

    /**
     * @param jsonWebToken токен, который необходимо записать в бд
     *                     <p>
     *                     В случае заглушки данный метод необходимо оставить пустым
     *                     </p>
     */
    void save(JsonWebToken jsonWebToken);


    /**
     * @param owner      имя юзера
     * @param parameters параметры сессии
     * @return токен юзера с заданными параметрами сессии
     */
    JsonWebToken findTokenByParameters(String owner, TokenParameters parameters);


    /**
     * @param owner имя юзера
     * @return все токены юзеры со всех сессий
     */
    Collection<JsonWebToken> findByOwner(String owner);

    /**
     * @param jsonWebToken токен, который необходимо удалить
     *                     <p>В случае заглушки данный метод необходимо оставить пустым</p>
     */
    void deleteToken(JsonWebToken jsonWebToken);


    JsonWebToken findByUsernameAndRawToken(String owner, String rawToken);


    void deleteByUsernameAndSession(String username, TokenParameters parameters);

    /**
     * @param owner токен пользователя, который необходимо удалить
     *              <p>В случае заглушки данный метод необходимо оставить пустым</p>
     */
    void deleteAllUserTokens(String owner);
}
