package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.getHeightNavIndicator;
import static com.example.playerslidding.utils.StaticMethods.hasNavBar;
import static com.example.playerslidding.utils.StaticMethods.hideSoftKeyboard;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMarginWithAnim;
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
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.activity.ActivityMain;
import com.example.playerslidding.databinding.FragmentLoginUserInformationBinding;
import com.example.playerslidding.shared.Account;
import com.example.playerslidding.utils.KeyboardHeightProvider;

public class FragmentLoginUserInfo extends Fragment implements KeyboardHeightProvider.KeyboardHeightListener {
    private FragmentLoginUserInformationBinding b;
    private KeyboardHeightProvider keyboardHeightProvider;
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
        new Handler().postDelayed(() -> setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight), 50);
        keyboardHeightProvider.start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = Account.newInstance(getContext());
        keyboardHeightProvider = new KeyboardHeightProvider(getContext(), getActivity().getWindowManager(), getActivity().getWindow().getDecorView(), this);
        hideSoftKeyboard(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardHeightProvider.dismiss();
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
                if (s.toString().trim().length() > 4) {
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
        setBackgroundDrawable(getContext(), b.btnLogin, R.color.background, R.color.accent, 10, false, 2);
        b.btnLogin.setTextColor(getResources().getColor(R.color.accent));
        b.btnLogin.setEnabled(false);
        setBackgroundDrawable(getContext(), b.edtName, R.color.hover, 0, 10, false, 0);
    }

    @Override
    public void onKeyboardHeightChanged(int height, boolean isLandscape) {
        if (getContext() == null) return;
        RelativeLayout.MarginLayoutParams layout = (RelativeLayout.MarginLayoutParams) b.layBottom.getLayoutParams();

        if (height > 0) {
            if (navigationBarHeight > 0) {
                setMarginWithAnim(b.layBottom, layout.bottomMargin, height);
            } else if (hasNavBar(getContext())) {
                setMarginWithAnim(b.layBottom, layout.bottomMargin, height + getHeightNavIndicator(getContext()));
            } else {
                setMarginWithAnim(b.layBottom, layout.bottomMargin, height);
            }
        } else {
            setMarginWithAnim(b.layBottom, layout.bottomMargin, height);
        }
    }
}