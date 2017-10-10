package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class AntiTheftService extends Service implements AlarmCallback, OnSharedPreferenceChangeListener{

    private Notification mNotification;
    private SpikeMovementDetector spike;
    private int sensitivity;
    private int delay;
    MediaPlayer mp;

    public AntiTheftService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDelayStarted() {

        //Make a delay
        Runnable r = new Runnable() {

            @Override
            //Make an Alarm sound
            public void run() {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                float vlm = 100;
                mp = MediaPlayer.create(getApplicationContext(), notification);
                mp.setVolume(vlm, vlm);
                mp.setLooping(true);
                mp.start();
                Log.i("alarmset", "alarm went off!");
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, delay * 1000);
    }

    //Create notification
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("servi", "service doesn't work");
        //Do the Movement detection
        spike = new SpikeMovementDetector(this, sensitivity, this);
        spike.registerSensorListener();



        Intent intentA = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intentA, 0);

        // build notification
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
        spike.unregisterSensorListener();

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(42);
        super.onDestroy();
    }

    //Update delay and sensitiviy if changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.i("prefchange", "");
        delay = sharedPreferences.getInt(getString(R.string.progress1), 5);
        sensitivity = sharedPreferences.getInt(getString(R.string.progress2), 10);
    }
}
