package ch.ethz.inf.vs.a3.vsdavidnchat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import ch.ethz.inf.vs.a3.message.Message;
import ch.ethz.inf.vs.a3.message.MessageComparator;
import ch.ethz.inf.vs.a3.message.MessageTypes;
import ch.ethz.inf.vs.a3.queue.PriorityQueue;
import ch.ethz.inf.vs.a3.udpclient.NetworkConsts;

public class ChatActivity extends MessageClientCallbackClass {

    UUID uuid;
    TextView msgView;
    PriorityQueue<Message> queue;
    NetworkConsts netConsts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        netConsts = new NetworkConsts();
        uuid = UUID.fromString(getIntent().getStringExtra("UUID"));
        msgView = (TextView)findViewById(R.id.messageView);
        MessageComparator comparator = new MessageComparator();
        queue = new PriorityQueue<>(comparator);


        try {
            getMessagesIntoQueue();
        } catch (JSONException e) {
            System.out.println("JSON ERROR");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("PROBABLY ASYNC ERROR");
            e.printStackTrace();
        }

        displayMessages();
    }



    public void displayMessages(){
        while(!queue.isEmpty()){
            Message current = queue.poll();
            msgView.append(current.toString() + "\n");
        }
    }

    public void getMessagesIntoQueue() throws JSONException, InterruptedException, ExecutionException, TimeoutException {
        //get ServerAddress, ServerPort from Settings or use default.
        SharedPreferences sharedPref = getSharedPreferences("values", MODE_PRIVATE);
        String serverAddr = sharedPref.getString("address", netConsts.SERVER_ADDRESS);
        String serverPort = sharedPref.getString("port", netConsts.UDP_PORT+"");

        String username = "";

        //make a registerMessage.
        MessageTypes types = new MessageTypes();
        Message chatLog = new Message(username, uuid, "{}", types.RETRIEVE_CHAT_LOG);
        JSONObject msg = chatLog.getJson();

        //make new MessageClient to send chat log message
        MessageClient sendCl = new MessageClient(msg, serverAddr, serverPort, username, uuid, this, true);

        sendCl.execute();


    }

    @Override
    public void handleMessageResponse(String response){
        Log.d("#", "THIS IS RESPONSE1: " + response);
        try {
            JSONArray jsonArray = new JSONArray(response);

            for(int i = 0; i < jsonArray.length(); i++){
                Message temp = new Message((JSONObject) jsonArray.get(i));
                queue.add(temp);
            }

        }catch (JSONException e){
            e.printStackTrace();
            Log.e("JSON", "JSON parse error occured");
        }

        displayMessages();
    }
}
