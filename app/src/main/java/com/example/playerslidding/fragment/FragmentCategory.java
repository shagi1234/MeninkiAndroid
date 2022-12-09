package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.adapter.AdapterViewPager;
import com.example.playerslidding.data.FragmentPager;
import com.example.playerslidding.databinding.FragmentCategoryBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;


public class FragmentCategory extends Fragment {
    private FragmentCategoryBinding b;

    public static FragmentCategory newInstance() {
        FragmentCategory fragment = new FragmentCategory();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.frameCtg, 0, statusBarHeight, 0, 0);
        setPadding(b.layUserInfo, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCategoryBinding.inflate(inflater, container, false);
        setBackgrounds();
        setViewPager();
        return b.getRoot();
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.icCart, R.color.background, 0, 4, false, 0);
    }


    private void setViewPager() {

        b.tabLayout.setupWithViewPager(b.viewPager);

        b.tabLayout.setTabRippleColor(null);

        b.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition(); // syntactic sugar
                b.viewPager.setCurrentItem(tabPosition, true);
                // Repeat of the code above -- tooltips reset themselves after any tab relayout, so I
                // have to constantly keep turning them off again.
                for (int i = 0; i < b.tabLayout.getTabCount(); i++) {
                    TooltipCompat.setTooltipText(Objects.requireNonNull(b.tabLayout.getTabAt(i)).view, null);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        b.viewPager.setOffscreenPageLimit(2);
        ArrayList<FragmentPager> mFragment = new ArrayList<>();
        mFragment.add(new FragmentPager(FragmentCategoryList.newInstance(), "Авто".toUpperCase()));
        mFragment.add(new FragmentPager(FragmentCategoryList.newInstance(), "Техника".toUpperCase()));
        mFragment.add(new FragmentPager(FragmentCategoryList.newInstance(), "Авто".toUpperCase()));
        mFragment.add(new FragmentPager(FragmentCategoryList.newInstance(), "Одежда".toUpperCase()));
        mFragment.add(new FragmentPager(FragmentCategoryList.newInstance(), "Техника".toUpperCase()));


        AdapterViewPager adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);

    }


}