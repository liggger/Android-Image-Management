package uk.ac.shef.oak.com4510.model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

/**
 * @description The Image entity.
 * @author Zhicheng Zhou
 */

@Entity(foreignKeys = @ForeignKey(entity = Path.class, parentColumns = "path_id", childColumns = "path_id", onDelete = CASCADE),
        indices = @Index({"path_id"}))
@TypeConverters({Converters.class})
public class Image {
    // The image id.
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id")
    private int image_id;

    // The path id.
    @ColumnInfo(name = "path_id")
    private int path_id;

    // The latitude of the image.
    @ColumnInfo(name = "latitude")
    private double latitude;

    // The longitude of the image.
    @ColumnInfo(name = "longitude")
    private double longitude;

    // The time when the image is taken.
    @ColumnInfo(name = "time")
    private Date time;

    // The resource of the image.
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] picture;

    /**
     * The constructor of the image.
     * The path id generates automatically.
     *
     * @param path_id The path id.
     * @param latitude The latitude of the image.
     * @param longitude The longitude of the image.
     * @param time The time.
     */
    public Image(int path_id, double latitude, double longitude, Date time) {
        this.path_id = path_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    /*
      The getters and the setters.
     */
    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getPath_id() {
        return path_id;
    }

    public void setPath_id(int path_id) {
        this.path_id = path_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Judge whether the objects are the same.
     * @param obj The object.
     * @return True if the objects are the same, else, return false.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
