package ch.ethz.inf.vs.a3.vsdavidnchat;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;

/**
 * Created by jzfri on 27.10.2017.
 */

public class RegisterClient extends AsyncTask {
    String port;
    String address;
    JSONObject message;
    String username;
    UUID uuid;

    public RegisterClient(JSONObject mes, String addr, String p, String usern, UUID u){
        port = p;
        address = addr;
        message = mes;
        username = usern;
        uuid = u;
    }

    public void makeMessage() {

        JSONObject message = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            header.put("username", username);
            header.put("uuid", uuid.toString());
            header.put("timestamp", "{}");
            header.put("type", "register");

            message.put("header", header);
            message.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("#", "message: " + message.toString());

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    public int register(){
        makeMessage();
        byte [] mess = message.toString().getBytes();
        byte [] response = new byte[1024];

        try {
            InetAddress serveraddr = InetAddress.getByName(address);
            DatagramPacket p = new DatagramPacket(mess, mess.length, serveraddr, Integer.parseInt(port));
            DatagramSocket s = new DatagramSocket (Integer.parseInt(port));
            s.setSoTimeout(10000);
            s.send(p);
            DatagramPacket p2 = new DatagramPacket(response, response.length, serveraddr, Integer.parseInt(port));

            s.receive(p2);
            String text = new String(response,0,p.getLength());
            s.close();

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
}


