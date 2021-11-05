package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CheckoutActivity extends AppCompatActivity {

    EditText randomEditText;
    EditText randomEditText2;
    private ImageView barcode;
    private ImageView qrcode;
    Button completePurchaseButton;
    String currentBagString="";
    int counterForTimesAllowedToReadFireBaseMethod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        counterForTimesAllowedToReadFireBaseMethod=0;

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Checkout");

        //barcode = findViewById(R.id.barcode);
        qrcode = findViewById(R.id.qrcode);
        randomEditText = findViewById(R.id.randomEditText);
        randomEditText2 = findViewById(R.id.randomEditText2);
        completePurchaseButton = findViewById(R.id.completePurchaseButton);

        Intent intent = getIntent();
        String stringTotalPrice = intent.getStringExtra("total_price");
        randomEditText.setText("Subtotal: " + stringTotalPrice);
        randomEditText.setEnabled(false);

        Double totalDouble = Double.parseDouble(stringTotalPrice);
        Double totalWithTax = totalDouble*1.14975;
        randomEditText2.setText("Total: " + String.format("%.2f",totalWithTax));
        randomEditText2.setEnabled(false);

        // receive current bag from bag fragment and store in arraylist
        ArrayList<String> currentBagArrayList = (ArrayList<String>)getIntent().getSerializableExtra("current_bag");
        for (String s : currentBagArrayList){
            currentBagString += s + "\t";
        }
        Log.i("CURRENTBAGSTRING",currentBagString);

        //getBarcode();
        getQRCode();

        createDocumentInCollectionForUser();


        completePurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // receive current bag from bag fragment using intent parcelable extra, send data to history fragment
                // update purchaseCompleted field in firebase collection "pastShoppingEventsPerUser" to 1 for current user

                firstEvents();

                Intent goToAfterCheckoutIntent = new Intent(CheckoutActivity.this,CompletedEventActivity.class);
                startActivity(goToAfterCheckoutIntent);
                finish();
            }
        });

    }

    /*private void getBarcode(){
        try{
            setBarcode();
        } catch (Exception e){
            e.printStackTrace();
        }
    }*/

    private void getQRCode(){
        try{
            setQRCode();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    /*public void setBarcode() throws WriterException {

        BitMatrix bitMatrix = multiFormatWriter.encode(randomEditText.getText().toString().substring(7), BarcodeFormat.CODE_128,400,170,null);
        // first variable in .encode is what we want to receive, means the total price from
        // bag fragment, for now it is set to random edit text
        // second variable (BarcodeFormat.X), X is type of barcode (UPC, EAN8, EAN 13, CODE_128 etc)

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        barcode.setImageBitmap(bitmap);
        // set generated barcode (bitmap) to image view (barcode)
    }*/

    public void setQRCode() throws WriterException {

        BitMatrix bitMatrix = multiFormatWriter.encode(randomEditText.getText().toString().substring(7), BarcodeFormat.QR_CODE,350,300,null);
        // first variable in .encode is what we want to receive, means the total price from
        // bag fragment, for now it is set to random edit text
        // second variable (BarcodeFormat.X), X is type of barcode (UPC, EAN8, EAN 13, CODE_128 etc)

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        qrcode.setImageBitmap(bitmap);
        // set generated barcode (bitmap) to image view (barcode)
    }

    public void createDocumentInCollectionForUser(){

        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if (documentSnapshot.exists()){
                        // document already exists, do nothing
                    } else {
                        pastShoppingEventsPerUser pastShoppingEventsPerUser = new pastShoppingEventsPerUser();

                        // add document of name set to email of user to collection itemsPerUser
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(pastShoppingEventsPerUser, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                } else {
                    // task unsuccessful
                }
            }
        });
    }

    public void firstEvents(){
        // if purchaseCompleted0 = 0, set to 1 and set shoppingEvent0 to current bag
        // else if purchaseCompleted0 = 1, check purchaseCompleted1, if = 0, shoppingEvent1 takes
        // array values of shoppingEvent0 and shoppingEvent0 takes new current bag and so on
        // account for out of bounds index

        // 1. get purchaseCompleted0 value
        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String[] purchaseCompletedStringArray = new String[9];
                // getting purchaseCompleted values from firebase for shopping events
                for (int i=0;i<purchaseCompletedStringArray.length;i++){
                    purchaseCompletedStringArray[i]=(value.get("purchaseCompleted" + i)).toString();
                }
                int[] purchaseCompletedIntArray = new int[9];
                for (int i=0;i<purchaseCompletedIntArray.length;i++){
                    purchaseCompletedIntArray[i]=Integer.parseInt(purchaseCompletedStringArray[i]);
                }

                /* if purchaseCompleted[0] = 0, store currentbag in shoppingEvent0
                 ** and update purchaseCompleted0 to 1 */
                if (purchaseCompletedIntArray[0] == 0){
                    if (counterForTimesAllowedToReadFireBaseMethod < 1) {
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("shoppingEvent0", currentBagString);

                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("purchaseCompleted0", 1);

                        String timeStamp = (String) android.text.format.DateFormat.format("yyyy-MM-dd @ kk:mm:ss", new java.util.Date());
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("timeStamp0", timeStamp);


                        counterForTimesAllowedToReadFireBaseMethod += 1;
                    }
                } else if (purchaseCompletedIntArray[0] == 1){
                    // check if purchaseCompleted1 = 0
                    if (purchaseCompletedIntArray[1] == 0) {
                        if (counterForTimesAllowedToReadFireBaseMethod < 1) {
                            // store currentbag in shoppingEvent0 and set purchaseCompleted1 to 1

                            // getting past shoppingEvent0
                            String shoppingEvent0String = (value.get("shoppingEvent0")).toString();
                            Log.i("shoppingEvent0String", shoppingEvent0String);

                            String timeStamp0String = (value.get("timeStamp0")).toString();

                            FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("purchaseCompleted1", 1);

                            // waste time for firebase to correctly update shoppingEvent0String
                            try {
                                TimeUnit.MILLISECONDS.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // setting shoppingEvent1 to shoppingEvent0
                            FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("shoppingEvent1", shoppingEvent0String);

                            // setting shoppingEvent0 to current bag
                            FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("shoppingEvent0", currentBagString);

                            // setting timeStamp1 to timeStamp 0
                            FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("timeStamp1", timeStamp0String);

                            String timeStamp = (String) android.text.format.DateFormat.format("yyyy-MM-dd @ kk:mm:ss", new java.util.Date());
                            // setting timeStamp0 to new timeStamp of new shopping event
                            FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("timeStamp0", timeStamp);

                            counterForTimesAllowedToReadFireBaseMethod += 1;
                        }
                    } else if (purchaseCompletedIntArray[1] == 1){
                        if (purchaseCompletedIntArray[2] == 0){
                            if (counterForTimesAllowedToReadFireBaseMethod < 1) {
                                String shoppingEvent0String = (value.get("shoppingEvent0")).toString();
                                String shoppingEvent1String = (value.get("shoppingEvent1")).toString();
                                Log.i("shoppingEvent1String", shoppingEvent1String);

                                String timeStamp0String = (value.get("timeStamp0")).toString();
                                String timeStamp1String = (value.get("timeStamp1")).toString();


                                FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update("purchaseCompleted2", 1);

                                // waste time for firebase to correctly update shoppingEvent0String
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                // setting shoppingEvent2 to shoppingEvent1
                                FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update("shoppingEvent2", shoppingEvent1String);

                                // setting shoppingEvent1 to shoppingEvent0
                                FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update("shoppingEvent1", shoppingEvent0String);

                                // setting shoppingEvent0 to current bag
                                FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update("shoppingEvent0", currentBagString);

                                // setting timeStamp2 to timeStamp 1
                                FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update("timeStamp2", timeStamp1String);

                                // setting timeStamp1 to timeStamp 0
                                FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update("timeStamp1", timeStamp0String);

                                String timeStamp = (String) android.text.format.DateFormat.format("yyyy-MM-dd @ kk:mm:ss", new java.util.Date());
                                // setting timeStamp0 to new timeStamp of new shopping event
                                FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update("timeStamp0", timeStamp);

                                counterForTimesAllowedToReadFireBaseMethod += 1;
                            }
                        } else if (purchaseCompletedIntArray[2] == 1) {
                            if (purchaseCompletedIntArray[3] == 0){
                                    if (counterForTimesAllowedToReadFireBaseMethod < 1) {
                                        String shoppingEvent0String = (value.get("shoppingEvent0")).toString();
                                        String shoppingEvent1String = (value.get("shoppingEvent1")).toString();
                                        String shoppingEvent2String = (value.get("shoppingEvent2")).toString();

                                        String timeStamp0String = (value.get("timeStamp0")).toString();
                                        String timeStamp1String = (value.get("timeStamp1")).toString();
                                        String timeStamp2String = (value.get("timeStamp2")).toString();

                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("purchaseCompleted3", 1);

                                        // waste time for firebase to correctly update shoppingEvent0String
                                        try {
                                            TimeUnit.SECONDS.sleep(1);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        // setting shoppingEvent3 to shoppingEvent2
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("shoppingEvent3", shoppingEvent2String);

                                        // setting shoppingEvent2 to shoppingEvent1
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("shoppingEvent2", shoppingEvent1String);

                                        // setting shoppingEvent1 to shoppingEvent0
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("shoppingEvent1", shoppingEvent0String);

                                        // setting shoppingEvent0 to current bag
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("shoppingEvent0", currentBagString);

                                        // setting timeStamp3 to timeStamp 2
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("timeStamp3", timeStamp2String);

                                        // setting timeStamp2 to timeStamp 1
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("timeStamp2", timeStamp1String);

                                        // setting timeStamp1 to timeStamp 0
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("timeStamp1", timeStamp0String);

                                        String timeStamp = (String) android.text.format.DateFormat.format("yyyy-MM-dd @ kk:mm:ss", new java.util.Date());
                                        // setting timeStamp0 to new timeStamp of new shopping event
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("timeStamp0", timeStamp);

                                        counterForTimesAllowedToReadFireBaseMethod += 1;
                                    }
                            } else if (purchaseCompletedIntArray[3] == 1){
                                if (purchaseCompletedIntArray[4] == 0){
                                    if (counterForTimesAllowedToReadFireBaseMethod<1) {
                                        String shoppingEvent0String = (value.get("shoppingEvent0")).toString();
                                        String shoppingEvent1String = (value.get("shoppingEvent1")).toString();
                                        String shoppingEvent2String = (value.get("shoppingEvent2")).toString();
                                        String shoppingEvent3String = (value.get("shoppingEvent3")).toString();

                                        String timeStamp0String = (value.get("timeStamp0")).toString();
                                        String timeStamp1String = (value.get("timeStamp1")).toString();
                                        String timeStamp2String = (value.get("timeStamp2")).toString();
                                        String timeStamp3String = (value.get("timeStamp3")).toString();

                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("purchaseCompleted4", 1);

                                        // waste time for firebase to correctly update shoppingEvent0String
                                        try {
                                            TimeUnit.SECONDS.sleep(1);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        // setting shoppingEvent4 to shoppingEvent3
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("shoppingEvent4", shoppingEvent3String);

                                        // setting shoppingEvent3 to shoppingEvent2
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("shoppingEvent3", shoppingEvent2String);

                                        // setting shoppingEvent2 to shoppingEvent1
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("shoppingEvent2", shoppingEvent1String);

                                        // setting shoppingEvent1 to shoppingEvent0
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("shoppingEvent1", shoppingEvent0String);

                                        // setting shoppingEvent0 to current bag
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("shoppingEvent0", currentBagString);

                                        // setting timeStamp4 to timeStamp 3
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("timeStamp4", timeStamp3String);

                                        // setting timeStamp3 to timeStamp 2
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("timeStamp3", timeStamp2String);

                                        // setting timeStamp2 to timeStamp 1
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("timeStamp2", timeStamp1String);

                                        // setting timeStamp1 to timeStamp 0
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("timeStamp1", timeStamp0String);

                                        String timeStamp = (String) android.text.format.DateFormat.format("yyyy-MM-dd @ kk:mm:ss", new java.util.Date());
                                        // setting timeStamp0 to new timeStamp of new shopping event
                                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("timeStamp0", timeStamp);

                                        counterForTimesAllowedToReadFireBaseMethod+=1;
                                    }
                                }
                            }
                        }
                    }
                }
                // if all purchaseCompleted are 1, meaning client is frequent client
                if (purchaseCompletedIntArray[0] == 1 && purchaseCompletedIntArray[1] == 1&& purchaseCompletedIntArray[2] == 1
                    && purchaseCompletedIntArray[3] == 1 && purchaseCompletedIntArray[4] == 1){
                    if (counterForTimesAllowedToReadFireBaseMethod<1) {
                        String shoppingEvent0String = (value.get("shoppingEvent0")).toString();
                        String shoppingEvent1String = (value.get("shoppingEvent1")).toString();
                        String shoppingEvent2String = (value.get("shoppingEvent2")).toString();
                        String shoppingEvent3String = (value.get("shoppingEvent3")).toString();

                        String timeStamp0String = (value.get("timeStamp0")).toString();
                        String timeStamp1String = (value.get("timeStamp1")).toString();
                        String timeStamp2String = (value.get("timeStamp2")).toString();
                        String timeStamp3String = (value.get("timeStamp3")).toString();

                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("purchaseCompleted4", 1);

                        // waste time for firebase to correctly update shoppingEvent0String
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // setting shoppingEvent4 to shoppingEvent3
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("shoppingEvent4", shoppingEvent3String);

                        // setting shoppingEvent3 to shoppingEvent2
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("shoppingEvent3", shoppingEvent2String);

                        // setting shoppingEvent2 to shoppingEvent1
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("shoppingEvent2", shoppingEvent1String);

                        // setting shoppingEvent1 to shoppingEvent0
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("shoppingEvent1", shoppingEvent0String);

                        // setting shoppingEvent0 to current bag
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("shoppingEvent0", currentBagString);

                        // setting timeStamp4 to timeStamp 3
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("timeStamp4", timeStamp3String);

                        // setting timeStamp3 to timeStamp 2
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("timeStamp3", timeStamp2String);

                        // setting timeStamp2 to timeStamp 1
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("timeStamp2", timeStamp1String);

                        // setting timeStamp1 to timeStamp 0
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("timeStamp1", timeStamp0String);

                        String timeStamp = (String) android.text.format.DateFormat.format("yyyy-MM-dd @ kk:mm:ss", new java.util.Date());
                        // setting timeStamp0 to new timeStamp of new shopping event
                        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("timeStamp0", timeStamp);

                        counterForTimesAllowedToReadFireBaseMethod+=1;
                    }
                }
            }
        });
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