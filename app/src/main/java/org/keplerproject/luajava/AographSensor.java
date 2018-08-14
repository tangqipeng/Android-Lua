package org.keplerproject.luajava;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;

import sk.kottman.androlua.MyApplication;

/**
 * Created by tangqipeng
 * 2018/8/9
 * email: tangqipeng@aograph.com
 */
public class AographSensor {

    private static SensorManager sm;
    private static List<Sensor> sensorList;

    public static void registerSensorListener() {
        Log.i("sssss", "registerSensorListener");

        if (sm == null) {
            sm = (SensorManager) MyApplication.context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);

            sensorList = sm.getSensorList(Sensor.TYPE_ALL);
        }
    }

    private static Sensor findSensor(int sensorType) {
        if (sensorList != null && sensorList.size() > 0) {
            for (Sensor sensor : sensorList) {
                if (sensor.getType() == sensorType) {
                    return sensor;
                }
            }
        }
        return null;
    }

    public static void openSensor(int sensorType){
        Log.i("sssss", "openSensor");
        Sensor sensor = findSensor(sensorType);
        if (sensor != null && sm != null) {
            Log.i("sssss", "sensor:"+sensor.getName());
            sm.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public static void closeSensor(int sensorType) {
        Sensor sensor = findSensor(sensorType);
        if (sensor != null) {
            sm.unregisterListener(sensorEventListener, sensor);
            sensorList.remove(sensor);
        }
    }

    private static SensorEventListener sensorEventListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent sensorEvent) {
//            int type = sensorEvent.sensor.getType();
//
//            switch (type){
//                case Sensor.TYPE_MAGNETIC_FIELD:
//                    break;
//                case Sensor.TYPE_ACCELEROMETER:
//                    break;
//                case Sensor.TYPE_LINEAR_ACCELERATION:
//                    break;
//                case Sensor.TYPE_ORIENTATION:
//                    break;
//                case Sensor.TYPE_PROXIMITY:
//                    break;
//            }

        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

    };

}
