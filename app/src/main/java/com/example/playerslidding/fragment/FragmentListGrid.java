package com.example.playerslidding.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    private int orientation;
    public final static int VERTICAL_GRID = 0;
    public final static int HORIZONTAL_LINEAR = 1;

    public static FragmentListGrid newInstance(int orientation) {
        FragmentListGrid fragment = new FragmentListGrid();
        Bundle args = new Bundle();
        args.putInt("orientation", orientation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orientation = getArguments().getInt("orientation");
        }
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

        if (orientation == HORIZONTAL_LINEAR) {
            adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_HORIZONTAL);
            b.recGrid.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            adapterGrid = new AdapterGrid(getContext(), getActivity(), AdapterGrid.TYPE_GRID);
            b.recGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }

        b.recGrid.setAdapter(adapterGrid);
    }
}