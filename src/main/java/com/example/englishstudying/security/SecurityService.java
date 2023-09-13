package com.example.englishstudying.security;

import com.example.englishstudying.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final PasswordEncoder passwordEncoder;
    private final ReactiveMongoTemplate mongoTemplate;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private int expirationInSeconds;
    private TokenDetails generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, user.getId());
    }
    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);
        return generateToken(expirationDate, claims, subject);
    }
    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        String token = Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();

    }
    private Mono<TokenDetails> authenticate(String username, String password) {
        return mongoTemplate
                .findOne(Query.query(Criteria.where("username").is(username)), User.class)
                .flatMap(user -> this.mapFoundUser(user, password))
                .switchIfEmpty(Mono.error(new AuthException("Invalid username", "INVALID_USERNAME")));

    }
    private Mono<TokenDetails> mapFoundUser(User user, String password) {
        if(!user.isEnabled()) {
            return Mono.error(new AuthException("Account disabled", "USER_ACCOUNT_DISABLED"));
        }
        if(!passwordEncoder.matches(password, user.getPassword())) {
            return Mono.error(new AuthException("Invalid password", "INVALID_PASSWORD"));
        }
        return Mono.just(generateToken(user).toBuilder()
                .userId(user.getId())
                .build()
        );
    }
}
