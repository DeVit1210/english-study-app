package com.example.englishstudying.game;

import com.example.englishstudying.game.settings.GameSettingsRequest;
import com.example.englishstudying.game.settings.GameSettingsResolver;
import com.example.englishstudying.word.Pair;
import com.example.englishstudying.word.Word;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;

@Document
@Data
public class Game {
    private String id;
    private LocalDateTime time;
    private int totalAnswersQuantity;
    private int rightAnswersQuantity;
    private Map<String, List<String>> wrongAnsweredWords;
    private WordListHolder wordListHolder;

    private Game(List<Word> words) {
        this.id = UUID.randomUUID().toString();
        this.totalAnswersQuantity = 0;
        this.rightAnswersQuantity = 0;
        this.time = LocalDateTime.now();
        this.wrongAnsweredWords = new HashMap<>();
        this.wordListHolder = new WordListHolder(words);
    }
    public double getResultInPercents() {
        return ((double) rightAnswersQuantity) / totalAnswersQuantity * 100;
    }

    public static Game from(GameSettingsResolver resolver, GameSettingsRequest request) {
        return new Game(resolver.resolveSettings(request));
    }
    public Word getRandomWord() {
        return this.wordListHolder.getWord();
    }

    static class WordListHolder {
        private final Iterator<Word> iterator;
        WordListHolder(List<Word> wordsToGuess) {
            Collections.shuffle(wordsToGuess);
            this.iterator = wordsToGuess.iterator();
        }
        Word getWord() {
            if(iterator.hasNext()) {
                return iterator.next();
            } else throw new IllegalStateException("no words left!");
        }
    }

    public Mono<Game> addWrongAnsweredWord(Word word) {
        this.wrongAnsweredWords.put(
                word.getRussianMeaning(),
                word.getEnglishMeanings().stream().map(Pair::getMeaning).toList()
        );
        return Mono.just(this);
    }
}
