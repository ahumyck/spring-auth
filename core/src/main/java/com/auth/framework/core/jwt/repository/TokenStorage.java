package com.auth.framework.core.jwt.repository;

import com.auth.framework.core.jwt.Token;

import java.util.Collection;


/**
 * Обертка для общения с БД, где хранятся токены
 *
 * <p>
 * Если вы не хотите, чтобы токены хранились в базе данных то НЕОБХОДИМО сделать интерфейс-заглушку
 * </p>
 */
public interface TokenStorage {

    /**
     * @return все токены, которые есть в бд
     * <p>
     * В случае заглушки должен вернуть пустой список
     * </p>
     */
    Collection<Token> findAll();

    /**
     * @param token токен, который необходимо записать в бд
     *              <p>
     *              В случае заглушки данный метод необходимо оставить пустым
     *              </p>
     */
    void save(Token token);

    /**
     * @param owner имя пользователя, для которого необходимо найти токен в бд
     * @return токен пользователя
     * <p>
     * В случае заглушки необходимо вернуть ЛЮБОЙ НЕ NULL объект, т.е.
     * данный метод должен быть реализован примерно так:
     * <p>
     * Token findByOwner(String owner) {
     * return new Object();
     * }
     * </p>
     */
    Token findByOwner(String owner);

    /**
     * @param token токен, который необходимо удалить
     *              <p>В случае заглушки данный метод необходимо оставить пустым</p>
     */
    void deleteToken(Token token);

    /**
     * @param owner токен пользователя, который необходимо удалить
     *              <p>В случае заглушки данный метод необходимо оставить пустым</p>
     */
    void deleteTokenByOwner(String owner);
}
