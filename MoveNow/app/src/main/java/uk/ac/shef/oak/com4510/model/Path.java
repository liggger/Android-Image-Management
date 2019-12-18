package uk.ac.shef.oak.com4510.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

/**
 * @description The Path entity.
 * @author Zhicheng Zhou
 */

@Entity
@TypeConverters({Converters.class})
public class Path {

    // The path id.
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "path_id")
    private int path_id;

    // The path title.
    @ColumnInfo(name = "title")
    private String title;

    // The date of the tracking time.
    @ColumnInfo(name = "date")
    private Date date;

    // The latest pressure data for every 20 seconds.
    @ColumnInfo(name = "pressure")
    private String pressure;

    // The latest temperature data for every 20 seconds.
    @ColumnInfo(name = "temperature")
    private String temperature;

    // All the pictures' latitude.
    @ColumnInfo(name = "latitude_list")
    private List<Double> latitudeList;

    // All the pictures' longitude.
    @ColumnInfo(name = "longitude_list")
    private List<Double> longitudeList;

    /**
     * The constructor of the path.
     * The path id generates automatically.
     *
     * @param title The path title.
     * @param date The path date.
     * @param pressure The value of the pressure sensor.
     * @param temperature The value of the temperature sensor.
     * @param latitudeList The latitude list.
     * @param longitudeList The longitude list.
     */
    public Path(String title, Date date, String pressure, String temperature, List<Double> latitudeList, List<Double> longitudeList) {
        this.title = title;
        this.date = date;
        this.pressure = pressure;
        this.temperature = temperature;
        this.latitudeList = latitudeList;
        this.longitudeList = longitudeList;
    }

    /*
      The getters and the setters.
     */

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

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
