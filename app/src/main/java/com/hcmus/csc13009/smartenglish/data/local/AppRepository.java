package com.hcmus.csc13009.smartenglish.data.local;

import android.app.Application;

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

    private AppRepository(Application app) {

        AppRoomDatabase db = AppRoomDatabase.getDatabase(app);
        // get all dao
        wordDao = db.wordDao();
        // get all data
        allWords = wordDao.getAllWords();
    }

    AppRepository getInstance(Application app) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(app);
        }
        return INSTANCE;
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
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

}
