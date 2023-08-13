package com.example.englishstudying.word;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WordService {
    private final ReactiveMongoTemplate mongoTemplate;
    public Mono<Word> addWord(WordAddingRequest request) {
        return mongoTemplate.insert(new Word(request.russianMeaning(), request.englishMeanings()));
    }

    public Flux<Word> getAllWords() {
        return mongoTemplate.findAll(Word.class);
    }

    public Mono<Word> deleteWord(String id) {
        return mongoTemplate.findAndRemove(Query.query(Criteria.where("id").is(id)), Word.class);
    }
}
