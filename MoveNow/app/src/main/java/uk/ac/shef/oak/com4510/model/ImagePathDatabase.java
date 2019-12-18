package uk.ac.shef.oak.com4510.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * @description The ImagePath database.
 * @author Zhicheng Zhou
 */

@Database(entities = {Image.class, Path.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class ImagePathDatabase extends RoomDatabase {
    // marking the instance as volatile to ensure atomic access to the variable
    private static ImagePathDatabase INSTANCE;

    public static synchronized ImagePathDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ImagePathDatabase.class, "ImagePath.db")
                    .build();
        }
        return INSTANCE;
    }

    /**
     * Get the ImageDao.
     * @return The ImageDao.
     */
    public abstract ImageDao getImageDao();

    /**
     * Get the PathDao.
     * @return The PathDao.
     */
    public abstract PathDao getPathDao();
}
