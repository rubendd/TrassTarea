package com.rdd.trasstarea.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.rdd.trasstarea.model.Task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

// la solucion para no implentar los hilos manualmente:
// https://developer.android.com/training/data-storage/room/async-queries?hl=es-419#guava-livedata
@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTask(Task task);


    @Delete
    Completable delete(Task task);

    @Query("SELECT * FROM task")
    Single<List<Task>> getAll();

    @Update
    Completable updateTask(Task... task);

    @Query("SELECT * FROM task where prioritaria = 1")
    Single<List<Task>> getPriotiraias();

}
