package a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import a4.vs.inf.ethz.ch.vs_davidn_phoneserver.R;

public class SoapClientActivity extends AppCompatActivity implements SensorListener{
    TextView txt1;
    XmlSensor xmlSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soap_client);

        //initialize TextView
        txt1 = (TextView) findViewById(R.id.soapSensorTxt);

        xmlSensor = new XmlSensor();
        xmlSensor.registerListener(this);
        xmlSensor.getTemperature();
    }

    @Override
    public void onReceiveSensorValue(double value) {
        Log.d("#", "Temperature updated");
        txt1.setText("Temperature: " + value);
    }

    @Override
    public void onReceiveMessage(String message) {

    }
}
