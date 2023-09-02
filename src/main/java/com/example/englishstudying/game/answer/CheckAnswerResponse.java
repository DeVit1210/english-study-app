package com.example.englishstudying.game.answer;

import java.util.List;

public record CheckAnswerResponse(String gameId,
                                  String wordToGuess,
                                  String currentAnswer,
                                  boolean isAnswerRight,
                                  List<String> possibleAnswers) {
}
