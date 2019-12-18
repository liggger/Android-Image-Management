package uk.ac.shef.oak.com4510.model;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * @description The ImageDao
 * @author Zhicheng Zhou
 */

@Dao
public interface ImageDao {
    /**
     * Insert one image.
     * @param image The image to be inserted.
     */
    @Insert
    void insertImage(Image image);

    /**
     * Get the whole images by date in ascending order to use the paging method.
     * @return The images sorted by time.
     */
    @Query("SELECT * FROM Image ORDER BY time ASC")
    DataSource.Factory<Integer, Image> getImagesByDate();

    /**
     * Find the target images according to the path id.
     * @param path_id The path id.
     * @return The images according to the path id.
     */
    @Query("SELECT * FROM Image WHERE path_id = :path_id")
    LiveData<List<Image>> findImagesByPathId(int path_id);

    /**
     * Find the target image according to the image id.
     * @param image_id The image id.
     * @return The image according to the image.
     */
    @Query("SELECT * FROM Image WHERE image_id = :image_id")
    LiveData<Image> findImageByImageId(int image_id);

    @Query("SELECT COUNT(image_id) from image")
    LiveData<Integer> countImageNumber();
}
