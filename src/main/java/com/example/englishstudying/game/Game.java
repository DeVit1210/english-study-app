package com.example.englishstudying.game;

import com.example.englishstudying.game.settings.GameSettingsRequest;
import com.example.englishstudying.game.settings.GameSettingsResolver;
import com.example.englishstudying.word.Word;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Document
@Data
public class Game {
    private String id;
    private LocalDateTime time;
    private int totalAnswersQuantity;
    private int rightAnswersQuantity;
    @Transient
    private List<Word> wrongAnsweredWords;
    @Transient
    private WordListHolder wordListHolder;

    private Game(List<Word> words) {
        this.id = UUID.randomUUID().toString();
        this.totalAnswersQuantity = 0;
        this.rightAnswersQuantity = 0;
        this.time = LocalDateTime.now();
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
}
