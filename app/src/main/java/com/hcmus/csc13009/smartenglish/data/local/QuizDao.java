package com.hcmus.csc13009.smartenglish.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hcmus.csc13009.smartenglish.data.model.Quiz;

import java.util.List;

@Dao
public interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertQuiz(Quiz quiz);
    @Delete
    void deleteQuiz(Quiz quiz);
    @Update
    void updateQuiz(Quiz quiz);
    @Query("SELECT * FROM quiz_table")
    LiveData<List<Quiz>> getAllQuiz();
    @Query("SELECT * FROM quiz_table LIMIT 1")
    Quiz[] getLastQuiz();
}
