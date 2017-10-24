package a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import a2.vs.inf.ethz.ch.vs_davidn_Webservices.R;

public class RestClientActivity extends AppCompatActivity implements SensorListener, Button.OnClickListener {
    double temperature = 0.0;
    RawHttpSensor httpSensor;
    TextSensor textSensor;
    JsonSensor jsonSensor;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);

        final Button buttonRaw = (Button) findViewById(R.id.button9);
        final Button buttonText = (Button) findViewById(R.id.button10);
        final Button buttonJson = (Button) findViewById(R.id.button11);

        buttonRaw.setOnClickListener(this);
        buttonText.setOnClickListener(this);
        buttonJson.setOnClickListener(this);

        //initialize TextViews
        txt = (TextView) findViewById(R.id.restTxt);
    }

    @Override
    public void onReceiveSensorValue(double value) {
        Log.d("#", "temperature ist updated.");
        temperature = value;
        txt.setText("Temperature: " + value);
    }

    @Override
    public void onReceiveMessage(String message) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button9:
                Log.d("#", "Constructing RawHttpSensor.");
                httpSensor = new RawHttpSensor();
                httpSensor.registerListener(this);
                httpSensor.getTemperature();
                break;

            case R.id.button10:
                Log.d("#", "Constructing TextSensor.");
                textSensor = new TextSensor();
                textSensor.registerListener(this);
                textSensor.getTemperature();
                break;

            case R.id.button11:
                Log.d("#", "Constructing JsonSensor.");
                jsonSensor = new JsonSensor();
                jsonSensor.registerListener(this);
                jsonSensor.getTemperature();
                break;

            default:
                break;
        }
    }
}
