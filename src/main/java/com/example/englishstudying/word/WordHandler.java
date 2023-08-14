package com.example.englishstudying.word;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class WordHandler {
    private final ReactiveMongoTemplate mongoTemplate;
    public Mono<ServerResponse> addWord(ServerRequest request) {
        return request.bodyToMono(Word.class)
                .flatMap(mongoTemplate::insert)
                .flatMap(saved -> ServerResponse.created(URI.create("/api/v1/word/" + saved.getId())).build());
    }

    public Mono<ServerResponse> getAllWords(ServerRequest request) {
        Flux<Word> wordFlux = mongoTemplate.findAll(Word.class);
        return ServerResponse.ok().body(wordFlux, Word.class);
    }

    public Mono<ServerResponse> deleteWord(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Word> word = mongoTemplate.findAndRemove(Query.query(Criteria.where("id").is(id)), Word.class);
        return ServerResponse.ok().body(word, Word.class);
    }
}


//@Service
//@RequiredArgsConstructor
//public class WordService {
//    private final ReactiveMongoTemplate mongoTemplate;
//    public Mono<Word> addWord(WordAddingRequest request) {
//        return mongoTemplate.insert(new Word(request.russianMeaning(), request.englishMeanings()));
//    }
//
//    public Flux<Word> getAllWords() {
//        return mongoTemplate.findAll(Word.class);
//    }
//
//    public Mono<Word> deleteWord(String id) {
//        return mongoTemplate.findAndRemove(Query.query(Criteria.where("id").is(id)), Word.class);
//    }
//}
