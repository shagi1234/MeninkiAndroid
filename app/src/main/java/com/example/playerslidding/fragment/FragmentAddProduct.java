package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.logWrite;
import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setMargins;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.playerslidding.R;
import com.example.playerslidding.adapter.AdapterMediaAddPost;
import com.example.playerslidding.data.SelectedMedia;
import com.example.playerslidding.databinding.FragmentAddProductBinding;
import com.example.playerslidding.interfaces.OnBackPressedFragment;

public class FragmentAddProduct extends Fragment implements OnBackPressedFragment {
    private FragmentAddProductBinding b;
    private AdapterMediaAddPost mediaAddPost;

    public static FragmentAddProduct newInstance() {
        FragmentAddProduct fragment = new FragmentAddProduct();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        setMargins(b.layHeader, 0, statusBarHeight, 0, 0);
        setPadding(b.getRoot(), 0, 0, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentAddProductBinding.inflate(inflater, container, false);
        setBackgrounds();
        setRecycler();

        b.backBtn.setOnClickListener(v -> getActivity().onBackPressed());

        return b.getRoot();
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.title, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.desc, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.price, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.oldPrice, R.color.white, R.color.hover, 4, false, 1);
    }

    private void setRecycler() {
        mediaAddPost = new AdapterMediaAddPost(getContext());
        b.recMedia.setLayoutManager(new GridLayoutManager(getContext(), 2));
        b.recMedia.setAdapter(mediaAddPost);
    }

    @Override
    public boolean onBackPressed() {
        SelectedMedia.getArrayList().clear();
        return false;
    }
}