package ch.ethz.inf.vs.a3.vsdavidnchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import ch.ethz.inf.vs.a3.message.Message;
import ch.ethz.inf.vs.a3.message.MessageTypes;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {
    EditText input;
    Button joinButton;
    Button settingsButton;
    String username;
    String serverAddr;
    String serverPort;
    UUID uuid;
    MessageClient sendCl;

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
        //get ServerAddress, ServerPort from Settings or use default.
        SharedPreferences sharedPref = getSharedPreferences("values", MODE_PRIVATE);
        serverAddr = sharedPref.getString("address", "10.0.2.2");
        serverPort = sharedPref.getString("port", "4446");

        //make a random UUID.
        uuid = UUID.randomUUID();

        //make a registerMessage.
        MessageTypes types = new MessageTypes();
        Message registerMessage = new Message(username, uuid, "{}", types.REGISTER);
        JSONObject msg = registerMessage.message;

        //make new MessageClient to send register message
        sendCl = new MessageClient(msg, serverAddr, serverPort, username, uuid);
        String response = sendCl.doInBackground(null).toString();
        Log.d("#", "response: " + response);

        //check if response is ack
        JSONObject resp = new JSONObject(response);
        if(resp.getJSONObject("header").get("type").equals("ack")){
            Log.d("#", "response is an ack!");
            Intent myIntent = new Intent(this,ChatActivity.class);
            startActivityForResult(myIntent, 0);
        }
    }

    public void deregister(){

        //make deregister Message.
        MessageTypes types = new MessageTypes();
        Message deregisterMessage = new Message(username, uuid, "{}", types.DEREGISTER);
        JSONObject msg = deregisterMessage.message;

        //make new MessageClient to send register message
        sendCl.setMessage(msg);
        String response = sendCl.doInBackground(null).toString();
        Log.d("#", "response: " + response);
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

    @Override
    public void onBackPressed() {
        deregister();
        super.onBackPressed();
    }
}
