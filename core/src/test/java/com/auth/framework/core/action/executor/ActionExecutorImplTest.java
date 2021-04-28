package com.auth.framework.core.action.executor;

import com.auth.framework.core.access.admin.AdminValidatorWithInjectedAdminUserRoleName;
import com.auth.framework.core.action.Action;
import com.auth.framework.core.action.ActionStub;
import com.auth.framework.core.exceptions.ActionExecutionException;
import com.auth.framework.core.exceptions.UserHasNoAccessException;
import com.auth.framework.core.users.UserPrincipal;
import com.auth.framework.core.users.UserPrincipalStub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

class ActionExecutorImplTest {

    private final String adminRoleName = "admin";
    private final String userRoleName = "user";
    private Action<Object> simpleAction;
    private Action<Object> actionForAdmins;
    private UserPrincipal simpleUser;
    private UserPrincipal admin;
    private final ActionExecutor actionExecutor = new ActionExecutorImpl(new AdminValidatorWithInjectedAdminUserRoleName(adminRoleName));


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
        } catch (UserHasNoAccessException | ActionExecutionException e) {
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
        } catch (ActionExecutionException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void simpleActionWithAdmin() {
        try {
            actionExecutor.executeAs(admin, simpleAction);
            Assertions.assertTrue(true);
        } catch (UserHasNoAccessException | ActionExecutionException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void adminActionWithAdmin() {
        try {
            actionExecutor.executeAs(admin, actionForAdmins);
            Assertions.assertTrue(true);
        } catch (UserHasNoAccessException | ActionExecutionException e) {
            Assertions.fail(e.getMessage());
        }
    }
}