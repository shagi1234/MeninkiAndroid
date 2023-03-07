package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.api.request.RequestCreateShop;
import tm.store.meninki.databinding.FragmentNewShopBinding;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentNewShop extends Fragment {
    private FragmentNewShopBinding b;

    public static FragmentNewShop newInstance() {
        FragmentNewShop fragment = new FragmentNewShop();
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
        setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentNewShopBinding.inflate(inflater, container, false);
        new Handler().postDelayed(() -> {
            setBackgrounds();
            setSpinners();
        }, 100);

        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.goBasket.setOnClickListener(v -> {
            b.goBasket.setEnabled(false);

            createShop();

            new Handler().postDelayed(() -> b.goBasket.setEnabled(true), 200);
        });
    }

    private void createShop() {
        RequestCreateShop requestCreateShop = new RequestCreateShop();

        requestCreateShop.setCategories(new String[]{"ab45f412-d713-4bd4-b7d6-568a488a2381"});
        requestCreateShop.setUserId(Account.newInstance(getContext()).getPrefUserUUID());
        requestCreateShop.setDescriptionTm(b.about.getText().toString());
        requestCreateShop.setName(b.storeName.getText().toString());
        requestCreateShop.setPhoneNumber(b.phoneNumber.getText().toString());
        requestCreateShop.setEmail(b.extraContact.getText().toString());

        Call<UserProfile> call = StaticMethods.getApiHome().createShop(requestCreateShop);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                if (response.code() == 200 && response.body() != null) {
                    // onResponse
                    Log.e("TAG", "initListeners: " + response.code());


                } else {
                    // onError
                }

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                //onError
            }
        });
    }


    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.storeName, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.storeNameTwo, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.phoneNumber, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.phoneNumberTwo, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.phoneNumberThree, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.about, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.extraContact, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.extraContactTwo, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.website, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.saveButton, R.color.accent, 0, 4, false, 0);

    }

    private void setSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.spinnerStore.setAdapter(adapter);
    }
}