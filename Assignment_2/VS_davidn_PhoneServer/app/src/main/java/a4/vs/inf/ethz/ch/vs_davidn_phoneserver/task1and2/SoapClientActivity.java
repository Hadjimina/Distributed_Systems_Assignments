package a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import a4.vs.inf.ethz.ch.vs_davidn_phoneserver.R;

public class SoapClientActivity extends AppCompatActivity implements SensorListener, Button.OnClickListener{
    TextView txt1;
    XmlSensor xmlSensor;
    SoapSensor soapSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soap_client);

        //initialize TextView and Buttons
        final Button buttonXml = (Button) findViewById(R.id.xmlSensorButton);
        final Button buttonSoap = (Button) findViewById(R.id.soapSensorButton);
        txt1 = (TextView) findViewById(R.id.soapTxt);

        buttonXml.setOnClickListener(this);
        buttonSoap.setOnClickListener(this);
    }

    @Override
    public void onReceiveSensorValue(double value) {
        Log.d("#", "Temperature updated");
        txt1.setText("Temperature: " + value);
    }

    @Override
    public void onReceiveMessage(String message) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xmlSensorButton:
                Log.d("#", "Constructing XmlSensor.");
                xmlSensor = new XmlSensor();
                xmlSensor.registerListener(this);
                xmlSensor.getTemperature();
                break;

            case R.id.soapSensorButton:
                Log.d("#", "Constructing SoapSensor.");
                soapSensor = new SoapSensor();
                soapSensor.registerListener(this);
                soapSensor.getTemperature();
                break;
            default:
                break;
        }
    }
}
