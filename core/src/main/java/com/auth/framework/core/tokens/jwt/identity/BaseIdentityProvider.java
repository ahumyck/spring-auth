package com.auth.framework.core.tokens.jwt.identity;

import com.auth.framework.core.constants.AuthenticationConstants;
import com.auth.framework.core.exceptions.WrongTypeSigningKeyException;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import com.auth.framework.core.tokens.jwt.params.TokenParameters;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.security.Key;

import static org.jose4j.jwa.AlgorithmConstraints.ConstraintType.PERMIT;

public abstract class BaseIdentityProvider implements IdentityProvider {

    protected JsonWebKey jsonWebKey;
    protected String allowedAlgorithm;
    protected Integer durationTime;
    protected Integer activeBefore;
    protected Integer allowedClockSkewInSeconds;
    protected TokenFactory factory;

    public BaseIdentityProvider(JsonWebKey jsonWebKey,
                                String allowedAlgorithm,
                                Integer durationTime,
                                Integer activeBefore,
                                Integer allowedClockSkewInSeconds,
                                TokenFactory factory) {
        this.jsonWebKey = jsonWebKey;
        this.allowedAlgorithm = allowedAlgorithm;
        this.durationTime = durationTime;
        this.activeBefore = activeBefore;
        this.allowedClockSkewInSeconds = allowedClockSkewInSeconds;
        this.factory = factory;
    }

    @Override
    public JsonWebToken generateTokenForUser(String username, String sessionName, TokenParameters parameters) {

        JwtClaims claims = new JwtClaims();
        claims.setExpirationTimeMinutesInTheFuture(durationTime);
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        claims.setNotBeforeMinutesInThePast(activeBefore); // time before which the token is not yet valid (2 minutes ago)
        claims.setSubject(username); // the subject/principal is whom the token is about
        claims.setStringClaim(AuthenticationConstants.SESSION_PARAMETER, sessionName);
        if (parameters != null) {
            parameters.forEach(claims::setClaim);
        }
        try {
            return factory.createToken(username, signToken(claims), durationTime, sessionName, parameters);
        } catch (JoseException | WrongTypeSigningKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String resolveOwner(String rawToken) {
        try {
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime() // the JWT must have an expiration time
                    .setVerificationKey(getPublicKey(jsonWebKey))
                    .setAllowedClockSkewInSeconds(allowedClockSkewInSeconds) // allow some leeway in validating time based claims to account for clock skew
                    .setRequireSubject() // the JWT must have a subject claim
                    .setJwsAlgorithmConstraints(PERMIT, allowedAlgorithm)
                    .build(); // create the JwtConsumer instance
            return jwtConsumer.processToClaims(rawToken).getSubject();
        } catch (WrongTypeSigningKeyException | InvalidJwtException | MalformedClaimException e) {
            throw new RuntimeException(e);
        }
    }


    private String signToken(JwtClaims claims) throws JoseException, WrongTypeSigningKeyException {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(jsonWebKey.getAlgorithm());
        jws.setKeyIdHeaderValue(jsonWebKey.getKeyId());
        jws.setKey(getPrivateKey(jsonWebKey));
        return jws.getCompactSerialization();
    }

    protected abstract Key getPrivateKey(JsonWebKey jsonWebKey) throws WrongTypeSigningKeyException;

    protected abstract Key getPublicKey(JsonWebKey jsonWebKey) throws WrongTypeSigningKeyException;
}
