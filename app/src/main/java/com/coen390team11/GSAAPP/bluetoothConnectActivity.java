package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

    private static final int BLUETOOTH_CONNECT_CODE = 1;
    private static final int BLUETOOTH_SCAN_CODE = 2;
    private static final int BLUETOOTH_ADVERTISE_CODE = 3;
    private static final int BLUETOOTH_ADMIN_CODE = 4;
    private static final int BLUETOOTH_CODE = 5;

    // needs to be tested on actual android device, does not work on emulator
    // for now class is not used to avoid fatal exception, when enabling class
    // change intent in LoginActivity in method "userLoggedInSuccess" from MainActivity.class
    // to bluetoothConnectActivity.class in else statement

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

        connectionStatusTextView = findViewById(R.id.connectionStatusTextView);
        deviceListView = findViewById(R.id.deviceListView);
        searchButton = findViewById(R.id.searchButton);

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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // request bluetooth permissions when search button clicked
                checkPermission(Manifest.permission.BLUETOOTH_CONNECT,BLUETOOTH_CONNECT_CODE);
                checkPermission(Manifest.permission.BLUETOOTH_SCAN,BLUETOOTH_SCAN_CODE);
                checkPermission(Manifest.permission.BLUETOOTH_ADVERTISE,BLUETOOTH_ADVERTISE_CODE);
                checkPermission(Manifest.permission.BLUETOOTH_ADMIN,BLUETOOTH_ADMIN_CODE);
                checkPermission(Manifest.permission.BLUETOOTH,BLUETOOTH_CODE);

                if (checkPermission(Manifest.permission.BLUETOOTH_CONNECT,BLUETOOTH_CONNECT_CODE)
                        && checkPermission(Manifest.permission.BLUETOOTH_SCAN,BLUETOOTH_SCAN_CODE)
                        && checkPermission(Manifest.permission.BLUETOOTH_ADVERTISE,BLUETOOTH_ADVERTISE_CODE)){
                    Intent goToMainActivityIntent = new Intent(bluetoothConnectActivity.this,MainActivity.class);
                    startActivity(goToMainActivityIntent);
                }


            }
        });
    }

    public boolean checkPermission(String permission, int requestCode){

        if (ContextCompat.checkSelfPermission(bluetoothConnectActivity.this, permission) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "This permission is already granted.", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            ActivityCompat.requestPermissions(bluetoothConnectActivity.this,new String[] {permission}, requestCode);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == BLUETOOTH_CONNECT_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Bluetooth connecting permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth connecting permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == BLUETOOTH_SCAN_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Bluetooth scanning permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth scanning permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == BLUETOOTH_ADVERTISE_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Bluetooth advertise permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth advertise permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == BLUETOOTH_ADMIN_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Bluetooth admin permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth admin permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == BLUETOOTH_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Bluetooth permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}