package com.example.englishstudying.user;

import com.example.englishstudying.security.SecurityService;
import com.example.englishstudying.user.dto.AuthenticationRequest;
import com.example.englishstudying.user.dto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final SecurityService securityService;
    private final ReactiveMongoTemplate mongoTemplate;
    private final UserMapper userMapper;
    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(AuthenticationRequest.class)
                .flatMap(securityService::authenticate)
                .flatMap(userMapper::mapResponse)
                .flatMap(authenticationResponse -> ServerResponse
                        .accepted()
                        .body(authenticationResponse, AuthenticationResponse.class)
                );
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(AuthenticationRequest.class)
                .flatMap(userMapper::mapRequest)
                .flatMap(mongoTemplate::insert)
                .flatMap(savedUser -> ServerResponse
                        .created(URI.create("/api/v1/user/" + savedUser.getId()))
                        .build()
                );
    }
}
