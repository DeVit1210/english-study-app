package com.example.englishstudying.word;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class WordEndpointConfiguration {
    @Bean
    RouterFunction<ServerResponse> wordEndpoint(WordHandler wordHandler) {
        return RouterFunctions.route()
                .nest(RequestPredicates.path("/api/v1/word"), builder -> builder
                        .GET("", wordHandler::getAllWords)
                        .GET("/{id}", wordHandler::findWordById)
                        .POST("", wordHandler::addWord)
                        .DELETE("/{id}", wordHandler::deleteWord))
                .build();
    }
}
