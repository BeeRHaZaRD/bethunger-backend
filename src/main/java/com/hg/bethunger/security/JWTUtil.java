package com.hg.bethunger.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * @author Neil Alishev
 */
@Component
public class JWTUtil {
    @Value("${bethunger.jwt.secret}")
    private String secret;

    @Value("${bethunger.jwt.issuer}")
    private String issuer;

    @Value("${bethunger.jwt.lifetime}")
    private Duration lifetime;

    private final String subject = "auth";

    public String generateToken(String username) {
        Instant now = Instant.now();
        return JWT.create()
            .withSubject(subject)
            .withClaim("username", username)
            .withIssuedAt(now)
            .withIssuer(issuer)
            .withExpiresAt(now.plusSeconds(lifetime.getSeconds()))
            .sign(Algorithm.HMAC256(secret));
    }

    public void validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
            .withSubject(subject)
            .withIssuer(issuer)
            .build();

        verifier.verify(token);
    }

    public String extractUsername(String token) {
        return JWT.decode(token).getClaim("username").asString();
    }

    public Map<String, Claim> extractClaims(String token) {
        return JWT.decode(token).getClaims();
    }
}
