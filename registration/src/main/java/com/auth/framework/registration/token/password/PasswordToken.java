package com.auth.framework.registration.token.password;

import java.io.Serializable;
import java.util.Date;

public interface PasswordToken extends Serializable {

    String getOwner();

    String getToken();

    Integer getDuration();

    Date getExpireDate();
}
