package uk.ac.shef.oak.com4510.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * @description The PathDao.
 * @author Zhicheng Zhou
 */

@Dao
public interface PathDao {
    /**
     * Insert one path.
     * @param path The path to be inserted.
     */
    @Insert
    void insertPath(Path path);


    /**
     * Get all paths.
     * @return All the paths.
     */
    @Query("SELECT * FROM path")
    LiveData<List<Path>> getPaths();

    /**
     * Get the latest path.
     * @return The latest path.
     */
    @Query("SELECT * FROM path ORDER BY path_id DESC LIMIT 1")
    LiveData<Path> getOnePath();

    /**
     * Find the target path according to the path id.
     * @param path_id The path id.
     * @return The target path.
     */
    @Query("SELECT * FROM path WHERE path_id = :path_id")
    LiveData<Path> findPathById(int path_id);

    /**
     * Update the target path.
     * @param path The target path.
     */
    @Update
    void updatePath(Path path);
}
