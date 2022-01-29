package com.hcmus.csc13009.smartenglish.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "word_table")
public class Word implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String word;
    @ColumnInfo(name = "correct")
    private int correct;
    @ColumnInfo(name = "total")
    private int total;
    @ColumnInfo(name = "last_date")
    private long lastDate;

    public Word(@NonNull String word, int correct, int total, long lastDate) {
        this.word = word;
        this.correct = correct;
        this.total = total;
        this.lastDate = lastDate;
    }

    @NonNull
    public String getWord() {
        return word;
    }

    public int getCorrect() {
        return correct;
    }

    public int getTotal() {
        return total;
    }

    public long getLastDate() {
        return lastDate;
    }

    public void setWord(@NonNull String word) {
        this.word = word;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setLastDate(long lastDate) {
        if (getLastDate() < lastDate)
            this.lastDate = lastDate;
    }
}
