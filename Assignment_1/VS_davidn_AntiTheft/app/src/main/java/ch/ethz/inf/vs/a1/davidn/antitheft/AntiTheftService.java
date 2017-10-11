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

    //Create notification
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences prefs = getSharedPreferences("values", MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(this);

        //Sensor initialization


        //Do the Movement detection
        spike = new SpikeMovementDetector(this, sensitivity);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(spike, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

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

    @Override
    public void onDelayStarted() {

        //Make a delay
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after delay
                Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                float vlm = 1.0f;
                mp = MediaPlayer.create(getApplicationContext(), alarm);
                mp.setVolume(vlm, vlm);
                mp.setLooping(true);
                mp.start();
                Log.i("alarmset", "alarm went off!");
            }
        }, delay * 1000);
    }


    //Update delay and sensitiviy if changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.i("prefchange", "preferenece got changed");
        delay = sharedPreferences.getInt(getString(R.string.progress1), 5);
        sensitivity = sharedPreferences.getInt(getString(R.string.progress2), 10);
        spike.setSensitivity(sensitivity);
    }

    //Notification delete, unregister Sensorlistener
    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(spike);

        mp.stop();

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(42);
        super.onDestroy();
    }


}
