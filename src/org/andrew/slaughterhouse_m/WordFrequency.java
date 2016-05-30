package org.andrew.slaughterhouse_m;

import java.io.Serializable;

public class WordFrequency implements Serializable {
    private String word;
    private int frequency;

    public WordFrequency(String word) {
        this.word = word;
        this.frequency = 1;
    }

    public WordFrequency(String word, int frequency) {

        this.word = word;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "WordFrequency{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                '}';
    }

    public int getFrequency() {

        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getWord() {

        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void increment() {
        this.frequency++;
    }
}
