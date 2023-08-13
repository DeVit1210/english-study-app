package com.example.englishstudying.word;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class WordServiceTest {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private WordService wordService;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @BeforeEach
    public void configureCollectionsBeforeTests() {
        Mono<Boolean> createIfMissing = reactiveMongoTemplate.collectionExists(Word.class)
                .filter(x -> !x)
                .flatMap(exists -> reactiveMongoTemplate.createCollection(Word.class))
                .thenReturn(true);
        StepVerifier.create(createIfMissing)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testAddWord() {
        WordAddingRequest request = new WordAddingRequest(
                "hello", new ArrayList<>(List.of("Привет", "Здарова"))
        );
        Publisher<Word> word = this.wordRepository
                .deleteAll()
                .then(this.wordService.addWord(request));

        StepVerifier.create(word)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testFindAllWords() {
        WordAddingRequest request = new WordAddingRequest(
                "hello", new ArrayList<>(List.of("Привет", "Здарова"))
        );

        Flux<Word> wordFlux = this.wordRepository.deleteAll()
                .then(this.wordService.addWord(request))
                .then(this.wordService.addWord(request))
                .then(this.wordService.addWord(request))
                .thenMany(this.wordService.getAllWords());

        StepVerifier.create(wordFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void testDeleteWord() {
        WordAddingRequest request = new WordAddingRequest(
                "hello", new ArrayList<>(List.of("Привет", "Здарова"))
        );
        String id = this.wordRepository.deleteAll()
                .then(this.wordService.addWord(request))
                .map(Word::getId)
                .block();

        Flux<Word> beforeDeleting = this.wordService.getAllWords();
        StepVerifier.create(beforeDeleting).expectNextCount(1).verifyComplete();

        Flux<Word> afterDeletingWithWrongId = this.wordService.deleteWord(UUID.randomUUID().toString())
                .thenMany(this.wordService.getAllWords());
        StepVerifier.create(afterDeletingWithWrongId).expectNextCount(1).verifyComplete();

        Flux<Word> afterDeleting = this.wordService.deleteWord(id)
                .thenMany(this.wordService.getAllWords());
        StepVerifier.create(afterDeleting).expectNextCount(0).verifyComplete();

    }
}