package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.FragmentHelper.replaceFragment;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterStore;
import tm.store.meninki.adapter.AdapterViewPager;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.enums.CardType;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.response.ResponseCard;
import tm.store.meninki.databinding.FragmentFeedBinding;
import tm.store.meninki.utils.StaticMethods;

public class FragmentFeed extends Fragment {
    private FragmentFeedBinding b;
    private AdapterViewPager adapterFeedPager;
    private AdapterStore adapterStore;

    public static FragmentFeed newInstance() {
        FragmentFeed fragment = new FragmentFeed();
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
        b = FragmentFeedBinding.inflate(inflater, container, false);
        setBackground();
        setRecycler();
        replaceFragment(mainFragmentManager, R.id.content_view_pager, FragmentListGrid.newInstance(FragmentListGrid.VERTICAL_GRID, FragmentListGrid.FEED, -1, null,CardType.getAll()));
        initListeners();

        getPosts();

        return b.getRoot();
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

    private void initListeners() {
        b.filter.setOnClickListener(view -> addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentFilterAndSort.newInstance()));
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

}