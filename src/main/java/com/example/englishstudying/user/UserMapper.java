package com.example.englishstudying.user;

import com.example.englishstudying.security.TokenDetails;
import com.example.englishstudying.user.dto.AuthenticationRequest;
import com.example.englishstudying.user.dto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.expiration}")
    private int expirationInSeconds;
    public Mono<User> mapRequest(AuthenticationRequest request) {
        return Mono.just(User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .gamesHistory(new ArrayList<>())
                .build()
        );
    }

    public Mono<AuthenticationResponse> mapResponse(TokenDetails tokenDetails) {
        return Mono.just(AuthenticationResponse.builder()
                .userId(tokenDetails.getUserId())
                .expiresAt(tokenDetails.getExpiresAt())
                .issuedAt(tokenDetails.getIssuedAt())
                .token(tokenDetails.getToken())
                .build()
        );
    }
}
