package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.databinding.FragmentNewShopBinding;

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

        return b.getRoot();
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

        b.goBasket.setOnClickListener(v -> getActivity().onBackPressed());

    }

    private void setSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.spinnerStore.setAdapter(adapter);
    }
}