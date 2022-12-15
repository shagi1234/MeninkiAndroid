package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.playerslidding.databinding.FragmentCountryAndNumberBinding;
import com.example.playerslidding.databinding.FragmentCountryCodeBinding;

public class FragmentCountryCode extends Fragment {
    private FragmentCountryCodeBinding b;

    public static FragmentCountryCode newInstance(String param1, String param2) {
        FragmentCountryCode fragment = new FragmentCountryCode();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.getRoot(),0,statusBarHeight,0,navigationBarHeight);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCountryCodeBinding.inflate(inflater, container, false);

        return b.getRoot();
    }
}