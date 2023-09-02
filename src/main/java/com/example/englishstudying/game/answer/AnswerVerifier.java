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
        return mongoTemplate
                .findOne(Query.query(Criteria.where("russianMeaning").is(request.wordToGuess())), Word.class)
                .flatMapIterable(Word::getEnglishMeanings)
                .map(Pair::getMeaning)
                .collectList()
                .flatMap(meanings -> createResponseBuilder(request, meanings))
                .map(AnswerResponseBuilder::build);
    }

    private Mono<AnswerResponseBuilder> createResponseBuilder(CheckAnswerRequest request, List<String> possibleAnswers) {
        boolean isAnswerCorrect = possibleAnswers.contains(request.answer());
        return Mono.just(AnswerResponseBuilder.builder(request, isAnswerCorrect, possibleAnswers));
    }
}
