package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest implements AlarmCallback {

    boolean result = false;
    SpikeMovementDetector movementDetector;
    Context context = InstrumentationRegistry.getTargetContext();

    @Before
    public void setup() {
        result = false;
        movementDetector = new SpikeMovementDetector(this, 10, context);
    }

    @Test
    public void test1() throws Exception {
        float[] values1 = {0.0f, 0.0f, 0.0f};
        result = movementDetector.doAlarmLogic(values1);
        assertFalse(result);
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ch.ethz.inf.vs.a1.davidn.antitheft", appContext.getPackageName());
    }

    @Override
    public void onDelayStarted() {

    }
}
