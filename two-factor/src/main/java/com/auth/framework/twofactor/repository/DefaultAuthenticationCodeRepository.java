package com.auth.framework.twofactor.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAuthenticationCodeRepository implements AuthenticationCodeRepository {

    private final Map<String, String> codeRepository = new ConcurrentHashMap<>();

    @Override
    public void save(String username, String code) {
        codeRepository.put(username, code);
    }

    @Override
    public String find(String username) {
        return codeRepository.get(username);
    }

    @Override
    public boolean removeUserCode(String username) {
        return codeRepository.remove(username) != null;
    }

    @Override
    public boolean removeCode(String code) {
        for (Map.Entry<String, String> userToCodeEntry : codeRepository.entrySet()) {
            if (code.equals(userToCodeEntry.getValue())) {
                return removeUserCode(userToCodeEntry.getKey());
            }
        }
        return false;
    }
}
