package ch.ethz.inf.vs.a3.vsdavidnchat;

import android.os.AsyncTask;
import android.util.Log;

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

public class MessageClient extends AsyncTask {
    String port;
    String address;
    JSONObject message;
    String username;
    UUID uuid;

    public MessageClient(JSONObject mes, String addr, String p, String usern, UUID u){
        port = p;
        address = addr;
        message = mes;
        username = usern;
        uuid = u;
        Log.d("#", "Message Client created");
    }

    public void setMessage(JSONObject mess){
        message = mess;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("#", "doIn Background started");
        String response = "";
        while (response.equals("")){
            response = sendAndReceive();
        }
        return response;
    }

    public String sendAndReceive(){
        Log.d("#", "sendAndReceive started");
        byte [] mess = message.toString().getBytes();
        byte [] response = new byte[1024];
        String text = "";
        DatagramSocket s;

        try {
            InetAddress serveraddr = InetAddress.getByName(address);
            s = new DatagramSocket (Integer.parseInt(port));

            Log.d("#", "starting to send");
            DatagramPacket p = new DatagramPacket(mess, mess.length, serveraddr, Integer.parseInt(port));
            s.setSoTimeout(10000);
            s.send(p);

            DatagramPacket p2 = new DatagramPacket(response, response.length);
            s.receive(p2);
            text = new String(p2.getData());
            s.close();
            return text;

        } catch (SocketException e) {;

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return text;
    }
}


