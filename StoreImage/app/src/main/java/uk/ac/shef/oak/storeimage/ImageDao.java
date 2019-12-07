package uk.ac.shef.oak.storeimage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ImageDao {
    @Insert
    void insertImage(Image image);

    @Query("SELECT * FROM Image LIMIT 1")
    LiveData<Image> getOneImage();

    @Query("SELECT * FROM IMAGE")
    LiveData<Image> getAllImages();
}
