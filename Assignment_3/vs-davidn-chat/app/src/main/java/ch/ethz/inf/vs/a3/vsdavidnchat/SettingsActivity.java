package ch.ethz.inf.vs.a3.vsdavidnchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity implements Button.OnClickListener{
    Button buttonSave;
    EditText editTextAddr;
    EditText editTextPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editTextAddr = (EditText) findViewById(R.id.editTextAddr);
        editTextPort = (EditText) findViewById(R.id.editTextPort);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPref = getSharedPreferences("values", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (view.getId()) {

            case R.id.buttonSave:
                if (editTextAddr.getText() != null) {
                    editor.putString("address", editTextAddr.getText().toString());
                    editor.commit();
                }
                if (editTextPort.getText() != null) {
                    editor.putString("port", editTextPort.getText().toString());
                    editor.commit();
                }
                break;
            default:
                break;
        }
        finish();
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
