package com.hcmus.csc13009.smartenglish.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hcmus.csc13009.smartenglish.data.model.Word;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {
    static public AppRoomDatabase INSTANCE = null;
    static final public ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(2);

    public abstract WordDao wordDao();

    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppRoomDatabase.class,
                            "smartenglish_databasse")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
