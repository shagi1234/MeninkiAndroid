package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setMargins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.playerslidding.adapter.AdapterGrid;
import com.example.playerslidding.data.StoreDTO;
import com.example.playerslidding.databinding.FragmentListGridBinding;
import com.example.playerslidding.utils.StoreList;

import java.util.ArrayList;

public class FragmentListGrid extends Fragment {
    private FragmentListGridBinding b;
    private ArrayList<StoreDTO> stores = new ArrayList<>();
    private AdapterGrid adapterGrid;

    public static FragmentListGrid newInstance() {
        FragmentListGrid fragment = new FragmentListGrid();
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
//        setMargins(b.recGrid, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentListGridBinding.inflate(inflater, container, false);
        setRecycler();
        adapterGrid.setStories(StoreList.getGrids());
        return b.getRoot();
    }


    private void setRecycler() {
        adapterGrid = new AdapterGrid(getContext(), getActivity(),AdapterGrid.TYPE_GRID);
        RecyclerView.LayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        b.recGrid.setLayoutManager(staggered);
        b.recGrid.setAdapter(adapterGrid);
    }
}