package tm.store.meninki.fragment;

import static tm.store.meninki.api.enums.CardType.product;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.data.FragmentPager;
import tm.store.meninki.databinding.FragmentOpenCategoryBinding;
import tm.store.meninki.utils.StaticMethods;

public class FragmentOpenCategory extends Fragment {
    private FragmentOpenCategoryBinding b;
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ID = "uuid";
    private String uuid;
    private String name;
    private AdapterViewPager adapterFeedPager;

    public static FragmentOpenCategory newInstance(String name, String id) {
        FragmentOpenCategory fragment = new FragmentOpenCategory();
        Bundle args = new Bundle();
        args.putString(PARAM_ID, id);
        args.putString(PARAM_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        StaticMethods.setPadding(b.viewPager, 0, 0, 0, navigationBarHeight);
        StaticMethods.setPadding(b.header, dpToPx(10, getContext()), statusBarHeight, dpToPx(10, getContext()), 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString(PARAM_ID);
            name = getArguments().getString(PARAM_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentOpenCategoryBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler().postDelayed(() -> {
            initListeners();
            setViewPager();
        }, 200);


    }

    private void initListeners() {
        b.headerTxt.setText(name);

        b.back.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void setViewPager() {
        b.tabLayout.setupWithViewPager(b.viewPager);

        b.tabLayout.setTabRippleColor(null);

        b.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition(); // syntactic sugar
                b.viewPager.setCurrentItem(tabPosition, true);
                // Repeat of the code above -- tooltips reset themselves after any tab relayout, so I
                // have to constantly keep turning them off again.
                for (int i = 0; i < b.tabLayout.getTabCount(); i++) {
                    TooltipCompat.setTooltipText(Objects.requireNonNull(b.tabLayout.getTabAt(i)).view, null);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ArrayList<FragmentPager> mFragment = new ArrayList<>();

        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, FragmentListGrid.CATEGORY, -1, new String[]{uuid}, new int[]{product}), ""));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, FragmentListGrid.CATEGORY, -1, new String[]{uuid}, new int[]{product}), ""));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, FragmentListGrid.CATEGORY, -1, new String[]{uuid}, new int[]{product}), ""));

        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);
    }
}