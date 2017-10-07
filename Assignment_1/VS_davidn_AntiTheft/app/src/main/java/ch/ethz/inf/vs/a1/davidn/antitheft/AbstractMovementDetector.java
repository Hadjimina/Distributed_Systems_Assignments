package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.SENSOR_SERVICE;

public abstract class AbstractMovementDetector implements SensorEventListener {

    protected AlarmCallback callback;
    protected int sensitivity;
    Context mContext;

    private SensorManager mSensorManager;

    public AbstractMovementDetector(AlarmCallback callback, int sensitivity, Context mContext){
        this.callback = callback;
        this.sensitivity = sensitivity;
        mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }

    // Sensor monitoring
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            // Copy values because the event is not owned by the application
            float[] values = event.values.clone();
            if(doAlarmLogic(values)){
                callback.onDelayStarted();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do not do anything
    }

    public abstract boolean doAlarmLogic(float[] values);

}
