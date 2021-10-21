package com.coen390team11.GSAAPP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Toast.makeText(getApplicationContext(), "Succesfully logged in.", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        String user_name = sharedPreferences.getString("logged_in_username","");
        textView.setText("Welcome " + user_name);





    }
}