package ch.ethz.inf.vs.a1.davidn.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter;
    ListView listView;
    List<ScanResult> results;
    List<String> deviceArray;
    ArrayAdapter<String> listAdapter;
    BluetoothLeScanner scanner;
    boolean permissionGranted = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        int permissionCheck3 = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck3 == 0) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        listView = (ListView) findViewById(R.id.listView);
        deviceArray = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceArray);
        listView.setAdapter(listAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
                Object item = adapter.getItemAtPosition(position);
                BluetoothDevice device = null;
                for (int i = 0; i < results.size(); i++){
                    if(item.toString().equals(results.get(i).getDevice().getName())){
                        device = results.get(i).getDevice();
                    }
                }
                scanner.stopScan(callback);
                Intent intent = new Intent(MainActivity.this,connectionActivity.class);
                intent.putExtra("DEVICE", device);
                startActivity(intent);
            }
        });

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        scanDevices();

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    void scanDevices() {
        scanner = mBluetoothAdapter.getBluetoothLeScanner();
        List<ScanFilter> filters = new ArrayList<>();
        filters.add(filter());
        Handler mHandler = new Handler();
        results = new ArrayList<ScanResult>();
        // Stops scanning after 10 seconds.
        final long SCAN_PERIOD = 20000;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scanner.stopScan(callback);
            }
        }, SCAN_PERIOD);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            scanner.startScan(filters, settings(), callback);
        }


    }


    ScanCallback callback = new ScanCallback()
    {

        @Override
        public void onScanResult ( int callbackType, ScanResult result){
            updateList(result.getDevice().getName());
            System.out.println("Do hätts ebbis");
            results.add(result);
        }

        @Override
        public void onBatchScanResults (List < ScanResult > results) {
        }

        @Override
        public void onScanFailed ( int errorCode){
        System.out.println("BLE// onScanFailed");
        Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    ScanFilter filter(){
        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setDeviceName("Smart Humigadget");
        return builder.build();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    ScanSettings settings(){
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setCallbackType(1);
        return builder.build();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
        }
    }

    void updateList(String deviceName){
        deviceArray.add(deviceName);
        listAdapter.notifyDataSetChanged();
    }



}
