package com.coen390team11.GSAAPP.ui.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coen390team11.GSAAPP.PrimaryActivity;
import com.coen390team11.GSAAPP.R;
import com.coen390team11.GSAAPP.databinding.FragmentSettingsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Locale;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private SettingsViewModel notificationsViewModel;
    private FragmentSettingsBinding binding;

    TextInputLayout getNameEditText;
    TextInputLayout getEmailEditText;
    TextInputLayout getPhoneNumberEditText;
    TextInputLayout getCartlyCardNumberEditText;
    Spinner changeLanguageSpinner;
    Button saveSettingsButton;
    Switch dataSavingModeSwitch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //loadLocale();

        changeLanguageSpinner = binding.changeLanguageSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.languages, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        changeLanguageSpinner.setAdapter(adapter);
        //changeLanguageSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) getContext());
        changeLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*SharedPreferences.Editor editor = getContext().getSharedPreferences("LanguageFromSpinner", Context.MODE_PRIVATE).edit();
                editor.putString("LanguageFromSpinner", String.valueOf(i));
                editor.apply();*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        getNameEditText = binding.getNameEditText;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("settings_fragment_info",Context.MODE_PRIVATE);
                String passName = sharedPreferences.getString("get_name","");
                getNameEditText.getEditText().setText(passName);
                //getNameEditText.getEditText().setTextColor(Color.parseColor("#000000"));
            }
        });

        getEmailEditText = binding.getEmailEditText;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("settings_fragment_info",Context.MODE_PRIVATE);
                String passEmail = sharedPreferences.getString("get_email","");
                getEmailEditText.getEditText().setText(passEmail);
                //getEmailEditText.getEditText().setTextColor(Color.parseColor("#000000"));
            }
        });

        getPhoneNumberEditText = binding.getPhoneNumberEditText;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("settings_fragment_info",Context.MODE_PRIVATE);
                Long passPhone = sharedPreferences.getLong("get_phone",0);
                getPhoneNumberEditText.getEditText().setText(passPhone + "");

            }
        });

        getCartlyCardNumberEditText = binding.getCartlyCardNumberEditText;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("settings_fragment_info",Context.MODE_PRIVATE);
                Long passRewardsNumber = sharedPreferences.getLong("get_rewards_number",0);
                getCartlyCardNumberEditText.getEditText().setText(passRewardsNumber + "");
                //getEmailEditText.getEditText().setTextColor(Color.parseColor("#000000"));
            }
        });

        saveSettingsButton = binding.saveSettingsButton;
        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // language
                /*SharedPreferences sharedPreferences = getContext().getSharedPreferences("LanguageFromSpinner", Activity.MODE_PRIVATE);
                String lang = sharedPreferences.getString("LanguageFromSpinner","");
                Log.i("LANGUAGEFROMSPINNER",lang);

                if (Integer.parseInt(lang) == 0) {
                    setLocale("en");
                    getActivity().invalidateOptionsMenu();
                    getFragmentManager()
                            .beginTransaction()
                            .detach(SettingsFragment.this)
                            .attach(SettingsFragment.this)
                            .commit();

                } else if (Integer.parseInt(lang) == 1){
                    setLocale("fr");
                    getActivity().invalidateOptionsMenu();
                    getFragmentManager()
                            .beginTransaction()
                            .attach(SettingsFragment.this)
                            .detach(SettingsFragment.this)
                            .commit();
                }*/




                HashMap updateUserHashMap = new HashMap();


                String getNewPhoneNumber = getPhoneNumberEditText.getEditText().getText().toString();
                if (getNewPhoneNumber.toString().length() != 10 ) {
                    Snackbar.make(view, "Phone number must be 10 digits.", Snackbar.LENGTH_LONG).show();
                } else {
                    updateUserHashMap.put("mobile",Long.parseLong(getNewPhoneNumber));


                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).update(updateUserHashMap)
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                               // Toast.makeText(getContext(), "Succesfully updated information.", Toast.LENGTH_SHORT).show();
                                //Snackbar.make(view, "Succesfully updated information.", Snackbar.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // do nothing
                    }
                });
                }
            }
        });

        dataSavingModeSwitch = binding.dataSavingModeSwitch;
        dataSavingModeSwitch.setOnCheckedChangeListener(this);

        // get state of switch from firebase
        // getting dataSavingMode value
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String dataSavingModeString = (value.get("dataSavingMode")).toString();
                if (dataSavingModeString.equals("1")){
                    dataSavingModeSwitch.setChecked(true);
                } else if (dataSavingModeString.equals("0")){
                    dataSavingModeSwitch.setChecked(false);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch(compoundButton.getId()){
            case R.id.dataSavingModeSwitch:
                if (isChecked){ // when user requests to disable images
                    FirebaseFirestore.getInstance().collection("users")
                            .document( FirebaseAuth.getInstance().getCurrentUser().getUid()).update("dataSavingMode",1);

                } else { // normal mode
                    FirebaseFirestore.getInstance().collection("users")
                            .document( FirebaseAuth.getInstance().getCurrentUser().getUid()).update("dataSavingMode",0);

                }
                break;
        }
    }

    /*public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getContext().getResources().updateConfiguration(configuration, getContext().getResources().getDisplayMetrics());

        // save data to shared preferences
        SharedPreferences.Editor editor = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

    }

    // load language from shared preferences
    public void loadLocale(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = sharedPreferences.getString("My_Lang","");
        Log.i("LANGUAGELOADLOCALE",language);
        setLocale(language);
    }*/

}