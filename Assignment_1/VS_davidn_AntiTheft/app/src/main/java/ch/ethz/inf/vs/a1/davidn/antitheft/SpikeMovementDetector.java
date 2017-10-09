package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.content.Context;
import android.util.Log;

import static java.lang.Math.abs;

public class SpikeMovementDetector extends AbstractMovementDetector {

    public SpikeMovementDetector(AlarmCallback mCallback, int sensitivity, Context mContext) {
        super(mCallback, sensitivity, mContext);
    }

    @Override
    //Find out if Alarm should go off
    public boolean doAlarmLogic(float[] values) {
        Log.i("alarmlogic", "succesful call to do alarmlogic");
        return ((abs(values[0]) + abs(values[1]) + abs(values[2])) >= mSensitivity);

    }
}
