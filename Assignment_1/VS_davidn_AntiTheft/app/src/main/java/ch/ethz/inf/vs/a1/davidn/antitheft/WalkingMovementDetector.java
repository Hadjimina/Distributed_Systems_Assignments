package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.hardware.SensorEvent;
import android.util.Log;

import static java.lang.Math.abs;

/**
 * Created by jzfri on 11.10.2017.
 * This MovementDetector takes 50 Sensor samples and takes the average absolute
 * sum + sensitivity as  threshold, hence you can use the alarm while walking and keeping the phone in
 * the pocket without triggering the Alarm. This Movement Detector can be enabled in the settings.
 * Delay and Sensitivity adjusted in settings have also effect on this Movement Detector!
 */

public class WalkingMovementDetector extends AbstractMovementDetector {

    private float[] samples = new float[50];
    private int sampleCount = 0;
    private float average = 0f;

    public WalkingMovementDetector(AlarmCallback mCallback, int sensitivity) {
        super(mCallback, sensitivity);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //make a sample and then wait 10s. Do this 10 times. Then calculate average.
        if(sampleCount < 50 && sampleCount>=0){
            float[] ithsample = event.values.clone();
            samples[sampleCount] = abs(ithsample[0]) + abs(ithsample[1]) + abs(ithsample[2]);
            sampleCount++;
            Log.i("#", "asdf sampling " + sampleCount);
        }else if(sampleCount == 50) {
            float sum = 0;
            for (int i = 0; i < 50; i++) {
                sum += samples[i];
            }
            sampleCount++;
            average = sum / 50;
            Log.i("#", "asdf calculate average");
        }else{
            super.onSensorChanged(event);
        }
    }

    @Override
    public boolean doAlarmLogic(float[] values) {
        Log.i("#", "asdf doing Walking movement alarmlogic.");
        return ((abs(values[0]) + abs(values[1]) + abs(values[2])) >= (average + mSensitivity));
    }
}
