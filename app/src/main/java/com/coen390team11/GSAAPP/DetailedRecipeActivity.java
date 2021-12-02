package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailedRecipeActivity extends AppCompatActivity {

    TextView recipeProductNameTextView, ingredientsTextView, instructionsTextView;
    private RequestQueue requestQueue;
    String tempDescription;
    String tempInstructions;
    String instructions2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_recipe);

        recipeProductNameTextView = findViewById(R.id.recipeProductNameTextView);
        ingredientsTextView = findViewById(R.id.ingredientsTextView);
        instructionsTextView = findViewById(R.id.instructionsTextView);
        instructionsTextView.setMovementMethod(new ScrollingMovementMethod());
        requestQueue = Volley.newRequestQueue(this);


        SharedPreferences sharedPreferences = getSharedPreferences("recipe_name", Context.MODE_PRIVATE);
        String recipe_name = sharedPreferences.getString("recipe_name","");
        recipeProductNameTextView.setText(recipe_name + "");

        jsonParse();

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Recipe Instructions");
    }

    private void jsonParse(){
        SharedPreferences sharedPreferences = getSharedPreferences("recipe_name", Context.MODE_PRIVATE);
        String query = sharedPreferences.getString("recipe_name","");
        String url = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + query;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("meals");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject meals = jsonArray.getJSONObject(i);

                        String ingredient1 = meals.getString("strIngredient1");
                        String ingredient2 = meals.getString("strIngredient2");
                        String ingredient3 = meals.getString("strIngredient3");
                        String ingredient4 = meals.getString("strIngredient4");
                        String ingredient5 = meals.getString("strIngredient5");
                        String ingredient6 = meals.getString("strIngredient6");
                        String ingredient7 = meals.getString("strIngredient7");
                        String ingredient8 = meals.getString("strIngredient8");
                        String ingredient9 = meals.getString("strIngredient9");
                        String ingredient10 = meals.getString("strIngredient10");
                        String ingredient11 = meals.getString("strIngredient11");
                        String ingredient12 = meals.getString("strIngredient12");
                        String ingredient13 = meals.getString("strIngredient13");
                        String ingredient14 = meals.getString("strIngredient14");
                        String ingredient15 = meals.getString("strIngredient15");
                        String ingredient16 = meals.getString("strIngredient16");

                        String ingredient1Measure = meals.getString("strMeasure1");
                        String ingredient2Measure = meals.getString("strMeasure2");
                        String ingredient3Measure = meals.getString("strMeasure3");
                        String ingredient4Measure = meals.getString("strMeasure4");
                        String ingredient5Measure = meals.getString("strMeasure5");
                        String ingredient6Measure = meals.getString("strMeasure6");
                        String ingredient7Measure = meals.getString("strMeasure7");
                        String ingredient8Measure = meals.getString("strMeasure8");
                        String ingredient9Measure = meals.getString("strMeasure9");
                        String ingredient10Measure = meals.getString("strMeasure10");
                        String ingredient11Measure = meals.getString("strMeasure11");
                        String ingredient12Measure = meals.getString("strMeasure12");
                        String ingredient13Measure = meals.getString("strMeasure13");
                        String ingredient14Measure = meals.getString("strMeasure14");
                        String ingredient15Measure = meals.getString("strMeasure15");
                        String ingredient16Measure = meals.getString("strMeasure16");

                        String instructions = meals.getString("strInstructions");
                        String[] trim = instructions.split("\r\n");
                        for (int k=0;k<trim.length;k++){
                            instructions2 += "\u2022 " + trim[i] + "\n";
                        }


                        if (ingredientsTextView.getText().toString().isEmpty()){
                            tempDescription =("\u2022 " + ingredient1Measure + " " + ingredient1 + "\n"
                            + "\u2022 " + ingredient2Measure + " " + ingredient2 + "\n"
                            + "\u2022 " + ingredient3Measure + " " + ingredient3 + "\n"
                            + "\u2022 " + ingredient4Measure + " " + ingredient4 + "\n"
                            + "\u2022 " + ingredient5Measure + " " + ingredient5 + "\n"
                            + "\u2022 " + ingredient6Measure + " " + ingredient6 + "\n"
                            + "\u2022 " + ingredient7Measure + " " + ingredient7 + "\n"
                            + "\u2022 " + ingredient8Measure + " " + ingredient8 + "\n"
                            + "\u2022 " + ingredient9Measure + " " + ingredient9 + "\n"
                            + "\u2022 " + ingredient10Measure + " " + ingredient10 + "\n");

                            if (!ingredient11Measure.isEmpty() && !ingredient11Measure.equals(null))
                            {
                                tempDescription += "\u2022 " + ingredient11Measure + " " + ingredient11 + "\n";
                            }
                            if (!ingredient12Measure.isEmpty() && !ingredient12Measure.equals(null))
                            {
                                tempDescription += "\u2022 " + ingredient12Measure + " " + ingredient12 + "\n";
                            }
                            if (!ingredient13Measure.isEmpty() && !ingredient13Measure.equals(null))
                            {
                                tempDescription += "\u2022 " + ingredient13Measure + " " + ingredient13 + "\n";
                            }
                            if (!ingredient14Measure.isEmpty() && !ingredient14Measure.equals(null))
                            {
                                tempDescription += "\u2022 " + ingredient14Measure + " " + ingredient14 + "\n";
                            }
                            if (!ingredient15Measure.isEmpty() && !ingredient15Measure.equals(null))
                            {
                                tempDescription += "\u2022 " + ingredient15Measure + " " + ingredient15 + "\n";
                            }
                            if (!ingredient16Measure.isEmpty() && !ingredient16Measure.equals(null))
                            {
                                tempDescription += "\u2022 " + ingredient16Measure + " " + ingredient16 + "\n";
                            }


                            ingredientsTextView.setText(tempDescription);
                        }

                        if (instructionsTextView.getText().toString().isEmpty()){
                            tempInstructions = instructions;
                            instructionsTextView.setText(tempInstructions);
                        }





                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

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