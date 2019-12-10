package uk.ac.shef.oak.com4510.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PathDao {
    @Insert
    void insertPath(Path path);

    @Query("SELECT * FROM Path")
    LiveData<List<Path>> getPaths();
}
