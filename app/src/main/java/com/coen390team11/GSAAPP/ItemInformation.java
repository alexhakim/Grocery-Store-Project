package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ItemInformation extends AppCompatActivity {

    ImageView productImageView;
    TextView itemNameTextView;
    TextView itemDescriptionInformationTextView;
    ImageButton decreaseItemCountImageButton;
    TextView modifyQuantityTextView;
    ImageButton increaseItemCountImageButton;
    Button updateItemQuantityButton;
    int countForItem = 1;
    String tempDescription = "";

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
        setTitle("Product Information");

        productImageView = findViewById(R.id.productImageView);
        itemNameTextView = findViewById(R.id.itemNameTextView);
        itemDescriptionInformationTextView = findViewById(R.id.itemDescriptionInformationTextView);
        decreaseItemCountImageButton = findViewById(R.id.decreaseItemCountImageButton);
        modifyQuantityTextView = findViewById(R.id.modifyQuantityTextView);
        increaseItemCountImageButton = findViewById(R.id.increaseItemCountImageButton);
        updateItemQuantityButton = findViewById(R.id.updateItemQuantityButton);

        itemNameTextView.setText(productName.substring(3) + "");
        modifyQuantityTextView.setText(countForItem + "");

        decreaseItemCountImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //countForItem = Integer.parseInt(modifyQuantityTextView.getText().toString());
                if (countForItem-1 >= 1) {
                    modifyQuantityTextView.setText(--countForItem + "");
                } else {
                    // cannot decrement
                }
            }
        });

        increaseItemCountImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //countForItem = Integer.parseInt(modifyQuantityTextView.getText().toString());
                modifyQuantityTextView.setText(++countForItem + "");
            }
        });

        updateItemQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store quantity update on firebase
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

        FirebaseFirestore.getInstance().collection("items").document(productName.substring(3)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

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
                        if (totalURL.contains("'")){ // remove extra ' from product names such as Lay's or French's
                            totalURL = totalURL.replace("'","");
                        }
                        Log.i("TOTALURL", totalURL);


                        // split entire url into two parts, 1 before product name and 1 after product name
                        // after part is .split("Image?alt") and substring(53???)
                        // before part is is .split("https") and .substring(73???)

                        Picasso.get().load(totalURL).into(productImageView);
                    }catch (Exception e){
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: // if user presses on back button go back to bag fragment
                onBackPressed();
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}