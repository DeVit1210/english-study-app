package com.example.englishstudying.game.answer;

import java.util.List;

public record CheckAnswerResponse(boolean isAnswerRight, List<String> possibleAnswers) {
}
