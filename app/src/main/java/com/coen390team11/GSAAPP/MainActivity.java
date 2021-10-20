package com.coen390team11.GSAAPP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView userIDTextView;
    TextView emailIDTextView;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userIDTextView = findViewById(R.id.userIDTextView);
        emailIDTextView = findViewById(R.id.emailIDTextView);
        logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent backToLoginIntent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(backToLoginIntent);
                finish();
            }
        });




    }
}