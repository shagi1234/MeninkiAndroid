package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.playerslidding.adapter.AdapterRedactorPrice;
import com.example.playerslidding.databinding.FragmentReadactorPriceBinding;

public class FragmentRedactorPrice extends Fragment {
    private FragmentReadactorPriceBinding b;

    public static FragmentRedactorPrice newInstance() {
        FragmentRedactorPrice fragment = new FragmentRedactorPrice();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentReadactorPriceBinding.inflate(inflater, container, false);
        setRecycler();
        return b.getRoot();
    }

    private void setRecycler() {
        AdapterRedactorPrice adapterRedactorPrice = new AdapterRedactorPrice(getContext());
        b.recMedia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recMedia.setAdapter(adapterRedactorPrice);

        adapterRedactorPrice.setImageUrl(null);
    }
}