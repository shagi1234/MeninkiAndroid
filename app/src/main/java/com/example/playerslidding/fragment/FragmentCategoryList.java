package com.example.playerslidding.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.playerslidding.adapter.AdapterText;
import com.example.playerslidding.databinding.FragmentCategoryListBinding;

public class FragmentCategoryList extends Fragment {
    private FragmentCategoryListBinding b;
    private AdapterText adapterText;

    public static FragmentCategoryList newInstance() {
        FragmentCategoryList fragment = new FragmentCategoryList();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCategoryListBinding.inflate(inflater, container, false);
        setRecycler();

        adapterText.setTabs(null);
        return b.getRoot();
    }

    private void setRecycler() {
        adapterText = new AdapterText(getContext());
        b.recCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recCategory.setAdapter(adapterText);
    }
}