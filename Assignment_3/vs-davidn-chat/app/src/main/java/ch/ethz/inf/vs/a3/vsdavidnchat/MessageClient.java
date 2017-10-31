package ch.ethz.inf.vs.a3.vsdavidnchat;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.UUID;

import ch.ethz.inf.vs.a3.udpclient.NetworkConsts;

/**
 * Created by jzfri on 27.10.2017.
 */

public class MessageClient extends AsyncTask {
    String port;
    String address;
    JSONObject message;
    String username;
    UUID uuid;
    NetworkConsts netConsts;
    String currentResp;
    // parent class for callback method
    MessageClientCallbackClass parentClass;
    //used to check if we get multiple response, if we only get one we do not wait for others
    boolean getsMultipleResponse;

    public MessageClient(JSONObject mes, String addr, String p, String usern, UUID u, MessageClientCallbackClass parentClass, boolean getsMultipleResponse){
        port = p;
        address = addr;
        message = mes;
        username = usern;
        uuid = u;
        netConsts = new NetworkConsts();
        this.parentClass = parentClass;
        this.getsMultipleResponse = getsMultipleResponse;

        Log.d("#", "Message Client created");
    }

    public void setMessage(JSONObject mess){
        message = mess;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("#", "doIn Background started");
        String response = "";
        int limit = 0;
        while (response.equals("") && limit < 20){
            response += sendAndReceive();
            limit++;
        }
        if (limit >= 20){
            throw new Error("Server does not respond");
        }
        currentResp = response;
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

            if(!getsMultipleResponse){

                s.receive(p2);
                //only get valid data
                String temp = new String(p2.getData());
                text += temp.substring(0, p2.getLength());
            }else{
                int counter = 0;
                while(true) {
                    try {
                        s.receive(p2);
                        //only get valid data
                        String temp = new String(p2.getData());
                        text += temp.substring(0, p2.getLength())+",";

                    }catch (SocketTimeoutException e) {
                        //only stops when we reach the timeout
                        text = "["+ text.substring(0, text.length()-1) + "]";
                        break;
                    }
                }
                counter++;
            }

            s.close();
            Log.d("#", "sendAndReceive values"+ text);
            return text;

        } catch (SocketException e) {;

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return text;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        parentClass.handleMessageResponse(currentResp);
    }
}


