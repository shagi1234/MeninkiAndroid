package tm.store.meninki.fragment;

import static tm.store.meninki.api.enums.CardType.product;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setPaddingWithHandler;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import tm.store.meninki.adapter.AdapterTabLayout;
import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.data.FragmentPager;
import tm.store.meninki.data.TabItemCustom;
import tm.store.meninki.databinding.FragmentOpenCategoryBinding;
import tm.store.meninki.utils.StaticMethods;

public class FragmentOpenCategory extends Fragment {
    private FragmentOpenCategoryBinding b;
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ID = "uuid";
    private String uuid;
    private String name;
    private AdapterTabLayout adapterTabLayout;
    private AdapterViewPager adapterFeedPager;
    private ArrayList<TabItemCustom> tabs = new ArrayList<>();

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
        StaticMethods.setPaddingWithHandler(b.viewPager, 0, 0, 0, navigationBarHeight);
        StaticMethods.setPaddingWithHandler(b.header, dpToPx(10, getContext()), statusBarHeight, dpToPx(10, getContext()), 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString(PARAM_ID);
            name = getArguments().getString(PARAM_NAME);
        }
        tabs.add(new TabItemCustom("Товары", true));
        tabs.add(new TabItemCustom("Обзоры", false));
        tabs.add(new TabItemCustom("Услуги", false));
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
            setRecyclerTab();
            setViewPager();
            adapterTabLayout.setTabs(tabs);
        }, 200);


    }

    private void initListeners() {
        b.headerTxt.setText(name);

        b.back.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        b.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (adapterTabLayout.isClicked) {
                    adapterTabLayout.lastClicked = position;
                    adapterTabLayout.isClicked = false;
                    return;
                }

                tabs.get(position).setActive(true);
                tabs.get(adapterTabLayout.lastClicked).setActive(false);

                adapterTabLayout.notifyItemChanged(position);
                adapterTabLayout.notifyItemChanged(adapterTabLayout.lastClicked);

                adapterTabLayout.lastClicked = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setRecyclerTab() {
        adapterTabLayout = new AdapterTabLayout(getContext(), b.viewPager);
        b.recTab.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recTab.setAdapter(adapterTabLayout);
    }

    private void setViewPager() {
        ArrayList<FragmentPager> mFragment = new ArrayList<>();

        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, FragmentListGrid.CATEGORY, -1, new String[]{uuid}, new int[]{product}), ""));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, FragmentListGrid.CATEGORY, -1, new String[]{uuid}, new int[]{product}), ""));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, FragmentListGrid.CATEGORY, -1, new String[]{uuid}, new int[]{product}), ""));

        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);
    }
}