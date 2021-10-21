package com.coen390team11.GSAAPP;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class bluetoothConnectActivity extends AppCompatActivity {

    // needs to be tested on actual android device, does not work on emulator
    // for now class is commented to avoid fatal exception, when enabling class
    // change intent in "userLoggedInSuccess" from MainActivity.class
    // to bluetoothConnectActivity.class

    TextView connectionStatusTextView;
    ListView deviceListView;
    Button searchButton;
    ArrayList<String> availableDevices = new ArrayList<>();
    ArrayList<String> addresses = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    BluetoothAdapter bluetoothAdapter;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("ACTION --->",action);

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                connectionStatusTextView.setText("Search Complete.");
                searchButton.setEnabled(true);
            } else if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = bluetoothDevice.getName();
                String deviceAddress = bluetoothDevice.getAddress();
                String RSSI = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE)); // get RSSI
                Log.i("Device found","Name: " + deviceName + ", Address: " + deviceAddress + ", RSSI: " + RSSI);

                if (!addresses.contains(deviceAddress)){
                    addresses.add(deviceAddress);
                    String deviceStr = "";
                    if (deviceName == null || deviceName.equals("")){
                        deviceStr = deviceAddress + " - RSSI " + RSSI + "dBm";
                    }else {
                        deviceStr = deviceName + " - RSSI" + RSSI + "dBm";
                    }
                    availableDevices.add(deviceStr);
                    arrayAdapter.notifyDataSetChanged();
                }


            }
        }
    };

    public void search(View view){
        connectionStatusTextView.setText("Searching...");
        searchButton.setEnabled(false);
        availableDevices.clear();
        addresses.clear();
        bluetoothAdapter.startDiscovery();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);

        connectionStatusTextView = (TextView) findViewById(R.id.connectionStatusTextView);
        deviceListView = (ListView) findViewById(R.id.deviceListView);
        searchButton = (Button) findViewById(R.id.searchButton);

        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        String user_name = sharedPreferences.getString("logged_in_username","");
        Toast.makeText(getApplicationContext(), "Welcome " + user_name, Toast.LENGTH_SHORT).show();


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,availableDevices);
        deviceListView.setAdapter(arrayAdapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        /*if (bluetoothAdapter == null)
            Log.i("TAG","Bluetooth is not supported on your device");*/

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver,intentFilter);
    }
}