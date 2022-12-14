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
import android.util.Log;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCategoryBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
        getAllCategories();
        return b.getRoot();
    }

    private void getAllCategories() {
        ApiInterface apiInterface = (ApiInterface) ApiClient.createRequest(ApiInterface.class);
        Call<ArrayList<CategoryDto>> call = apiInterface.getAllCategory();
        call.enqueue(new Callback<ArrayList<CategoryDto>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryDto>> call, Response<ArrayList<CategoryDto>> response) {

                if (response.code() == 200 && response.body() != null) {
                    Log.e("TAG", "onResponse: " + response.body().toString());
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
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentBasket.newInstance());
        });
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.icCart, R.color.background, 0, 4, false, 0);
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

        ArrayList<FragmentPager> mFragment = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            mFragment.add(new FragmentPager(FragmentCategoryList.newInstance(), data.get(i).getName().toUpperCase()));
        }

        AdapterViewPager adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);

    }


}