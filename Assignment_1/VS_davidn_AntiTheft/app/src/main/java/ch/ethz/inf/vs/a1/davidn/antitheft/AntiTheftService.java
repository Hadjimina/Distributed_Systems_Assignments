package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AntiTheftService extends Service implements AlarmCallback{

    private Notification mNotification;


    public AntiTheftService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDelayStarted() {

    }

    //Create notification
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("SERVICE", "started");

        Intent intentA = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intentA, 0);

    // build notification
    // the addAction re-use the same intent to keep the example short
        mNotification  = new Notification.Builder(this)
                .setContentTitle("Your Device has been locked")
                .setContentText("Moving the device will trigger an alarm")
                .setSmallIcon(R.drawable.locked)
                .setContentIntent(pIntent)
                .setOngoing(true).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(42, mNotification);

        return super.onStartCommand(intent, flags, startId);
    }

    //Notification delete
    @Override
    public void onDestroy() {
        Log.i("SERVICE", "destroyed");
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(42);
        super.onDestroy();
    }
}
