package tm.store.meninki.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import tm.store.meninki.R;
import tm.store.meninki.databinding.FragmentAdvertisementsBinding;

public class FragmentAdvertisements extends Fragment {
    private FragmentAdvertisementsBinding b;

    public static FragmentAdvertisements newInstance() {
        FragmentAdvertisements fragment = new FragmentAdvertisements();
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
        b = FragmentAdvertisementsBinding.inflate(inflater, container, false);
        gatAllAdvertisements();
        setTabCategories();
        return b.getRoot();
    }

    private void setTabCategories() {
        b.tabCategories.setTabRippleColor(null);
//        b.tabCategories.addTab(b.tabCategories.newTab().setText(""));
//
//        Objects.requireNonNull(b.tabCategories.getTabAt(0)).setIcon(R.drawable.ic_categories_tab);
//        b.tabCategories.getTabAt(0).getIcon().setTint(getResources().getColor(R.color.bg));
        b.tabCategories.addTab(b.tabCategories.newTab().setText("Tab1"));
        b.tabCategories.addTab(b.tabCategories.newTab().setText("Tab2"));
        b.tabCategories.addTab(b.tabCategories.newTab().setText("Tab3"));

        b.tabCategories.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("TAG", "onTabSelected: " + tab.getId());
                if (tab.getPosition() == 0) {
                    int tintColor = getResources().getColor(R.color.white);
                    tab.getIcon().setTint(tintColor);
                } else
                    b.tabCategories.getTabAt(0).getIcon().setTint(getResources().getColor(R.color.contrast));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void gatAllAdvertisements() {

    }
}