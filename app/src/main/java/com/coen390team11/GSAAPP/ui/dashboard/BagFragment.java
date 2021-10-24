package com.coen390team11.GSAAPP.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coen390team11.GSAAPP.CheckoutActivity;
import com.coen390team11.GSAAPP.R;
import com.coen390team11.GSAAPP.databinding.FragmentBagBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BagFragment extends Fragment {

    private BagViewModel dashboardViewModel;
    private FragmentBagBinding binding;
    FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(BagViewModel.class);

        binding = FragmentBagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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