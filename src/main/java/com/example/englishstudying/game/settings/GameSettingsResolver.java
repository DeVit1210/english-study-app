package com.example.englishstudying.game.settings;

import com.example.englishstudying.game.settings.GameSettingsRequest;
import com.example.englishstudying.word.Difficulty;
import com.example.englishstudying.word.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameSettingsResolver {
    private final ReactiveMongoTemplate mongoTemplate;
    public List<Word> resolveSettings(GameSettingsRequest request) {
        Difficulty requestedDifficulty = Difficulty.valueOf(request.difficulty().toUpperCase());
        int requestedQuantity = request.wordsQuantity();
        return mongoTemplate.findAll(Word.class)
                .filter(word -> word.getDifficulty().equals(requestedDifficulty))
                .take(requestedQuantity)
                .collectList()
                .block();
    }
}
