package tm.store.meninki.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.ResponsePostGetAllItem;
import tm.store.meninki.api.enums.CardType;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.response.ResponseCard;
import tm.store.meninki.databinding.FragmentFeedBinding;
import tm.store.meninki.utils.StaticMethods;

public class FragmentFeed extends Fragment {
    private FragmentFeedBinding b;
    private int page = 1;
    private int limit = 20;
    private AdapterGrid adapterGrid;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentFeedBinding.inflate(inflater, container, false);
        setProgress(true);
        setRecycler();
        check();

        return b.getRoot();
    }

    private void check() {
        RequestCard requestCard = new RequestCard();
        requestCard.setDescending(true);
        requestCard.setPageNumber(page);
        requestCard.setTake(limit);

        Call<ArrayList<ResponsePostGetAllItem>> call = StaticMethods.getApiHome().getAllPosts(requestCard);
        call.enqueue(new RetrofitCallback<ArrayList<ResponsePostGetAllItem>>() {
            @Override
            public void onResponse(ArrayList<ResponsePostGetAllItem> response) {
                if (response == null) return;
                adapterGrid.setPosts(response);
                setProgress(false);

            }

            @Override
            public void onFailure(Throwable t) {
                setProgress(false);
            }
        });
    }

    private void getStories() {
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
//                adapterStore.setStories(response);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("TAG_Error", "onFailure: " + t);
            }
        });
    }

    private void setProgress(boolean isProgress) {
        if (isProgress) {
            b.progressBar.setVisibility(View.VISIBLE);
            b.recGrid.setVisibility(View.GONE);
        } else {
            b.progressBar.setVisibility(View.GONE);
            b.recGrid.setVisibility(View.VISIBLE);
        }
    }

    private void setRecycler() {
        adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_POST, 10);
        b.recGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        b.recGrid.setAdapter(adapterGrid);
    }


}