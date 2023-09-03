package com.example.englishstudying.game.finish;

import java.util.List;
import java.util.Map;

public record FinishGameResponse(Map<String, List<String>> wrongAnsweredWords, double resultInPercents) {
}
