package a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.Proxy;

/**
 * Created by jzfri on 18.10.2017.
 */

class SoapSensor extends AbstractSensor {
    @Override
    public String executeRequest() throws Exception {

        Log.d("#", "executing request in SoapSensor");
        String NAMESPACE = "http://webservices.vslecture.vs.inf.ethz.ch/" ;
        String METHOD_NAME = "getSpot";
        String URL = "http://vslab.inf.ethz.ch:8080/SunSPOTWebServices/SunSPOTWebservice";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("id", "Spot3");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        Log.d("#", "envelope initialized");

        HttpTransportSE httpTransportSE = new HttpTransportSE(Proxy.NO_PROXY,URL,60000);
        httpTransportSE.call(URL, envelope);
        Log.d("#", "http call done");

        try {
            SoapObject response = (SoapObject) envelope.getResponse();
            String temp = response.getProperty(5).toString();
            Log.d("#", "result is: " + temp);
            return temp;

        }catch(Exception ex){

        }
        return "0.0";
    }

    @Override
    public double parseResponse(String response) {
        return Double.parseDouble(response);
    }
}
