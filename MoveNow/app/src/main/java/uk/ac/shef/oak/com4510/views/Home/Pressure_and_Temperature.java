package uk.ac.shef.oak.com4510.views.Home;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * @description The pressure and temperature sensors.
 * @author Legao Dai
 */

public class Pressure_and_Temperature {
    // Returns the simple name of the Pressure and Temperature.
    private static final String TAG = Pressure_and_Temperature.class.getSimpleName();
    // The SensorManager.
    private SensorManager sensorManager;
    // The pressure sensor event listener.
    private SensorEventListener pressure_sensorEventListener = null;
    // The pressure sensor event listener.
    private SensorEventListener temperature_sensorEventListener = null;

    // The pressure sensor.
    private Sensor pressure;
    // The temperature sensor.
    private Sensor temperature;

    // The pressure list.
    private static ArrayList<Float> pressure_list = new ArrayList<>();
    // The temperature list.
    private static ArrayList<Float> temperature_list = new ArrayList<>();
    // The length of the pressure list.
    private static Integer pressureSize = pressure_list.size();
    // The length of the temperature list.
    private static Integer temperatureSize = temperature_list.size();

    /**
     * Initialize the pressure and temperature sensors.
     *
     * @param context The context.
     */
    public void initPressure_and_Temperature(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);


        if (!standardPressureSensorAvailable() && !standardTemperatureSensorAvailable()) {
            Log.d(TAG, "Error");
        } else {
            Log.d(TAG, "Successful");
            pressure_sensorEventListener = new SensorEventListener() {
                /*
                  Get the value of the pressure sensor.
                 */
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float press_value = sensorEvent.values[0];
                    pressure_list.add(press_value);
                    pressureSize++;
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };

            /*
              Get the value of the temperature sensor.
              */
            temperature_sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float temperature_value = sensorEvent.values[0];
                    temperature_list.add(temperature_value);
                    temperatureSize++;
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };

        }
    }


    /**
     * Check whether the pressure sensor is available.
     *
     * @return True if the pressure sensor is available, else, false.
     */
    private boolean standardPressureSensorAvailable() {
        return (pressure != null);
    }

    /**
     * Start the pressure sensor.
     */
    public void startPressureSensor() {
        try {
            sensorManager.registerListener(pressure_sensorEventListener, pressure, (1000 * 1000));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Stop the pressure sensor.
     */
    public void stopPressureSensor() {
        sensorManager.unregisterListener(pressure_sensorEventListener);
    }

    /**
     * Get the latest value of the pressure sensor.
     *
     * @return The pressure value
     */
    public String getPressure() {
        return String.valueOf(pressure_list.get(pressureSize - 1));
    }

    /**
     * Check whether the temperature sensor is available.
     *
     * @return True if the temperature sensor is available, else, false.
     */
    private boolean standardTemperatureSensorAvailable() {
        return (temperature != null);
    }

    /**
     * Start the temperature sensor.
     */
    public void startTemperatureSensor() {
        try {
            sensorManager.registerListener(temperature_sensorEventListener, temperature, (1000 * 1000));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Stop the temperature sensor.
     */
    public void stopTemperatureSensor() {
        sensorManager.unregisterListener(temperature_sensorEventListener);
    }


    /**
     * Get the latest value of the temperature sensor.
     *
     * @return The temperature value.
     */
    public String getTemperature() {
        return String.valueOf(temperature_list.get(temperatureSize - 1));
    }
}
