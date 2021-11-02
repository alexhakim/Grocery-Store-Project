package com.coen390team11.GSAAPP;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

// SEND & RECEIVE DATA VIA BLUETOOTH FOR PHONE/SCANNER

public class BluetoothConnectionService {
    private static final String tag = "Connection";
    private static final String name = "Grocery Store Android Application";


    private static final UUID uuid =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // if doesn't work try changing UUID

    private final BluetoothAdapter bluetoothAdapter;
    Context contxt;
    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog progressDialog;
    private ConnectedThread connectedThread;

    public BluetoothConnectionService(Context context) {
        contxt = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }



    // thread runs while listening for incoming connections. Server-side client.
    // runs until a connection is accepted
    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            try{
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(name, uuid);

                Log.d(tag, "AcceptThread: Setting up Server using: " + uuid);
            }catch (IOException e){
                e.printStackTrace();
            }
            mmServerSocket = tmp;
        }

        public void run(){
            Log.d(tag, "run: AcceptThread Running.");

            BluetoothSocket socket = null;

            try{
                Log.d(tag, "run: RFCOM server socket start.....");
                socket = mmServerSocket.accept();
                Log.d(tag, "run: RFCOM server socket accepted connection.");

            }catch (IOException e){
                e.printStackTrace();
            }


            if(socket != null){
                connected(socket,mmDevice);
            }

            Log.i(tag, "END mAcceptThread ");
        }

        public void cancel() {
            Log.d(tag, "cancel: Canceling AcceptThread.");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // thread runs while attempting to make an outgoing connectio
    // with a device.
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(tag, "ConnectThread: started.");
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run(){
            BluetoothSocket tmp = null;
            Log.i(tag, "RUN mConnectThread ");

            try {
                Log.d(tag, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                        + uuid );
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmSocket = tmp;
            bluetoothAdapter.cancelDiscovery(); // avoid slowing

            try {

                mmSocket.connect();

                Log.d(tag, "run: ConnectThread connected.");
            } catch (IOException e) {

                try {
                    mmSocket.close();
                    Log.d(tag, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(tag, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(tag, "run: ConnectThread: Could not connect to UUID: " + uuid );
            }


            connected(mmSocket,mmDevice);
        }
        public void cancel() {
            try {
                Log.d(tag, "cancel: Closing Client Socket.");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(tag, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
            }
        }
    }



    // start
    public synchronized void start() {
        Log.d(tag, "start");

        // Cancel any thread attempting to make a connection
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
    }


    public void startClient(BluetoothDevice device,UUID uuid){
        Log.d(tag, "startClient: Started.");

        //initprogress dialog
        progressDialog = ProgressDialog.show(contxt,"Connecting Bluetooth"
                ,"Please Wait...",true);

        connectThread = new ConnectThread(device, uuid);
        connectThread.start();
    }

    // maintain bluetooth connection + send/receive
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(tag, "Starting");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try{
                progressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }


            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];  // buffer store for the stream

            int bytes; // bytes returned from read()

            while (true) {
                // Read from the InputStream
                try {
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(tag, "InputStream: " + incomingMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        //Call this from activity to send data to the remote device
        // TODO: unnecessary because we are not sending to scanner, only receiving
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(tag, "OutputStream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Call this from activity to shutdown bluetooth connection
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(tag, "connected: Starting.");
        connectedThread = new ConnectedThread(mmSocket);
        connectedThread.start();
    }

    /**
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temp object
        ConnectedThread r;
        // Synchronize copy of ConnectedThread
        Log.d(tag, "write: Write Called.");
        connectedThread.write(out);
    }
}