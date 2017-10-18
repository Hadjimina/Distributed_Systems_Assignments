package a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import a4.vs.inf.ethz.ch.vs_davidn_phoneserver.R;

public class RestClientActivity extends AppCompatActivity implements SensorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);
    }

    @Override
    public void onReceiveSensorValue(double value) {

    }

    @Override
    public void onReceiveMessage(String message) {

    }
}
