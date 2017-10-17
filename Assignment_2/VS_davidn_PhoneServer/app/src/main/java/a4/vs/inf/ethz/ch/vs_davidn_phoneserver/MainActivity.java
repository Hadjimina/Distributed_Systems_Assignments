package a4.vs.inf.ethz.ch.vs_davidn_phoneserver;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    TextView mStateValue;
    private ToggleButton mServerToggle;
    private Helper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStateValue = (TextView) findViewById(R.id.stateValue);
        mServerToggle = (ToggleButton) findViewById(R.id.toggleServer);
        mHelper = new Helper();

        mStateValue.setText("Not running");
        mStateValue.setTextColor(Color.RED);

        final Intent startService = new Intent(this, bkgService.class);


        mServerToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mStateValue.setText(mHelper.getIpAddress()+":"+mHelper.getPort()+"/index.html");
                    mStateValue.setTextColor(Color.GREEN);
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
