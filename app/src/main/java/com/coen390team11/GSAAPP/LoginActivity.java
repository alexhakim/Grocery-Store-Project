package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailEditText;
    TextInputLayout passwordEditText;
    Button loginButton;
    TextView registerTextView;
    TextView forgotPasswordTextView;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (TextInputLayout) findViewById(R.id.emailEditText);
        passwordEditText = (TextInputLayout) findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);


        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        setTitle("Cartly");


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
                    Snackbar.make(findViewById(android.R.id.content),"Missing Fields.",Snackbar.LENGTH_SHORT).show();
                } else {

                    progressDialog = ProgressDialog.show(LoginActivity.this,"Logging you in"
                            ,"Please Wait...",true);

                    String email = emailEditText.getEditText().getText().toString();
                    String password = passwordEditText.getEditText().getText().toString();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                progressDialog.dismiss();

                                // fetch user from cloud
                                RegisterActivity registerActivity = new RegisterActivity();
                                registerActivity.getUserDetails(LoginActivity.this);

                                /*Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();*/
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Snackbar.make(findViewById(android.R.id.content),"Please verify your credentials.",Snackbar.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPasswordIntent = new Intent(LoginActivity.this,forgotPasswordActivity.class);
                startActivity(forgotPasswordIntent);
                finish();
            }
        });
    }

    void userLoggedInSuccess(User userX){
        Log.i("First Name: ", userX.firstName);
        Log.i("Last Name: ", userX.lastName);
        Log.i("Email: ", userX.email);

        if (userX.profileCompleted == 0){
            // if user logs in for first time they will be prompted to enter their info
            Intent intent = new Intent(LoginActivity.this,UserProfileActivity.class);
            // parcelable to send object type
            intent.putExtra("extra_user_details", userX);
            startActivity(intent);
        } else {
            // redirect user to bluetoothconnect activity (if temporarily set to other activity)
            Intent intent2 = new Intent(LoginActivity.this,PrimaryActivity.class);
            startActivity(intent2);

            /*
            IF USER WANTS TO SEE HISTORY OR SETTINGS AND IS NOT AT THE STORE, IMPLEMENT FEATURE
            THAT TAKES THEM TO SEE HISTORY/SETTINGS BUT DISABLES ALL OTHER FEATURES REQUIRING PRESENCE
             */

        }

        /*Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);*/
        finish();
    }
}