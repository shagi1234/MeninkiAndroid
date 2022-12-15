package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.activity.ActivityMain;
import com.example.playerslidding.databinding.FragmentLoginUserInformationBinding;
import com.example.playerslidding.shared.Account;

public class FragmentLoginUserInfo extends Fragment {
    private FragmentLoginUserInformationBinding b;
    private Account account;

    public static FragmentLoginUserInfo newInstance() {
        FragmentLoginUserInfo fragment = new FragmentLoginUserInfo();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.getRoot(),0,statusBarHeight,0,navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = Account.newInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentLoginUserInformationBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.btnLogin.setOnClickListener(v -> {
            b.btnLogin.setEnabled(false);
            startActivity(new Intent(getContext(), ActivityMain.class));
            getActivity().finish();
            account.saveUserIsLoggedIn();
            new Handler().postDelayed(() -> b.btnLogin.setEnabled(true), 200);
        });

        b.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() < 4) {
                    setBackgroundDrawable(getContext(), b.btnLogin, R.color.accent, 0, 10, false, 0);
                    b.btnLogin.setTextColor(getResources().getColor(R.color.background));
                    b.btnLogin.setEnabled(true);
                } else {
                    setBackgroundDrawable(getContext(), b.btnLogin, R.color.background, R.color.accent, 10, false, 2);
                    b.btnLogin.setTextColor(getResources().getColor(R.color.accent));
                    b.btnLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.btnLogin, R.color.accent, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtName, R.color.hover, 0, 10, false, 0);
    }
}