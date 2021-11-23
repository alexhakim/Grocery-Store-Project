package com.coen390team11.GSAAPP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

public class ScannerDisconnectionDialog extends AppCompatDialogFragment {

    //static String productPrice;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_delete_request_dialog,null);




        builder.setView(view).setTitle("Scanner has unexpectedly disconnected.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    Intent goToBluetoothActivityIntent = new Intent(getContext(),BluetoothActivity.class);
                    startActivity(goToBluetoothActivityIntent);

            }
        }).setMessage("Please re-link scanner");






        return builder.create();
    }
}
