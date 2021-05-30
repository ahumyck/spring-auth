package com.auth.framework.twofactor.repository;

public interface AuthenticationCodeRepository {

    void save(String username, String code);

    String find(String username);

    boolean removeUserCode(String username);

    boolean removeCode(String code);
}
