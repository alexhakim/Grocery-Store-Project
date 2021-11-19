package com.coen390team11.GSAAPP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ChangeProductPriceDialog extends AppCompatDialogFragment {

    //static String productPrice;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_delete_request_dialog,null);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("product_name_employee", Context.MODE_PRIVATE);
        String productName = sharedPreferences.getString("product_name_employee","");
        String productPrice = sharedPreferences.getString("product_price_employee","");
        Log.i("PRODUCTPRICEEMPLOYEE2",productPrice);


        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_DECIMAL);


        builder.setView(input).setTitle(productName).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
            }
        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



                FirebaseFirestore.getInstance().collection("items")
                        .document(productName)
                        .update("price", input.getText().toString());
                Toast.makeText(getContext(), "Succesfully updated product price.", Toast.LENGTH_SHORT).show();
                Intent restartActivityWithUpdates = new Intent(getContext(),UpdatePricesActivity.class);
                startActivity(restartActivityWithUpdates);
                getActivity().finish();

            }
        }).setMessage("Current Price: " + productPrice + "\n\nENTER NEW PRICE: ");






        return builder.create();
    }
}
