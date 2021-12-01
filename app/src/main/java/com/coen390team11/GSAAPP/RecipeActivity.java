package com.coen390team11.GSAAPP;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RecipeActivity extends AppCompatActivity {

    EditText txt1, txt2, txt3;
    ListView ls;
    Button btn;
    String in;
    ArrayList<String> userList;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    ArrayAdapter<String> listAdapter;
    private Object HttpURLConnection;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        txt1 = findViewById(R.id.txt1);
        ls = findViewById(R.id.ls);
        in = txt1.getText().toString();
        initializeUserList();
        btn = findViewById(R.id.btn);
        try {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new fetchData().start();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class fetchData extends Thread {

        String data = "";


        @Override
        public void run() {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    progressDialog = new ProgressDialog(RecipeActivity.this);
                    progressDialog.setMessage("Fetching Data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                URL url = new URL("www.themealdb.com/api/json/v1/1/filter.php?i=" + in);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = null;
                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {

                    data = data + line;
                }

                if (!data.isEmpty()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(data);
                        JSONArray meals = (JSONArray) jsonObject.get("meals");
                        userList.clear();
                        for (int i = 0; i < meals.length(); i++) {
                            JSONObject recipes = meals.getJSONObject(i);
                            String recipe = recipes.getString("strMeals");
                            userList.add(recipe);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    listAdapter.notifyDataSetChanged();

                }
            });

        }

    }


    public void initializeUserList(){
        userList = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userList);
        ls.setAdapter(listAdapter);
    }
}