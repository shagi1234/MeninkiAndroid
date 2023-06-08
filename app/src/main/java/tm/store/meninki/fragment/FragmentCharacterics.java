package tm.store.meninki.fragment;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.Lists.getPersonalCharacters;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterPersonalCharacters;
import tm.store.meninki.api.data.PersonalCharacterDto;
import tm.store.meninki.data.CharactersDto;
import tm.store.meninki.databinding.FragmentCharactericsBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.interfaces.OnChangeProductCharactersCount;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.Option;
import tm.store.meninki.utils.StaticMethods;

public class FragmentCharacterics extends Fragment implements OnBackPressedFragment {
    private FragmentCharactericsBinding b;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private AdapterPersonalCharacters adapterPersonalCharacters;
    private String productId;
    private String TAG = "FragmentCharacteristics";
    private boolean isEmptyCharacteristics;

    public static FragmentCharacterics newInstance(String productId) {
        FragmentCharacterics fragment = new FragmentCharacterics();
        Bundle args = new Bundle();
        args.putString("prod_id", productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getString("prod_id");
        }
        isEmptyCharacteristics = getPersonalCharacters().getOptionTitles().size() == 0;
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
        b = FragmentCharactericsBinding.inflate(inflater, container, false);

        initBottomSheet();
        setRecycler();
        initListeners();
        return b.getRoot();
    }

    private void setRecycler() {
        adapterPersonalCharacters = new AdapterPersonalCharacters(getContext(), getPersonalCharacters(), productId);
        b.recMedia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recMedia.setAdapter(adapterPersonalCharacters);
    }

    private void initBottomSheet() {
        setBackgroundDrawable(getContext(), b.txtGoBasket, R.color.accent, 0, 50, false, 0);
        setBackgroundDrawable(getContext(), b.bgRedactorCharacter, R.color.contrast, 0, 50, false, 0);

        bottomSheetBehavior = BottomSheetBehavior.from(b.bottom.root);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (STATE_HIDDEN == newState) {
                    b.grayContainer.setVisibility(View.GONE);
                } else {
                    b.grayContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (Math.abs(slideOffset) < 0.5) {
                    b.grayContainer.setAlpha((float) (slideOffset / 2 + 0.5));
                }
            }
        });

    }

    private void initListeners() {
        b.backBtn.setOnClickListener(v -> getActivity().onBackPressed());

        b.addCharacterics.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        b.grayContainer.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));

        b.bottom.photo.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            getPersonalCharacters().getOptionTitles().add("Characteristic");
            getPersonalCharacters().getOptions().add(new ArrayList<>());
            getPersonalCharacters().getOptionTypes().add(Option.CHARACTER_IMAGE);
            adapterPersonalCharacters.insert(adapterPersonalCharacters.getItemCount() - 1);
        });

        b.bottom.txt.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            getPersonalCharacters().getOptionTitles().add("Characteristic");
            getPersonalCharacters().getOptions().add(new ArrayList<>());
            getPersonalCharacters().getOptionTypes().add(Option.CHARACTER_TEXT);
            adapterPersonalCharacters.insert(adapterPersonalCharacters.getItemCount() - 1);
        });

        b.goBasket.setOnClickListener(v -> createOrUpdateOption());
    }

    private void createOrUpdateOption() {
        CharactersDto charactersDto = new CharactersDto();
        charactersDto.setOptions(getPersonalCharacters().getOptions());
        charactersDto.setOptionTitles(getPersonalCharacters().getOptionTitles());
        charactersDto.setOptionTypes(null);

        Call<ArrayList<PersonalCharacterDto>> call;

        if (isEmptyCharacteristics)
            call = StaticMethods.getApiHome().createOption( charactersDto);
        else
            call = StaticMethods.getApiHome().updateOption( charactersDto);

        call.enqueue(new Callback<ArrayList<PersonalCharacterDto>>() {
            @Override
            public void onResponse(Call<ArrayList<PersonalCharacterDto>> call, Response<ArrayList<PersonalCharacterDto>> response) {
                if (response.code() == 200 && response.body() != null) {
                    Log.e(TAG, "onResponse: ");
                    return;
                }

            }

            @Override
            public void onFailure(Call<ArrayList<PersonalCharacterDto>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = mainFragmentManager.findFragmentByTag(FragmentAddProduct.class.getName());
        if (fragment instanceof OnChangeProductCharactersCount) {
            ((OnChangeProductCharactersCount) fragment).onCountChange(getPersonalCharacters().getOptions().size());
        }

        if (bottomSheetBehavior.getState() != STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return true;
        }

        return false;
    }
}