package com.example.englishstudying.game.answer;

import com.example.englishstudying.word.WordHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LevelModifier {
    private final WordHandler wordHandler;
}
