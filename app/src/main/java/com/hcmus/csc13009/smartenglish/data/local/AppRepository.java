package com.hcmus.csc13009.smartenglish.data.local;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.hcmus.csc13009.smartenglish.data.model.Word;

import java.util.List;

public class AppRepository {
    // singleton
    static private AppRepository INSTANCE = null;
    // all dao
    final private WordDao wordDao;
    // all data
    private final LiveData<List<Word>> allWords;

    public AppRepository(Application app) {

        AppRoomDatabase db = AppRoomDatabase.getDatabase(app);
        // get all dao
        wordDao = db.wordDao();
        // get all data
        allWords = wordDao.getAllWords();
    }

    public static AppRepository getInstance(Application app) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(app);
        }
        return INSTANCE;
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public LiveData<List<Word>> getAllCorrectWords() {
        return wordDao.getAllCorrectWords();
    }

    public LiveData<List<Word>> getAllInCorrectWords() {
        return wordDao.getAllInCorrectWords();
    }

    public void insertWord(Word word) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
           wordDao.insertWord(word);
        });
    }

    public void deleteWord(Word word) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            wordDao.deleteWord(word);
        });
    }

    public void updateWord(Word word) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            wordDao.updateWord(word);
        });
    }

    public void updateScore(@NonNull String label, int score) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Word> words = wordDao.getWord(label);
            Word word;
            int correct = 0;
            int wrong = -score;
            if (score > 0) {
                correct = score;
                wrong = 0;
            }
            if (words == null || words.isEmpty()) {
                word = new Word(label, correct, correct + wrong);
                wordDao.insertWord(word);
            } else {
                word = words.get(0);
                word.setCorrect(word.getCorrect() + correct);
                word.setTotal(word.getTotal() + correct + wrong);
                wordDao.updateWord(word);
            }
//            Log.i("@@@ word", word.getTotal() + " " + word.getCorrect());
        });
    }
}
