package com.coen390team10.GSAAPP;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView registerTextView;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Boolean loginMode = true;

    @Override
    public void onClick(View view){ // switch between sign in and sign up
        if (view.getId() == R.id.registerTextView){

            Button loginButton = (Button) findViewById(R.id.loginButton);

            if (loginMode){ // checking if in login mode
                loginMode = false;
                loginButton.setText("Sign Up");
                registerTextView.setText("Login");
            } else {
                loginMode = true;
                loginButton.setText("Login");
                registerTextView.setText("Sign Up");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerTextView = (TextView) findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(this); // switch

        ActionBar actionBar2 = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar2.setBackgroundDrawable(colorDrawable);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if the user did not input username/password
                if (usernameEditText.getText().toString().isEmpty()
                        || passwordEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please input a username and password.", Toast.LENGTH_SHORT).show();
                } else { // if we have both username and password

                }
            }
        });


    }
}