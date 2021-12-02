package com.coen390team11.GSAAPP;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.coen390team11.GSAAPP.ui.LogoutDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class EmployeeLoginActivity extends AppCompatActivity {

    TextInputLayout employeeIDEditText;
    TextInputLayout employeePasswordEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);

        employeeIDEditText = findViewById(R.id.employeeIDEditText);
        employeePasswordEditText = findViewById(R.id.employeePasswordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if there are missing fields
                if (employeeIDEditText.getEditText().getText().toString().isEmpty()
                        || employeePasswordEditText.getEditText().getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content),"Missing Fields.",Snackbar.LENGTH_SHORT).show();
                } else {
                    if (employeePasswordEditText.getEditText().getText().toString().equals("random")){
                        Intent goToUpdatePricesActivityIntent = new Intent(getApplicationContext(),UpdatePricesActivity.class);
                        startActivity(goToUpdatePricesActivityIntent);
                        finish();
                    } else { // if password is incorrect
                        Snackbar.make(findViewById(android.R.id.content),"Incorrect login credentials.",Snackbar.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_information,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}