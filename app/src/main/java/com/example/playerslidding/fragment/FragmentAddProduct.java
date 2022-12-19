package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddProduct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddProduct extends Fragment implements OnBackPressedFragment {
    private FragmentAddProductBinding b;
    private AdapterMediaAddPost mediaAddPost;

    public static FragmentAddProduct newInstance(String param1, String param2) {
        FragmentAddProduct fragment = new FragmentAddProduct();
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
        b = FragmentAddProductBinding.inflate(inflater, container, false);
        setBackgrounds();
        setRecycler();
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