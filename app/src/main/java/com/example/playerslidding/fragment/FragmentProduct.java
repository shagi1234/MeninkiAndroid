package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.getWindowHeight;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playerslidding.R;
import com.example.playerslidding.adapter.AdapterCharColor;
import com.example.playerslidding.adapter.AdapterCharImage;
import com.example.playerslidding.adapter.AdapterCharPick;
import com.example.playerslidding.adapter.AdapterVerticalImagePager;
import com.example.playerslidding.adapter.AdapterViewPager;
import com.example.playerslidding.data.FragmentPager;
import com.example.playerslidding.databinding.FragmentProductBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentProduct extends Fragment {
    private FragmentProductBinding b;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private ArrayList<String> s = new ArrayList<>();
    private ArrayList<String> picks = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private String uuid;
    private int isStore;
    public final static int STORE = 0;
    public final static int PRODUCT = 1;
    private AdapterCharImage adapterCharImage;
    private AdapterCharPick adapterCharPick;
    private AdapterCharColor adapterCharColor;

    public static FragmentProduct newInstance(String uuid, int isStore) {
        FragmentProduct fragment = new FragmentProduct();
        Bundle args = new Bundle();
        args.putString("uuid", uuid);
        args.putInt("is_store", isStore);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString("uuid");
            isStore = getArguments().getInt("is_store");
        }
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");

        picks.add("XXL");
        picks.add("XL");
        picks.add("L");
        picks.add("M");
        picks.add("S");
        picks.add("XS");

        colors.add(R.color.black);
        colors.add(R.color.alert);
        colors.add(R.color.alert);
        colors.add(R.color.holder);
        colors.add(R.color.black);
        colors.add(R.color.black);
        colors.add(R.color.dark);
        colors.add(R.color.holder);


    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.coordinator, 0, 0, 0, navigationBarHeight);
        setPadding(b.getRoot(), 0, statusBarHeight, 0, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentProductBinding.inflate(inflater, container, false);
        checkUI();
        setBackgrounds();
        initListeners();

        setBottomSheet();

        setImagePager();
        setViewPager();

        return b.getRoot();
    }

    private void checkUI() {
        if (isStore == STORE) {
            b.bottomSheet.checkStore.setVisibility(View.GONE);
            b.bottomSheet.layBasket.setVisibility(View.GONE);
            b.bottomSheet.btnCaption.setVisibility(View.GONE);
        } else {
            b.bottomSheet.checkStore.setVisibility(View.VISIBLE);
            b.bottomSheet.layBasket.setVisibility(View.VISIBLE);
            b.bottomSheet.btnCaption.setVisibility(View.VISIBLE);

            setRecyclerCharImage();
            setRecyclerCharPick();
            setRecyclerColor();

            getProductData();

        }
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        b.bottomSheet.btnCaption.setOnClickListener(v -> {
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentAddPost.newInstance());
        });

        b.bottomSheet.layBasket.setOnClickListener(v -> b.bottomSheet.layCountControl.setVisibility(View.VISIBLE));

    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.bottomSheet.goBasket, R.color.accent, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.bottomSheet.layCountControl, R.color.grey, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.bottomSheet.btnCaption, R.color.white, 0, 4, false, 0);
    }

    private void setImagePager() {
        AdapterVerticalImagePager adapterVerticalImagePager = new AdapterVerticalImagePager(getContext());
        b.imagePager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getWindowHeight(getActivity()) * 55 / 75));

        b.imagePager.setClipToPadding(false);
        b.imagePager.setClipChildren(false);
        b.imagePager.setOffscreenPageLimit(3);
        b.imagePager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        b.imagePager.setAdapter(adapterVerticalImagePager);

        adapterVerticalImagePager.setImageList(s);
        b.indicator.setViewPager(b.imagePager);


    }

    private void setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(b.bottomSheet.root);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(getWindowHeight(getActivity()) * 20 / 75);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void getProductData() {
        adapterCharImage.setImageUrl(s);
        adapterCharPick.setPicks(picks);
        adapterCharColor.setColor(colors);
    }

    private void setRecyclerCharImage() {
        adapterCharImage = new AdapterCharImage(getContext());
        b.bottomSheet.recCharImage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.bottomSheet.recCharImage.setAdapter(adapterCharImage);
    }

    private void setRecyclerCharPick() {
        adapterCharPick = new AdapterCharPick(getContext());
        b.bottomSheet.recCharPick.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.bottomSheet.recCharPick.setAdapter(adapterCharPick);
    }

    private void setRecyclerColor() {
        adapterCharColor = new AdapterCharColor(getContext());
        b.bottomSheet.recCharColor.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.bottomSheet.recCharColor.setAdapter(adapterCharColor);
    }

    private void setViewPager() {

        b.bottomSheet.tabLayout.setupWithViewPager(b.bottomSheet.viewPager);

        b.bottomSheet.tabLayout.setTabRippleColor(null);

        b.bottomSheet.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition(); // syntactic sugar
                b.bottomSheet.viewPager.setCurrentItem(tabPosition, true);
                // Repeat of the code above -- tooltips reset themselves after any tab relayout, so I
                // have to constantly keep turning them off again.
                for (int i = 0; i < b.bottomSheet.tabLayout.getTabCount(); i++) {
                    TooltipCompat.setTooltipText(Objects.requireNonNull(b.bottomSheet.tabLayout.getTabAt(i)).view, null);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        b.bottomSheet.viewPager.setOffscreenPageLimit(2);
        ArrayList<FragmentPager> mFragment = new ArrayList<>();
        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.HORIZONTAL_LINEAR, uuid, FragmentListGrid.CATEGORY), "Похожие".toUpperCase()));

        AdapterViewPager adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.bottomSheet.viewPager.setAdapter(adapterFeedPager);

    }

}