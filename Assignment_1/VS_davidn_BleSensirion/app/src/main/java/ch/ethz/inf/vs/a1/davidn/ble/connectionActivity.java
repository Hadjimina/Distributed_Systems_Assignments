package ch.ethz.inf.vs.a1.davidn.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
    GraphView tempGraph;
    LineGraphSeries<DataPoint> tempSeries;
    double graph2LastXValueTemp= 0;
    GraphView humGraph;
    LineGraphSeries<DataPoint> humSeries;
    double graph2LastXValueHum= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        tempTextView = (TextView) findViewById(R.id.tempTextView);
        humTextView = (TextView) findViewById(R.id.humTextView);
        tempData = (TextView) findViewById(R.id.humTextView);
        humData = (TextView) findViewById(R.id.humTextView);
        TextView celsius = (TextView) findViewById(R.id.celsius);
        TextView percent = (TextView) findViewById(R.id.percent);

        //Sets up temperatur graph
        tempGraph = (GraphView) findViewById(R.id.tempGraph);
        Viewport vp1 = tempGraph.getViewport();
        vp1.setXAxisBoundsManual(true);
        vp1.setMinX(0);
        vp1.setMaxX(100);
        vp1.setYAxisBoundsManual(true);
        vp1.setMinY(0);
        vp1.setMaxY(50);
        tempSeries = new LineGraphSeries<>(new DataPoint[] {});
        tempSeries.setColor(Color.RED);
        tempGraph.addSeries(tempSeries);

        //Sets up humidity graph
        humGraph = (GraphView) findViewById(R.id.humGraph);
        Viewport vp = humGraph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(100);
        vp.setYAxisBoundsManual(true);
        vp.setMinY(0);
        vp.setMaxY(100);
        humSeries = new LineGraphSeries<>(new DataPoint[] {});
        humGraph.addSeries(humSeries);

        //Sets up GATT
        device = getIntent().getParcelableExtra("DEVICE");
        gatt = device.connectGatt(this, false, gattCallback);


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

                //Set up GATT services
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

                //Set up notifications
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
                    connectionActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ((TextView) findViewById(R.id.humData)).setText(String.valueOf(value));
                            graph2LastXValueHum += 1d;
                            humSeries.appendData(new DataPoint(graph2LastXValueHum, value), true, 100);
                        }
                    });
                    /*try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
                else if(uuid.equals(UUID_TEMPERATURE_CHARACTERISTIC)){
                    connectionActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ((TextView) findViewById(R.id.tempData)).setText(String.valueOf(value));
                            graph2LastXValueTemp += 1d;
                            tempSeries.appendData(new DataPoint(graph2LastXValueTemp, value), true, 100);
                        }
                    });
                    /*
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
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

    @Override
    public void onStop() {
        super.onStop();
        gatt.close();
    }

}
