package com.example.englishstudying.game;

import com.example.englishstudying.game.answer.AnswerVerifier;
import com.example.englishstudying.game.answer.CheckAnswerRequest;
import com.example.englishstudying.game.answer.CheckAnswerResponse;
import com.example.englishstudying.game.answer.LevelModifier;
import com.example.englishstudying.game.settings.GameSettingsRequest;
import com.example.englishstudying.game.settings.GameSettingsResolver;
import com.example.englishstudying.word.WordHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.logging.Level;

@Component
@RequiredArgsConstructor
public class GameHandler {
    private final ReactiveMongoTemplate mongoTemplate;
    private final GameSettingsResolver settingsResolver;
    private final AnswerVerifier answerVerifier;
    private final LevelModifier levelModifier;
    public Mono<ServerResponse> startGame(ServerRequest request) {
        return request.bodyToMono(GameSettingsRequest.class)
                .flatMap(settingRequest -> Mono.just(Game.from(settingsResolver, settingRequest)))
                .flatMap(mongoTemplate::insert)
                .flatMap(createdGame -> ServerResponse
                        .created(URI.create("/api/v1/game/" + createdGame.getId()))
                        .build()
                );
    }

    public Mono<ServerResponse> requestWord(ServerRequest request) {
        Mono<GameResponse> gameResponse = mongoTemplate.findById(request.pathVariable("id"), Game.class)
                .flatMap(game -> Mono.just(new GameResponse(
                        game.getRandomWord().getRussianMeaning(),
                        game.getTotalAnswersQuantity(),
                        game.getRightAnswersQuantity()))
                );
        return ServerResponse.ok().body(gameResponse, GameResponse.class);
    }

    public Mono<ServerResponse> checkAnswer(ServerRequest request) {
        return request.bodyToMono(CheckAnswerRequest.class)
                .flatMap(answerVerifier::verify)
                .flatMap(levelModifier::modifyDifficulty)
                .flatMap(response -> ServerResponse.ok().body(response, CheckAnswerResponse.class));
    }
}
