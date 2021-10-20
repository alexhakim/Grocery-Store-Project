package com.coen390team10.GSAAPP;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailEditText;
    TextInputLayout passwordEditText;
    Button loginButton;
    TextView registerTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (TextInputLayout) findViewById(R.id.emailEditText);
        passwordEditText = (TextInputLayout) findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);


        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToRegisterIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(switchToRegisterIntent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // hide soft keyboard
                emailEditText.getEditText().onEditorAction(EditorInfo.IME_ACTION_DONE);
                passwordEditText.getEditText().onEditorAction(EditorInfo.IME_ACTION_DONE);

                // if the user did not input username/password
                if (emailEditText.getEditText().getText().toString().isEmpty()
                        || passwordEditText.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please input a username/password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}