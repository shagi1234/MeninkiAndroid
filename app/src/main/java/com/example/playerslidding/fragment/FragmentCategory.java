package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.Const.mainFragmentManager;
import static com.example.playerslidding.utils.FragmentHelper.addFragment;
import static com.example.playerslidding.utils.StaticMethods.logWrite;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.adapter.AdapterViewPager;
import com.example.playerslidding.api.ApiClient;
import com.example.playerslidding.api.ApiInterface;
import com.example.playerslidding.data.CategoryDto;
import com.example.playerslidding.data.FragmentPager;
import com.example.playerslidding.databinding.FragmentCategoryBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentCategory extends Fragment {
    private FragmentCategoryBinding b;
    private ArrayList<CategoryDto> categoryDtos = new ArrayList<>();
    private ArrayList<CategoryDto> sub = new ArrayList<>();

    public static FragmentCategory newInstance() {
        FragmentCategory fragment = new FragmentCategory();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.frameCtg, 0, statusBarHeight, 0, 0);
        setPadding(b.layUserInfo, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sub.add(new CategoryDto("Мужская", "Мужская", "", null, "", "", "", null, null, null, false));
        sub.add(new CategoryDto("Новинки", "Новинки", "", null, "", "", "", null, null, null, false));
        sub.add(new CategoryDto("спецодежда (униформа)", "спецодежда (униформа)", "", null, "", "", "", null, null, null, false));
        sub.add(new CategoryDto("Ткани", "Ткани", "", null, "", "", "", null, null, null, false));

        categoryDtos.add(new CategoryDto("Ветеринария", "Ветеринария", "", null, "", "", "", sub, null, null, false));
        categoryDtos.add(new CategoryDto("Авто", "Авто", "", null, "", "", "", sub, null, null, false));
        categoryDtos.add(new CategoryDto("Техника", "Техника", "", null, "", "", "", sub, null, null, false));
        categoryDtos.add(new CategoryDto("Одежда", "Одежда", "", null, "", "", "", sub, null, null, false));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCategoryBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
//        getAllCategories();
        setViewPager(categoryDtos);
        return b.getRoot();
    }

    private void getAllCategories() {
        ApiInterface apiInterface = (ApiInterface) ApiClient.createRequest(ApiInterface.class);
        Call<ArrayList<CategoryDto>> call = apiInterface.getAllCategory();
        call.enqueue(new Callback<ArrayList<CategoryDto>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryDto>> call, Response<ArrayList<CategoryDto>> response) {

                if (response.code() == 200 && response.body() != null) {
                    setViewPager(response.body());
                } else {
                    logWrite(response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryDto>> call, Throwable t) {
                logWrite(t.getMessage());

            }
        });
    }

    private void initListeners() {
        b.icCart.setOnClickListener(v -> {
            b.icCart.setEnabled(false);
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentBasket.newInstance());
            new Handler().postDelayed(() -> b.icCart.setEnabled(true), 200);
        });
        b.clickUserData.setOnClickListener(v -> {
            b.layUserInfo.setEnabled(false);
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentProfile.newInstance(FragmentProfile.TYPE_USER));
            new Handler().postDelayed(() -> b.layUserInfo.setEnabled(true), 200);
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.icCart, R.color.background, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.userImage, R.color.background, R.color.accent, 0, true, 2);
    }


    private void setViewPager(ArrayList<CategoryDto> data) {

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

        b.viewPager.setOffscreenPageLimit(2);
//todo shu yeri zynya bosh wagt duzetmeli constructory ayyrmaly
        ArrayList<FragmentPager> mFragment = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            mFragment.add(new FragmentPager(new FragmentCategoryList(data.get(i).getSubCategories()), data.get(i).getName().toUpperCase()));
        }

        AdapterViewPager adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);

    }


}