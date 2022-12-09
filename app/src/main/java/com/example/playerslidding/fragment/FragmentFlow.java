package com.example.playerslidding.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.playerslidding.adapter.AdapterViewPager;
import com.example.playerslidding.data.FragmentPager;
import com.example.playerslidding.databinding.FragmentFlowBinding;
import com.example.playerslidding.interfaces.ChangeFlowPage;
import com.example.playerslidding.interfaces.OnBackPressedFragment;

import java.util.ArrayList;

public class FragmentFlow extends Fragment implements ChangeFlowPage, OnBackPressedFragment {
    private FragmentFlowBinding b;
    private AdapterViewPager adapterFeedPager;

    public static FragmentFlow newInstance() {
        FragmentFlow fragment = new FragmentFlow();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentFlowBinding.inflate(inflater, container, false);
        setViewPager();
        b.viewPager.setCurrentItem(1);
        return b.getRoot();
    }

    private void setViewPager() {
        b.viewPager.setOffscreenPageLimit(2);
        ArrayList<FragmentPager> mFragment = new ArrayList<>();
        mFragment.add(new FragmentPager(FragmentCategory.newInstance(), ""));
        mFragment.add(new FragmentPager(FragmentMain.newInstance(), ""));
        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);
    }

    @Override
    public void change() {
        b.viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onBackPressed() {
        if (b.viewPager.getCurrentItem() == 0) {
            b.viewPager.setCurrentItem(1);
            return true;
        }
        return false;
    }
}