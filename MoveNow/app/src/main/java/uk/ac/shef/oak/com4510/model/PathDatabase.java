package uk.ac.shef.oak.com4510.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Path.class}, version = 1, exportSchema = false)
public abstract class PathDatabase extends RoomDatabase {
    private static PathDatabase INSTANCE;
    public static synchronized PathDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PathDatabase.class, "path")
                    .build();
        }
        return INSTANCE;
    }
    public abstract PathDao getPathDao();
}
