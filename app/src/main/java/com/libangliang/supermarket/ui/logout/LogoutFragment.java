package com.libangliang.supermarket.ui.logout;

import android.content.Intent;
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

import com.libangliang.supermarket.LoginActivity;
import com.libangliang.supermarket.MainActivity;
import com.libangliang.supermarket.R;

import io.paperdb.Paper;

public class LogoutFragment extends Fragment {

    private LogoutViewModel logoutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logoutViewModel =
                ViewModelProviders.of(this).get(LogoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        logoutViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        Paper.book().destroy();
        getActivity().finish();

        return root;
    }
}