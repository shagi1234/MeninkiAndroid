package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterMediaAddPost;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.FragmentAddProductBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.interfaces.OnChangeProductCharactersCount;
import tm.store.meninki.utils.Lists;

public class FragmentAddProduct extends Fragment implements OnBackPressedFragment, OnChangeProductCharactersCount {
    private FragmentAddProductBinding b;
    private AdapterMediaAddPost mediaAddPost;

    public static FragmentAddProduct newInstance() {
        FragmentAddProduct fragment = new FragmentAddProduct();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.layHeader, 0, statusBarHeight, 0, 0);
        setPadding(b.getRoot(), 0, 0, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentAddProductBinding.inflate(inflater, container, false);

        check();

        initListeners();
        setBackgrounds();
        setRecycler();


        return b.getRoot();
    }

    private void check() {
        if (getContext() == null) return;

        if (Lists.getPersonalCharacters().size() > 0) {
            b.wariants.setVisibility(View.VISIBLE);
            b.layPriceIfMany.setVisibility(View.VISIBLE);
            b.layPriceIf1.setVisibility(View.GONE);
        } else {
            b.wariants.setVisibility(View.GONE);
            b.layPriceIfMany.setVisibility(View.GONE);
            b.layPriceIf1.setVisibility(View.VISIBLE);
        }
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(v -> getActivity().onBackPressed());

        b.redactorCharacter.setOnClickListener(v -> {
            b.redactorCharacter.setEnabled(false);

            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentCharacterics.newInstance());

            new Handler().postDelayed(() -> b.redactorCharacter.setEnabled(true), 200);
        });

        b.redactorPrice.setOnClickListener(v -> {
            b.redactorPrice.setEnabled(false);

            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentRedactorPrice.newInstance());

            new Handler().postDelayed(() -> b.redactorPrice.setEnabled(true), 200);
        });

        b.goBasket.setOnClickListener(v -> getActivity().onBackPressed());

    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.title, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.desc, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.price, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.txtGoBasket, R.color.accent, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.oldPrice, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.desc, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.wariants, R.color.hover, 0, 10, 10, 0, 0, false, 0);
        setBackgroundDrawable(getContext(), b.prices, R.color.hover, 0, 10, 10, 0, 0, false, 0);
        setBackgroundDrawable(getContext(), b.redactorCharacter, R.color.hover, 0, 0, 0, 10, 10, false, 0);
        setBackgroundDrawable(getContext(), b.redactorPrice, R.color.hover, 0, 0, 0, 10, 10, false, 0);
    }

    private void setRecycler() {
        mediaAddPost = new AdapterMediaAddPost(getContext());
        b.recMedia.setLayoutManager(new GridLayoutManager(getContext(), 2));
        b.recMedia.setAdapter(mediaAddPost);
    }

    @Override
    public boolean onBackPressed() {
        SelectedMedia.getArrayList().clear();
        return false;
    }

    @Override
    public void onCountChange(int count) {
        check();
    }
}