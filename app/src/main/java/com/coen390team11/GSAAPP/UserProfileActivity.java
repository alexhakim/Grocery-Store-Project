package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    User userDetails;

    TextInputLayout firstNameEditText;
    TextInputLayout lastNameEditText;
    TextInputLayout emailEditText;
    TextInputLayout phoneNumberEditText;
    Spinner genderSpinner;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.genders, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);
        saveButton = findViewById(R.id.saveButton);

        Intent intent = getIntent();
        userDetails = new User();
        if (intent.hasExtra("extra_user_details")){
            // get user details from intent as ParcelableExtra
            userDetails = intent.getParcelableExtra("extra_user_details");
        }

        // get first name
        firstNameEditText.getEditText().setText(userDetails.firstName);
        // color
        firstNameEditText.getEditText().setTextColor(Color.parseColor("#000000"));

        // get last name
        lastNameEditText.getEditText().setText(userDetails.lastName);
        // color
        lastNameEditText.getEditText().setTextColor(Color.parseColor("#000000"));


        // get email
        emailEditText.getEditText().setText(userDetails.email);
        // color
        emailEditText.getEditText().setTextColor(Color.parseColor("#000000"));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateProfileNumber()){
                    HashMap userHashMap = new HashMap();

                    String phoneNumber = phoneNumberEditText.getEditText().getText().toString();

                    // get gender from onItemSelected method
                    SharedPreferences sharedPreferences = getSharedPreferences("gender", Context.MODE_PRIVATE);
                    String gender = sharedPreferences.getString("gender","");

                    // if phone number not empty, put in hashmap
                    if (!(phoneNumber.isEmpty())){
                        userHashMap.put("mobile",Long.parseLong(phoneNumber));
                    }
                    // put gender in hashmap
                    userHashMap.put("gender",gender);

                    // set profileCompleted to 1 in hashmap, meaning that the user will
                    // not be sent to user profile screen on next login
                    userHashMap.put("profileCompleted", 1);

                    // update data
                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).update(userHashMap)
                            .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(getApplicationContext(), "Success.", Toast.LENGTH_SHORT).show();
                            // if successful, go to bluetoothactivity to connect scanner, temporarily set to Primaryactivity
                            Intent goToBluetoothActivity = new Intent(UserProfileActivity.this,PrimaryActivity.class);
                            startActivity(goToBluetoothActivity);
                            finish();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error while updating user details. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            });


                }
            }
        });


    }
    // get gender from spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String genderSelected = adapterView.getItemAtPosition(i).toString();
        SharedPreferences sharedPreferences = getSharedPreferences("gender", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("gender", genderSelected);
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }

    // maybe add (or if phonenumber < X digits)
    public boolean validateProfileNumber(){
        if (phoneNumberEditText.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phoneNumberEditText.getEditText().getText().toString().length() != 10){
            Toast.makeText(getApplicationContext(), "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}