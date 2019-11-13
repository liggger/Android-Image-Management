package com.liggger.imagemanagement.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Image {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id")
    private int image_id;

    @ColumnInfo(name = "temperature")
    private int temperature;

    public Image(int temperature) {
        this.temperature = temperature;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
