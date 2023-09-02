package com.example.englishstudying.game.answer;

import com.example.englishstudying.game.Game;
import com.example.englishstudying.word.Word;
import com.example.englishstudying.word.WordHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LevelModifier {
    private final ReactiveMongoTemplate mongoTemplate;
    private final WordHandler wordHandler;
    public Mono<CheckAnswerResponse> modifyDifficulty(CheckAnswerResponse response) {
        Mono<Game> currentGame = mongoTemplate.findById(response.gameId(), Game.class);
        return wordHandler.changeDifficulty(response.wordToGuess(), response.currentAnswer(), response.isAnswerRight())
                .flatMap(mongoTemplate::save)
                .flatMap(savedWord -> currentGame.flatMap(game -> game.addWrongAnsweredWord(savedWord)))
                .map(game -> response);
    }
}
