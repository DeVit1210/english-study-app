package com.example.englishstudying.word;

import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.function.Predicate;

@AllArgsConstructor
public class Pair implements Comparable<Pair> {
    private static final int HARD_INITIAL_DIFFICULTY_VALUE = 6;
    private static final int MEDIUM_INITIAL_DIFFICULTY_VALUE = 3;
    private static final int EASY_INITIAL_DIFFICULTY_VALUE = 0;

    private String meaning;
    private int difficultyValue;
    public static Pair from(String meaning) {
        return new Pair(meaning, HARD_INITIAL_DIFFICULTY_VALUE);
    }
    public Difficulty getDifficulty() {
        return this.difficultyValue > MEDIUM_INITIAL_DIFFICULTY_VALUE ? Difficulty.HARD
                : this.difficultyValue > EASY_INITIAL_DIFFICULTY_VALUE ? Difficulty.MEDIUM
                : Difficulty.EASY;
    }
    public String getMeaning() {
        return meaning;
    }
    public void setDifficulty(Difficulty difficulty) {
        switch (difficulty) {
            case EASY -> this.difficultyValue = EASY_INITIAL_DIFFICULTY_VALUE;
            case MEDIUM -> this.difficultyValue = MEDIUM_INITIAL_DIFFICULTY_VALUE;
            case HARD -> this.difficultyValue = HARD_INITIAL_DIFFICULTY_VALUE;
        }
    }
    public void increaseDifficulty() { this.difficultyValue++; }
    public void decreaseDifficulty() { this.difficultyValue--; }

    public static Pair find(Word word, Predicate<Pair> filteringRule) {
        return word.getEnglishMeanings().stream()
                .filter(filteringRule)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
    @Override
    public int compareTo(Pair other) {
        return Integer.compare(other.difficultyValue, this.difficultyValue);
    }
}
