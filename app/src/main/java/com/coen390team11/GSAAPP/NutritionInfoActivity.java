package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Locale;

public class NutritionInfoActivity extends AppCompatActivity {

    TextView productNameTextView;
    TextView servingSizeTextView;
    TextView amountPerServingTextView;
    TextView caloriesTextView;
    TextView totalFatTextView;
    TextView cholesterolTextView;
    TextView sodiumTextView;
    TextView carbsTextView;
    TextView sugarsTextView;
    TextView proteinTextView;
    String productName;
    TextView priceTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadLocale();
        setContentView(R.layout.activity_nutrition_info);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Product Information");

        productNameTextView = findViewById(R.id.productNameTextView);
        servingSizeTextView = findViewById(R.id.servingSizeTextView);
        amountPerServingTextView = findViewById(R.id.amountPerServingTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        totalFatTextView = findViewById(R.id.totalFatTextView);
        cholesterolTextView = findViewById(R.id.cholesterolTextView);
        sodiumTextView = findViewById(R.id.sodiumTextView);
        carbsTextView = findViewById(R.id.carbsTextView);
        sugarsTextView = findViewById(R.id.sugarsTextView);
        proteinTextView = findViewById(R.id.proteinTextView);
        priceTextView = findViewById((R.id.priceTextView));

        Intent intent = getIntent();
        productName = intent.getStringExtra("product_name");

        productNameTextView.setText("Product Name: " + productName.substring(2));
        Log.d("PRODUCTNAME",productName.substring(2));

        FirebaseFirestore.getInstance().collection("nutrition").document(productName.substring(2))
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        try {
                            Log.d("CALORIES",(value.get("calories")).toString());
                            String calories = (value.get("calories")).toString();
                            String carbohydrates = (value.get("carbohydrates")).toString();
                            String cholesterol = (value.get("cholesterol")).toString();
                            String fat = (value.get("fat")).toString();
                            String perQuantity = (value.get("perQuantity")).toString();
                            String protein = (value.get("protein")).toString();
                            String sodium = (value.get("sodium")).toString();
                            String sugar = (value.get("sugar")).toString();

//                            String price = (value.get("price").toString());
//
//                            priceTextView.setText("Price: " + price);

                            servingSizeTextView.setText("Serving Size: Per " + perQuantity);
                            caloriesTextView.setText("Calories: " + calories);
                            totalFatTextView.setText("Total Fat: " + fat);
                            cholesterolTextView.setText("Cholesterol: " + cholesterol);
                            sodiumTextView.setText("Sodium: " + sodium);
                            carbsTextView.setText("Total Carbohydrate: " + carbohydrates);
                            sugarsTextView.setText("Sugars: " + sugar);
                            proteinTextView.setText("Protein: " + protein);
                        }catch (Exception e){
                            e.printStackTrace();
                            productName = productName.substring(2);
                            if (productName.startsWith(" ")){
                                productName = productName.substring(1);
                            }
                            FirebaseFirestore.getInstance().collection("nutrition").document(productName)
                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            try{
                                                Log.d("CALORIESCATCH",(value.get("calories")).toString());
                                                String calories = (value.get("calories")).toString();
                                                String carbohydrates = (value.get("carbohydrates")).toString();
                                                String cholesterol = (value.get("cholesterol")).toString();
                                                String fat = (value.get("fat")).toString();
                                                String perQuantity = (value.get("perQuantity")).toString();
                                                String protein = (value.get("protein")).toString();
                                                String sodium = (value.get("sodium")).toString();
                                                String sugar = (value.get("sugar")).toString();

                                                servingSizeTextView.setText("Serving Size: Per " + perQuantity);
                                                caloriesTextView.setText("Calories: " + calories);
                                                totalFatTextView.setText("Total Fat: " + fat);
                                                cholesterolTextView.setText("Cholesterol: " + cholesterol);
                                                sodiumTextView.setText("Sodium: " + sodium);
                                                carbsTextView.setText("Total Carbohydrate: " + carbohydrates);
                                                sugarsTextView.setText("Sugars: " + sugar);
                                                proteinTextView.setText("Protein: " + protein);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    }
                });


        FirebaseFirestore.getInstance().collection("items").document(productName.substring(3))
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        try {
                            //Log.d("CALORIES",(value.get("calories")).toString());
                            String price = (value.get("price").toString());

                            priceTextView.setText("Price: $" + price);
                        }catch (Exception e){
                            e.printStackTrace();
                            productName = productName.substring(2);
                            if (productName.startsWith(" ")){
                                productName = productName.substring(1);
                            }
                            FirebaseFirestore.getInstance().collection("items").document(productName)
                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            try{
                                                Log.d("CALORIESCATCH",(value.get("calories")).toString());
                                                String price = (value.get("price").toString());

                                                priceTextView.setText("Price: $" + price);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    }
                });


       /* OkHttpClient client = new OkHttpClient();
        String query = productName.substring(2);
        String url = "https://api.calorieninjas.com/v1/nutrition?query= " + query;

        Request request = new Request.Builder().url(url).addHeader("X-Api-Key","vCo0UQqUblTUHhxpb1s+cw==CTYmZFM7ID8GhLSN").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {

                if (response.isSuccessful()) {
                        String result = response.body().string();
                        String trim[] = result.split(",");


                        String getCaloriesSegment = trim[8];
                        String getCaloriesAmount = getCaloriesSegment.substring(12);

                        String getTotalFatSegment = trim[7];
                        String getTotalFatAmount = getTotalFatSegment.substring(15);

                        String getSaturatedFatSegment = trim[6];
                        String getSaturatedFatAmount = getSaturatedFatSegment.substring(19);

                        String getCholesterolSegment = trim[9];
                        String getCholesterolAmount = getCholesterolSegment.substring(19);

                        String getSodiumSegment = trim[3];
                        String getSodiumAmount = getSodiumSegment.substring(14);

                        String getCarbsSegment = trim[11];
                        String getCarbsAmount = getCarbsSegment.substring(25,getCarbsSegment.length()-3);

                        String getDietaryFiberSegment = trim[1];
                        String getDietaryFiberAmount = getDietaryFiberSegment.substring(12);

                        String getSugarsSegment = trim[0];
                        String getSugarsAmount = getSugarsSegment.substring(23, getSugarsSegment.length());

                        String getProteinSegment = trim[10];
                        String getProteinAmount = getProteinSegment.substring(13);


                    // calling getActivity because we are currently in background thread
                    // not doing this would cause fatal exception
                    NutritionInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            caloriesTextView.setText("Calories: " + getCaloriesAmount);
                            totalFatTextView.setText("Total Fat: " + getTotalFatAmount + "g");
                            saturatedFatTextView.setText("Saturated Fat: " + getSaturatedFatAmount + "g");
                            cholesterolTextView.setText("Cholesterol: " + getCholesterolAmount + "mg");
                            sodiumTextView.setText("Sodium: " + getSodiumAmount + "mg");
                            carbsTextView.setText("Total Carbohydrate: " + getCarbsAmount + "g");
                            dietaryFiberTextView.setText("Dietary Fiber: " + getDietaryFiberAmount + "g");
                            sugarsTextView.setText("Sugars: " + getSugarsAmount + "g");
                            proteinTextView.setText("Protein: " + getProteinAmount + "g");

                        }

                    });
                }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });*/

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

    /*public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        // save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

    }

    // load language from shared preferences
    public void loadLocale(){
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = sharedPreferences.getString("My_Lang","");
        Log.i("LANGUAGELOADLOCALE",language);
        setLocale(language);
    }*/
}