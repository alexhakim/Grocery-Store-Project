package com.coen390team11.GSAAPP.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.coen390team11.GSAAPP.CheckoutActivity;
import com.coen390team11.GSAAPP.ConfirmDeleteDialog;
import com.coen390team11.GSAAPP.ItemInformation;
import com.coen390team11.GSAAPP.PrimaryActivity;
import com.coen390team11.GSAAPP.R;
import com.coen390team11.GSAAPP.databinding.FragmentBagBinding;
import com.coen390team11.GSAAPP.deleteItem;
import com.coen390team11.GSAAPP.itemsPerUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
    Double checkoutTotalPrice = 0.00;
    String productPrice;
    ArrayAdapter arrayAdapter;
    String getBarcodes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(BagViewModel.class);

        binding = FragmentBagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

        FirebaseFirestore.getInstance().collection("itemScanned")
                .document("itemBarcode")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        try{
                            barcode.clear();
                            productsInBagArrayList.clear();
                            getBarcodes = (value.get("barcodeArray")).toString();
                            Log.i("GETBARCODEFIREBASE", getBarcodes);



                            String[] trim = getBarcodes.split(",");
                            ArrayList<String> trimToArrayList = new ArrayList<String>();
                            // converting array to arraylist
                            Collections.addAll(trimToArrayList, trim);
                            for (int i = 0; i < trimToArrayList.size(); i++) {
                                if (trimToArrayList.get(i).contains("[")) {
                                    String temp = trimToArrayList.get(i);
                                    trimToArrayList.set(i, temp.substring(1));
                                }
                                if (trimToArrayList.get(i).contains("]")) {
                                    String temp = trimToArrayList.get(i);
                                    trimToArrayList.set(i, temp.substring(0, temp.length() - 1));
                                }
                                if (trimToArrayList.get(i).contains(" ")) {
                                    String temp = trimToArrayList.get(i);
                                    trimToArrayList.set(i, temp.substring(1));
                                }
                                barcode.add(trimToArrayList.get(i));


                                Log.i("BARCODENULLAB", trimToArrayList.get(i));
                            }

                            // remove duplicates
                            linkedHashSet = new LinkedHashSet<>(barcode);
                            noDuplicates = new ArrayList<>(linkedHashSet);
                            Log.i("NODUPLICATESARRAYLIST", String.valueOf(noDuplicates));

                            // key: barcode, value: counter. Counter for each barcode
                            for (int i = 0; i < barcode.size(); i++) {
                                counter = 2;
                                for (int j = 0; j < barcode.size(); j++) {
                                    if (barcode.get(i).equals(barcode.get(j))) {
                                        hashMapCount.put(barcode.get(i), counter - 1);
                                        counter++;
                                    }
                                }
                            }
                            Log.d("MAP:     ", hashMapCount.toString());

                            FirebaseFirestore.getInstance().collection("items").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                // for each existing document in "items" collection
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("QUERY ---> ", document.getId() + " => " + document.getData());


                                                    // converting all received document data to string
                                                    String dataToString = document.getData().toString();

                                                    for (int i = 0; i < barcode.size(); i++) {
                                                        if (dataToString.contains(barcode.get(i))) {

                                                            // if document contains barcode, return name
                                                            // implement return field
                                                            Log.d("DATA ---->", dataToString);

                                                            String[] trim = dataToString.split(",");
                                                            String nameSegment = trim[3];
                                                            String productName = nameSegment.substring(6);
                                                            Log.i("PRODUCT: ", productName);
                                                            hashMapName.put(barcode.get(i), productName);
                                                            Log.wtf("HASHMAPBARCODENAMESXD:     ", hashMapName.toString());
                                                            //Toast.makeText(getContext(), hashMapName.toString(), Toast.LENGTH_SHORT).show();


                                                            String priceSegment = trim[2];
                                                            productPrice = priceSegment.substring(7);
                                                            //checkoutTotalPrice += Double.parseDouble(productPrice);
                                                            Log.wtf("CHECKOUT PRICE: ", String.valueOf(checkoutTotalPrice));
                                                            //Toast.makeText(getContext(), String.valueOf(prices), Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                }

                                                // hashMapCount size == hashMapName size since both have barcodes as keys
                                                // get count for barcode from hashMapCount and get name for barcode from hashMapName
                                                try {
                                                    checkoutTotalPrice = 0.0;
                                                    for (int i = 0; i < hashMapCount.size(); i++) {
                                                        if (!hashMapName.get(noDuplicates.get(i)).toString().equals("Robin Hood All Purpose Flour")) {
                                                            productsInBagArrayList.add(hashMapCount.get(noDuplicates.get(i)) + "x " + hashMapName.get(noDuplicates.get(i)));
                                                            Log.i("ARRL --->", hashMapName.get(noDuplicates.get(i)) + " " + hashMapCount.get(noDuplicates.get(i)));
                                                            Log.i("BARCODE ---->", noDuplicates.get(i));
                                                            int finalI = i;
                                                            FirebaseFirestore.getInstance().collection("items")
                                                                    .document((hashMapName.get(noDuplicates.get(i))).toString())
                                                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                                            String getProductPrice = (value.get("price")).toString();
                                                                            checkoutTotalPrice += Double.parseDouble(getProductPrice) * Integer.parseInt(hashMapCount.get(noDuplicates.get(finalI)).toString());
                                                                        }
                                                                    });
                                                        }
                                                        //checkoutTotalPrice += Double.parseDouble((String) hashMapName.get(noDuplicates.get(i)));
                                                    }
                                                    //productsInBagArrayList.remove(0);

                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                                Log.d("PNM --->", hashMapName.toString());
                                                try {
                                                    arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, productsInBagArrayList);
                                                    currentBagListView.setAdapter(arrayAdapter);
                                                    arrayAdapter.notifyDataSetChanged();

                                                    Set<String> set = new HashSet<String>();
                                                    set.clear();
                                                    set.addAll(productsInBagArrayList);
                                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("passtoprimary",Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putStringSet("passtoprimary", set);
                                                    editor.apply();
                                                } catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }
                                    });

                        }catch (Exception e){
                            e.printStackTrace();
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
                                                Log.i("NEWPRODUCTSINBAG",String.valueOf(productsInBagArrayList));

                                                // update total price after removing X item
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


                                                        String getDeletedItemBarcode = (value.get("barcode")).toString();
                                                        Log.i("BARCODEARRAYINDELETE",String.valueOf(barcode));
                                                        Log.i("DELETEDITEMBARCODE",getDeletedItemBarcode);

                                                        for (int i=0;i< barcode.size();i++){
                                                            if (barcode.get(i).equals(getDeletedItemBarcode)){
                                                                barcode.remove(getDeletedItemBarcode);
                                                            }
                                                        }

                                                        Map<String, Object> updateDelete = new HashMap<>();
                                                        updateDelete.put("barcodeArray", barcode);
                                                        FirebaseFirestore.getInstance().collection("itemScanned")
                                                                .document("itemBarcode")
                                                                .set(updateDelete).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d("DocumentSnapshot successfully written!", "DocumentSnapshot successfully written!");
                                                                //barcode.clear();
                                                                //productsInBagArrayList.clear();

                                                            }
                                                        });

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

    public void tempBarcodes(){
        // temp barcodes
                            barcode.add("7680801101"); // example of barilla spaghetti
                            barcode.add("7680801101"); // example of barilla spaghetti
                            barcode.add("7680801101"); // example of barilla spaghetti
                            barcode.add("7680801101"); // example of barilla spaghetti
                            barcode.add("7680801101"); // example of barilla spaghetti
                            barcode.add("0747113510"); // coca cola
                            barcode.add("0747113510"); // coca cola
                            barcode.add("0747113510"); // coca cola
                            barcode.add("0747113510"); // coca cola
                            barcode.add("6041004701"); // lays chips
                            barcode.add("6041004701"); // lays chips
                            barcode.add("6041004701"); // lays chips
                            barcode.add("6041004701"); // lays chips
                            barcode.add("6041004701"); // lays chips
                            barcode.add("6041004701"); // lays chips
                            barcode.add("0620200008"); // nutella spread
                            barcode.add("5620097439"); // french's ketchup
                            barcode.add("5620097439"); // french's ketchup
                            barcode.add("5900001654"); // robin hood all purpose flour
                            barcode.add("6810008424"); // kraft smooth peanut butter
                            barcode.add("6810008424"); // kraft smooth peanut butter
                            barcode.add("6563313434"); // lucky charms cereal
    }
}
