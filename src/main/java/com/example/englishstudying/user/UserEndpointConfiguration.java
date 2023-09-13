package com.example.englishstudying.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserEndpointConfiguration {
    @Bean
    RouterFunction<ServerResponse> userEndpoint(UserHandler userHandler) {
        return RouterFunctions.route()
                .nest(RequestPredicates.path("/api/v1/user"), builder -> builder
                        .POST("/register", userHandler::register)
                        .POST("/login", userHandler::login)
                ).build();
    }

}
