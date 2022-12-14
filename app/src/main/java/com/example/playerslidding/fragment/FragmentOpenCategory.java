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
import androidx.viewpager.widget.ViewPager;

import com.example.playerslidding.adapter.AdapterTabLayout;
import com.example.playerslidding.adapter.AdapterViewPager;
import com.example.playerslidding.data.FragmentPager;
import com.example.playerslidding.data.TabItemCustom;
import com.example.playerslidding.databinding.FragmentOpenCategoryBinding;

import java.util.ArrayList;

public class FragmentOpenCategory extends Fragment {
    private FragmentOpenCategoryBinding b;
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ID = "uuid";
    private String uuid;
    private String name;
    private AdapterTabLayout adapterTabLayout;
    private AdapterViewPager adapterFeedPager;
    private ArrayList<TabItemCustom> tabs = new ArrayList<>();

    public static FragmentOpenCategory newInstance(String name, String id) {
        FragmentOpenCategory fragment = new FragmentOpenCategory();
        Bundle args = new Bundle();
        args.putString(PARAM_ID, name);
        args.putString(PARAM_NAME, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.viewPager,0,0,0,navigationBarHeight);
        setPadding(b.header,0,statusBarHeight,0,0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString(PARAM_ID);
            name = getArguments().getString(PARAM_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentOpenCategoryBinding.inflate(inflater, container, false);
        initListeners();
        setRecyclerTab();
        setViewPager();
        adapterTabLayout.setTabs(tabs);
        return b.getRoot();
    }

    private void initListeners() {
        b.back.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        b.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (adapterTabLayout.isClicked) {
                    adapterTabLayout.lastClicked = position;
                    adapterTabLayout.isClicked = false;
                    return;
                }

                tabs.get(position).setActive(true);
                tabs.get(adapterTabLayout.lastClicked).setActive(false);

                adapterTabLayout.notifyItemChanged(position);
                adapterTabLayout.notifyItemChanged(adapterTabLayout.lastClicked);

                adapterTabLayout.lastClicked = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setRecyclerTab() {
        adapterTabLayout = new AdapterTabLayout(getContext(), b.viewPager);
        b.recTab.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recTab.setAdapter(adapterTabLayout);
    }

    private void setViewPager() {
        ArrayList<FragmentPager> mFragment = new ArrayList<>();

        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID), ""));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID), ""));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID), ""));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID), ""));

        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);
    }
}