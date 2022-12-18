package com.example.playerslidding.fragment;

import static com.example.playerslidding.utils.StaticMethods.navigationBarHeight;
import static com.example.playerslidding.utils.StaticMethods.setBackgroundDrawable;
import static com.example.playerslidding.utils.StaticMethods.setPadding;
import static com.example.playerslidding.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.playerslidding.R;
import com.example.playerslidding.databinding.FragmentFilterAndSortBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFilterAndSort#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFilterAndSort extends Fragment {

    private FragmentFilterAndSortBinding b;

    // TODO: Rename and change types and number of parameters
    public static FragmentFilterAndSort newInstance() {
        FragmentFilterAndSort fragment = new FragmentFilterAndSort();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentFilterAndSortBinding.inflate(inflater, container, false);
        setSpinners();
        setBackgrounds();
        return b.getRoot();
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.radio1, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.spinnner, R.color.white, R.color.hover, 4, false, 0);
        setBackgroundDrawable(getContext(), b.spinnerTwo, R.color.white, R.color.hover, 4, false, 0);
        setBackgroundDrawable(getContext(), b.radio2, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.radio3, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.maxMoney, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.minMoney, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.saveButton, R.color.accent, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.erase, R.color.white, 0, 4, false, 0);
    }

    private void setSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.spinnner.setAdapter(adapter);
        b.spinnerTwo.setAdapter(adapter);
    }
}