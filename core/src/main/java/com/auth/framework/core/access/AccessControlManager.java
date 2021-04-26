package com.auth.framework.core.access;

import com.auth.framework.core.users.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

public interface AccessControlManager {

    boolean haveRights(UserPrincipal user, String resource);

}
