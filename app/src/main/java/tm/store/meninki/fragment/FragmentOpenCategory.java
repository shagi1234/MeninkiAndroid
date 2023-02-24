package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import tm.store.meninki.adapter.AdapterTabLayout;
import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.data.FragmentPager;
import tm.store.meninki.data.TabItemCustom;
import tm.store.meninki.databinding.FragmentOpenCategoryBinding;

import java.util.ArrayList;

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
        setPadding(b.viewPager, 0, 0, 0, navigationBarHeight);
        setPadding(b.header, 0, statusBarHeight, 0, 0);
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
        initListeners();
        setRecyclerTab();
        setViewPager();
        adapterTabLayout.setTabs(tabs);
        return b.getRoot();
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

        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, uuid, FragmentListGrid.CATEGORY), ""));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, uuid, FragmentListGrid.CATEGORY), ""));
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, uuid, FragmentListGrid.CATEGORY), ""));

        adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);
    }
}