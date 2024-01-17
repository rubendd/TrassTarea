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


@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Update
    void updateTask(Task... task);

    @Query("SELECT * FROM task where prioritaria = 1")
    List<Task> getPriotiraias();

}
