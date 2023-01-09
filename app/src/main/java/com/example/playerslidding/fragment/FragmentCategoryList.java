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
    private ArrayList<CategoryDto> sub = new ArrayList<>();

    public static FragmentCategoryList newInstance() {
        Bundle args = new Bundle();

        FragmentCategoryList fragment = new FragmentCategoryList();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sub.add(new CategoryDto("Мужская", "Мужская", "", null, "", "", "", null, null, null, false));
        sub.add(new CategoryDto("Новинки", "Новинки", "", null, "", "", "", null, null, null, false));
        sub.add(new CategoryDto("спецодежда (униформа)", "спецодежда (униформа)", "", null, "", "", "", null, null, null, false));
        sub.add(new CategoryDto("Ткани", "Ткани", "", null, "", "", "", null, null, null, false));
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

        adapterText.setTabs(sub);
        return b.getRoot();
    }


    private void setRecycler() {
        adapterText = new AdapterText(getContext());
        b.recCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recCategory.setAdapter(adapterText);
    }
}