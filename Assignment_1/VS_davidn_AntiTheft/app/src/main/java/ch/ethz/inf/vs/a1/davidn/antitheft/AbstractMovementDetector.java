package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.Context.SENSOR_SERVICE;

public abstract class AbstractMovementDetector implements SensorEventListener {

    protected AlarmCallback callback;
    protected int mSensitivity;
    private Context context;

    private Sensor mSensor;
    private SensorManager mSensorManager;


    public AbstractMovementDetector(AlarmCallback mCallback, int sensitivity, Context mContext){
        this.callback = mCallback;
        this.mSensitivity = sensitivity;
        context = mContext;

        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Log.i("movcreated", "");
    }

    //Register the Sensor
    public void registerSensorListener(){
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i("movreg", "is registered");
    }

    //Unregister the Sensor
    public void unregisterSensorListener(){
        mSensorManager.unregisterListener(this);
    }

    // Sensor monitoring
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("senchange", "");
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            // Copy values because the event is not owned by the application
            float[] values = event.values.clone();
            if(doAlarmLogic(values)){
                callback.onDelayStarted();
                //sound alarm
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do not do anything
    }

    public abstract boolean doAlarmLogic(float[] values);

}
