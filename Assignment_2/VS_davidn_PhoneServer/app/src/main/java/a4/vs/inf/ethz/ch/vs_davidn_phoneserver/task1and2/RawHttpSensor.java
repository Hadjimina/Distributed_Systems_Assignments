package a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by jzfri on 18.10.2017.
 */

class RawHttpSensor extends AbstractSensor {
    String host = "vslab.ethz.ch";
    String path = "/sunspots/Spot1/sensors/temperature";
    int port = 8081;
    Socket skt;


    @Override
    public String executeRequest() throws Exception {

        HttpRawRequestImpl rawRequest = new HttpRawRequestImpl();
        String request = rawRequest.generateRequest(host, port, path);
        skt = new Socket(host, port);

        PrintWriter out = new PrintWriter(skt.getOutputStream(), false);
        //write request to socket outputStream
        out.print(rawRequest);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));

        String userInput;
        String response = "";

        while((userInput = in.readLine()) != null) {
            response += userInput + "\r\n";
        }
        return response;
    }

    @Override
    public double parseResponse(String response) {
        String start = "<span class=\"getterValue\">";
        String end = "</span>";
        String temperature = "0";


        if (response.contains(start)){
            int startIndex = response.indexOf(start) + start.length();
            int endIndex = response.indexOf(end);
            temperature = response.substring(startIndex, endIndex);
        }
        return Double.parseDouble(temperature);
    }

    @Override
    public void getTemperature() {

    }

    @Override
    public void registerListener(a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2.SensorListener listener) {

    }

    @Override
    public void unregisterListener(a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2.SensorListener listener) {

    }
}
