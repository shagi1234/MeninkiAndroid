package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

import com.example.playerslidding.adapter.AdapterViewPager;
import com.example.playerslidding.data.FragmentPager;
import com.example.playerslidding.databinding.FragmentMainBinding;
import com.example.playerslidding.interfaces.ChangeFlowPage;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentMain extends Fragment {
    private FragmentMainBinding b;
    private AdapterViewPager adapterFeedPager;

    public static FragmentMain newInstance() {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.main, 0, statusBarHeight, 0, 0);
        setMargins(b.frameFab, 20, 20, 20, navigationBarHeight + 20);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentMainBinding.inflate(inflater, container, false);
        setViewPager();
        initListeners();

        return b.getRoot();
    }

    private void initListeners() {
        b.frameMore.setOnClickListener(v -> {
            Fragment flow = mainFragmentManager.findFragmentByTag(FragmentFlow.class.getName());
            if (flow instanceof ChangeFlowPage) {
                ((ChangeFlowPage) flow).change();
            }
        });
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
        mFragment.add(new FragmentPager(FragmentFeed.newInstance(), "Лента"));
        mFragment.add(new FragmentPager(FragmentHome.newInstance(), "Домашняя"));
//        mFragment.add(new FragmentPager(FragmentNotice.newInstance(), "Объявления"));

        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);

    }
}