package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.logWrite;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;
import static tm.store.meninki.utils.StaticMethods.setPadding;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterCircle;
import tm.store.meninki.adapter.AdapterShops;
import tm.store.meninki.adapter.AdapterStore;
import tm.store.meninki.adapter.AdapterTabLayout;
import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.enums.CardType;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.response.ResponseCard;
import tm.store.meninki.api.response.ResponseHomeShops;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.data.FragmentPager;
import tm.store.meninki.data.TabItemCustom;
import tm.store.meninki.databinding.FragmentHomeBinding;
import tm.store.meninki.utils.StaticMethods;

public class FragmentHome extends Fragment {
    private FragmentHomeBinding b;
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
        setMargins(b.fab, dpToPx(20, getContext()), dpToPx(20, getContext()), dpToPx(20, getContext()), dpToPx(20, getContext()) + navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentHomeBinding.inflate(inflater, container, false);
        setBackground();
        setRecycler();
        setRecyclerShops();
        setRecyclerCircle();
        setRecyclerTab();
        setRecyclerTabNew();
        setViewPager(b.viewPager, FragmentListGrid.POPULAR);
        setViewPager(b.viewPagerNew, FragmentListGrid.NEW);
        initListeners();

        getCategories();
        getPosts();
        getShops();
        adapterShops.setStories(null);
        adapterTabLayout.setTabs(tabs);
        adapterTabLayoutNew.setTabs(tabs);

        return b.getRoot();
    }

    private void getShops() {
        RequestCard requestCard = new RequestCard();
        requestCard.setCategoryIds(null);
        requestCard.setDescending(true);
        requestCard.setSortType(0);
        requestCard.setPageNumber(1);
        requestCard.setTake(10);
        Call<ArrayList<ResponseHomeShops>> call = StaticMethods.getApiHome().getAllShop(requestCard);
        call.enqueue(new RetrofitCallback<ArrayList<ResponseHomeShops>>() {
            @Override
            public void onResponse(ArrayList<ResponseHomeShops> response) {
                if (response == null || response.size() == 0) {
                    //no content
                    return;
                }
                adapterShops.setStories(response);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private void getCategories() {
        Call<ArrayList<CategoryDto>> call = StaticMethods.getApiCategory().getAllCategory();
        call.enqueue(new Callback<ArrayList<CategoryDto>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CategoryDto>> call, @NonNull Response<ArrayList<CategoryDto>> response) {

                if (response.code() == 200 && response.body() != null) {
                    adapterCircle.setStories(response.body());
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

        b.clickFab.setOnClickListener(v -> {
            b.clickFab.setEnabled(false);
            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentAddProduct.newInstance());
            new Handler().postDelayed(() -> b.clickFab.setEnabled(true), 200);
        });
    }

    private void getPosts() {

        RequestCard requestCard = new RequestCard();
        requestCard.setCardTypes(new int[]{CardType.post});
        requestCard.setCategoryIds(null);
        requestCard.setDescending(true);
        requestCard.setPageNumber(1);
        requestCard.setTake(10);

        Call<ArrayList<ResponseCard>> call = StaticMethods.getApiHome().getCard(requestCard);
        call.enqueue(new RetrofitCallback<ArrayList<ResponseCard>>() {
            @Override
            public void onResponse(ArrayList<ResponseCard> response) {
                adapterStore.setStories(response);

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("TAG_Error", "onFailure: " + t);
            }
        });
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.backgroundSearch, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.edtSearch, R.color.white, 0, 10, false, 0);
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

    private void setViewPager(ViewPager viewPager, int type) {
        ArrayList<FragmentPager> mFragment = new ArrayList<>();

        mFragment.add(new FragmentPager(FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, type, 4, null, CardType.getAll()), ""));

        AdapterViewPager adapterFeedPager = new AdapterViewPager(getChildFragmentManager(), mFragment);
        viewPager.setAdapter(adapterFeedPager);
        viewPager.setEnabled(false);
    }
}