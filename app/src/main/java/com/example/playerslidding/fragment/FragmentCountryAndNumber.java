package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.databinding.FragmentCountryAndNumberBinding;
import com.example.playerslidding.interfaces.CountryClickListener;

public class FragmentCountryAndNumber extends Fragment implements CountryClickListener {
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
        setMargins(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    private void showKeyboard() {
        b.edtNumber.requestFocus();
        if (getActivity() == null) return;
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initListeners() {
        showKeyboard();
        b.btnLogin.setOnClickListener(v -> {
            b.btnLogin.setEnabled(false);
            addFragment(mainFragmentManager, R.id.container_login, FragmentSmsCode.newInstance());
            new Handler().postDelayed(() -> b.btnLogin.setEnabled(true), 200);
        });
        b.selectCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.selectCode.setEnabled(false);
                addFragment(mainFragmentManager, R.id.container_login, FragmentCountryCode.newInstance(FragmentCountryCode.TYPE_COUNTRY_CODE));
                new Handler().postDelayed(() -> b.selectCode.setEnabled(true), 200);
            }
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.btnLogin, R.color.accent, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.selectCode, R.color.hover, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtNumber, R.color.hover, 0, 10, false, 0);
    }

    @Override
    public void countryClick(String name, String code) {
        b.selectCode.setText(code);
    }
}