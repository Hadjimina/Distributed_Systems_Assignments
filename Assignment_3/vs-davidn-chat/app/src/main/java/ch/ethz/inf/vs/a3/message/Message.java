package ch.ethz.inf.vs.a3.message;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by jzfri on 28.10.2017.
 */

public class Message {

    public JSONObject message;

    public Message(String username, UUID uuid, String timestamp, String type){

        message = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            header.put("username", username);
            header.put("uuid", uuid.toString());
            header.put("timestamp", timestamp);
            header.put("type", type);

            message.put("header", header);
            message.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("#", "message: " + message.toString());
    }

}
