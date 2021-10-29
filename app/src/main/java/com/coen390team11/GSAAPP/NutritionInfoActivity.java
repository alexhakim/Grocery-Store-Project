package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class NutritionInfoActivity extends AppCompatActivity {

    TextView productNameTextView;
    TextView servingSizeTextView;
    TextView amountPerServingTextView;
    TextView caloriesTextView;
    TextView totalFatTextView;
    TextView saturatedFatTextView;
    TextView cholesterolTextView;
    TextView sodiumTextView;
    TextView carbsTextView;
    TextView dietaryFiberTextView;
    TextView sugarsTextView;
    TextView proteinTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        saturatedFatTextView = findViewById(R.id.saturatedFatTextView);
        cholesterolTextView = findViewById(R.id.cholesterolTextView);
        sodiumTextView = findViewById(R.id.sodiumTextView);
        carbsTextView = findViewById(R.id.carbsTextView);
        dietaryFiberTextView = findViewById(R.id.dietaryFiberTextView);
        sugarsTextView = findViewById(R.id.sugarsTextView);
        proteinTextView = findViewById(R.id.proteinTextView);



        SharedPreferences sharedPreferences = getSharedPreferences("product_name", Context.MODE_PRIVATE);
        String productName = sharedPreferences.getString("product_name","");

        productNameTextView.setText("Product Name: " + productName.substring(2));
        servingSizeTextView.setText("Serving Size: 100g");

        OkHttpClient client = new OkHttpClient();
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