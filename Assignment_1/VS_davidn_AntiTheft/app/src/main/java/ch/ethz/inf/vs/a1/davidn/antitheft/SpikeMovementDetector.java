package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.content.Context;

public class SpikeMovementDetector extends AbstractMovementDetector {

    public SpikeMovementDetector(AlarmCallback callback, int sensitivity, Context mContext) {
        super(callback, sensitivity, mContext);
    }

    @Override
    public boolean doAlarmLogic(float[] values) {
        // do alarm logic here
        return false;
    }
}
