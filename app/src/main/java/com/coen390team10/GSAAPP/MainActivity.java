package com.coen390team10.GSAAPP;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    /*TextView registerTextView;
    TextInputLayout usernameEditText;
    TextInputLayout passwordEditText;
    Button loginButton;
    Boolean loginMode = true;
    private DBHelper sqlLiteDatabase;*/

    /*@Override
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
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*usernameEditText = (TextInputLayout) findViewById(R.id.usernameEditText);
        passwordEditText = (TextInputLayout) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerTextView = (TextView) findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(this); // switch
        sqlLiteDatabase = new DBHelper(this);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // hide soft keyboard after input + click
                usernameEditText.getEditText().onEditorAction(EditorInfo.IME_ACTION_DONE);
                passwordEditText.getEditText().onEditorAction(EditorInfo.IME_ACTION_DONE);

                // if the user did not input username/password
                if (usernameEditText.getEditText().getText().toString().isEmpty()
                        || passwordEditText.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please input a username/password.", Toast.LENGTH_SHORT).show();
                } else { // if we have both username and password

                    if (loginButton.getText() == "Sign Up") {
                        boolean checkIfUserExists = sqlLiteDatabase.isUsernameExists(usernameEditText.getEditText().getText().toString());
                        boolean var = sqlLiteDatabase.registerUser(usernameEditText.getEditText().getText().toString(), passwordEditText.getEditText().getText().toString());
                        if (checkIfUserExists){
                            Toast.makeText(MainActivity.this,"Error. User already exists. Please login instead.", Toast.LENGTH_LONG).show();
                        }
                        else if (var) {
                            Toast.makeText(MainActivity.this, "Success.", Toast.LENGTH_SHORT).show();
                            Intent afterLR = new Intent(MainActivity.this, AfterLogin.class);
                            startActivity(afterLR);
                        } else {
                            Toast.makeText(MainActivity.this, "An error has occurred.", Toast.LENGTH_LONG).show();
                        }
                    } else if (loginButton.getText() == "Login") {
                        boolean var = sqlLiteDatabase.checkUser(usernameEditText.getEditText().getText().toString(), passwordEditText.getEditText().getText().toString());
                        if (sqlLiteDatabase.checkUser(usernameEditText.getEditText().getText().toString(), passwordEditText.getEditText().getText().toString())) {
                            Toast.makeText(MainActivity.this, "Success.", Toast.LENGTH_SHORT).show();
                            Intent afterLR = new Intent(MainActivity.this, AfterLogin.class);
                            startActivity(afterLR);
                        } else {
                            Toast.makeText(MainActivity.this, "An error has occurred. Please verify your credentials.", Toast.LENGTH_LONG).show();
                        }
                    }
                }


            }
        });


    }*/
    }
}