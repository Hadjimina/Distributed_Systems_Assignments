package ch.ethz.inf.vs.a1.davidn.sensors;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE;
import static android.hardware.Sensor.TYPE_GRAVITY;
import static android.hardware.Sensor.TYPE_GYROSCOPE;
import static android.hardware.Sensor.TYPE_LIGHT;
import static android.hardware.Sensor.TYPE_LINEAR_ACCELERATION;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;
import static android.hardware.Sensor.TYPE_ORIENTATION;
import static android.hardware.Sensor.TYPE_PRESSURE;
import static android.hardware.Sensor.TYPE_PROXIMITY;
import static android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY;
import static android.hardware.Sensor.TYPE_ROTATION_VECTOR;

/**
 * Created by philipp on 10/2/17.
 */

public class SensorInformation implements SensorTypes{

    public int getNumberValues(int sensorType){
        switch (sensorType){
            case TYPE_ACCELEROMETER:
                return 3;
            case TYPE_AMBIENT_TEMPERATURE:
                return 1;
            case TYPE_GRAVITY:
                return 3;
            case TYPE_GYROSCOPE:
                return 3;
            case TYPE_LIGHT:
                return 1;
            case TYPE_LINEAR_ACCELERATION:
                return 3;
            case TYPE_MAGNETIC_FIELD:
                return 3;
            case TYPE_ORIENTATION:
                return 3;
            case TYPE_PRESSURE:
                return 1;
            case TYPE_PROXIMITY:
                return 1;
            case TYPE_RELATIVE_HUMIDITY:
                return 1;
            case TYPE_ROTATION_VECTOR:
                return 3; //Could also be 5 => check documentation "https://developer.android.com/reference/android/hardware/SensorEvent.html#values"
            default:
                return -1;
        }
    }



    public String getUnitString(int sensorType){
        switch (sensorType){
            case TYPE_ACCELEROMETER:
                return "m/s^2";
            case TYPE_AMBIENT_TEMPERATURE:
                return "°C";
            case TYPE_GRAVITY:
                return "m/s^2";
            case TYPE_GYROSCOPE:
                return "radians/s";
            case TYPE_LIGHT:
                return "lux";
            case TYPE_LINEAR_ACCELERATION:
                return "m/s^2";
            case TYPE_MAGNETIC_FIELD:
                return "uT";
            case TYPE_ORIENTATION:
                return "degrees";
            case TYPE_PRESSURE:
                return "hPa";
            case TYPE_PROXIMITY:
                return "cm";
            case TYPE_RELATIVE_HUMIDITY:
                return "%";
            case TYPE_ROTATION_VECTOR:
                return "degrees"; //Unsure if correct
            default:
                return "error";
        }
    }
}
