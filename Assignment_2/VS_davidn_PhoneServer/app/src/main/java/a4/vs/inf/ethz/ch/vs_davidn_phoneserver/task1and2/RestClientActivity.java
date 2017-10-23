package a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import a4.vs.inf.ethz.ch.vs_davidn_phoneserver.R;

public class RestClientActivity extends AppCompatActivity implements SensorListener {
    double temperature = 0.0;
    RawHttpSensor httpSensor;
    TextSensor textSensor;
    JsonSensor jsonSensor;
    TextView txt1;
    TextView txt2;
    TextView txt3;
    int sensorIterator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);

        //initialize TextViews
        txt1 = (TextView) findViewById(R.id.httpRawSensorTxt);
        txt1.setText("Temperature from HttpRawSensor: 0.0");
        txt2 = (TextView) findViewById(R.id.textSensorText);
        txt2.setText("Temperature from TextSensor: 0.0");
        txt3 = (TextView) findViewById(R.id.jsonSensorTxt);
        txt3.setText("Temperature from JsonSensor: 0.0");

        //initialize Sensors

        httpSensor = new RawHttpSensor();
        httpSensor.registerListener(this);
        sensorIterator = 1;
        httpSensor.getTemperature();

        textSensor = new TextSensor();
        textSensor.registerListener(this);
        sensorIterator = 2;
        textSensor.getTemperature();

        jsonSensor = new JsonSensor();
        jsonSensor.registerListener(this);
        sensorIterator = 3;
        jsonSensor.getTemperature();
    }

    @Override
    public void onReceiveSensorValue(double value) {
        Log.d("#", "temperature ist updated.");
        temperature = value;
        if(sensorIterator == 1){
            txt1.setText("Temperature from RawHttpSensor: " + value);
        }else if(sensorIterator == 2){
            txt2.setText("Temperature from TextSensor: " + value);
        }else if(sensorIterator == 3){
            txt3.setText("Temperature from JsonSensor: " + value);
        }
    }

    @Override
    public void onReceiveMessage(String message) {

    }
}
