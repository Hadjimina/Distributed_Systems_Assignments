package a2.vs.inf.ethz.ch.vs_davidn_Webservices.task3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import a2.vs.inf.ethz.ch.vs_davidn_Webservices.Helper;
import a2.vs.inf.ethz.ch.vs_davidn_Webservices.R;

public class ServerActivity extends AppCompatActivity {

    TextView mStateValue;
    private ToggleButton mServerToggle;
    private Helper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        mStateValue = (TextView) findViewById(R.id.stateValue);
        mServerToggle = (ToggleButton) findViewById(R.id.toggleServer);
        mHelper = new Helper();

        //Check if service is running
        boolean isRunning = mHelper.serviceIsRunning(bkgService.class, getApplicationContext());
        if(isRunning){
            mStateValue.setText(mHelper.getIpAddress()+":"+mHelper.getPort());
            mStateValue.setTextColor(Color.BLACK);
            mServerToggle.setChecked(true);
        }else{
            mStateValue.setText("Not running");
            mStateValue.setTextColor(Color.RED);
        }



        final Intent startService = new Intent(this, bkgService.class);


        mServerToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mStateValue.setText(mHelper.getIpAddress()+":"+mHelper.getPort());
                    mStateValue.setTextColor(Color.BLACK);
                    startService(startService);

                }else{
                    mStateValue.setText("Not running");
                    mStateValue.setTextColor(Color.RED);
                    stopService(startService);
                }
            }
        });

    }

}
