package a4.vs.inf.ethz.ch.vs_davidn_phoneserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by philipp on 15.10.17.
 */

public class bkgService extends Service {

    private Server mServer;
    private String mIP;
    private int mPort;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mServer = new Server();
        mIP = mServer.getIpAddress();
        mPort = mServer.getPort();

        Log.i("SERVICE","started"+mIP+":"+mPort);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mServer.onDestroy();
        Log.i("SERVICE","destoryed");
    }

}