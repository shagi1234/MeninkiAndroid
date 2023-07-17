package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterOrders;
import tm.store.meninki.databinding.FragmentOrdersBinding;


public class FragmentOrders extends Fragment {
    FragmentOrdersBinding b;

    int orderCount;

    AdapterOrders adapterOrders;

    public FragmentOrders() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            setPadding(b.header, 0, statusBarHeight, 0, 0);
            setPadding(b.getRoot(), 0, 0, 0, navigationBarHeight);
        }, 50);

    }

    public static FragmentOrders newInstance() {
        FragmentOrders fragment = new FragmentOrders();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentOrdersBinding.inflate(inflater, container, false);
        setTab();
        setRecycler();
        return b.getRoot();
    }

    private void setRecycler() {
        adapterOrders = new AdapterOrders(getContext());
        b.rvOrders.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvOrders.setAdapter(adapterOrders);
    }

    private void setTab() {
        b.tabOrders.setTabRippleColor(null);
        b.tabOrders.addTab(b.tabOrders.newTab().setText(String.format("Новые (%s)", orderCount)));
        b.tabOrders.addTab(b.tabOrders.newTab().setText("Успешные" ));
        b.tabOrders.addTab(b.tabOrders.newTab().setText("Отказы"));

    }

}