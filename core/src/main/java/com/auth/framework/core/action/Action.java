package com.auth.framework.core.action;

import com.auth.framework.core.exceptions.ActionExecutionException;
import org.springframework.security.core.GrantedAuthority;


/**
 * Данный интерфейс должен использоваться только в том случае, если мы хотим выполнить какие-то действия пользователя
 *
 * @see com.auth.framework.core.action.executor.ActionExecutor ActionExecutor
 */
public interface Action<T> {

    /**
     * Действия, которые буду выполняться от лица пользователя
     */
    T execute() throws ActionExecutionException;


    /**
     * @return Права, которые должен иметь пользователь, чтобы выполнить действия
     */
    GrantedAuthority getAuthority();
}
