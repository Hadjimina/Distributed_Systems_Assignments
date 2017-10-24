package a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2;

/**
 * Created by jzfri on 18.10.2017.
 */

public class HttpRawRequestImpl implements HttpRawRequest {

    @Override
    public String generateRequest(String host, int port, String path){
        StringBuilder sb = new StringBuilder();
        sb.append("GET " + path + " HTTP/1.1" + "\r\n");
        sb.append("Host: " + host + ":" + port + "\r\n");
        sb.append("Accept: text/html" + "\r\n");
        sb.append("Connection: close" + "\r\n" + "\r\n");

        return sb.toString();
    }
}
