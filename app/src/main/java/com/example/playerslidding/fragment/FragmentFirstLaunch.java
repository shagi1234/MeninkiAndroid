package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.dpToPx;
import static com.example.playerslidding.utils.StaticMethods.initSystemUIViewListeners;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.databinding.FragmentFirstLaunchBinding;

public class FragmentFirstLaunch extends Fragment {
    private FragmentFirstLaunchBinding b;

    public static FragmentFirstLaunch newInstance() {
        FragmentFirstLaunch fragment = new FragmentFirstLaunch();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentFirstLaunchBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.getRoot(),0,statusBarHeight,0,navigationBarHeight);
    }

    private void initListeners() {
        b.nextBtn.setOnClickListener(v -> {
            b.nextBtn.setEnabled(false);
            addFragment(mainFragmentManager, R.id.container_login, FragmentLanguage.newInstance());
            new Handler().postDelayed(() -> b.nextBtn.setEnabled(true), 200);
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.nextBtn, R.color.accent, 0, 10, false, 0);
    }
}