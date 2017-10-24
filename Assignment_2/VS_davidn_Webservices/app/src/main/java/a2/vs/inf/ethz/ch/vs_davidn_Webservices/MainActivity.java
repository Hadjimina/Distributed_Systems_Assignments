package a2.vs.inf.ethz.ch.vs_davidn_Webservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2.RestClientActivity;
import a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2.SoapClientActivity;
import a2.vs.inf.ethz.ch.vs_davidn_Webservices.task3.ServerActivity;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonTask1 = (Button) findViewById(R.id.button);
        final Button buttonTask2 = (Button) findViewById(R.id.button2);
        final Button buttonTask3 = (Button) findViewById(R.id.button3);

        buttonTask1.setOnClickListener(this);
        buttonTask2.setOnClickListener(this);
        buttonTask3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.button:
                Log.d("#", "Task1 Activity starting.");
                intent = new Intent(this, RestClientActivity.class);
                break;

            case R.id.button2:
                Log.d("#", "Task2 Activity starting.");
                intent = new Intent(this, SoapClientActivity.class);
                break;

            case R.id.button3:
                Log.d("#", "Server Activity starting.");
                intent = new Intent(this, ServerActivity.class);
                break;

            default:
                intent = new Intent(this, RestClientActivity.class);
                break;
        }
        startActivity(intent);
    }
}
