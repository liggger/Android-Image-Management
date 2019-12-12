package uk.ac.shef.oak.com4510.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PathDao {
    @Insert
    void insertPath(Path path);

    @Query("SELECT * FROM path")
    LiveData<List<Path>> getPaths();

    @Query("SELECT * FROM path ORDER BY path_id DESC LIMIT 1")
    LiveData<Path> getOnePath();

    @Update
    void updatePath(Path path);
}
