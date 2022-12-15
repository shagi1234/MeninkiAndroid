package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.databinding.FragmentLanguageBinding;

public class FragmentLanguage extends Fragment {
    private FragmentLanguageBinding b;

    public static FragmentLanguage newInstance() {
        FragmentLanguage fragment = new FragmentLanguage();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.getRoot(),0,statusBarHeight,0,navigationBarHeight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentLanguageBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.btnNext.setOnClickListener(v -> {
            b.btnNext.setEnabled(false);
            addFragment(mainFragmentManager, R.id.container_login, FragmentCountryAndNumber.newInstance());
            new Handler().postDelayed(() -> b.btnNext.setEnabled(true), 200);
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.btnNext, R.color.accent, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.selectLanguage, R.color.hover, 0, 10, false, 0);
    }
}