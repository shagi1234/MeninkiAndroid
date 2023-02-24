package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterGrid;
import tm.store.meninki.adapter.AdapterStore;
import tm.store.meninki.databinding.FragmentNoticeBinding;

public class FragmentNotice extends Fragment {
    private FragmentNoticeBinding b;
    private AdapterStore adapterStore;
    private AdapterGrid adapterGrid;
    private AdapterGrid adapterGridHorizontal;

    public static FragmentNotice newInstance() {
        FragmentNotice fragment = new FragmentNotice();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.containerProfileId, 0, 0, 0, navigationBarHeight);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentNoticeBinding.inflate(inflater, container, false);
        setBackground();
        setRecyclerGrid();
        setRecyclerHorizontal();

        adapterGrid.setStories(null);
        adapterGridHorizontal.setStories(null);

        return b.getRoot();
    }

    private void setRecyclerGrid() {
        adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_GRID);
        RecyclerView.LayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        b.recProducts.setLayoutManager(staggered);
        b.recProducts.setAdapter(adapterGrid);
    }

    private void setRecyclerHorizontal() {
        adapterGridHorizontal = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_HORIZONTAL_SMALL);
        b.recProductsHorizontal.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.recProductsHorizontal.setAdapter(adapterGridHorizontal);
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.btnCaption, R.color.grey, 0, 10, false, 0);
    }

}