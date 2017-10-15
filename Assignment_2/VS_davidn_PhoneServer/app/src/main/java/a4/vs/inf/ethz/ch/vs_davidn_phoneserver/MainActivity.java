package a4.vs.inf.ethz.ch.vs_davidn_phoneserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    TextView mStateValue;
    private ToggleButton mServerToggle;
    private boolean mServerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStateValue = (TextView) findViewById(R.id.stateValue);
        mServerToggle = (ToggleButton) findViewById(R.id.toggleServer);

        final Intent startService = new Intent(this, bkgService.class);


        mServerToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startService(startService);

                }else{
                    stopService(startService);
                }
            }
        });

    }

}
