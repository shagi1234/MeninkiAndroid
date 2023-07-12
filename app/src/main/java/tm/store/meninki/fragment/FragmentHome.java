package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.logWrite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterCircle;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.enums.CardType;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.response.ResponseCard;
import tm.store.meninki.api.response.ResponseHomeShops;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.data.HomeArray;
import tm.store.meninki.databinding.FragmentHomeBinding;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentHome extends Fragment {
    private FragmentHomeBinding b;
    private AdapterCircle adapterCircle;
    private AdapterGrid adapterGridPopular;
    private AdapterGrid adapterGridNew;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentHomeBinding.inflate(inflater, container, false);
        setRecycler();
        b.progressBar.setVisibility(View.VISIBLE);
        b.main.setVisibility(View.INVISIBLE);

        getHome1();
        getHome2();
        getHome3();

        initListeners();

        return b.getRoot();
    }

    private void setRecycler() {
        adapterGridPopular = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_GRID, 10);
        b.recPopular.setLayoutManager(new GridLayoutManager(getContext(), 2));
        b.recPopular.setAdapter(adapterGridPopular);

        adapterGridNew = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_GRID, 10);
        b.recNewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        b.recNewProducts.setAdapter(adapterGridNew);

        adapterCircle = new AdapterCircle(getContext(), getActivity());
        b.recMarkets.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recMarkets.setAdapter(adapterCircle);
    }

    private void getHome1() {
        Call<ArrayList<HomeArray>> call = StaticMethods.getApiHome().getHome1(/*Account.newInstance(getContext()).getAccessToken()*/);
        call.enqueue(new Callback<ArrayList<HomeArray>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<HomeArray>> call, @NonNull Response<ArrayList<HomeArray>> response) {
                b.progressBar.setVisibility(View.GONE);
                b.main.setVisibility(View.VISIBLE);
                if (response.body() == null || response.body().size()==0) return;

                if (response.body().get(0).getBanner() != null)
                    Glide.with(getContext())
                            .load(response.body().get(0).getBanner().getBannerImage())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(b.banner1);

                if (response.body().size() > 1)
                    adapterGridPopular.setStories(response.body().get(1).getPopularProducts());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<HomeArray>> call, @NonNull Throwable t) {
                b.progressBar.setVisibility(View.VISIBLE);
                b.main.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void getHome2() {
        Call<ArrayList<HomeArray>> call = StaticMethods.getApiHome().getHome2(/*Account.newInstance(getContext()).getAccessToken()*/);
        call.enqueue(new Callback<ArrayList<HomeArray>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<HomeArray>> call, @NonNull Response<ArrayList<HomeArray>> response) {
                if (response.body() == null || response.body().size()==0) return;

                setBanner(response);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<HomeArray>> call, @NonNull Throwable t) {

            }
        });
    }

    private void setBanner(Response<ArrayList<HomeArray>> response) {
        assert response.body() != null;
        if (response.body().get(0).getBanner()==null ) return;
        if (response.body().size() > 0 && response.body().get(0).getBanner() != null)
            Glide.with(getContext())
                    .load(response.body().get(0).getBanner().getBannerImage())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(b.banner2);
        b.descLabel.setText(response.body().get(0).getBanner().getTitle());
        b.desc.setText(response.body().get(0).getBanner().getDescription());
    }

    private void getHome3() {
        Call<ArrayList<HomeArray>> call = StaticMethods.getApiHome().getHome3(/*Account.newInstance(getContext()).getAccessToken()*/);
        call.enqueue(new Callback<ArrayList<HomeArray>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<HomeArray>> call, @NonNull Response<ArrayList<HomeArray>> response) {
                b.progressBar.setVisibility(View.GONE);
                b.main.setVisibility(View.VISIBLE);
                if (response.body() == null || response.body().size()==0) return;
//                adapterGridNew.setStories(response.body().get(1).getNewProducts());
//                Log.e("TAG_shops", "onResponse: " + new Gson().toJson(response.body().get(0).getShops()));
                adapterCircle.setStories(response.body().get(0).getShops());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<HomeArray>> call, @NonNull Throwable t) {
                b.progressBar.setVisibility(View.GONE);
            }
        });
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
//                adapterShops.setStories(response);

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
//                    adapterCircle.setStories(response.body());
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

    private void getPosts() {

        RequestCard requestCard = new RequestCard();
//        requestCard.setCardTypes(new int[]{CardType.post});
        requestCard.setCategoryIds(null);
        requestCard.setDescending(true);
        requestCard.setPageNumber(1);
        requestCard.setTake(10);

        Call<ArrayList<ResponseCard>> call = StaticMethods.getApiHome().getCard(requestCard);
        call.enqueue(new RetrofitCallback<ArrayList<ResponseCard>>() {
            @Override
            public void onResponse(ArrayList<ResponseCard> response) {
//                adapterStore.setStories(response);

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("TAG_Error", "onFailure: " + t);
            }
        });
    }

    private void initListeners() {
//        b.clickFab.setOnClickListener(v -> {
//            b.clickFab.setEnabled(false);
//            addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentAddProduct.newInstance());
//            new Handler().postDelayed(() -> b.clickFab.setEnabled(true), 200);
//        });
    }
}