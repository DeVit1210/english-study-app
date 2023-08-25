package com.example.englishstudying.game.answer.builder;

import com.example.englishstudying.game.answer.CheckAnswerResponse;

import java.util.List;

public class WrongAnswerResponseBuilder extends AnswerResponseBuilder {
    public WrongAnswerResponseBuilder(List<String> possibleAnswers, String currentAnswer) {
        super(possibleAnswers, currentAnswer);
    }

    @Override
    public CheckAnswerResponse build() {
        return new CheckAnswerResponse(false, possibleAnswers);
    }
}
