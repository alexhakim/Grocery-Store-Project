package com.coen390team11.GSAAPP;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    TextView bluetoothStatusTextView;
    Button turnBluetoothOnButton;
    Button turnBluetoothOffButton;
    Button discoverScannerButton;
    ListView displayDevicesFoundListView;
    ArrayList<String> barcodes = new ArrayList<String>();
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter bluetoothAdapter;
    ArrayAdapter<String> arrayAdapter;
    Handler handler;
    ConnectedThread connectedThread;
    BluetoothSocket bluetoothSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Connect Scanner");

        bluetoothStatusTextView = (TextView)findViewById(R.id.bluetoothStatusTextView);
        turnBluetoothOnButton = (Button)findViewById(R.id.turnBluetoothOnButton);
        turnBluetoothOffButton = (Button)findViewById(R.id.turnBluetoothOffButton);
        discoverScannerButton = (Button)findViewById(R.id.discoverScannerButton);
        displayDevicesFoundListView = findViewById(R.id.displayDevicesFoundListView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio
        displayDevicesFoundListView.setAdapter(arrayAdapter); // assign model to view
        displayDevicesFoundListView.setOnItemClickListener(listener);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        FirebaseFirestore.getInstance().collection("itemScanned")
                .document("itemBarcode")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        try {
                            String getBarcodes = (value.get("barcodeArray")).toString();

                            String[] trim = getBarcodes.split(",");
                            ArrayList<String> trimToArrayList = new ArrayList<String>();
                            // converting array to arraylist
                            Collections.addAll(trimToArrayList, trim);
                            for (int i = 0; i < trimToArrayList.size(); i++) {
                                if (trimToArrayList.get(i).contains("[")) {
                                    String temp = trimToArrayList.get(i);
                                    trimToArrayList.set(i, temp.substring(1));
                                }
                                if (trimToArrayList.get(i).contains("]")) {
                                    String temp = trimToArrayList.get(i);
                                    trimToArrayList.set(i, temp.substring(0, temp.length() - 1));
                                }
                                if (trimToArrayList.get(i).contains(" ")) {
                                    String temp = trimToArrayList.get(i);
                                    trimToArrayList.set(i, temp.substring(1));
                                }
                            }
                            Log.i("BARCODEBLUETOOTH", String.valueOf(trimToArrayList));
                            ArrayList<String> testingDoubleEntry = new ArrayList<String>();
                            if (!(testingDoubleEntry.contains(trimToArrayList))){
                                testingDoubleEntry.addAll(trimToArrayList);
                                Log.i("TESTINGDOUBLENETRY",String.valueOf(testingDoubleEntry));
                                PassArrayList passArrayList = new PassArrayList(getApplicationContext());
                                passArrayList.putArrayList("checkingDoubleEntry",testingDoubleEntry);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });


        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 2){
                    String getMsg = null;
                    try {
                        getMsg = new String((byte[]) msg.obj, "UTF-8");
                        Log.i("BARCODE",getMsg);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.i("READMESSAGE",getMsg.substring(0,10));
                    SharedPreferences sharedPreferences = getSharedPreferences("barcode_by_bluetooth",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("barcode_by_bluetooth", getMsg.substring(0,10));
                    editor.apply();


                    PassArrayList passArrayList = new PassArrayList(getApplicationContext());
                    ArrayList<String> testingDoubleEntry = new ArrayList<String>();
                    testingDoubleEntry = passArrayList.getArrayList("checkingDoubleEntry");
                    barcodes.clear();
                    for (int i=0;i<testingDoubleEntry.size();i++){
                        barcodes.add(testingDoubleEntry.get(i));
                        Log.i("PERENTRY",String.valueOf(barcodes));
                    }

                    barcodes.add(getMsg.substring(0,10));
                    Map<String, Object> data = new HashMap<>();
                    data.put("barcodeArray", barcodes);
                    FirebaseFirestore.getInstance().collection("itemScanned")
                            .document("itemBarcode")
                            .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("DocumentSnapshot successfully written!", "DocumentSnapshot successfully written!");
                        }
                    });
                }

                if(msg.what == 3){
                    if(msg.arg1 == 1)
                        bluetoothStatusTextView.setText("Connected to Device: " + msg.obj);
                    else
                        bluetoothStatusTextView.setText("Connection Failed");
                }
            }
        };


            turnBluetoothOnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ON();
                }
            });
            turnBluetoothOffButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    OFF();
                }
            });
            discoverScannerButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    PAIR();
                }
            });
    }

    private void ON(){
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
            bluetoothStatusTextView.setText("Bluetooth enabled");
            Toast.makeText(getApplicationContext(),"Bluetooth turned on",Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(getApplicationContext(),"Bluetooth is already on", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        super.onActivityResult(requestCode, resultCode, Data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                bluetoothStatusTextView.setText("Enabled");
            } else
                bluetoothStatusTextView.setText("Disabled");
        }
    }

    private void OFF(){
        bluetoothAdapter.disable();
        bluetoothStatusTextView.setText("Bluetooth disabled");
        Toast.makeText(getApplicationContext(),"Bluetooth is now OFF", Toast.LENGTH_SHORT).show();
    }

    private void PAIR(){
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(),"Stopped discovering devices.",Toast.LENGTH_SHORT).show();
        }
        else{
            if(bluetoothAdapter.isEnabled()) {
                arrayAdapter.clear(); // clear
                bluetoothAdapter.startDiscovery();
                Toast.makeText(getApplicationContext(), "Scanner discovery has started", Toast.LENGTH_SHORT).show();
                arrayAdapter.notifyDataSetChanged();
                registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
            else{
                Toast.makeText(getApplicationContext(), "Bluetooth is not ON", Toast.LENGTH_SHORT).show();
            }
        }
    }

    final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                arrayAdapter.add(device.getName() + "\n" + device.getAddress());
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(!bluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "Bluetooth not ON", Toast.LENGTH_SHORT).show();
                return;
            }

            bluetoothStatusTextView.setText("Connecting...");
            String info = ((TextView) view).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0,info.length() - 17);

            new Thread()
            {
                @Override
                public void run() {
                    boolean fail = false;
                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                    try {
                        bluetoothSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getApplicationContext(), "Error occured while creating socket.", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        bluetoothSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            bluetoothSocket.close();
                            handler.obtainMessage(3, -1, -1)
                                    .sendToTarget();
                        } catch (IOException E) {
                            E.printStackTrace();
                        }
                    }
                    if(!fail) {
                        connectedThread = new ConnectedThread(bluetoothSocket, handler);
                        connectedThread.start();
                        handler.obtainMessage(3, 1, -1, name).sendToTarget();
                    }
                }
            }.start();
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method method = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) method.invoke(device, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  device.createRfcommSocketToServiceRecord(uuid);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}