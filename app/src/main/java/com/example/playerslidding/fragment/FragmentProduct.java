package com.example.playerslidding.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.playerslidding.databinding.FragmentProductBinding;

public class FragmentProduct extends Fragment {
    private FragmentProductBinding b;
    private String uuid;

    public static FragmentProduct newInstance(String uuid) {
        FragmentProduct fragment = new FragmentProduct();
        Bundle args = new Bundle();
        args.putString("uuid", uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString("uuid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentProductBinding.inflate(inflater, container, false);
        getProductData();
        return b.getRoot();
    }

    private void getProductData() {

    }
}