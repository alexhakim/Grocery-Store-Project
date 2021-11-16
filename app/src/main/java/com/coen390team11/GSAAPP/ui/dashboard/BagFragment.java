package com.coen390team11.GSAAPP.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.coen390team11.GSAAPP.CheckoutActivity;
import com.coen390team11.GSAAPP.ConfirmDeleteDialog;
import com.coen390team11.GSAAPP.ItemInformation;
import com.coen390team11.GSAAPP.Items;
import com.coen390team11.GSAAPP.LoginActivity;
import com.coen390team11.GSAAPP.NutritionInfoActivity;
import com.coen390team11.GSAAPP.R;
import com.coen390team11.GSAAPP.User;
import com.coen390team11.GSAAPP.databinding.FragmentBagBinding;
import com.coen390team11.GSAAPP.deleteItem;
import com.coen390team11.GSAAPP.itemsPerUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BagFragment extends Fragment {


    private BagViewModel dashboardViewModel;
    private FragmentBagBinding binding;
    FloatingActionButton fab;
    SwipeMenuListView currentBagListView;
    ArrayList<String> barcode = new ArrayList<String>();
    Map hashMapCount = new HashMap();
    Map hashMapName = new HashMap();
    int counter=0;
    LinkedHashSet<String> linkedHashSet;
    ArrayList<String> noDuplicates;
    Double checkoutTotalPrice = 0.0;
    String productPrice;
    ArrayAdapter arrayAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(BagViewModel.class);

        binding = FragmentBagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        itemsPerUser itemsPerUser = new itemsPerUser();
        deleteItem deleteItem = new deleteItem();

        // listview that will display scanned items
        currentBagListView = binding.currentBagListView;
        ArrayList<String> productsInBagArrayList = new ArrayList<String>();

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteMenuItem = new SwipeMenuItem(
                        getContext());
                deleteMenuItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteMenuItem.setWidth(150);
                deleteMenuItem.setIcon(R.drawable.ic_baseline_delete_24_red);
                menu.addMenuItem(deleteMenuItem);
            }
        };

        currentBagListView.setMenuCreator(creator);

        // currently adding temp barcodes
        barcode.add("7680801101"); // example of barilla spaghetti
        barcode.add("7680801101"); // example of barilla spaghetti
        barcode.add("7680801101"); // example of barilla spaghetti
        barcode.add("7680801101"); // example of barilla spaghetti
        barcode.add("7680801101"); // example of barilla spaghetti
        barcode.add("5449000000996"); // coca cola
        barcode.add("5449000000996"); // coca cola
        barcode.add("5449000000996"); // coca cola
        barcode.add("5449000000996"); // coca cola
        barcode.add("6041004701"); // lays chips
        barcode.add("6041004701"); // lays chips
        barcode.add("6041004701"); // lays chips
        barcode.add("6041004701"); // lays chips
        barcode.add("6041004701"); // lays chips
        barcode.add("6041004701"); // lays chips
        barcode.add("6202000084"); // nutella spread
        barcode.add("5620097439"); // french's ketchup
        barcode.add("5620097439"); // french's ketchup
        barcode.add("5900001654"); // robin hood all purpose flour
        barcode.add("6810008424"); // kraft smooth peanut butter
        barcode.add("6810008424"); // kraft smooth peanut butter
        barcode.add("6563313434"); // lucky charms cereal

        // copy barcode arraylist into noDuplicates arraylist but without duplicates
        linkedHashSet = new LinkedHashSet<>(barcode);
        noDuplicates = new ArrayList<>(linkedHashSet);


        // key: barcode, value: counter. Counter for each barcode
        for (int i=0;i<barcode.size();i++){
            counter=2;
            for (int j=0;j<barcode.size();j++){
                if (barcode.get(i).equals(barcode.get(j))){
                    hashMapCount.put(barcode.get(i),counter-1);
                    counter++;
                }
            }
        }
        Log.d("MAP:     ", hashMapCount.toString());
        // getting count for each barcode

        // store barcode -> name in other hashmap with key = barcode
        // get name from hashmapName according to barcode
        // get count from hashmapCount according to barcode
        // display in arraylist

        // needed to store current cart to database in case of adding/removing items and switching activities
        Log.i("FIREBASE ID: ", String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getEmail()));

        // get item name based on barcode input
        FirebaseFirestore.getInstance().collection("items").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            // for each existing document in "items" collection
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("QUERY ---> ", document.getId() + " => " + document.getData());

                                // converting all received document data to string
                                String dataToString = document.getData().toString();

                                for (int i=0;i<barcode.size();i++) {
                                    if (dataToString.contains(barcode.get(i))){

                                        // if document contains barcode, return name
                                        // implement return field
                                        Log.d("DATA ---->", dataToString);

                                        String[] trim = dataToString.split(",");
                                        String nameSegment = trim[3];
                                        String productName = nameSegment.substring(6);
                                        Log.i("PRODUCT: ",productName);
                                        hashMapName.put(barcode.get(i),productName);

                                        String priceSegment = trim[2];
                                        productPrice = priceSegment.substring(7);
                                        checkoutTotalPrice+=Double.parseDouble(productPrice);
                                        Log.i("CHECKOUT PRICE: ", String.valueOf(checkoutTotalPrice));

                                    }
                                }
                            }

                            // hashMapCount size == hashMapName size since both have barcodes as keys
                            // get count for barcode from hashMapCount and get name for barcode from hashMapName
                            for (int i=0;i<hashMapCount.size();i++){
                                productsInBagArrayList.add(hashMapCount.get(noDuplicates.get(i)) + "x " + hashMapName.get(noDuplicates.get(i)));
                                Log.i("ARRL --->",hashMapName.get(noDuplicates.get(i)) + " " + hashMapCount.get(noDuplicates.get(i)));
                                Log.i("BARCODE ---->",noDuplicates.get(i));
                            }

                            Log.d("PNM --->", hashMapName.toString());
                            arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, productsInBagArrayList);
                            currentBagListView.setAdapter(arrayAdapter);
                            arrayAdapter.notifyDataSetChanged();


                            // add document of name set to email of user to collection itemsPerUser
                            FirebaseFirestore.getInstance().collection("itemsPerUser").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                    .set(itemsPerUser, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                            FirebaseFirestore.getInstance().collection("deleteItem").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                    .set(deleteItem, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                            // storing current bag of logged in user in database
                            for (int i=0;i<hashMapCount.size();i++) {

                                Log.i("XYZ",hashMapCount.get(noDuplicates.get(i)) + "x " + hashMapName.get(noDuplicates.get(i)));

                                FirebaseFirestore.getInstance().collection("itemsPerUser")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).update("items",
                                        FieldValue.arrayUnion(hashMapCount.get(noDuplicates.get(i)) + "x " + hashMapName.get(noDuplicates.get(i))));
                            }
                        } else {
                            Log.d(TAG, "Error retrieving document information: ", task.getException());
                        }
                    }
                });

        // go to nutritioninfo activity
        currentBagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent goToItemInformationIntent = new Intent(getContext(), ItemInformation.class);
                startActivity(goToItemInformationIntent);

                // pass data of product name to nutritional info activity
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("product_name", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("product_name", productsInBagArrayList.get(position));
                editor.apply();
            }
        });

        currentBagListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        // alert dialog to confirm delete then delete from firebase
                        ConfirmDeleteDialog confirmDeleteDialog = new ConfirmDeleteDialog();
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("product_name", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("product_name", productsInBagArrayList.get(position));
                        editor.apply();
                        confirmDeleteDialog.show(getChildFragmentManager(),"OK");

                        SharedPreferences sp = getContext().getSharedPreferences("delete_item", Context.MODE_PRIVATE);
                        //String deleteItem = sp.getString("delete_item", "");
                        //Log.i("DELETEITEM",deleteItem);

                        FirebaseFirestore.getInstance().collection("deleteItem")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        try {
                                            String deleteItem = (value.get("itemDeleted")).toString();
                                            if (productsInBagArrayList.contains(deleteItem)) {
                                                productsInBagArrayList.remove(deleteItem);
                                                arrayAdapter.notifyDataSetChanged();


                                                Log.i("DELETEITEMSUBSTRING3",deleteItem.substring(3));
                                                FirebaseFirestore.getInstance().collection("items")
                                                        .document(deleteItem.substring(3)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                        String getDeletedItemPrice = (value.get("price")).toString();
                                                        Log.i("GETDELETEDITEMPRICE",getDeletedItemPrice);
                                                        getDeletedItemPrice = String.format("%.2f",Double.parseDouble(getDeletedItemPrice));
                                                        // single digit
                                                        Log.i("DELETEITEMCOUNT",deleteItem.substring(0,1));
                                                        checkoutTotalPrice-=(Double.parseDouble(getDeletedItemPrice)*Integer.parseInt(deleteItem.substring(0,1)));
                                                        //Toast.makeText(getContext(), checkoutTotalPrice+"OK", Toast.LENGTH_SHORT).show();
                                                        Log.i("CHECKOUTTOTALPRICE", String.valueOf(checkoutTotalPrice));
                                                    }
                                                });
                                            }
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        break;
                }
                return false;
            }
        });

        currentBagListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);


        // go to checkout floating action button
        fab = binding.fab;
        fab.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to checkout
                String totalPrice = String.format("%.2f",checkoutTotalPrice); // round to 2 decimal places
                Log.d("TOTALPRICE",checkoutTotalPrice.toString());
                Intent goToCheckoutIntent = new Intent(getContext(), CheckoutActivity.class);
                goToCheckoutIntent.putExtra("total_price",totalPrice);
                goToCheckoutIntent.putExtra("current_bag",productsInBagArrayList);

                // pass subtotal to DisplayPastShoppingEventActivity
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("product_subtotal", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("product_subtotal", totalPrice);
                editor.apply();

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
