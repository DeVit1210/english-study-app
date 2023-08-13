package com.example.englishstudying.word;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word {
    @Id
    private String id;
    private String russianMeaning;
    private List<Pair> englishMeanings;

    public Word(String russianMeaning, List<String> englishMeanings) {
        this.id = UUID.randomUUID().toString();
        this.russianMeaning = russianMeaning;
        this.englishMeanings = englishMeanings.stream().map(Pair::from).toList();
    }
}
