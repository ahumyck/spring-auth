package com.auth.framework.core.action.executor;

import com.auth.framework.core.action.Action;
import com.auth.framework.core.exceptions.UserHasNoAccessException;
import com.auth.framework.core.users.UserPrincipal;


/**
 * Эксекьютор, который выполняет определенный набор действия от лица пользователя
 */
public interface ActionExecutor {

    /**
     * @param principal - Пользователь, которые хочет выполнить какие-то действия
     * @param action    - Действие, которое хочет выполнить пользователь
     *
     * @throws UserHasNoAccessException - Данное исключение выбрасывается в случае,
     * когда у пользователя нет прав для выполнение экшена
     */
    Object executeAs(UserPrincipal principal, Action action) throws UserHasNoAccessException;
}
