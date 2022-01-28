package com.hcmus.csc13009.smartenglish.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "word_table")
public class Word implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "word")
    private String word;
    @ColumnInfo(name = "mean")
    private String mean;
    @ColumnInfo(name = "correct")
    private int correct;
    @ColumnInfo(name = "total")
    private int total;

    public Word(String word, String mean, int correct, int total) {
        this.word = word;
        this.mean = mean;
        this.correct = correct;
        this.total = total;
    }

    public String getWord() {
        return word;
    }

    public String getMean() {
        return mean;
    }

    public int getCorrect() {
        return correct;
    }

    public int getTotal() {
        return total;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
