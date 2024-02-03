package com.hg.bethunger.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

/**
 * @author Neil Alishev
 */
@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;

    @Value("${jwt_issuer}")
    private String issuer;

    private final String subject = "auth";

    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(7).toInstant());
        return JWT.create()
            .withSubject(subject)
            .withClaim("username", username)
            .withIssuedAt(new Date())
            .withIssuer(issuer)
            .withExpiresAt(expirationDate)
            .sign(Algorithm.HMAC256(secret));
    }

    // TODO split into validateToken and extractClaims
    public Map<String, Claim> validateTokenAndRetrieveClaims(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
            .withSubject(subject)
            .withIssuer(issuer)
            .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaims();
    }

    public void validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
            .withSubject(subject)
            .withIssuer(issuer)
            .build();

        verifier.verify(token);
    }

    public Map<String, Claim> extractClaims(String token) {
        return JWT.decode(token).getClaims();
    }
}
