package com.auth.framework.core.access;

import com.auth.framework.core.access.isAdmin.IsUserAdminValidatorDefault;
import com.auth.framework.core.role.AttributeGrantedAuthority;
import com.auth.framework.core.users.UserPrincipalStub;
import com.auth.framework.core.utils.ResourceFileProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class XMLAccessControlManagerTest {

    private UserPrincipalStub userPrincipal;
    private AccessControlManager controlManager;

    @BeforeEach
    public void setup() {
        controlManager = new XMLAccessControlManager(
                ResourceFileProvider.provideFullPath("authorities.xml"),
                new IsUserAdminValidatorDefault()
        );

        userPrincipal = UserPrincipalStub
                .builder()
                .username("username")
                .password("password")
                .attributes(Collections.singletonList(
                        new AttributeGrantedAuthority("Authority A")
                ))
                .build();
    }

    @Test
    public void haveRightsToA() {
        Assertions.assertTrue(controlManager.haveRights(userPrincipal, "localhost:8080/A"));
    }

    @Test
    public void haveRightsToB() {
        Assertions.assertTrue(controlManager.haveRights(userPrincipal, "localhost:8080/B"));
    }

    @Test
    public void haveRightsToF() {
        Assertions.assertTrue(controlManager.haveRights(userPrincipal, "localhost:8080/F"));

    }

    @Test
    public void haveRightsToAA() {
        Assertions.assertTrue(controlManager.haveRights(userPrincipal, "localhost:8080/A/A"));
    }

    @Test
    public void haveRightsToBD() {
        Assertions.assertTrue(controlManager.haveRights(userPrincipal, "localhost:8080/B/D"));
    }

    @Test
    public void haveRightsToBDF() {
        Assertions.assertTrue(controlManager.haveRights(userPrincipal, "localhost:8080/B/D/F"));
    }

    @Test
    public void haveNotRightsC() {
        Assertions.assertFalse(controlManager.haveRights(userPrincipal, "localhost:8080/C"));
    }

    @Test
    public void haveNotRightsD() {
        Assertions.assertFalse(controlManager.haveRights(userPrincipal, "localhost:8080/D"));
    }

    @Test
    public void haveNotRightsCA() {
        Assertions.assertFalse(controlManager.haveRights(userPrincipal, "localhost:8080/C/A"));
    }

    @Test
    public void haveNotRightsCB() {
        Assertions.assertFalse(controlManager.haveRights(userPrincipal, "localhost:8080/D/B"));
    }
}