package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.playerslidding.R;
import com.example.playerslidding.databinding.FragmentNewShopBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentNewShop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNewShop extends Fragment {


    private FragmentNewShopBinding b;
    private Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentNewShop() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentNewShop newInstance() {
        FragmentNewShop fragment = new FragmentNewShop();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        if (getArguments() != null) {

        }
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
        b = FragmentNewShopBinding.inflate(inflater,container,false);
        setBackgrounds();
        setSpinners();
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

    }

    private void setSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.spinnerStore.setAdapter(adapter);
    }
}