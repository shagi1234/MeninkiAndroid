package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.getWindowHeight;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setPadding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playerslidding.adapter.AdapterVerticalImagePager;
import com.example.playerslidding.databinding.FragmentProductBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class FragmentProduct extends Fragment {
    private FragmentProductBinding b;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private ArrayList<String> s = new ArrayList<>();
    private String uuid;

    public static FragmentProduct newInstance(String uuid) {
        FragmentProduct fragment = new FragmentProduct();
        Bundle args = new Bundle();
        args.putString("uuid", uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString("uuid");
        }
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
        s.add("https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg");
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.coordinator, 0, 0, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentProductBinding.inflate(inflater, container, false);
        setBottomSheet();
        setViewPager();
        getProductData();
        return b.getRoot();
    }

    private void setViewPager() {
        AdapterVerticalImagePager adapterVerticalImagePager = new AdapterVerticalImagePager(getContext());

        b.imagePager.setClipToPadding(false);
        b.imagePager.setClipChildren(false);
        b.imagePager.setOffscreenPageLimit(3);
        b.imagePager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        b.imagePager.setAdapter(adapterVerticalImagePager);
        adapterVerticalImagePager.setImageList(s);

    }

    private void setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(b.bottomSheet.root);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(getWindowHeight(getActivity()) / 3);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    private void getProductData() {

    }
}