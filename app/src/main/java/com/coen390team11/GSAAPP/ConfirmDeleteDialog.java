package com.coen390team11.GSAAPP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfirmDeleteDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_delete_request_dialog,null);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("product_name", Context.MODE_PRIVATE);
        String productNameBasedOnIndex = sharedPreferences.getString("product_name", "");
        Log.i("productNameBasedOnIndex",productNameBasedOnIndex);




        builder.setView(view).setTitle("Remove item(s) from bag").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
            }
        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // 1. get current bag from firebase [DONE]
                // 2. convert received string from firebase to arraylist [DONE]
                // 3. remove item based on item index [DONE]
                // 4. send updated current bag to firebase [DONE]

                //updatedDeletedNoDuplicatesFirebase();
                SharedPreferences sp = getContext().getSharedPreferences("delete_item",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("delete_item", productNameBasedOnIndex);
                editor.commit();
                //Toast.makeText(getContext(), productNameBasedOnIndex, Toast.LENGTH_SHORT).show();

                FirebaseFirestore.getInstance().collection("deleteItem")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .update("itemDeleted",productNameBasedOnIndex);


            }
        });



        return builder.create();
    }

    public void updatedDeletedNoDuplicatesFirebase(){
        // getting currentbag from firebase
        FirebaseFirestore.getInstance().collection("tempBag")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String tempCurrentBag = value.get("tempCurrentBag").toString();

                        // converting string to arraylist
                        if (!(tempCurrentBag.isEmpty())){
                            String[] trim = tempCurrentBag.split(",");
                            ArrayList<String> fromFirebaseArrayList = new ArrayList<String>();
                            for (int i=0;i<trim.length;i++){
                                Log.d("TRIM",trim[i]);
                                fromFirebaseArrayList.add(trim[i]);
                                Log.d("FROMFIREBASEARRAYLIST", String.valueOf(fromFirebaseArrayList));
                            }

                            // copy barcode arraylist into noDuplicates arraylist but without duplicates
                            Log.d("FROMFIREBASEARRAYLISTOUTER", String.valueOf(fromFirebaseArrayList));

                            // remove item based on item index in listview
                            try {
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("product_name_confirm", Context.MODE_PRIVATE);
                                String productNameBasedOnIndex = sharedPreferences.getString("product_name_confirm", "");

                                productNameBasedOnIndex = productNameBasedOnIndex.substring(2);
                                if (productNameBasedOnIndex.startsWith(" ")) {
                                    productNameBasedOnIndex = productNameBasedOnIndex.substring(1);
                                }


                                // get barcode based on name received from shared preferences
                                FirebaseFirestore.getInstance().collection("items")
                                        .document(productNameBasedOnIndex)
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                Log.d("BARCODEBASEDONNAME",value.get("barcode").toString());
                                                String barcodeBasedOnName = value.get("barcode").toString();
                                                for (int i=0;i< fromFirebaseArrayList.size();i++){
                                                    // if barcode matches then remove from arraylist
                                                    if (barcodeBasedOnName.equals(fromFirebaseArrayList.get(i))){
                                                        fromFirebaseArrayList.remove(i);
                                                    }
                                                }

                                                // converting fromFirebaseArrayList to string
                                                String bagToString = TextUtils.join(",",fromFirebaseArrayList);


                                                // write new updated bag to firebase
                                                FirebaseFirestore.getInstance().collection("tempBag")
                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .update("tempCurrentBag",bagToString);
                                            }

                                        });

                                Log.d("productNameBasedOnIndex", productNameBasedOnIndex);


                            } catch(Exception e){
                                e.printStackTrace();
                            }


                        }
                    }
                });
    }


}
