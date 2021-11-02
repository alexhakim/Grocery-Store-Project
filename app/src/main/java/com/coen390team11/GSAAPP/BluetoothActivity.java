package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public final String tag = "Bluetooth";
    BluetoothAdapter bluetoothAdapter;
    Button discoverableButton;
    BluetoothConnectionService bluetoothConnection;
    //Button startConnectionButton;
    //Button sendButton;
    //EditText editText;
    BluetoothDevice bluetoothDevice;
    public ArrayList<BluetoothDevice> bluetoothDevicesArrayList = new ArrayList<>();
    public DeviceListAdapter deviceListAdapter;
    ListView listView;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // try changing uuid if doesn't work with hc-05 arduino


    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // when device found
            if (action.equals(bluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, bluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(tag, "OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(tag, " TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(tag, "ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(tag, "TURNING ON");
                        break;
                }
            }
        }
    };

    // discoverability mode on/off
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(tag, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(tag, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(tag, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(tag, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(tag, "mBroadcastReceiver2: Connected.");
                        break;
                }
            }
        }
    };




   // list devices that are not paired
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(tag, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                bluetoothDevicesArrayList.add(device);
                Log.d(tag, "onReceive: " + device.getName() + ": " + device.getAddress());
                deviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, bluetoothDevicesArrayList);
                listView.setAdapter(deviceListAdapter);
            }
        }
    };


     // detect bond state changes (pair status change)
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(tag, "BroadcastReceiver: BOND_BONDED.");

                    bluetoothDevice = mDevice;
                }

                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(tag, "BroadcastReceiver: BOND_BONDING.");
                }

                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(tag, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };



    @Override
    protected void onDestroy() {
        Log.d(tag, "onDestroy: called.");
        super.onDestroy();

        // do we want this for cross-activity???
        try {
            unregisterReceiver(mBroadcastReceiver1);
            unregisterReceiver(mBroadcastReceiver2);
            unregisterReceiver(mBroadcastReceiver3);
            unregisterReceiver(mBroadcastReceiver4);
        }catch (Exception e){
            e.printStackTrace();
        }
        //mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Button switchButton = (Button) findViewById(R.id.switchButton);
        discoverableButton = (Button) findViewById(R.id.discoverableButton);
        listView = (ListView) findViewById(R.id.listView);
        bluetoothDevicesArrayList = new ArrayList<>();
        //startConnectionButton = (Button) findViewById(R.id.startConnectionButton);
        //sendButton = (Button) findViewById(R.id.sendButton);
        //editText = (EditText) findViewById(R.id.editText);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listView.setOnItemClickListener(BluetoothActivity.this);

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(tag, "onClick: enabling/disabling bluetooth.");
                enableDisableBT();
            }
        });

        /*startConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConnection();
            }
        });*/

        /*sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] bytes = editText.getText().toString().getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
            }
        });*/

    }

    // start connection
    public void startConnection(){
        startBTConnection(bluetoothDevice,MY_UUID_INSECURE);
    }


    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(tag, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        bluetoothConnection.startClient(device,uuid);
    }



    public void enableDisableBT(){
        if(bluetoothAdapter == null){
            Log.d(tag, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!bluetoothAdapter.isEnabled()){
            Log.d(tag, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(bluetoothAdapter.isEnabled()){
            Log.d(tag, "enableDisableBT: disabling BT.");
            bluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }

    }


    public void btnEnableDisable_Discoverable(View view) {
        Log.d(tag, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(bluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);

    }

    public void btnDiscover(View view) {
        Log.d(tag, "btnDiscover: Looking for unpaired devices.");

        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
            Log.d(tag, "btnDiscover: Canceling discovery.");

            checkBTPermissions();

            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!bluetoothAdapter.isDiscovering()){

            checkBTPermissions();

            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    // check permissions
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(tag, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        bluetoothAdapter.cancelDiscovery();

        Log.d(tag, "You Clicked on a device.");
        String deviceName = bluetoothDevicesArrayList.get(i).getName();
        String deviceAddress = bluetoothDevicesArrayList.get(i).getAddress();

        Log.d(tag, "Device name: " + deviceName);
        Log.d(tag, "Device address: " + deviceAddress);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(tag, "Trying to pair with " + deviceName);
            bluetoothDevicesArrayList.get(i).createBond();

            bluetoothDevice = bluetoothDevicesArrayList.get(i);
            bluetoothConnection = new BluetoothConnectionService(BluetoothActivity.this);
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}