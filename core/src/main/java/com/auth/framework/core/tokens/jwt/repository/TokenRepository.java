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
     * @param owner      имя пользователя, для которого необходимо найти токен в бд
     * @param parameters
     * @return токен пользователя
     * <p>
     * В случае заглушки необходимо вернуть NULL.
     */
    JsonWebToken findTokenByParameters(String owner, String sessionName, TokenParameters parameters);


    Collection<JsonWebToken> findByOwner(String owner);

    /**
     * @param jsonWebToken токен, который необходимо удалить
     *                     <p>В случае заглушки данный метод необходимо оставить пустым</p>
     */
    void deleteToken(JsonWebToken jsonWebToken);


    void deleteByUsernameAndSession(String username, String sessionName, TokenParameters parameters);

    /**
     * @param owner токен пользователя, который необходимо удалить
     *              <p>В случае заглушки данный метод необходимо оставить пустым</p>
     */
    void deleteAllUserTokens(String owner);
}
