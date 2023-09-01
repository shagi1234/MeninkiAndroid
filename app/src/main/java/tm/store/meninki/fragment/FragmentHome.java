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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterCircle;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.data.response.ResponseCard;
import tm.store.meninki.api.data.response.ResponseHomeShops;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.data.HomeArray;
import tm.store.meninki.databinding.FragmentHomeBinding;
import tm.store.meninki.utils.StaticMethods;

public class FragmentHome extends Fragment {
    private FragmentHomeBinding b;
    private AdapterCircle adapterCircle;
    private AdapterGrid adapterGridPopularProd;
    private AdapterGrid adapterGridPopularPost;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    private void initListeners() {

        b.swipeLayout.setOnRefreshListener(() -> {
            getHome1();
            getHome2();
            getHome3();
            b.swipeLayout.setRefreshing(false);
        });

    }

    private void setRecycler() {
        adapterGridPopularProd = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_GRID, 10);
        b.recPopularProd.setLayoutManager(new GridLayoutManager(getContext(), 2));
        b.recPopularProd.setAdapter(adapterGridPopularProd);

        adapterGridPopularPost = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_POST, 10);
        b.recPopularPost.setLayoutManager(new GridLayoutManager(getContext(), 2));
        b.recPopularPost.setAdapter(adapterGridPopularPost);

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
                ArrayList<HomeArray> responseBody = response.body();

                if (responseBody == null || responseBody.isEmpty()) {
                    b.popularProdLay.setVisibility(View.GONE);
                    b.banner1.setVisibility(View.GONE);
                    return;
                }

                if (responseBody.get(0).getBanner() != null) {
                    b.banner1.setVisibility(View.VISIBLE);

                    Glide.with(getContext())
                            .load(responseBody.get(0).getBanner().getBannerImage())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(b.banner1);
                } else {
                    b.banner1.setVisibility(View.GONE);
                }

                if (responseBody.get(0).getPopularProducts() != null) {
                    b.popularProdLay.setVisibility(View.VISIBLE);
                    adapterGridPopularProd.setStories(responseBody.get(0).getPopularProducts());
                } else {
                    b.popularProdLay.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<HomeArray>> call, @NonNull Throwable t) {
            }
        });
    }

    private void getHome2() {
        Call<ArrayList<HomeArray>> call = StaticMethods.getApiHome().getHome2(/*Account.newInstance(getContext()).getAccessToken()*/);
        call.enqueue(new Callback<ArrayList<HomeArray>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<HomeArray>> call, @NonNull Response<ArrayList<HomeArray>> response) {
                b.main.setVisibility(View.VISIBLE);

                if (response.body() == null || response.body().size() == 0) {
                    b.banner1.setVisibility(View.GONE);
                    b.popularPostLay.setVisibility(View.GONE);
                    return;
                }

                if (response.body().get(0).getPopularPosts() != null) {
                    b.popularPostLay.setVisibility(View.VISIBLE);
                    adapterGridPopularPost.setPosts(response.body().get(0).getPopularPosts());
                } else b.popularPostLay.setVisibility(View.GONE);

                if (response.body().size() > 1 && response.body().get(1).getBanner() != null) {
                    setBanner(response);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<HomeArray>> call, @NonNull Throwable t) {

            }
        });
    }

    private void setBanner(Response<ArrayList<HomeArray>> response) {
        if (response.body().size() == 0 || response.body().get(0).getBanner() == null) {
            return;
        }
        if (response.body().size() > 0 && response.body().get(0).getBanner() != null) {
            b.banner2.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(response.body().get(0).getBanner().getBannerImage()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(b.banner2);
            b.descLabel.setText(response.body().get(0).getBanner().getTitle());
            b.desc.setText(response.body().get(0).getBanner().getDescription());
        } else b.layBanner2.setVisibility(View.GONE);
    }

    private void getHome3() {
        Call<ArrayList<HomeArray>> call = StaticMethods.getApiHome().getHome3(/*Account.newInstance(getContext()).getAccessToken()*/);
        call.enqueue(new Callback<ArrayList<HomeArray>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<HomeArray>> call, @NonNull Response<ArrayList<HomeArray>> response) {
                b.progressBar.setVisibility(View.GONE);
                b.main.setVisibility(View.VISIBLE);

                if (response.body() == null || response.body().size() == 0) return;

                if (response.body().get(0).getBanner() == null) b.banner2.setVisibility(View.GONE);
                else b.banner2.setVisibility(View.VISIBLE);

                if (response.body().get(1).getNewProducts() == null || response.body().get(1).getNewProducts().size() == 0)
                    b.newsLay.setVisibility(View.GONE);
                else {
                    b.newsLay.setVisibility(View.VISIBLE);
                    adapterGridNew.setStories(response.body().get(1).getNewProducts());
                }

                if (response.body().get(0).getShops() == null) b.shopsLay.setVisibility(View.GONE);
                adapterCircle.setStories(response.body().get(0).getShops());

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<HomeArray>> call, @NonNull Throwable t) {
                b.progressBar.setVisibility(View.GONE);
            }
        });
    }
}