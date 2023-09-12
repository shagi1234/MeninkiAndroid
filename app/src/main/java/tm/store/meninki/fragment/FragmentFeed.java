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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.ResponsePostGetAllItem;
import tm.store.meninki.api.enums.CardType;
import tm.store.meninki.api.request.RequestCard;
import tm.store.meninki.api.data.response.ResponseCard;
import tm.store.meninki.databinding.FragmentFeedBinding;
import tm.store.meninki.interfaces.OnSearched;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentFeed extends Fragment implements OnSearched {
    private FragmentFeedBinding b;
    private int page = 1;
    private int limit = 20;
    private AdapterGrid adapterGrid;
    private boolean isLastPage;
    private boolean isSearch;

    public static FragmentFeed newInstance(boolean isSearch) {
        FragmentFeed fragment = new FragmentFeed();
        Bundle args = new Bundle();
        args.putBoolean("is_search", isSearch);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isSearch = getArguments().getBoolean("is_search");
        }
        Log.e("TAG_fragment", "onCreate: ");
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
        if (isSearch) {
            onSearched(editSearch.getText().toString().trim());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentFeedBinding.inflate(inflater, container, false);
        setProgress(true);
        checkUi();
        setRecycler();
        initListeners();
        RequestCard requestCard = new RequestCard();
        requestCard.setDescending(true);
        requestCard.setPageNumber(page);
        requestCard.setTake(limit);

        check(requestCard);

        return b.getRoot();
    }

    private void checkUi() {
        if (isSearch) {
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

    private void initListeners() {
        b.swipeLayout.setOnRefreshListener(() -> {
            RequestCard requestCard = new RequestCard();
            requestCard.setDescending(true);
            requestCard.setPageNumber(page);
            requestCard.setTake(limit);
            check(requestCard);
            b.swipeLayout.setRefreshing(false);
        });
    }

    private void check(RequestCard requestCard) {
        Log.e("TAG_token", "check: " + Account.newInstance(getContext()).getAccessToken());

        Call<ArrayList<ResponsePostGetAllItem>> call = StaticMethods.getApiHome().getAllPosts(requestCard);
        call.enqueue(new RetrofitCallback<ArrayList<ResponsePostGetAllItem>>() {
            @Override
            public void onResponse(ArrayList<ResponsePostGetAllItem> response) {
                b.progressBar.setVisibility(View.GONE);
                if (response == null || response.size() == 0) {
                    isLastPage = true;
                    if (page == 1) {
                        //showNoContent
                        b.recGrid.setVisibility(View.GONE);
                        b.noContent.setVisibility(View.VISIBLE);
                    }
                    return;
                }
                b.recGrid.setVisibility(View.VISIBLE);
                b.noContent.setVisibility(View.GONE);
                adapterGrid.setPosts(response);
                setProgress(false);

            }

            @Override
            public void onFailure(Throwable t) {
                setProgress(false);
                //showNoConnection
                b.recGrid.setVisibility(View.GONE);
                b.noContent.setVisibility(View.VISIBLE);
                b.noContent.setText(R.string.no_connection);
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

        b.recGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) b.recGrid.getLayoutManager();
                if (lm == null) return;

                int visibleItemCount = lm.getChildCount();
                int totalItemCount = lm.getItemCount();
//                int pastVisibleItems = lm.findFirstVisibleItemPositions();
//
//                if ((visibleItemCount + pastVisibleItems) >= totalItemCount && !isLastPage) {
//                    page++;
//                    check();
//                }
            }
        });
    }


    @Override
    public void onSearched(String text) {
        RequestCard requestCard = new RequestCard();
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