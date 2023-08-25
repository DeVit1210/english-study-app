package com.example.englishstudying.game.answer.builder;

import com.example.englishstudying.game.answer.CheckAnswerResponse;

import java.util.List;

public class CorrectAnswerResponseBuilder extends AnswerResponseBuilder {
    public CorrectAnswerResponseBuilder(List<String> possibleAnswers, String currentAnswer) {
        super(possibleAnswers, currentAnswer);
    }

    @Override
    public CheckAnswerResponse build() {
        List<String> filtered = possibleAnswers.stream()
                .filter(possibleAnswer -> !possibleAnswer.equals(currentAnswer))
                .toList();
        return new CheckAnswerResponse(true, filtered);
    }
}
