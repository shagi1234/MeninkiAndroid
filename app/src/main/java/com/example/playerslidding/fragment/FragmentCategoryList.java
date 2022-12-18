package com.example.playerslidding.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.playerslidding.adapter.AdapterText;
import com.example.playerslidding.data.CategoryDto;
import com.example.playerslidding.databinding.FragmentCategoryListBinding;

import java.util.ArrayList;

public class FragmentCategoryList extends Fragment {
    private FragmentCategoryListBinding b;
    private AdapterText adapterText;
    private final ArrayList<CategoryDto> categoryDto;

    public FragmentCategoryList(ArrayList<CategoryDto> subCategories) {
        this.categoryDto=subCategories;
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

        adapterText.setTabs(categoryDto);
        return b.getRoot();
    }


    private void setRecycler() {
        adapterText = new AdapterText(getContext());
        b.recCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recCategory.setAdapter(adapterText);
    }
}