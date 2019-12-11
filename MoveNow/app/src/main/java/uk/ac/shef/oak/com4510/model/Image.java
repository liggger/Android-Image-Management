package uk.ac.shef.oak.com4510.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

@Entity
public class Image {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id")
    private int image_id;

//    @ColumnInfo(name = "path_id")
//    private int path_id;
//
//    @ColumnInfo(name = "latitude")
//    private double latitude;
//
//    @ColumnInfo(name = "longitude")
//    private double longitude;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] picture;

//    public Image(int path_id, double latitude, double longitude, byte[] picture) {
//        this.path_id = path_id;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.picture = picture;
//    }

    public Image(byte[] picture) {
        this.picture = picture;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

//    public int getPath_id() {
//        return path_id;
//    }
//
//    public void setPath_id(int path_id) {
//        this.path_id = path_id;
//    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

//    public double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
//    public double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }
}
