package a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jzfri on 18.10.2017.
 */

class XmlSensor extends AbstractSensor {
    @Override
    public String executeRequest() throws Exception {
        Log.d("#", "executing request.");
        String resp = "";

        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <S:Header/>\n" +
                "    <S:Body>\n" +
                "        <ns2:getSpot xmlns:ns2=\"http://webservices.vslecture.vs.inf.ethz.ch/\">\n" +
                "            <id>Spot3</id>\n" +
                "        </ns2:getSpot>\n" +
                "    </S:Body>\n" +
                "</S:Envelope>";

        URL url = new URL("http://vslab.inf.ethz.ch:8080/SunSPOTWebServices/SunSPOTWebservice");
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/xml");

        PrintWriter out = new PrintWriter(connection.getOutputStream(), false);
        out.print(request);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String userInput;
        while((userInput = in.readLine()) != null) {
            resp += userInput + "\r\n";
        }
        if(connection != null){
            connection.disconnect();
        }
        Log.d("#", "response is: " + resp);
        return resp;
    }

    @Override
    public double parseResponse(String response) throws XmlPullParserException, IOException {
        Log.d("#", "parsing response");
        boolean found = false;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput( new StringReader (response) );
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                if(xpp.getName().equals("temperature")){
                    found = true;
                    Log.d("#", "temperature found");
                }
            } else if(eventType == XmlPullParser.TEXT) {
                if(found){
                    return Double.parseDouble(xpp.getText());
                }
            }
            eventType = xpp.next();
        }
        return 0.0;
    }
}
