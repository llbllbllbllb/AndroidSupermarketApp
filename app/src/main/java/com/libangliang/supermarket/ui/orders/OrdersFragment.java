package com.libangliang.supermarket.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.libangliang.supermarket.R;

public class OrdersFragment extends Fragment {

    private OrdersViewModel ordersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ordersViewModel =
                ViewModelProviders.of(this).get(OrdersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        ordersViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}