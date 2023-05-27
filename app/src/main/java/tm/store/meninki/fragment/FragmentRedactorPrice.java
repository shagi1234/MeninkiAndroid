package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterRedactorPrice;
import tm.store.meninki.api.data.PersonalCharacterDto;
import tm.store.meninki.api.request.RequestUpdatePCh;
import tm.store.meninki.databinding.FragmentReadactorPriceBinding;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentRedactorPrice extends Fragment {
    private FragmentReadactorPriceBinding b;
    private String prodId;
    private AdapterRedactorPrice adapterRedactorPrice;
    private ArrayList<PersonalCharacterDto> personalCharacteristics;

    public static FragmentRedactorPrice newInstance(String prodId) {
        FragmentRedactorPrice fragment = new FragmentRedactorPrice();
        Bundle args = new Bundle();
        args.putString("prod_id", prodId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prodId = getArguments().getString("prod_id");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        StaticMethods.setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentReadactorPriceBinding.inflate(inflater, container, false);
        getOptions();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.goBasket.setOnClickListener(v -> updatePH());
    }

    private void updatePH() {
        RequestUpdatePCh r = new RequestUpdatePCh();
        r.setPersonalCharacteristics(personalCharacteristics);
        Call<Boolean> call = StaticMethods.getApiHome().updatePCh(Account.newInstance(getContext()).getAccessToken(), r);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.code() == 200 && response.body() != null && response.body()) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {

            }
        });

    }


    private void getOptions() {
        Call<ArrayList<PersonalCharacterDto>> call = StaticMethods.getApiHome().getPCh(Account.newInstance(getContext()).getAccessToken(), prodId);
        call.enqueue(new Callback<ArrayList<PersonalCharacterDto>>() {
            @Override
            public void onResponse(Call<ArrayList<PersonalCharacterDto>> call, Response<ArrayList<PersonalCharacterDto>> response) {
                if (response.code() == 200 && response.body() != null) {
                    personalCharacteristics = response.body();
                    setRecycler(personalCharacteristics);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PersonalCharacterDto>> call, Throwable t) {
                Toast.makeText(getContext(), "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecycler(ArrayList<PersonalCharacterDto> body) {
        setBackgroundDrawable(getContext(), b.txtGoBasket, R.color.accent, 0, 4, false, 0);

        adapterRedactorPrice = new AdapterRedactorPrice(getContext(), body);
        b.recMedia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL  , false));
        b.recMedia.setAdapter(adapterRedactorPrice);

    }
}