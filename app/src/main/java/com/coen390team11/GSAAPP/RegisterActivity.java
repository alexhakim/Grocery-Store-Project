package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        lastNameEditText =  (TextInputLayout) findViewById(R.id.lastNameEditText);
        emailEditText =  (TextInputLayout) findViewById(R.id.emailEditText);
        passwordEditText = (TextInputLayout) findViewById(R.id.passwordEditText);
        passwordConfirmEditText =  (TextInputLayout) findViewById(R.id.passwordConfirmEditText);
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
                Intent switchToLoginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
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
                    Toast.makeText(getApplicationContext(), "Please fill all fields.", Toast.LENGTH_LONG).show();
                } else if (!(passwordEditText.getEditText().getText().toString().equals(passwordConfirmEditText.getEditText().getText().toString()))){

                    Toast.makeText(getApplicationContext(), "Password does not match.", Toast.LENGTH_SHORT).show();
                } else {

                    String email = emailEditText.getEditText().getText().toString();
                    String password = passwordEditText.getEditText().getText().toString();

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Success.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                intent.putExtra("email_id",email);
                                intent.putExtra("user_id",FirebaseAuth.getInstance().getCurrentUser().getUid());
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent switchToLoginIntent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(switchToLoginIntent);
                //onBackPressed();
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}