package com.auth.framework.core.tokens.jwt.managers;


import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.params.TokenParameters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Класс, который создает токен и добавляет его в ответ пользователя
 */
public interface TokenManager {

    /**
     * В обязанности входит
     * <p>
     * 1) Создать токен
     * </p>
     * <p>
     * 2) Записать его в хралище данных
     * </p>
     * <p>
     * 3) Добавить его в ответ пользователю
     * </p>
     *
     * @param response ответ пользователю
     * @param request  запрос пользователя
     * @param username для кого будет сгенерирован токен
     */
    void createTokenForUsername(HttpServletRequest request,
                                HttpServletResponse response,
                                String username,
                                TokenParameters parameters);


    /**
     * @param request запрос, в котором находится токен
     * @return имя пользователя, если токен был, или Optional.empty(), если токена не было
     */
    Optional<JsonWebToken> validateAndGetToken(HttpServletRequest request);
}
