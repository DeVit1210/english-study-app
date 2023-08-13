package com.example.englishstudying.word;

import java.util.List;

public record WordAddingRequest(String russianMeaning, List<String> englishMeanings) {

}
