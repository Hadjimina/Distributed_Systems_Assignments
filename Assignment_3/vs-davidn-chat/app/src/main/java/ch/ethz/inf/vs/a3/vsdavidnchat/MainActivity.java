package ch.ethz.inf.vs.a3.vsdavidnchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {
    EditText input;
    Button joinButton;
    Button settingsButton;
    String username;
    String serverAddr;
    String serverPort;
    UUID uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.editText);
        joinButton = (Button) findViewById(R.id.button);
        joinButton.setOnClickListener(this);
        settingsButton = (Button) findViewById(R.id.button2);
        settingsButton.setOnClickListener(this);
        username = "";
    }

    public void register() throws JSONException {
        SharedPreferences sharedPref = getSharedPreferences("values", MODE_PRIVATE);
        serverAddr = sharedPref.getString("address", "10.0.2.2");
        serverPort = sharedPref.getString("port", "4446");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button:
                if (input.getText() != null) {
                    username = input.getText().toString();
                    try {
                        register();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.button2:
                Intent myIntent = new Intent(this,SettingsActivity.class);
                startActivityForResult(myIntent, 0);
                break;
            default:
                break;
        }
    }
}
