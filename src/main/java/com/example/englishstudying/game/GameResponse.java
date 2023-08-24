package com.example.englishstudying.game;

public record GameResponse(String wordToGuess, int totalWordsQuantity, int wrongGuessedQuantity) {
}
