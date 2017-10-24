package a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jzfri on 18.10.2017.
 */

class JsonSensor extends AbstractSensor {
    URL url;
    HttpURLConnection urlConnection;
    String response = "";

    @Override
    public String executeRequest() throws Exception {
        Log.d("#", "executing request of JsonSensor.");
        url = new URL("http://vslab.inf.ethz.ch:8081/sunspots/Spot1/sensors/temperature");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Connection", "close");
        urlConnection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        String userInput;
        while ((userInput = in.readLine()) != null) {
            response += userInput + "\r\n";
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        Log.d("#", "response is: " + response);
        return response;
    }

    @Override
    public double parseResponse(String response) {
        try {
            JSONObject temperature = new JSONObject(response);
            return temperature.getDouble("value");
        } catch (JSONException e) {
            e.printStackTrace();
            return 0.0;
        }

    }
}