package ch.ethz.inf.vs.a3.vsdavidnchat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import ch.ethz.inf.vs.a3.message.Message;
import ch.ethz.inf.vs.a3.message.MessageTypes;
import ch.ethz.inf.vs.a3.udpclient.NetworkConsts;

public class MainActivity extends MessageClientCallbackClass implements Button.OnClickListener {
    EditText input;
    Button joinButton;
    Button settingsButton;
    String username, serverAddr, serverPort;
    UUID uuid;
    MessageClient sendCl;
    NetworkConsts netConsts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.editText);
        joinButton = (Button) findViewById(R.id.joinBtn);
        joinButton.setOnClickListener(this);
        settingsButton = (Button) findViewById(R.id.settingsBtn);
        settingsButton.setOnClickListener(this);
        username = "";
        netConsts = new NetworkConsts();
    }

    public void makeNotification(){
        Intent intent = new Intent(this, ChatActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Registered")
                .setContentText("You have successfully registered to the server!")
                //.setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

    public void register() throws JSONException {
        //get ServerAddress, ServerPort from Settings or use default.
        SharedPreferences sharedPref = getSharedPreferences("values", MODE_PRIVATE);
        serverAddr = sharedPref.getString("address", netConsts.SERVER_ADDRESS);
        serverPort = sharedPref.getString("port", netConsts.UDP_PORT+"");

        //make a random UUID.
        uuid = UUID.randomUUID();

        //make a registerMessage.
        MessageTypes types = new MessageTypes();
        Message registerMessage = new Message(username, uuid, "{}", types.REGISTER);
        JSONObject msg = registerMessage.getJson();

        //make new MessageClient to send register message
        sendCl = new MessageClient(msg, serverAddr, serverPort, username, uuid,this,false);
        String response = sendCl.doInBackground(null).toString();
        Log.d("#", "response: " + response);

        //check if response is ack
        JSONObject resp = new JSONObject(response);
        if(resp.getJSONObject("header").get("type").equals("ack")){
            Log.d("#", "response is an ack!");
            makeNotification();
            Intent myIntent = new Intent(this,ChatActivity.class);
            myIntent.putExtra("UUID",uuid.toString());
            startActivityForResult(myIntent, 0);
        }
    }

    public void deregister() throws JSONException {

        //make deregister Message.
        MessageTypes types = new MessageTypes();
        Message deregisterMessage = new Message(username, uuid, "{}", types.DEREGISTER);
        JSONObject msg = deregisterMessage.getJson();

        //make new MessageClient to send register message
        sendCl.setMessage(msg);
        String response = sendCl.doInBackground(null).toString();
        Log.d("#", "response: " + response);

        //check if response is ack
        JSONObject resp = new JSONObject(response);
        if(resp.getJSONObject("header").get("type").equals("ack")){
            Log.d("#", "response is an ack!");
            Intent myIntent = new Intent(this,ChatActivity.class);
            myIntent.putExtra("UUID",uuid.toString());
            startActivityForResult(myIntent, 0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.joinBtn:
                if (input.getText() != null) {
                    username = input.getText().toString();
                    try {
                        register();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.settingsBtn:
                Intent myIntent = new Intent(this,SettingsActivity.class);
                startActivityForResult(myIntent, 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        try {
            deregister();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        try {
            deregister();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void handleMessageResponse(String response) {
        Log.d("#", "THIS IS RESPONSE1: " + response);
    }
}
