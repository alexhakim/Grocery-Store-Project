package com.coen390team11.GSAAPP;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Handler handler;

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        bluetoothSocket = socket;
        this.handler = handler;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        inputStream = in;
        outputStream = out;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytesReceived;
        while (true) {
            try {
                // Read from the InputStream
                bytesReceived = inputStream.available();
                if(bytesReceived != 0) {
                    buffer = new byte[1024];
                    SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                    bytesReceived = inputStream.available(); // how many bytes are ready to be read?
                    bytesReceived = inputStream.read(buffer, 0, bytesReceived); // record how many bytes we actually read
                    handler.obtainMessage(2, bytesReceived, -1, buffer)
                            .sendToTarget(); // Send the obtained bytes to the UI activity
                }
            } catch (IOException e) {
                e.printStackTrace();

                break;
            }
        }
    }
}
