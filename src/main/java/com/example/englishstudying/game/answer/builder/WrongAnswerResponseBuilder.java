package com.example.englishstudying.game.answer.builder;

import com.example.englishstudying.game.answer.CheckAnswerRequest;
import com.example.englishstudying.game.answer.CheckAnswerResponse;

import java.util.List;

public class WrongAnswerResponseBuilder extends AnswerResponseBuilder {

    public WrongAnswerResponseBuilder(CheckAnswerRequest request, List<String> possibleAnswers) {
        super(request.gameId(), request.wordToGuess(), possibleAnswers, request.answer());
    }

    @Override
    public CheckAnswerResponse build() {
        return new CheckAnswerResponse(gameId, wordToGuess, currentAnswer, false, possibleAnswers);
    }
}
