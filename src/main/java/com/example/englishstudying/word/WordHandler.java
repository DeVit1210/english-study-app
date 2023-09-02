package com.example.englishstudying.word;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Slf4j
@RequiredArgsConstructor
public class WordHandler {
    private final ReactiveMongoTemplate mongoTemplate;
    public Mono<ServerResponse> addWord(ServerRequest request) {
        log.info("addWord()");
        Mono<WordAddingRequest> body = request.bodyToMono(WordAddingRequest.class);
        log.info(body.toString());
        return body
                .flatMap(bodyMono -> Mono.just(new Word(bodyMono.russianMeaning(), bodyMono.englishMeanings())))
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

    public Mono<ServerResponse> findWordById(ServerRequest request) {
        Mono<Word> word = mongoTemplate.findById(request.pathVariable("id"), Word.class);
        return ServerResponse.ok().body(word, Word.class);
    }

    public Mono<ServerResponse> updateWord(ServerRequest request) {
        Query query = Query.query(Criteria.where("id").is(request.pathVariable("id")));
        return request.bodyToMono(WordAddingRequest.class)
                .flatMap(bodyMono -> Mono.just(new Word(bodyMono.russianMeaning(), bodyMono.englishMeanings())))
                .flatMap(word -> {
                    Update updateDefinition = new Update()
                            .set("russianMeaning", word.getRussianMeaning())
                            .set("englishMeanings", word.getEnglishMeanings());
                    FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
                    return mongoTemplate.findAndModify(query, updateDefinition, options, Word.class);
                })
                .flatMap(updatedWord -> ServerResponse.ok().body(Mono.just(updatedWord), Word.class));
    }

    public Mono<Word> changeDifficulty(String russianMeaning, String englishMeaning, boolean increase) {
        Query query = Query.query(Criteria.where("russianMeaning").is(russianMeaning));
        return mongoTemplate.findOne(query, Word.class)
                .flatMap(word -> {
                    Pair pair = Pair.find(word, p -> englishMeaning.equals(p.getMeaning()));
                    if (increase) {
                        pair.increaseDifficulty();
                    } else pair.decreaseDifficulty();
                    return Mono.just(word);
                });
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
