package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coen390team11.GSAAPP.ui.LogoutDialog;
import com.coen390team11.GSAAPP.ui.dashboard.BagFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ItemInformation extends AppCompatActivity {

    ImageView productImageView;
    TextView itemNameTextView;
    TextView itemDescriptionInformationTextView;
    ImageButton decreaseItemCountImageButton;
    TextView modifyQuantityTextView;
    ImageButton increaseItemCountImageButton;
    Button updateItemQuantityButton;
    int productCount;
    String tempDescription = "";
    TextView priceOfProductTextView;
    int dataSavingMode;
    int initialItemCount;
    int initialProductCount;
    ArrayList<String> trimToArrayList = new ArrayList<String>();
    ArrayList<String> trimToArrayListMAX = new ArrayList<String>();
    ArrayList<String> trimToArrayListMIN = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_information);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("product_name", Context.MODE_PRIVATE);
        String productName = sharedPreferences.getString("product_name","");
        String productCountString = productName.substring(0,1);
        productCount = Integer.parseInt(productCountString);



        setTitle("Product Information");

        productImageView = findViewById(R.id.productImageView);
        itemNameTextView = findViewById(R.id.itemNameTextView);
        itemDescriptionInformationTextView = findViewById(R.id.itemDescriptionInformationTextView);
        decreaseItemCountImageButton = findViewById(R.id.decreaseItemCountImageButton);
        modifyQuantityTextView = findViewById(R.id.modifyQuantityTextView);
        increaseItemCountImageButton = findViewById(R.id.increaseItemCountImageButton);
        updateItemQuantityButton = findViewById(R.id.updateItemQuantityButton);
        priceOfProductTextView = findViewById(R.id.priceOfProductTextView);

        itemNameTextView.setText(productName.substring(3) + "");
        modifyQuantityTextView.setText(productCount + "");
        initialItemCount=productCount;
        initialProductCount=initialItemCount;

        FirebaseFirestore.getInstance().collection("items")
                .document(productName.substring(3)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                // TODO: crashing if 1 item quantity is double digits, most likely from bag fragment switch trim[] and split to getting specific fields
                if ((value.get("price")) != null) {
                    String priceOfProductString = (value.get("price")).toString();
                    if (!(priceOfProductString.isEmpty())) {
                        Double priceOfProduct = Double.parseDouble(priceOfProductString);
                        priceOfProductTextView.setText("Price Per Unit: $" + priceOfProduct);
                    }
                }

            }
        });

        FirebaseFirestore.getInstance().collection("itemScanned")
                .document("itemBarcode").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String getCurrentBagString = (value.get("barcodeArray")).toString();
                //Toast.makeText(getApplicationContext(), getCurrentBagString, Toast.LENGTH_SHORT).show();

                String[] trim = getCurrentBagString.split(",");
                //ArrayList<String> trimToArrayList = new ArrayList<String>();
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
                //Toast.makeText(getApplicationContext(), String.valueOf(trimToArrayList), Toast.LENGTH_SHORT).show();
                TinyDB tinyDB = new TinyDB(getApplicationContext());
                tinyDB.putListString("currentBag",trimToArrayList);
            }
        });


        decreaseItemCountImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //countForItem = Integer.parseInt(modifyQuantityTextView.getText().toString());
                if (productCount-1 >= 1) {
                    modifyQuantityTextView.setText(--productCount + "");
                } else {
                    // do nothing, minimum quantity of 1
                }
            }
        });

        increaseItemCountImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //countForItem = Integer.parseInt(modifyQuantityTextView.getText().toString());

                modifyQuantityTextView.setText(++productCount + "");
            }
        });

        updateItemQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Integer.parseInt(modifyQuantityTextView.getText().toString()) > initialItemCount) {
                    max();
                } else if (Integer.parseInt(modifyQuantityTextView.getText().toString()) < initialItemCount){
                    min();
                }

                // when save button pressed go back to currentbag
                onBackPressed();
            }
        });

        FirebaseFirestore.getInstance().collection("descriptionPerItem").document(productName.substring(3))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult();
                    Log.i("DOCUMENT SNAPSHOT: ", String.valueOf(documentSnapshot));

                    try {
                        String dataToString = documentSnapshot.toString();

                        String[] trim = dataToString.split("\\{");
                        String descriptionSegment = trim[7];
                        String descriptionWithQuotation = descriptionSegment.substring(45);
                        String[] trimQuotation = descriptionWithQuotation.split("\"");
                        String description = trimQuotation[0];
                        Log.i("DESCRIPTION SEGMENT: ", description);

                        String[] BulletPoints = description.split("\\.");

                        for (int i=0;i<BulletPoints.length;i++) {
                            tempDescription+=("\u2022 " + BulletPoints[i] + "\n");
                        }

                        itemDescriptionInformationTextView.setText(tempDescription);



                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // getting dataSavingMode value
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String dataSavingModeString = (value.get("dataSavingMode")).toString();
                dataSavingMode = Integer.parseInt(dataSavingModeString);

                if (dataSavingMode == 0) {
                    // get images for products
                    FirebaseFirestore.getInstance().collection("items").document(productName.substring(3)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                try {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    String dataToString = documentSnapshot.toString();
                                    Log.i("PHOTO: ", String.valueOf(documentSnapshot));
                                    String[] trimFirstPart = dataToString.split("https");
                                    String[] trimSecondPart = dataToString.split("\\?alt");
                                    String finalFirstPart = trimFirstPart[1].substring(0, 73);
                                    String splitSecondPart = trimSecondPart[1];
                                    Log.i("splitFirstPart: ", finalFirstPart);
                                    Log.i("splitSecondPart: ", splitSecondPart);
                                    String[] finalSecond = splitSecondPart.split("\"");
                                    String finalSecondPart = finalSecond[0];
                                    Log.i("finalSecondPart: ", finalSecondPart);

                                    // trim product name, add %20 between each space
                                    String[] trimmedProductName = productName.substring(3).split(" ");
                                    // split by space

                                    // now get all product name parts
                                    String productNameParts = "";
                                    for (int i = 0; i < trimmedProductName.length; i++) {
                                        productNameParts += trimmedProductName[i];
                                        productNameParts += "%20";

                                        if (i + 1 == trimmedProductName.length) {
                                            Log.d("productNameParts: ", productNameParts);
                                        }
                                    }
                                    productNameParts = productNameParts.substring(0, productNameParts.length() - 3);
                                    productNameParts += ".jpeg?alt";
                                    Log.d("productNamePartsPERPART: ", productNameParts);
                                    // image name part done

                                    String totalURL = "https" + finalFirstPart + productNameParts + finalSecondPart;
                                    if (totalURL.contains("'")) { // remove extra ' from product names such as Lay's or French's
                                        totalURL = totalURL.replace("'", "");
                                    }
                                    Log.i("TOTALURL", totalURL);


                                    // split entire url into two parts, 1 before product name and 1 after product name
                                    // after part is .split("Image?alt") and substring(53???)
                                    // before part is is .split("https") and .substring(73???)

                                    Picasso.get().load(totalURL).into(productImageView);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }
        });



    }

    public void max(){
        // store quantity update on firebase
        int itemCount = Integer.parseInt(modifyQuantityTextView.getText().toString())-initialItemCount;
        String itemName = itemNameTextView.getText().toString();

        TinyDB tinyDB = new TinyDB(getApplicationContext());
        trimToArrayListMAX = tinyDB.getListString("currentBag");
        Toast.makeText(getApplicationContext(), String.valueOf(trimToArrayListMAX), Toast.LENGTH_SHORT).show();

        // get barcode for itemName and pass to bagfragment with sharedpreferences along with count
        FirebaseFirestore.getInstance().collection("items")
                .document(itemName).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String barcodeForItemName = (value.get("barcode")).toString();

                for (int i=0;i<itemCount;i++){
                    trimToArrayListMAX.add(barcodeForItemName);
                }

                Map<String, Object> plus = new HashMap<>();
                plus.put("barcodeArray", trimToArrayListMAX);

                FirebaseFirestore.getInstance()
                        .collection("itemScanned")
                        .document("itemBarcode")
                        .set(plus).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("Successfully increased this item's"," quantity.");
                    }
                });

                Log.i("barcodeForItemQuantityChange",barcodeForItemName);
                Log.i("countForItemQuantityChange",  initialItemCount + "---" + String.valueOf(itemCount));

            }
        });
    }

    public void min(){
        int itemCount = initialItemCount-Integer.parseInt(modifyQuantityTextView.getText().toString());
        String itemName = itemNameTextView.getText().toString();

        TinyDB tinyDB = new TinyDB(getApplicationContext());
        trimToArrayListMIN = tinyDB.getListString("currentBag");
        Toast.makeText(getApplicationContext(), String.valueOf(trimToArrayListMIN), Toast.LENGTH_SHORT).show();

        // get barcode for itemName and pass to bagfragment with sharedpreferences along with count
        FirebaseFirestore.getInstance().collection("items")
                .document(itemName).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String barcodeForItemName = (value.get("barcode")).toString();

                Log.i("itemCountMIN", String.valueOf(itemCount));

                for (int i=0;i<itemCount;i++){
                    trimToArrayListMIN.remove(barcodeForItemName);
                }

                Map<String, Object> minus = new HashMap<>();
                minus.put("barcodeArray", trimToArrayListMIN);

                FirebaseFirestore.getInstance()
                        .collection("itemScanned")
                        .document("itemBarcode")
                        .set(minus).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("Successfully decreased this item's"," quantity.");
                    }
                });


                /*SharedPreferences sharedPreferences = getSharedPreferences("barcodeForItemQuantityChangeMIN",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("barcodeForItemQuantityChangeMIN", barcodeForItemName);
                editor.putInt("countForItemQuantityChangeMIN",itemCount);
                editor.apply();*/

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_information,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}