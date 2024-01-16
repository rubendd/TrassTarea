package com.rdd.trasstarea.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rdd.trasstarea.database.dao.TaskDao;
import com.rdd.trasstarea.model.Task;

@Database(
        entities = {Task.class},
        version = 2
)
public abstract class AppDataBase extends RoomDatabase {

    private static volatile AppDataBase INSTANCE;

    public abstract TaskDao daoTask();

    public static AppDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "Trasstarea.db")
                            .fallbackToDestructiveMigration()  // Esto borra y recrea la base de datos si es necesario
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
