package tm.store.meninki.fragment;

import static tm.store.meninki.adapter.AdapterGrid.TYPE_ADVERTISEMENT;
import static tm.store.meninki.fragment.FragmentMain.editSearch;
import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.logWrite;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.setPaddingWithHandler;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import tm.store.meninki.api.enums.SortType;
import tm.store.meninki.api.request.RequestAllAdvertisement;
import tm.store.meninki.api.services.ServiceAdvertisement;
import tm.store.meninki.data.AdvertisementDto;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.databinding.FragmentAdvertisementsBinding;
import tm.store.meninki.interfaces.GetAllAdvertisement;
import tm.store.meninki.interfaces.OnSearched;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentAdvertisements extends Fragment implements GetAllAdvertisement, OnSearched {
    private FragmentAdvertisementsBinding b;
    AdapterGrid adapterGrid;
    Account account;
    private RequestAllAdvertisement requestAllAdvertisement;
    private boolean isSearch;

    public static FragmentAdvertisements newInstance(boolean isSearch) {
        FragmentAdvertisements fragment = new FragmentAdvertisements();
        Bundle args = new Bundle();
        args.putBoolean("is_search", isSearch);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account(getContext());
        if (getArguments() != null) {
            isSearch = getArguments().getBoolean("is_search");
        }

    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!getUserVisibleHint() || getActivity() == null) {
            return;
        }
        if (editSearch == null) return;

        if (isSearch)
            onSearched(editSearch.getText().toString().trim());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentAdvertisementsBinding.inflate(inflater, container, false);
        setRecyclerAdvertisements();
        checkUi();
        initListeners();
        getAllAdvertisements(createInitialRequestBody(null));
        return b.getRoot();
    }

    private void checkUi() {
        if (isSearch) {
            b.appBarLayout.setVisibility(View.GONE);
            b.tabCategories.setVisibility(View.GONE);

            if (editSearch != null)
                editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (editSearch != null)
                            onSearched(editSearch.getText().toString().trim());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
        } else {
            b.appBarLayout.setVisibility(View.VISIBLE);
            b.tabCategories.setVisibility(View.VISIBLE);

            getAllCategories();
        }
    }

    private RequestAllAdvertisement createInitialRequestBody(String categoryId) {
        requestAllAdvertisement = new RequestAllAdvertisement();
        requestAllAdvertisement.setSortType(0);
        requestAllAdvertisement.setDescending(true);
        requestAllAdvertisement.setPageNumber(1);
        requestAllAdvertisement.setTake(20);

        if (categoryId != null)
            requestAllAdvertisement.setCategoryIds(new String[]{categoryId});
        return requestAllAdvertisement;
    }

    private void getAllCategories() {
        Call<ArrayList<CategoryDto>> call = StaticMethods.getApiCategory().getAllCategory(1);
        call.enqueue(new Callback<ArrayList<CategoryDto>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CategoryDto>> call, @NonNull Response<ArrayList<CategoryDto>> response) {

                if (response.code() == 200 && response.body() != null) {
                    setTabCategories(response.body());
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
        b.filterLay.setOnClickListener(view -> {
            b.filterLay.setEnabled(false);
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentFilterAdvertisement.newInstance(requestAllAdvertisement));
            new Handler().postDelayed(() -> b.filterLay.setEnabled(true), 200);
        });

        b.plusClick.setOnClickListener(view -> {
            b.plusClick.setEnabled(false);
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentAddAdvertisement.newInstance(account.getPrefUserUUID()));
            new Handler().postDelayed(() -> b.plusClick.setEnabled(true), 200);
        });

        b.swipeLayout.setOnRefreshListener(() -> {
            getAllAdvertisements(requestAllAdvertisement);
            getActivity().runOnUiThread(() -> b.swipeLayout.setRefreshing(false));
        });

    }

    private void setRecyclerAdvertisements() {
        adapterGrid = new AdapterGrid(getContext(), getActivity(), TYPE_ADVERTISEMENT, -1,isSearch);
        b.recGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));
        b.recGrid.setAdapter(adapterGrid);
    }

    private void getAllAdvertisements(RequestAllAdvertisement requestAllAdvertisement) {
        b.progressBar.setVisibility(View.VISIBLE);
        b.noContent.setVisibility(View.GONE);
        b.recGrid.setVisibility(View.GONE);

        checkSortType();

        ServiceAdvertisement serviceAdvertisement = (ServiceAdvertisement) ApiClient.createRequest(ServiceAdvertisement.class);
        Call<ArrayList<AdvertisementDto>> call = serviceAdvertisement.getAllAdvertisements(requestAllAdvertisement);
        call.enqueue(new Callback<ArrayList<AdvertisementDto>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<AdvertisementDto>> call, @NonNull Response<ArrayList<AdvertisementDto>> response) {
                if (response.body() == null || response.body().size() == 0) {
                    b.noContent.setText(R.string.no_content);
                    b.noContent.setVisibility(View.VISIBLE);
                    b.progressBar.setVisibility(View.GONE);
                    b.recGrid.setVisibility(View.GONE);
                    return;

                }
                b.recGrid.setVisibility(View.VISIBLE);
                adapterGrid.setAdvertisements(response.body());

                b.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ArrayList<AdvertisementDto>> call, Throwable t) {
                b.progressBar.setVisibility(View.GONE);
                b.recGrid.setVisibility(View.GONE);
                b.noContent.setVisibility(View.VISIBLE);
                b.noContent.setText(R.string.no_connection);
                Toast.makeText(getContext(), getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkSortType() {
        switch (requestAllAdvertisement.getSortType()) {
            case SortType.date:
                b.tvFilterResult.setText(R.string.sort_by_date);
                break;
            case SortType.rating:
                b.tvFilterResult.setText(R.string.sort_by_rating);
                break;
            case SortType.price:
                b.tvFilterResult.setText(R.string.sort_by_price);
                break;
            case SortType.viewNumber:
                b.tvFilterResult.setText(R.string.sort_by_view_count);
                break;
        }
    }

    private void setTabCategories(ArrayList<CategoryDto> categories) {
        if (getActivity() == null) return;
        b.tabCategories.setTabRippleColor(null);
        b.tabCategories.removeAllTabs();
        b.tabCategories.addTab(b.tabCategories.newTab().setText(""));

        Objects.requireNonNull(b.tabCategories.getTabAt(0)).setIcon(R.drawable.ic_categories_tab);
        b.tabCategories.getTabAt(0).getIcon().setTint(getActivity().getResources().getColor(R.color.bg));

        for (int i = 0; i < categories.size(); i++) {
            b.tabCategories.addTab(b.tabCategories.newTab().setText(categories.get(i).getName()));
        }

        b.tabCategories.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (getActivity() == null) return;

                if (tab.getPosition() == 0) {
                    getAllAdvertisements(createInitialRequestBody(null));
                } else
                    getAllAdvertisements(createInitialRequestBody(categories.get(tab.getPosition() - 1).getId()));

                try {
                    if (tab.getPosition() == 0) {
                        int tintColor = getActivity().getResources().getColor(R.color.white);
                        tab.getIcon().setTint(tintColor);
                    } else
                        b.tabCategories.getTabAt(0).getIcon().setTint(getResources().getColor(R.color.contrast));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void getAllAds(RequestAllAdvertisement requestAllAdvertisement) {
        this.requestAllAdvertisement = requestAllAdvertisement;

        checkSortType();

        getAllAdvertisements(requestAllAdvertisement);
    }

    @Override
    public void onSearched(String text) {
        RequestAllAdvertisement requestAllAdvertisement = createInitialRequestBody(null);
        requestAllAdvertisement.setSearch(text);
        if (text.length() == 0) {
            b.recGrid.setVisibility(View.INVISIBLE);
        } else b.recGrid.setVisibility(View.VISIBLE);

        getAllAdvertisements(requestAllAdvertisement);
    }
}