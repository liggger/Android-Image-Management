package uk.ac.shef.oak.com4510.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

// singleton
@Database(entities = {Image.class, Path.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class ImageDatabase extends RoomDatabase {
    private static ImageDatabase INSTANCE;
    public static synchronized ImageDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ImageDatabase.class, "ImagePath.db")
                    .build();
        }
        return INSTANCE;
    }

    public abstract ImageDao getImageDao();

    public abstract PathDao getPathDao();

}
