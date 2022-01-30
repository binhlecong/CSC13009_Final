package com.hcmus.csc13009.smartenglish.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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
    @ColumnInfo(name = "word_type")
    private int wordType;

    public Word(@NonNull String word, int correct, int total, long lastDate, int wordType) {
        this.word = word;
        this.correct = correct;
        this.total = total;
        this.lastDate = lastDate;
        this.wordType = wordType;
    }
    @Ignore
    public Word(@NonNull String word, int correct, int total, long lastDate, boolean isTestMode) {
        this.word = word;
        this.correct = correct;
        this.total = total;
        this.lastDate = lastDate;
        this.wordType = isTestMode ? 1 : 2;
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

    public int getWordType() {
        return wordType;
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

    public void setWordType(int wordType) {
        this.wordType = wordType;
    }

    public void setWordType(boolean isTestMode) {
        if (isTestMode)
            this.wordType |= 1;
        else
            this.wordType |= 2;
    }
}
