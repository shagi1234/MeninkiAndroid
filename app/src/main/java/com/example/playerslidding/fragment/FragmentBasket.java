package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.playerslidding.adapter.AdapterGrid;
import com.example.playerslidding.data.SelectedMedia;
import com.example.playerslidding.databinding.FragmentBasketBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBasket#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBasket extends Fragment {
    private FragmentBasketBinding b;
    private AdapterGrid adapterGrid;

    public static FragmentBasket newInstance() {
        FragmentBasket fragment = new FragmentBasket();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.layHeader, statusBarHeight, 0, 0, 0);
        setMargins(b.bottomConstraint, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentBasketBinding.inflate(inflater, container, false);
        setRecycler();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setRecycler() {
        adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_BASKET);
        b.recProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recProducts.setAdapter(adapterGrid);
        adapterGrid.setStories(null);
    }

}