package com.liggger.com4510.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Image {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id")
    private int image_id;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] picture;

    public Image(byte[] picture) {
        this.picture = picture;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
