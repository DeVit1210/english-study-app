package com.example.englishstudying.game.answer;

import com.example.englishstudying.game.answer.builder.AnswerResponseBuilder;
import com.example.englishstudying.word.Pair;
import com.example.englishstudying.word.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnswerVerifier {
    private final ReactiveMongoTemplate mongoTemplate;
    public Mono<CheckAnswerResponse> verify(CheckAnswerRequest request) {
        final String currentAnswer = request.answer();
        return mongoTemplate
                .findOne(Query.query(Criteria.where("russianMeaning").is(request.wordToGuess())), Word.class)
                .flatMapIterable(Word::getEnglishMeanings)
                .map(Pair::getMeaning)
                .collectList()
                .flatMap(meanings -> createResponseBuilder(meanings.contains(currentAnswer), meanings, currentAnswer))
                .map(AnswerResponseBuilder::build);
    }

    private Mono<AnswerResponseBuilder> createResponseBuilder(boolean isAnswerCorrect, List<String> possibleAnswers,
                                                              String currentAnswer) {
        return Mono.just(AnswerResponseBuilder.builder(isAnswerCorrect, possibleAnswers, currentAnswer));
    }
}
