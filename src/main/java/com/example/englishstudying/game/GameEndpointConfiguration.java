package com.example.englishstudying.game;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
@Configuration
public class GameEndpointConfiguration {
    @Bean
    RouterFunction<ServerResponse> gameEndpoint(GameHandler gameHandler) {
        return RouterFunctions.route()
                .nest(RequestPredicates.path("/api/v1/"), builder -> builder
                        .POST("/start", gameHandler::startGame)
                        .GET("/getWord/{id}", gameHandler::requestWord)
                        .POST("/checkAnswer/{id}", gameHandler::checkAnswer)
                        .POST("/finish/{id}", gameHandler::finishGame)
                ).build();
    }
}
