package com.example.englishstudying.game.answer;

public record CheckAnswerRequest(String gameId, String wordToGuess, String answer) {
}
