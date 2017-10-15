package a4.vs.inf.ethz.ch.vs_davidn_phoneserver;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by philipp on 15.10.17.
 */
//http://androidsrc.net/android-client-server-using-sockets-server-implementation/

public class Server {

    ServerSocket serverSocket;
    String message = "";
    static final int socketServerPORT = 8080;

    public Server() {
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                Log.i("SERVER asdf", "closed");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.i("SERVER asdf", "not closed");
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                // create ServerSocket using specified port
                serverSocket = new ServerSocket(socketServerPORT);

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
            OutputStream outputStream;
            String msgReply = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>My First Heading</h1>\n" +
                    "<p>My first paragraph.</p>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            try {
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();

                message += "replayed: " + msgReply + "\n";

                final Runnable r1 = (new Runnable() {

                    @Override
                    public void run() {
                        System.out.println(message);
                    }
                });
                r1.run();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            final Runnable r2 = (new Runnable() {

                @Override
                public void run() {
                    System.out.println(message);

                }
            });
            r2.run();
        }

    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
}