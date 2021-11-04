package com.coen390team11.GSAAPP.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
                String timeStampShoppingEvent0 = (value.get("timestamp0")).toString();
                String timeStampShoppingEvent1 = (value.get("timestamp1")).toString();
                String timeStampShoppingEvent2 = (value.get("timestamp2")).toString();
                String timeStampShoppingEvent3 = (value.get("timestamp3")).toString();
                String timeStampShoppingEvent4 = (value.get("timestamp4")).toString();

                pastShoppingEvents.add(timeStampShoppingEvent0);
                pastShoppingEvents.add(timeStampShoppingEvent1);
                pastShoppingEvents.add(timeStampShoppingEvent2);
                pastShoppingEvents.add(timeStampShoppingEvent3);
                pastShoppingEvents.add(timeStampShoppingEvent4);

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