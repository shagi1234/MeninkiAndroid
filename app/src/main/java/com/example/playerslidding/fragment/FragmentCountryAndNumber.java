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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.databinding.FragmentCountryAndNumberBinding;

public class FragmentCountryAndNumber extends Fragment {
    private FragmentCountryAndNumberBinding b;

    public static FragmentCountryAndNumber newInstance() {
        FragmentCountryAndNumber fragment = new FragmentCountryAndNumber();
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
        b = FragmentCountryAndNumberBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    private void initListeners() {
        b.btnLogin.setOnClickListener(v -> {
            b.btnLogin.setEnabled(false);
            addFragment(mainFragmentManager, R.id.container_login, FragmentSmsCode.newInstance());
            new Handler().postDelayed(() -> b.btnLogin.setEnabled(true), 200);
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.btnLogin, R.color.accent, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.selectCode, R.color.hover, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtNumber, R.color.hover, 0, 10, false, 0);
    }
}