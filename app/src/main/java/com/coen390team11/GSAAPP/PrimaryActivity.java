package com.coen390team11.GSAAPP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;

import com.coen390team11.GSAAPP.ui.LogoutDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.coen390team11.GSAAPP.databinding.ActivityPrimaryBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PrimaryActivity extends AppCompatActivity {

    private ActivityPrimaryBinding binding;
    User userY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrimaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_bag, R.id.navigation_history, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_primary);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // for settings fragment, data then passed with shared preferences to fragment
        getUserDetails(PrimaryActivity.this);

    }
    // used to display info in settings fragment
    public final void getUserDetails(@NotNull Activity activity){
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

                SharedPreferences sharedPreferences = activity.getSharedPreferences("settings_fragment_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("get_name", useR.firstName + " " + useR.lastName);
                editor.putString("get_email",useR.email);
                editor.putLong("get_phone",useR.mobile);
                editor.putLong("get_rewards_number",useR.rewardsCardNumber);
                editor.apply();

                if (activity instanceof PrimaryActivity) {
                    ((PrimaryActivity) activity).userDetailsSuccess(useR);
                }
            }
        });
    }

    void userDetailsSuccess(User userX){

        userY = userX; // temp

        // Logs
        Log.i("User ---> ",userX.firstName);
        Log.i("User ---> ",userX.lastName);
        try{
            Log.i("User --->", String.valueOf(userX.mobile));
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.i("User ---> ",userX.email);
    }

    public void openLogoutDialog(){
        LogoutDialog logoutDialog = new LogoutDialog();
        logoutDialog.show(getSupportFragmentManager(),"Logout");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_primary,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Logout: // if user presses on "Logout" then the following
                // will be executed:
                openLogoutDialog();
                return true;

            case R.id.Rewards: // if user presses on "Rewards" then the following
                // will be executed:
                // start rewards activity here
                Intent goToRewardsActivityIntent = new Intent(getApplicationContext(),RewardsActivity.class);
                startActivity(goToRewardsActivityIntent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}