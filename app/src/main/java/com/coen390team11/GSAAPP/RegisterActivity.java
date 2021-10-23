package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout firstNameEditText;
    TextInputLayout lastNameEditText;
    TextInputLayout emailEditText;
    TextInputLayout passwordEditText;
    TextInputLayout passwordConfirmEditText;
    Button registerButton;
    TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameEditText = (TextInputLayout) findViewById(R.id.firstNameEditText);
        lastNameEditText = (TextInputLayout) findViewById(R.id.lastNameEditText);
        emailEditText = (TextInputLayout) findViewById(R.id.emailEditText);
        passwordEditText = (TextInputLayout) findViewById(R.id.passwordEditText);
        passwordConfirmEditText = (TextInputLayout) findViewById(R.id.passwordConfirmEditText);
        registerButton = findViewById(R.id.registerButton);
        loginTextView = findViewById(R.id.loginTextView);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToLoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(switchToLoginIntent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // hide soft keyboard
                emailEditText.getEditText().onEditorAction(EditorInfo.IME_ACTION_DONE);
                passwordEditText.getEditText().onEditorAction(EditorInfo.IME_ACTION_DONE);

                // if the user did not input username/password
                if (firstNameEditText.getEditText().getText().toString().isEmpty()
                        || lastNameEditText.getEditText().getText().toString().isEmpty()
                        || emailEditText.getEditText().getText().toString().isEmpty()
                        || passwordEditText.getEditText().getText().toString().isEmpty()
                        || passwordConfirmEditText.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Missing fields.", Toast.LENGTH_LONG).show();
                } else if (!(passwordEditText.getEditText().getText().toString().equals(passwordConfirmEditText.getEditText().getText().toString()))) {

                    Toast.makeText(getApplicationContext(), "Password does not match.", Toast.LENGTH_SHORT).show();
                } else if (passwordEditText.getEditText().getText().toString().length() <= 6) {
                    Toast.makeText(getApplicationContext(), "Password length must be greater than 6.", Toast.LENGTH_SHORT).show();

                } else {

                    String email = emailEditText.getEditText().getText().toString();
                    String password = passwordEditText.getEditText().getText().toString();

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Success.", Toast.LENGTH_SHORT).show();

                                User user = new User();
                                user.email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                user.id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                user.firstName = firstNameEditText.getEditText().getText().toString();
                                user.lastName = lastNameEditText.getEditText().getText().toString();
                                user.rewardsCardNumber = generateRewardsNumber();

                                // store user in cloud
                                FirebaseFirestore.getInstance().collection("users").document(user.id)
                                        .set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                                /*FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                String currentUserID = "";
                                if (currentUser != null) {
                                    currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                }*/


                                Toast.makeText(getApplicationContext(), "Registration Successful.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }

    public Long generateRewardsNumber(){ // add to database to make unique
        Random randRewards = new Random();
        int upperBound = 999999999;
        int lowerBound = 100000000;
        int randomInt = randRewards.nextInt(upperBound-lowerBound) + lowerBound;
        Long randomNumber = Long.valueOf(randomInt);
        return randomNumber;

    }

    public final void getUserDetails(@NotNull final Activity activity) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = "";
        if (currentUser != null) {
            currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        FirebaseFirestore.getInstance().collection("users").document(currentUserID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                User useR = documentSnapshot.toObject(User.class);

                SharedPreferences sharedPreferences = activity.getSharedPreferences("app_preferences",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("logged_in_username", useR.firstName + " " + useR.lastName);
                editor.apply();

                if (activity instanceof LoginActivity) {
                    ((LoginActivity) activity).userLoggedInSuccess(useR);
                }


            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent switchToLoginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(switchToLoginIntent);
                //onBackPressed();
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}