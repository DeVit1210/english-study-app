package com.example.englishstudying.game.settings;

import com.example.englishstudying.word.Word;

@FunctionalInterface
public interface GameSettings {
    boolean applySettings(Word word);
}
