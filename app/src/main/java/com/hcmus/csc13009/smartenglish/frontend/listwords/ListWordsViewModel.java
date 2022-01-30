package com.hcmus.csc13009.smartenglish.frontend.listwords;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hcmus.csc13009.smartenglish.data.local.AppRepository;
import com.hcmus.csc13009.smartenglish.data.model.Word;

import java.util.List;

public class ListWordsViewModel  extends AndroidViewModel {

    final private AppRepository repository;
    private LiveData<List<Word>> allWords;
    public ListWordsViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allWords = repository.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return this.allWords;
    }

    public LiveData<List<Word>> getAllCorrectWords() {
        return repository.getAllCorrectWords();
    }

    public LiveData<List<Word>> getAllInCorrectWords() {
        return repository.getAllInCorrectWords();
    }

    public void update(Word word) {
        repository.updateWord(word);
    }
}
