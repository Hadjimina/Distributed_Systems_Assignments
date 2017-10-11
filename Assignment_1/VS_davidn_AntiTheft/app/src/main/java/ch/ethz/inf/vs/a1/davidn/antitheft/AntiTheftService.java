package ch.ethz.inf.vs.a1.davidn.antitheft;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class AntiTheftService extends Service implements AlarmCallback, OnSharedPreferenceChangeListener{

    private Notification mNotification;
    public SensorManager mSensorManager;
    private Sensor mSensor;
    private AbstractMovementDetector spikeOrWalk;
    private int sensitivity;
    private int delay;
    private boolean walkmov;
    MediaPlayer mp;



    public AntiTheftService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //Create notification
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences prefs = getSharedPreferences("values", MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(this);
        delay = prefs.getInt(getString(R.string.progress1), 5);
        sensitivity = prefs.getInt(getString(R.string.progress2), 10);
        walkmov = prefs.getBoolean(getString(R.string.walkmov), false);

        Log.d("#", "asdf got sharedpreferences in service " + sensitivity + " " + delay);

        //Do the Movement detection

        if(walkmov){
            spikeOrWalk = new WalkingMovementDetector(this, sensitivity);
        }else{
            spikeOrWalk = new SpikeMovementDetector(this, sensitivity);
        }

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(spikeOrWalk, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getApplicationContext(), alarm);

        Intent intentA = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intentA, 0);

        // build notification
        mNotification  = new Notification.Builder(this)
                .setContentTitle("Your Device has been locked")
                .setContentText("Moving the device will trigger an alarm")
                .setSmallIcon(R.drawable.locked)
                .setContentIntent(pIntent)
                .setOngoing(true).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(2, mNotification);

        return START_STICKY;
    }

    @Override
    public void onDelayStarted() {


        //Make a delay
        Log.d("#", "asdf on delay started " + delay);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after delay
                float vlm = 1.0f;
                mp.setVolume(vlm, vlm);
                mp.setLooping(true);
                mp.start();
            }
        }, delay * 1000);
    }


    //Update delay and sensitiviy if changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.i("#", "asdf preferenece changed");
        delay = sharedPreferences.getInt(getString(R.string.progress1), 5);
        sensitivity = sharedPreferences.getInt(getString(R.string.progress2), 10);
        spikeOrWalk.setSensitivity(sensitivity);
    }

    //Notification delete, unregister Sensorlistener
    @Override
    public void onDestroy() {
        Log.d("#", "asdf Service is being destroyed");
        mSensorManager.unregisterListener(spikeOrWalk);

        mp.stop();

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(2);
        Log.d("#", "asdf service successfully destroyed");
        super.onDestroy();
    }


}
