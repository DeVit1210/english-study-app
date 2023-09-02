package com.example.englishstudying.game.answer.builder;

import com.example.englishstudying.game.answer.CheckAnswerRequest;
import com.example.englishstudying.game.answer.CheckAnswerResponse;

import java.util.List;
public class CorrectAnswerResponseBuilder extends AnswerResponseBuilder {

    public CorrectAnswerResponseBuilder(CheckAnswerRequest request, List<String> possibleAnswers) {
        super(request.gameId(), request.wordToGuess(), possibleAnswers, request.answer());
    }

    @Override
    public CheckAnswerResponse build() {
        List<String> filtered = possibleAnswers.stream()
                .filter(possibleAnswer -> !possibleAnswer.equals(currentAnswer))
                .toList();
        return new CheckAnswerResponse(gameId, wordToGuess, currentAnswer, true, filtered);
    }
}
