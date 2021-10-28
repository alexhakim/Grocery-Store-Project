package com.coen390team11.GSAAPP.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coen390team11.GSAAPP.CheckoutActivity;
import com.coen390team11.GSAAPP.NutritionInfoActivity;
import com.coen390team11.GSAAPP.R;
import com.coen390team11.GSAAPP.databinding.FragmentBagBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BagFragment extends Fragment {

    private BagViewModel dashboardViewModel;
    private FragmentBagBinding binding;
    FloatingActionButton fab;
    ListView currentBagListView;
    ArrayList<String> barcode = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(BagViewModel.class);

        binding = FragmentBagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // listview that will display scanned items
        currentBagListView = binding.currentBagListView;
        ArrayList<String> productsInBagArrayList = new ArrayList<String>();


        // barcode database api from UPCDatabase using OkHttp


        // currently adding temp barcodes
        barcode.add("7035020003611"); // example of baguette
        barcode.add("0064100052079"); // rice krispies
        barcode.add("0070038605713"); // ketchup
        barcode.add("0768395432539"); // lollipop
        barcode.add("0090202000083"); // soy sauce


        // use O(n^2) to traverse array for multiple quantities
        for (int i=0;i<barcode.size();i++) {
            OkHttpClient client = new OkHttpClient();
            String url = "https://api.upcdatabase.org/product/" + barcode.get(i) + "?apikey=D46EBAD968F75DB328602AB30694737A";

            Request request = new Request.Builder().url(url).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String result = response.body().string();

                        // split string into segments separated by coma
                        String trim[] = result.split(",");
                        String getCorrectSegment = trim[2];
                        String getCorrectName = getCorrectSegment.substring(9, getCorrectSegment.length() - 1).substring(0, 1).toUpperCase() + getCorrectSegment.substring(10, getCorrectSegment.length() - 1).toLowerCase();

                        // calling getActivity because we are currently in background thread
                        // not doing this would cause fatal exception
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                // display in current bag
                                productsInBagArrayList.add(getCorrectName);
                                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, productsInBagArrayList);
                                currentBagListView.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();

                                // use https://calorieninjas.com/api api for nutritional information
                                // or https://developer.edamam.com/food-database-api
                                // or https://www.nutritionix.com/business/api



                            }
                        });
                    }
                }
            });
        }


        // go to nutritioninfo activity
        currentBagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent goToNutritionInfoIntent = new Intent(getContext(), NutritionInfoActivity.class);
                startActivity(goToNutritionInfoIntent);

                // pass data of product name to nutritional info activity
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("product_name", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("product_name", productsInBagArrayList.get(position));
                editor.apply();
            }
        });


        // go to checkout floating action button
        fab = binding.fab;
        fab.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to checkout
                Intent goToCheckoutIntent = new Intent(getContext(), CheckoutActivity.class);
                startActivity(goToCheckoutIntent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}