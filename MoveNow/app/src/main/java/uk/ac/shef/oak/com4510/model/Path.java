package uk.ac.shef.oak.com4510.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Path {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "path_id")
    private int path_id;

    @ColumnInfo(name = "title")
    private String title;

    public Path(String title) {
        this.title = title;
    }

    public int getPath_id() {
        return path_id;
    }

    public void setPath_id(int path_id) {
        this.path_id = path_id;
    }

    public String getTitle() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }
}
