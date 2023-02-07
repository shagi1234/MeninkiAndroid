package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.playerslidding.R;
import com.example.playerslidding.adapter.AdapterMediaAddPost;
import com.example.playerslidding.data.SelectedMedia;
import com.example.playerslidding.databinding.FragmentAddProductBinding;
import com.example.playerslidding.interfaces.OnBackPressedFragment;
import com.example.playerslidding.interfaces.OnChangeProductCharactersCount;
import com.example.playerslidding.utils.Lists;

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