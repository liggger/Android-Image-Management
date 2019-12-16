package uk.ac.shef.oak.com4510.model;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDao {
    @Insert
    void insertImage(Image image);

    @Query("SELECT * FROM Image LIMIT 1")
    LiveData<Image> getOneImage();

    @Query("SELECT * FROM Image ORDER BY time ASC")
    LiveData<List<Image>> getImages();

    @Query("SELECT * FROM Image ORDER BY time ASC")
    DataSource.Factory<Integer, Image> getImagesByDate();

    @Query("SELECT * FROM Image WHERE path_id = :path_id")
    LiveData<List<Image>> findImagesByPathId(int path_id);

    @Query("SELECT * FROM Image WHERE image_id = :image_id")
    LiveData<Image> findImageByImageId(int image_id);


}
