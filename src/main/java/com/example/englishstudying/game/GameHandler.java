package com.example.englishstudying.game;

import com.example.englishstudying.game.settings.GameSettingsRequest;
import com.example.englishstudying.game.settings.GameSettingsResolver;
import com.example.englishstudying.word.Word;
import com.example.englishstudying.word.WordHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class GameHandler {
    private final ReactiveMongoTemplate mongoTemplate;
    private final WordHandler wordHandler;
    private final GameSettingsResolver settingsResolver;
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
        Mono<Boolean> booleanMono = request.bodyToMono(CheckAnswerRequest.class)
                .flatMap(answerRequest -> mongoTemplate
                        .findOne(Query.query(Criteria.where(answerRequest.wordToGuess())), Word.class)
                        .map(w -> w.getEnglishMeanings()
                                .stream()
                                .anyMatch(pair -> pair.getMeaning().equals(answerRequest.answer()))
                        )
                );

        // TODO: temporal response, create CheckAnswerResponse record,
        //  which will notify the user whether the answer right or not
        //  and show the list of all different possible answers

        return ServerResponse.ok().body("Answer is right: " + booleanMono, String.class);
    }


//    private void changeDifficulty(boolean increased) {
//        String id = "any";
//        String englishMeaning = "any";
//        wordHandler.changeDifficulty(id, englishMeaning, increased)
//                .flatMap(mongoTemplate::save);
//    }
}
