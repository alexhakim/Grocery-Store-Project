package com.coen390team11.GSAAPP.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coen390team11.GSAAPP.DisplayPastShoppingEventActivity;
import com.coen390team11.GSAAPP.databinding.FragmentHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private HistoryViewModel homeViewModel;
    private FragmentHistoryBinding binding;
    ListView historyListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        historyListView = binding.historyListView;
        ArrayList<String> pastShoppingEvents = new ArrayList<String>();

        /* here we add the past shopping events received from firebase,
        ** which are received from the complete purchase button
        ** in the checkout activity to the arraylist*/

        FirebaseFirestore.getInstance().collection("pastShoppingEventsPerUser")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String timeStampShoppingEvent0 = (value.get("timeStamp0")).toString();
                String timeStampShoppingEvent1 = (value.get("timeStamp1")).toString();
                String timeStampShoppingEvent2 = (value.get("timeStamp2")).toString();
                String timeStampShoppingEvent3 = (value.get("timeStamp3")).toString();
                String timeStampShoppingEvent4 = (value.get("timeStamp4")).toString();

                pastShoppingEvents.add("Shopping Event on " + timeStampShoppingEvent0);
                pastShoppingEvents.add("Shopping Event on " + timeStampShoppingEvent1);
                pastShoppingEvents.add("Shopping Event on " + timeStampShoppingEvent2);
                pastShoppingEvents.add("Shopping Event on " + timeStampShoppingEvent3);
                pastShoppingEvents.add("Shopping Event on " + timeStampShoppingEvent4);

                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, pastShoppingEvents);
                historyListView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

            }
        });

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goToPastShoppingEventIntent = new Intent(getContext(), DisplayPastShoppingEventActivity.class);
                startActivity(goToPastShoppingEventIntent);
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