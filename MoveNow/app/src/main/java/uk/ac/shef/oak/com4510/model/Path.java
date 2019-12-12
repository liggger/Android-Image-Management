package uk.ac.shef.oak.com4510.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Entity
@TypeConverters({Converters.class})
public class Path {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "path_id")
    private int path_id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "latitude_list")
    private List<Double> latitudeList;

    @ColumnInfo(name = "longitude_list")
    private List<Double> longitudeList;

    public Path(String title, Date date, List<Double> latitudeList, List<Double> longitudeList) {
        this.title = title;
        this.date = date;
        this.latitudeList = latitudeList;
        this.longitudeList = longitudeList;
    }

    public int getPath_id() {
        return path_id;
    }

    public void setPath_id(int path_id) {
        this.path_id = path_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Double> getLatitudeList() {
        return latitudeList;
    }

    public void setLatitudeList(List<Double> latitudeList) {
        this.latitudeList = latitudeList;
    }

    public List<Double> getLongitudeList() {
        return longitudeList;
    }

    public void setLongitudeList(List<Double> longitudeList) {
        this.longitudeList = longitudeList;
    }
}
