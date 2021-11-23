package com.coen390team11.GSAAPP;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UpdatePricesActivity extends AppCompatActivity {

    ListView searchProducts;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_prices);

        ActionBar actionBar = getSupportActionBar();
        // changing color of action bar
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#344398"));
        actionBar.setBackgroundDrawable(colorDrawable);

        searchProducts = findViewById(R.id.searchProducts);

        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<String> priceArrayList = new ArrayList<String>();

        // getting products from firebase
        FirebaseFirestore.getInstance().collection("items")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String getDocumentData = document.getData().toString();
                        Log.i("GETDOCUMENTDATAEMPLOYEE",getDocumentData);
                        String[] trim = getDocumentData.split(",");
                        String productNameSegment = trim[3];
                        String productName = productNameSegment.substring(6);
                        Log.i("PRODUCTNAMEEMPLOYEE",productName);

                        String productPriceSegment = trim[2];
                        String productPrice = productPriceSegment.substring(7);

                        arrayList.add(productName);
                        priceArrayList.add(productPrice);

                        arrayAdapter = new ArrayAdapter<String>(UpdatePricesActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        searchProducts.setAdapter(arrayAdapter);



                        searchProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                SharedPreferences sharedPreferences = getSharedPreferences("product_name_employee", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("product_name_employee", arrayList.get(i));
                                editor.putString("product_price_employee", priceArrayList.get(i));
                                editor.apply();
                                ChangeProductPriceDialog changeProductPriceDialog = new ChangeProductPriceDialog();
                                changeProductPriceDialog.show(getSupportFragmentManager(),"OK");
                            }
                        });


                    }
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout,menu);
        //MenuItem item = menu.findItem(R.id.searchInterface);
        /*try {
            SearchView searchView = (SearchView) item.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    arrayAdapter.getFilter().filter(s);
                    return false;
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }*/

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.LogoutC:
                Intent goToLoginActivityIntent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(goToLoginActivityIntent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}