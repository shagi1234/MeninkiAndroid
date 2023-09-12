package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.getHeightNavIndicator;
import static tm.store.meninki.utils.StaticMethods.hasNavBar;
import static tm.store.meninki.utils.StaticMethods.hideSoftKeyboard;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMarginWithAnim;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonObject;

import retrofit2.Call;
import tm.store.meninki.R;
import tm.store.meninki.activity.ActivityMain;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.response.DataCheckSms;
import tm.store.meninki.databinding.FragmentLoginUserInformationBinding;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.KeyboardHeightProvider;
import tm.store.meninki.utils.StaticMethods;

public class FragmentLoginUserInfo extends Fragment implements KeyboardHeightProvider.KeyboardHeightListener, TextWatcher {
    private FragmentLoginUserInformationBinding b;
    private KeyboardHeightProvider keyboardHeightProvider;
    private Account account;
    private boolean signedInGoogle;

    public static FragmentLoginUserInfo newInstance(boolean isSignedInGoogle) {
        FragmentLoginUserInfo fragment = new FragmentLoginUserInfo();
        Bundle args = new Bundle();
        args.putBoolean("isSignedInGoogle", isSignedInGoogle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> StaticMethods.setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight), 50);
        keyboardHeightProvider.start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null) return;
        account = Account.newInstance(getContext());
        keyboardHeightProvider = new KeyboardHeightProvider(getContext(), getActivity().getWindowManager(), getActivity().getWindow().getDecorView(), this);
        hideSoftKeyboard(getActivity());

        if (getArguments() != null) {
            signedInGoogle = getArguments().getBoolean("isSignedInGoogle");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardHeightProvider.dismiss();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentLoginUserInformationBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.nextBtn.setOnClickListener(v -> {
            if (getActivity() == null) return;
            b.nextBtn.setEnabled(false);
            b.nextBtn.setAlpha(0.7f);
            b.btnProgress.setVisibility(View.VISIBLE);

            if (signedInGoogle) {
                sendDataGoogle();
                return;
            }
            createUser();
        });


        b.edtName.addTextChangedListener(this);
        b.edtUsername.addTextChangedListener(this);
    }

    private void sendDataGoogle() {
        if (getContext() == null || getActivity() == null) return;
        if (b.edtUsername.getText().toString().trim().equals("")) {
            Toast.makeText(getContext(), "Please write username", Toast.LENGTH_SHORT).show();
            return;
        }

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct == null) return;

        JsonObject j = new JsonObject();
        j.addProperty("userId", acct.getId());
        j.addProperty("fullName", acct.getDisplayName());
        j.addProperty("userName", b.edtUsername.getText().toString().trim());
        j.addProperty("email", acct.getEmail());
        Call<DataCheckSms> call = StaticMethods.getApiLogin().registrationByGoogle(j);
        call.enqueue(new RetrofitCallback<DataCheckSms>() {
            @Override
            public void onResponse(DataCheckSms response) {
                if (getContext() == null || getActivity() == null) return;

                account.saveAccessToken(response.getAccessToken());
                account.saveRefreshToken(response.getRefreshToken());
                account.saveValidToToken(response.getValidTo());
                account.saveUserUUID(response.getUserId());

                //go next activity
                account.saveUserIsLoggedIn();

                goNextActivity();

            }

            @Override
            public void onFailure(Throwable t) {
//                showNoConnection();
            }
        });

    }
    private void createUser() {
        JsonObject j = new JsonObject();
        j.addProperty("id", account.getPrefUserUUID());
        j.addProperty("firstName", b.edtName.getText().toString().trim());
        j.addProperty("UserName", b.edtUsername.getText().toString().trim());
        j.addProperty("phoneNumber", account.getUserPhoneNumber());

        Call<Boolean> call = StaticMethods.getApiLogin().updateUser(j);
        call.enqueue(new RetrofitCallback<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                if (!response) return;

                account.saveRegisterName(b.edtName.getText().toString().trim());
                account.saveUserIsLoggedIn();
                goNextActivity();
                b.nextBtn.setAlpha(1);
                b.btnProgress.setVisibility(View.GONE);
                new Handler().postDelayed(() -> b.nextBtn.setEnabled(true), 200);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                b.nextBtn.setAlpha(1);
                b.btnProgress.setVisibility(View.GONE);
                new Handler().postDelayed(() -> b.nextBtn.setEnabled(true), 200);

            }
        });
    }

    private void goNextActivity() {
        if (getActivity() == null) return;
        hideSoftKeyboard(getActivity());

        new Handler().postDelayed(() -> {
            startActivity(new Intent(getContext(), ActivityMain.class));
            getActivity().finish();
        }, 200);;
    }

    private void setNextBtnEnabled() {
        if (isEnabled()) {
            setBackgroundDrawable(getContext(), b.nextBtn, R.color.accent, 0, 10, false, 0);
            b.nextBtn.setTextColor(getResources().getColor(R.color.bg));
            b.nextBtn.setEnabled(true);
        } else {
            setBackgroundDrawable(getContext(), b.nextBtn, R.color.bg, R.color.accent, 10, false, 2);
            b.nextBtn.setTextColor(getResources().getColor(R.color.accent));
            b.nextBtn.setEnabled(false);
        }
    }

    private boolean isEnabled() {
        return b.edtName.toString().trim().length() > 4 && b.edtUsername.getText().toString().trim().length() > 4;
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.edtName, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtUsername, R.color.low_contrast, 0, 10, false, 0);
        setNextBtnEnabled();

        if (signedInGoogle) {
            b.edtName.setVisibility(View.GONE);
            b.hintName.setVisibility(View.GONE);
        } else {
            b.edtName.setVisibility(View.VISIBLE);
            b.hintName.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        setNextBtnEnabled();
    }
}