package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class DisplayPastShoppingEventActivity extends AppCompatActivity {

    ListView pastShoppingEventXItemsListView;
    TextView subtotalPriceOfShoppingEventTextView;
    TextView GSTTextView;
    TextView QSTTextView;
    TextView totalPriceOfShoppingEventTextView;
    TextView totalCaloriesTextView;
    TextView totalFatTextView;
    TextView totalCholesterolTextView;
    TextView totalSodiumTextView;
    TextView totalCarbohydratesTextView;
    TextView totalProteinTextView;
    int totalCalories=0;
    int p=0;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadLocale();
        setContentView(R.layout.activity_display_past_shopping_event);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Past Shopping Event");

        pastShoppingEventXItemsListView = findViewById(R.id.pastShoppingEventXItemsListView);
        subtotalPriceOfShoppingEventTextView = findViewById(R.id.subtotalPriceOfShoppingEventTextView);
        GSTTextView = findViewById(R.id.GSTTextView);
        QSTTextView = findViewById(R.id.QSTTextView);
        totalPriceOfShoppingEventTextView = findViewById(R.id.totalPriceOfShoppingEventTextView);
        totalCaloriesTextView = findViewById(R.id.totalCaloriesTextView);
        totalFatTextView = findViewById(R.id.totalFatTextView);
        totalCholesterolTextView = findViewById(R.id.totalCholesterolTextView);
        totalSodiumTextView = findViewById(R.id.totalSodiumTextView);
        totalCarbohydratesTextView = findViewById(R.id.totalCarbohydratesTextView);
        totalProteinTextView = findViewById(R.id.totalProteinTextView);

        //SharedPreferences sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        //String language = sharedPreferences.getString("My_Lang","");

        /*Log.i("LANGUAGEDISPLAYPASTSHOPPINGEVENTACTIVITY",language);
        if (language == "fr") {
            GSTTextView.setText("Estimation du GST");
            QSTTextView.setText("Estimation du QST");
            setTitle("Événement Complété Auparavant");
        }*/

        ArrayList<String> pastShoppingEventsArrayList = new ArrayList<String>();

        // pass list view position from history fragment using sharedpreferences, then get shoppingEvent(X)
        Intent intent = getIntent();
        int positionInTimeStampListView = intent.getIntExtra("position",0);

        /* here we add the past shopping events received from firebase,
         ** which are received from the complete purchase button
         ** in the checkout activity to the arraylist*/

        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String shoppingEventX = (value.get("shoppingEvent" + positionInTimeStampListView)).toString();


                // now split it by \t
                String[] trimShoppingEventX = shoppingEventX.split("\t");
                for (int i=0;i< trimShoppingEventX.length;i++){
                    pastShoppingEventsArrayList.add(trimShoppingEventX[i]);
                    Log.d("trimShoppingEventX", trimShoppingEventX[i]);
                    Log.d("pastShoppingEvents", String.valueOf(pastShoppingEventsArrayList));
                }

                Collections.sort(pastShoppingEventsArrayList);
                ArrayAdapter arrayAdapter = new ArrayAdapter(DisplayPastShoppingEventActivity.this, android.R.layout.simple_list_item_1, pastShoppingEventsArrayList);
                pastShoppingEventXItemsListView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

            }
        });

        pastShoppingEventXItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent goToNutritionInfoIntent = new Intent(DisplayPastShoppingEventActivity.this, NutritionInfoActivity.class);
                goToNutritionInfoIntent.putExtra("product_name",pastShoppingEventsArrayList.get(position));
                startActivity(goToNutritionInfoIntent);
            }
        });

        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                try {

                    Intent intent = getIntent();
                    int positionInTimeStampListView = intent.getIntExtra("position",0);

                        if ((value.get("zSubTotal" + positionInTimeStampListView)) != null){
                            String zSubTotalXString = (value.get("zSubTotal" + positionInTimeStampListView)).toString();
                            if (!(zSubTotalXString.isEmpty())){
                                String gst = String.format("%.2f",Double.parseDouble(zSubTotalXString)*0.05);
                                String qst = String.format("%.2f",Double.parseDouble(zSubTotalXString)*0.09975);
                                subtotalPriceOfShoppingEventTextView.setText("Subtotal: $" + zSubTotalXString);
                                GSTTextView.setText("Estimated GST: $" + gst);
                                QSTTextView.setText("Estimated QST: $" + qst);
                                try {
                                    Double totalDouble = Double.parseDouble(zSubTotalXString) + Double.parseDouble(gst) + Double.parseDouble(qst);
                                    String total = String.format("%.2f", totalDouble);
                                    totalPriceOfShoppingEventTextView.setText("Total Price: $" + total);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                } catch (NullPointerException e){
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
        language = sharedPreferences.getString("My_Lang","");
        Log.i("LANGUAGELOADLOCALE2",language);
        setLocale(language);
    }*/
}