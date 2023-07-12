package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import retrofit2.Call;
import tm.store.meninki.R;
import tm.store.meninki.api.ApiClient;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.response.DataCheckSms;
import tm.store.meninki.api.services.ServiceLogin;
import tm.store.meninki.databinding.FragmentSmsCodeBinding;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentSmsCode extends Fragment {
    private FragmentSmsCodeBinding b;
    private CountDownTimer countDownTimer;
    private Account account;
    String number;

    public static FragmentSmsCode newInstance(String number) {
        FragmentSmsCode fragment = new FragmentSmsCode();
        Bundle args = new Bundle();
        args.putString("_number", number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            number = getArguments().getString("_number");
        }
        account = Account.newInstance(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentSmsCodeBinding.inflate(inflater, container, false);
        b.phoneNum.setText(String.format("+993 %s", number));
        initListeners();
        countDownTime(120, b.btnResendCode);
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        StaticMethods.setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    private void initListeners() {
        showKeyboard();
        b.edtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 4) {
                    checkSmsCode(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkSmsCode(String trim) {
        ServiceLogin serviceLogin = (ServiceLogin) ApiClient.createRequest(ServiceLogin.class);
        JsonObject j = new JsonObject();
        j.addProperty("id", account.getSendSmsId());
        j.addProperty("phoneConfirmationCode", trim);

        Call<DataCheckSms> call = serviceLogin.checkSms(j);
        call.enqueue(new RetrofitCallback<DataCheckSms>() {
            @Override
            public void onResponse(DataCheckSms response) {
                account.saveRefreshToken(response.getRefreshToken());
                account.saveAccessToken(response.getAccessToken());
                account.saveValidToToken(response.getValidTo());
                account.saveUserUUID(response.getUserId());

                addFragment(mainFragmentManager, R.id.container_login, FragmentLoginUserInfo.newInstance(false));
                b.edtCode.setText("");
            }

            @Override
            public void onFailure(Throwable t) {
                shake();
            }
        });

    }

    private void shake() {
        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

        b.edtCode.startAnimation(shake);
        Vibrator v = (Vibrator) getContext().getSystemService(getContext().VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(300);
        }
    }


    private void showKeyboard() {
        b.edtCode.requestFocus();
        if (getActivity() == null) return;
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void countDownTime(int Seconds, final TextView tv) {
        if (getActivity() == null) return;
        countDownTimer = new CountDownTimer(Seconds * 1000L + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (getActivity() == null) return;
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tv.setText(getActivity().getResources().getString(R.string.repeat_after)
                        + " " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                if (getActivity() == null) return;
                b.btnResendCode.setEnabled(true);
                tv.setText(getActivity().getResources().getString(R.string.send_again));

                b.btnResendCode.setOnClickListener(view -> {
                    b.btnResendCode.setEnabled(false);

                    // again

                    countDownTime(120, b.btnResendCode);
                });

            }
        }.start();
    }
}