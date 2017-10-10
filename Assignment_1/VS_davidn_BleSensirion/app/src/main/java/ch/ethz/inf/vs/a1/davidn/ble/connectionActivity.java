package ch.ethz.inf.vs.a1.davidn.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import static ch.ethz.inf.vs.a1.davidn.ble.SensirionSHT31UUIDS.NOTIFICATION_DESCRIPTOR_UUID;
import static ch.ethz.inf.vs.a1.davidn.ble.SensirionSHT31UUIDS.UUID_HUMIDITY_CHARACTERISTIC;
import static ch.ethz.inf.vs.a1.davidn.ble.SensirionSHT31UUIDS.UUID_HUMIDITY_SERVICE;
import static ch.ethz.inf.vs.a1.davidn.ble.SensirionSHT31UUIDS.UUID_TEMPERATURE_CHARACTERISTIC;
import static ch.ethz.inf.vs.a1.davidn.ble.SensirionSHT31UUIDS.UUID_TEMPERATURE_SERVICE;

public class connectionActivity extends AppCompatActivity {
    BluetoothDevice device;
    BluetoothGattCharacteristic humCharacteristics;
    BluetoothGattCharacteristic tempCharacteristics;
    BluetoothGatt gatt;
    BluetoothGattDescriptor descriptorTemp;
    BluetoothGattDescriptor descriptorHum;
    TextView tempTextView;
    TextView humTextView;
    TextView tempData;
    TextView humData;
    Queue<Float> tempQueue;
    Queue<Float> humQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        tempTextView = (TextView) findViewById(R.id.tempTextView);
        humTextView = (TextView) findViewById(R.id.humTextView);
        tempData = (TextView) findViewById(R.id.humTextView);
        humData = (TextView) findViewById(R.id.humTextView);

        tempQueue = new LinkedList<Float>(){};
        humQueue = new LinkedList<Float>(){};


        device = getIntent().getParcelableExtra("DEVICE");
        System.out.println(device.getName());

        gatt = device.connectGatt(this, false, gattCallback);

        System.out.println("ZMORGÄ");
        ((TextView) findViewById(R.id.tempData)).setText("New Text");
        /*while(true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (humQueue.peek() != null || tempQueue.peek() != null) {
                if(humQueue.peek() != null){
                    //((TextView) findViewById(R.id.humData)).setText(String.valueOf(humQueue.poll()));
                    humQueue.poll();
                    System.out.println("POLLING FROM HUM");
                }
                if(tempQueue.peek() != null){
                    //((TextView) findViewById(R.id.tempData)).setText(String.valueOf(tempQueue.poll()));
                    System.out.println("POLLING FROM TEMP");
                }
            }
        }*/
    }

        public final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt.discoverServices();

                }
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                BluetoothGattService humService =
                        gatt.getService((UUID_HUMIDITY_SERVICE));
                humCharacteristics = humService.getCharacteristic(UUID_HUMIDITY_CHARACTERISTIC);
                BluetoothGattCharacteristic realHumCharacteristics =
                        new BluetoothGattCharacteristic(humCharacteristics.getUuid(), humCharacteristics.getProperties(), BluetoothGattCharacteristic.PERMISSION_READ);
                humService.addCharacteristic(realHumCharacteristics);

                BluetoothGattService tempService =
                        gatt.getService((UUID_TEMPERATURE_SERVICE));
                tempCharacteristics = tempService.getCharacteristic(UUID_TEMPERATURE_CHARACTERISTIC);
                BluetoothGattCharacteristic realTempCharacteristics =
                        new BluetoothGattCharacteristic(tempCharacteristics.getUuid(), tempCharacteristics.getProperties(), BluetoothGattCharacteristic.PERMISSION_READ);
                tempService.addCharacteristic(realTempCharacteristics);

                gatt.setCharacteristicNotification(humCharacteristics, true);
                gatt.setCharacteristicNotification(tempCharacteristics, true);
                descriptorHum = new BluetoothGattDescriptor(NOTIFICATION_DESCRIPTOR_UUID, BluetoothGattDescriptor.PERMISSION_READ);
                descriptorHum.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                descriptorTemp = new BluetoothGattDescriptor(NOTIFICATION_DESCRIPTOR_UUID, BluetoothGattDescriptor.PERMISSION_READ);
                descriptorTemp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                humCharacteristics.addDescriptor(descriptorHum);
                tempCharacteristics.addDescriptor(descriptorTemp);
                gatt.writeDescriptor(descriptorHum);
                gatt.writeDescriptor(descriptorTemp);

            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status){
                if(descriptor.getCharacteristic().getUuid().equals(UUID_HUMIDITY_CHARACTERISTIC)){
                    System.out.println(gatt.writeDescriptor(descriptorTemp));

                }
                /*else if (descriptor.getCharacteristic().getUuid().equals(UUID_TEMPERATURE_CHARACTERISTIC)){
                    gatt.writeDescriptor(descriptorHum);
                }*/
            }



            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristics){
                UUID uuid = characteristics.getUuid();
                final float value = convertRawValue(characteristics.getValue());
                if(uuid.equals(UUID_HUMIDITY_CHARACTERISTIC)){
                    System.out.println("HUMIDITY: "+value);
                    connectionActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ((TextView) findViewById(R.id.humData)).setText(String.valueOf(value));
                        }
                    });
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else if(uuid.equals(UUID_TEMPERATURE_CHARACTERISTIC)){
                    System.out.println("TEMPERATURE: "+convertRawValue(characteristics.getValue()));
                    connectionActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ((TextView) findViewById(R.id.tempData)).setText(String.valueOf(value));
                        }
                    });
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Log.d("NO CHARACTERISTICS", "Neither hum nor temp characteristics");
                }
            }
        };

    private float convertRawValue(byte[] raw) {
        ByteBuffer wrapper = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN);
        return wrapper.getFloat();
    }

}
