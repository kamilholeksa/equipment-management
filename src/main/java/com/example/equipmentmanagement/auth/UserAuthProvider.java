package com.example.equipmentmanagement.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.equipmentmanagement.dto.UserAuthDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

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

    public String createRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000L * refreshTokenValidity);

        return JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);
    }

    public Authentication validateToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);

        UserAuthDto user = new UserAuthDto();
        user.setUsername(decoded.getSubject());
        user.setFirstName(decoded.getClaim("firstName").asString());
        user.setLastName(decoded.getClaim("lastName").asString());
        user.setEmail(decoded.getClaim("email").asString());
        user.setRoles(decoded.getClaim("roles").asList(String.class));

        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    public boolean isTokenValid(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decoded = JWT.require(algorithm).build().verify(token);
        return decoded.getSubject();
    }
}
