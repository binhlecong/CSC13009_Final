package com.hcmus.csc13009.smartenglish.data.local;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hcmus.csc13009.smartenglish.data.model.Word;

import java.util.List;

@Dao
public interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWord(Word word);

    @Delete
    void deleteWord(Word word);
    
    @Update
    void updateWord(Word word);

    @Query("SELECT * FROM word_table")
    LiveData<List<Word>> getAllWords();

    @Query( "SELECT * FROM word_table " +
            "WHERE CAST(correct as REAL)/total >= 0.6 " +
            "ORDER BY CAST(correct as REAL)/total DESC, total DESC")
    LiveData<List<Word>> getAllCorrectWords();

    @Query( "SELECT * FROM word_table " +
            "WHERE CAST(correct as REAL)/total < 0.5 " +
            "ORDER BY CAST(correct as REAL)/total ASC, total DESC")
    LiveData<List<Word>> getAllInCorrectWords();

    @Query("SELECT * FROM word_table WHERE word LIKE :label")
    List<Word> getWord(@NonNull String label);


}
