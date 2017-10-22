package a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import a4.vs.inf.ethz.ch.vs_davidn_phoneserver.R;

public class RestClientActivity extends AppCompatActivity implements SensorListener {
    double temperature = 0.0;
    RawHttpSensor httpSensor;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("# RestClientActivity is being created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);

        //initialize RawHttpSensor
        httpSensor = new RawHttpSensor();
        httpSensor.registerListener(this);
        httpSensor.getTemperature();
        //initialize TextView
        txt = (TextView) findViewById(R.id.temp);
        txt.setText("Temperature: " + temperature);
    }

    @Override
    public void onReceiveSensorValue(double value) {
        temperature = value;
        txt.setText("Temperature: " + value);
    }

    @Override
    public void onReceiveMessage(String message) {

    }
}
