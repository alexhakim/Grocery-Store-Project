package com.coen390team11.GSAAPP;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProblemWithProductDialog extends AppCompatDialogFragment {

    CheckBox ProblemWithImageOfProductCheckBox;
    CheckBox ProblemWithDescriptionOfProductCheckBox;
    int imageIssueCounter = 0;
    int descriptionIssueCounter = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.checkbox,null);

        ProblemWithImageOfProductCheckBox = view.findViewById(R.id.ProblemWithImageOfProductCheckBox);
        ProblemWithDescriptionOfProductCheckBox = view.findViewById(R.id.ProblemWithDescriptionOfProductCheckBox);

        builder.setView(view).setTitle("What seems to be the issue?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
            }
        }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Thanks for letting us know!", Toast.LENGTH_SHORT).show();


            }
        });

        ProblemWithDescriptionOfProductCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Log.i("CHECKED", String.valueOf(isChecked));
                    descriptionIssueCounter+=1;

                    // update value in firebase
                    FirebaseFirestore.getInstance().collection("reportIssue")
                            .document("issueWithProduct")
                            .update("descriptionDoesNotMatch",descriptionIssueCounter);

                } else {
                    //not checked
                    Log.i("CHECKED", String.valueOf(isChecked));
                }
            }
        });

        ProblemWithImageOfProductCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Log.i("CHECKED", String.valueOf(isChecked));
                    imageIssueCounter+=1;

                    // update value in firebase
                    FirebaseFirestore.getInstance().collection("reportIssue")
                            .document("issueWithProduct")
                            .update("imageDoesNotMatch",imageIssueCounter);
                } else {
                    //not checked
                    Log.i("CHECKED", String.valueOf(isChecked));
                }
            }
        });

        return builder.create();
    }


}
