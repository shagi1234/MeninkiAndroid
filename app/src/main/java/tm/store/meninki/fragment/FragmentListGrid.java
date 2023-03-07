package tm.store.meninki.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.enums.CardType;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.response.ResponseCard;
import tm.store.meninki.data.StoreDTO;
import tm.store.meninki.databinding.FragmentListGridBinding;
import tm.store.meninki.utils.StaticMethods;

public class FragmentListGrid extends Fragment {
    private FragmentListGridBinding b;
    private ArrayList<StoreDTO> stores = new ArrayList<>();
    private AdapterGrid adapterGrid;
    private int orientation;
    private int type;
    public final static int VERTICAL_GRID = 0;
    public final static int HORIZONTAL_LINEAR = 1;
    private String[] categoryIds;
    public final static int CATEGORY = 1;
    public final static int POPULAR = 2;
    public final static int NEW = 3;
    public final static int FEED = 4;
    private int page = 1;
    private int limit = 10;
    public int maxSize;

    public static FragmentListGrid newInstance(int orientation, int type, int maxSize, String[] categoryIds) {
        FragmentListGrid fragment = new FragmentListGrid();
        Bundle args = new Bundle();
        args.putInt("orientation", orientation);
        args.putStringArray("category_ids", categoryIds);
        args.putInt("type", type);
        args.putInt("max_size", maxSize);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orientation = getArguments().getInt("orientation");
            type = getArguments().getInt("type");
            maxSize = getArguments().getInt("max_size");
            categoryIds = getArguments().getStringArray("category_ids");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentListGridBinding.inflate(inflater, container, false);
        setRecycler();
        setProgress(true);
        check();
        return b.getRoot();
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

    private void check() {
        RequestCard requestCard = new RequestCard();
        requestCard.setCardTypes(new int[]{CardType.product});
        requestCard.setCategoryIds(categoryIds);
        requestCard.setDescending(true);
        requestCard.setPageNumber(page);
        requestCard.setTake(limit);

        Call<ArrayList<ResponseCard>> call = StaticMethods.getApiHome().getCard(requestCard);
        call.enqueue(new RetrofitCallback<ArrayList<ResponseCard>>() {
            @Override
            public void onResponse(ArrayList<ResponseCard> response) {
                adapterGrid.setStories(response);
                setProgress(false);
                Log.e("TAG_Error", "onResponse: " + response.size());

            }

            @Override
            public void onFailure(Throwable t) {
                setProgress(false);
                Log.e("TAG_Error", "onFailure: " + t);
            }
        });
    }


    private void setRecycler() {
        if (orientation == HORIZONTAL_LINEAR) {
            adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_HORIZONTAL, maxSize);
            b.recGrid.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_GRID, maxSize);
            b.recGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
        b.recGrid.setAdapter(adapterGrid);
    }
}