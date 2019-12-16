package uk.ac.shef.oak.com4510.views.Home;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

public class Pressure_and_Temperature {
    private static final String TAG = Pressure_and_Temperature.class.getSimpleName();
    private SensorManager sensorManager;
    private SensorEventListener pressure_sensorEventListener = null;
    private SensorEventListener temperature_sensorEventListener = null;

    private Sensor pressure;
    private Sensor temperature;

    private static ArrayList<Float> pressure_list = new ArrayList<>();
    private static ArrayList<Float> temperature_list = new ArrayList<>();
    private static Integer pressuresize = pressure_list.size();
    private static Integer temperaturesize = temperature_list.size();


    public void Pressure_and_Temperature(Context context){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    }

    public void initPressure_and_Temperature(Context context){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);


        if (!standardPressureSensorAvailable() && !standardTemperatureSensorAvailable()){
            Log.d(TAG,"Error");
        }
        else {
            Log.d(TAG,"Successful");
            pressure_sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float press_value = sensorEvent.values[0];
                    pressure_list.add(press_value);
                    pressuresize++;
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };

            temperature_sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float temperature_value = sensorEvent.values[0];
                    temperature_list.add(temperature_value);
                    temperaturesize++;
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };

        }
    }


    //pressure
    private boolean standardPressureSensorAvailable(){
        return (pressure!=null);
    }

    public void startPressureSensor(){
        try {
            sensorManager.registerListener(pressure_sensorEventListener,pressure,(1000*1000));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void stopPressureSensor(){
        sensorManager.unregisterListener(pressure_sensorEventListener);
    }


    public String getPressure(){
        String press_value = String.valueOf(pressure_list.get(pressuresize-1));
        return press_value;
    }

    //temperature
    private boolean standardTemperatureSensorAvailable(){
        return (temperature!=null);
    }

    public void startTemperatureSensor(){
        try {
            sensorManager.registerListener(temperature_sensorEventListener,temperature,(1000*1000));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void stopTemperatureSensor(){
        sensorManager.unregisterListener(temperature_sensorEventListener);
    }


    public String getTemperature(){
        String temperature_value = String.valueOf(temperature_list.get(temperaturesize-1));
        return temperature_value;
    }
}
