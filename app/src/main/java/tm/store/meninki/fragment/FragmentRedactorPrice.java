package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterRedactorPrice;
import tm.store.meninki.databinding.FragmentReadactorPriceBinding;

public class FragmentRedactorPrice extends Fragment {
    private FragmentReadactorPriceBinding b;

    public static FragmentRedactorPrice newInstance() {
        FragmentRedactorPrice fragment = new FragmentRedactorPrice();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentReadactorPriceBinding.inflate(inflater, container, false);
        setRecycler();
        return b.getRoot();
    }

    private void setRecycler() {
        setBackgroundDrawable(getContext(), b.txtGoBasket, R.color.accent, 0, 4, false, 0);

        AdapterRedactorPrice adapterRedactorPrice = new AdapterRedactorPrice(getContext());
        b.recMedia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recMedia.setAdapter(adapterRedactorPrice);

        adapterRedactorPrice.setImageUrl(null);
    }
}