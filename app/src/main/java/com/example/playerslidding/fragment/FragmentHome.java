package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setPadding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.example.playerslidding.R;
import com.example.playerslidding.adapter.AdapterCircle;
import com.example.playerslidding.adapter.AdapterShops;
import com.example.playerslidding.adapter.AdapterStore;
import com.example.playerslidding.adapter.AdapterTabLayout;
import com.example.playerslidding.adapter.AdapterViewPager;
import com.example.playerslidding.data.FragmentPager;
import com.example.playerslidding.data.TabItemCustom;
import com.example.playerslidding.databinding.FragmentPostBinding;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private FragmentPostBinding b;
    private AdapterStore adapterStore;
    private AdapterTabLayout adapterTabLayout;
    private ArrayList<TabItemCustom> tabs = new ArrayList<>();
    private AdapterCircle adapterCircle;
    private AdapterShops adapterShops;
    private AdapterTabLayout adapterTabLayoutNew;

    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();
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
        setPadding(b.containerProfileId, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentPostBinding.inflate(inflater, container, false);
        setBackground();
        setRecycler();
        setRecyclerShops();
        setRecyclerCircle();
        setRecyclerTab();
        setRecyclerTabNew();
        setResources();
        setViewPager(b.viewPager);
        setViewPager(b.viewPagerNew);
        initListeners();

        adapterStore.setStories(null);
        adapterShops.setStories(null);
        adapterCircle.setStories(null);
        adapterTabLayout.setTabs(tabs);
        adapterTabLayoutNew.setTabs(tabs);

        return b.getRoot();
    }

    private void setRecyclerShops() {
        adapterShops = new AdapterShops(getContext(), getActivity());
        b.recMarkets.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recMarkets.setAdapter(adapterShops);
    }

    private void setRecyclerCircle() {
        adapterCircle = new AdapterCircle(getContext(), getActivity());
        b.recCircular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recCircular.setAdapter(adapterCircle);
    }

    private void initListeners() {
        b.btnCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(mainFragmentManager,R.id.fragment_container_main,FragmentFilterAndSort.newInstance());
            }
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
        b.viewPagerNew.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (adapterTabLayoutNew.isClicked) {
                    adapterTabLayoutNew.lastClicked = position;
                    adapterTabLayoutNew.isClicked = false;
                    return;
                }

                tabs.get(position).setActive(true);
                tabs.get(adapterTabLayoutNew.lastClicked).setActive(false);

                adapterTabLayoutNew.notifyItemChanged(position);
                adapterTabLayoutNew.notifyItemChanged(adapterTabLayoutNew.lastClicked);

                adapterTabLayoutNew.lastClicked = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setResources() {
//        Glide.with(getContext()).load(StoreList.getStoreDTOS().get(0).getImagePath()).into(b.bigImage1);
//        Glide.with(getContext()).load(StoreList.getStoreDTOS().get(0).getImagePath()).into(b.bigImage2);
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.backgroundSearch, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtSearch, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.btnCaption, R.color.white, 0, 10, false, 0);
    }

    private void setRecycler() {
        adapterStore = new AdapterStore(getContext(), getActivity());
        b.recStores.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recStores.setAdapter(adapterStore);
    }

    private void setRecyclerTab() {
        adapterTabLayout = new AdapterTabLayout(getContext(), b.viewPagerNew);
        b.recTab.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recTab.setAdapter(adapterTabLayout);
    }

    private void setRecyclerTabNew() {
        adapterTabLayoutNew = new AdapterTabLayout(getContext(), b.viewPager);
        b.recTabNew.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recTabNew.setAdapter(adapterTabLayoutNew);
    }

    private void setViewPager(ViewPager viewPager) {
        ArrayList<FragmentPager> mFragment = new ArrayList<>();

        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, "", FragmentListGrid.POPULAR), ""));

        AdapterViewPager adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        viewPager.setAdapter(adapterFeedPager);
        viewPager.setEnabled(false);
    }
}