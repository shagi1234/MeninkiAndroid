package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.logWrite;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.api.ApiClient;
import tm.store.meninki.api.services.ServiceCategory;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.data.FragmentPager;
import tm.store.meninki.databinding.FragmentCategoryBinding;


public class FragmentCategory extends Fragment {
    private FragmentCategoryBinding b;

    public static FragmentCategory newInstance() {
        FragmentCategory fragment = new FragmentCategory();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        ServiceCategory serviceCategory = (ServiceCategory) ApiClient.createRequest(ServiceCategory.class);
        Call<ArrayList<CategoryDto>> call = serviceCategory.getAllCategory();
        call.enqueue(new Callback<ArrayList<CategoryDto>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CategoryDto>> call, @NonNull Response<ArrayList<CategoryDto>> response) {

                if (response.code() == 200 && response.body() != null) {
                    setViewPager(response.body());
                } else {
                    logWrite(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<CategoryDto>> call, @NonNull Throwable t) {
                logWrite(t.getMessage());
            }
        });
    }

    private void initListeners() {

    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.backgroundSearch, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtSearch, R.color.low_contrast, 0, 10, false, 0);
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
            mFragment.add(new FragmentPager(FragmentCategoryList.newInstance(data.get(i).getSubCategories(), FragmentCategoryList.TYPE_SUBCATEGORY, ""), data.get(i).getName().toUpperCase()));
        }

        AdapterViewPager adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        b.viewPager.setAdapter(adapterFeedPager);

    }


}