package tm.store.meninki.fragment;

import static tm.store.meninki.fragment.FragmentMain.editSearch;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.data.response.ResponseCard;
import tm.store.meninki.databinding.FragmentListGridBinding;
import tm.store.meninki.interfaces.OnSearched;
import tm.store.meninki.utils.StaticMethods;

public class FragmentListGrid extends Fragment implements OnSearched {
    private FragmentListGridBinding b;
    private AdapterGrid adapterGrid;
    private int orientation;
    public final static int VERTICAL_GRID = 0;
    public final static int HORIZONTAL_LINEAR = 1;
    private String[] categoryIds;
    private int type;
    private int[] cardTypes;
    public final static int CATEGORY_PROD = 1;
    public final static int SEARCH_PRODUCT = 2;
    private int page = 1;
    private int limit = 20;
    public int maxSize;

    public static FragmentListGrid newInstance(int orientation, int type, int maxSize, String[] categoryIds, int[] cardType) {
        FragmentListGrid fragment = new FragmentListGrid();
        Bundle args = new Bundle();
        args.putInt("orientation", orientation);
        args.putInt("type", type);
        args.putStringArray("category_ids", categoryIds);
        args.putIntArray("card_types", cardType);
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
            cardTypes = getArguments().getIntArray("card_types");
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
        if (type == SEARCH_PRODUCT)
            onSearched(editSearch.getText().toString().trim());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentListGridBinding.inflate(inflater, container, false);
        setRecycler();
        setProgress(true);
        isSearch();
        RequestCard requestCard = new RequestCard();
        requestCard.setCategoryIds(categoryIds);
        requestCard.setDescending(true);
        requestCard.setPageNumber(page);
        requestCard.setTake(limit);
        check(requestCard);
        return b.getRoot();
    }

    private void isSearch() {
        if (type == SEARCH_PRODUCT) {

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
        }
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

    private void check(RequestCard requestCard) {
        setProgress(true);
        Call<ArrayList<ResponseCard>> call = StaticMethods.getApiHome().getCard(requestCard);
        call.enqueue(new RetrofitCallback<ArrayList<ResponseCard>>() {
            @Override
            public void onResponse(ArrayList<ResponseCard> response) {

                if (response == null || response.size() == 0) {
                    setProgress(false);
                    b.noContent.setText(R.string.no_content);
                    b.noContent.setVisibility(View.VISIBLE);
                    return;
                }

                b.noContent.setVisibility(View.GONE);
                adapterGrid.setStories(response);
                setProgress(false);

            }

            @Override
            public void onFailure(Throwable t) {
                setProgress(false);
                Log.e("TAG_Error", "onFailure: " + t);
                b.noContent.setVisibility(View.VISIBLE);
                b.noContent.setText(R.string.no_connection);
            }
        });
    }


    private void setRecycler() {
        if (orientation == HORIZONTAL_LINEAR) {
            adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_HORIZONTAL, maxSize, false);
            b.recGrid.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_GRID, maxSize, false);
            b.recGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
        b.recGrid.setAdapter(adapterGrid);
    }

    @Override
    public void onSearched(String text) {
        RequestCard requestCard = new RequestCard();
        requestCard.setCategoryIds(categoryIds);
        requestCard.setDescending(true);
        requestCard.setPageNumber(page);
        requestCard.setTake(limit);
        requestCard.setSearch(text);

        if (text.length() == 0) {
            b.recGrid.setVisibility(View.INVISIBLE);
        } else b.recGrid.setVisibility(View.VISIBLE);

        check(requestCard);
    }
}