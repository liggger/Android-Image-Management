package uk.ac.shef.oak.com4510.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Converters {

    @TypeConverter
    public static List<Double> stringToDoubleList(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Double>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String DoubleListToString(List<Double> latitude_list) {
        Gson gson = new Gson();
        return gson.toJson(latitude_list);
    }

    @TypeConverter
    public static Date fromString(String value) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @TypeConverter
    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateTime = format.format(date);
        return dateTime;
    }
}
