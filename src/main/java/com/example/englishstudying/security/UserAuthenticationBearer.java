package com.example.englishstudying.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

public class UserAuthenticationBearer {
    public static Mono<Authentication> create(TokenHandler.VerificationResult verificationResult) {
        Claims claims = verificationResult.getClaims();
        String subject = claims.getSubject();
        String username = claims.get("username", String.class);
        CustomPrincipal principal = new CustomPrincipal(subject, username);
        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null));
    }
}
