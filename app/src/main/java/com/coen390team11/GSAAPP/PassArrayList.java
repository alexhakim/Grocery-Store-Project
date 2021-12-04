package com.coen390team11.GSAAPP;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class PassArrayList {

    SharedPreferences sharedPreferences;
    Context context;

    public PassArrayList(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }
    public void putArrayList(String key, ArrayList<String> stringList) {
        String[] ArrayListStr = stringList.toArray(new String[stringList.size()]);
        sharedPreferences.edit().putString(key, TextUtils.join("‚‗‚", ArrayListStr)).apply();
    }
    public ArrayList<String> getArrayList(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(sharedPreferences.getString(key, ""), "‚‗‚")));
    }
}
