package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.hideSoftKeyboard;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.activity.ActivityMain;
import tm.store.meninki.api.ApiClient;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.response.DataCheckSms;
import tm.store.meninki.api.data.response.DataSendSms;
import tm.store.meninki.api.services.ServiceLogin;
import tm.store.meninki.databinding.FragmentCountryAndNumberBinding;
import tm.store.meninki.interfaces.CountryClickListener;
import tm.store.meninki.shared.Account;

public class FragmentCountryAndNumber extends Fragment implements CountryClickListener {
    private FragmentCountryAndNumberBinding b;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private GoogleSignInAccount acct;
    private Account account;

    public static FragmentCountryAndNumber newInstance() {
        FragmentCountryAndNumber fragment = new FragmentCountryAndNumber();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() == null) return;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getContext(), gso);
        acct = GoogleSignIn.getLastSignedInAccount(getContext());
        account = Account.newInstance(getContext());

        if (acct != null) {
            //next page
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCountryAndNumberBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setMargins(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight), 50);
    }

    private void openGoogleAccountPicker() {
        // Start the sign-in flow
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 123);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Get the signed-in account
                task.getResult(ApiException.class);
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                // send data
                checkUserAlreadyExist(acct.getId());

            } catch (ApiException e) {
                Log.e("TAG", "onActivityResult: " + e);
            }
        }
    }

    private void checkUserAlreadyExist(String googleId) {
        ServiceLogin serviceLogin = (ServiceLogin) ApiClient.createRequest(ServiceLogin.class);
        JsonObject j = new JsonObject();
        j.addProperty("phoneNumber", b.selectCode.getText().toString().trim().substring(1) + b.edtNumber.getText().toString().trim());
        j.addProperty("googlrId", googleId);

        Call<Boolean> call = serviceLogin.checkUserIsAlreadyExist(j);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (googleId == null) {
                    sendSms(response.body() != null && Boolean.TRUE.equals(response.body()));
                    return;
                }
                if (response.body() != null && Boolean.TRUE.equals(response.body())) {
                    signInGoogle(googleId);
                } else {
                    registrationByGoogle();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private void registrationByGoogle() {
        addFragment(mainFragmentManager, R.id.container_login, FragmentLoginUserInfo.newInstance(true));

    }

    private void signInGoogle(String googleId) {
        ServiceLogin serviceLogin = (ServiceLogin) ApiClient.createRequest(ServiceLogin.class);
        JsonObject j = new JsonObject();
        j.addProperty("googleId", googleId);

        Call<DataCheckSms> call = serviceLogin.loginByGoogle(j);
        call.enqueue(new Callback<DataCheckSms>() {
            @Override
            public void onResponse(Call<DataCheckSms> call, Response<DataCheckSms> response) {
                if (response.body() == null || !response.isSuccessful()) return;

                account.saveAccessToken(response.body().getAccessToken());
                account.saveRefreshToken(response.body().getRefreshToken());
                account.saveValidToToken(response.body().getValidTo());
                account.saveUserUUID(response.body().getUserId());

                account.saveUserIsLoggedIn();
                hideSoftKeyboard(getActivity());

                new Handler().postDelayed(() -> {
                    startActivity(new Intent(getContext(), ActivityMain.class));
                    getActivity().finish();
                }, 200);

            }

            @Override
            public void onFailure(Call<DataCheckSms> call, Throwable t) {

            }
        });
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
            b.btnLogin.setAlpha(0.7f);
            b.btnProgress.setVisibility(View.VISIBLE);
            checkUserAlreadyExist(null);
        });

        b.selectCode.setOnClickListener(v -> {
            b.selectCode.setEnabled(false);
            addFragment(mainFragmentManager, R.id.container_login, FragmentCountryCode.newInstance(FragmentCountryCode.TYPE_COUNTRY_CODE));
            new Handler().postDelayed(() -> b.selectCode.setEnabled(true), 200);
        });

        b.signInGoogle.setOnClickListener(v -> openGoogleAccountPicker());

        b.edtNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setNextBtnEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        b.clickLang.setOnClickListener(v -> {
            b.clickLang.setEnabled(false);
            addFragment(mainFragmentManager, R.id.container_login, FragmentCountryCode.newInstance(FragmentCountryCode.TYPE_LANGUAGE));
            new Handler().postDelayed(() -> b.clickLang.setEnabled(true), 200);
        });

    }

    private void sendSms(boolean isAlreadyExist) {
        ServiceLogin serviceLogin = (ServiceLogin) ApiClient.createRequest(ServiceLogin.class);
        JsonObject j = new JsonObject();
        j.addProperty("phoneNumber", this.b.selectCode.getText().toString().trim().substring(1) + this.b.edtNumber.getText().toString().trim());

        Call<DataSendSms> call = isAlreadyExist ? serviceLogin.loginByPhone(j) : serviceLogin.registrationByNumber(j);
        call.enqueue(new Callback<DataSendSms>() {
            @Override
            public void onResponse(@NonNull Call<DataSendSms> call, @NonNull Response<DataSendSms> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                account.saveSendSmsId(response.body().getId());
                account.saveUserPhoneNumber(FragmentCountryAndNumber.this.b.selectCode.getText().toString().trim().substring(1) + FragmentCountryAndNumber.this.b.edtNumber.getText().toString().trim());

                addFragment(mainFragmentManager, R.id.container_login, FragmentSmsCode.newInstance(FragmentCountryAndNumber.this.b.edtNumber.getText().toString(), isAlreadyExist));
                FragmentCountryAndNumber.this.b.btnLogin.setAlpha(1);
                FragmentCountryAndNumber.this.b.btnProgress.setVisibility(View.GONE);
                new Handler().postDelayed(() -> FragmentCountryAndNumber.this.b.btnLogin.setEnabled(true), 200);
            }

            @Override
            public void onFailure(Call<DataSendSms> call, Throwable t) {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                FragmentCountryAndNumber.this.b.btnLogin.setAlpha(1);
                FragmentCountryAndNumber.this.b.btnProgress.setVisibility(View.GONE);
                new Handler().postDelayed(() -> FragmentCountryAndNumber.this.b.btnLogin.setEnabled(true), 200);
            }
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.selectCode, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.layEdtPhone, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtNumber, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.signInGoogle, R.color.low_contrast, 0, 50, false, 0);

        setNextBtnEnabled();
    }

    private void setNextBtnEnabled() {
        if (getActivity() == null) return;
        if (checkEnabled()) {
//            setBackgroundDrawable(getContext(), b.btnLogin, R.color.accent, 0, 50, false, 0);
            b.btnLogin.setTextColor(getActivity().getResources().getColor(R.color.bg));
            b.btnLogin.setEnabled(true);
            return;
        }
        b.btnLogin.setEnabled(false);
//        setBackgroundDrawable(getContext(), b.btnLogin, R.color.on_bg_ls, 0, 50, false, 0);
        b.btnLogin.setTextColor(getActivity().getResources().getColor(R.color.neutral_dark));

    }

    private boolean checkEnabled() {
        return b.edtNumber.getText().toString().trim().length() == 8;
    }

    @Override
    public void countryClick(String name, String code) {
        b.selectCode.setText(code);
    }
}