package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setPadding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.example.playerslidding.R;
import com.example.playerslidding.adapter.AdapterStore;
import com.example.playerslidding.adapter.AdapterTabLayout;
import com.example.playerslidding.adapter.AdapterViewPager;
import com.example.playerslidding.data.FragmentPager;
import com.example.playerslidding.data.TabItemCustom;
import com.example.playerslidding.databinding.FragmentHomeBinding;
import com.example.playerslidding.utils.StoreList;

import java.util.ArrayList;

public class FragmentFeed extends Fragment {
    private FragmentHomeBinding b;
    private AdapterViewPager adapterFeedPager;
    private AdapterStore adapterStore;
    private AdapterTabLayout adapterTabLayout;
    private ArrayList<TabItemCustom> tabs;

    public static FragmentFeed newInstance() {
        FragmentFeed fragment = new FragmentFeed();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabs = StoreList.getTabs();
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.containerProfileId, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentHomeBinding.inflate(inflater, container, false);

        setBackground();
        setRecycler();
        setRecyclerTab();
        setViewPager();
        initListeners();

        adapterStore.setStories(StoreList.getStoreDTOS());
        adapterTabLayout.setTabs(tabs);

        return b.getRoot();
    }

    private void initListeners() {
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

        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID), "Домашняя"));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID), "Лента"));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID), "Объявления"));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID), "Объявления"));

        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.backgroundSearch, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtSearch, R.color.white, 0, 10, false, 0);
    }

    private void setRecycler() {
        adapterStore = new AdapterStore(getContext(), getActivity());
        b.recStores.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recStores.setAdapter(adapterStore);
    }

}