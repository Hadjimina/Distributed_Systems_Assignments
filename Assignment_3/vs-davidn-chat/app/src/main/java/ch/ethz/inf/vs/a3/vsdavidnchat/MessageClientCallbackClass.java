package ch.ethz.inf.vs.a3.vsdavidnchat;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by philipp on 29.10.17.
 */

public abstract class MessageClientCallbackClass extends AppCompatActivity {
    abstract public void handleMessageResponse(String response);
}
