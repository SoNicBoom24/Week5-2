package com.example.week52;

import java.io.Serializable;
import java.util.ArrayList;

public class Sentence implements Serializable {
    public ArrayList<String> badSentences, goodSentences;

    public Sentence() {
        this.badSentences = new ArrayList<>();
        this.goodSentences = new ArrayList<>();
    }
}
