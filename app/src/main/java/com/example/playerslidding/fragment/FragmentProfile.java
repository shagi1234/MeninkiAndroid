package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.databinding.FragmentProfileBinding;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding b;

    public static FragmentProfile newInstance(String param) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.layHeader, 0, statusBarHeight, 0, 0);
        setMargins(b.coordinator, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentProfileBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.layShops.setOnClickListener(v -> {
            b.layShops.setEnabled(false);
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentMyShops.newInstance());
            new Handler().postDelayed(() -> b.layShops.setEnabled(true), 200);
        });
        b.layBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.backgroundSearch, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtSearch, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.layUserData, R.color.white, R.color.hover, 10, false, 1);
        setBackgroundDrawable(getContext(), b.layShops, R.color.white, R.color.hover, 10, false, 1);
        setBackgroundDrawable(getContext(), b.layStatistics, R.color.hover, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.layPosts, R.color.hover, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.layMessages, R.color.hover, 0, 10, false, 0);
    }
}