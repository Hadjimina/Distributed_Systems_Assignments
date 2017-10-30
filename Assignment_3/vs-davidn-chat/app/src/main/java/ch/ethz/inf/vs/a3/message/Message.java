package ch.ethz.inf.vs.a3.message;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by jzfri on 28.10.2017.
 */

public class Message {

    String username, type, content, timestamp;
    UUID uuid;

    public Message(String username, UUID uuid, String timestamp, String type){

        this.username = username;
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.type = type;

    }

    public Message(JSONObject jsonObject){
        try {
            JSONObject header = jsonObject.getJSONObject("header");
            JSONObject body = jsonObject.getJSONObject("body");

            this.username = header.getString("username");
            this.uuid = UUID.fromString(header.getString("uuid"));
            JSONObject timestamp = header.getJSONObject("timestamp");
            this.timestamp = timestamp.toString();

            this.type = header.getString("type");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setContent(JSONObject body) {
        try{
            this.content = body.getString("content");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setContent(String content){
        this.content = content;
    }



    public JSONObject getJson(){
        JSONObject json = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            header.put("username", username);
            header.put("uuid", uuid.toString());
            header.put("timestamp", timestamp);
            header.put("type", type);

            json.put("header", header);
            json.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("#", "message: " + json.toString());
        return json;
    }



}
