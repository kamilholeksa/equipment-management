package com.example.equipmentmanagement.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.equipmentmanagement.exception.UnauthorizedException;
import com.example.equipmentmanagement.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserAuthProvider {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.token-validity}")
    private int tokenValidity;

    @Value("${application.security.jwt.refresh-token-validity}")
    private int refreshTokenValidity;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String createToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000L * tokenValidity);

        return JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("auth", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
    }

    public String createToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000L * tokenValidity);

        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("auth", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
    }

    public String createRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000L * refreshTokenValidity);

        return JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);
    }

    public boolean isTokenValid(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            throw new UnauthorizedException("Token is invalid");
        }
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decoded = JWT.require(algorithm).build().verify(token);
        return decoded.getSubject();
    }
}
