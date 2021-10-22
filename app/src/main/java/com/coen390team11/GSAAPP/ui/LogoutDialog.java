package com.coen390team11.GSAAPP.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.coen390team11.GSAAPP.LoginActivity;
import com.coen390team11.GSAAPP.PrimaryActivity;
import com.coen390team11.GSAAPP.R;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_logout_dialog,null);
        builder.setView(view).setTitle("Are you sure you want to log out?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();

                // we need to send the user back to login page when they log out
                Intent logoutIntent = new Intent(getContext(), LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                getActivity().finish();

            }
        });

        return builder.create();
    }
}
