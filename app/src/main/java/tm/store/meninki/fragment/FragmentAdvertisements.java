package tm.store.meninki.fragment;

import static tm.store.meninki.adapter.AdapterGrid.TYPE_ADVERTISEMENT;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.logWrite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.api.ApiClient;
import tm.store.meninki.api.request.RequestAllAdvertisement;
import tm.store.meninki.api.services.ServiceAdvertisement;
import tm.store.meninki.api.services.ServiceCategory;
import tm.store.meninki.data.AdvertisementDto;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.databinding.FragmentAdvertisementsBinding;

public class FragmentAdvertisements extends Fragment {
    private FragmentAdvertisementsBinding b;
    AdapterGrid adapterGrid;

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
        setRecyclerAdvertisements();
        setTabCategories();
        initListeners();

        getAllAdvertisements();
        return b.getRoot();
    }

    private void initListeners() {
        b.filterClick.setOnClickListener(view -> {
            b.filterClick.setEnabled(false);
                addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentFilterAdvertisement.newInstance());
            new Handler().postDelayed(() -> b.filterClick.setEnabled(true), 200);
        });

    }

    private void setRecyclerAdvertisements() {
        adapterGrid = new AdapterGrid(getContext(), getActivity(), TYPE_ADVERTISEMENT, -1);
        b.recGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));
        b.recGrid.setAdapter(adapterGrid);
    }

    private void getAllAdvertisements() {
        int[] welayats = {0};
        String[] categoryIds = {"3fa85f64-5717-4562-b3fc-2c963f66afa6"};
        RequestAllAdvertisement requestAllAdvertisement = new RequestAllAdvertisement();
        requestAllAdvertisement.setSortType(0);
        requestAllAdvertisement.setDescending(true);
        requestAllAdvertisement.setPageNumber(1);
        requestAllAdvertisement.setTake(20);
        requestAllAdvertisement.setWelayats(welayats);
        requestAllAdvertisement.setCategoryIds(categoryIds);

        ServiceAdvertisement serviceAdvertisement = (ServiceAdvertisement) ApiClient.createRequest(ServiceAdvertisement.class);
        Call<ArrayList<AdvertisementDto>> call = serviceAdvertisement.getAllAdvertisements(requestAllAdvertisement);
        call.enqueue(new Callback<ArrayList<AdvertisementDto>>() {
            @Override
            public void onResponse(Call<ArrayList<AdvertisementDto>> call, Response<ArrayList<AdvertisementDto>> response) {
                b.progressBar.setVisibility(View.GONE);
                if (response.body() == null || response.body().size() == 0) {
                    b.noContent.setVisibility(View.VISIBLE);
                    return;

                }
                if (response.code() == 200 && response.body() != null) {
                    adapterGrid.setAdvertisements(response.body());
                } else {
                    logWrite(response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AdvertisementDto>> call, Throwable t) {
                b.progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Check Internet Connection!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setTabCategories() {
        b.tabCategories.setTabRippleColor(null);
        b.tabCategories.addTab(b.tabCategories.newTab().setText(""));

        Objects.requireNonNull(b.tabCategories.getTabAt(0)).setIcon(R.drawable.ic_categories_tab);
        b.tabCategories.getTabAt(0).getIcon().setTint(getResources().getColor(R.color.bg));

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

}