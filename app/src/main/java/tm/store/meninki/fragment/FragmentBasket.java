package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.databinding.FragmentBasketBinding;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentBasketBinding.inflate(inflater, container, false);
        setBackgrounds();
        setRecycler();
        initListeners();
        return b.getRoot();
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.bgPayment, R.color.accent, 0, 10, false, 0);
    }

    private void initListeners() {
    }

    private void setRecycler() {
        adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_BASKET, -1);
//        b.recProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        b.recProducts.setAdapter(adapterGrid);
        adapterGrid.setStories(null);
    }

}