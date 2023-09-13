package com.example.englishstudying.security;

import com.example.englishstudying.security.exception.AuthException;
import com.example.englishstudying.security.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
public class TokenHandler {
    private final String secret;
    public Mono<VerificationResult> check(String accessToken) {
        return Mono.just(verify(accessToken))
                .onErrorResume(throwable -> Mono.error(new UnauthorizedException(throwable.getMessage())));
    }

    private VerificationResult verify(String token) {
        Claims claims = getClaimsFromToken(token);
        final Date expirationDate = claims.getExpiration();
        if(expirationDate.before(new Date())) {
            throw new AuthException("Token expired", "TOKEN_EXPIRED");
        }
        return new VerificationResult(claims, token);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class VerificationResult {
        private Claims claims;
        private String token;
    }
}
