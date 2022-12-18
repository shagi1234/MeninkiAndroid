package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.playerslidding.R;
import com.example.playerslidding.adapter.AdapterMyShops;
import com.example.playerslidding.adapter.AdapterTabLayout;
import com.example.playerslidding.data.TabItemCustom;
import com.example.playerslidding.databinding.FragmentMyShopsBinding;
import com.example.playerslidding.interfaces.OnTabClicked;

import java.util.ArrayList;

public class FragmentMyShops extends Fragment implements OnTabClicked {
    private FragmentMyShopsBinding b;
    private AdapterTabLayout adapterTabLayout;
    private ArrayList<TabItemCustom> tabs = new ArrayList<>();
    private AdapterMyShops adapterMyShops;

    public static FragmentMyShops newInstance() {
        FragmentMyShops fragment = new FragmentMyShops();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabs.add(new TabItemCustom("Работающие", true));
        tabs.add(new TabItemCustom("Ожидают подтверждения", false));
        tabs.add(new TabItemCustom("Скрытые", false));
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.layHeader, 0, statusBarHeight, 0, 0);
        setMargins(b.coordinator, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentMyShopsBinding.inflate(inflater, container, false);
        setBackgrounds();
        setRecyclerTab();
        setRecycler();
        initListeners();
        adapterMyShops.setStories(null);
        adapterTabLayout.setTabs(tabs);
        return b.getRoot();
    }

    private void initListeners() {
        b.icBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void setRecycler() {
        adapterMyShops = new AdapterMyShops(getContext(), getActivity());
        b.recProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recProducts.setAdapter(adapterMyShops);
    }

    private void setRecyclerTab() {
        adapterTabLayout = new AdapterTabLayout(getContext(), null);
        b.recTab.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recTab.setAdapter(adapterTabLayout);
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.layBtn, R.color.accent, 0, 10, false, 0);
    }

    @Override
    public void onClick(int adapterPosition) {

    }
}