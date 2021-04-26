package com.auth.framework.core.action.executor;

import com.auth.framework.core.access.isAdmin.IsUserAdminValidatorWithInjectedAdminRoleName;
import com.auth.framework.core.action.Action;
import com.auth.framework.core.action.ActionStub;
import com.auth.framework.core.exceptions.access.UserHasNoAccessException;
import com.auth.framework.core.jwt.repository.RedisTokenStorage;
import com.auth.framework.core.jwt.repository.TokenStorage;
import com.auth.framework.core.jwt.repository.low.TokenRedisRepositoryImpl;
import com.auth.framework.core.users.UserPrincipal;
import com.auth.framework.core.users.UserPrincipalStub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

class ActionExecutorImplTest {

    private final String adminRoleName = "admin";
    private final String userRoleName = "user";
    private Action simpleAction;
    private Action actionForAdmins;
    private UserPrincipal simpleUser;
    private UserPrincipal admin;
    private final ActionExecutor actionExecutor = new ActionExecutorImpl(new IsUserAdminValidatorWithInjectedAdminRoleName(adminRoleName));


    @BeforeEach
    public void setup() {
        SimpleGrantedAuthority userGrantedAuthority = new SimpleGrantedAuthority(userRoleName);
        SimpleGrantedAuthority adminGrantedAuthority = new SimpleGrantedAuthority(adminRoleName);

        simpleAction = new ActionStub(userGrantedAuthority, "nop");
        actionForAdmins = new ActionStub(adminGrantedAuthority, "nop");
        simpleUser = new UserPrincipalStub("user1",
                null,
                Collections.singletonList(userGrantedAuthority),
                null);
        admin = new UserPrincipalStub("user2",
                null,
                Collections.singletonList(adminGrantedAuthority),
                null);
    }

    @Test
    public void simpleActionWithSimpleUser() {
        try {
            actionExecutor.executeAs(simpleUser, simpleAction);
            Assertions.assertTrue(true);
        } catch (UserHasNoAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void adminActionWithSimpleUser() {
        try {
            actionExecutor.executeAs(simpleUser, actionForAdmins);
            Assertions.fail("UserHasNoAccessException expected");
        } catch (UserHasNoAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void simpleActionWithAdmin() {
        try {
            actionExecutor.executeAs(admin, simpleAction);
            Assertions.assertTrue(true);
        } catch (UserHasNoAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void adminActionWithAdmin() {
        try {
            actionExecutor.executeAs(admin, actionForAdmins);
            Assertions.assertTrue(true);
        } catch (UserHasNoAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }
}