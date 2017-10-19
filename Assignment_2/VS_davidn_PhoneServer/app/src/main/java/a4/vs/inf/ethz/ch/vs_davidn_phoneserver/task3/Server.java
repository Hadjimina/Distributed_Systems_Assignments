package a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task3;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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

public class Server {

    ServerSocket serverSocket;
    String message = "";
    Context context;

    private final AssetManager mAssets;
    private final int mPort;

    public Server(int port, AssetManager assets, Context context) {
        mPort = port;
        mAssets = assets;
        this.context = context;

        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort(){
        return mPort;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
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

            // Read HTTP headers and parse out the route.
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while (!TextUtils.isEmpty(line = reader.readLine())) {
                System.out.println("LINE IS :"+line);
                int start,end;
                start = line.indexOf('/') + 1;

                if (line.startsWith("GET /") && !line.contains("execute")) {
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
            // Output stream that we send the response to
            output = new PrintStream(socket.getOutputStream());

            // Prepare the content to send.
            if (null == route) {
                System.out.print("ERROR route is null");
                writeServerError(output);
                return;
            }



            byte[] bytes = loadContent(route);
            if (null == bytes) {
                System.out.print("ERROR bytes is null");
                writeServerError(output);
                return;
            }

            // Send out the content.
            output.println("HTTP/1.0 200 OK");
            output.println("Content-Type: " + detectMimeType(route));
            output.println("Content-Length: " + bytes.length);
            output.println();
            output.write(bytes);
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

    private void writeServerError(PrintStream output) {
        output.println("HTTP/1.0 500 Internal Server Error");
        output.flush();
    }

    private byte[] loadContent(String fileName) throws IOException {
        InputStream input = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            input = mAssets.open(fileName);
            byte[] buffer = new byte[1024];
            int size;
            while (-1 != (size = input.read(buffer))) {
                output.write(buffer, 0, size);
            }
            output.flush();
            return output.toByteArray();
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (null != input) {
                input.close();
            }
        }
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

    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                // create ServerSocket using specified port
                serverSocket = new ServerSocket(mPort);

                while (true) {
                    // block the call until connection is created and return
                    // Socket object
                    Socket socket = serverSocket.accept();
                    count++;
                    message += "#" + count + " from "
                            + socket.getInetAddress() + ":"
                            + socket.getPort() + "\n";

                    final Runnable r3 = new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(message);
                        }
                    };
                    r3.run();

                    SocketServerReplyThread socketServerReplyThread =
                            new SocketServerReplyThread(socket, count);
                    socketServerReplyThread.run();

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket, int c) {
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