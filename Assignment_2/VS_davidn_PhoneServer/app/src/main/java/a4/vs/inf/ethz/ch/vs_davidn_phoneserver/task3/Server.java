package a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task3;

import android.content.Context;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by philipp on 15.10.17.
 */
//Simplify & beautify
//https://www.journaldev.com/10356/android-broadcastreceiver-example-tutorial

public class Server implements SensorEventListener {

    ServerSocket serverSocket;
    String message = "";
    Context context;

    private final AssetManager mAssets;
    private final int mPort;
    private SensorManager mSensorManager;
    private Sensor mLight;
    private Sensor mAccel;
    private float mLightValue;
    private String mAccelString;

    public Server(int port, AssetManager assets, Context context) {
        mPort = port;
        mAssets = assets;
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //register sensors
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);

        Thread serverThread = new Thread(new serverThread());
        serverThread.start();
    }

    public int getPort(){
        return mPort;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                mSensorManager.unregisterListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(Socket socket) throws IOException {
        BufferedReader reader = null;
        PrintStream output = null;

        try {
            String route = null;

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            while (!TextUtils.isEmpty(line = reader.readLine())) {
                System.out.println("LINE IS :"+line);
                int start,end;
                start = line.indexOf('/') + 1;

                //Default site
                if(line.startsWith("GET /") && !line.contains(".html")){
                    route = "index.html";
                }
                else if (line.startsWith("GET /") && !line.contains("execute")) {
                    end = line.indexOf(' ', start);
                    route = line.substring(start, end);
                    break;
                }else if(line.startsWith("GET /") && line.contains("execute=Vibrate+phone")){
                    vibratePhone();
                    end = line.indexOf('?', start);
                    route = line.substring(start, end);
                    break;
                }
                else if(line.startsWith("GET /") && line.contains("execute=Show+toast+on+phone")){
                    showToast();
                    end = line.indexOf('?', start);
                    route = line.substring(start, end);
                    break;
                }


            }

            System.out.println("route is "+route);
            output = new PrintStream(socket.getOutputStream());

            if (null == route) {
                System.out.print("ERROR route is null");
                serverError(output);
                return;
            }



            String site = loadContent(route);
            if (null == site) {
                System.out.print("ERROR bytes is null");
                serverError(output);
                return;
            }

            if(route.contains("sensor1")){
                StringBuilder sb = new StringBuilder(site);
                int index = sb.indexOf("%");
                sb.deleteCharAt(index);
                sb.insert(index,"Accelerometer: "+mAccelString);
                site = sb.toString();

            }else if(route.contains("sensor2")){
                StringBuilder sb = new StringBuilder(site);
                int index = sb.indexOf("%");
                sb.deleteCharAt(index);
                sb.insert(index,"Light: "+mLightValue+" lx");
                site = sb.toString();
            }

            output.println("HTTP/1.0 200 OK");
            output.println("Content-Type: " + detectMimeType(route));
            output.println("Content-Length: " + site.getBytes().length);
            output.println();
            output.println(site);
            output.flush();
        } finally {
            if (null != output) {
                output.close();
            }
            if (null != reader) {
                reader.close();
            }
        }
    }

    public void vibratePhone(){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);

    }

    public void showToast(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context,"Hello from the Interwebz",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void serverError(PrintStream output) {

        output.println("HTTP/1.0 500 Internal Server Error");
        output.flush();

    }

    private String loadContent(String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream input;
        String toReturn = "";

        try {
            input = assetManager.open(fileName);

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            toReturn = new String(buffer);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return toReturn;
    }

    private String detectMimeType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        } else if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else {
            return "application/octet-stream";
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        if(sensorType == Sensor.TYPE_LIGHT){
            mLightValue = sensorEvent.values[0];
        }else if(sensorType == Sensor.TYPE_ACCELEROMETER){
            mAccelString = String.format("<br />x: %.3f m/s^2<br />y: %.3f m/s^2<br />z: %.3f m/s^2", sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2] );
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Do nothing
    }

    private class serverThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(mPort);

                while (true) {
                    Socket socket = serverSocket.accept();
                    count++;

                    final Runnable r3 = new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(message);
                        }
                    };
                    r3.run();

                    replyThread replyThread =
                            new replyThread(socket, count);
                    replyThread.run();

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class replyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;

        replyThread(Socket socket, int c) {
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {

            try {
                handle(hostThreadSocket);
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("ERROR");
            }
        }

    }

}