package com.example.playerslidding.fragment;

import static androidx.constraintlayout.widget.ConstraintSet.PARENT_ID;
import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.StaticMethods.getWindowWidth;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.slidingX;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.playerslidding.R;
import com.example.playerslidding.adapter.AdapterViewPager;
import com.example.playerslidding.data.FragmentPager;
import com.example.playerslidding.databinding.FragmentMainBinding;
import com.example.playerslidding.interfaces.ChangeFlowPage;

import java.util.ArrayList;

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
        initComponents();
        setViewPager();
        initListeners();

        return b.getRoot();
    }

    private void initComponents() {
        b.swipeLayout.setLayoutParams(new FrameLayout.LayoutParams(getWindowWidth(getActivity()) * 2, ViewGroup.LayoutParams.WRAP_CONTENT));
        ConstraintLayout.LayoutParams c1 = new ConstraintLayout.LayoutParams(getWindowWidth(getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT);
        c1.startToStart = PARENT_ID;
        c1.topToTop = PARENT_ID;
        c1.bottomToBottom = PARENT_ID;

        b.layFeed.setLayoutParams(c1);
        ConstraintLayout.LayoutParams c2 = new ConstraintLayout.LayoutParams(getWindowWidth(getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT);
        c2.startToEnd = b.layFeed.getId();
        c2.topToTop = PARENT_ID;
        c2.bottomToBottom = PARENT_ID;
        b.layHome.setLayoutParams(c2);
    }

    private void initListeners() {
        b.frameMore.setOnClickListener(v -> {
            Fragment flow = mainFragmentManager.findFragmentById(R.id.fragment_container_main);
            if (flow instanceof ChangeFlowPage) {
                ((ChangeFlowPage) flow).change();
            }
        });

        b.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("TAG_test_scroll", "onPageScrolled: " + "positionOffset" + positionOffset + "\n" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

                if (position != 0) {
                    slidingX(b.swipeLayout, -getWindowWidth(getActivity()));
                } else {
                    slidingX(b.swipeLayout, 0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setViewPager() {

        b.viewPager.setOffscreenPageLimit(2);
        ArrayList<FragmentPager> mFragment = new ArrayList<>();
        mFragment.add(new FragmentPager(FragmentFeed.newInstance(), "Лента" + "\n" + "Новые 13"));
        mFragment.add(new FragmentPager(FragmentHome.newInstance(), "Домашняя" + "\n" + "Новые 11"));

        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);

    }
}