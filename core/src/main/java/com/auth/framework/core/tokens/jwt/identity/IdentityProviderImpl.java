package com.auth.framework.core.tokens.jwt.identity;

import com.auth.framework.core.constants.AuthenticationConstants;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import com.auth.framework.core.tokens.jwt.params.TokenParameters;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import static org.jose4j.jwa.AlgorithmConstraints.ConstraintType.PERMIT;

public class IdentityProviderImpl implements IdentityProvider {

    private final PublicJsonWebKey jsonWebKey;
    private final String allowedAlgorithm;
    private final Integer durationTime;
    private final Integer activeBefore;
    private final Integer allowedClockSkewInSeconds;
    private final TokenFactory factory;


    public IdentityProviderImpl(PublicJsonWebKey jsonWebKey,
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


    private String signToken(JwtClaims claims) throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(jsonWebKey.getAlgorithm());
        jws.setKeyIdHeaderValue(jsonWebKey.getKeyId());
        jws.setKey(jsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(jsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(jsonWebKey.getAlgorithm());
        return jws.getCompactSerialization();
    }

    @Override
    public JsonWebToken generateTokenForUser(String username, String sessionName, TokenParameters parameters) {
        try {
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
            return factory.createToken(username, signToken(claims), durationTime, sessionName, parameters);
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String resolveOwner(String rawToken) {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setVerificationKey(jsonWebKey.getPublicKey())
                .setAllowedClockSkewInSeconds(allowedClockSkewInSeconds) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject() // the JWT must have a subject claim
                .setJwsAlgorithmConstraints(PERMIT, allowedAlgorithm)
                .build(); // create the JwtConsumer instance
        try {
            return jwtConsumer.processToClaims(rawToken).getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
