package uk.ac.shef.oak.storeimage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Image.class}, version = 1, exportSchema = false)
public abstract class ImageDatabase extends RoomDatabase {
    private static ImageDatabase INSTANCE;
    public static synchronized ImageDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ImageDatabase.class, "image")
                    .build();
        }
        return INSTANCE;
    }
    public abstract ImageDao getImageDao();
}
