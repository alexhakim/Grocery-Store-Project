package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class RewardsActivity extends AppCompatActivity {

    TextView tierTextView;
    TextView pointsEarnedTextView;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        ArrayList<String> price = new ArrayList<String>();

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Cartly® Rewards");

        tierTextView = findViewById(R.id.tierTextView);
        pointsEarnedTextView = findViewById(R.id.pointsEarnedTextView);

        Log.i("CURRENTUSEREMAILINREWARDS",FirebaseAuth.getInstance().getCurrentUser().getEmail());

        FirebaseFirestore.getInstance().collection("pointsPerUser")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        try {
                            String pointsPerUser = (value.get("points")).toString();
                            Log.i("POINTSFORXUSER", pointsPerUser);
                            total += Double.parseDouble(pointsPerUser);
                            if (total < 500) {
                                tierTextView.setText("Average");
                                pointsEarnedTextView.setText(total + "");
                            } else if (total > 500 && total < 1000) {
                                tierTextView.setText("Enthusiast");
                                pointsEarnedTextView.setText(total * 2 + "");
                            } else if (total >= 1000) {
                                tierTextView.setText("Addict");
                                pointsEarnedTextView.setText(total * 3 + "");
                            }
                        }catch (Exception e){
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