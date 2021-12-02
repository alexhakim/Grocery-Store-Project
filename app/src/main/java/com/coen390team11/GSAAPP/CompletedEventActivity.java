package com.coen390team11.GSAAPP;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coen390team11.GSAAPP.ui.LogoutDialog;
import com.coen390team11.GSAAPP.ui.home.HistoryFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collections;

public class CompletedEventActivity extends AppCompatActivity {

    ListView pastShoppingEventXItemsListView;
    TextView subtotalPriceOfShoppingEventTextView;
    TextView GSTTextView;
    TextView QSTTextView;
    TextView totalPriceOfShoppingEventTextView;
    /*TextView totalCaloriesTextView;
    TextView totalFatTextView;
    TextView totalCholesterolTextView;
    TextView totalSodiumTextView;
    TextView totalCarbohydratesTextView;
    TextView totalProteinTextView;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_event);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        setTitle("Receipt");

        pastShoppingEventXItemsListView = findViewById(R.id.pastShoppingEventXItemsListView);
        subtotalPriceOfShoppingEventTextView = findViewById(R.id.subtotalPriceOfShoppingEventTextView);
        GSTTextView = findViewById(R.id.GSTTextView);
        QSTTextView = findViewById(R.id.QSTTextView);
        totalPriceOfShoppingEventTextView = findViewById(R.id.totalPriceOfShoppingEventTextView);
        /*totalCaloriesTextView = findViewById(R.id.totalCaloriesTextView);
        totalFatTextView = findViewById(R.id.totalFatTextView);
        totalCholesterolTextView = findViewById(R.id.totalCholesterolTextView);
        totalSodiumTextView = findViewById(R.id.totalSodiumTextView);
        totalCarbohydratesTextView = findViewById(R.id.totalCarbohydratesTextView);
        totalProteinTextView = findViewById(R.id.totalProteinTextView);*/

        ArrayList<String> pastShoppingEventsArrayList = new ArrayList<String>();

        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String shoppingEvent0 = (value.get("shoppingEvent0")).toString();

                // now split it by \t to add items seperately in arraylist
                String[] trimShoppingEvent0 = shoppingEvent0.split("\t");
                for (int i=0;i< trimShoppingEvent0.length;i++){
                    pastShoppingEventsArrayList.add(trimShoppingEvent0[i]);
                    Log.d("trimShoppingEventX", trimShoppingEvent0[i]);
                    Log.d("pastShoppingEvents", String.valueOf(pastShoppingEventsArrayList));
                }

                Collections.sort(pastShoppingEventsArrayList);
                ArrayAdapter arrayAdapter = new ArrayAdapter(CompletedEventActivity.this, android.R.layout.simple_list_item_1, pastShoppingEventsArrayList);
                pastShoppingEventXItemsListView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("product_subtotal", Context.MODE_PRIVATE);
        String subtotal = sharedPreferences.getString("product_subtotal","");
        String gst = String.format("%.2f",Double.parseDouble(subtotal)*0.05);
        String qst = String.format("%.2f",Double.parseDouble(subtotal)*0.09975);
        subtotalPriceOfShoppingEventTextView.setText("Subtotal: $" + subtotal);
        GSTTextView.setText("Estimated GST: $" + gst);
        QSTTextView.setText("Estimated QST: $" + qst);
        Double totalDouble = Double.parseDouble(subtotal) + Double.parseDouble(gst) + Double.parseDouble(qst);
        String total = String.format("%.2f",totalDouble);
        totalPriceOfShoppingEventTextView.setText("Total Price: $" + total);

        // for rewards
        FirebaseFirestore.getInstance().collection("pointsPerUser")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .update("points", FieldValue.increment(Double.parseDouble(subtotal)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_after_checkout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            /*case R.id.LogoutB:
                //LogoutDialog logoutDialog = new LogoutDialog();
                //logoutDialog.show(getSupportFragmentManager(),"Logout");
                /*Intent goToLoginActivityIntent = new Intent(CompletedEventActivity.this,SplashActivity.class);
                goToLoginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToLoginActivityIntent);
                this.finish();*/
            case R.id.goToPastEvents:
                LogoutDialog logoutDialog = new LogoutDialog();
                logoutDialog.show(getSupportFragmentManager(),"Logout");
                /*Intent goToPrimaryActivityIntent = new Intent(CompletedEventActivity.this,LoginActivity.class);
                startActivity(goToPrimaryActivityIntent);
                this.finish();*/
            /*case R.id.LGX:
                Intent goToPrimaryActivityIntent = new Intent(CompletedEventActivity.this,LoginActivity.class);
                startActivity(goToPrimaryActivityIntent);
                this.finish();*/
        }
        return super.onOptionsItemSelected(item);
    }

}