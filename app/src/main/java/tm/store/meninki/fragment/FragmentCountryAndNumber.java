package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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
import tm.store.meninki.R;
import tm.store.meninki.activity.ActivityMain;
import tm.store.meninki.api.ApiClient;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.response.DataCheckSms;
import tm.store.meninki.api.response.DataSendSms;
import tm.store.meninki.api.services.ServiceLogin;
import tm.store.meninki.databinding.FragmentCountryAndNumberBinding;
import tm.store.meninki.interfaces.CountryClickListener;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

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

    private void signInGoogle() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                // send data
                sendDataGoogle();
            } catch (ApiException e) {
                Log.e("TAG", "onActivityResult: " + e);
            }
        }
    }

    private void sendDataGoogle() {
        if (getContext() == null || getActivity() == null) return;
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct == null) return;
        JsonObject j = new JsonObject();
        j.addProperty("userId", acct.getId());
        j.addProperty("fullName", acct.getDisplayName());
        j.addProperty("email", acct.getEmail());
        Call<DataCheckSms> call = StaticMethods.getApiLogin().signInGoogle(j);
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

                startActivity(new Intent(getContext(), ActivityMain.class));
                getActivity().finish();

            }

            @Override
            public void onFailure(Throwable t) {
//                showNoConnection();
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
            sendSms();
            new Handler().postDelayed(() -> b.btnLogin.setEnabled(true), 200);
        });
        b.selectCode.setOnClickListener(v -> {
            b.selectCode.setEnabled(false);
            addFragment(mainFragmentManager, R.id.container_login, FragmentCountryCode.newInstance(FragmentCountryCode.TYPE_COUNTRY_CODE));
            new Handler().postDelayed(() -> b.selectCode.setEnabled(true), 200);
        });

        b.signInGoogle.setOnClickListener(v -> signInGoogle());

    }

    private void sendSms() {
        ServiceLogin serviceLogin = (ServiceLogin) ApiClient.createRequest(ServiceLogin.class);
        JsonObject j = new JsonObject();
        j.addProperty("phoneNumber", b.selectCode.getText().toString().trim().substring(1) + b.edtNumber.getText().toString().trim());

        Call<DataSendSms> call = serviceLogin.sendSms(j);
        call.enqueue(new RetrofitCallback<DataSendSms>() {
            @Override
            public void onResponse(DataSendSms response) {

                account.saveSendSmsId(response.getId());
                account.saveUserPhoneNumber(b.selectCode.getText().toString().trim().substring(1) + b.edtNumber.getText().toString().trim());

                addFragment(mainFragmentManager, R.id.container_login, FragmentSmsCode.newInstance());

            }

            @Override
            public void onFailure(Throwable t) {

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