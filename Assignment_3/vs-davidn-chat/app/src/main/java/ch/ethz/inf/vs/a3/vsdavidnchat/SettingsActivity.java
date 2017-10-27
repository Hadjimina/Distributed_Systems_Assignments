package ch.ethz.inf.vs.a3.vsdavidnchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity implements Button.OnClickListener{
    Button buttonSave = (Button)findViewById(R.id.buttonSave);
    EditText editTextAddr;
    EditText editTextPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonSave:
                if (editTextAddr.getText() != null) {

                }
                if (editTextPort.getText() != null) {

                }
                break;
            default:
                break;
        }
    }
}
