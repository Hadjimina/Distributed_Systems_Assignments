package ch.ethz.inf.vs.a1.davidn.antitheft;

import static java.lang.Math.abs;

public class SpikeMovementDetector extends AbstractMovementDetector {

    public SpikeMovementDetector(AlarmCallback mCallback, int sensitivity) {
        super(mCallback, sensitivity);
    }

    @Override
    //Find out if Alarm should go off
    public boolean doAlarmLogic(float[] values) {

        return ((abs(values[0]) + abs(values[1]) + abs(values[2])) >= mSensitivity);

    }
}
