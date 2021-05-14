package com.auth.framework.core.sessions;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SessionManagerImpl implements SessionManager {

    private final TokenRepository tokenRepository;

    public SessionManagerImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    public void killSession(Session session) {
        tokenRepository.deleteByOwnerAndParameters(session.getUsername(), session.getParameters());
    }

    @Override
    public List<Session> getAllSessionsForUsername(String username) {
        return tokenRepository
                .findByOwner(username)
                .stream()
                .map(Session::new)
                .collect(Collectors.toList());
    }

    @SafeVarargs
    @Override
    public final List<Session> getFilteredSessionForUsername(String username, Predicate<? super JsonWebToken>... predicates) {
        Supplier<Stream<Predicate<? super JsonWebToken>>> predicateStreamSupplier = () -> Arrays.stream(predicates);
        return tokenRepository
                .findByOwner(username)
                .stream()
                .filter(token -> predicateStreamSupplier.get().anyMatch(predicate -> predicate.test(token)))
                .map(Session::new)
                .collect(Collectors.toList());
    }

}
