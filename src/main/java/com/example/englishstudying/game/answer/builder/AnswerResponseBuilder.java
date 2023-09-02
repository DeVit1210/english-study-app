package com.example.englishstudying.game.answer.builder;

import com.example.englishstudying.game.answer.CheckAnswerRequest;
import com.example.englishstudying.game.answer.CheckAnswerResponse;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public abstract class AnswerResponseBuilder {
    protected final String gameId;
    protected final String wordToGuess;
    protected final List<String> possibleAnswers;
    protected final String currentAnswer;
    public abstract CheckAnswerResponse build();
    public static AnswerResponseBuilder builder(CheckAnswerRequest request, boolean isAnswerCorrect, List<String> possibleAnswers) {
        return isAnswerCorrect
                ? new CorrectAnswerResponseBuilder(request, possibleAnswers)
                : new WrongAnswerResponseBuilder(request, possibleAnswers);
    }
}
