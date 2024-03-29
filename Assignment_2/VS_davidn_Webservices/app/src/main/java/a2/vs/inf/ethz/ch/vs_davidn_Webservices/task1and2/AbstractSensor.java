package a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2;

import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a sensor representation.
 *
 * @author Leyna Sadamori
 * @see Sensor
 * @see SensorListener
 */
public abstract class AbstractSensor implements a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2.Sensor {
    protected List<a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2.SensorListener> listeners = new ArrayList<a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2.SensorListener>();

    @Override
    public void registerListener(a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2.SensorListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2.SensorListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void getTemperature() {
        new AsyncWorker().execute();
    }

    /**
     * Send message to all listeners. Useful for error messages.
     *
     * @param message Message to be sent to listeners
     */
    protected void sendMessage(String message) {
        for (a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2.SensorListener listener : listeners) {
            listener.onReceiveMessage(message);
        }
    }

    /**
     * Send parsed sensor values to all listeners.
     *
     * @param value Sensor value to be sent to listeners
     */
    protected void sendValue(double value) {
        for (a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2.SensorListener listener : listeners) {
            listener.onReceiveSensorValue(value);
        }
    }

    /**
     * AsyncTask to execute the request in a separate thread.
     * The response is parsed by {@link a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2.Sensor#parseResponse(String)}
     * and the value is sent to the listeners.
     * Exceptions are caught and sent as a message to the listeners.
     *
     * @author Leyna Sadamori
     */
    public class AsyncWorker extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                return executeRequest();

            } catch (Exception e) {
                e.printStackTrace();
                publishProgress(e.toString());
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            sendMessage(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
                try {
                    sendValue(parseResponse(result));
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
