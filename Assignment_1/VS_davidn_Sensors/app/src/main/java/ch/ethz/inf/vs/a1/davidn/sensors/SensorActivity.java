package ch.ethz.inf.vs.a1.davidn.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static ch.ethz.inf.vs.a1.davidn.sensors.R.id.graph;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorInformation mInfo;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mSensorId;
    private TextView dummy; //TODO delte dummy textview
    private double graph2LastXValue = 0;
    private GraphView mGraph;
    private LineGraphSeries<DataPoint> mSeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        setTitle("Sensors");
        this.dummy = (TextView) findViewById(R.id.textView);

        //Initialze helper class & sensors
        this.mInfo = new SensorInformation();
        this.mSensorId = getIntent().getIntExtra("sensorId",-1);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(mSensorId);
        mGraph = (GraphView) findViewById(graph);
        mSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 0),new DataPoint(2, 1000), new DataPoint(3, 3000)});
        graph2LastXValue += 2;
        mGraph.addSeries(mSeries);


        //Styling



        //scrolling
        Viewport vp = mGraph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(20);

        // set manual Y bounds
        vp.setYAxisBoundsManual(true);
        vp.setMinY(0);
        vp.setMaxY(3000);

        //Setup of axis labels
        GridLabelRenderer gridLabel = mGraph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("s");
        gridLabel.setVerticalAxisTitle(mInfo.getUnitString(mSensorId));

        dummy.setText("sensor type = "+ mSensorId +"\n type of value: "+ mInfo.getUnitString(mSensorId)+"\n number of values: "+ mInfo.getNumberValues(mSensorId));
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {

        //Math.abs used b.c. of error being "-1"
        if(Math.abs(mInfo.getNumberValues(mSensorId)) > 1 ){
            dummy.setText("sensor type = "+ mSensorId +
                    "\n type of value: "+ mInfo.getUnitString(mSensorId)+
                    "\n number of values: "+ mInfo.getNumberValues(mSensorId)+
                    "\n values: "+event.values[0]+" "+event.values[1]+" "+event.values[2]);

        }else{
            graph2LastXValue += 1;
            mSeries.appendData(new DataPoint(graph2LastXValue, event.values[0]), true, 1);

            dummy.setText("sensor type = "+ mSensorId +
                    "\n type of value: "+ mInfo.getUnitString(mSensorId)+
                    "\n number of values: "+ mInfo.getNumberValues(mSensorId)+
                    "\n values: "+event.values[0]);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
