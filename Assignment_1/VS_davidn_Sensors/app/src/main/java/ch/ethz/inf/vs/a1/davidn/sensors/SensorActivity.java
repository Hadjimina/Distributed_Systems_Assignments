package ch.ethz.inf.vs.a1.davidn.sensors;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Random;

import static ch.ethz.inf.vs.a1.davidn.sensors.R.id.graph;

public class SensorActivity extends AppCompatActivity implements SensorEventListener, GraphContainer {

    private SensorInformation mInfo;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mSensorId, mSize;
    private String mSensorName;
    private TextView textView;
    private long mStartTime;
    private long mCurrentX;//TODO change to real time
    private GraphView mGraph;
    private ArrayList<LineGraphSeries<DataPoint>> mSeriesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //TODO handle time
        mStartTime = System.currentTimeMillis();

        setTitle("Sensors");
        this.textView = (TextView) findViewById(R.id.textView);

        //Initialze helper class & sensors
        this.mInfo = new SensorInformation();
        this.mSensorId = getIntent().getIntExtra("sensorId",-1);
        this.mSensorName = getIntent().getStringExtra("sensorName");
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(mSensorId);
        mGraph = (GraphView) findViewById(graph);
        mSize = mInfo.getNumberValues(mSensorId);
        mSeriesList = new ArrayList<LineGraphSeries<DataPoint>>();

        //scrolling
        Viewport vp = mGraph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(100);


        //Setup of axis labels
        GridLabelRenderer gridLabel = mGraph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("i-th change in value");
        gridLabel.setVerticalAxisTitle(mInfo.getUnitString(mSensorId));

        //initializes dummy array & series list
        if(mSize != -1){
            float[] dummy = new float[mSize];
            for(int i = 0; i < mSize; i++){
                Log.i("Size", i+"");
                dummy[i] = 0.0f;

                //Get random graph color
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                LineGraphSeries<DataPoint> temp = new LineGraphSeries<>(new DataPoint[] {});
                temp.setColor(color);

                mSeriesList.add(temp);
                mGraph.addSeries(temp);
            }
            textView.setText(getUpperText(dummy));
        }else {
            textView.setText(mSensorName+"\n is not supported");
        }


    }

    public String getUpperText(float[] events){

        String toReturn = mSensorName;

        for(float f : events){
            toReturn += "\n"+f;
        }

        return toReturn;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {

        if(mSize == -1) return;

        long xValue = (System.currentTimeMillis() - mStartTime) / 1000;
        textView.setText(getUpperText(event.values));
        for(int j = 0; j < mSize; j++){
            mSeriesList.get(j).appendData(new DataPoint(mCurrentX, event.values[j]), true, 100);
        }
        mCurrentX++;

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


    //TODO implement functions
    @Override
    public void addValues(double xIndex, float[] values) {

    }

    @Override
    public float[][] getValues() {


       /* for( int j = 0; j < mCurrentX; j++){
            for(int i = 0; i < mSize; i++){
                toReturn[i] = mSeriesList.get(i).getY();
            }
        }*/


        return new float[][]{{0f},{0f}};
    }

}
