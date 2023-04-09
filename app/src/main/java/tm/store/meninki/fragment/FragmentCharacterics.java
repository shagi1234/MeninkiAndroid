package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPaddingWithHandler;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterPersonalCharacters;
import tm.store.meninki.data.CharactersDto;
import tm.store.meninki.databinding.FragmentCharactericsBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.interfaces.OnChangeProductCharactersCount;
import tm.store.meninki.utils.Lists;
import tm.store.meninki.utils.StaticMethods;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class FragmentCharacterics extends Fragment implements OnBackPressedFragment {
    private FragmentCharactericsBinding b;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private AdapterPersonalCharacters adapterPersonalCharacters;
    public static int COUNT_VARIANTS;

    public static FragmentCharacterics newInstance() {
        FragmentCharacterics fragment = new FragmentCharacterics();
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
        StaticMethods.setPaddingWithHandler(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
//        setMargins(b.layBtn, dpToPx(20, getContext()), 0, dpToPx(20, getContext()), navigationBarHeight);
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
        adapterPersonalCharacters = new AdapterPersonalCharacters(getContext(), Lists.getPersonalCharacters());
        b.recMedia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recMedia.setAdapter(adapterPersonalCharacters);
    }

    private void initBottomSheet() {
        setBackgroundDrawable(getContext(), b.txtGoBasket, R.color.accent, 0, 4, false, 0);

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
            Lists.getPersonalCharacters().add(new CharactersDto("ХАРАКТЕРИСТИКА", 0, AdapterPersonalCharacters.CHARACTER_IMAGE));
            adapterPersonalCharacters.insert(Lists.getPersonalCharacters().size() - 1);
        });

        b.bottom.txt.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            Lists.getPersonalCharacters().add(new CharactersDto("ХАРАКТЕРИСТИКА", 0, AdapterPersonalCharacters.CHARACTER_TEXT));
            adapterPersonalCharacters.insert(Lists.getPersonalCharacters().size() - 1);
        });

        b.bottom.txtAndPhoto.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            Lists.getPersonalCharacters().add(new CharactersDto("ХАРАКТЕРИСТИКА", 0, AdapterPersonalCharacters.CHARACTER_COLOR));
            adapterPersonalCharacters.insert(Lists.getPersonalCharacters().size() - 1);
        });

        b.goBasket.setOnClickListener(v -> getActivity().onBackPressed());
    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = mainFragmentManager.findFragmentByTag(FragmentAddProduct.class.getName());
        if (fragment instanceof OnChangeProductCharactersCount) {
            ((OnChangeProductCharactersCount) fragment).onCountChange(Lists.getPersonalCharacters().size());
        }

        if (bottomSheetBehavior.getState() != STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return true;
        }

        return false;
    }
}