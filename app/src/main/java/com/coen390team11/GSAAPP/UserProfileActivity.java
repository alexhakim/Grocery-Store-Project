package com.coen390team11.GSAAPP;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

public class UserProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
        User userDetails = new User();
        if (intent.hasExtra("extra_user_details")){
            // get user details from intent as ParcelableExtra
            userDetails = intent.getParcelableExtra("extra_user_details");
        }

        // setting first name to not editable
        firstNameEditText.setFocusable(false);
        firstNameEditText.setClickable(true);
        // get first name
        firstNameEditText.getEditText().setText(userDetails.firstName);
        firstNameEditText.getEditText().setTextColor(Color.parseColor("#000000"));

        // setting last name to not editable
        lastNameEditText.setFocusable(false);
        lastNameEditText.setClickable(true);
        // get last name
        lastNameEditText.getEditText().setText(userDetails.lastName);
        lastNameEditText.getEditText().setTextColor(Color.parseColor("#000000"));


        // setting email to not editable
        emailEditText.setFocusable(false);
        emailEditText.setClickable(true);
        // get email
        emailEditText.getEditText().setText(userDetails.email);
        emailEditText.getEditText().setTextColor(Color.parseColor("#000000"));


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String genderSelected = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}