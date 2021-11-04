package com.coen390team11.GSAAPP;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class DisplayPastShoppingEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_past_shopping_event);

        ArrayList<String> pastShoppingEvents = new ArrayList<String>();

        /* here we add the past shopping events received from firebase,
         ** which are received from the complete purchase button
         ** in the checkout activity to the arraylist*/

        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String shoppingEventX = (value.get("shoppingEvent0")).toString();

                pastShoppingEvents.add(shoppingEventX);

            }
        });
    }
}