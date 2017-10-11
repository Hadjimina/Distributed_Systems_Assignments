package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public abstract class AbstractMovementDetector implements SensorEventListener {

    protected AlarmCallback callback;
    protected int mSensitivity;
    private boolean alarmSet;


    public AbstractMovementDetector(AlarmCallback mCallback, int sensitivity){
        this.callback = mCallback;
        this.mSensitivity = sensitivity;

    }

    // Sensor monitoring
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("#", "onSensorChanged");
        if (!alarmSet){
            if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                // Copy values because the event is not owned by the application
                float[] values = event.values.clone();
                if (doAlarmLogic(values)) {
                    alarmSet = true;
                    callback.onDelayStarted();
                    //sound alarm
                }
            }
        }
    }

    public void setSensitivity(int sens){
        mSensitivity = sens;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do not do anything
    }

    public abstract boolean doAlarmLogic(float[] values);

}
